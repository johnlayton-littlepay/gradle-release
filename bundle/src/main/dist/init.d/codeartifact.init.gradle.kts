import aws.sdk.kotlin.services.codeartifact.*
import kotlinx.coroutines.runBlocking

initscript {
    dependencies {
        classpath(fileTree("${initscript.sourceFile?.parentFile}/lib") { include("*.jar") })
    }
}

fun getToken(d: String, o: String, r: String): String {
    return System.getenv("CODEARTIFACT_AUTH_TOKEN")?.also {
        logger.info("Use environment authorization token")
    } ?: runBlocking {
        logger.info("Use AWS SDK to get authorization token")
        val token = CodeartifactClient
            .fromEnvironment {
                region = r
            }
            .use { client ->
                client.getAuthorizationToken {
                    domain = d
                    domainOwner = o
                }
            }.authorizationToken
        return@runBlocking token!!
    }
}

fun applyCredentials(repository: MavenArtifactRepository) {
    val pattern = "([a-zA-Z0-9\\-]+)-([0-9]+).d.codeartifact.([a-zA-Z0-9\\-]*).amazonaws.com".toRegex()
    pattern.find(repository?.url?.host ?: "")?.let { result ->
        logger.info("Add credentials to repository ${repository.url}")
        val (domain, owner, region) = result.destructured
        logger.info("\tOwner  :: ${owner}")
        logger.info("\tDomain :: ${domain}")
        logger.info("\tRegion :: ${region}")
        val token = getToken(domain, owner, region)
        repository.credentials {
            username = "aws"
            password = token
        }
    }
}

allprojects {
    repositories {
        all {
            if (this is MavenArtifactRepository) {
                applyCredentials(this);
            }
        }
    }

    afterEvaluate {
        if (plugins.hasPlugin("maven-publish")) {
            val publishing: PublishingExtension = project.getExtensions().getByType(PublishingExtension::class.java)
            publishing.repositories {
                all {
                    if (this is MavenArtifactRepository) {
                        applyCredentials(this);
                    }
                }
            }
        }
    }
}

settingsEvaluated {
    pluginManagement {
        repositories {
            all {
                if (this is MavenArtifactRepository) {
                    applyCredentials(this);
                }
            }
        }
    }
}

