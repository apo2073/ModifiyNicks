plugins {
    kotlin("jvm") version "2.1.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.7.7"
}

group = "kr.apo2073"
version = "1.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven("https://jitpack.io")
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/") {
        name = "sonatype-oss-snapshots"
    }
}

val ver="1.20.4-R0.1-SNAPSHOT"

dependencies {
//    compileOnly("org.spigotmc:spigot-api:${ver}")
//    implementation("org.bukkit:craftbukkit:$ver")
//    implementation(files("libs/CMI-9.7.7.12.jar"))
    compileOnly("io.papermc.paper:paper-api:${ver}")
    paperweight.paperDevBundle(ver)
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    implementation("com.github.apo2073:ApoLib:1.0.4")
    implementation("net.wesjd:anvilgui:1.10.3-SNAPSHOT")
}

val targetJavaVersion = 17
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

//tasks.shadowJar {
//    archiveFileName.set("ChangeNick.jar")
//    destinationDirectory=file("C:\\Users\\PC\\Desktop\\Test_Server\\20.4\\plugins")
//    archiveClassifier.set("all")
//    mergeServiceFiles()
//    relocate("net.wesjd.anvilgui", "kr.apo2073.modifyNick.anvilgui")
//    minimize()
//    dependencies {
//        include(dependency("net.wesjd:anvilgui:1.10.3-SNAPSHOT"))
//    }
//}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}
