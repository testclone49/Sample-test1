package com.stripe.repository;

import com.stripe.dto.ChargingResponseCustom;
import com.stripe.entity.ChargingRecordDocument;
import com.stripe.entity.StripeCustomer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChargingRecordDocumentRepository extends MongoRepository<ChargingRecordDocument,String> {

    @Query(value = "{'systemPaymentInfo.userId' :?0, 'systemPaymentInfo.activeStatus':'?1'}")
    Optional<ChargingRecordDocument> getChargingRecordBySystemUserIdAndActiveStatus(String userId, String activeStatus);

    @Query(value = "{'systemPaymentInfo.transactionExpiringStatus' :?0}")
    List<ChargingRecordDocument> findByTransactionExpiringStatus(String transactionExpiringStatus);

    @Query(value = "{'systemPaymentInfo.transactionExpiringStatus' :?0, 'systemPaymentInfo.activeStatus':'?1'}")
    List<ChargingRecordDocument> findByTransactionExpiringStatusAndDeactivateStatus(String transactionExpiringStatus,String activeStatus);

    @Query(value = "{'systemPaymentInfo.userId' :?0}")
    List<ChargingRecordDocument> findBySystemUserId(String systemUserID);
}
