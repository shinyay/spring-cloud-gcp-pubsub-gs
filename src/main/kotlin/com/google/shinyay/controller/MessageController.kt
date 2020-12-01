package com.google.shinyay.controller

import com.google.cloud.spring.pubsub.core.PubSubTemplate
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageController(val pubSubTemplate: PubSubTemplate) {
}