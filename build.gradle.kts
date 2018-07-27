import java.net.URI
import net.linguica.gradle.maven.settings.LocalMavenSettingsLoader
import net.linguica.gradle.maven.settings.MavenSettingsPluginExtension

val kotlinVersion = "1.2.30"

buildscript {
    repositories {
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath("net.linguica.gradle:maven-settings-plugin:0.5")
    }
}

plugins {
    kotlin("jvm").version("1.2.30")
    maven
    `maven-publish`
    id("pl.allegro.tech.build.axion-release").version("1.8.1")
}

apply(plugin = "java")

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

scmVersion {
    tag.prefix = "release"
}
project.version = scmVersion.version

tasks["release"].doFirst {
    if (scmVersion.scmPosition.branch != "master") {
        throw Exception("Releasing allowed only on master branch")
    }
}

maven {
    group = "com.atlassian.performance.tools"
    version = scmVersion.version
}

publishing {
    publications.create("mavenJava", MavenPublication::class.java) {
        artifact(tasks["jar"])
    }
    if (scmVersion.version.endsWith("SNAPSHOT")) {
        repositories.add(project.repositories["atlassian-private-snapshot"])
    } else {
        repositories.add(project.repositories["atlassian-private"])
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "atlassian-public"
        url = URI("https://packages.atlassian.com/maven-external/")
    }
    maven {
        name = "atlassian-private"
        url = URI("https://packages.atlassian.com/maven-private/")
        credentials(
            atlassianPrivateFromEnv()
                ?: mavenCredentials()
                ?: throw Exception("Maven settings for '$name' are missing")
        )
    }
    maven {
        name = "atlassian-private-snapshot"
        url = URI("https://packages.atlassian.com/maven-private-snapshot")
        credentials(
            atlassianPrivateFromEnv()
                ?: mavenCredentials()
                ?: throw Exception("Maven settings for '$name' are missing")
        )
    }
}

fun atlassianPrivateFromEnv(): Action<in PasswordCredentials>? {
    val envUsername = System.getenv("atlassian_private_username")
    val envPassword = System.getenv("atlassian_private_password")
    if (envUsername == null || envPassword == null) {
        return null
    }
    return Action {
        username = envUsername
        password = envPassword
    }
}

fun MavenArtifactRepository.mavenCredentials(): Action<in PasswordCredentials>? {
    val settings = LocalMavenSettingsLoader(MavenSettingsPluginExtension(project)).loadSettings()
    val server = settings.getServer(name) ?: return null

    return Action {
        username = server.username
        password = server.password
    }
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlinVersion")
    testCompile("junit:junit:4.12")
    testCompile("org.hamcrest:hamcrest-library:1.3")
    log4j(
        "api",
        "core",
        "slf4j-impl"
    ).forEach { compile(it) }
}

fun log4j(
    vararg modules: String
): List<String> = modules.map { module ->
    "org.apache.logging.log4j:log4j-$module:2.10.0"
}

val wrapper = tasks["wrapper"] as Wrapper
wrapper.gradleVersion = "4.9"
wrapper.distributionType = Wrapper.DistributionType.ALL