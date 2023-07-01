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


-keep class com.soheibbettahar.litarus.data.network.guttendexApi.NetworkFormats {
    *;
}

-keep class com.soheibbettahar.litarus.data.network.guttendexApi.NetworkBook {
    *;
}

-keep class com.soheibbettahar.litarus.data.network.guttendexApi.NetworkBooksPage {
    *;
}

-keep class com.soheibbettahar.litarus.data.network.guttendexApi.NetworkPerson {
    *;
}

-keep class com.soheibbettahar.litarus.data.network.googleBooksApi.NetworkGoogleBook {
    *;
}

-keep class com.soheibbettahar.litarus.data.network.googleBooksApi.NetworkGoogleBookInfo {
    *;
}

-keep class com.soheibbettahar.litarus.data.network.googleBooksApi.NetworkGoogleBookPage {
    *;
}

-keep class com.soheibbettahar.litarus.data.network.googleBooksApi.NetworkImageLinks {
    *;
}


# OkHttp platform used only on JVM and when Conscrypt and other security providers are available. This should be fixed in version > 5.0.0-alpha2
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**