package ru.kuznetsov.shop.gate.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.represent.dto.*;

import java.time.LocalDateTime;
import java.util.*;

import static ru.kuznetsov.shop.represent.common.KafkaConst.*;

@Component
public class SaveOperationListener {

    private final Map<String, List<OperationContainer>> successfulOperations = new HashMap<>();
    private final Map<String, List<OperationContainer>> failedOperations = new HashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    Logger logger = LoggerFactory.getLogger(SaveOperationListener.class);

    @KafkaListener(topics = ADDRESS_SAVE_SUCCESSFUL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addAddressToSuccessful(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        putToSuccessfulOperations(dto, operationId, AddressDto.class);
    }

    @KafkaListener(topics = ADDRESS_SAVE_FAIL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addAddressToFail(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        putToFailedOperations(dto, operationId, AddressDto.class);
    }

    @KafkaListener(topics = PRODUCT_SAVE_SUCCESSFUL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addProductToSuccessful(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        putToSuccessfulOperations(dto, operationId, ProductDto.class);
    }

    @KafkaListener(topics = PRODUCT_SAVE_FAIL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addProductToFailed(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        putToFailedOperations(dto, operationId, ProductDto.class);
    }

    @KafkaListener(topics = PRODUCT_CATEGORY_SAVE_SUCCESSFUL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addProductCategoryToSuccessful(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        putToSuccessfulOperations(dto, operationId, ProductCategoryDto.class);
    }

    @KafkaListener(topics = PRODUCT_CATEGORY_SAVE_FAIL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addProductCategoryToFailed(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        putToFailedOperations(dto, operationId, ProductCategoryDto.class);
    }

    @KafkaListener(topics = STOCK_SAVE_SUCCESSFUL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addStockToSuccessful(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        putToSuccessfulOperations(dto, operationId, StockDto.class);
    }

    @KafkaListener(topics = STOCK_SAVE_FAIL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addStockToFailed(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        putToFailedOperations(dto, operationId, StockDto.class);
    }

    @KafkaListener(topics = STORE_SAVE_SUCCESSFUL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addStoreToSuccessful(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        putToSuccessfulOperations(dto, operationId, StoreDto.class);
    }

    @KafkaListener(topics = STORE_SAVE_FAIL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addStoreToFailed(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        putToFailedOperations(dto, operationId, StoreDto.class);
    }

    public Map<String, List<LocalDateTime>> getSuccessfulOperationsWithTime() {
        return getOperationsWithTime(successfulOperations);
    }

    public Map<String, List<LocalDateTime>> getFailedOperationsWithTime() {
        return getOperationsWithTime(failedOperations);
    }

    private Map<String, List<LocalDateTime>> getOperationsWithTime(Map<String, List<OperationContainer>> operationMap) {
        Map<String, List<LocalDateTime>> returnMap = new HashMap<>();

        for (Map.Entry<String, List<OperationContainer>> entry : operationMap.entrySet()) {
            List<LocalDateTime> operationTimes = entry.getValue().stream()
                    .map(container -> container.dateTime)
                    .toList();

            returnMap.put(entry.getKey(), operationTimes);
        }

        return returnMap;
    }

    public void removeSuccessfulOperations(List<String> keys) {
        for (String key : keys) {
            successfulOperations.remove(key);
        }
    }

    public void removeFailedOperations(List<String> keys) {
        for (String key : keys) {
            failedOperations.remove(key);
        }
    }

    private <E extends AbstractDto> void putToSuccessfulOperations(String objectJson, byte[] operationId, Class<E> clazz) {
        logger.info("Adding {} to successfully save operation with operationId: {}", objectJson, operationId);

        putOperationToMap(objectJson, operationId, clazz, successfulOperations);
    }

    private <E extends AbstractDto> void putToFailedOperations(String objectJson, byte[] operationId, Class<E> clazz) {
        logger.info("Adding {} to failed save operation with operationId: {}", objectJson, operationId);

        putOperationToMap(objectJson, operationId, clazz, failedOperations);
    }

    private <E extends AbstractDto> void putOperationToMap(
            String objectJson,
            byte[] operationId,
            Class<E> clazz,
            Map<String, List<OperationContainer>> operationMap) {

        String operationIdDecoded = new String(operationId);

        try {
            Long id = objectMapper.readValue(objectJson, clazz).getId();
            OperationContainer container = new OperationContainer(id, LocalDateTime.now());

            if (operationMap.containsKey(operationIdDecoded)) {
                operationMap.get(operationIdDecoded).add(container);
            } else {
                List<OperationContainer> containers = new ArrayList<>();
                containers.add(container);
                operationMap.put(operationIdDecoded, containers);
            }
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Data
    @Getter
    @AllArgsConstructor
    private static class OperationContainer {
        private Long id;
        private LocalDateTime dateTime;
    }
}
