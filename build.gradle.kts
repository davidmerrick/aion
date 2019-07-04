import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.merricklabs.aion"

repositories {
    mavenCentral()
    jcenter()
    maven("https://jitpack.io")
}

plugins {
    id("de.fayard.buildSrcVersions") version Versions.de_fayard_buildsrcversions_gradle_plugin
    kotlin("jvm") version Versions.org_jetbrains_kotlin
    id("com.github.johnrengelman.shadow") version Versions.com_github_johnrengelman_shadow_gradle_plugin
}

dependencies {
    implementation(Libs.okhttp)
    implementation("com.sparkjava:spark-core:2.9.1")
    implementation("com.amazonaws.serverless:aws-serverless-java-container-jersey:1.3.2")
    implementation("org.glassfish.jersey.media:jersey-media-json-jackson:2.29")
    implementation("org.glassfish.jersey.inject:jersey-hk2:2.29")
    implementation(Libs.geocalc)
    implementation(Libs.google_maps_services)
    implementation(Libs.kotlin_stdlib_jdk8)
    implementation(Libs.kotlin_reflect)
    implementation(Libs.slf4j_api)
    implementation(Libs.slf4j_jdk14)
    implementation(Libs.kotlin_logging)
    implementation(Libs.koin_core)
    implementation(Libs.aws_lambda_java_core)
    implementation(Libs.aws_lambda_java_log4j2)
    implementation(Libs.aws_lambda_java_events)
    implementation(Libs.aws_java_sdk_dynamodb)
    implementation(Libs.aws_java_sdk_sns)
    implementation(Libs.jackson_core)
    implementation(Libs.jackson_databind)
    implementation(Libs.jackson_annotations)
    implementation(Libs.jackson_module_kotlin)
    implementation(Libs.biweekly)
    implementation(Libs.guava)

    // JAX-B dependencies for JDK 9+
    implementation("javax.xml.bind:jaxb-api:2.2.11")
    implementation("com.sun.xml.bind:jaxb-core:2.2.11")
    implementation("com.sun.xml.bind:jaxb-impl:2.2.11")
    implementation("javax.activation:activation:1.1.1")

    testImplementation(Libs.testng)
    testImplementation(Libs.koin_test)
    testImplementation(Libs.kotlintest_runner_junit5)
    testImplementation("org.glassfish.jersey.containers:jersey-container-grizzly2-http:2.29")
}

val deployDev = tasks.create<Exec>("deployDev") {
    commandLine = listOf("serverless", "deploy", "--stage=dev")
}

val deployPrd = tasks.create<Exec>("deployPrd") {
    commandLine = listOf("serverless", "deploy", "--stage=prd")
}

// Alias for deploy dev
val deploy = tasks.create("deploy")
deploy.dependsOn(deployDev)

deployDev.dependsOn(tasks.getByName("shadowJar"))
deployPrd.dependsOn(tasks.getByName("shadowJar"))

tasks.test {
    useTestNG()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}