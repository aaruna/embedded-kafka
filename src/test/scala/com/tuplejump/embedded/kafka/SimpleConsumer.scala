/*
 * Copyright 2016 Tuplejump
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tuplejump.embedded.kafka

import java.util.Properties
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

import kafka.serializer.StringDecoder
import kafka.consumer.{ Consumer, ConsumerConfig }

import scala.util.Try

/**
 * Very simple consumer of a single Kafka topic.
 * TODO update to new consumer.
 */
class SimpleConsumer(
    zookeeper: String,
    topic: String,
    groupId: String,
    partitions: Int,
    numThreads: Int,
    count: AtomicInteger
) {

  val connector = Consumer.create(createConsumerConfig)

  val streams = connector
    .createMessageStreams(Map(topic -> partitions), new StringDecoder(), new StringDecoder())
    .get(topic)

  val executor = Executors.newFixedThreadPool(numThreads)

  for (stream <- streams) {
    executor.submit(new Runnable() {
      def run() {
        for (s <- stream) {
          while (s.iterator.hasNext) {
            count.getAndIncrement
          }
        }
      }
    })
  }

  private def createConsumerConfig: ConsumerConfig = {
    val props = new Properties()
    props.put("consumer.timeout.ms", "2000")
    props.put("zookeeper.connect", zookeeper)
    props.put("group.id", groupId)
    props.put("zookeeper.session.timeout.ms", "400")
    props.put("zookeeper.sync.time.ms", "10")
    props.put("auto.commit.interval.ms", "1000")

    new ConsumerConfig(props)
  }

  def shutdown(): Unit = Try {
    connector.shutdown()
    executor.shutdown()
  }
}