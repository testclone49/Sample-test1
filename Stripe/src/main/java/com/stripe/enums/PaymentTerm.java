package com.stripe.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.stripe.util.EnumUtils;
import org.springframework.util.StringUtils;

public enum PaymentTerm {

    PRE_PAID, NET_30, NET_60, NET_90, NET_180;

    @JsonCreator
    public static PaymentTerm fromValue(String value) {
        return EnumUtils.getEnumFromString(PaymentTerm.class, value.toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return StringUtils.capitalize(name());
    }
}
