package com.zhao.dice.model;

import android.content.Context;
import android.text.TextUtils;

import com.zhao.dice.model.plugins.QQMessage.QQMessageDecoder;
import com.zhao.dice.model.plugins.ReflectionUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findMethodBestMatch;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

public class QQFunction {
    public static Object PBUFieldGet(Adaptation adaptation,Object PBUField){
        return ReflectionUtil.invokeMethod(XposedHelpers.findMethodBestMatch(PBUField.getClass(),"get"),PBUField);
    }
    private static class Util{
        //生成SessionInfo
        private static Object createSessionInfo(Adaptation adaptation, String troopuin, String frienduin, int istroop){
            Object sessionInfo = XposedHelpers.newInstance(findClass("com.tencent.mobileqq.activity.aio.SessionInfo", adaptation.classLoader));
            //TIM 3.0.0 1080
            String field_frienduin;//群号或好友qq
            String field_istroop;//是否为群聊
            String field_troopuin;//群号
            if (istroop==1000)
                ReflectionUtil.setField(sessionInfo, "troopUin", troopuin, String.class);
            else if (istroop==1)
                ReflectionUtil.setField(sessionInfo, "troopUin", frienduin, String.class);
            if(adaptation.QQpackagename.equals(Adaptation.TIM) && adaptation.QQversion>=1080) {
                field_frienduin="ltK";
                field_istroop="yM";
                field_troopuin="mCn";
            }else{
                //QQ 8.3.0 或 TIM 2.5.5
                field_frienduin="a";
                field_istroop="a";
                field_troopuin="c";
            }
            ReflectionUtil.setField(sessionInfo, field_frienduin, frienduin, String.class);
            ReflectionUtil.setField(sessionInfo, field_istroop, istroop, int.class);
            ReflectionUtil.setField(sessionInfo, field_troopuin, troopuin, String.class);
            return sessionInfo;
        }
        private static void sendMixMessage(Adaptation adaptation, String frienduin, int istroop,ArrayList pictures,String text){

            Object mMixedMsgManager=adaptation.getMixedMsgManager();
            //Lcom/tencent/mobileqq/mixedmsg/MixedMsgManager;->a(Lcom/tencent/mobileqq/app/QQAppInterface;Ljava/lang/String;Ljava/lang/String;ILjava/util/ArrayList;ZLjava/lang/String;Ljava/util/ArrayList;Lcom/tencent/mobileqq/data/MessageForReplyText$SourceMsgInfo;)V
            Method mixedMessageSender=ReflectionUtil.getMethod(mMixedMsgManager,"a",void.class,adaptation.Class_QQAppInterface,String.class,String.class,int.class,ArrayList.class,boolean.class,String.class,ArrayList.class,adaptation.Class_MessageForReplyText$SourceMsgInfo);
            AwLog.Log("mixedMessageSender="+mixedMessageSender);
            String troopcode=null;
            if(istroop!=0){
                troopcode=Troop.Get.code(adaptation,frienduin);
            }
            ReflectionUtil.invokeMethod(mixedMessageSender,mMixedMsgManager,adaptation.getAppInterface(),frienduin,troopcode,istroop,pictures,false,text,new ArrayList<>(),null);

        }
        //发送语音信息（不检查语音文件是否存在）
        private static void voice(Adaptation adaptation, String frienduin, int istroop, String amrPath){
            Method voice_create_method=ReflectionUtil.getStaticMethod(adaptation.Class_ChatActivityFacade,"a",adaptation.Class_MessageRecord,adaptation.Class_QQAppInterface,String.class,adaptation.Class_SessionInfo,int.class,int.class);
            Object sessionInfo = createSessionInfo(adaptation,null,frienduin,istroop);
            if(Adaptation.QQ.equals(adaptation.QQpackagename)){
                //QQ 8.3.0
                Method voice_send_method=ReflectionUtil.getStaticMethod(adaptation.Class_ChatActivityFacade,"a",long.class,adaptation.Class_QQAppInterface,adaptation.Class_SessionInfo,String.class);
                ReflectionUtil.invokeStaticMethod(voice_send_method,
                        adaptation.getAppInterface(),
                        sessionInfo,
                        amrPath);
            }else if(Adaptation.TIM.equals(adaptation.QQpackagename)){
                //TIM 3.0.0
                //a(Lcom/tencent/mobileqq/app/QQAppInterface;ILjava/lang/String;Ljava/lang/String;JZIIZIIZ)V
                Object recored=ReflectionUtil.invokeStaticMethod(voice_create_method,
                        adaptation.getAppInterface(),
                        amrPath,
                        sessionInfo,
                        -2,
                        0);
                QQMessageDecoder.BaseInfo msginfo= new QQMessageDecoder(recored).GetBaseInfo();
                Method voice_send_method=ReflectionUtil.getStaticMethod(adaptation.Class_ChatActivityFacade,"a",void.class,adaptation.Class_QQAppInterface,int.class,String.class,String.class,long.class,boolean.class,int.class,int.class,boolean.class,int.class,int.class,boolean.class);
                ReflectionUtil.invokeStaticMethod(voice_send_method, adaptation.getAppInterface(),
                        istroop,
                        frienduin,
                        amrPath,
                        msginfo.uniseq,
                        false,
                        5000,
                        0, true, 0, 2, true);
            }
        }
        //把文件发到群
        private static void fileToTroop(Adaptation adaptation, String groupuin, String filePath){
            Object fileManagerEngine=adaptation.getFileManagerEngine();
            //Lcom/tencent/mobileqq/filemanager/app/FileManagerEngine;->hI(Ljava/lang/String;Ljava/lang/String;)Z
            //TIM3.0.0
            Method sendFileMethod=ReflectionUtil.getMethod(fileManagerEngine,"hI",boolean.class,String.class,String.class);
            if(sendFileMethod==null)
                return;//找不到函数，无法发文件
            //调用发送文件函数
            ReflectionUtil.invokeMethod(sendFileMethod,fileManagerEngine,filePath,groupuin);
        }
        //把文件发给好友
        private static void fileToFriend(Adaptation adaptation, String frienduin, String filePath){
            Object fileManagerEngine=adaptation.getFileManagerEngine();
            AwLog.Log("------发送私聊文件！fileManagerEngine="+fileManagerEngine);
            //Lcom/tencent/mobileqq/filemanager/app/FileManagerEngine;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IZ)Lcom/tencent/mobileqq/filemanager/data/FileManagerEntity;
            //TIM 3.0.0
            Method sendFileMethod=ReflectionUtil.getMethod(fileManagerEngine,"a",adaptation.Class_FileManagerEntity,String.class,String.class,String.class,int.class,boolean.class);
            AwLog.Log("------发送文件！sendFileMethod="+sendFileMethod);
            if(sendFileMethod==null)
                return;//找不到函数，无法发文件
            //调用发送文件函数
            ReflectionUtil.invokeMethod(sendFileMethod,fileManagerEngine,filePath,null,frienduin,0,true);
        }
    }
    public static class Troop{
        public static class Get{
            //获取群消息通知
            public static List<Object> getTroopNotice(Adaptation adaptation){
                Object mMessageFacade=adaptation.getMessageFacade();
                if(mMessageFacade==null)
                    return null;
                //Lcom/tencent/mobileqq/app/message/QQMessageFacade;->dy(Ljava/lang/String;I)Ljava/util/List;
                Method method=findMethodBestMatch(mMessageFacade.getClass(),"dy",String.class,int.class);
                return (List<Object>) ReflectionUtil.invokeMethod(method,mMessageFacade,"9986", 0);
            }
            //获取群信息
            public static Object info(Adaptation adaptation, String troopuin) {//troopcode
                Method m= ReflectionUtil.getMethod(adaptation.Method_GetTroopInfo.Clazz,adaptation.Method_GetTroopInfo.MethodName,adaptation.Class_TroopInfo,String.class);
                Object troopManager=adaptation.getTroopManager();
                return ReflectionUtil.invokeMethod(m,troopManager,troopuin);
            }
            //获取群代码（可以理解为第二个群号
            public static String code(Adaptation adaptation, String troopuin){//troopcode
                Object TroopInfo= info(adaptation,troopuin);
                if(TroopInfo==null)
                    return null;
                return (String) ReflectionUtil.getObjectField(TroopInfo,"troopcode",String.class);
            }
            //获取群聊管理员数组（下标0为群主
            public static String[] admin(Adaptation adaptation, String troopuin){//troopcode
                Object TroopInfo= info(adaptation,troopuin);
                if(TroopInfo==null)
                    return null;
                String Administrator=(String) ReflectionUtil.getObjectField(TroopInfo,"Administrator",String.class);
                String troopowneruin=(String) ReflectionUtil.getObjectField(TroopInfo,"troopowneruin",String.class);
                String[] Administrates=Administrator.split("\\|");
                String[] admin=new String[Administrates.length+1];
                System.arraycopy(Administrates,0,admin,1,Administrates.length);
                admin[0]=troopowneruin;
                return admin;
            }
            //获取群成员名字
            public static String memberName(Adaptation adaptation, String groupuin, String senderUin) {
                Object TroopManager=adaptation.getTroopManager();
                Object troopMemberInfo = XposedHelpers.callMethod(TroopManager,adaptation.Method_GetTroopMemberInfo.MethodName,groupuin, senderUin);
                String nickname = (String) ReflectionUtil.getObjectField(troopMemberInfo, "troopnick",String.class);
                if (TextUtils.isEmpty(nickname)) {
                    nickname = (String) ReflectionUtil.getObjectField(troopMemberInfo, "friendnick",String.class);
                }
                return nickname.replaceAll("\\u202E", "").trim();
            }
        }
        public static class Set{
            //退群保平安
            public static void dismiss(Adaptation adaptation, String troopuin) {
                Object mTroopHandler=adaptation.getTroopHandler();
                //Lcom/tencent/mobileqq/app/TroopHandler;->OL(Ljava/lang/String;)V
                Method method=XposedHelpers.findMethodBestMatch(mTroopHandler.getClass(),"OL",troopuin);
                ReflectionUtil.invokeMethod(method,mTroopHandler,troopuin);
            }
            public static void Memberinfo(Adaptation adaptation, String troopuin,String qq,String name) {
                //TIM 3.0.0
                Object troopHandler=adaptation.getTroopHandler();
                //Lcom/tencent/mobileqq/app/TroopHandler;->a(Ljava/lang/String;Ljava/util/ArrayList;Ljava/util/ArrayList;)V
                Method changeMemberInfo=ReflectionUtil.getMethod(troopHandler,"a",void.class,String.class,ArrayList.class,ArrayList.class);
                Object mTroopMemberCardInfo=XposedHelpers.newInstance(adaptation.Class_TroopMemberCardInfo);
                //依据 关键词 "modify_name" 上翻 Lcom/tencent/mobileqq/data/TroopMemberCardInfo;-><init>()V
                ReflectionUtil.setField(mTroopMemberCardInfo,"name",name,String.class);
                ReflectionUtil.setField(mTroopMemberCardInfo,"memberuin",qq,String.class);
                ReflectionUtil.setField(mTroopMemberCardInfo,"troopuin",troopuin,String.class);
                ReflectionUtil.setField(mTroopMemberCardInfo,"email","",String.class);
                ReflectionUtil.setField(mTroopMemberCardInfo,"memo","",String.class);
                ReflectionUtil.setField(mTroopMemberCardInfo,"tel","",String.class);
                ReflectionUtil.setField(mTroopMemberCardInfo,"sex",(byte)-1,byte.class);
                XposedUtil.getObjAttr(mTroopMemberCardInfo);
                ArrayList<Object> info1=new ArrayList<>();
                info1.add(mTroopMemberCardInfo);
                ArrayList<Integer> info2=new ArrayList<>();
                info2.add(1);//猜测：可能是istroop
                ReflectionUtil.invokeMethod(changeMemberInfo,troopHandler,troopuin,info1,info2);
            }
            public static boolean hasPayPrivilege(long j, int i) {
                return ((j | 32) & ((long) i)) != 0;
            }
            //同意入群
            public static void inviteAgree(final Adaptation adaptation, Object structMsg){
                //AwLog.Log("正在同意...1");
                int i2=1;
                int i3 = (int) PBUFieldGet(adaptation,getObjectField(structMsg,"msg_type"));
                long j = (long) PBUFieldGet(adaptation,getObjectField(structMsg,"msg_seq"));
                long j2 = (long) PBUFieldGet(adaptation,getObjectField(structMsg,"req_uin"));
                Object msg=getObjectField(structMsg,"msg");
                int i4 = (int) PBUFieldGet(adaptation,getObjectField(msg,"sub_type"));
                int i5 = (int) PBUFieldGet(adaptation,getObjectField(msg,"src_id"));
                int i6 = (int) PBUFieldGet(adaptation,getObjectField(msg,"sub_src_id"));
                int i7 = (int) PBUFieldGet(adaptation,getObjectField(msg,"group_msg_type"));
                if((Integer) i4==1&& (Integer) i7==2) {//可处理的群邀请
                    AwLog.Log("正在同意...2");
                    List<Object> list = (List<Object>) PBUFieldGet(adaptation, getObjectField(msg, "actions"));
                    AwLog.Log("正在同意...3 list=" + list);
                    if (list != null && i2 < list.size()) {
                        AwLog.Log("正在同意...4");
                        Object handler = adaptation.getBusinessHandler(0);
                        AwLog.Log("正在同意...5 handler=" + handler);
                        //Lcom/tencent/mobileqq/app/MessageHandler;->cpu()Lcom/tencent/mobileqq/app/message/SystemMessageProcessor;
                        Method cpu = XposedHelpers.findMethodBestMatch(handler.getClass(), "cpu");
                        AwLog.Log("正在同意...6 cpu=" + cpu);
                        Object SystemMessageProcessor = ReflectionUtil.invokeMethod(cpu, handler);
                        AwLog.Log("正在同意...7 SystemMessageProcessor=" + SystemMessageProcessor);
                        Object action_info = PBUFieldGet(adaptation, getObjectField(list.get(i2), "action_info"));
                        AwLog.Log("正在同意...8 action_info=" + action_info);
                        Method agree = findMethodBestMatch(SystemMessageProcessor.getClass(), "a", int.class, long.class, long.class, int.class, int.class, int.class, int.class, action_info.getClass(), int.class);
                        AwLog.Log("正在同意...9 agree=" + agree);
                        ReflectionUtil.invokeMethod(agree, SystemMessageProcessor, i3, j, j2, i4, i5, i6, i7, action_info, i2);
                        AwLog.Log("正在同意...执行完成!");
                    }
                }
            }
            public static void handleAll(final Adaptation adaptation){
                //AwLog.Log("自动同意进群开始");
                //QQFunction.Troop.Set.inviteAgree(adaptation,"901488731");
                List<Object> obj=QQFunction.Troop.Get.getTroopNotice(adaptation);
                if(obj==null) {
                    AwLog.Log("agreeInviteAll WARNING! getTroopNotice==NULL");
                    return;
                }
                //Lcom/tencent/mobileqq/systemmsg/MessageForSystemMsg;->getSystemMsg()Ltencent/mobileim/structmsg/structmsg$StructMsg;
                Method mth=XposedHelpers.findMethodBestMatch(findClass("com.tencent.mobileqq.systemmsg.MessageForSystemMsg",adaptation.classLoader),"getSystemMsg");
                for(Object o : obj) {
                    Object structMsg = ReflectionUtil.invokeMethod(mth, o);
                    Object msg_seq=getObjectField(structMsg,"msg_seq");

                    Object msg=getObjectField(structMsg,"msg");
                    Object sub_type=QQFunction.PBUFieldGet(adaptation,getObjectField(msg,"sub_type"));//为1代表可以处理 其他值代表无法处理
                    Object group_msg_type=QQFunction.PBUFieldGet(adaptation,getObjectField(msg,"group_msg_type"));//为2代表群邀请 为1代表加群请求
                    Object group_name=QQFunction.PBUFieldGet(adaptation,getObjectField(msg,"group_name"));//群名称
                    Object group_code=QQFunction.PBUFieldGet(adaptation,getObjectField(msg,"group_code"));//群号
                    Object msg_describe=QQFunction.PBUFieldGet(adaptation,getObjectField(msg,"msg_describe"));//动作描述
                    Object action_uin=QQFunction.PBUFieldGet(adaptation,getObjectField(msg,"action_uin"));//操作者
                    Object msg_detail=QQFunction.PBUFieldGet(adaptation,getObjectField(msg,"msg_detail"));//动作细节
                    StringBuilder sb=new StringBuilder();
                    for(Field f : msg.getClass().getDeclaredFields()){
                        try {
                            sb.append(f.getName()).append("=").append(QQFunction.PBUFieldGet(adaptation, f.get(msg))).append(" ");
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    AwLog.Log(sb.toString());
                    if("邀请你加群".equals(msg_describe)&&!"已同意该邀请".equals(msg_detail)){//可处理的群邀请
                        //AwLog.Log("正在同意...group_code="+group_code);
                        Class cls=XposedHelpers.findClassIfExists("com.tencent.mobileqq.activity.contact.troop.TroopNotificationUtils$TroopPrivilegeCallback",adaptation.classLoader);
                        Method met=findMethodBestMatch(findClass("com.tencent.mobileqq.activity.TroopRequestActivity",adaptation.classLoader),"a",Context.class,adaptation.Class_QQAppInterface,String.class,findClass("tencent.mobileim.structmsg.structmsg$StructMsg",adaptation.classLoader), cls);
                        //AwLog.Log("inviteAgree="+met);
                        //AwLog.Log("TroopRequestActivity cls="+cls);
                        Object callback = Proxy.newProxyInstance(adaptation.classLoader, new Class[]{cls}, new InvocationHandler(){
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                QQFunction.Troop.Set.inviteAgree(adaptation,args[1]);
                                return null;
                            }
                        });
                        //AwLog.Log("structMsg="+structMsg.getClass());
                        //AwLog.Log("group_code="+group_code.getClass());
                        //Lcom/tencent/mobileqq/activity/TroopRequestActivity;->a(Landroid/content/Context;Lcom/tencent/mobileqq/app/QQAppInterface;Ljava/lang/String;Ltencent/mobileim/structmsg/structmsg$StructMsg;Lcom/tencent/mobileqq/activity/contact/troop/TroopNotificationUtils$TroopPrivilegeCallback;)V
                        ReflectionUtil.invokeStaticMethod(met,adaptation.context,adaptation.getAppInterface(),group_code.toString(),structMsg,callback);
                    }else if("已将你移出群".equals(msg_describe)) {//严重问题:会重复调用
                        AwLog.Log("操作者:"+action_uin+"群:"+group_code);
                    }
                    //AwLog.Log(group_msg_type+" "+sub_type+" "+group_name);
                }
                //AwLog.Log("自动同意进群结束");
            }
        }
    }
    public static class Sender{
        //发送文本化消息 text=发送文本 pics=发送图片
        public static void textAndPic(Adaptation adaptation, String frienduin, String selfuin, String troopuin, int istroop, String text, ArrayList<String> pics, QQMessageDecoder.BaseInfo replayTo) {
            if(pics!=null && pics.size()>0){
                AwLog.Log("试图发送图文混合消息...");
                try {
                    Util.sendMixMessage(adaptation, frienduin, istroop, pics, text);
                }catch (Throwable e){
                    AwLog.Log("sendMixMessage ERROR!"+e.getMessage());
                }
                AwLog.Log("发送混合消息结束...");
                return;
            }
            Method MessageSender=XposedHelpers.findMethodBestMatch(adaptation.Method_MessageSender.Clazz, adaptation.Method_MessageSender.MethodName,
                    adaptation.Class_QQAppInterface,
                    Context.class,
                    adaptation.Class_SessionInfo,
                    String.class,
                    ArrayList.class,
                    adaptation.Class_SendMsgParams.Clazz);

            Object qqAppInterface = XposedHelpers.callMethod(adaptation.context, "getAppRuntime",selfuin);
            Class MessageForReplyText$SourceMsgInfo_clazz=findClass("com.tencent.mobileqq.data.MessageForReplyText$SourceMsgInfo", adaptation.classLoader);
            Object MessageForReplyText$SourceMsgInfo=XposedHelpers.newInstance(MessageForReplyText$SourceMsgInfo_clazz);
            Object sendMsgParams = XposedHelpers.newInstance(adaptation.Class_SendMsgParams.Clazz);

            if(replayTo!=null){
                ReflectionUtil.setField(MessageForReplyText$SourceMsgInfo, "mSourceMsgSeq", replayTo.msgseq,long.class);
                ReflectionUtil.setField(MessageForReplyText$SourceMsgInfo, "mSourceMsgSenderUin", Long.valueOf(replayTo.senderuin),long.class);
                ReflectionUtil.setField(MessageForReplyText$SourceMsgInfo, "mSourceMsgText", replayTo.msg,String.class);
                ReflectionUtil.setField(MessageForReplyText$SourceMsgInfo, "mSourceMsgTime", (int)(replayTo.time),int.class);
                ReflectionUtil.setField(MessageForReplyText$SourceMsgInfo, "mSourceSummaryFlag", 1,int.class);
                //名字原来是 mSourceMsgInfo
                ReflectionUtil.setField(sendMsgParams, null, MessageForReplyText$SourceMsgInfo,MessageForReplyText$SourceMsgInfo_clazz);
                //ReflectionUtil.setField(sendMsgParams, "a", MessageForReplyText$SourceMsgInfo,MessageForReplyText$SourceMsgInfo_clazz);
            }
            Object sessionInfo =Util.createSessionInfo(adaptation,troopuin,frienduin,istroop);
            ReflectionUtil.invokeStaticMethod(MessageSender, qqAppInterface, adaptation.context, sessionInfo, text, new ArrayList<>(), sendMsgParams);

        }

        //发送语音（如果存在
        public static void voiceIfExist(Adaptation adaptation, String frienduin, int istroopint, String filepath){
            if (new File(filepath).exists()) {
                //发送语音
                Util.voice(adaptation,
                        frienduin,
                        istroopint,
                        filepath);
            }
        }
        //发送群文件/好友文件
        public static void file(Adaptation adaptation, String uin, int istroop, String filePath){
            if(istroop==1){
                //群聊
                Util.fileToTroop(adaptation,uin,filePath);
            }else if(istroop==0){
                //私聊
                Util.fileToFriend(adaptation,uin,filePath);
            }
            //暂不支持群私聊
        }

    }
}
