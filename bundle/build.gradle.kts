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
    maven {
        name = "codeartifact"
        url = uri("https://littlepay-970114829785.d.codeartifact.ap-southeast-2.amazonaws.com/maven/release")
        credentials {
            username = "aws"
            password = System.getenv("CODEARTIFACT_AUTH_TOKEN")
        }
    }
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

