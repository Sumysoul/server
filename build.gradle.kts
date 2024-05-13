plugins {
    java
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
}

repositories {
    mavenCentral()
}

group = "com.jdum.commerce"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

val awsSpringServerlessVersion: String by project
val awsSpringCloudVersion: String by project
val jjwtVersion: String by project
val mapstructVersion: String by project
val lombokVersion: String by project
val lombokBindingVersion: String by project
val springCloudVersion: String by project

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.amazonaws.serverless:aws-serverless-java-container-springboot3:$awsSpringServerlessVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.cloud:spring-cloud-starter-aws:$awsSpringCloudVersion")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    implementation("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")

    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:$lombokBindingVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    compileOnly("org.projectlombok:lombok:$lombokVersion")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

tasks.register("buildZip", Zip::class) {
    description = "Builds zip file for serverless deployment"
    group = JavaBasePlugin.BUILD_TASK_NAME
    archiveBaseName.set("sumysoul")
    from(tasks.named("compileJava"))
    from(tasks.named("processResources"))
    into("lib") {
        from(configurations.runtimeClasspath) {
            exclude("tomcat-embed-*")
        }
    }
}

tasks.named("build") {
    dependsOn("buildZip")
}
