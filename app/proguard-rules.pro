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

# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 7

# 混合时不使用大小写混合，混合后的类名为小写
#-dontusemixedcaseclassnames

# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses

# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers

# 这句话能够使我们的项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose

# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify

# 保留Annotation不混淆 这在JSON实体映射时非常重要，比如fastJson
-keepattributes *Annotation*,InnerClasses

# 避免混淆泛型
-keepattributes Signature

# 抛出异常时保留代码行号
#-keepattributes SourceFile,LineNumberTable

# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*

# 忽略警告
-ignorewarnings

# 设置是否允许改变作用域
-allowaccessmodification

# 把混淆类中的方法名也混淆了
#-useuniqueclassmembernames

# apk 包内所有 class 的内部结构
#-dump class_files.txt

# 未混淆的类和成员
-printseeds seeds_txt

# 列出从apk中删除的代码
-printusage unused.txt

# 混淆前后的映射
-printmapping mapping.txt

-keep public class * extends de.robv.android.xposed.IXposedHookLoadPackage


-assumenosideeffects class com.zhao.dice.model.AwLog {
    public static *** *(...);
}
-assumenosideeffects class android.util.Log {
    public static *** *(...);
}
-assumenosideeffects class java.io.PrintStream {
  public *** println(...);
  public *** print(...);
}
-assumenosideeffects class java.lang.Throwable {
    public void printStackTrace();
}
-assumenosideeffects class de.robv.android.xposed.XposedBridge{
    public synchronized static void log(String);
}

-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keep class org.keplerproject.luajava.**{ *; }

# Only required if you use AsyncExecutor

#-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
#    <init>(java.lang.Throwable);
#}
