package com.zhao.dice.model.plugins.Friends;


import android.database.Cursor;
import android.text.TextUtils;

public class FriendInfo {
    public static final int CLIENT_DEFAULT = 0;
    public static final int CLIENT_IPHONE = 3;
    public static final int CLIENT_MOBILE = 2;
    public static final int CLIENT_PC = 1;
    public static final int GATHER_TYPE_GATHERED = 1;
    public static final int GATHER_TYPE_NORMAL = 0;
    public static final int GATHER_TYPE_RECOMMENDED = 2;
    private static final long LOGIN_TYPE_OFFLINE = 10;
    public static int MULTI_FLAGS_MASK_OLYMPICTORCH = 2;
    public static int MULTI_FLAGS_MASK_SHIELD = 1;
    public static final int NET_2G = 2;
    public static final int NET_3G = 3;
    public static final int NET_4G = 4;
    public static final int NET_UNKNOW = 0;
    public static final int NET_WIFI = 1;
    public static final int TERM_TYPE_ANDROID_PAD = 68104;
    public static final int TERM_TYPE_AOL_CHAOJIHUIYUAN = 73730;
    public static final int TERM_TYPE_AOL_HUIYUAN = 73474;
    public static final int TERM_TYPE_AOL_SQQ = 69378;
    public static final int TERM_TYPE_CAR = 65806;
    public static final int TERM_TYPE_HRTX_IPHONE = 66566;
    public static final int TERM_TYPE_HRTX_PC = 66561;
    public static final int TERM_TYPE_MC_3G = 65795;
    public static final int TERM_TYPE_MISRO_MSG = 69634;
    public static final int TERM_TYPE_MOBILE_ANDROID = 65799;
    public static final int TERM_TYPE_MOBILE_ANDROID_NEW = 72450;
    public static final int TERM_TYPE_MOBILE_HD = 65805;
    public static final int TERM_TYPE_MOBILE_HD_NEW = 71426;
    public static final int TERM_TYPE_MOBILE_IPAD = 68361;
    public static final int TERM_TYPE_MOBILE_IPAD_NEW = 72194;
    public static final int TERM_TYPE_MOBILE_IPHONE = 67586;
    public static final int TERM_TYPE_MOBILE_OTHER = 65794;
    public static final int TERM_TYPE_MOBILE_PC = 65793;
    public static final int TERM_TYPE_MOBILE_WINPHONE_NEW = 72706;
    public static final int TERM_TYPE_QQ_FORELDER = 70922;
    public static final int TERM_TYPE_QQ_SERVICE = 71170;
    public static final int TERM_TYPE_TIM_ANDROID = 77570;
    public static final int TERM_TYPE_TIM_IPHONE = 77826;
    public static final int TERM_TYPE_TIM_PC = 77313;
    public static final int TERM_TYPE_TV_QQ = 69130;
    public static final int TERM_TYPE_WIN8 = 69899;
    public static final int TERM_TYPE_WINPHONE = 65804;
    public long abilityBits = 0;
    public int age;
    public String alias;
    public String bcardId;
    public long bitSet = 0;
    @Deprecated
    public byte cNetwork = 0;
    public byte cSpecialFlag;
    public long datetime;
    public byte detalStatusFlag;
    public int eNetwork = 0;
    public String eimMobile;
    public String eimid;
    public short faceid;
    public byte gathtertype = 0;
    public byte gender;
    public int groupid = -1;
    public int hollywoodVipInfo = 0;
    public int iTermType;
    public byte isIphoneOnline;
    public boolean isMqqOnLine;
    @Deprecated
    public byte isRemark = 1;
    public long lastLoginType;
    public int mComparePartInt;
    public String mCompareSpell;
    public byte memberLevel;
    public int multiFlags = 0;
    public String name;
    public int netTypeIconId;
    public int qqVipInfo = 0;
    public String recommReason;
    public String remark;
    @Deprecated
    public byte[] richBuffer;
    @Deprecated
    public long richTime;
    public long showLoginClient;
    @Deprecated
    public String signature;
    public String smartRemark;
    public byte sqqOnLineState;
    public byte sqqtype;
    public byte status = 10;
    public String strTermDesc;
    public int superQqInfo = 0;
    public int superVipInfo = 0;
    public String uin;

    public static boolean isValidUin(long j) {
        return j > 10000;
    }

    public boolean entityByCursor(Cursor cursor) {
        this.uin = cursor.getString(cursor.getColumnIndex("uin"));
        this.remark = cursor.getString(cursor.getColumnIndex("remark"));
        this.name = cursor.getString(cursor.getColumnIndex("name"));
        this.faceid = cursor.getShort(cursor.getColumnIndex("faceid"));
        this.status = (byte) cursor.getShort(cursor.getColumnIndex("status"));
        this.sqqtype = (byte) cursor.getShort(cursor.getColumnIndex("sqqtype"));
        this.cSpecialFlag = (byte) cursor.getShort(cursor.getColumnIndex("cSpecialFlag"));
        this.groupid = cursor.getInt(cursor.getColumnIndex("groupid"));
        this.memberLevel = (byte) cursor.getShort(cursor.getColumnIndex("memberLevel"));
        this.isMqqOnLine = cursor.getInt(cursor.getColumnIndex("isMqqOnLine")) != 0;
        this.sqqOnLineState = (byte) cursor.getShort(cursor.getColumnIndex("sqqOnLineState"));
        this.detalStatusFlag = (byte) cursor.getShort(cursor.getColumnIndex("detalStatusFlag"));
        this.datetime = cursor.getLong(cursor.getColumnIndex("datetime"));
        this.alias = cursor.getString(cursor.getColumnIndex("alias"));
        this.isIphoneOnline = (byte) cursor.getShort(cursor.getColumnIndex("isIphoneOnline"));
        this.iTermType = cursor.getInt(cursor.getColumnIndex("iTermType"));
        this.qqVipInfo = cursor.getInt(cursor.getColumnIndex("qqVipInfo"));
        this.superQqInfo = cursor.getInt(cursor.getColumnIndex("superQqInfo"));
        this.superVipInfo = cursor.getInt(cursor.getColumnIndex("superVipInfo"));
        this.lastLoginType = cursor.getLong(cursor.getColumnIndex("lastLoginType"));
        this.showLoginClient = cursor.getLong(cursor.getColumnIndex("showLoginClient"));
        this.mComparePartInt = cursor.getInt(cursor.getColumnIndex("mComparePartInt"));
        this.mCompareSpell = cursor.getString(cursor.getColumnIndex("mCompareSpell"));
        this.eNetwork = cursor.getInt(cursor.getColumnIndex("eNetwork"));
        this.multiFlags = cursor.getInt(cursor.getColumnIndex("multiFlags"));
        this.abilityBits = cursor.getLong(cursor.getColumnIndex("abilityBits"));
        this.bcardId = cursor.getString(cursor.getColumnIndex("bcardId"));
        this.bitSet = cursor.getLong(cursor.getColumnIndex("bitSet"));
        this.gathtertype = (byte) cursor.getShort(cursor.getColumnIndex("gathtertype"));
        this.smartRemark = cursor.getString(cursor.getColumnIndex("smartRemark"));
        this.age = cursor.getInt(cursor.getColumnIndex("age"));
        this.gender = (byte) cursor.getShort(cursor.getColumnIndex("gender"));
        this.recommReason = cursor.getString(cursor.getColumnIndex("recommReason"));
        return true;
    }
    public long getLastLoginType() {
        long j = this.lastLoginType;
        return j == 0 ? LOGIN_TYPE_OFFLINE : j;
    }
    public boolean hasLoginedOnTIM() {
        return (this.bitSet & 2) != 0;
    }
    public boolean isFriend() {
        return this.groupid >= 0;
    }
    public boolean isOffline() {
        byte b = this.status;
        if (b == 10) {
            if (!this.isMqqOnLine) {
                byte b2 = this.detalStatusFlag;
            }
        } else
            return !(b == 11 || (b == 20 && this.sqqOnLineState == 1));
        return false;
    }
    public boolean isShield() {
        return (this.multiFlags & MULTI_FLAGS_MASK_SHIELD) > 0;
    }

    public boolean isShowOlympicTorch() {
        return (this.multiFlags & MULTI_FLAGS_MASK_OLYMPICTORCH) > 0;
    }

    public void setShieldFlag(boolean z) {
        if (z) {
            this.multiFlags |= MULTI_FLAGS_MASK_SHIELD;
        } else {
            this.multiFlags &= ~MULTI_FLAGS_MASK_SHIELD;
        }
    }

    public void setOlympicTorchFlag(boolean z) {
        if (z) {
            this.multiFlags |= MULTI_FLAGS_MASK_OLYMPICTORCH;
        } else {
            this.multiFlags &= ~MULTI_FLAGS_MASK_OLYMPICTORCH;
        }
    }

    public String getFriendNick() {
        if (!TextUtils.isEmpty(this.remark)) {
            return this.remark;
        }
        if (!TextUtils.isEmpty(this.name)) {
            return this.name;
        }
        return this.uin;
    }

    public String getFriendNickWithoutUin() {
        if (!TextUtils.isEmpty(this.remark)) {
            return this.remark;
        }
        return !TextUtils.isEmpty(this.name) ? this.name : "";
    }

    public String getFriendNickWithAlias() {
        if (!TextUtils.isEmpty(this.remark)) {
            return this.remark;
        }
        if (!TextUtils.isEmpty(this.name)) {
            return this.name;
        }
        if (!TextUtils.isEmpty(this.alias)) {
            return this.alias;
        }
        return this.uin;
    }

    public String getFriendName() {
        if (!TextUtils.isEmpty(this.name)) {
            return this.name;
        }
        return this.uin;
    }

    public static boolean isValidUin(String str) {
        try {
            return Long.parseLong(str) > 10000;
        } catch (Exception unused) {
            return false;
        }
    }
}