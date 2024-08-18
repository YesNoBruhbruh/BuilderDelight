plugins {
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "me.maanraj514"
version = "1.0"

repositories {
    mavenCentral()
//    mavenLocal() needed for NMS
    maven("https://repo.papermc.io/repository/maven-public/")
//    maven("https://maven.enginehub.org/repo/") // WorldEdit
    maven("https://maven.respark.dev/releases") // FAWE
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
//    compileOnly("org.spigotmc:spigot:1.20.4-R0.1-SNAPSHOT")
//    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.3.0")

    implementation(platform("com.intellectualsites.bom:bom-newest:1.44")) // Ref: https://github.com/IntellectualSites/bom
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit") { isTransitive = false }

    implementation("com.h2database:h2:2.3.232")

//    implementation("dev.respark.licensegate:license-gate:1.0.3")
}

kotlin {
    jvmToolchain(17)
}