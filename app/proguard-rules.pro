# Keep Task data class and its members for Firestore serialization
-keep class com.example.advancedconceptsapp.ui.data.Task { <init>(...); *; }
# Keep any other data classes used with Firestore similarly
# -keep class com.yourcompany.advancedconceptsapp.data.AnotherFirestoreModel { <init>(...); *; }

# Keep Coroutine builders and intrinsics (often needed, though AGP/R8 handle some automatically)
-keepnames class kotlinx.coroutines.intrinsics.** { *; }

# Keep companion objects for Workers if needed (sometimes R8 removes them)
-keepclassmembers class * extends androidx.work.Worker {
    public static ** Companion;
}

# Keep specific fields/methods if using reflection elsewhere
# -keepclassmembers class com.example.SomeClass {
#    private java.lang.String someField;
#    public void someMethod();
# }

# Add rules for any other libraries that require them (e.g., Retrofit, Gson, etc.)
# Consult library documentation for necessary Proguard/R8 rules.