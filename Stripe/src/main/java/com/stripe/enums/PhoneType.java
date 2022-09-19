package com.stripe.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.stripe.util.EnumUtils;

public enum  PhoneType {
    MOBILE, WORK, HOME;

    @JsonCreator
    public static PhoneType fromValue(String value) {
        return EnumUtils.getEnumFromString(PhoneType.class, value);
    }

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
