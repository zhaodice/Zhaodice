package com.zhao.dice.model.plugins.QQMessage;

import com.zhao.dice.model.Adaptation;
import com.zhao.dice.model.AwLog;
import com.zhao.dice.model.QQFunction;
import com.zhao.dice.model.plugins.SettingEntry.ConfigReader;

import java.util.ArrayList;

public class SpecialCodeExecutor {
    public static String ExCode(Adaptation adaptation, String input, String frienduin, int istroopint, ArrayList<String> out_pictures){
        //解析 #{CHANGE MEMBER NICK-QQ 新名字}
        String[] cmds = SpecialMediaDecoder.cmd_decoder(SpecialMediaDecoder.OPS_CHANGE_MEMBER_NICK,input);
        AwLog.Log("是否有群名修改指令...");
        if(cmds.length>0){
            AwLog.Log("检测到群名修改指令");
            for(String cmd : cmds) {
                String[] info=cmd.split(" ",2);
                //AwLog.Log("info="+info);
                if(info.length==2){
                    String qq = info[0];
                    String name = info[1];
                    //AwLog.Log("qq=" + qq + "/name=" + name);
                    QQFunction.Troop.Set.Memberinfo(adaptation, frienduin, qq, name);
                }
            }
            input = SpecialMediaDecoder.cmd_clean(SpecialMediaDecoder.OPS_CHANGE_MEMBER_NICK,input);
        }

        //解析 #{VOICE-文件名}
        String[] files = SpecialMediaDecoder.cmd_decoder(SpecialMediaDecoder.OPS_VOICE,input);
        if (files.length > 0) {
            input = SpecialMediaDecoder.cmd_clean(SpecialMediaDecoder.OPS_VOICE,input);
            String filepath = ConfigReader.PATH_SOUND_ROBOT + "/" + files[0];
            QQFunction.Sender.voiceIfExist(adaptation, frienduin, istroopint, filepath);
        }
        //解析 #{FILE-文件名}
        files = SpecialMediaDecoder.cmd_decoder(SpecialMediaDecoder.OPS_FILE,input);
        if (files.length > 0) {
            input = SpecialMediaDecoder.cmd_clean(SpecialMediaDecoder.OPS_FILE,input);
            String filepath = files[0];
            QQFunction.Sender.file(adaptation,frienduin,istroopint,filepath);
        }
        //解析 #{PICTURE-文件名}
        files = SpecialMediaDecoder.cmd_decoder(SpecialMediaDecoder.OPS_PICTURE,input);
        if (files.length > 0) {
            input = SpecialMediaDecoder.cmd_clean(SpecialMediaDecoder.OPS_PICTURE,input);
            //out_pictures = new ArrayList<String>();
            for (String file : files) {
                out_pictures.add(ConfigReader.PATH_PICTURES + "/" + file);
            }
        }
        //解析[CQ:image,file=文件名]
        //Pattern.compile( "\\[CQ:image,file=(.*?)]");
        String CQ_regex_image= "\\[CQ:image,file=(.*?)]";
        files = SpecialMediaDecoder.cmd_decoder(CQ_regex_image,input);
        if (files.length > 0) {
            input = SpecialMediaDecoder.cmd_clean(CQ_regex_image,input);
            //out_pictures = new ArrayList<String>();
            for (String file : files) {
                out_pictures.add(ConfigReader.PATH_PICTURES + "/" + file);
            }
        }
        return input;
    }
}
