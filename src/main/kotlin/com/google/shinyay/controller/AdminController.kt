package com.google.shinyay.controller

import com.google.cloud.spring.pubsub.PubSubAdmin
import com.google.pubsub.v1.Topic
import com.google.pubsub.v1.TopicName
import com.google.shinyay.logger
import org.springframework.web.bind.annotation.*
import java.util.function.Function
import java.util.stream.Collectors


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

    @GetMapping("/topic")
    fun listTopics(): List<String>? {
        return pubSubAdmin
                .listTopics()
                .stream()
                .map(Topic::getName)
                .collect(Collectors.toList<String>())
    }

    @DeleteMapping("/topic")
    fun deleteTopic(@RequestParam topicName: String) {
        pubSubAdmin.deleteTopic(topicName)
        logger.info("Deleted Topic: $topicName")
    }

    @DeleteMapping("/subscription")
    fun deleteSubscription(@RequestParam subscriptionName: String) {
        pubSubAdmin.deleteSubscription(subscriptionName)
        logger.info("Deleted Subscription: $subscriptionName")
    }
}