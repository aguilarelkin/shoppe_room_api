# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Conservar las clases de Firebase y Google Sign-In
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.auth.api.signin.** { *; }
-keep class com.google.android.gms.tasks.** { *; }

# Conservar la clase de la actividad de autenticación de Google
-keep class com.google.android.gms.auth.api.signin.GoogleSignInActivity { *; }

# Mantener las clases de autenticación de Firebase
-keep class com.google.firebase.auth.FirebaseAuth { *; }

# No ofuscar métodos importantes relacionados con la autenticación
-keepclassmembers class com.google.firebase.auth.FirebaseAuth {
    public *;
}

# Mantener las clases de la API de Google
-keep class com.google.android.gms.auth.api.signin.GoogleSignInClient { *; }

# Mantener los métodos de la clase GoogleSignInClient
-keepclassmembers class com.google.android.gms.auth.api.signin.GoogleSignInClient {
    public *;
}

# Mantener las clases de Gson (que es utilizada por Firebase)
-keep class com.google.gson.** { *; }

# Mantener clases relacionadas con las notificaciones (si las usas)
-keep class com.google.firebase.messaging.** { *; }

# Conservar las clases de Activity, Service y BroadcastReceiver (si las usas)
#-keep class android.app.** { *; }
#-keep class android.content.** { *; }
#-keep class android.widget.** { *; }
