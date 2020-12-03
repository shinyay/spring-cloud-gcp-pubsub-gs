package com.google.shinyay.controller

import com.google.cloud.spring.pubsub.PubSubAdmin
import com.google.shinyay.logger
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminController(val pubSubAdmin: PubSubAdmin) {

    @PostMapping("/topic")
    fun createTopic(@RequestParam topicName: String) {
        val topic = pubSubAdmin.createTopic(topicName)
        logger.info("Create Topic: ${topic.name}")
    }

    @PostMapping("/subscription")
    fun createSubscription(@RequestParam subscriptionName: String,
                           @RequestParam topicName: String) {
        val subscription = pubSubAdmin.createSubscription(subscriptionName, topicName)
        logger.info("Created: ${subscription.name}")
    }
}