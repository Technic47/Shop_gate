package ru.kuznetsov.shop.gate.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.gate.service.OperationService;
import ru.kuznetsov.shop.represent.dto.*;

import static ru.kuznetsov.shop.gate.enums.OperationType.SAVE;
import static ru.kuznetsov.shop.represent.common.KafkaConst.*;

@Component
@RequiredArgsConstructor
public class SaveOperationListener {

    private final OperationService operationService;

    @KafkaListener(topics = ADDRESS_SAVE_SUCCESSFUL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addAddressToSuccessful(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        operationService.putToSuccessfulOperations(dto, operationId, AddressDto.class, SAVE);
    }

    @KafkaListener(topics = ADDRESS_SAVE_FAIL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addAddressToFail(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        operationService.putToFailedOperations(dto, operationId, AddressDto.class, SAVE);
    }

    @KafkaListener(topics = PRODUCT_SAVE_SUCCESSFUL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addProductToSuccessful(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        operationService.putToSuccessfulOperations(dto, operationId, ProductDto.class, SAVE);
    }

    @KafkaListener(topics = PRODUCT_SAVE_FAIL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addProductToFailed(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        operationService.putToFailedOperations(dto, operationId, ProductDto.class, SAVE);
    }

    @KafkaListener(topics = PRODUCT_CATEGORY_SAVE_SUCCESSFUL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addProductCategoryToSuccessful(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        operationService.putToSuccessfulOperations(dto, operationId, ProductCategoryDto.class, SAVE);
    }

    @KafkaListener(topics = PRODUCT_CATEGORY_SAVE_FAIL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addProductCategoryToFailed(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        operationService.putToFailedOperations(dto, operationId, ProductCategoryDto.class, SAVE);
    }

    @KafkaListener(topics = STOCK_SAVE_SUCCESSFUL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addStockToSuccessful(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        operationService.putToSuccessfulOperations(dto, operationId, StockDto.class, SAVE);
    }

    @KafkaListener(topics = STOCK_SAVE_FAIL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addStockToFailed(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        operationService.putToFailedOperations(dto, operationId, StockDto.class, SAVE);
    }

    @KafkaListener(topics = STORE_SAVE_SUCCESSFUL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addStoreToSuccessful(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        operationService.putToSuccessfulOperations(dto, operationId, StoreDto.class, SAVE);
    }

    @KafkaListener(topics = STORE_SAVE_FAIL_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void addStoreToFailed(String dto, @Header(OPERATION_ID_HEADER) byte[] operationId) {
        operationService.putToFailedOperations(dto, operationId, StoreDto.class, SAVE);
    }
}
