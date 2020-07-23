package com.zhao.dice.model.plugins.QQMessage;

import android.content.Context;
import android.text.TextUtils;

import com.ZhaoDiceUitl.COCHelper;
import com.ZhaoDiceUitl.LuaPluginManager;
import com.zhao.dice.model.Adaptation;
import com.zhao.dice.model.AwLog;
import com.zhao.dice.model.QQFunction;
import com.zhao.dice.model.XposedUtil;
import com.zhao.dice.model.plugins.Friends.FriendPool;
import com.zhao.dice.model.plugins.ReflectionUtil;
import com.zhao.dice.model.plugins.SettingEntry.ConfigReader;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findMethodBestMatch;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

public class QQMessageXposed {
    private static LuaPluginManager luaPluginManager;
    //private static XC_MethodHook MessageSender_callback;
    //static XC_MethodHook.Unhook MessageSender_Unhook;
    private static Method MessageSender;
    private static LongMessageReceiver longMessageReceiver=new LongMessageReceiver();
    static class MessageHandled{//记录已处理的消息，防止重复处理
        private static ArrayList<Long> MessageHandled=new ArrayList<>();
        static void add(long msgId){
            MessageHandled.add(msgId);//插到最后
            if(MessageHandled.size()>100){//超过100条开始清理
                MessageHandled.remove(0);//删第一条（最老的一条
            }
        }
        static boolean has(long msgId){
            for(long i : MessageHandled){
                if(msgId==i)
                    return true;
            }
            return false;
        }
    }
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
    static class QQDice implements Runnable {
        Adaptation adaptation;
        QQMessageDecoder.BaseInfo messageRecordBaseInfo;
        boolean is_admin;
        QQDice(Adaptation adaptation, QQMessageDecoder.BaseInfo messageRecordBaseInfo, boolean is_admin) {
            this.adaptation = adaptation;
            this.messageRecordBaseInfo = messageRecordBaseInfo;
            this.is_admin = is_admin;
        }

        @Override
        public void run() {
            boolean handle_my_self = ConfigReader.readBoolean(adaptation, ConfigReader.CONFIG_KEY_SWITCH_HANDLE_MYSELF, false);
            if (!handle_my_self && messageRecordBaseInfo.senderuin.equals(adaptation.getAccount()))
                return;
            //是否为公骰模式
            boolean is_publicMode=ConfigReader.readBoolean(adaptation,ConfigReader.CONFIG_KEY_SWITCH_PUBLIC_MODE,false);

            String userID;
            String groupID = null;
            boolean is_dice_open;
            is_dice_open= is_publicMode;//公骰模式下，默认骰开，私骰模式下，默认关。
            if (messageRecordBaseInfo.istroopint == 1) {//群聊
                String white = COCHelper.helper_storage.getGlobalInfo(adaptation.getAccount(), "WHITE_LIST").trim();
                if (!TextUtils.isEmpty(white)) {
                    //设置了白名单
                    String[] list = white.split("\n");
                    //boolean is_in_white = false;
                    for (String s : list) {
                        s = s.trim();
                        if(s.startsWith("#")){
                            s=s.substring(1);
                            if (messageRecordBaseInfo.frienduin.equals(s)) {//骰子被命令关闭
                                is_dice_open=false;
                                break;
                            }
                        }
                        if (messageRecordBaseInfo.frienduin.equals(s)) {//骰子被命令打开
                            is_dice_open = true;
                            break;
                        }
                    }
                    //if (!is_in_white) {//由于设置了白名单，当前群不在白名单，则拒绝
                    //    AwLog.Log("拒绝操作，因为不在白名单内。");
                    //    is_dice_open = false;
                    //}
                }

                userID = messageRecordBaseInfo.senderuin;
                groupID = messageRecordBaseInfo.frienduin;
            } else {
                is_dice_open=true;//私聊直接是开启状态
                userID = messageRecordBaseInfo.frienduin;
            }

            String groupMemberName=null;
            if (messageRecordBaseInfo.istroopint == 1) //消息源是群聊
                groupMemberName = QQFunction.Troop.Get.memberName(adaptation, messageRecordBaseInfo.frienduin, messageRecordBaseInfo.senderuin);
            COCHelper.helper_interface_in in_data = new COCHelper.helper_interface_in(adaptation, messageRecordBaseInfo.msg, groupID, userID, messageRecordBaseInfo.selfuin, groupMemberName, messageRecordBaseInfo.time, is_dice_open, is_admin,is_publicMode);
            //解析骰子命令或关键词
            COCHelper.helper_interface_out out_data = COCHelper.cmd(in_data);
            ArrayList<String> pictures=new ArrayList<>();
            if (out_data == null) {
                //交给插件控制
                if(is_dice_open)
                    luaPluginManager.callToPlugin.event_message_handle(messageRecordBaseInfo.istroopint,messageRecordBaseInfo.senderuin,messageRecordBaseInfo.frienduin,messageRecordBaseInfo.msg);
                AwLog.Log("Cannot handle:" + messageRecordBaseInfo.msg);
            } else {
                if (TextUtils.isEmpty(out_data.msg)) {
                    out_data.msg = "WARNING! 指令已执行，但自定义回复内容为空，请检查配置。";
                } else {
                    //计划移动到SpecialCodeExecutor.ExCode内
                    //解析 #{CMD-xxx}
                    String[] cmds = SpecialMediaDecoder.cmd_decoder(SpecialMediaDecoder.OPS_CMD,out_data.msg);
                    if(cmds.length>0){
                        for(String cmd : cmds) {
                            in_data.setMsg(cmd);
                            COCHelper.helper_interface_out tmp_out_data = COCHelper.cmd(in_data);
                            String result;
                            if(tmp_out_data==null){
                                result="#命令无效#";
                            }else
                                result=tmp_out_data.msg;
                            out_data.msg=SpecialMediaDecoder.cmd_replaceFirst(SpecialMediaDecoder.OPS_CMD,out_data.msg,result);
                            //out_data.msg = out_data.msg.replaceFirst(CMD_CMD_PATTERN_STRING, result);
                        }
                    }
                    //解析特殊操作代码
                    out_data.msg=SpecialCodeExecutor.ExCode(adaptation,out_data.msg,messageRecordBaseInfo.frienduin,messageRecordBaseInfo.istroopint,pictures);
                    //解析{ENTER} 换行
                    out_data.msg = out_data.msg.replaceAll("\\\\n", "\n");
                }
                //机器人消息发送
                if (messageRecordBaseInfo.istroopint == 1) {//消息源是群聊
                    if (out_data.forcePrivateChat) {//是否强制私聊
                        AwLog.Log("强制群私聊！");
                        int istroop;
                        if(FriendPool.isFriend(adaptation,messageRecordBaseInfo.senderuin)){//判断目标是否为好友
                            //是好友
                            istroop=0;//好友私聊模式
                            AwLog.Log("检测到是好友，进入好友私聊");
                        }else{
                            istroop=1000;//群私聊模式
                            AwLog.Log("检测到不是好友，进入群私聊");
                        }
                        String troopcode = QQFunction.Troop.Get.code(adaptation, messageRecordBaseInfo.frienduin);
                        QQFunction.Sender.textAndPic(adaptation, messageRecordBaseInfo.senderuin, messageRecordBaseInfo.selfuin, troopcode, istroop, out_data.msg, pictures,null);
                    } else {
                        AwLog.Log("群聊！");
                        QQFunction.Sender.textAndPic(adaptation, messageRecordBaseInfo.frienduin, messageRecordBaseInfo.selfuin, null, 1, out_data.msg,pictures, out_data.isrelay ? messageRecordBaseInfo : null);
                    }
                } else if (messageRecordBaseInfo.istroopint == 1000 || messageRecordBaseInfo.istroopint == 0) { //消息源是群私聊 或 私聊
                    AwLog.Log("私聊处理！");
                    QQFunction.Sender.textAndPic(adaptation, messageRecordBaseInfo.frienduin, messageRecordBaseInfo.selfuin, messageRecordBaseInfo.senderuin, messageRecordBaseInfo.istroopint, out_data.msg,pictures, null);
                }
                AwLog.Log("sent");
            }
        }
    }
    //istroop=1000 群私聊 istroop=0私聊 istroop=1群聊



    public static void init(final Adaptation adaptation){
        if(luaPluginManager==null) {
            luaPluginManager = LuaPluginManager.getInstance(adaptation);
            luaPluginManager.load(COCHelper.helper_storage.storage_save_path + "/plugin");
        }
        /*
        if(autoAgree==null) {
            AwLog.Log("initing AutoAgree");
            autoAgree=AutoAgree.getInstance(adaptation);
        }*/
        Adaptation.MethodInfo messageReceiverMethod=adaptation.Method_MessageReceiver;
        //AwLog.Log("aaaaaaaaaaaaaaaaaa messageReceiverMethod="+messageReceiverMethod);
        //邀请自动进群等操作
        XposedHelpers.findAndHookMethod("com.tencent.mobileqq.app.message.BaseMessageManager", adaptation.classLoader, "a", adaptation.Class_MessageRecord, boolean.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                boolean is_dice_open=ConfigReader.readBoolean(adaptation,ConfigReader.CONFIG_KEY_SWITCH_DICE,false);
                boolean is_publicMode=ConfigReader.readBoolean(adaptation,ConfigReader.CONFIG_KEY_SWITCH_PUBLIC_MODE,false);
                if(is_dice_open&&is_publicMode) {
                    QQFunction.Troop.Set.handleAll(adaptation);
                }
                AwLog.Log(XposedUtil.getFiledsInfo(param.args[0]).toString());
                super.beforeHookedMethod(param);
            }
        });
        //消息处理
        findAndHookMethod(messageReceiverMethod.Clazz, messageReceiverMethod.MethodName,
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
                            //AwLog.Log("call_me=" + call_me);
                            if(call_me){//是否在叫机器人，包含at和默认
                                boolean is_dice_open=ConfigReader.readBoolean(adaptation,ConfigReader.CONFIG_KEY_SWITCH_DICE,false);
                                boolean is_admin=false;
                                if(messageRecordBaseInfo.istroopint==1) {//如果是群聊，则获取群管理员信息（包括群主
                                    String[] troop_admins = QQFunction.Troop.Get.admin(adaptation, messageRecordBaseInfo.frienduin);
                                    //AwLog.Log("管理员列表："+ Arrays.toString(troop_admins));
                                    if(troop_admins!=null){
                                        for(String uin : troop_admins){
                                            //AwLog.Log("管理员比对 "+messageRecordBaseInfo.senderuin+" vs "+uin);
                                            if(messageRecordBaseInfo.senderuin.equals(uin)){//发送者是管理员
                                                is_admin=true;
                                                //AwLog.Log("比对成功！"+messageRecordBaseInfo.senderuin+"是管理员");
                                                break;
                                            }
                                        }
                                    }
                                }
                                if(!MessageHandled.has(messageRecordBaseInfo.msgUid)){
                                    //这个消息显然还没处理过
                                    //AwLog.Log("is_dice_open=" + is_dice_open);
                                    if(is_dice_open){//骰子总开关已打开
                                        //执行骰子
                                        AwLog.Log(messageRecordBaseInfo.msg+"/"+messageRecordBaseInfo.senderuin);
                                        new Thread(new QQDice(adaptation,messageRecordBaseInfo,is_admin)).start();
                                    }
                                    //现在已经处理过了，加入记录，防重复操作
                                    MessageHandled.add(messageRecordBaseInfo.msgUid);
                                }
                            }
                        }

                        /*else if(msgtype==QQMessageDefine.MSG_TYPE_MEDIA_PIC){
                            //原图 rawMsgUrl 大预览图 bigThumbMsgUrl 小预览图 thumbMsgUrl
                            QQMessageDecoder.QQPicture picture=msgdecoder.GetPictureInfo();
                            AwLog.Log(msgtype+"/"+"图片消息: \nrawMsgUrl="+picture.rawMsgUrl+"\nbigThumbMsgUrl="+picture.bigThumbMsgUrl+"\nthumbMsgUrl="+picture.thumbMsgUrl);
                        }else if(msgtype==QQMessageDefine.MSG_TYPE_TROOP_TIPS_ADD_MEMBER){//新人入群消息
                            QQMessageDecoder.BaseInfo messageRecordBaseInfo=msgdecoder.GetBaseInfo();
                            AwLog.Log(msgtype+"/"+messageRecordBaseInfo.senderuin+"加入了群"+messageRecordBaseInfo.frienduin);
                        }else if(msgtype==QQMessageDefine.MSG_TYPE_MIX) {//图文混合消息
                            List msgElemList= (List) XposedHelpers.getObjectField(param.args[1], "msgElemList");
                            for(Object msgElem : msgElemList ){
                                String TAG=XposedHelpers.getObjectField(msgElem, "TAG").toString();
                                if("MessageForPic".equals(TAG)){
                                    QQMessageDecoder.QQPicture picture=QQMessageDecoder.GetPictureInfo(msgElem,msgdecoder.GetBaseInfo().istroopint);
                                    AwLog.Log(msgtype+"/"+"图片消息: \nrawMsgUrl="+picture.rawMsgUrl+"\nbigThumbMsgUrl="+picture.bigThumbMsgUrl+"\nthumbMsgUrl="+picture.thumbMsgUrl);
                                }else if("MessageForText".equals(TAG)){
                                    String sb=XposedHelpers.getObjectField(msgElem, "sb").toString();
                                    AwLog.Log(msgtype+"/"+"文本消息: \n"+sb);
                                }
                            }
                        }*/
                        //AwLog.Log("ID:"+msgtype+"/"+XposedUtil.getFiledsInfo(param.args[1]));
                        super.beforeHookedMethod(param);
                    }
                });

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



        //doOnActivityResult
/*
        AwLog.Log("------AAAAAAAAAAAAAAA------ST");
        try {
            findAndHookMethod(findClass("com.tencent.mobileqq.activity.TroopMemberCardActivity",adaptation.classLoader), "doOnActivityResult", int.class,int.class,Intent.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    AwLog.Log("------~~~TroopMemberCardActivity doOnActivityResult~~~~~~~~~~~~ request= "+param.args[0]+" result="+param.args[1]+" Intent"+param.args[2]);
                    //"com.tencent.mobileqq.filemanager.activity.BaseFileAssistantActivity"
                    AwLog.Log("------AAAAAAAAAAAAAAA------Method-Stack:" + ReflectionUtil.getStackTraceString(new Throwable()));
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
        }catch (Throwable e){
            AwLog.Log("------AAAAAAAAAAAAAAA------"+e.getMessage());
        }
        AwLog.Log("------AAAAAAAAAAAAAAA------");
*/



/*

        final String baseDir = "/sdcard/dumps";

        findAndHookConstructor("dalvik.system.BaseDexClassLoader", adaptation.classLoader,String.class, File.class, String.class, ClassLoader.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String outDir = baseDir;
                String dexPath = (String) param.args[0];

                //Ignore loading of files from /system, comment this out if you wish
                if (dexPath.startsWith("/system/"))
                    return;
                String packageName=adaptation.QQpackagename;
                AwLog.Log("Hooking dalvik.system.BaseDexClassLoader for "+packageName);
                String uniq = UUID.randomUUID().toString();
                outDir = outDir + "/" + packageName  + dexPath.replace("/", "_") + "-" + uniq;

                AwLog.Log("Capturing " + dexPath);
                AwLog.Log("Writing to " + outDir);

                InputStream in = new FileInputStream(dexPath);
                OutputStream out = new FileOutputStream(outDir);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }

            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {

            }
        });

        //I forgot some silly packers load one class at a time using DexFile
        findAndHookMethod("dalvik.system.DexFile", adaptation.classLoader, "openDexFile", String.class, String.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String outDir = baseDir;
                String dexPath = (String) param.args[0];

                //Ignore loading of files from /system, comment this out if you wish
                if (dexPath.startsWith("/system/"))
                    return;
                String packageName=adaptation.QQpackagename;
                AwLog.Log("Hooking dalvik.system.DexFile for " + packageName);
                String uniq = UUID.randomUUID().toString();
                outDir = outDir + "/" + packageName  + dexPath.replace("/", "_") + "-" + uniq;

                AwLog.Log("Capturing " + dexPath);
                AwLog.Log("Writing to " + outDir);

                InputStream in = new FileInputStream(dexPath);
                OutputStream out = new FileOutputStream(outDir);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }

        });


*/

/*
        XposedHelpers.findAndHookMethod("com.tencent.mobileqq.mixedmsg.MixedMsgManager", adaptation.classLoader, "a", adaptation.Class_QQAppInterface, String.class, String.class, int.class, ArrayList.class, boolean.class, String.class, ArrayList.class, adaptation.Class_MessageForReplyText$SourceMsgInfo, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                AwLog.Log("------图片发送!------Method a");
                for(int i=0;i<param.args.length;i++){
                    try {
                        AwLog.Log("------图片发送!------"+i+":"+param.args[i]);
                    }catch (Throwable e){

                    }
                }
                super.beforeHookedMethod(param);
            }
        });*/
/*
        XposedBridge.hookAllMethods(adaptation.Class_ChatActivityFacade, "a", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                AwLog.Log("------AAAAAAAAAAAAAAA------Method a");
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
        });
*/
            //Lcom/tencent/biz/troophomework/outer/TroopHWRecordArrangeActivity;
        //XposedBridge.hookMethod(MessageSender,MessageSender_callback);
    }

}
