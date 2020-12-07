package com.google.shinyay

import com.google.cloud.ServiceOptions
import com.google.cloud.pubsub.v1.SubscriptionAdminClient
import com.google.cloud.pubsub.v1.TopicAdminClient
import com.google.pubsub.v1.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.web.util.UriComponentsBuilder
import java.util.stream.Collectors
import java.util.stream.StreamSupport

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringCloudGcpPubsubGsApplicationTests(val testRestTemplate: TestRestTemplate) {

    lateinit var projectName: String
    lateinit var topicAdminClient: TopicAdminClient
    lateinit var subscriptionAdminClient: SubscriptionAdminClient
    lateinit var baseUrl: String
    val defaultTopicName = "test-topic"
    val defaultSubscriptionName = "test-subscription"
    @LocalServerPort
    var testPort = 0

    @BeforeAll
    fun initAll() {
        projectName = ProjectName.of(ServiceOptions.getDefaultProjectId()).project
        topicAdminClient = TopicAdminClient.create()
        subscriptionAdminClient = SubscriptionAdminClient.create()
        baseUrl = "http://localhost:$testPort"

        createTopics(defaultTopicName)
        createSubscriptions(defaultSubscriptionName, defaultTopicName)
    }

    private fun createTopics(vararg topicNames: String) {
        topicNames.forEach {
            if (getTopicNamesFromProject()?.contains(it) == true) {
                topicAdminClient.createTopic(TopicName.of(projectName, it))
                logger.info("Created Topic: $it")
            } else {
                logger.info("Already Existed: $it")
            }
        }
    }

    private fun createSubscriptions(subscriptionName: String, topicName: String) {
        if (getSubscriptionNamesFromProject()?.contains(subscriptionName) == true) {
            subscriptionAdminClient.createSubscription(
                    ProjectSubscriptionName.of(projectName, subscriptionName),
                    TopicName.of(projectName, topicName),
                    PushConfig.getDefaultInstance(),
                    10)
            logger.info("Created Subscription: $subscriptionName")
        } else {
            logger.info("Already Existed: $subscriptionName")
        }
    }

    @AfterAll
    fun cleanupPubsubClients() {
        deleteSubscriptions(defaultSubscriptionName)
        subscriptionAdminClient.close()
        deleteTopics(defaultTopicName)
        topicAdminClient.close()
    }

    private fun deleteSubscriptions(vararg subscriptionNames: String) {
		for (subscription in subscriptionNames) {
			val testSubscriptionName = ProjectSubscriptionName.format(
                    projectName, subscription)
			val projectSubscriptions: MutableList<Any>? = getSubscriptionNamesFromProject()
			if (projectSubscriptions?.contains(testSubscriptionName) == true) {
				subscriptionAdminClient.deleteSubscription(testSubscriptionName)
                logger.info("Deleted Subscription: $testSubscriptionName")
			}
		}
	}

	private fun getSubscriptionNamesFromProject(): MutableList<Any>? {
		val response = subscriptionAdminClient.listSubscriptions("projects/$projectName")
		return StreamSupport.stream(response.iterateAll().spliterator(), false)
				.map(Subscription::getName)
				.collect(Collectors.toList())
	}

    private fun deleteTopics(vararg topicNames: String) {
        for (topic in topicNames) {
            val testTopicName = ProjectTopicName.format(projectName, topic)
            val projectTopics: MutableList<Any>? = getTopicNamesFromProject()
            if (projectTopics?.contains(testTopicName) == true) {
                topicAdminClient.deleteTopic(testTopicName)
                logger.info("Deleted Topic: $testTopicName")
            }
        }
    }

    private fun getTopicNamesFromProject(): MutableList<Any>? {
        val response = topicAdminClient.listTopics("projects/$projectName")
        return StreamSupport.stream(response.iterateAll().spliterator(), false)
                .map(Topic::getName)
                .collect(Collectors.toList())
    }

    private fun createTopicByController(topicName: String) {
        val url = UriComponentsBuilder.fromHttpUrl("/topic")
    }

    @Test
    fun contextLoads() {
    }

}
