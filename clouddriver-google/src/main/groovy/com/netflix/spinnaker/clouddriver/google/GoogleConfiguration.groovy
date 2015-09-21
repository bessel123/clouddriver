/*
 * Copyright 2015 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.clouddriver.google

import com.netflix.spinnaker.clouddriver.google.config.GoogleConfigurationProperties
import com.netflix.spinnaker.clouddriver.google.health.GoogleHealthIndicator
import com.netflix.spinnaker.clouddriver.google.security.GoogleCredentialsInitializer
import com.netflix.spinnaker.clouddriver.google.util.ReplicaPoolBuilder
import com.netflix.spinnaker.clouddriver.google.util.ResourceViewsBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableConfigurationProperties
@EnableScheduling
@ConditionalOnProperty('google.enabled')
@ComponentScan(["com.netflix.spinnaker.clouddriver.google"])
@PropertySource(value = "classpath:META-INF/clouddriver-core.properties", ignoreResourceNotFound = true)
@Import([ GoogleCredentialsInitializer ])
class GoogleConfiguration {
  @Bean
  @ConfigurationProperties("google")
  GoogleConfigurationProperties googleConfigurationProperties() {
    new GoogleConfigurationProperties()
  }

  @Bean
  GoogleHealthIndicator googleHealthIndicator() {
    new GoogleHealthIndicator()
  }

  @Bean
  String applicationName(@Value('${Implementation-Version:Unknown}') String implementationVersion) {
    "Spinnaker/$implementationVersion"
  }

  @Bean
  ReplicaPoolBuilder replicaPoolBuilder(String applicationName) {
    new ReplicaPoolBuilder(applicationName)
  }

  @Bean
  ResourceViewsBuilder resourceViewsBuilder(String applicationName) {
    new ResourceViewsBuilder(applicationName)
  }
}

