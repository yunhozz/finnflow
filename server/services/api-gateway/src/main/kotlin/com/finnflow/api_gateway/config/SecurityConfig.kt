package com.finnflow.api_gateway.config

import com.finnflow.api_gateway.filter.JwtFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun webFilterChain(
        http: ServerHttpSecurity,
        jwtFilter: JwtFilter
    ): SecurityWebFilterChain =
        http
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .addFilterBefore(jwtFilter, SecurityWebFiltersOrder.HTTP_BASIC)
            .authorizeExchange {
                it.pathMatchers("/api/**").permitAll() // TODO: 테스트를 위해 모두 허용
                it.anyExchange().authenticated()
            }
            .build()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource =
        UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", CorsConfiguration().apply {
                allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                allowedOriginPatterns = listOf("*")
                allowedHeaders = listOf("*")
            })
        }
}