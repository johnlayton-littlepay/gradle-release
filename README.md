# littlepay-gradle

### Usage

1. Initialise gradle project, eg;
```shell

gradle init --type kotlin-library --dsl kotlin
```
2. Update the wrapper distribution url, eg;
```shell

gradle wrapper --gradle-distribution-url https://github.com/johnlayton-littlepay/gradle-release/releases/download/0.0.7/littlepay-gradle-0.0.7.zip
```
3. Add dependency configuration to version catalog. eg;
```toml
[versions]
...

[plugins]
...

[libraries]
...
lp-sqs = { group= "com.littlepay", name = "lp-sqs", version = "1.0.3" }
```
4. Add repository to `lib/build.gradle.kts`, eg;
```kotlin
repositories {
    mavenCentral()
   maven {
        name = "codeartifact"
        url = uri("https://littlepay-970114829785.d.codeartifact.ap-southeast-2.amazonaws.com/maven/release")
    }
}
```
5. Add dependency to `lib/build.gradle.kts`, eg;
```kotlin
dependencies {
    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api(libs.commons.math3)

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation(libs.guava)
    
    implementation(libs.lp.sqs)
}
```
6. Ensure that AWS SSO credentials (or profile is selected), eg;
```shell

aws sso login [--sso-session littlepay]
```
7. Build project;
```shell

gradle build
```
