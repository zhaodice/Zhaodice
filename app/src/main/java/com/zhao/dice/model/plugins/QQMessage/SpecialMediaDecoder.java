package com.zhao.dice.model.plugins.QQMessage;

import com.zhao.dice.model.QQFunction;
import com.zhao.dice.model.plugins.SettingEntry.ConfigReader;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecialMediaDecoder {
    final static String OPS_PICTURE=get_op_name("PICTURE");
    final static String OPS_FILE=get_op_name("FILE");
    final static String OPS_VOICE=get_op_name("VOICE");
    final static String OPS_CMD=get_op_name("CMD");
    final static String OPS_CHANGE_MEMBER_NICK=get_op_name("CHANGE MEMBER NICK");

    //获取特殊代码
    private static String get_op_name(String op_name){
        return String.format("#[{]%s-(.*?)[}]",op_name);
    }
    //解析特殊代码的参数,返回数组
    static String[] cmd_decoder(String regex, String input){
        // 解析 #{VOICE-the flower of hope.amr} 等
        //String regex=get_op_name(op_name);
        String[] s=new String[10];
        int count=0;
        Matcher m=Pattern.compile(regex).matcher(input);
        while (m.find()) {
            s[count++]=m.group(1);
        }
        String[] t=new String[count];
        System.arraycopy(s,0,t,0,count);
        return t;
    }
    //将特殊代码解析为指定内容(只替换一次)
    static String cmd_replaceFirst(String regex, String input,String replacement) {
        return input.replaceFirst(regex, replacement);
    }
    static String cmd_clean(String regex, String input) {
        return input.replaceAll(regex,"");
    }
}
