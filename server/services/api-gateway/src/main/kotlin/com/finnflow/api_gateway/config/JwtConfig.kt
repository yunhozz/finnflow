package com.finnflow.api_gateway.config

import com.finnflow.common.props.JwtProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig {

    @Bean
    @ConfigurationProperties(prefix = "app.jwt")
    fun jwtProperties() = JwtProperties()
}