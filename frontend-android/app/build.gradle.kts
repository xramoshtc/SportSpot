                                                                        import org.jetbrains.dokka.gradle.DokkaTask

                                                                        plugins {
                                                                            alias(libs.plugins.android.application)
                                                                            alias(libs.plugins.kotlin.android)
                                                                            alias(libs.plugins.kotlin.compose)
                                                                            id("org.jetbrains.dokka")
                                                                        }
                                                                        android {
                                                                            namespace = "com.example.sportspot"
                                                                            compileSdk = 36

                                                                            defaultConfig {
                                                                                applicationId = "com.example.sportspot"
                                                                                minSdk = 24
                                                                                targetSdk = 36
                                                                                versionCode = 1
                                                                                versionName = "1.0"

                                                                                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                                                                            }

                                                                            buildTypes {
                                                                                release {
                                                                                    isMinifyEnabled = false
                                                                                    proguardFiles(
                                                                                        getDefaultProguardFile("proguard-android-optimize.txt"),
                                                                                        "proguard-rules.pro"
                                                                                    )
                                                                                }
                                                                            }
                                                                            compileOptions {
                                                                                sourceCompatibility = JavaVersion.VERSION_11
                                                                                targetCompatibility = JavaVersion.VERSION_11
                                                                            }
                                                                            kotlinOptions {
                                                                                jvmTarget = "11"
                                                                            }
                                                                            buildFeatures {
                                                                                compose = true
                                                                            }
                                                                        }

                                                                        dependencies {

                                                                            implementation(libs.androidx.core.ktx)
                                                                            implementation(libs.androidx.lifecycle.runtime.ktx)
                                                                            implementation(libs.androidx.activity.compose)
                                                                            implementation(platform(libs.androidx.compose.bom))
                                                                            implementation(libs.androidx.ui)
                                                                            implementation(libs.androidx.ui.graphics)
                                                                            implementation(libs.androidx.ui.tooling.preview)
                                                                            implementation(libs.androidx.material3)
                                                                            implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
                                                                            implementation("androidx.navigation:navigation-compose:2.7.7")
                                                                            implementation("com.squareup.retrofit2:retrofit:2.9.0")
                                                                            implementation("com.squareup.retrofit2:converter-gson:2.9.0")
                                                                            implementation("androidx.datastore:datastore-preferences:1.0.0")
                                                                            implementation("androidx.compose.material:material-icons-extended")
                                                                            testImplementation("junit:junit:4.13.2")
                                                                            testImplementation("io.mockk:mockk:1.13.10")
                                                                            testImplementation("androidx.arch.core:core-testing:2.2.0")
                                                                            testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
                                                                            androidTestImplementation("androidx.test.ext:junit:1.1.5")
                                                                            androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
                                                                            androidTestImplementation("androidx.test:core:1.5.0")
                                                                            androidTestImplementation("androidx.test:runner:1.5.2")
                                                                            androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")

                                                                            testImplementation(libs.junit)
                                                                            androidTestImplementation(libs.androidx.junit)
                                                                            androidTestImplementation(libs.androidx.espresso.core)
                                                                            androidTestImplementation(platform(libs.androidx.compose.bom))
                                                                            androidTestImplementation(libs.androidx.ui.test.junit4)
                                                                            debugImplementation(libs.androidx.ui.tooling)
                                                                            debugImplementation(libs.androidx.ui.test.manifest)
                                                                        }

                                                                        /*
                                                                         Configure Dokka for this module. We use tasks.withType<DokkaTask>() to avoid
                                                                         invoking dokkaHtml as a function and to configure all Dokka tasks (dokkaHtml, etc.).
                                                                        */
                                                                        tasks.withType<DokkaTask>().configureEach {
                                                                            dokkaSourceSets {
                                                                                named("main") {
                                                                                    moduleName.set("SportSpot")
                                                                                    displayName.set("SportSpot")
                                                                                    // Ensure Dokka scans your source folders
                                                                                    sourceRoots.from(file("src/main/kotlin"))
                                                                                    sourceRoots.from(file("src/main/java"))
                                                                                    // If you have a MODULE.md in the module, you can include it:
                                                                                    // includes.from("MODULE.md")
                                                                                }
                                                                            }
                                                                            // Put output inside the module build folder
                                                                            outputDirectory.set(buildDir.resolve("dokka"))
                                                                        }
