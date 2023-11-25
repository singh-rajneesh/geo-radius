package com.project.georadiusservice.config.rabbit;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;


@Getter
@Setter
@UtilityClass
public class OrderIntentQueueNames {

    public static final String ORDER_INTENT_PROCESSING_QUEUE = "farmrise.order.intent.processing.queue";
    public static final String ORDER_PROCESSING_QUEUE = "farmrise.order.processing.queue";
    public static final String ORDER_INTENT_ADVISOR_REASSIGN_QUEUE = "farmrise.order.intent.advisor.reassign.queue";
    public static final String S3_DATA_PROCESSING_QUEUE = "farmrise.s3.data.processing.queue";
    public static final String S3_DATA_PROCESSING_QUEUE_DLQ = "farmrise.s3.data.processing.dlq.queue";
    public static final String ADVISOR_WHATSAPP_USER_CREATION_QUEUE = "whatsapp.advisor.user.creation.queue";
    public static final String WHATSAPP_EVENT_QUEUE = "whatsapp.event.queue";

    public static final String PURCHASE_FULFILLMENT_QUEUE = "farmrise.product.purchase.fulfillment.queue";

    public static final String PRODUCT_PURCHASED_QUEUE = "farmrise.product.purchased.queue";
    public static final String PURCHASED_REWARDS_QUEUE = "farmrise.purchased.rewards.queue";

    public static final String FARMER_PROFILE_IMAGE_DELETION_QUEUE = "farmrise.user.profile.image.deletion.queue";
}
