package com.stripe.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.stripe.util.EnumUtils;
import org.springframework.util.StringUtils;

public enum Status {

    NEW, ACTIVE, CANCELLED, CLOSED;

    @JsonCreator
    public static Status fromValue(String value) {
        return EnumUtils.getEnumFromString(Status.class, value.toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return StringUtils.capitalize(name());
    }
}
