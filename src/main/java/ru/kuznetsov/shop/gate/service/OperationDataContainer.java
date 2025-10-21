package ru.kuznetsov.shop.gate.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
@AllArgsConstructor
public class OperationDataContainer {
    private Long payloadId;
    private LocalDateTime dateTime;
}
