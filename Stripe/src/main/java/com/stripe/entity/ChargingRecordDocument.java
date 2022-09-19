package com.stripe.entity;

import com.stripe.dto.ChargingResponseCustom;
import com.stripe.dto.SharedModal;
import com.stripe.dto.SystemPaymentInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Document(collection = "charging_record_document")
@Data
public class ChargingRecordDocument  implements Serializable {

    @Id
    private String chargingRecordId;
    private SystemPaymentInfo systemPaymentInfo;
    private ChargingResponseCustom chargingResponseCustom;
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date lastModifiedDate;


}
