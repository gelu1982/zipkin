/*
 * Copyright 2012 Twitter Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.redis.{Redis, Client}
import com.twitter.zipkin.builder.Scribe
import com.twitter.zipkin.redis
import com.twitter.zipkin.collector.builder.CollectorServiceBuilder
import com.twitter.zipkin.storage.Store

val client = Client(ClientBuilder().hosts("0.0.0.0:6379")
                                   .hostConnectionLimit(4)
                                   .hostConnectionCoresize(4)
                                   .codec(Redis())
                                   .build())

val redisBuilder = Store.Builder(
    redis.StorageBuilder(client),
    redis.IndexBuilder(client)
)

CollectorServiceBuilder(Scribe.Interface(categories = Set("zipkin")))
  .writeTo(redisBuilder)