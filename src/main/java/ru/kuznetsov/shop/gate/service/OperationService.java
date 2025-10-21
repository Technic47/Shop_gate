package ru.kuznetsov.shop.gate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.kuznetsov.shop.gate.enums.OperationType;
import ru.kuznetsov.shop.represent.dto.AbstractDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static ru.kuznetsov.shop.gate.enums.OperationType.SAVE;

@Service
public class OperationService {

    @Value("${operation.timeout}")
    private long waitingForOperationTime;

    @Getter
    private final Map<Operation, List<OperationDataContainer>> operations = new HashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(OperationService.class);

    public boolean containsOperation(String operationId) {
        return operations.keySet().stream()
                .map(Operation::getId)
                .anyMatch(operationId::equals);
    }

    public Optional<Operation> getOperation(String operationId) {
        return operations.keySet().stream()
                .filter(operation -> operation.getId().equals(operationId))
                .findFirst();
    }

    public List<OperationDataContainer> getOperationData(Operation operation) {
        return operations.get(operation);
    }

    public void removeOperations(List<Operation> operationIds) {
        for (Operation operation : operationIds) {
            removeOperation(operation);
        }
    }

    public void removeOperation(Operation operation) {
        operations.remove(operation);
    }

    public <E extends AbstractDto> void putToSuccessfulOperations(String objectJson,
                                                                  byte[] operationId,
                                                                  Class<E> clazz,
                                                                  OperationType type) {
        logger.info("Adding {} to successfully save operation with operationId: {}", objectJson, operationId);

        putOperationToMap(objectJson, operationId, clazz, type, 1);
    }

    public <E extends AbstractDto> void putToFailedOperations(String objectJson,
                                                              byte[] operationId,
                                                              Class<E> clazz,
                                                              OperationType type) {
        logger.info("Adding {} to failed save operation with operationId: {}", objectJson, operationId);

        putOperationToMap(objectJson, operationId, clazz, type, 0);
    }


    private <E extends AbstractDto> void putOperationToMap(
            String objectJson,
            byte[] operationId,
            Class<E> clazz,
            OperationType type,
            int result) {

        String operationIdDecoded = new String(operationId);

        try {
            Long id = objectMapper.readValue(objectJson, clazz).getId();
            Operation operation = new Operation(operationIdDecoded, type, result);
            OperationDataContainer container = new OperationDataContainer(id, LocalDateTime.now());

            if (operations.containsKey(operation)) {
                operations.get(operation).add(container);
            } else {
                List<OperationDataContainer> containers = new ArrayList<>();
                containers.add(container);
                operations.put(operation, containers);
            }
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void removeOldOperations() {
        for (Map.Entry<Operation, List<OperationDataContainer>> entry : operations.entrySet()) {
            entry.getValue().stream()
                    .max(Comparator.comparing(OperationDataContainer::getDateTime))
                    .ifPresent(container -> {
                        if (container.getDateTime().isBefore(LocalDateTime.now().minusHours(1))) {
                            removeOperation(entry.getKey());
                        }
                    });
        }
    }

    public List<Long> getEntityIdsByOperationId(String operationId) {
        CompletableFuture<Operation> operationWithData = waitForOperation(operationId);
        List<Long> entityIds;

        try {
            Operation operation = operationWithData.orTimeout(waitingForOperationTime, TimeUnit.SECONDS).get();
            List<OperationDataContainer> operationData = getOperationData(operation);
            OperationType operationType = operation.getType();
            int result = operation.getResult();

            if (operationType != SAVE)
                throw new RuntimeException("Invalid operation type received for operation " + operationId);
            if (result != 1) throw new RuntimeException("Operation is not success. Id " + operationId);

            entityIds = operationData.stream()
                    .map(OperationDataContainer::getPayloadId)
                    .toList();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return entityIds;
    }

    private CompletableFuture<Operation> waitForOperation(String operationId) {
        return CompletableFuture.supplyAsync(() -> {
            while ((!containsOperation(operationId))) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return getOperation(operationId).orElseThrow(() -> new RuntimeException("Operation with id " + operationId + " not found"));
        });
    }
}
