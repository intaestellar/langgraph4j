plugins {
    id("java")
}

group = "intae"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(platform("org.bsc.langgraph4j:langgraph4j-bom:1.8.14"))
    implementation("org.bsc.langgraph4j:langgraph4j-core")
}

tasks.test {
    useJUnitPlatform()
}