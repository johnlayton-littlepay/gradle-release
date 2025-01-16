plugins {
    alias(libs.plugins.jgitver)
}

group = "com.littlepay.gradle"

jgitver {
    useSnapshot = true
    useDistance = false
    nonQualifierBranches = "main"
}
