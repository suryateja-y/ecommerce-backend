package com.academy.projects.ecommerce.usermanagementservice.kafka.producer.user;

import com.academy.projects.ecommerce.usermanagementservice.dtos.ActionType;
import com.academy.projects.ecommerce.usermanagementservice.kafka.dtos.SellerDto;
import com.academy.projects.ecommerce.usermanagementservice.models.Seller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class SellerUpdateManager implements ISellerUpdateManager {
    private final KafkaTemplate<String, SellerDto> sellerKafkaTemplate;

    private final Logger logger = LoggerFactory.getLogger(SellerUpdateManager.class);
    @Value("${application.kafka.topics.seller-update-topic}")
    private String sellerUpdateTopic;

    public SellerUpdateManager(KafkaTemplate<String, SellerDto> sellerKafkaTemplate) {
        this.sellerKafkaTemplate = sellerKafkaTemplate;
    }

    @Override
    public void sendRegistration(Seller seller) {
        sellerKafkaTemplate.send(sellerUpdateTopic, from(seller, ActionType.CREATE));
        logger.info("Sent Seller Registration to the Observers. Seller: '{}'!!!", seller.getId());
    }

    @Override
    public void sendUpdate(Seller seller) {
        sellerKafkaTemplate.send(sellerUpdateTopic, from(seller, ActionType.UPDATE));
        logger.info("Sent Seller Update to the Observers. Seller: '{}'!!!", seller.getId());
    }

    @Override
    public void sendAddressUpdate(Seller seller) {
        sellerKafkaTemplate.send(sellerUpdateTopic, from(seller, ActionType.DETAILS_UPDATE));
        logger.info("Sent Seller Address Update to the Observers. Seller: '{}'!!!", seller.getId());
    }

    @Override
    public void sendStatusUpdate(Seller seller) {
        sellerKafkaTemplate.send(sellerUpdateTopic, from(seller, ActionType.STATUS_UPDATE));
        logger.info("Sent Seller Status Update to the Observers. Seller: '{}'!!!", seller.getId());
    }

    private SellerDto from(Seller seller, ActionType action) {
        return SellerDto.builder()
                .action(action)
                .seller(seller)
                .build();
    }
}
