package com.finnflow.common.props

data class JwtProperties(
    var secretKey: String = "",
    var tokenType: String = "",
    var accessTokenValidTime: Long = 0,
    var refreshTokenValidTime: Long = 0
)