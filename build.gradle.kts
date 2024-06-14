import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "1.2.70"
val log4jVersion = "[2.6, 2.999.999)"

plugins {
    id("com.atlassian.performance.tools.gradle-release").version("0.10.0")
    kotlin("jvm").version("1.3.20")
    `java-library`
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        languageVersion = "1.4"
        languageVersion = "1.2" // the maximum, which still produces 1.1.x metadata required by 1.2.70 kotlin clients
    }
}

configurations.all {
    if (name.startsWith("kotlin") || name.startsWith("dokka")) {
        return@all
    }
    resolutionStrategy {
        activateDependencyLocking()
        failOnVersionConflict()
    }
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    api("com.github.stephenc.jcip:jcip-annotations:1.0-1")
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    testImplementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    testImplementation("junit:junit:4.12")
    testImplementation("org.assertj:assertj-core:3.12.2")
}

tasks.wrapper {
    gradleVersion = "7.6.3"
    distributionType = Wrapper.DistributionType.ALL
}
