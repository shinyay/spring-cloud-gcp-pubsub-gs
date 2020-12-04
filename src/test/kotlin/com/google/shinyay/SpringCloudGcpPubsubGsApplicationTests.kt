package com.google.shinyay

import com.google.cloud.ServiceOptions
import com.google.cloud.pubsub.v1.SubscriptionAdminClient
import com.google.cloud.pubsub.v1.TopicAdminClient
import com.google.pubsub.v1.ProjectName
import com.google.pubsub.v1.ProjectTopicName
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.event.annotation.AfterTestClass

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class SpringCloudGcpPubsubGsApplicationTests {

	lateinit var projectName:String
	lateinit var topicAdminClient: TopicAdminClient
	lateinit var subscriptionAdminClient: SubscriptionAdminClient
	val topicName = "test-topic"

	@BeforeAll
	fun initAll() {
		projectName = ProjectName.of(ServiceOptions.getDefaultProjectId()).project
		topicAdminClient = TopicAdminClient.create()
		subscriptionAdminClient = SubscriptionAdminClient.create()

		topicAdminClient.createTopic(ProjectTopicName.of(projectName, topicName))
//		subscriptionAdminClient.createSubscription(projectName, topicName, PushConfig.getDefaultInstance(), 10)
	}

	@AfterTestClass
	fun cleanupPubsubClients() {

	}

//	private fun deleteSubscriptions(vararg testSubscriptions: String) {
//		for (testSubscription in testSubscriptions) {
//			val testSubscriptionName = ProjectSubscriptionName.format(
//					projectName, testSubscription)
//			val projectSubscriptions: List<String>? = getSubscriptionNamesFromProject()
//			if (projectSubscriptions.contains(testSubscriptionName)) {
//				subscriptionAdminClient.deleteSubscription(testSubscriptionName)
//			}
//		}
//	}
//	private fun getSubscriptionNamesFromProject(): List<String>? {
//		val response = subscriptionAdminClient.listSubscriptions("projects/$projectName")
//		return StreamSupport.stream(response.iterateAll().spliterator(), false)
//				.map<Any>(Subscription::getName)
//				.collect(Collectors.toList<Any>())
//	}
	@Test
	fun contextLoads() {
	}

}
