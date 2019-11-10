import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
}

group = "de.oncoding"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("au.com.dius:pact-jvm-consumer-junit5_2.12:3.6.5")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.2")
    testImplementation("org.junit.platform:junit-platform-runner:1.4.1")
    testImplementation("org.mockito:mockito-junit-jupiter:2.23.4")
    testImplementation("org.assertj:assertj-core:3.14.0")

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}


tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        exceptionFormat = TestExceptionFormat.FULL
    }

    addTestListener(object : TestListener {
        override fun beforeTest(p0: TestDescriptor?) = Unit
        override fun beforeSuite(p0: TestDescriptor?) = Unit
        override fun afterTest(desc: TestDescriptor, result: TestResult) = Unit
        override fun afterSuite(desc: TestDescriptor, result: TestResult) {
            printTestResults(desc, result)
        }
    })
}

fun printTestResults(desc: TestDescriptor, result: TestResult) {
    if (desc.name.contains("Gradle Test", ignoreCase = true)) {
        val output = result.run {
            "Results: $resultType (" +
                    "$testCount tests, " +
                    "$successfulTestCount successes, " +
                    "$failedTestCount failures, " +
                    "$skippedTestCount skipped" +
                    ")"
        }
        val testResultLine = "|  $output  |"
        val repeatLength = testResultLine.length
        val seperationLine = "-".repeat(repeatLength)
        println(seperationLine)
        println(testResultLine)
        println(seperationLine)
    }
}
