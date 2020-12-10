# Spring Cloud GCP PubSub Getting Started

Cloud Pub/Sub is an asynchronous messaging service that decouples services that produce events from services that process events.
Spring Cloud GCP for Pub/Sub is an abstraction allows Spring to use Cloud Pub/Sub without depending on any Cloud Pub/Sub API.

## Description
### Dependencies
- org.springframework.cloud
  - spring-cloud-gcp-starter-pubsub

### Operate Cloud PubSub by gcloud CLI
#### Enable Cloud PubSub
```shell script
$ gcloud services enable pubsub.googleapis.com
```

#### Create Topic
```shell script
$ gcloud pubsub topics create messages
```

#### Confirm Topic
```shell script
$ gcloud pubsub topics list
```

#### Create Subscription
```shell script
$ gcloud pubsub subscriptions create messages-subscription --topic messages
```

#### Confirm Subscription
```shell script
$ gcloud pubsub subscriptions list
```

#### Publish Message
```shell script
$ gcloud pubsub topics publish messages --message="hello" \
  --attribute="origin=gcloud-sample,username=gcp,publishTime="(date +%Y%m%d-%H%M%S)""
```

#### Pull Messages
```shell script
$ gcloud pubsub subscriptions pull messages-subscription --auto-ack --limit=10
```

### PubSubAdmin
`PubSubAdmin` is the abstraction provided by Spring Cloud GCP to manage Google Cloud Pub/Sub resources.
It allows for the **creation**, **deletion** and **listing** of topics and subscriptions.

#### Autowire PubSubAdmin
```kotlin
class AdminController(val pubSubAdmin: PubSubAdmin)
```

#### Create Topic
- `createTopic(String topicName): Topic`

```kotlin
fun createTopic(@RequestParam topicName: String) {
    pubSubAdmin.createTopic(topicName)
}
```

#### Create Subscription
- `createSubscription(String subscriptionName, String topicName): Subscription`
- `createSubscription(String subscriptionName, String topicName, Integer ackDeadline) :Subscription`
- `createSubscription(String subscriptionName, String topicName, Integer ackDeadline, String pushEndpoint): Subscription`

```kotlin
fun createSubscription(@RequestParam subscriptionName: String,
                       @RequestParam topicName: String) {
    pubSubAdmin.createSubscription(subscriptionName, topicName)
}
```

#### List Topics
- `listTopics(): List<Topic>`

#### List Subscriptions
- `listSubscriptions(): List<Subscription>`

#### Delete Topic
- `deleteTopic(String topicName)`

```kotlin
fun deleteTopic(@RequestParam topicName: String) {
    pubSubAdmin.deleteTopic(topicName)
}
```

#### Delete Subscription
- `deleteSubscription(String subscriptionName)`

## Demo
### Authenticate with Google Cloud
```shell script
$ gcloud auth application-default login
```

#### Updated Authentication Credential
```shell script
$ cat ~/.config/gcloud/application_default_credentials.json
```

### Create Topic
```shell script
$ curl -X POST -H "Content-Type: application/x-www-form-urlencoded" -d "topicName=messages" localhost:8080/topic
```

### Create Subscription
```shell script
$ curl -X POST -H "Content-Type: application/x-www-form-urlencoded" -d "topicName=messages&subscriptionName=messages-subscription" localhost:8080/subscription
```

### Publish Message
```shell script
$ curl -X POST -H "Content-Type: application/x-www-form-urlencoded" \
  -d "topicName=messages&message="MSG:(date +%Y%m%d-%H%M%S)"" localhost:8080/message
```

### Pull and Ack Messages
```shell script
$ curl -X GET "localhost:8080/message?subscriptionName=messages-subscription"
```

### Delete Subscription
```shell script
$ curl -X DELETE "localhost:8080/message?subscriptionName=messages-subscription"
```

### Delete Topic
```shell script
$ curl -X DELETE "localhost:8080/topic?topicName=messages"
```

## Features

- feature:1
- feature:2

## Requirement

## Usage

## Installation

## Licence

Released under the [MIT license](https://gist.githubusercontent.com/shinyay/56e54ee4c0e22db8211e05e70a63247e/raw/34c6fdd50d54aa8e23560c296424aeb61599aa71/LICENSE)

## Author

[shinyay](https://github.com/shinyay)
