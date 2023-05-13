plugins {
    id("com.android.library")
    kotlin("android")
}

setupAndroid()

dependencies {
    api(project(":android:predictor"))
}


val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}

initArtifactory()

publishArtifacts(
    sourcesJar = sourcesJar,
    configurations = configurations,
    project = project,
    artifactId = Publishing.getCustomizationArtifactId(project),
    addDependencies = true,
    extraDependencies = listOf(Publishing.getCoreArtifactId(project))
)