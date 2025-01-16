plugins {
    alias(libs.plugins.git.versioning)
}

group = "com.littlepay.gradle"

gitVersioning.apply {
    refs {
        branch("main") {
            describeTagPattern = "(?<version>.*)"
            version = "\${describe.tag.version}-SNAPSHOT"
        }
        branch(".+") {
            describeTagPattern = "(?<version>.*)"
            version = "\${describe.tag.version}-\${ref}-SNAPSHOT"
        }
        tag("(?<version>.*)") {
            version = "\${ref.version}"
        }
    }
    rev {
        version = "\${commit}"
    }
}