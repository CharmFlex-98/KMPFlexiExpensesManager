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
-keep class com.charmflex.cp.flexiexpensesmanager.core.app.AppFlavour { *; }
-keep class com.charmflex.cp.flexiexpensesmanager.features.remote.feature_flag.model.PremiumFeature { *; }

# Additional Kotlin-specific rules
-dontwarn kotlin.**
-keep class kotlin.Metadata { *; }
