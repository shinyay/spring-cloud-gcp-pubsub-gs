package com.google.shinyay.controller

import com.google.cloud.spring.pubsub.PubSubAdmin
import com.google.shinyay.logger
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminController(val pubSubAdmin: PubSubAdmin) {

    @PostMapping("/topic")
    fun createTopic(@RequestParam topicName: String) {
        val topic = pubSubAdmin.createTopic(topicName)
        logger.info("Created Topic: ${topic.name}")
    }

    @PostMapping("/subscription")
    fun createSubscription(@RequestParam subscriptionName: String,
                           @RequestParam topicName: String) {
        val subscription = pubSubAdmin.createSubscription(subscriptionName, topicName)
        logger.info("Created Subscription: ${subscription.name}")
    }

    @DeleteMapping("/topic")
    fun deleteTopic(@RequestParam topicName: String) {
        pubSubAdmin.deleteTopic(topicName)
        logger.info("Deleted: $topicName")
    }

    @DeleteMapping("/subscription")
    fun deleteSubscription(@RequestParam subscriptionName: String) {
        pubSubAdmin.deleteSubscription(subscriptionName)
        logger.info("Deleted: $subscriptionName")
    }
}