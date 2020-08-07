package com.zhao.dice.model.plugins.SettingEntry;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


import com.zhao.dice.model.Adaptation;
import com.zhao.dice.model.AwLog;
import com.zhao.dice.model.BuildConfig;
import com.zhao.dice.model.plugins.ReflectionUtil;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class SettingEntryXposed {
    public static final int R_ID_SETTING_ENTRY = 0x300AFF71;
    public static void init(final Adaptation adaptation) {
        //Log.d("chulhu","Setting initing!");
        AwLog.Log("Loading Setting...");
        AwLog.Log("adaptation.Class_QQSettingSettingActivity="+adaptation.Class_QQSettingSettingActivity);
        XposedHelpers.findAndHookMethod(adaptation.Class_QQSettingSettingActivity, "doOnCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                //Log.d("chulhu","Setting!");
                try {
                    View itemRef = (View) ReflectionUtil.getObjectField(param.thisObject, "a", adaptation.Class_FormSimpleItem.Clazz);
                    if (itemRef == null) {
                        itemRef = (View) ReflectionUtil.getFirstNSFByType(param.thisObject, adaptation.Class_FormSimpleItem.Clazz);
                    }else if (itemRef == null) {
                        AwLog.Log("ERROR! itemRef==null");
                        return;
                    }
                    AwLog.Log("itemRef="+itemRef);
                    View item = (View) XposedHelpers.newInstance(itemRef.getClass(), param.thisObject);
                    item.setId(R_ID_SETTING_ENTRY);
                    {
                        Method method = ReflectionUtil.getMethod(item, "setLeftText", null, CharSequence.class);
                        ReflectionUtil.invokeMethod(method, item, BuildConfig.APPNAME);
                        method = ReflectionUtil.getMethod(item, "setRightText", null, CharSequence.class);
                        ReflectionUtil.invokeMethod(method, item, BuildConfig.VERSION_NAME);
                        method = ReflectionUtil.getMethod(item, "setBgType", null, int.class);
                        ReflectionUtil.invokeMethod(method, item, 2);
                    }
                    //重新连service保证能启动设置。
                    //ModelService.BindModelService(adaptation.context,adaptation.mConnection);
                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //打开设置ativity
                            SettingDialog.OpenDialog(v.getContext(),adaptation.getAccount());
                            //SettingDialog.showAlterDialog(v.getContext());
                        }
                    });
                    ViewGroup list = (ViewGroup) itemRef.getParent();
                    ViewGroup.LayoutParams reflp;
                    if (list.getChildCount() == 1) {
                        //junk!
                        list = (ViewGroup) list.getParent();
                        reflp = ((View) itemRef.getParent()).getLayoutParams();
                    } else {
                        reflp = itemRef.getLayoutParams();
                    }
                    ViewGroup.LayoutParams lp = null;
                    if (reflp != null) {
                        lp = new ViewGroup.LayoutParams(MATCH_PARENT, /*reflp.height*/WRAP_CONTENT);
                    }
                        /*
                        int index = 0;
                        int account_switch = list.getContext().getResources().getIdentifier("account_switch", "id", list.getContext().getPackageName());
                        try {
                            if (account_switch > 0) {
                                View accountItem = (View) ((View) list.findViewById(account_switch)).getParent();
                                for (int i = 0; i < list.getChildCount(); i++) {
                                    if (list.getChildAt(i) == accountItem) {
                                        index = i + 1;
                                        break;
                                    }
                                }
                            }
                            if (index > list.getChildCount()) index = 0;
                        } catch (NullPointerException ignored) {
                        }*/
                    list.addView(item, 0, lp);
                } catch (Throwable e) {
                    Log.d("chulhu","Setting ERROR!"+ReflectionUtil.getStackTraceString(e));
                }
                //Log.d("chulhu","Setting loaded!");
            }
        });
        AwLog.Log("Loaded Setting");
        //Log.d("chulhu","Setting inited!");
    }
}
