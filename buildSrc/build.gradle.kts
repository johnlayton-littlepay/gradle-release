plugins {
    `kotlin-dsl`
    alias(libs.plugins.jvm)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
}

gradlePlugin {
    plugins {
        register("gradle-bundle") {
            id = "gradle-bundle"
            implementationClass = "com.littlepay.gradle.GradleBundlePlugin"
        }
    }
}
