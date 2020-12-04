package com.google.shinyay

import com.google.cloud.ServiceOptions
import com.google.cloud.pubsub.v1.SubscriptionAdminClient
import com.google.cloud.pubsub.v1.TopicAdminClient
import com.google.pubsub.v1.ProjectName
import com.google.pubsub.v1.PushConfig
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.event.annotation.BeforeTestClass

@SpringBootTest
class SpringCloudGcpPubsubGsApplicationTests {

	lateinit var projectName:String
	lateinit var topicAdminClient: TopicAdminClient
	lateinit var subscriptionAdminClient: SubscriptionAdminClient
	val topicName = "test-topic"

	@BeforeTestClass
	fun prepare() {
		projectName = ProjectName.of(ServiceOptions.getDefaultProjectId()).project
		topicAdminClient = TopicAdminClient.create()
		subscriptionAdminClient = SubscriptionAdminClient.create()

		topicAdminClient.createTopic(topicName)
		subscriptionAdminClient.createSubscription(projectName, topicName, PushConfig.getDefaultInstance(), 10)
	}

	@Test
	fun contextLoads() {
	}

}
