package com.zhao.dice.model.plugins;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zhao.dice.model.Adaptation;
import com.zhao.dice.model.AwLog;
import com.zhao.dice.model.XposedUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class DebugXposed {
    public static URL getClassLocation(ClassLoader cl,final Class cls) throws MalformedURLException {
        if (cls == null) {
            throw new IllegalArgumentException("null point, cls");
        }
        URL result =null;
        final String clsAsResource = cls.getName().replace('.', '/').concat(".class");
        final ProtectionDomain pd = cls.getProtectionDomain();

        if (pd != null) {
            final CodeSource cs = pd.getCodeSource();
            // 'cs' can be null depending on the classloader behavior:
            if (cs != null) result = cs.getLocation();
            if (result != null) {
                // Convert a code source location into a full class file location
                // for some common cases:
                if ("file".equals(result.getProtocol())) {
                    if (result.toExternalForm().endsWith(".jar") ||
                            result.toExternalForm().endsWith(".zip"))
                        result = new URL("jar:".concat(result.toExternalForm())
                                .concat("!/").concat(clsAsResource));
                    else if (new File(result.getFile()).isDirectory())
                        result = new URL(result, clsAsResource);

                }
            }
        }
        if (result == null) {
            // Try to find 'cls' definition as a resource; this is not
            // document．d to be legal, but Sun's implementations seem to         //allow this:
            final ClassLoader clsLoader = cl;
            result = clsLoader != null ?
                    clsLoader.getResource(clsAsResource) :
                    ClassLoader.getSystemResource(clsAsResource);
        }
        return result;
    }
    public static void init(final Adaptation adaptation){
        findAndHookMethod(Activity.class, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Class clazz=param.thisObject.getClass();
                AwLog.Log("Creating Activity:"+param.thisObject.getClass());
                if(clazz.getName().equals("com.troop.member.plugin.TroopMemberCardMoreInfoActivity")){
                    AwLog.Log("Catch!");
                    ClassLoader cl=clazz.getClassLoader();
                    AwLog.Log("getResource Activity:"+cl.toString());

                }
                //com.troop.member.plugin.TroopMemberCardMoreInfoActivity

                super.beforeHookedMethod(param);
            }
        });

        findAndHookMethod(Activity.class, "startActivityForResult", Intent.class,int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                AwLog.Log("------~~~startActivityForResult~~~~~~~~~~~~"+param.args[0]+" request="+param.args[1]);
                //"com.tencent.mobileqq.filemanager.activity.BaseFileAssistantActivity"
                AwLog.Log("------AAAAAAAAAAAAAAA------Method-Stack:" + ReflectionUtil.getStackTraceString(new Throwable()));
                super.beforeHookedMethod(param);
            }
        });
        /*
        Object troopHandler=adaptation.getTroopHandler();
        Method changeMemberInfo=ReflectionUtil.getMethod(troopHandler,"a",void.class,String.class, ArrayList.class,ArrayList.class);
        XposedBridge.hookMethod(changeMemberInfo, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                AwLog.Log("群名修改函数屏蔽！"+param.args[0]);
                XposedUtil.getObjAttr(((ArrayList)param.args[1]).get(0));
                AwLog.Log("2:"+((ArrayList)param.args[2]).get(0));
                param.setResult(null);
                super.beforeHookedMethod(param);
            }
        });*/
    }
}
