-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

# kotlinx-serialization-json specific. Add this if you have java.lang.NoClassDefFoundError kotlinx.serialization.json.JsonObjectSerializer
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Change here com.yourcompany.yourpackage
-keep,includedescriptorclasses class com.origins.predictor.predictorShared.**$$serializer { *; } # <-- change package name to your app's
-keepclassmembers class com.origins.predictor.predictorShared.** { # <-- change package name to your app's
    *** Companion;
}
-keepclasseswithmembers class ccom.origins.predictor.predictorShared.** { # <-- change package name to your app's
    kotlinx.serialization.KSerializer serializer(...);
}
