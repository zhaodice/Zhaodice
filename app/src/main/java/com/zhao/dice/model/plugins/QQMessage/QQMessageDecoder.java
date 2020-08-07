package com.zhao.dice.model.plugins.QQMessage;

import android.text.TextUtils;

import com.zhao.dice.model.AwLog;
import com.zhao.dice.model.XposedUtil;
import com.zhao.dice.model.plugins.ReflectionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class QQMessageDecoder {
    public int msgtype=0;
    public boolean has_content=false;
    private Object messageRecord;
    private BaseInfo baseInfo;
    private QQPicture qqPicture;
    public QQMessageDecoder(Object messageRecord){
        if(messageRecord!=null) {
            this.msgtype = (int) XposedHelpers.getObjectField(messageRecord, "msgtype");
            this.messageRecord = messageRecord;
            this.has_content=true;
        }
    }
    public BaseInfo GetBaseInfo(){
        if(baseInfo!=null)
            return baseInfo;
        return baseInfo=new BaseInfo(messageRecord,msgtype);
    }
    public QQPicture GetPictureInfo(){
        if(qqPicture!=null)
            return qqPicture;
        return qqPicture=new QQPicture(messageRecord,GetBaseInfo().istroopint);
    }
    public static QQPicture GetPictureInfo(Object messageRecord,int istroop){
        return new QQPicture(messageRecord,istroop);
    }
    public static class QQPicture{
        final String rawMsgUrl;
        final String bigThumbMsgUrl;
        final String thumbMsgUrl;
        QQPicture(Object MessageRecord,int istroop){
            final String prefix;
            if(istroop==1)
                prefix="https://gchat.qpic.cn";
            else
                prefix="https://c2cpicdw.qpic.cn";
            this.rawMsgUrl = prefix + XposedHelpers.getObjectField(MessageRecord, "rawMsgUrl").toString();
            this.bigThumbMsgUrl = prefix+XposedHelpers.getObjectField(MessageRecord, "bigThumbMsgUrl").toString();
            this.thumbMsgUrl = prefix+XposedHelpers.getObjectField(MessageRecord, "thumbMsgUrl").toString();
        }
    }
    public static class BaseInfo{//基本信息
        public final String frienduin;//来自哪里的消息，群或QQ
        public final String msg;//消息内容
        public final String senderuin;//发送者QQ
        public final String selfuin;//自己的QQ
        public final int istroopint;//是否在群(0 1 1000形式)
        public final long time;//消息时间
        public final long msgseq;//消息识别码(短
        public final long msgUid;//消息识别码
        public final long uniseq;//消息识别码(全球唯一)
        public final ArrayList<AtInfo> at=new ArrayList<>();//at信息

        //-----------------长消息-------------

        BaseReplayInfo baseReplayInfo=null;
        LongMsgInfo longMsgInfo=null;

        static class AtInfo{
            public String name;
            public String uin;
            AtInfo(String name,String uin){
                this.name=name;
                this.uin=uin;
            }
        }
        static class LongMsgInfo{
            public final String msg;//消息内容
            public final int longMsgCount;//长消息数量
            public final int longMsgId;//长消息识别号
            public final int longMsgIndex;//长消息位置
            LongMsgInfo(Object MessageRecord,int longMsgId,String msg){
                this.msg=msg;
                this.longMsgCount=(int)XposedHelpers.getObjectField(MessageRecord,"longMsgCount");
                this.longMsgId=longMsgId;
                //this.longMsgId=(int)XposedHelpers.getObjectField(MessageRecord,"longMsgId");
                this.longMsgIndex=(int)XposedHelpers.getObjectField(MessageRecord,"longMsgIndex");
            }
        }
        static class BaseReplayInfo{
            public final String msg;//消息内容
            public final long senderuin;//发送者QQ
            public final long id;//消息识别码(本地)
            BaseReplayInfo(Object mSourceMsgInfo){
                //XposedUtil.getFiledsInfo(mSourceMsgInfo);
                this.senderuin=(long)XposedHelpers.getObjectField(mSourceMsgInfo,"mSourceMsgSenderUin");
                this.msg=(String) XposedHelpers.getObjectField(mSourceMsgInfo,"mSourceMsgText");
                this.id=(long)XposedHelpers.getObjectField(mSourceMsgInfo,"mSourceMsgSeq");
                /*
                mSourceMsgInfo信息说明表（来自QQ8.3.0
                    {name=mAnonymousNickName, type=class java.lang.String, value=null}
                    name=mAtInfoStr, type=class java.lang.String, value=null}
                    name=mRichMsg, type=class java.lang.String, value=null}
                    {name=mSourceMsgSenderUin, type=long, value=3220172216}
                    {name=mSourceMsgSeq, type=long, value=2671115}
                    {name=mSourceMsgText, type=class java.lang.String, value=我们学校给我们准备了10w只口罩准备开学}
                    {name=mSourceMsgTime, type=int, value=1585360281}
                    {name=mSourceMsgToUin, type=long, value=0}
                    {name=mSourceMsgTroopName, type=class java.lang.String, value=null}
                    {name=mSourceSummaryFlag, type=int, value=1}
                    {name=mType, type=int, value=0}
                    {name=oriMsgType, type=int, value=0}
                    {name=origUid, type=long, value=-2043980475}
                    {name=replyPicHeight, type=int, value=0}
                    {name=replyPicWidth, type=int, value=0}
                    {name=REPLY_TYPE_ABNORMAL, type=int, value=-1}
                    {name=REPLY_TYPE_NORMAL, type=int, value=0}
                    {name=SOURCE_SUMMARY_FLAG_CPMPLETE, type=int, value=1}
                    {name=SOURCE_SUMMARY_FLAG_DIRTY, type=int, value=0}
                * */
            }
        }




        BaseInfo(Object MessageRecord,int msgtype){
            //AwLog.Log("---------MessageRecord---------");
            //XposedUtil.getObjAttr(MessageRecord);
            /*

03-28 16:59:17.979 315-698/? I/chulhu: 变量action = null
03-28 16:59:17.980 315-698/? I/chulhu: 变量latitude = null
03-28 16:59:17.980 315-698/? I/chulhu: 变量location = null
03-28 16:59:17.980 315-698/? I/chulhu: 变量locationUrl = null
03-28 16:59:17.981 315-698/? I/chulhu: 变量longitude = null
03-28 16:59:17.981 315-698/? I/chulhu: 变量mIsMsgSignalOpen = false
03-28 16:59:17.981 315-698/? I/chulhu: 变量mMsgSendTime = 0
03-28 16:59:17.982 315-698/? I/chulhu: 变量mMsgSignalCount = 0
03-28 16:59:17.982 315-698/? I/chulhu: 变量mMsgSignalNetType = 0
03-28 16:59:17.982 315-698/? I/chulhu: 变量mMsgSignalSum = 0
03-28 16:59:17.983 315-698/? I/chulhu: 变量mPasswdRedBagFlag = 0
03-28 16:59:17.983 315-698/? I/chulhu: 变量mPasswdRedBagSender = 0
03-28 16:59:17.983 315-698/? I/chulhu: 变量mWantGiftSenderUin = 0
03-28 16:59:17.984 315-698/? I/chulhu: 变量msgVia = 0
03-28 16:59:17.984 315-698/? I/chulhu: 变量sb = null
03-28 16:59:17.984 315-698/? I/chulhu: 变量sb2 = null
03-28 16:59:17.984 315-698/? I/chulhu: 变量url = null
03-28 16:59:17.985 315-698/? I/chulhu: 变量SPAN_TYPE_EMOJI = 1
03-28 16:59:17.985 315-698/? I/chulhu: 变量SPAN_TYPE_LINK = 0
03-28 16:59:17.985 315-698/? I/chulhu: 变量SPAN_TYPE_SYS_EMOTCATION = 2
03-28 16:59:17.986 315-698/? I/chulhu: 变量arkServerExtraInfo = null
03-28 16:59:17.986 315-698/? I/chulhu: 变量arkServerMsgId = null
03-28 16:59:17.986 315-698/? I/chulhu: 变量atInfoParsed = false
03-28 16:59:17.987 315-698/? I/chulhu: 变量isFromArkServer = false
03-28 16:59:17.987 315-698/? I/chulhu: 变量mContextList = []
03-28 16:59:17.987 315-698/? I/chulhu: 变量mContextMatchType = 0
03-28 16:59:17.987 315-698/? I/chulhu: 变量mEchoType = 0
03-28 16:59:17.988 315-698/? I/chulhu: 变量mHasReportShowIcon = false
03-28 16:59:17.988 315-698/? I/chulhu: 变量mHasReportShowIconEach = false
03-28 16:59:17.988 315-698/? I/chulhu: 变量mHasReportShowUnderline = false
03-28 16:59:17.989 315-698/? I/chulhu: 变量mHasReportShowUnderlineEach = false
03-28 16:59:17.989 315-698/? I/chulhu: 变量mIconAppPath = null
03-28 16:59:17.989 315-698/? I/chulhu: 变量mIsMsgParsedByAi = false
03-28 16:59:17.990 315-698/? I/chulhu: 变量mIsShow = false
03-28 16:59:17.990 315-698/? I/chulhu: 变量mOldAppInfo = com.tencent.mobileqq.data.RecommendCommonMessage$ArkMsgAppInfo@3d931200
03-28 16:59:17.990 315-698/? I/chulhu: 变量fakeSenderType = 0
03-28 16:59:17.991 315-698/? I/chulhu: 变量hasPlayedDui = false
03-28 16:59:17.991 315-698/? I/chulhu: 变量isAioAnimChecked = false
03-28 16:59:17.991 315-698/? I/chulhu: 变量isDui = false
03-28 16:59:17.991 315-698/? I/chulhu: 变量isFirstMsg = false
03-28 16:59:17.992 315-698/? I/chulhu: 变量isFlowMessage = false
03-28 16:59:17.992 315-698/? I/chulhu: 变量isShowQIMStyleGroup = false
03-28 16:59:17.992 315-698/? I/chulhu: 变量isShowQimStyleAvater = false
03-28 16:59:17.992 315-698/? I/chulhu: 变量isShowTIMStyleGroup = false
03-28 16:59:17.992 315-698/? I/chulhu: 变量isShowTimStyleAvater = false
03-28 16:59:17.993 315-698/? I/chulhu: 变量mAnimFlag = true
03-28 16:59:17.993 315-698/? I/chulhu: 变量mIsParsed = false
03-28 16:59:17.993 315-698/? I/chulhu: 变量mMessageSource = null
03-28 16:59:17.994 315-698/? I/chulhu: 变量mMsgAnimFlag = false
03-28 16:59:17.994 315-698/? I/chulhu: 变量mMsgAnimTime = 0
03-28 16:59:17.994 315-698/? I/chulhu: 变量mNeedGrayTips = false
03-28 16:59:17.994 315-698/? I/chulhu: 变量mNeedTimeStamp = false
03-28 16:59:17.995 315-698/? I/chulhu: 变量mPendantAnimatable = false
03-28 16:59:17.995 315-698/? I/chulhu: 变量AIO_MARGIN_MSG_TYPE_DIFF = 1
03-28 16:59:17.995 315-698/? I/chulhu: 变量AIO_MARGIN_MSG_TYPE_SAME = 0
03-28 16:59:17.996 315-698/? I/chulhu: 变量advertisementItem = null
03-28 16:59:17.996 315-698/? I/chulhu: 变量atInfoList = null
03-28 16:59:17.996 315-698/? I/chulhu: 变量atInfoTempList = null
03-28 16:59:17.997 315-698/? I/chulhu: 变量extInt = 65536
03-28 16:59:17.997 315-698/? I/chulhu: 变量extLong = 1
03-28 16:59:17.997 315-698/? I/chulhu: 变量extStr = {"ark_text_analysis_flag":"0","vip_font_id":"147751","diy_timestamp":"0","vip_face_id":"0","vip_font_effect_id":"0","vip_type":"81","vip_level":"6"}
03-28 16:59:17.997 315-698/? I/chulhu: 变量extraflag = 0
03-28 16:59:17.998 315-698/? I/chulhu: 变量frienduin = 912151623
03-28 16:59:17.998 315-698/? I/chulhu: 变量isBlessMsg = false
03-28 16:59:17.998 315-698/? I/chulhu: 变量isCheckNeedShowInListTypeMsg = false
03-28 16:59:17.999 315-698/? I/chulhu: 变量isMultiMsg = false
03-28 16:59:17.999 315-698/? I/chulhu: 变量isOpenTroopMessage = false
03-28 16:59:17.999 315-698/? I/chulhu: 变量isReMultiMsg = false
03-28 16:59:17.999 315-698/? I/chulhu: 变量isReplySource = false
03-28 16:59:18.000 315-698/? I/chulhu: 变量isValid = true
03-28 16:59:18.000 315-698/? I/chulhu: 变量isread = false
03-28 16:59:18.000 315-698/? I/chulhu: 变量issend = 0
03-28 16:59:18.001 315-698/? I/chulhu: 变量istroop = 1
03-28 16:59:18.001 315-698/? I/chulhu: 变量longMsgCount = 1
03-28 16:59:18.001 315-698/? I/chulhu: 变量longMsgId = 0
03-28 16:59:18.002 315-698/? I/chulhu: 变量longMsgIndex = 0
03-28 16:59:18.002 315-698/? I/chulhu: 变量mExJsonObject = {"ark_text_analysis_flag":"0","vip_font_id":"147751","diy_timestamp":"0","vip_face_id":"0","vip_font_effect_id":"0","vip_type":"81","vip_level":"6"}
03-28 16:59:18.002 315-698/? I/chulhu: 变量mIsShowQidianTips = 0
03-28 16:59:18.003 315-698/? I/chulhu: 变量mMessageInfo = null
03-28 16:59:18.003 315-698/? I/chulhu: 变量mQidianMasterUin = 0
03-28 16:59:18.003 315-698/? I/chulhu: 变量mQidianTaskId = 0
03-28 16:59:18.003 315-698/? I/chulhu: 变量mQidianTipText = null
03-28 16:59:18.004 315-698/? I/chulhu: 变量mRobotFlag = 0
03-28 16:59:18.004 315-698/? I/chulhu: 变量msg = 跑139W
03-28 16:59:18.004 315-698/? I/chulhu: 变量msg2 = null
03-28 16:59:18.004 315-698/? I/chulhu: 变量msgBackupMsgRandom = 0
03-28 16:59:18.005 315-698/? I/chulhu: 变量msgBackupMsgSeq = 0
03-28 16:59:18.005 315-698/? I/chulhu: 变量msgData = null
03-28 16:59:18.005 315-698/? I/chulhu: 变量msgId = 0
03-28 16:59:18.006 315-698/? I/chulhu: 变量msgUid = 72057595135850223
03-28 16:59:18.006 315-698/? I/chulhu: 变量msgseq = 1585385957
03-28 16:59:18.006 315-698/? I/chulhu: 变量msgtype = -1000
03-28 16:59:18.007 315-698/? I/chulhu: 变量needNeedShowInList = false
03-28 16:59:18.007 315-698/? I/chulhu: 变量needUpdateMsgTag = true
03-28 16:59:18.007 315-698/? I/chulhu: 变量selfuin = 2135983891
03-28 16:59:18.007 315-698/? I/chulhu: 变量sendFailCode = 0
03-28 16:59:18.008 315-698/? I/chulhu: 变量senderuin = 736076532
03-28 16:59:18.008 315-698/? I/chulhu: 变量shmsgseq = 1184716
03-28 16:59:18.008 315-698/? I/chulhu: 变量stickerHidden = false
03-28 16:59:18.008 315-698/? I/chulhu: 变量stickerInfo = null
03-28 16:59:18.009 315-698/? I/chulhu: 变量time = 1585385957
03-28 16:59:18.009 315-698/? I/chulhu: 变量uniseq = 6809180837393242625
03-28 16:59:18.009 315-698/? I/chulhu: 变量versionCode = 3
03-28 16:59:18.010 315-698/? I/chulhu: 变量vipBubbleDiyTextId = 0
03-28 16:59:18.010 315-698/? I/chulhu: 变量vipBubbleID = 61
03-28 16:59:18.010 315-698/? I/chulhu: 变量vipSubBubbleId = 0
            * */
            this.frienduin = (String) XposedHelpers.getObjectField(MessageRecord, "frienduin");
            this.senderuin = (String) XposedHelpers.getObjectField(MessageRecord, "senderuin");
            this.istroopint=(int) XposedHelpers.getObjectField(MessageRecord, "istroop");
            this.selfuin = (String) XposedHelpers.getObjectField(MessageRecord, "selfuin");
            this.msgseq = (long)XposedHelpers.getObjectField(MessageRecord, "shmsgseq");
            this.time = (long)XposedHelpers.getObjectField(MessageRecord, "time");
            this.uniseq= (long)XposedHelpers.getObjectField(MessageRecord, "uniseq");
            this.msgUid= (long)XposedHelpers.getObjectField(MessageRecord, "msgUid");
            String msg= (String) XposedHelpers.getObjectField(MessageRecord, "msg");
            String extStr=(String) XposedHelpers.getObjectField(MessageRecord, "extStr");
            if(!TextUtils.isEmpty(extStr)) {//exStr扩展消息
                try {
                    JSONObject extJSON=new JSONObject(extStr);
                    String troop_at_info_list_String=extJSON.optString("troop_at_info_list");//获取troop_at_info_list的json字符串
                    if(!TextUtils.isEmpty(troop_at_info_list_String)) {
                        JSONArray troop_at_info_list = new JSONArray(troop_at_info_list_String);//将字符串转换为JSONObject对象
                        if (troop_at_info_list != null) {
                            ArrayList<int[]> alist=new ArrayList<>();
                            for (int i = 0; i < troop_at_info_list.length(); i++) {//遍历@信息
                                //发现At信息
                                JSONObject atInfo = troop_at_info_list.getJSONObject(i);
                                String uin = atInfo.getString("uin");//解析@的qq号
                                int startPos = atInfo.getInt("startPos");
                                int textLen = atInfo.getInt("textLen");
                                AwLog.Log("msg="+msg+",atInfo="+atInfo.toString());
                                int[] ints=new int[2];
                                ints[0]=startPos;
                                ints[1]=startPos+textLen;
                                alist.add(ints);
                                String atname = msg.substring(ints[0], ints[1]);//解析@的名字
                                at.add(new AtInfo(atname, uin));//加入解析结果
                            }
                            //去掉所有@的提示
                            StringBuilder msg_every_char=new StringBuilder();
                            loop_in_StringBuilder:
                            for(int i=0;i<msg.length();i++){
                                for(int[] ints : alist){
                                    if(ints[0]<=i&&i<ints[1]){
                                        continue loop_in_StringBuilder;
                                    }
                                }
                                msg_every_char.append(msg.substring(i,i+1));
                            }

                            AwLog.Log("msg_every_char="+msg_every_char.toString());
                            msg=msg_every_char.toString();
                            msg=msg.trim();
                        }
                    }
                } catch (Throwable e) {
                    AwLog.Log("解析extStr错误！ "+e.getMessage());
                }
            }
            this.msg=msg;
            //AwLog.Log("msgtype:"+msgtype);
            if(msgtype==QQMessageDefine.MSG_TYPE_REPLY_TEXT){
                Object mSourceMsgInfo=XposedHelpers.getObjectField(MessageRecord,"mSourceMsgInfo");
                baseReplayInfo= new BaseReplayInfo(mSourceMsgInfo);
            }
            int longMsgId=(int)XposedHelpers.getObjectField(MessageRecord,"longMsgId");
            if(longMsgId>0){
                AwLog.Log("处理长消息！");
                longMsgInfo= new LongMsgInfo(MessageRecord, longMsgId, msg);
            }
        }
        public BaseInfo(String frienduin, String msg, String senderuin, String selfuin, int istroopint, long time, long msgseq, long msgUid, long uniseq, BaseReplayInfo baseReplayInfo){
            this.frienduin=frienduin;
            this.msg=msg;
            this.senderuin=senderuin;
            this.selfuin=selfuin;
            this.istroopint=istroopint;
            this.time=time;
            this.msgseq=msgseq;
            this.msgUid=msgUid;
            this.uniseq=uniseq;
            this.baseReplayInfo=baseReplayInfo;
        }
    }
}
