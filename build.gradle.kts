plugins {
    kotlin("multiplatform") version "1.7.10"
    id("java")
}

group = "llesha"
version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations {
            //kotlinOptions.jvmTarget = "1.8"
            val main = getByName("main")
            tasks {
                register<Copy>("unzip") {
                    group = "library"

                    val targetDir = File(buildDir, "3rd-libs")
                    project.delete(files(targetDir))
                    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                    main.compileDependencyFiles.forEach {
                        println(it)
                        from(zipTree(it))
                        into(targetDir)

                    }
                }
                register<Jar>("fatJar") {
                    group = "library"
                    manifest {
                        attributes["Implementation-Title"] = "Fat Jar"
                        attributes["Implementation-Version"] = archiveVersion
                        attributes["Main-Class"] = "MainKt"
                    }
                    archiveBaseName.set("${project.name}-fat")
                    val thirdLibsDir = File(buildDir, "3rd-libs")
                    from(main.output.classesDirs, thirdLibsDir)
                    with(jar.get() as CopySpec)
                }
            }
            tasks.getByName("fatJar").dependsOn("unzip")
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }

    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.jsoup:jsoup:1.7.2")
            }
        }
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
    }
}
