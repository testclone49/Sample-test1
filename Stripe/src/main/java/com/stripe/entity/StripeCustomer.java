package com.stripe.entity;

import com.stripe.model.*;
import com.stripe.dto.SharedModal;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.Map;

@Document(collection = "stripe_customer_document")
@Data
public class StripeCustomer  {

    @Id
    String mongoId;
    String stripeCustomerId;
    String userId;
    String object;
    Long accountBalance;
    String businessVatId;
    Long created;
    String currency;
    String defaultSource;
    Boolean deleted;
    Boolean delinquent;
    String description;
    Discount discount;
    String email;
    Boolean livemode;
    Map<String, String> metadata;
    ShippingDetails shipping;
    ExternalAccountCollection sources;
    CustomerSubscriptionCollection subscriptions;
    /** @deprecated */
    @Deprecated
    CustomerCardCollection cards;
    /** @deprecated */
    @Deprecated
    String defaultCard;
    /** @deprecated */
    @Deprecated
    Customer.NextRecurringCharge nextRecurringCharge;
    /** @deprecated */
    @Deprecated
    Subscription subscription;
    /** @deprecated */
    @Deprecated
    Long trialEnd;
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date lastModifiedDate;
}
