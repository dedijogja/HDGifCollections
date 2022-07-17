#include <jni.h>
#include <string>

//key text sudah diganti HD Gif Collections, key asset sudah diganti ke HD Gif Collections
std::string keyDesText = "4Ib\"]6Mq4E{S`th-";
std::string keyDesAssets = "ZeIxOTYHRvBnTTzXgOigWw==";

//kode iklan sudah di ganti ke HD Gif Collection
std::string adBanner =              "bF+Ga{qAI,W+()*-q**3%7@^5!#!&%00%96%__";
std::string adInterstitial =        "bF+Ga{qAI,W+()*-q**3%7@^5!#!$&9=*5-$*_";
std::string adNativeMenu =          "bF+Ga{qAI,W+()*-q**3%7@^5!#!%+-7#30(@W";

//startApp sudah diganti HD Gif Collections
std::string startAppId = "4Q(W7)4^$";

//pesan eror sudah diganti HD Gif Collections
std::string smesek = "bFM?\\Y!{MhYUSTS,h4WjSo1P]U`/EG,";

//area package, mempersulit proses replace String .so file dengan memisahkan menjadi beberapa bagian
std::string awalP = "com.hdg";
std::string tengahP = "ifstudios.";
std::string ahirP = "allahgifcollectionnew";
std::string finalPackage = awalP + tengahP + ahirP;

jstring dapatkanPackage(JNIEnv* env, jobject activity) {
    jclass android_content_Context =env->GetObjectClass(activity);
    jmethodID midGetPackageName = env->GetMethodID(android_content_Context,"getPackageName", "()Ljava/lang/String;");
    jstring packageName= (jstring)env->CallObjectMethod(activity, midGetPackageName);
    return packageName;
}

const char * cekStatus(JNIEnv* env, jobject activity, const char * text){
    if(strcmp(env->GetStringUTFChars(dapatkanPackage(env, activity), NULL), finalPackage.c_str()) != 0){
        jclass Exception = env->FindClass("java/lang/RuntimeException");
        env->ThrowNew(Exception, "JNI Failed!");
        return NULL;
    }
    return text;
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_hdgifstudios_util_Native_keyDesText(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, keyDesText.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_hdgifstudios_util_Native_keyDesAssets(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, keyDesAssets.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_hdgifstudios_util_Native_packageName(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, finalPackage.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_hdgifstudios_util_Native_adInterstitial(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, adInterstitial.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_hdgifstudios_util_Native_adBanner(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, adBanner.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_hdgifstudios_util_Native_adNativeMenu(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, adNativeMenu.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_hdgifstudios_util_Native_startAppId(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, startAppId.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_hdgifstudios_util_Native_smesek(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, smesek.c_str()));
}
