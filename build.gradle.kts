plugins {
    id("java")
    application
}

group = "no.nav"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.json:json:20240303")
    implementation("io.github.cdimascio:dotenv-java:3.0.2")
}

application {
    mainClass.set("no.nav.Main")
}

tasks.test {
    useJUnitPlatform()
}