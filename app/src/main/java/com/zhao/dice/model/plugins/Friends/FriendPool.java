package com.zhao.dice.model.plugins.Friends;

import android.database.Cursor;

import com.zhao.dice.model.Adaptation;
import com.zhao.dice.model.AwLog;
import com.zhao.dice.model.plugins.ReflectionUtil;

import java.lang.reflect.Method;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FriendPool {
    private static FriendInfo[] getFriendInfo(Adaptation adaptation){
        Object entityManager=adaptation.getEntityManager();
        Method query=ReflectionUtil.getMethod(adaptation.Class_EntityManager,"query", Cursor.class, boolean.class,String.class,String[].class,String.class,String[].class,String.class,String.class,String.class,String.class);
        Cursor cursor= (Cursor) ReflectionUtil.invokeMethod(query,entityManager,false,"Friends",null,"groupid>=?",new String[]{"0"},null,null,null,null);
        if(cursor!=null){
            try {
                if (cursor.moveToFirst()) {
                    int count = cursor.getCount();
                    int index=0;
                    FriendInfo[] friendsInfo=new FriendInfo[count];
                    //AwLog.Log("获取好友总数："+count);
                    do {
                        FriendInfo friendInfo=new FriendInfo();
                        friendInfo.entityByCursor(cursor);
                        friendsInfo[index++]=friendInfo;
                    } while (cursor.moveToNext());
                    //AwLog.Log("读取好友信息成功！");
                    return friendsInfo;
                }
            } catch (Exception e) {
                //AwLog.Log("获取好友信息失败！："+e.getMessage());
            }
        }
        return null;
    }
    public static boolean isFriend(Adaptation adaptation,String uin){
        FriendInfo[] friendsInfo=getFriendInfo(adaptation);
        if(friendsInfo==null)
            return false;
        for(FriendInfo friendInfo : friendsInfo){
            if(friendInfo.uin.equals(uin))
                return true;
        }
        return false;
    }
}
