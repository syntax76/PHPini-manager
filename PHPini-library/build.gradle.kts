plugins {
    id("java")
}

group = "de.hermann-bsd"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

val slf4jVersion = "2.0.7"
val jetbrainsAnnotationsVersion = "24.0.0"

dependencies {
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
    implementation("org.jetbrains:annotations:$jetbrainsAnnotationsVersion")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}