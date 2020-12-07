package com.google.shinyay

import com.google.cloud.ServiceOptions
import com.google.cloud.pubsub.v1.SubscriptionAdminClient
import com.google.cloud.pubsub.v1.TopicAdminClient
import com.google.pubsub.v1.*
import org.assertj.core.api.Assertions
import org.awaitility.kotlin.await
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.web.util.UriComponentsBuilder
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import java.util.stream.StreamSupport

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringCloudGcpPubsubGsApplicationTests() {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate
    lateinit var projectName: String
    lateinit var topicAdminClient: TopicAdminClient
    lateinit var subscriptionAdminClient: SubscriptionAdminClient
    lateinit var baseUrl: String
    val defaultTopicName = "default-topic"
    val defaultSubscriptionName = "defaul-subscription"
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

    @Test
    @Order(1)
    fun createTopicByController() {
        val testTopicName = "test-topic"
        val expectedTopicName = ProjectTopicName.format(projectName, testTopicName)
        val url = UriComponentsBuilder.fromHttpUrl("$baseUrl/topic")
                .queryParam("topicName", testTopicName)
                .toUriString()
        testRestTemplate.postForEntity(url, null, String::class.java)
        await.atMost(30, TimeUnit.SECONDS).untilAsserted{
            Assertions.assertThat(getTopicNamesFromProject()).contains(expectedTopicName)
        }
    }

    @Test
    @Order(2)
    fun createSubscriptionByController() {
        val testTopicName = "test-topic"
        val testSubscriptionName = "test-subscription"
        val expectedSubscriptionName = ProjectSubscriptionName.format(projectName, testSubscriptionName)
        val url = UriComponentsBuilder.fromHttpUrl("$baseUrl/subscription")
                .queryParam("subscriptionName", testSubscriptionName)
                .queryParam("topicName", testTopicName)
                .toUriString()
        testRestTemplate.postForEntity(url, null, String::class.java)
        await.atMost(30, TimeUnit.SECONDS).untilAsserted{
            Assertions.assertThat(getSubscriptionNamesFromProject()).contains(expectedSubscriptionName)
        }
    }

    @Test
    @Order(3)
    fun deleteSubscriptionByController() {
        val testSubscriptionName = "test-subscription"
        val expectedSubscriptionName = ProjectSubscriptionName.format(projectName, testSubscriptionName)
        val url = UriComponentsBuilder.fromHttpUrl("$baseUrl/subscription")
                .queryParam("subscriptionName", testSubscriptionName)
                .toUriString()
        testRestTemplate.delete(url)
        await.atMost(30, TimeUnit.SECONDS).untilAsserted {
            Assertions.assertThat(getTopicNamesFromProject()).doesNotContain(expectedSubscriptionName)
        }
    }

    @Test
    @Order(3)
    fun deleteTopicByController() {
        val testTopicName = "test-topic"
        val expectedTopicName = ProjectTopicName.format(projectName, testTopicName)
        val url = UriComponentsBuilder.fromHttpUrl("$baseUrl/topic")
                .queryParam("topicName", testTopicName)
                .toUriString()
        testRestTemplate.delete(url)
        await.atMost(30, TimeUnit.SECONDS).untilAsserted{
            Assertions.assertThat(getTopicNamesFromProject()).doesNotContain(expectedTopicName)
        }
    }
}
