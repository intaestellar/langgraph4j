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
    implementation("org.bsc.langgraph4j:langgraph4j-langchain4j")

    implementation(platform("dev.langchain4j:langchain4j-bom:1.13.0"))
    implementation("dev.langchain4j:langchain4j")
    implementation("dev.langchain4j:langchain4j-open-ai")
    implementation("dev.langchain4j:langchain4j-http-client-jdk")

    runtimeOnly("org.slf4j:slf4j-simple:2.0.13")
}

tasks.test {
    useJUnitPlatform()
}