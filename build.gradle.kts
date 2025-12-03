plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.15"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.12.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("com.example.autoservicio1")
    mainClass.set("com.example.autoservicio1.HelloApplication")
}

javafx {
    version = "21.0.6"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.graphics")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("com.itextpdf:kernel:8.0.2")
    implementation("com.itextpdf:layout:8.0.2")
    implementation("org.apache.poi:poi-ooxml:5.2.5")
    implementation("org.apache.poi:poi:5.2.5")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
}
