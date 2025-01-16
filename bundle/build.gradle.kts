import com.littlepay.gradle.DownloadGradle
import org.gradle.api.file.DuplicatesStrategy.WARN

plugins {
    id("base")
    id("distribution")
    id("gradle-bundle")
    id("maven-publish")
}

repositories {
    mavenCentral()
}

littlepay {
    gradleVersion = "8.12"
}

val aws by configurations.creating

dependencies {
    aws(awssdk.services.codeartifact)
}

val download = tasks.register<DownloadGradle>("download") {
    version = littlepay.gradleVersion
}

distributions {
    main {
        distributionBaseName = "littlepay-gradle"
        contents {
            from(download.get().output)
            into("init.d/lib") {
                from(aws)
                duplicatesStrategy = WARN
            }
        }
    }
}

