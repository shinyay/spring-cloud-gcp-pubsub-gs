package com.google.shinyay.controller

import com.google.cloud.spring.pubsub.core.PubSubTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

@RestController
class MessageController(val pubSubTemplate: PubSubTemplate) {

    @PostMapping("/message")
    fun publish(@RequestParam topicName: String,
                @RequestParam message: String): RedirectView {
        pubSubTemplate.publish(topicName, message)
        return RedirectView("/")
    }
}