plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
}

dependencies {
    // Spring Boot Actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // Spring Boot Devtools
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    // Spring Boot Validation
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // Spring Cloud Config
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // Jackson
    implementation("tools.jackson.module:jackson-module-kotlin")
    // JWT
    api(libs.jjwt.api)
    runtimeOnly(libs.jjwt.impl)
    runtimeOnly(libs.jjwt.jackson)
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
    testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}