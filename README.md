# Spring Cloud GCP PubSub Getting Started

Overview

## Description
### Dependencies
- org.springframework.cloud
  - spring-cloud-gcp-starter-pubsub

### Enable Cloud PubSub
```shell script
$ gcloud services enable pubsub.googleapis.com
```

### Create Topic
```shell script
$ gcloud pubsub topics create messages
```

#### Confirm Topic
```shell script
$ gcloud pubsub topics list
```

### Create Subscription
```shell script
$ gcloud pubsub subscriptions create messages-subscription --topic messages
```

## Demo

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
