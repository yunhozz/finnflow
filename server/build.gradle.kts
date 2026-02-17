plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.spring.dependency.management)
}

allprojects {
    group = "com.finnflow"
    version = "0.0.1-SNAPSHOT"
    description = "Project for Finnflow Server"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin(rootProject.libs.plugins.kotlin.jvm.get().pluginId)
        plugin(rootProject.libs.plugins.spring.dependency.management.get().pluginId)
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(24)
        }
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
        }
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${rootProject.libs.versions.spring.cloud.get()}")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}