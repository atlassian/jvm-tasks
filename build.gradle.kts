import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "1.2.70"
val log4jVersion = "[2.6, 2.999.999)"

plugins {
    kotlin("jvm").version("1.2.70")
    id("com.atlassian.performance.tools.gradle-release").version("0.7.3")
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    testCompile("junit:junit:4.12")
    testCompile("org.assertj:assertj-core:3.12.2")
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("net.jcip:jcip-annotations:1.0")
    testImplementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
}

tasks.wrapper {
    gradleVersion = "5.2.1"
    distributionType = Wrapper.DistributionType.ALL
}
