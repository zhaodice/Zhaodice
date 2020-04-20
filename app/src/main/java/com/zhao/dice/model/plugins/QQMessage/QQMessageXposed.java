package com.zhao.dice.model.plugins.QQMessage;

import android.content.Context;
import android.text.TextUtils;
import com.ZhaoDiceUitl.COCHelper;
import com.zhao.dice.model.Adaptation;
import com.zhao.dice.model.AwLog;
import com.zhao.dice.model.plugins.ReflectionUtil;
import com.zhao.dice.model.plugins.SettingEntry.ConfigReader;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class QQMessageXposed {
    private static final String VOICE_CMD_PATTERN_STRING="#[{]VOICE-(.*?.amr)[}]";
    private static final Pattern VOICE_CMD_PATTERN=Pattern.compile(VOICE_CMD_PATTERN_STRING);
    //private static XC_MethodHook MessageSender_callback;
    //static XC_MethodHook.Unhook MessageSender_Unhook;
    private static Method MessageSender;
    private static LongMessageReceiver longMessageReceiver=new LongMessageReceiver();
    static class LongMessageReceiver{
        Map<Integer,QQMessageDecoder.BaseInfo.LongMsgInfo[]> LongMessageReceiveCache=new HashMap<>();
        QQMessageDecoder.BaseInfo push(QQMessageDecoder.BaseInfo baseInfo){//将长消息进行缓存，当接收完毕后返回完整的消息内容
            QQMessageDecoder.BaseInfo.LongMsgInfo[] cache;
            QQMessageDecoder.BaseInfo.LongMsgInfo longmsginfo=baseInfo.longMsgInfo;
            if(LongMessageReceiveCache.containsKey(longmsginfo.longMsgId)) {
                AwLog.Log("LongMessageReceiver push got cache");
                cache = LongMessageReceiveCache.get(longmsginfo.longMsgId);
            }else{
                AwLog.Log("LongMessageReceiver push creating cache");
                cache=new QQMessageDecoder.BaseInfo.LongMsgInfo[5];
                LongMessageReceiveCache.put(longmsginfo.longMsgId, cache);
            }

            if(cache==null)
                return null;
            cache[longmsginfo.longMsgIndex]=longmsginfo;
            int count=0;
            for (QQMessageDecoder.BaseInfo.LongMsgInfo longMsgInfo : cache) {
                if (longMsgInfo == null) {
                    break;
                }
                count++;
            }
            if(count==longmsginfo.longMsgCount){//消息已完整接收
                LongMessageReceiveCache.remove(longmsginfo.longMsgId);
                StringBuilder completed_msg=new StringBuilder();
                for(int i=0;i<count;i++){
                    completed_msg.append(cache[i].msg);
                }
                return new QQMessageDecoder.BaseInfo(baseInfo.frienduin,completed_msg.toString(),baseInfo.senderuin,baseInfo.selfuin,baseInfo.istroopint,baseInfo.time,baseInfo.msgseq,baseInfo.msgUid,baseInfo.uniseq,baseInfo.baseReplayInfo);
            }else {
                AwLog.Log("接收长消息进度 " + (longmsginfo.longMsgIndex + 1) + "/" + longmsginfo.longMsgCount);
                AwLog.Log("接收缓存大小 " + count + "/" + longmsginfo.longMsgCount);
            }
            return null;
        }
    }
    static class QQDice implements Runnable{
        Adaptation adaptation;
        QQMessageDecoder.BaseInfo messageRecordBaseInfo;
        boolean is_admin;
        QQDice(Adaptation adaptation,QQMessageDecoder.BaseInfo messageRecordBaseInfo,boolean is_admin){
            this.adaptation=adaptation;
            this.messageRecordBaseInfo=messageRecordBaseInfo;
            this.is_admin=is_admin;
        }
        @Override
        public void run() {
            String userID;
            String groupID=null;
            boolean is_dice_open=true;
            if(messageRecordBaseInfo.istroopint==1){//群聊
                String white=COCHelper.helper_storage.getGlobalInfo(adaptation.getAccount(),"WHITE_LIST").trim();
                if(!TextUtils.isEmpty(white)) {
                    //设置了白名单
                    String[] list=white.split("\n");
                    boolean is_in_white=false;
                    for (String s : list) {
                        s=s.trim();
                        if (messageRecordBaseInfo.frienduin.equals(s)) {
                            //当前群在白名单里
                            is_in_white = true;
                            break;
                        }
                    }
                    if(!is_in_white){//由于设置了白名单，当前群不在白名单，故不操作
                        AwLog.Log("拒绝操作，因为不在白名单内。");
                        is_dice_open=false;
                    }
                }
                userID=messageRecordBaseInfo.senderuin;
                groupID=messageRecordBaseInfo.frienduin;
            }else{
                userID=messageRecordBaseInfo.frienduin;
            }
            boolean handle_my_self=ConfigReader.readBoolean(adaptation,ConfigReader.CONFIG_KEY_SWITCH_HANDLE_MYSELF,false);
            if(!handle_my_self&&userID.equals(adaptation.getAccount()))
                return;

            COCHelper.helper_interface_in in_data = new COCHelper.helper_interface_in(adaptation,messageRecordBaseInfo.msg,groupID,userID,messageRecordBaseInfo.selfuin,messageRecordBaseInfo.time,is_dice_open,is_admin);
            COCHelper.helper_interface_out out_data = COCHelper.cmd(in_data);
            if (out_data == null) {
                AwLog.Log("Cannot handle:" + messageRecordBaseInfo.msg);
            } else {
                if(TextUtils.isEmpty(out_data.msg)) {
                    out_data.msg="WARNING! 指令已执行，但自定义回复内容为空，请检查配置。";
                }else{
                    String[] files = voice_cmd_decoder(out_data.msg);
                    if (files.length > 0) {
                        out_data.msg = out_data.msg.replaceAll(VOICE_CMD_PATTERN_STRING, "");
                        String filepath = ConfigReader.PATH_SOUND_ROBOT + "/" + files[0];
                        QQSendVoiceFile(adaptation, messageRecordBaseInfo.frienduin, messageRecordBaseInfo.istroopint, filepath);
                    }
                }
                if (messageRecordBaseInfo.istroopint == 1){//消息源是群聊
                    if(out_data.forcePrivateChat) {//是否强制私聊
                        AwLog.Log("强制群私聊！");
                        //开启群私聊
                        String troopcode=GetTroopcode(adaptation,messageRecordBaseInfo.frienduin);
                        QQsend(adaptation, messageRecordBaseInfo.senderuin, messageRecordBaseInfo.selfuin, troopcode, 1000, out_data.msg, null);
                    }else{
                        AwLog.Log("群聊！");
                        QQsend(adaptation, messageRecordBaseInfo.frienduin, messageRecordBaseInfo.selfuin, null, 1, out_data.msg, out_data.isrelay?messageRecordBaseInfo:null);
                    }
                }else if (messageRecordBaseInfo.istroopint == 1000 || messageRecordBaseInfo.istroopint == 0){ //消息源是群私聊
                    AwLog.Log("私聊处理！");
                    QQsend(adaptation, messageRecordBaseInfo.frienduin, messageRecordBaseInfo.selfuin, messageRecordBaseInfo.senderuin, messageRecordBaseInfo.istroopint, out_data.msg, null);
                }
                AwLog.Log("sent");
            }
        }
    }
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
    //istroop=1000 群私聊 istroop=0私聊 istroop=1群聊
    private static void QQsend(Adaptation adaptation, String frienduin, String selfuin, String troopuin, int istroop, String s, QQMessageDecoder.BaseInfo replayTo) {
        Object qqAppInterface = XposedHelpers.callMethod(adaptation.context, "getAppRuntime",selfuin);

        //Object sessionInfo =

        //XposedUtil.getObjAttr(sessionInfo);
        Class MessageForReplyText$SourceMsgInfo_clazz=findClass("com.tencent.mobileqq.data.MessageForReplyText$SourceMsgInfo", adaptation.classLoader);
        Object MessageForReplyText$SourceMsgInfo=XposedHelpers.newInstance(MessageForReplyText$SourceMsgInfo_clazz);
        Object sendMsgParams = XposedHelpers.newInstance(adaptation.Class_SendMsgParams.Clazz);
/*
        if(!TextUtils.isEmpty(troopuin)) {
            AwLog.Log("QQsend 发送群私聊！");
            ReflectionUtil.setField(sessionInfo, "troopUin", GetTroopcode(adaptation,troopuin), String.class);
            ReflectionUtil.setField(sessionInfo, field_istroop, 1000, int.class);//1000代表群私聊
        }*/
        AwLog.Log("replayTo="+replayTo);
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
        Object sessionInfo =createSessionInfo(adaptation,troopuin,frienduin,istroop);
        ReflectionUtil.invokeStaticMethod(MessageSender, qqAppInterface, adaptation.context, sessionInfo, s, new ArrayList<>(), sendMsgParams);
    }
    private static void QQSendVoiceFile(Adaptation adaptation, String frienduin, int istroopint, String filepath){
        if (new File(filepath).exists()) {
            AwLog.Log("文件存在！准备发送..." + filepath);
            //发送语音
            QQSendVoice(adaptation,
                    frienduin,
                    istroopint,
                    filepath);
            AwLog.Log("发送成功..." + filepath);
        }else{
            AwLog.Log("语音文件不存在=" + filepath);
        }
    }
    private static void QQSendVoice(Adaptation adaptation, String frienduin, int istroop, String amrPath){
        //
        AwLog.Log("------发送语音测试------");
        Method voice_create_method=ReflectionUtil.getStaticMethod(adaptation.Class_ChatActivityFacade,"a",adaptation.Class_MessageRecord,adaptation.Class_QQAppInterface,String.class,adaptation.Class_SessionInfo,int.class,int.class);
        AwLog.Log("voice_create_method="+voice_create_method);
        Object sessionInfo = createSessionInfo(adaptation,null,frienduin,istroop);
        AwLog.Log("aaaa got sessionInfo="+sessionInfo);

        if(Adaptation.QQ.equals(adaptation.QQpackagename)){
            //QQ 8.3.0
            Method voice_send_method=ReflectionUtil.getStaticMethod(adaptation.Class_ChatActivityFacade,"a",long.class,adaptation.Class_QQAppInterface,adaptation.Class_SessionInfo,String.class);
            AwLog.Log("voice_send_method="+voice_send_method);
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
            AwLog.Log("recored="+recored);
            QQMessageDecoder.BaseInfo msginfo= new QQMessageDecoder(recored).GetBaseInfo();
            AwLog.Log("uniseq="+msginfo.uniseq);
            Method voice_send_method=ReflectionUtil.getStaticMethod(adaptation.Class_ChatActivityFacade,"a",void.class,adaptation.Class_QQAppInterface,int.class,String.class,String.class,long.class,boolean.class,int.class,int.class,boolean.class,int.class,int.class,boolean.class);
            AwLog.Log("voice_send_method="+voice_send_method);
            ReflectionUtil.invokeStaticMethod(voice_send_method, adaptation.getAppInterface(), istroop, frienduin, amrPath, msginfo.uniseq, false, 5000, 0, true, 0, 2, true);
        }
        AwLog.Log("------发送语音1测试结束------");
    }
    private static Object GetTroopInfo(Adaptation adaptation, String troopuin) {//troopcode
        Method m=ReflectionUtil.getMethod(adaptation.Method_GetTroopInfo.Clazz,adaptation.Method_GetTroopInfo.MethodName,adaptation.Class_TroopInfo,String.class);
        Object troopManager=adaptation.getTroopManager();
        return ReflectionUtil.invokeMethod(m,troopManager,troopuin);
    }
    private static String GetTroopcode(Adaptation adaptation, String troopuin){//troopcode
        //adaptation.context
        Object TroopInfo=GetTroopInfo(adaptation,troopuin);
        if(TroopInfo==null)
            return null;
        return (String) ReflectionUtil.getObjectField(TroopInfo,"troopcode",String.class);
    }
    private static String[] GetTroopAdmin(Adaptation adaptation, String troopuin){//troopcode
        //adaptation.context
        Object TroopInfo=GetTroopInfo(adaptation,troopuin);
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

    public static void init(final Adaptation adaptation){

        Adaptation.MethodInfo messageReceiverMethod=adaptation.Method_MessageReceiver;
        //AwLog.Log("aaaaaaaaaaaaaaaaaa messageReceiverMethod="+messageReceiverMethod);
        XposedHelpers.findAndHookMethod(messageReceiverMethod.Clazz, messageReceiverMethod.MethodName,
                adaptation.Class_QQAppInterface,
                adaptation.Class_MessageRecord,
                boolean.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                       // AwLog.Log("---------------------------------");
                       // XposedUtil.getObjAttr(param.args[1]);
                        //AwLog.Log("----------------MessageRecord------------");
                        adaptation.setAppInterface(param.args[0]);
                        QQMessageDecoder msgdecoder=new QQMessageDecoder(param.args[1]);
                        int msgtype=msgdecoder.msgtype;
                        //-1000普通文字消息 -5008分享 -2059新人入群 -2017群文件 -1049回复某人 -2011卡片消息 -2007原创表情
                        if(msgtype==QQMessageDefine.MSG_TYPE_STRUCT_LONG_TEXT || msgtype==QQMessageDefine.MSG_TYPE_TEXT || msgtype==QQMessageDefine.MSG_TYPE_REPLY_TEXT){
                            QQMessageDecoder.BaseInfo messageRecordBaseInfo=msgdecoder.GetBaseInfo();
                            if(messageRecordBaseInfo.longMsgInfo!=null) {
                                AwLog.Log("接收长消息");
                                QQMessageDecoder.BaseInfo push_result=longMessageReceiver.push(messageRecordBaseInfo);
                                if(push_result==null) {
                                    //消息只收到了一半，不予操作
                                    return;
                                }else{
                                    AwLog.Log("接收长消息成功，内容为"+push_result.msg);
                                    messageRecordBaseInfo = push_result;
                                }
                            }
                            //AwLog.Log("At信息："+messageRecordBaseInfo.at);
                            //XposedUtil.getObjAttr(param.args[1]);
                            //骰子启动
                            boolean call_me;//是否在叫我
                            if(messageRecordBaseInfo.at.size()>0){//如果有at信息
                                call_me=false;
                                String account=adaptation.getAccount();
                                for(int i=0;i<messageRecordBaseInfo.at.size();i++){
                                    QQMessageDecoder.BaseInfo.AtInfo at=messageRecordBaseInfo.at.get(i);
                                    if(account.equals(at.uin)){//at含有自己
                                        call_me=true;//则是在叫自己
                                        break;
                                    }
                                }
                            }else{
                                call_me=true;//没有at信息，则是在叫自己
                            }
                            AwLog.Log("call_me=" + call_me);
                            if(call_me){//是否在叫机器人，包含at和默认
                                boolean is_dice_open=ConfigReader.readBoolean(adaptation,ConfigReader.CONFIG_KEY_SWITCH_DICE,false);
                                boolean is_admin=false;
                                if(messageRecordBaseInfo.istroopint==1) {//如果是群聊，则获取群管理员信息（包括群主
                                    String[] troop_admins = GetTroopAdmin(adaptation, messageRecordBaseInfo.frienduin);
                                    //AwLog.Log("管理员列表："+troop_admins);
                                    if(troop_admins!=null){
                                        for(String uin : troop_admins){
                                            if(messageRecordBaseInfo.senderuin.equals(uin)){//发送者是管理员
                                                is_admin=true;
                                                break;
                                            }
                                        }
                                    }
                                }


                                AwLog.Log("is_dice_open=" + is_dice_open);
                                if(is_dice_open){//骰子总开关已打开
                                    new Thread(new QQDice(adaptation,messageRecordBaseInfo,is_admin)).start();
                                }
                                boolean is_voice_robot_open=ConfigReader.readBoolean(adaptation,ConfigReader.CONFIG_KEY_SWITCH_VOICE_ROBOT,false);
                                if(is_voice_robot_open && !adaptation.getAccount().equals(messageRecordBaseInfo.senderuin)) {
                                    //#{VOICE-the flower of hope.amr}
                                    //sdcard/cocdata/sound_human
                                    try {
                                        String[] files = voice_cmd_decoder(messageRecordBaseInfo.msg);
                                        if (files.length > 0) {
                                            String filepath = ConfigReader.PATH_SOUND_HUMAN + "/" + files[0];
                                            QQSendVoiceFile(adaptation,messageRecordBaseInfo.frienduin,messageRecordBaseInfo.istroopint,filepath);
                                        } else
                                            AwLog.Log("没有发现语音指令:" + messageRecordBaseInfo.msg);
                                    } catch (Throwable e) {
                                        AwLog.Log("voice_robot ERROR:" + e.getMessage());
                                    }
                                }
                            }

                        }
                        super.beforeHookedMethod(param);
                    }
                });
        MessageSender=XposedHelpers.findMethodBestMatch(adaptation.Method_MessageSender.Clazz, adaptation.Method_MessageSender.MethodName,
                adaptation.Class_QQAppInterface,
                Context.class,
                adaptation.Class_SessionInfo,
                String.class,
                ArrayList.class,
                adaptation.Class_SendMsgParams.Clazz);
        /*
        XposedBridge.hookMethod(MessageSender, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                AwLog.Log("------Class_SessionInfo------");
                XposedUtil.getObjAttr(param.args[2]);
                AwLog.Log("------Class_SendMsgParams------");
                XposedUtil.getObjAttr(param.args[5]);
                super.beforeHookedMethod(param);
            }
        });*/

        //语音发送函数
        //Lcom/tencent/mobileqq/activity/BaseChatPie;->a(Ljava/lang/String;IILcom/tencent/mobileqq/utils/QQRecorder$RecorderParam;IZ)V
        //Lcom/tencent/mobileqq/activity/ChatActivityFacade;->a(Lcom/tencent/mobileqq/app/QQAppInterface;ILjava/lang/String;Ljava/lang/String;JZIIZIIZ
        //AwLog.Log("------VOICE SEND------");

        /*
        findAndHookMethod(findClass("com.tencent.mobileqq.activity.ChatActivityFacade", adaptation.classLoader), "a",
                adaptation.Class_QQAppInterface,int.class,String.class,String.class,long.class,boolean.class,int.class,int.class,boolean.class,int.class,int.class,boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                AwLog.Log("------VOICE SEND------Method");
                //param.args[1]
                param.setResult(null);
                XposedUtil.getObjAttr(param.args[1]);
                for(int i=0;i<param.args.length;i++){
                    try {
                        AwLog.Log("------VOICE SEND------"+i+":"+param.args[i]);
                    }catch (Throwable e){

                    }
                }
                XposedUtil.getObjAttr(param.args[3]);
                super.beforeHookedMethod(param);
            }
        });*/
/*
        findAndHookMethod(findClass("com.tencent.mobileqq.activity.ChatActivityFacade", adaptation.classLoader), "a",
                adaptation.Class_QQAppInterface,String.class,adaptation.Class_SessionInfo,int.class,int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        AwLog.Log("------VOICE SEND------Method");
                        //param.args[1]
                        param.setResult(null);
                        XposedUtil.getObjAttr(param.args[2]);
                        for(int i=0;i<param.args.length;i++){
                            try {
                                AwLog.Log("------VOICE SEND------"+i+":"+param.args[i]);
                            }catch (Throwable e){

                            }
                        }
                        XposedUtil.getObjAttr(param.args[3]);
                        super.beforeHookedMethod(param);
                    }
                });
*/




        /*
        XposedBridge.hookAllMethods(adaptation.Class_ChatActivityFacade, "a", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                AwLog.Log("------AAAAAAAAAAAAAAA------Method");
                for(int i=0;i<param.args.length;i++){
                    try {
                        AwLog.Log("------AAAAAAAAAAAAAAA------"+i+":"+param.args[i]);
                    }catch (Throwable e){

                    }
                }
                AwLog.Log("------AAAAAAAAAAAAAAA------Method-Return:"+param.getResult());
                AwLog.Log("------AAAAAAAAAAAAAAA------Method-Stack:"+ReflectionUtil.getStackTraceString(new Throwable()));

                super.afterHookedMethod(param);
            }
        });*/
        /*
        XposedHelpers.findAndHookMethod(adaptation.Class_ChatActivityFacade,"a",adaptation.Class_QQAppInterface,adaptation.Class_SessionInfo,String.class,new XC_MethodHook(){
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                AwLog.Log("!!!!!!!!!!!!");
                AwLog.Log("------VOICE------1:"+param.args[0]);
                AwLog.Log("------VOICE------2:"+param.args[1]);
                AwLog.Log("------VOICE------2:"+param.args[2]);
                super.afterHookedMethod(param);
            }
        });*/
        //AwLog.Log("------VOICE SEND------END");

            //Lcom/tencent/biz/troophomework/outer/TroopHWRecordArrangeActivity;
        //XposedBridge.hookMethod(MessageSender,MessageSender_callback);
    }
    private static String[] voice_cmd_decoder(String input){// 解析 #{VOICE-the flower of hope.amr}
        String[] s=new String[10];
        int count=0;
        Matcher m=VOICE_CMD_PATTERN.matcher(input);
        while (m.find()) {
            s[count++]=m.group(1);
        }
        String[] t=new String[count];
        System.arraycopy(s,0,t,0,count);
        return t;
    }
}
