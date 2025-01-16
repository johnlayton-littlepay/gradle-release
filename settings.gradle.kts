pluginManagement {
    repositories {
        maven("https://plugins.gradle.org/m2")
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        create("awssdk") {
            from("aws.sdk.kotlin:version-catalog:1.3.112")
        }
    }
}

rootProject.name = "littlepay-gradle"

include(
    "bundle"
)
