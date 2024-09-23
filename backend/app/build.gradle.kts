import com.google.protobuf.gradle.id

plugins {
    // Apply the Kotlin JVM plugin.
    id("org.jetbrains.kotlin.jvm") version "1.8.0"
    // Apply the application plugin.
    application
    // Apply the Protobuf plugin.
    id("com.google.protobuf") version "0.9.4"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Testing dependencies.
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Application dependencies.
    implementation("com.google.guava:guava:32.1.2-jre")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("io.grpc:grpc-netty-shaded:1.57.2")
    implementation("io.grpc:grpc-protobuf:1.57.2")
    implementation("io.grpc:grpc-stub:1.57.2")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    // If you want to generate Kotlin code, include the grpc-kotlin-stub
    implementation("io.grpc:grpc-kotlin-stub:1.3.0")
}

// Java toolchain configuration.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17" // Set this to a supported JVM target, like "20", "17", or "11"
    }
}

group = "org.example"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("org.example.ServerKt")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

protobuf {
    protoc {
        // Specify the version of protoc.
        artifact = "com.google.protobuf:protoc:3.22.3"
    }
    plugins {
        // Plugin for generating Java gRPC code.
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.57.2"
        }
        // Plugin for generating Kotlin gRPC code.
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.3.0:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                id("grpc")
                id("grpckt")
            }
            // Optionally, configure generated source code output directories.
            task.generateDescriptorSet = true
            task.descriptorSetOptions.includeSourceInfo = true
        }
    }
}

sourceSets {
    main {
        proto {
            srcDir("src/main/proto")
        }
        kotlin {
            srcDirs(
                "build/generated/source/proto/main/kotlin",
                "build/generated/source/proto/main/grpc",
                "build/generated/source/proto/main/grpckt"
            )
        }
    }
}

tasks.withType<Copy> {
    // Set a duplicates handling strategy
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
