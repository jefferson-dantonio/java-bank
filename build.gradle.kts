plugins {
    id("java")
}



configurations{
    compileOnly{
        extendsFrom(configurations.annotationProcessor.get())
    }
}

group = "br.com.jdm"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    compileOnly("org.projectlombok:lombok:1.18.38") // Use the latest version
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}