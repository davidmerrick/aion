/**
 * Find which updates are available by running
 *     `$ ./gradlew buildSrcVersions`
 * This will only update the comments.
 *
 * YOU are responsible for updating manually the dependency version. */
object Versions {
    const val aws_java_sdk_dynamodb: String = "1.11.527" // available: "1.11.573"

    const val aws_java_sdk_sns: String = "1.11.527" // available: "1.11.573"

    const val aws_lambda_java_core: String = "1.2.0"

    const val aws_lambda_java_events: String = "2.2.6"

    const val aws_lambda_java_log4j2: String = "1.1.0"

    const val com_fasterxml_jackson_core: String = "2.9.9"

    const val jackson_module_kotlin: String = "2.9.9"

    const val com_github_johnrengelman_shadow_gradle_plugin: String = "4.0.4"
    // available: "5.0.0"

    const val okhttp: String = "3.14.2"

    const val twilio: String = "7.39.0"

    const val de_fayard_buildsrcversions_gradle_plugin: String = "0.3.2"

    const val kotlin_logging: String = "1.6.26"

    const val kotlintest_runner_junit5: String = "3.3.2"

    const val org_jetbrains_kotlin_jvm_gradle_plugin: String = "1.3.31"

    const val org_jetbrains_kotlin: String = "1.3.31"

    const val org_koin: String = "2.0.1"

    const val slf4j_api: String = "1.7.25" // available: "1.7.26"

    const val slf4j_jdk14: String = "1.7.26"

    const val testng: String = "6.14.3"

    /**
     *
     *   To update Gradle, edit the wrapper file at path:
     *      ./gradle/wrapper/gradle-wrapper.properties
     */
    object Gradle {
        const val runningVersion: String = "5.4"

        const val currentVersion: String = "5.4.1"

        const val nightlyVersion: String = "5.6-20190616000028+0000"

        const val releaseCandidate: String = "5.5-rc-3"
    }
}
