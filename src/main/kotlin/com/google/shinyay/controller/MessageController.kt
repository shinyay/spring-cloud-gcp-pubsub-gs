package com.google.shinyay.controller

import com.google.cloud.pubsub.v1.Subscriber
import com.google.cloud.spring.pubsub.core.PubSubTemplate
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage
import com.google.shinyay.logger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class MessageController(val pubSubTemplate: PubSubTemplate) {

    @PostMapping("/message")
    fun publishMessage(@RequestParam topicName: String,
                       @RequestParam message: String) {
        logger.info("Published: $message")
        pubSubTemplate.publish(topicName, message)
    }

    @GetMapping("/message")
    fun viewMessages(@RequestParam subscriptionName: String): String {
        val messages = pubSubTemplate.pull(subscriptionName, 10, true)
        logger.info(messages.toString())
        messages.forEach { msg ->
            logger.info("ACK: ${msg.ackId}")
            msg.ack()
        }
        return """
            Pulled Message size: ${messages.size}
            ${messages.toString()}
        """.trimIndent()
    }

    @GetMapping("/subscription")
    fun subscribe(@RequestParam subscriptionName: String) {
        val subscriber: Subscriber = pubSubTemplate.subscribe(subscriptionName) { message: BasicAcknowledgeablePubsubMessage ->
            logger.info("Message received from " + subscriptionName + " subscription: "
                    + message.pubsubMessage.data.toStringUtf8())
            message.ack()
        }
    }
}