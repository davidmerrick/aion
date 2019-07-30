group = "com.merricklabs.aion"

repositories {
    mavenCentral()
    jcenter()
    maven("https://jitpack.io")
}

plugins {
    id("de.fayard.buildSrcVersions") version Versions.de_fayard_buildsrcversions_gradle_plugin
    kotlin("jvm") version Versions.org_jetbrains_kotlin
}

dependencies {
    implementation(Libs.okhttp)
    implementation(Libs.aws_serverless_java_container_jersey)
    implementation(Libs.jersey_media_json_jackson)
    implementation(Libs.jersey_hk2)
    compile("org.awaitility:awaitility:4.0.0-rc1")
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
    implementation(Libs.jackson_core)
    implementation(Libs.jackson_databind)
    implementation(Libs.jackson_annotations)
    implementation(Libs.jackson_module_kotlin)
    implementation(Libs.biweekly)
    implementation(Libs.guava)

    // JAX-B dependencies for JDK 9+
    implementation(Libs.jaxb_api)
    implementation(Libs.jaxb_core)
    implementation(Libs.jaxb_impl)
    implementation(Libs.activation)

    testImplementation(Libs.testng)
    testImplementation(Libs.koin_test)
    testImplementation(Libs.kotlintest_runner_junit5)
    testImplementation(Libs.jersey_container_grizzly2_http)
    testImplementation(Libs.mockito_kotlin)
}

tasks {

    val deployPrd by creating(Exec::class) {
        commandLine = listOf("serverless", "deploy", "--stage=prd")
    }

    val deployDev by creating(Exec::class) {
        commandLine = listOf("serverless", "deploy", "--stage=dev")
    }

    val deploy by creating {
        dependsOn(deployDev)
    }

    test {
        useTestNG()
    }

    val buildZip by creating(Zip::class) {
        from(compileKotlin)
        from(processResources)
        into("lib") {
            from(configurations.runtimeClasspath)
        }
    }

    build {
        dependsOn(buildZip)
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}