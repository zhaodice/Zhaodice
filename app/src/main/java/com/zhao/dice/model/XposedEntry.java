package com.zhao.dice.model;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import com.zhao.dice.model.plugins.DebugXposed;
import com.zhao.dice.model.plugins.QQMessage.QQMessageXposed;
import com.zhao.dice.model.plugins.SettingEntry.SettingEntryXposed;

import java.io.File;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class XposedEntry implements IXposedHookLoadPackage  {
    static boolean hooked=false;
    public static final long START_TIME=System.currentTimeMillis();//初始化启动时间


    private boolean deleteFile(File file)
    {
        if (!file.exists())
            return false;
        if (file.isFile())
        {
            file.delete();
        }
        else if (file.isDirectory())
        {
            File[] listFiles = file.listFiles();
            if (listFiles != null)
            {
                for (File f:listFiles)
                {
                    deleteFile(f);
                }
            }
            file.delete();
        }
        return !file.exists();
    }

    public static void HookFrist(XC_MethodHook.MethodHookParam param) throws PackageManager.NameNotFoundException {
        AwLog.Log("Application attach hooked="+hooked);
        if(!hooked){
            final Context context=((Context)param.thisObject);
            AwLog.Log("context="+context);
            final ClassLoader cl = context.getClassLoader();
            AwLog.Log("package info loaded");

            AwLog.Log("new Adaptation...");
            Adaptation adaptation=new Adaptation(context);
            adaptation.SelectBestMethod(cl);
            //初始化骰子
            try{
                QQMessageXposed.init(adaptation);
            }catch (Throwable e){}
            //初始化菜单
            try{
                SettingEntryXposed.init(adaptation);
            }catch (Throwable e){}
            //初始化调试信息
            if("debug".equals(BuildConfig.BUILD_TYPE)) {
                try {
                    DebugXposed.init(adaptation);
                } catch (Throwable e) {
                }
            }

            //初始hook
            hooked=true;
        }
        //_onCompleted
    }
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        // TODO Auto-generated method stub
        //Log.d("chulhu","Load: "+lpparam.packageName);
        //适配QQ与TIM
        //Log.d("chulhu","lpparam.packageName:"+lpparam.packageName);
        if (!lpparam.packageName.equals(Adaptation.QQ) && !lpparam.packageName.equals(Adaptation.TIM)) {
            return;//什么都不是
        }
        AwLog.Log("Load: "+lpparam.packageName);
        //Context createPackageContext=((Context) XposedHelpers.callMethod(XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread",(ClassLoader)null),"currentActivityThread",new Object[0]),"getSystemContext",new Object[0])).createPackageContext(lpparam.packageName,2);
        //deleteFile(new File(createPackageContext.getDir("test",0).getParentFile(),"tinker"));
        //Log.d("chulhu","Load: "+lpparam.packageName);

        XposedHelpers.findAndHookMethod("com.tencent.mobileqq.qfix.QFixApplication", lpparam.classLoader, "attachBaseContext", Context.class,
                new XC_MethodHook() {
                    @Override
                    public void beforeHookedMethod(MethodHookParam param) {
                        AwLog.Log("Deleting hotpatch ");
                        deleteFile(((Context) param.args[0]).getFileStreamPath("hotpatch"));
                    }/*
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        //HookFrist(param);
                        super.afterHookedMethod(param);
                    }*/
                }
        );

        findAndHookMethod(findClass("com.tencent.common.app.BaseApplicationImpl",lpparam.classLoader), "onCreate", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                //Log.d("chulhu","Context load")
                HookFrist(param);
                super.afterHookedMethod(param);
            }
        });
        /*
        if(lpparam.packageName.equals(Adaptation.TIM)){
            XposedHelpers.findAndHookMethod(Application.class,"attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    HookFrist(param);
                    super.afterHookedMethod(param);
                }
            });
        }*/
        XposedHelpers.findAndHookMethod(Application.class,"attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                /*
                findAndHookMethod(View.class, "findViewById",int.class, new XC_MethodHook() {
                    @SuppressLint("ResourceType")
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        //AwLog.Log("AHKDFV findViewById=" + param.args[0]);
                        if((int)param.args[0]==0x7f0a18fa) {
                            AwLog.Log("AHKDFV "+ ReflectionUtil.getStackTraceString(new Throwable()));
                        }
                        super.afterHookedMethod(param);
                    }
                });*/
                Context context= (Context) param.args[0];
                ClassLoader classLoader=context.getClassLoader();
                super.afterHookedMethod(param);
            }
        });

        /*
        //Log.d("chulhu","Loaded: "+lpparam.packageName);
        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {

        });*/
        AwLog.Log("handleLoadPackage done");
    }
}
