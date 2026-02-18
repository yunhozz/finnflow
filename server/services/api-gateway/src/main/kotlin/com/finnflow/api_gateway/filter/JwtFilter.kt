package com.finnflow.api_gateway.filter

import com.finnflow.common.props.JwtProperties
import com.finnflow.common.utils.logger
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.lang.Strings
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import javax.crypto.SecretKey

@Component
class JwtFilter(
    private val jwtProperties: JwtProperties
) : WebFilter {

    private val log = logger()

    private lateinit var secretKey: SecretKey

    @PostConstruct
    fun init() {
        val secretKeyBytes = jwtProperties.secretKey.toByteArray()
        secretKey = Keys.hmacShaKeyFor(secretKeyBytes)
    }

    data class CustomUserDetails(
        private val username: String,
        private val roles: List<String>
    ) : UserDetails {
        override fun getUsername() = username
        override fun getPassword() = null
        override fun getAuthorities() = roles
            .map { SimpleGrantedAuthority(it) }
            .toMutableSet()

        override fun isAccountNonExpired() = true
        override fun isAccountNonLocked() = true
        override fun isCredentialsNonExpired() = true
        override fun isEnabled() = true
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request
        log.info("Request URI: [{}] {}", request.method, request.uri)

        val headerToken = request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        log.debug("Header Token: $headerToken")

        return resolveToken(headerToken)?.let { token ->
            log.debug("Resolved token : $token")
            val claims = createClaimsJws(token)?.payload
                ?: throw RuntimeException()
            val roles = claims["roles"] as List<String>

            log.debug("claims : {}", claims)

            val userDetails = CustomUserDetails(claims.subject, roles)
            val authentication = UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)

            val mutatedExchange = exchange.mutate()
                .request(
                    request.mutate()
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .build()
                )
                .build()

            val securityContext = ReactiveSecurityContextHolder.withAuthentication(authentication)

            chain.filter(mutatedExchange).contextWrite(securityContext)

        } ?: chain.filter(exchange)
    }

    private fun resolveToken(token: String?): String? =
        token.takeIf { Strings.hasText(it) }?.let {
            val parts = it.split(" ")
            return if (parts.size == 2 && parts[0] == jwtProperties.tokenType)
                parts[1] else null
        }

    private fun createClaimsJws(token: String): Jws<Claims>? =
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)

        } catch (e: JwtException) {
            log.warn("[Invalid JWT Token] Exception Class: ${e.javaClass.simpleName}, Message: ${e.message}")
            null
        }
}