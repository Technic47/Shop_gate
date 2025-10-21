package ru.kuznetsov.shop.gate.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.kuznetsov.shop.gate.enums.OperationType;

@Data
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Operation {
    private String id;
    private OperationType type;
    private int result;
}
