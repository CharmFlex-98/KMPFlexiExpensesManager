# Keep enum classes and their members
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    *;
}

# Keep the enum classes themselves
-keep enum * { *; }

# For Kotlin enums specifically
-keepclassmembers class * extends java.lang.Enum {
    *;
}

# Keep enum constructors and fields
-keepclassmembers enum * {
    <fields>;
    <methods>;
}

# Prevent enum obfuscation by keeping their names
-keepnames class * extends java.lang.Enum

# If you're using enum serialization (like with kotlinx.serialization)
-keepattributes *Annotation*
-keepclassmembers class **$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}

# For your specific enum classes, you can also add explicit rules:
-keep class com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType { *; }
-keep class com.charmflex.cp.flexiexpensesmanager.features.scheduler.domain.models.SchedulerPeriod { *; }
-keep class com.charmflex.cp.flexiexpensesmanager.core.app.model.AppFlavour { *; }
-keep class com.charmflex.cp.flexiexpensesmanager.features.remote.feature_flag.model.PremiumFeature { *; }


## Keep Apache POI classes (for Excel file handling)
-dontwarn org.apache.**
-dontwarn org.openxmlformats.schemas.**
-dontwarn org.etsi.**
-dontwarn org.w3.**
-dontwarn com.microsoft.schemas.**
-dontwarn com.graphbuilder.**
-dontnote org.apache.**
-dontnote org.openxmlformats.schemas.**
-dontnote org.etsi.**
-dontnote org.w3.**
-dontnote com.microsoft.schemas.**
-dontnote com.graphbuilder.**
-keep class org.apache.poi.** { *; }
-keep class org.** { *; }
-keep class com.bea.xml.stream.**{*;}
-keep class org.apache.xmlbeans.** { *; }
-keep class com.microsoft.** { *; }
-keep class org.openxmlformats.** {*;}
-keep class com.apache.poi.** { *; }
-keep class schemaorg_apache_xmlbeans.** {*;}

#-keep class com.posthog.** { *; }
# Additional Kotlin-specific rules
-dontwarn kotlin.**
-keep class kotlin.Metadata { *; }
