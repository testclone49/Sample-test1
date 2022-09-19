package com.stripe.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.stripe.util.EnumUtils;
import org.springframework.util.StringUtils;

public enum CardCompany {

    VISA, AMEX, MASTERCARD, DISCOVER;

    @JsonCreator
    public static CardCompany fromValue(String value) {
        return EnumUtils.getEnumFromString(CardCompany.class, value.toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return StringUtils.capitalize(name());
    }
}
