package com.zhao.dice.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.zhao.dice.model.plugins.ReflectionUtil;
import com.zhao.dice.model.plugins.SettingEntry.ConfigReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import de.robv.android.xposed.XposedHelpers;

import static de.robv.android.xposed.XposedHelpers.callMethod;

public class Adaptation {
    static ArrayList<MethodInfo> DefaultMethods;
    public SharedPreferences sharedPreferences;
    public static final String QQ="com.tencent.mobileqq";
    public static final String TIM="com.tencent.tim";

    Adaptation(Context context){
        this.context=context;
        this.classLoader=context.getClassLoader();
        this.QQpackagename=context.getPackageName();
        this.QQversion=Adaptation.getQQVersion(context);
        this.sharedPreferences=ConfigReader.getSharedPreferences(this);
    }
    public static class MethodInfo {
        boolean best;//是否为最佳函数（与适配版本[完全]相同）
        public long Version;
        public String ClassName;
        public String MethodName;
        public Class Clazz;

        MethodInfo(long Version, String ClassName, String MethodName) {
            this.Version = Version;
            this.ClassName = ClassName;
            this.MethodName = MethodName;
        }
        void findClass(ClassLoader classLoader){
            try {
                this.Clazz = Adaptation.findClass(this.ClassName,classLoader);
            }catch (XposedHelpers.ClassNotFoundError e){
                AwLog.Log("ClassNotFoundError! ClassName:"+ClassName);
                this.Clazz = null;
            }
        }
    }

    /*
    #!/bin/sh
    #自动分析脚本 FOR QQ
    for i in *.apk;  do
        aapt dump badging $i | grep "package:"
        apktool d --no-res $i -o ./apktemp > /dev/null

        echo "Method_FlashPictureDecoder:"
        grep -r -B 60 "\"ENCRYPT:\"" | grep "method public static" | grep "(Ljava/lang/String;)Z"

        echo "Method_MessageReceiver:"
        grep -r ".method public static .(Lcom/tencent/mobileqq/app/QQAppInterface;Lcom/tencent/mobileqq/data/MessageRecord;Z)Z"

        echo "Method_MessageSender:"
        grep -r ".method public static .(Lcom/tencent/mobileqq/app/QQAppInterface;Landroid/content/Context;Lcom/tencent/mobileqq/activity/aio/SessionInfo;Ljava/lang/String;Ljava/util/ArrayList;"|grep ";)\[J"

        echo "Class_SendMsgParams SEE( ):"
        grep -r ".method public setSendMsgParams("

        echo "Method_MessageRecordBuilder:"
        grep -r ".method public static"| grep "(I)Lcom/tencent/mobileqq/data/MessageRecord;"

        echo "Method_ContactUtils_getBuddyName:"
        grep -r -B 120 "iget-object v., v., Lcom/tencent/mobileqq/data/Friends;->remark:Ljava/lang/String;" | grep "method public static .(Lcom/tencent/mobileqq/app/QQAppInterface;Ljava/lang/String;Z)Ljava/lang/String;"

        echo "Method_ContactUtils_getDiscussionMemberShowName:"
        grep -r -B 60 "getDiscussionMemberShowName uin is null" | grep "method public static .(Lcom/tencent/mobileqq/app/QQAppInterface;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"

        echo "Method_ChatAddAt"
        grep -r "iget-object v., p., Lcom/tencent/mobileqq/activity/BaseChatPie;->.:Lcom/tencent/mobileqq/activity/aio/SessionInfo;" -B 10 | grep ".method public .(Z)V"

        echo "Method_GiftPlayer"
        grep -r -B 45 "play TroopGiftAnimation Start,packageId:" | grep "method public .(Lcom/tencent/mobileqq/data/MessageForDeliverGiftTips;)V"


        echo "Methods_GetTroopInfo:"
        grep -r -A 100 "\"getTroopMemberName tmi == null\""  | grep "(Ljava/lang/String;)Lcom/tencent/mobileqq/data/TroopInfo;"        rm -rf ./apktemp

        rm -rf ./apktemp
    done





    #!/bin/sh
    #自动分析脚本 FOR TIM
    for i in *.apk;  do
        aapt dump badging $i | grep "package:"
        apktool d --no-res $i -o ./apktemp > /dev/null

        echo "Method_FlashPictureDecoder:"
        grep -r -B 60 "\"ENCRYPT:\"" | grep "method public static" | grep "(Ljava/lang/String;)Z"

        echo "Method_MessageReceiver:"
        grep -r ".method public static .(Lcom/tencent/mobileqq/app/QQAppInterface;Lcom/tencent/mobileqq/data/MessageRecord;Z)Z"

        echo "Method_MessageSender:"
        grep -r ".method public static .(Lcom/tencent/mobileqq/app/QQAppInterface;Landroid/content/Context;Lcom/tencent/mobileqq/activity/aio/SessionInfo;Ljava/lang/String;Ljava/util/ArrayList;"|grep ";)\[J"

        echo "Class_SendMsgParams SEE( ):"
        grep -r ".method public setSendMsgParams("

        echo "Method_MessageRecordBuilder:"
        grep -r ".method public static"| grep "(I)Lcom/tencent/mobileqq/data/MessageRecord;"

        echo "Method_ContactUtils_getBuddyName:"
        grep -r -B 120 "iget-object v., v., Lcom/tencent/mobileqq/data/Friends;->remark:Ljava/lang/String;" | grep "method public static .(Lcom/tencent/mobileqq/app/QQAppInterface;Ljava/lang/String;Z)Ljava/lang/String;"

        echo "Method_ContactUtils_getDiscussionMemberShowName:"
        grep -r -B 60 "getDiscussionMemberShowName uin is null" | grep "method public static .(Lcom/tencent/mobileqq/app/QQAppInterface;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"

        echo "Methods_GetTroopInfo:"
        grep -r -A 100 "\"getTroopMemberName tmi == null\""  | grep "(Ljava/lang/String;)Lcom/tencent/mobileqq/data/TroopInfo;"        rm -rf ./apktemp
    done
    * */

    public ClassLoader classLoader;
    public Context context;
    private Object mAppInterface;
    private Object mTroopManager;
    private Object mMessageFacade;
    private Object mFileManagerEngine;
    private Object mEntityManager;
    private Object mMixedMsgManager;
    private Object mTroopHandler;

    public String QQpackagename;
    public long QQversion;

    //消息接收函数
    private static final ArrayList Methods_MessageReceiver = new ArrayList();
    public MethodInfo Method_MessageReceiver;//最佳函数

    //消息发送函数
    private static final ArrayList Methods_MessageSender = new ArrayList();
    public MethodInfo Method_MessageSender;//最佳函数

    private static final ArrayList Classes_SendMsgParams = new ArrayList();
    public MethodInfo Class_SendMsgParams;


    private static final ArrayList Classes_FormSimpleItem= new ArrayList();
    public MethodInfo Class_FormSimpleItem;

    private static final ArrayList Methods_ContactUtils_getDiscussionMemberShowName = new ArrayList();
    public MethodInfo Method_ContactUtils_getDiscussionMemberShowName;

    private static final ArrayList Methods_ContactUtils_getBuddyName = new ArrayList();
    public MethodInfo Method_ContactUtils_getBuddyName;


    private static final ArrayList Methods_GetTroopMemberInfo = new ArrayList();
    public MethodInfo Method_GetTroopMemberInfo;

    private static final ArrayList Methods_GetTroopInfo = new ArrayList();
    public MethodInfo Method_GetTroopInfo;





    /*一些固定的class*/
    public Class Class_QQAppInterface;
    public Class Class_SessionInfo;
    public Class Class_MessageRecord;
    public Class Class_MessageForReplyText$SourceMsgInfo;
    public Class Class_HotChatFlashPicActivity;
    public Class Class_BaseApplicationImpl;
    public Class Class_QQMessageFacade;
    public Class Class_RevokeMsgInfo;

    public Class Class_MessageForDeliverGiftTips;

    public Class Class_QQSettingSettingActivity;
    public Class Class_AppRuntime;
    public Class Class_BaseActivity;
    public Class Class_TroopInfo;

    public Class Class_FileManagerEngine;
    public Class Class_FileManagerEntity;

    public Class Class_ChatActivityFacade;

    public Class Class_Friends;
    public Class Class_EntityManager;

    public Class Class_TroopMemberCardInfo;

    public String getAccount(){
        String account=(String) callMethod(getAppInterface(),"getAccount");
        //AwLog.Log("getAccount="+account);
        return account;
    }
    public Object getBusinessHandler(int i){
        return callMethod(getAppInterface(),"getBusinessHandler",i);
    }
    public Object getTroopHandler(){//TroopHandler=20
        if(mTroopHandler==null)
            mTroopHandler=getBusinessHandler(20);
        return mTroopHandler;
    }
    public Object getMessageFacade(){
        if(mMessageFacade==null)
            mMessageFacade= ReflectionUtil.getObjectField(getAppInterface(),null,Class_QQMessageFacade);
        return mMessageFacade;
    }
    public Object getFileManagerEngine(){
        if(mFileManagerEngine==null) {
            Method m = ReflectionUtil.getMethod(this.Class_QQAppInterface, null, this.Class_FileManagerEngine);
            mFileManagerEngine=ReflectionUtil.invokeMethod(m,this.mAppInterface);
        }
        return mFileManagerEngine;
    }
    public Object getEntityManager(){
        if(mEntityManager==null) {
            mEntityManager=ReflectionUtil.getObjectField(getAppInterface(),null,Class_EntityManager);
        }
        return mEntityManager;
    }
    public Object getTroopManager(){
        if(mTroopManager==null)
            mTroopManager=callMethod(getAppInterface(),"getManager",0x34);
        //AwLog.Log("getTroopManager="+mTroopManager);
        return mTroopManager;
    }
    public Object getMixedMsgManager(){
        if(mMixedMsgManager==null)
            mMixedMsgManager=callMethod(getAppInterface(),"getManager",174);
        return mMixedMsgManager;
    }
    public Object getAppInterface(){
        if(mAppInterface==null)
            mAppInterface= callMethod(context,"getRuntime");
        //AwLog.Log("getAppInterface="+mAppInterface);
        return mAppInterface;
    }
    public void setAppInterface(Object appInterface){
        this.mAppInterface=appInterface;
        //AwLog.Log("setAppInterface="+mAppInterface);
    }
    public static long getQQVersion(Context context){
        final long QQversion;
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT >= 28)
            QQversion = info.getLongVersionCode();
        else
            QQversion=info.versionCode;
        return QQversion;
    }
    //0x7f0f1360 2131694432 撤回消息
    /*根据版本号找出适配的函数*/
    public static MethodInfo SelectBestMethod(ClassLoader classLoader,long QQVersion,ArrayList<MethodInfo> methods) {//根据包版本号返回最合适的函数名称
        MethodInfo methodinfo=null;
        //AwLog.Log("methods.size()="+methods.size());
        if(methods.size()==0)
            return null;
        //匹配原则：保证函数库版本尽可能大，但函数库版本需要小于或等于QQ版本
        for (int i = 0; i < methods.size(); i++) {
            methodinfo = methods.get(i);
            if (methodinfo.Version == QQVersion) {
                methodinfo.best=true;
                break;
            } else if (methodinfo.Version > QQVersion) {
                //AwLog.Log("i="+i);
                if(i==0){
                    methodinfo = methods.get(0);//无法回退，直接用最旧版本
                }else{
                    methodinfo = methods.get(i-1);//函数库版本大于QQ版本，让函数库回退到较旧版本
                }
                //Log.d("chulhu","找到函数："+QQVersion+" "+methodinfo.Version+" "+methodinfo.ClassName+" "+methodinfo.MethodName);
                //is_found=true;
                break;
            }
        }/*
        if(is_found)
            Log.d("chulhu","找到函数："+QQVersion+" "+methodinfo.Version+" "+methodinfo.ClassName+" "+methodinfo.MethodName);
        else
            Log.d("chulhu","无法匹配函数!版本不支持.");*/
        methodinfo.findClass(classLoader);
        return methodinfo;
    }
    private static void JSONArray2ArrayList(JSONArray a,ArrayList<MethodInfo> b){

        for(int i=0;i<a.length();i++){
            JSONObject t= null;
            try {
                t = a.getJSONObject(i);
                b.add(new MethodInfo(t.getLong("v"),t.getString("c"),t.optString("m",null)));
            } catch (JSONException e) {
                Log.e("chulhu","e:"+e.getMessage());
                e.printStackTrace();
            }
        }

    }
    private static boolean initMethods(Adaptation adaptation){//加载所有适配的类名与函数名
        ConfigReader.initConfig(adaptation);
        try {
            JSONObject all=new JSONObject(ConfigReader.readMethodsJSON(adaptation));
            JSONArray2ArrayList(all.getJSONArray("Classes_SendMsgParams"),Classes_SendMsgParams);
            JSONArray2ArrayList(all.getJSONArray("Classes_FormSimpleItem"),Classes_FormSimpleItem);
            JSONArray2ArrayList(all.getJSONArray("Methods_MessageReceiver"),Methods_MessageReceiver);
            JSONArray2ArrayList(all.getJSONArray("Methods_MessageSender"),Methods_MessageSender);
            JSONArray2ArrayList(all.getJSONArray("Methods_ContactUtils_getDiscussionMemberShowName"),Methods_ContactUtils_getDiscussionMemberShowName);
            JSONArray2ArrayList(all.getJSONArray("Methods_ContactUtils_getBuddyName"),Methods_ContactUtils_getBuddyName);
            JSONArray2ArrayList(all.getJSONArray("Methods_GetTroopMemberInfo"),Methods_GetTroopMemberInfo);
            JSONArray2ArrayList(all.getJSONArray("Methods_GetTroopInfo"),Methods_GetTroopInfo);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    static Class findClass(String clazz,ClassLoader cl){
        try {
            return XposedHelpers.findClass(clazz,cl);
        }catch (Throwable e){
            AwLog.Log("ClassNotFound:"+clazz);
        }
        return null;
    }
    //函数适配器
    public Adaptation SelectBestMethod(ClassLoader cl) {
        // Bind to LocalService 跨进程

        AwLog.Log("init this");
        this.initMethods(this);//根据包名，初始化所有被混淆的类名函数名
        AwLog.Log("init Classes");
        this.Class_QQAppInterface=findClass("com.tencent.mobileqq.app.QQAppInterface",cl);
        this.Class_SessionInfo=findClass("com.tencent.mobileqq.activity.aio.SessionInfo",cl);
        this.Class_MessageRecord = findClass("com.tencent.mobileqq.data.MessageRecord", cl);
        this.Class_MessageForReplyText$SourceMsgInfo=findClass("com.tencent.mobileqq.data.MessageForReplyText$SourceMsgInfo",cl);
        this.Class_HotChatFlashPicActivity=findClass("com.tencent.mobileqq.dating.HotChatFlashPicActivity",cl);
        this.Class_BaseApplicationImpl=findClass("com.tencent.common.app.BaseApplicationImpl",cl);
        this.Class_QQMessageFacade=findClass("com.tencent.mobileqq.app.message.QQMessageFacade",cl);
        this.Class_RevokeMsgInfo=findClass("com.tencent.mobileqq.revokemsg.RevokeMsgInfo",cl);
        this.Class_AppRuntime=findClass("mqq.app.AppRuntime",cl);
        this.Class_BaseActivity=findClass("com.tencent.mobileqq.app.BaseActivity",cl);
        this.Class_MessageForDeliverGiftTips=findClass("com.tencent.mobileqq.data.MessageForDeliverGiftTips",cl);
        this.Class_TroopInfo=findClass("com.tencent.mobileqq.data.TroopInfo",cl);
        this.Class_FileManagerEngine=findClass("com.tencent.mobileqq.filemanager.app.FileManagerEngine",cl);
        this.Class_FileManagerEntity=findClass("com.tencent.mobileqq.filemanager.data.FileManagerEntity",cl);
        //群名片信息类
        this.Class_TroopMemberCardInfo=findClass("com.tencent.mobileqq.data.TroopMemberCardInfo",cl);
        //好友信息类
        this.Class_Friends=findClass("com.tencent.mobileqq.data.Friends",cl);
        //APP数据库管理类
        this.Class_EntityManager=findClass("com.tencent.mobileqq.persistence.EntityManager",cl);
        //SendMsgParams类（发送的消息需要附加的参数，单独回复某消息需要）
        this.Class_SendMsgParams=SelectBestMethod(cl,QQversion,Classes_SendMsgParams);
        //setting菜单类
        //Log.d("chulhu","loading FormSimpleItem");
        this.Class_FormSimpleItem=SelectBestMethod(cl,QQversion,Classes_FormSimpleItem);

        this.Class_QQSettingSettingActivity=findClass("com.tencent.mobileqq.activity.QQSettingSettingActivity",cl);

        AwLog.Log("init Methods...");
        //消息接收函数
        this.Method_MessageReceiver=SelectBestMethod(cl,QQversion,Methods_MessageReceiver);
        //消息发送函数
        this.Method_MessageSender=SelectBestMethod(cl,QQversion,Methods_MessageSender);
        if(Method_MessageSender!=null)
            this.Class_ChatActivityFacade=Method_MessageSender.Clazz;
        //昵称读取函数
        this.Method_ContactUtils_getDiscussionMemberShowName = SelectBestMethod(cl,QQversion,Methods_ContactUtils_getDiscussionMemberShowName);
        this.Method_ContactUtils_getBuddyName = SelectBestMethod(cl,QQversion,Methods_ContactUtils_getBuddyName);
        //群成员信息获取函数
        this.Method_GetTroopMemberInfo=SelectBestMethod(cl,QQversion,Methods_GetTroopMemberInfo);
        //群聊信息获取函数
        this.Method_GetTroopInfo=SelectBestMethod(cl,QQversion,Methods_GetTroopInfo);
        return this;
    }

}
