package com.javacode.java_code_ta.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OperationType {
    DEPOSIT,
    WITHDRAW;

    @JsonCreator
    public static OperationType fromString(String operationType) {
        try {
            return OperationType.valueOf(operationType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid operation type: " + operationType);
        }
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }

}