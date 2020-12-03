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
        pubSubAdmin.createTopic(topicName)
        logger.info("Create Topic: $topicName")
    }
}