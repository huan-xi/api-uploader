
plugins {
    id("org.jetbrains.intellij") version "1.13.3"
    java
    id("idea")
}
dependencies {
    compileOnly(
        "org.projectlombok:lombok:1.18.24"
    )

    annotationProcessor(
        "org.projectlombok:lombok:1.18.24"

    )
//    implementation(
//        "cn.hutool:hutool-core:5.8.3"
//    )
//    implementation(
//        "cn.hutool:hutool-http:5.8.3"
//    )
}

group = "cn.hperfect"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}

dependencies {
//    classpath("org.jetbrains.intellij.plugins:gradle-intellij-plugin:1.13.3")
    compileOnly("org.jetbrains.intellij.plugins:gradle-intellij-plugin:1.13.3")
    testImplementation("junit", "junit", "4.12")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
//    version.set("2021.2")
    type.set("IC")
//    localPath.set("/Applications/IntelliJIDEACE.app/Contents")
    localPath.set("/Applications/IntelliJ IDEA CE.app/Contents")
    plugins.set(listOf("java"))

}


tasks.withType<JavaCompile> {
    sourceCompatibility = "11"
    targetCompatibility = "11"
}



