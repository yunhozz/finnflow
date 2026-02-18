plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
}

dependencies {
    // Common Module
    implementation(project(":common"))
    // Spring Security
    implementation("org.springframework.boot:spring-boot-starter-security")
    // Reactive Gateway
    implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webflux")
    // Reactor
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-security-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
}