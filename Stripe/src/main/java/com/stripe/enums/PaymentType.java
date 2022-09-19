package com.stripe.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.stripe.util.EnumUtils;
import org.springframework.util.StringUtils;

public enum PaymentType {

    CREDIT_CARD, WIRE_TRANSFER, CHECK;

    @JsonCreator
    public static PaymentType fromValue(String value) {
        return EnumUtils.getEnumFromString(PaymentType.class, value.toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return StringUtils.capitalize(name());
    }
}
