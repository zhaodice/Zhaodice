package com.ZhaoDiceUitl;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.zhao.dice.model.Adaptation;
import com.zhao.dice.model.AwLog;
import com.zhao.dice.model.BuildConfig;
import com.zhao.dice.model.GlobalApplication;
import com.zhao.dice.model.QQFunction;
import com.zhao.dice.model.plugins.ReflectionUtil;
import com.zhao.dice.model.plugins.SettingEntry.ConfigReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class COCHelper {
    private static final String ENV_DEFAULT_PLAYER_NAME="调查员";//默认档位名
    private static final MersenneTwister mersenneTwister=new MersenneTwister();//梅森旋转随机数算法库
    private static final Pattern expression_pattern_complex=Pattern.compile("([0-9()*/dkq+\\-]+)");//匹配所有骰点公式
    private static final Pattern expression_pattern_NdNkqN = Pattern.compile("(\\d+)?d(\\d+)?([kq]\\d+)?");//匹配 1d4 d4 d 1d4k2 1d4q2;
    private static final Pattern expression_pattern_SN = Pattern.compile("([^0-9]+)(\\d+)?");//匹配 斗殴50 斗殴
    private static final Pattern expression_pattern_Roll2 = Pattern.compile("(\\d+#)?([^0-9d+\\-]+)?([0-9()*/dkq+\\-]+)?");//匹配 斗殴50 斗殴1d4 斗殴d4 斗殴d 斗殴
    private static final Pattern expression_pattern_PAYERNAME=Pattern.compile("(.*) hp([0-9]+)/([0-9]+) san([0-9]+)/([0-9]+)");//解析玩家名字
    private static final Pattern expression_pattern_Roll=Pattern.compile("([ca])?([bp])?(\\d+)?(#\\d+)?([^0-9+\\-]+)?([+\\-])?(\\d+)?");//Roll点通用表达式

    private static final Pattern expression_pattern_SpecialChars=Pattern.compile("[`~!@#$%^&*()+=|{}':;,/.<>?！￥…（）—【】‘；：”“’。，、？]");
    public static class helper_draw{
        //牌堆引用码
        private static Pattern expression_pattern_draw_shiki =Pattern.compile("[{][%]?(.*?)[}]");//溯洄
        private static Pattern expression_pattern_draw_sitanya =Pattern.compile("[{][$%](.*?)[}]");//斯塔尼亚
        //骰码
        private static Pattern expression_pattern_draw_XdX =Pattern.compile("\\[(.*?)[]]");
        //牌堆数据集
        private static JSONObject DATA_DRAW_shiki=new JSONObject();//溯洄
        private static ArrayList<String> DATA_DRAW_shiki_index = new ArrayList<>();
        private static JSONObject DATA_DRAW_sitanya=new JSONObject();//斯塔尼亚
        private static ArrayList<String> DATA_DRAW_sitanya_index= new ArrayList<>();
        //已载牌堆列表
        private static ArrayList<String> DATA_DRAW_loaded= new ArrayList<>();

        static class DrawResult{
            int index;
            String content;
            DrawResult(int index,String content){
                this.index=index;
                this.content=content;
            }
        }
        static void drawLoader(){
            AwLog.Log("正在加载牌堆...");
            //初始化牌堆数据集
            File[] files =ConfigReader.PATH_DRAW.listFiles();
            if(files!=null) {
                file_continue: for (File file : files) {
                    //搜索已载牌堆，如果载了就不载了。
                    String fpath=file.getPath();
                    for(String path : DATA_DRAW_loaded){
                        if(path.equals(fpath)){
                            continue file_continue;//该文件已加载过，不再重复加载。
                        }
                    }
                    //读入序列化文件
                    StringBuilder sb = new StringBuilder();
                    try {
                        FileReader fileReader = new FileReader(file);
                        int ch;
                        while ((ch = fileReader.read()) != -1) {
                            sb.append((char) ch);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(sb.length()==0)
                        continue;

                    //JSON格式牌堆解析
                    try {
                        JSONObject json;
                        json = new JSONObject(sb.toString());
                        Iterator<String> headerkeys = json.keys();
                        while (headerkeys.hasNext()) {
                            String headerkey = headerkeys.next();
                            JSONArray headerValue = json.getJSONArray(headerkey);
                            headerkey=headerkey.toLowerCase();
                            DATA_DRAW_shiki.put(headerkey, headerValue);
                            if(!headerkey.startsWith("_"))
                                DATA_DRAW_shiki_index.add(headerkey);
                        }
                        AwLog.Log(String.format("JSON解析成功! %s",fpath));
                        DATA_DRAW_loaded.add(fpath);//牌堆加载成功，记录，防止重复加载
                        continue;//解析成功，continue
                    } catch (Throwable e) {
                        //AwLog.Log(String.format("%s JSON解析失败!错误信息：%s",file.getPath(),e.getMessage()));
                    }

                    //YAML格式牌堆解析
                    try {
                        Yaml yaml = new Yaml();
                        Map obj = (Map) yaml.load(sb.toString());
                        String command=(String) obj.get("command");
                        if(command==null)
                            throw new NullPointerException("command is null");//牌堆数据不完整 - command为NULL
                        {
                            ArrayList includes = (ArrayList) obj.get("includes");
                            if (includes == null) {//牌堆数据不完整 - 索引为空
                                DATA_DRAW_sitanya_index.add(command);
                            } else {
                                for (Object o : includes) {
                                    if ("default".equals(o)) {
                                        DATA_DRAW_sitanya_index.add(command);
                                    } else {
                                        DATA_DRAW_sitanya_index.add(command + " " + o);
                                    }
                                }
                            }
                        }
                        for (Object o : obj.entrySet()) {
                            Map.Entry entry = (Map.Entry) o;
                            String key= (String) entry.getKey();
                            Object value= entry.getValue();
                            if(value.getClass()==ArrayList.class){
                                if("includes".equals(key))
                                    continue;
                                //牌堆部分
                                String global_key;
                                if("default".equals(key)){
                                    global_key=command;
                                }else{
                                    global_key=command+" "+key;
                                }
                                global_key=global_key.toLowerCase();
                                //global_key的取值：
                                //黑暗之魂
                                //黑暗之魂 黑魂
                                //黑暗之魂 黑魂一
                                //..
                                JSONArray contents_jry=new JSONArray();
                                ArrayList contents_arrayList= (ArrayList) value;
                                for(Object content : contents_arrayList.toArray()){
                                    contents_jry.put(content);
                                }
                                DATA_DRAW_sitanya.put(global_key,contents_jry);
                            }
                        }
                        //continue;//解析成功，continue
                        //AwLog.writeFile(Environment.getExternalStorageDirectory()+"/DATA_DRAW_sitanya.log",DATA_DRAW_sitanya.toString());
                        //AwLog.Log("DECK! ---------------YAML!!!-------------");

                        //AwLog.Log(obj.getClass().getName());
                        //AwLog.Log(obj.toString());
                        AwLog.Log(String.format("YAML解析成功! %s",fpath));
                        DATA_DRAW_loaded.add(fpath);//牌堆加载成功，记录，防止重复加载
                        continue;
                    } catch (Throwable e) {
                        //AwLog.Log("---------------YAML ERROR!!!-------------"+e.getMessage());
                    }
                    AwLog.Log("无法识别的文件 "+fpath);
                }
            }
            AwLog.Log("牌堆加载成功...");
        }
        static String draw(String drawname){
            drawLoader();
            StringBuilder sb=new StringBuilder();
            if("help".equals(drawname)){
                sb.append("塔骰牌堆:\n");
                for(String index : DATA_DRAW_sitanya_index){
                    sb.append(String.format(".deck %s\n",index));
                }
                sb.append("溯洄骰牌堆:\n");
                for(String index : DATA_DRAW_shiki_index){
                    sb.append(String.format(".deck %s\n",index));
                }
                return sb.toString();
            }else if("list".equals(drawname)){
                sb.append("-已载牌堆文件列表-\n");
                for(String path : DATA_DRAW_loaded){
                    sb.append(String.format("/%s\n",new File(path).getName()));
                }
                return sb.toString();
            }
            JSONObject drawRecord=new JSONObject();//抽牌记录
            String draw_result=draw(drawRecord,drawname,1,0);//先抽溯洄
            if(draw_result==null)
                draw_result=draw(drawRecord,drawname,2,0);//然后抽斯塔尼亚
            if(draw_result!=null){
                //解析类似[7+1d10]这种表达式
                Matcher m = expression_pattern_draw_XdX.matcher(draw_result);
                while (m.find()) {
                    try {
                        String replacement = m.group(0);
                        String expression_XdX = m.group(1);
                        draw_result = helper_calculation.replaceOnce(draw_result, replacement, String.valueOf(helper_calculation.XdXCalculation(expression_XdX).number));
                    }catch (Throwable ignored){

                    }
                }
            }
            return draw_result;
        }
        static JSONArray jsonArrayClone(JSONArray jry){
            JSONArray new_jry=new JSONArray();
            for(int i=0;i<jry.length();i++) {
                try {
                    new_jry.put(jry.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return new_jry;
        }
        static String draw(JSONObject drawRecord,String drawname,int mode,int deep){
            DrawResult drawResult=null;
            try {
                switch (mode) {
                    case 1: {//溯洄
                        drawResult=draw(jsonArrayClone(DATA_DRAW_shiki.getJSONArray(drawname)),drawRecord,drawname,mode,deep);
                    }
                    case 2: {//斯塔尼亚
                        drawResult=draw(jsonArrayClone(DATA_DRAW_sitanya.getJSONArray(drawname)),drawRecord,drawname,mode,deep);
                    }
                }
            }catch (Throwable e){
                //AwLog.Log("抽牌错误！"+ ReflectionUtil.getStackTraceString(e));
            }
            AwLog.Log("抽牌结果 drawResult="+ drawResult);
            if(drawResult!=null)
                return drawResult.content;
            return null;
        }
        static DrawResult draw(JSONArray draws,JSONObject drawRecord,String drawname,int mode,int deep){
            //抽取一个牌堆,找不到牌堆或存在错误则返回null mode=1 溯洄 mode=2 斯塔尼亚
            //从数组draws里抽取
            //drawRecord为抽牌记录（不放回时有用）
            //drawname =
            // 超能力
            // 黑暗之魂
            // 黑暗之魂 黑魂地点
            // 黑暗之魂 黑魂生物
            //...

            if(deep>30) {
                AwLog.Log("嵌套深度太长! drawname="+drawname);
                return null;//嵌套深度太长
            }try{
                switch (mode){
                    case 1:{//溯洄
                        //JSONArray draws = DATA_DRAW_shiki.getJSONArray(drawname);
                        int draw_index=helper_calculation.getRandomInt(0, draws.length() - 1);
                        String draw_content = draws.getString(draw_index);
                        Matcher m = expression_pattern_draw_shiki.matcher(draw_content);
                        //JSONObject drawRecord=new JSONObject();//抽牌记录
                        while (m.find()) {
                            String inner_draw_name = m.group(1);
                            AwLog.Log("inner_draw_name="+inner_draw_name);
                            String replacement=m.group(0);
                            AwLog.Log("replacement="+replacement);
                            if(inner_draw_name==null || replacement==null)
                                continue;
                            inner_draw_name=inner_draw_name.toLowerCase();

                            //如果带%是放回抽取，不带%是不放回抽取
                            boolean put_back="%".equals(replacement.substring(1,2));//如果带% 则put_back=true
                            String inner_draw_content=null;
                            if(put_back){
                                inner_draw_content=draw(drawRecord,inner_draw_name, mode, ++deep);
                            }else{
                                AwLog.Log("不放回抽取！"+drawname);
                                JSONArray inner_draws;
                                //AwLog.Log("drawRecord="+drawRecord.toString());
                                if(drawRecord.has(inner_draw_name)) {//有本次抽牌记录，加入记录
                                    inner_draws = drawRecord.getJSONArray(inner_draw_name);
                                    if(inner_draws.length()==0) {//牌被抽干了
                                        inner_draws = jsonArrayClone(DATA_DRAW_shiki.getJSONArray(inner_draw_name));//重新导入牌
                                        drawRecord.put(inner_draw_name,inner_draws);
                                    }
                                    AwLog.Log("不放回抽取！再次抽相同的牌！使用阉割数据："+inner_draws.toString());
                                }else {//无本次抽牌记录，全新记录
                                    if(!DATA_DRAW_shiki.has(inner_draw_name)) //引用的内容缺失
                                        continue;
                                    inner_draws = jsonArrayClone(DATA_DRAW_shiki.getJSONArray(inner_draw_name));
                                    AwLog.Log("不放回抽取！第一次抽牌！"+inner_draw_name+"使用原始数据："+inner_draws.toString());
                                    drawRecord.put(inner_draw_name,inner_draws);
                                }
                                //AwLog.Log("drawRecord="+drawRecord.toString());
                                AwLog.Log("试图抽取："+inner_draws);
                                if(inner_draws.length()>0) {
                                    DrawResult inner_draw_result = draw(inner_draws,drawRecord, drawname, mode, deep);
                                    if (inner_draw_result != null) {
                                        inner_draws.remove(inner_draw_result.index);//抽完牌，则从牌堆里临时移除掉它，避免再次抽到
                                        inner_draw_content = inner_draw_result.content;
                                    } else {
                                        AwLog.Log("试图抽取失败，返回为空");
                                    }
                                }
                            }
                            if (inner_draw_content != null)
                                draw_content = helper_calculation.replaceOnce(draw_content, replacement, inner_draw_content);
                        }
                        return new DrawResult(draw_index,draw_content);
                    }
                    case 2: {//斯塔尼亚 {$xxx}放回 {%xxxx}不放回
                        AwLog.Log("sitanya: getting draws,name="+drawname);
                        //JSONArray draws = DATA_DRAW_sitanya.getJSONArray(drawname);
                        String deckname;
                        {
                            String[] drawname_split=drawname.split(" ",2);
                            deckname=drawname_split[0];
                        }

                        //AwLog.Log("sitanya: got draws="+draws);
                        int draw_index=helper_calculation.getRandomInt(0, draws.length() - 1);
                        String draw_content = draws.getString(draw_index);
                        Matcher m = expression_pattern_draw_sitanya.matcher(draw_content);
                        //AwLog.Log("sitanya: finding inner_draw");
                        while (m.find()) {
                            String inner_draw_name = m.group(1);
                            String replacement=m.group(0);
                            if(inner_draw_name==null || replacement==null)
                                continue;
                            //如果带$是放回抽取，带%是不放回抽取
                            inner_draw_name=deckname+" "+inner_draw_name;//组合出具体的牌堆，因为DATA_DRAW_sitanya是{具体牌名}{空格}{牌堆名}
                            boolean put_back="$".equals(replacement.substring(1,2));//如果带$ 则put_back=true

                            AwLog.Log("sitanya: inner_draw_name="+inner_draw_name);
                            //AwLog.Log("sitanya: inner_drawing="+deckname+" "+inner_draw_name);

                            String inner_draw_content=null;
                            if(put_back){
                                inner_draw_content=draw(drawRecord,inner_draw_name,mode, ++deep);
                            }else{
                                JSONArray inner_draws;
                                AwLog.Log("不放回抽取！"+replacement);
                                if(drawRecord.has(inner_draw_name)) {//有本次抽牌记录，加入记录
                                    inner_draws = drawRecord.getJSONArray(inner_draw_name);
                                    if(inner_draws.length()==0) {//牌被抽干了
                                        inner_draws = jsonArrayClone(DATA_DRAW_sitanya.getJSONArray(inner_draw_name));//重新导入牌
                                        drawRecord.put(inner_draw_name,inner_draws);
                                    }
                                    AwLog.Log("inner_draws="+inner_draws);
                                }else {//无本次抽牌记录，全新记录
                                    if(!DATA_DRAW_sitanya.has(inner_draw_name)) //引用的内容缺失
                                        continue;
                                    inner_draws = jsonArrayClone(DATA_DRAW_sitanya.getJSONArray(inner_draw_name));
                                    drawRecord.put(inner_draw_name,inner_draws);
                                }
                                if(inner_draws.length()>0) {
                                    DrawResult inner_draw_result = draw(inner_draws,drawRecord, inner_draw_name, mode, deep);
                                    if (inner_draw_result != null) {
                                        inner_draws.remove(inner_draw_result.index);//抽完牌，则从牌堆里临时移除掉它，避免再次抽到
                                        inner_draw_content = inner_draw_result.content;
                                    }
                                }
                            }
                            if (inner_draw_content != null)
                                draw_content = helper_calculation.replaceOnce(draw_content, replacement, inner_draw_content);
                        }
                        AwLog.Log("sitanya: find inner_draw_name over");
                        return new DrawResult(draw_index,draw_content);
                    }
                }
            }catch (Throwable e){
                AwLog.Log("抽牌错误！"+ ReflectionUtil.getStackTraceString(e));
            }
            return null;
        }
    }
    public static class helper_log{
        final static String log_save_path= ConfigReader.PATH_TMP+"/log";
        static void writeFile(String path, String content) {
            File writefile;
            try {
                writefile = new File(path);
                if (!writefile.exists()) {
                    if(!writefile.createNewFile())
                        return;
                    writefile = new File(path);
                    FileOutputStream fos = new FileOutputStream(writefile);
                    //写BOM
                    fos.write(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF});
                }
                FileOutputStream fw = new FileOutputStream(writefile,true);
                Writer out = new OutputStreamWriter(fw, StandardCharsets.UTF_8);
                out.write(content);
                //写入换行
                out.write("\r\n");
                out.close();
                fw.flush();
                fw.close();
            } catch (Exception ignored) {

            }
        }
        static void onMessageReceived(helper_interface_in msg){
            boolean logon="on".equals(helper_storage.getGroupInfo(msg.groupid,"logon",null));
            if(logon){
                String logpath=helper_log.log_save_path;
                String logfile=helper_storage.getGroupInfo(msg.groupid,"logfile",null);
                if(logfile==null){
                    //无法log，配置缺失
                    return;
                }
                {
                    File logd = new File(logpath);
                    if (!logd.exists())
                        if (!logd.mkdirs())
                            return;//无法log，因为没有存储权限
                }
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss",Locale.CHINA);
                String time=simpleDateFormat.format(new Date(msg.time*1000));
                String logfilepath=logpath+"/"+logfile;
                writeFile(logfilepath,String.format("%s(%s) %s \r\n %s",msg.nickName,msg.id,time,msg.msg));//写东西
                //AwLog.Log("记录此消息！"+msg.nickName+"/"+msg.time+"/"+msg.groupid+"/"+msg.selfid+"/"+msg.msg);
            }
        }
    }
    public static class helper_legacy{
        static Pattern shiki_roll=Pattern.compile("ra([0-9]+)#([bp])([0-9]+)?(.*)?");
        static String cmd_transformation(String cmd){
            //指令兼容其他骰
            Matcher mh=shiki_roll.matcher(cmd);//ra3#p2手枪50
            if(mh.find()) {
                String A = mh.group(1), B = mh.group(2), C = mh.group(3), D = mh.group(4);
                if (C == null)
                    C = "";
                if(D==null)
                    D="";
                return String.format("r%s%s#%s%s", B, A, C, D);
            }
            return cmd;
        }
    }
    public static class helper_storage{
        public final static String storage_save_path= GlobalApplication.SDCARD+"/cocdata";
        public final static String storage_data_save_path= storage_save_path + "/data";

        //legacy sice 5.23
        static {
            //将storage_save_path下的json迁移到storage_data_save_path之中
            File newdir=new File(storage_data_save_path);
            if(!newdir.exists() && newdir.mkdirs()){//针对旧版本的数据迁移
                File dir=new File(storage_save_path);
                File[] files=dir.listFiles();
                if(files!=null) {
                    for (File file : files) {
                        if(file.isFile())
                            file.renameTo(new File(storage_data_save_path+"/"+file.getName()));
                    }
                }
            }
        }
        private static boolean initConfig(){
            File f=new File(storage_save_path);
            if(f.exists())
                return true;
            return f.mkdirs();
        }
        private static void saveConfig(String id, JSONObject data){
            if(initConfig()) {
                String config_path = storage_data_save_path +"/"+ id + ".json";
                File configFile = new File(config_path);
                try {
                    if (!configFile.exists())
                        if (!configFile.createNewFile())
                            return;
                    FileWriter fileWritter = new FileWriter(config_path, false);
                    fileWritter.write(data.toString());
                    fileWritter.close();
                } catch (Throwable ignored) {

                }
            }
        }
        private static JSONObject readConfig(String id){
            return readConfig(id,false);
        }
        private static JSONObject readConfig(String id, boolean nullable){
            if(initConfig()) {
                try {
                    String config_path = storage_data_save_path +"/"+ id + ".json";
                    FileReader fileReader = new FileReader(config_path);
                    int ch;
                    StringBuilder sb = new StringBuilder();
                    while ((ch = fileReader.read()) != -1) {
                        sb.append((char) ch);
                    }
                    return new JSONObject(sb.toString());
                } catch (Throwable ignored) {

                }
            }
            if(nullable)
                return null;
            return new JSONObject();
        }
        static void saveAbilityInfo(String id, String player, JSONObject abilities){//保存技能信息
            JSONObject obj=readConfig(id);
            JSONObject muti_abilities=obj.optJSONObject("abilities");
            if(muti_abilities==null)
                muti_abilities=new JSONObject();
            try {
                muti_abilities.put(player,abilities);
                obj.put("abilities",muti_abilities);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            saveConfig(id,obj);
        }
        static JSONObject getAbilityInfo(String id) {//读取技能信息
            return getAbilityInfo(id,getPlayerName(id));
        }
        static JSONObject getAbilityInfo(String id, String player) {//读取技能信息
            return getAbilityInfo(id,player,false);
        }
        static JSONObject getAbilityInfo(String id, String player, boolean nullable) {//读取技能信息
            JSONObject obj=readConfig(id);
            JSONObject muti_abilities=obj.optJSONObject("abilities");
            JSONObject abilities;
            JSONObject abilities_default=helper_constant_data.default_abilities();
            if(muti_abilities==null) {
                if (nullable) return null;
                muti_abilities = new JSONObject();
            }
            abilities=muti_abilities.optJSONObject(player);
            if(abilities==null) {
                if (nullable) return null;
                abilities = abilities_default;//返回默认属性
            }else{
                //找出abilities_default有但abilities没有的属性，如果找出来，则赋予默认值
                Iterator<String> it = abilities_default.keys();
                while(it.hasNext()){
                    String key = it.next();
                    if(!abilities.has(key)){
                        try {
                            abilities.put(key,abilities_default.getInt(key));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return abilities;
        }
        /*
        public static void deleteBaseInfo(String id,String key){//删除信息
            JSONObject baseInfo=getBaseInfo(id);
            baseInfo.remove(key);
            saveBaseInfo(id,baseInfo);
        }*/
        static void saveBaseInfo(String id, String key, Object value){//保存信息
            JSONObject baseInfo=getBaseInfo(id);
            try {
                baseInfo.put(key,value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            saveBaseInfo(id,baseInfo);
        }
        static Object getBaseInfo(String id, String key){
            JSONObject baseInfo=getBaseInfo(id);
            return baseInfo.opt(key);
        }
        static void saveBaseInfo(String id, JSONObject baseInfo) {//保存信息
            JSONObject config=readConfig(id);
            try {
                config.put("baseInfo",baseInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            saveConfig(id,config);
        }
        static JSONObject getBaseInfo(String id) {
            JSONObject config=readConfig(id);
            JSONObject baseInfo=config.optJSONObject("baseInfo");
            if(baseInfo==null)
                baseInfo=new JSONObject();
            return baseInfo;
        }
        static String getPlayerName(String id){
            String name=(String)getBaseInfo(id,"name");
            if(helper_calculation.textIsEmpty(name)){
                name=ENV_DEFAULT_PLAYER_NAME;
            }
            return name;
        }
        static void savePlayerName(String id, String playerName){
            //仅仅是修改当前的角色名称，不对其他数据变动
            saveBaseInfo(id,"name",playerName);
        }
        static void deletePlayerInfo(String id, String playerName){
            JSONObject obj=readConfig(id);
            JSONObject muti_abilities=obj.optJSONObject("abilities");
            if(muti_abilities==null)
                muti_abilities=new JSONObject();
            else
                muti_abilities.remove(playerName);
            try {
                obj.put("abilities",muti_abilities);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            saveConfig(id,obj);
        }
        static void saveMetaInfo(String ID, String type, String key, String value) {//保存元信息
            JSONObject obj=readConfig(type+"_"+ID,true);
            if(obj==null)
                obj=readConfig(type);
            try {
                obj.put(key,value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            saveConfig(type+"_"+ID,obj);
        }
        public static void saveGlobalInfo(String selfuin,String key,String value) {//保存全局配置信息
            saveMetaInfo(selfuin,"Global",key,value);
        }
        public static void saveGroupInfo(String groupuin,String key,String value) {//保存群配置信息
            saveMetaInfo(groupuin,"Group",key,value);
        }
        public static void savePersonInfo(String QQ,String key,String value) {//保存消息者配置信息
            saveMetaInfo(QQ,"Person",key,value);
        }
        public static String getGlobalInfo(String selfuin,String key){//获取全局配置信息
            return getGlobalInfo(selfuin,key,"");
        }
        public static String getGlobalInfo(String selfuin, String key, String def){//获取全局配置信息
            return getMetaInfo(selfuin,"Global",key,def);
        }
        public static String getGroupInfo(String groupuin, String key, String def){//获取群配置信息
            return getMetaInfo(groupuin,"Group",key,def);
        }
        public static String getPersonInfo(String QQ, String key, String def){//获取消息者配置信息
            return getMetaInfo(QQ,"Person",key,def);
        }
        public static String getMetaInfo(String ID,String type, String key, String def){
            JSONObject obj=readConfig(type+"_"+ID,true);
            if(obj==null)
                obj=readConfig(type,true);
            if(obj==null) {
                //默认配置信息
                obj=helper_constant_data.get_default_global_settings();
                if("Global".equals(type))
                    saveConfig(type+"_"+ID,obj);
            }
            String str=obj.optString(key,def);
            if(helper_calculation.textIsEmpty(str))
                str=def;
            return str;
        }
        public static void setGlobalInfoDefault(){//恢复默认配置信息
            saveConfig("Global",helper_constant_data.get_default_global_settings());
        }
    }
    static class helper_constant_data{
        static JSONObject similar_abilities,default_global_settings;
        public static class name{
            static String[] ARRAY_EnglishFirstName() {
                return new String[]{ "Aaron", "Abel", "Abraham", "Adam", "Adrian", "Aidan", "Alva", "Alex", "Alexander", "Alan", "Albert", "Alfred", "Andrew", "Andy", "Angus", "Anthony", "Apollo", "Arnold", "Arthur", "August", "Austin", "Ben", "Benjamin", "Bert", "Benson", "Bill", "Billy", "Blake", "Bob", "Bobby", "Brad", "Brandon", "Brant", "Brent", "Brian", "Brown", "Bruce", "Caleb", "Cameron", "Carl", "Carlos", "Cary", "Caspar", "Cecil", "Charles", "Cheney", "Chris", "Christian", "Christopher", "Clark", "Cliff", "Cody", "Cole", "Colin", "Cosmo", "Daniel", "Denny", "Darwin", "David", "Dennis", "Derek", "Dick", "Donald", "Douglas", "Duke", "Dylan", "Eddie", "Edgar", "Edison", "Edmund", "Edward", "Edwin", "Elijah", "Elliott", "Elvis", "Eric", "Ethan", "Eugene", "Evan", "Ford", "Francis", "Frank", "Franklin", "Fred", "Gabriel", "Gaby", "Garfield", "Gary", "Gavin", "Geoffrey", "George", "Gino", "Glen", "Glendon", "Hank", "Hardy", "Harrison", "Harry", "Hayden", "Henry", "Hilton", "Hugo", "Hunk", "Howard", "Henry", "Ian", "Ignativs", "Ivan", "Isaac", "Isaiah", "Jack", "Jackson", "Jacob", "James", "Jason", "Jay", "Jeffery", "Jerome", "Jerry", "Jesse", "Jim", "Jimmy", "Joe", "John", "Johnny", "Jonathan", "Jordan", "Jose", "Joshua", "Justin", "Keith", "Ken", "Kennedy", "Kenneth", "Kenny", "Kevin", "Kyle", "Lance", "Larry", "Laurent", "Lawrence", "Leander", "Lee", "Leo", "Leonard", "Leopold", "Leslie", "Loren", "Lori", "Lorin", "Louis", "Luke", "Marcus", "Marcy", "Mark", "Marks", "Mars", "Marshal", "Martin", "Marvin", "Mason", "Matthew", "Max", "Michael", "Mickey", "Mike", "Nathaniel", "Neil", "Nelson", "Nicholas", "Nick", "Noah", "Norman", "Oliver", "Oscar", "Owen", "Patrick", "Paul", "Peter", "Philip", "Phoebe", "Quentin", "Randall", "Randolph", "Randy", "Ray", "Reed", "Rex", "Richard", "Richie", "Riley", "Robert", "Robin", "Robinson", "Rock", "Roger", "Ronald", "Rowan", "Roy", "Ryan", "Sam", "Sammy", "Samuel", "Scott", "Sean", "Shawn", "Sidney", "Simon", "Solomon", "Spark", "Spencer", "Spike", "Stanley", "Steve", "Steven", "Stewart", "Stuart", "Terence", "Terry", "Ted", "Thomas", "Tim", "Timothy", "Todd", "Tommy", "Tom", "Thomas", "Tony", "Tyler", "Ultraman", "Ulysses", "Van", "Vern", "Vernon", "Victor", "Vincent", "Warner", "Warren", "Wayne", "Wesley", "William", "Willy", "Zack", "Zachary","Abigail", "Abby", "Ada", "Adelaide", "Adeline", "Alexandra", "Ailsa", "Aimee", "Alexis", "Alice", "Alicia", "Alina", "Allison", "Alyssa", "Amanda", "Amy", "Amber", "Anastasia", "Andrea", "Angel", "Angela", "Angelia", "Angelina", "Ann", "Anna", "Anne", "Annie", "Anita", "Ariel", "April", "Ashley", "Audrey", "Aviva", "Barbara", "Barbie", "Beata", "Beatrice", "Becky", "Bella", "Bess", "Bette", "Betty", "Blanche", "Bonnie", "Brenda", "Brianna", "Britney", "Brittany", "Camille", "Candice", "Candy", "Carina", "Carmen", "Carol", "Caroline", "Carry", "Carrie", "Cassandra", "Cassie", "Catherine", "Cathy", "Chelsea", "Charlene", "Charlotte", "Cherry", "Cheryl", "Chloe", "Chris", "Christina", "Christine", "Christy", "Cindy", "Claire", "Claudia", "Clement", "Cloris", "Connie", "Constance", "Cora", "Corrine", "Crystal", "Daisy", "Daphne", "Darcy", "Dave", "Debbie", "Deborah", "Debra", "Demi", "Diana", "Dolores", "Donna", "Dora", "Doris", "Edith", "Editha", "Elaine", "Eleanor", "Elizabeth", "Ella", "Ellen", "Ellie", "Emerald", "Emily", "Emma", "Enid", "Elsa", "Erica", "Estelle", "Esther", "Eudora", "Eva", "Eve", "Evelyn", "Fannie", "Fay", "Fiona", "Flora", "Florence", "Frances", "Frederica", "Frieda", "Gina", "Gillian", "Gladys", "Gloria", "Grace", "Grace", "Greta", "Gwendolyn", "Hannah", "Haley", "Hebe", "Helena", "Hellen", "Henna", "Heidi", "Hillary", "Ingrid", "Isabella", "Ishara", "Irene", "Iris", "Ivy", "Jacqueline", "Jade", "Jamie", "Jane", "Janet", "Jasmine", "Jean", "Jenna", "Jennifer", "Jenny", "Jessica", "Jessie", "Jill", "Joan", "Joanna", "Jocelyn", "Joliet", "Josephine", "Josie", "Joy", "Joyce", "Judith", "Judy", "Julia", "Juliana", "Julie", "June", "Karen", "Karida", "Katherine", "Kate", "Kathy", "Katie", "Katrina", "Kay", "Kayla", "Kelly", "Kelsey", "Kimberly", "Kitty", "Lareina", "Lassie", "Laura", "Lauren", "Lena", "Lydia", "Lillian", "Lily", "Linda", "lindsay", "Lisa", "Liz", "Lora", "Lorraine", "Louisa", "Louise", "Lucia", "Lucy", "Lucine", "Lulu", "Lydia", "Lynn", "Mabel", "Madeline", "Maggie", "Mamie", "Manda", "Mandy", "Margaret", "Mariah", "Marilyn", "Martha", "Mavis", "Mary", "Matilda", "Maureen", "Mavis", "Maxine", "May", "Mayme", "Megan", "Melinda", "Melissa", "Melody", "Mercedes", "Meredith", "Mia", "Michelle", "Milly", "Miranda", "Miriam", "Miya", "Molly", "Monica", "Morgan", "Nancy", "Natalie", "Natasha", "Nicole", "Nikita", "Nina", "Nora", "Norma", "Nydia", "Octavia", "Olina", "Olivia", "Ophelia", "Oprah", "Pamela", "Patricia", "Patty", "Paula", "Pauline", "Pearl", "Peggy", "Philomena", "Phoebe", "Phyllis", "Polly", "Priscilla", "Quentina", "Rachel", "Rebecca", "Regina", "Rita", "Rose", "Roxanne", "Ruth", "Sabrina", "Sally", "Sandra", "Samantha", "Sami", "Sandra", "Sandy", "Sarah", "Savannah", "Scarlett", "Selma", "Selina", "Serena", "Sharon", "Sheila", "Shelley", "Sherry", "Shirley", "Sierra", "Silvia", "Sonia", "Sophia", "Stacy", "Stella", "Stephanie", "Sue", "Sunny", "Susan", "Tamara", "Tammy", "Tanya", "Tasha", "Teresa", "Tess", "Tiffany", "Tina", "Tonya", "Tracy", "Ursula", "Vanessa", "Venus", "Vera", "Vicky", "Victoria", "Violet", "Virginia", "Vita", "Vivian", "Wanda", "Wendy", "Whitney", "Wynne", "Winnie", "Yolanda", "Yvette", "Yvonne", "Zara", "Zelda", "Zoey", "Zora" };
            }
            static String[] ARRAY_EnglishLastName() {
                return new String[]{ "Admirind", "Aerum", "Akvum", "Ambrofaltkhawsen", "Ameblo", "Amik", "Amomian", "Arbar", "Barbarkor", "Bier", "Bird", "Biterlif", "Bondno", "Bravul", "Burlu", "Butik", "Celum", "Chener", "Chipen", "Delolmo", "Devum", "Domet", "Ehhum", "Emilan", "Enhhoran", "Esper", "Estrum", "Fajrer", "Famili", "Fesanan", "Filopator", "Fiskan", "Flugil", "Garan", "Geralan", "Gimik", "Glaving", "Grinhilt", "Gust", "Gharden", "Hakil", "Hark", "Haska", "Heldan", "Herb", "Homar", "Horbek", "Hhorum", "Inkuj", "Interes", "Irlan", "Ivens", "Jablich", "Jagu", "Jarum", "Junul", "Jhurnal", "Kamino", "Kandeling", "Kanjas", "Karlan", "Klub", "Kodlar", "Korjas", "Kovert", "Kradan", "Kredeblo", "Kruf", "Kudril", "Kuirej", "Kunul", "Kuvan", "Kvarop", "Laget", "Lamris", "Land", "Libret", "Loghej", "Lumstel", "Makavel", "Maksipes", "Marban", "Marist", "Marsaus", "Marum", "Mehhkaprad", "Memorind", "Montum", "Montril", "Nakan", "Nomum", "Oktoped", "Ostum", "Paner", "Panum", "Pentium", "Pentrist", "Pepian", "Pilk", "Piruj", "Pluming", "Plumuj", "Pluver", "Pomuj", "Preghej", "Pulver", "Rafnil", "Ralfan", "Rastagan", "Razil", "Regnestrum", "Regum", "Richul", "Rukspin", "Sabler", "Saghulo", "Sagum", "Saist", "Skatol", "Stelum", "Suker", "Shlosil", "Shuist", "Tander", "Terikan", "Tipum", "Traman", "Tranchil", "Travis", "Urbum", "Vendej", "Verdajh", "Verum", "Vilanan", "Vinberuj", "Vishon", "Vort", "Zerkos", "Zijat", "Zorajan","Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia", "Martinez", "Robinson", "Clark", "Rodriguez", "Lewis", "Lee", "Walker", "Hall", "Allen", "Young", "Hernandez", "King", "Wright", "Lopez", "Hill", "Scott", "Green", "Adams", "Baker", "Gonzalez", "Nelson", "Carter", "Mitchell", "Perez", "Roberts", "Turner", "Phillips", "Campbell", "Parker", "Evans", "Edwards", "Collins", "Stewart", "Sanchez", "Morris", "Rogers", "Reed", "Cook", "Morgan", "Bell", "Murphy", "Bailey", "Rivera", "Cooper", "Richardson", "Cox", "Howard", "Ward", "Torres", "Peterson", "Gray", "Ramirez", "James", "Watson", "Brooks", "Kelly", "Sanders", "Price", "Bennett", "Wood", "Barnes", "Ross", "Henderson", "Coleman", "Jenkins", "Perry", "Powell", "Long", "Patterson", "Hughes", "Flores", "Washington", "Butler", "Simmons", "Foster", "Gonzales", "Bryant", "Alexander", "Russell", "Griffin", "Diaz", "Hayes" };
            }
            static String[] ARRAY_EnglishFirstNameChineseTranslation() {
                return new String[]{ "亚伦", "亚伯", "亚伯拉罕", "亚当", "艾德里安", "艾登/艾丹", "阿尔瓦", "亚历克斯", "亚历山大", "艾伦", "艾伯特", "阿尔弗雷德", "安德鲁", "安迪", "安格斯", "安东尼", "阿波罗", "阿诺德", "亚瑟", "奥古斯特", "奥斯汀", "本", "本杰明", "伯特", "本森", "比尔", "比利", "布莱克", "鲍伯", "鲍比", "布拉德", "布兰登", "布兰特", "布伦特", "布赖恩", "布朗", "布鲁斯", "迦勒", "卡梅伦", "卡尔", "卡洛斯", "凯里", "卡斯帕", "塞西", "查尔斯", "采尼", "克里斯", "克里斯蒂安", "克里斯多夫", "克拉克", "柯利弗", "科迪", "科尔", "科林", "科兹莫", "丹尼尔", "丹尼", "达尔文", "大卫", "丹尼斯", "德里克", "狄克", "唐纳德", "道格拉斯", "杜克", "迪伦", "埃迪", "埃德加", "爱迪生", "艾德蒙", "爱德华", "艾德文", "以利亚", "艾略特", "埃尔维斯", "埃里克", "伊桑", "柳真", "埃文", "福特", "弗兰克思", "弗兰克", "富兰克林", "弗瑞德", "加百利", "加比", "加菲尔德", "加里", "加文", "杰弗里", "乔治", "基诺", "格林", "格林顿", "汉克", "哈帝", "哈里森", "哈利", "海顿", "亨利", "希尔顿", "雨果", "汉克", "霍华德", "亨利", "伊恩", "伊格纳缇伍兹", "伊凡", "艾萨克", "以赛亚/艾塞亚", "杰克", "杰克逊", "雅各布", "詹姆士", "詹森", "杰伊", "杰弗瑞", "杰罗姆", "杰瑞", "杰西", "吉姆", "吉米", "乔", "约翰", "约翰尼", "乔纳森", "乔丹", "约瑟夫", "约书亚", "贾斯汀", "凯斯", "肯", "肯尼迪", "肯尼斯", "肯尼", "凯文", "凯尔", "兰斯", "拉里", "劳伦特", "劳伦斯", "利安德尔", "李", "雷欧", "雷纳德", "利奥波特", "莱斯利", "劳伦", "劳瑞", "劳瑞恩", "路易斯", "卢克/路加", "马库斯", "马西", "马克", "马科斯", "马尔斯", "马歇尔", "马丁", "马文", "梅森", "马修", "马克斯", "迈克尔", "米奇", "麦克", "纳撒尼尔", "尼尔", "尼尔森", "尼古拉斯", "尼克", "诺亚", "诺曼", "奥利弗", "奥斯卡", "欧文", "帕特里克/派翠克", "保罗", "彼得", "菲利普", "菲比", "昆廷", "兰德尔", "伦道夫", "兰迪", "雷", "列得", "雷克斯", "理查德", "里奇", "赖利/瑞利", "罗伯特", "罗宾", "罗宾逊/鲁宾逊", "洛克", "罗杰", "罗纳德", "罗文", "罗伊", "赖安", "萨姆/山姆", "萨米", "塞缪尔", "斯考特", "肖恩", "肖恩", "西德尼", "西蒙", "所罗门", "斯帕克", "斯宾塞", "斯派克", "斯坦利", "史蒂夫", "史蒂文", "斯图尔特", "斯图亚特", "特伦斯", "特里", "泰德", "托马斯", "提姆", "蒂莫西", "托德", "汤米", "汤姆", "托马斯", "托尼", "泰勒", "奥特曼", "尤利塞斯", "范", "弗恩", "弗农", "维克多", "文森特", "华纳", "沃伦", "韦恩", "卫斯理", "威廉", "威利/维利", "扎克", "圣扎迦利","阿比盖尔", "艾比", "艾达", "阿德莱德", "艾德琳", "亚历桑德拉", "艾丽莎", "艾米", "亚历克西斯", "爱丽丝", "艾丽西娅", " 艾琳娜", "艾莉森", "艾莉莎/爱丽丝娅", "阿曼达", "艾美", "安伯", "阿纳斯塔西娅", "安德莉亚", "安琪", "安吉拉", "安吉莉亚", "安吉莉娜", "安", "安娜", "安妮", "安妮", "安尼塔", "艾莉尔", "阿普里尔", "艾许莉/阿什利/艾希礼", "欧蕊", "阿维娃", " 笆笆拉", "芭比", "贝亚特", "比阿特丽斯", "贝基", "贝拉", "贝斯", "贝蒂", "贝蒂", "布兰奇", "邦妮", "布伦达", "布莱安娜", "布兰妮", "布列塔尼", "卡米尔", "莰蒂丝", "坎蒂", "卡瑞娜", "卡门", "凯罗尔", "卡罗琳", "凯丽", "凯莉", "卡桑德拉", "凯西", "凯瑟琳", "凯茜", "切尔西", "沙琳", "夏洛特", "切莉", "雪莉尔", "克洛伊", "克莉丝", "克里斯蒂娜", "克里斯汀", "克里斯蒂", "辛迪", "克莱尔", "克劳迪娅", "克莱门特", "克劳瑞丝", "康妮", "康斯坦斯", "科拉", "科瑞恩", "科瑞斯特尔", "戴茜", "达芙妮", "达茜", "戴夫", "黛比", "黛博拉", "黛布拉", "黛米", "黛安娜", "德洛丽丝", "堂娜", "多拉", "桃瑞丝", "伊迪丝", "伊迪萨", "伊莱恩", "埃莉诺", "伊丽莎白", "埃拉", "爱伦", "艾莉", "艾米瑞达", "艾米丽", "艾玛", "伊妮德", "埃尔莎", "埃莉卡", "爱斯特尔", "爱丝特", "尤杜拉", "伊娃", "伊芙", "伊夫林", "芬妮", "费怡", "菲奥纳", "福罗拉", "弗罗伦丝", "弗郎西丝", "弗雷德里卡", "弗里达", "吉娜", "吉莉安", "格拉蒂丝", "格罗瑞娅", "格瑞丝", "格瑞丝", "格瑞塔", "格温多琳", "汉娜", "海莉", "赫柏", "海伦娜", "海伦", "汉纳", "海蒂", "希拉里/希拉蕊/希拉莉", "英格丽德", "伊莎贝拉", "爱沙拉", "艾琳", "艾丽丝", "艾维", "杰奎琳", "小玉", "詹米", "简", "珍妮特", "贾斯敏", "姬恩", "珍娜", "詹妮弗", "詹妮", "杰西卡", "杰西", "姬尔", "琼", "乔安娜", "乔斯林", "乔莉埃特", "约瑟芬", "乔茜", "乔伊", "乔伊斯", "朱迪丝", "朱蒂", "朱莉娅", "朱莉安娜", "朱莉", "朱恩", "凯琳", "卡瑞达", "凯瑟琳", "凯特", "凯西", "卡蒂", "卡特里娜", "凯", "凯拉", "凯莉", "凯尔西", "特里娜", "基蒂", "莱瑞拉", "蕾西", "劳拉", "罗兰/劳伦", "莉娜", "莉迪娅", "莉莲", "莉莉", "琳达", "琳赛", "丽莎", "莉兹", "洛拉", "罗琳", "路易莎", "路易丝", "露西娅", "露茜", "露西妮", "露露", "莉迪娅/莉蒂亚", "林恩", "梅布尔/玛佩尔", "马德琳", "玛姬", "玛米", "曼达", "曼迪", "玛格丽特", "玛丽亚", "玛里琳/玛丽莲/玛丽琳", "玛莎", "梅维丝", "玛丽", "玛蒂尔达", "莫琳", "梅维丝", "玛克辛", "梅", "梅米", "梅甘", "梅琳达", "梅利莎", "美洛蒂", "默西迪丝", "梅瑞狄斯", "米娅", "米歇尔", "米莉", "米兰达", "米里亚姆", "米娅", "茉莉", "莫尼卡", "摩尔根/摩根", "南茜", "娜塔莉", "娜塔莎", "妮可", "尼基塔", "尼娜", "娜拉/诺拉", "诺玛", "尼迪亚", "奥克塔维亚", "奥琳娜", "奥利维亚", " 奥菲莉娅", "奥帕", "帕梅拉", "帕特丽夏", "芭迪", "保拉", "波琳", "珀尔", "帕姬", "菲洛米娜", "菲比", "菲丽丝", "波莉", "普里西拉", "昆蒂娜", "雷切尔", "丽贝卡", "瑞加娜", "丽塔", "罗丝", "洛克萨妮", "露丝", "萨布丽娜", "萨莉", " 桑德拉", "萨曼莎", "萨米", "桑德拉", "桑迪", "莎拉", "萨瓦纳/萨瓦娜", "斯佳丽/斯嘉丽", "塞尔玛", "塞琳娜", "塞丽娜", "莎伦", "希拉", "雪莉", "雪丽", "雪莉", "斯莱瑞", "西尔维亚", "索尼亚", "索菲娅", "丝塔茜", "丝特拉", "斯蒂芬妮", "苏", "萨妮", "苏珊", "塔玛拉", "苔米", "谭雅坦尼娅", "塔莎", "特莉萨", "苔丝", "蒂凡妮", "蒂娜", "棠雅/东妮亚", "特蕾西", "厄休拉", "温妮莎", "维纳斯", "维拉", "维姬", "维多利亚", "维尔莉特", "维吉妮亚", "维达", "薇薇安", "旺达", "温蒂", "惠特尼", "韦恩", "温妮", "尤兰达", "伊薇特", "伊温妮", "莎拉", "塞尔达", "佐伊", "卓拉" };
            }
            static String[] ARRAY_EnglishLastNameChineseTranslation() {
                return new String[]{ "阿德米林德", "阿埃鲁姆", "阿克乌姆", "安布罗法尔特克豪森", "阿梅布洛", "阿米克", "阿莫米安", "阿尔巴尔", "巴尔巴尔科尔", "比埃尔", "比尔德", "比特尔利夫", "邦德诺", "布拉乌尔", "布尔卢", "布蒂克", "蔡卢姆", "切内尔", "奇彭", "德洛尔莫", " 德乌姆", "多梅特", "埃胡姆", "埃米兰", "恩霍兰", "埃斯佩尔", "埃斯特鲁姆", "法伊雷尔", "法米利", "费萨南", "菲洛帕托尔", "菲斯坎", "弗卢吉尔", "加兰", "格拉兰", "吉米克", "格拉温格", "格林希尔特", "古斯特", "扎尔登", "哈基尔", "哈尔克", "哈斯卡", "海尔丹", "赫尔布", "霍马尔", "霍尔贝克", "霍鲁姆", "因奎", "因特雷斯", "伊尔兰", "伊文斯", "亚布利奇", "亚古", "亚鲁姆", "尤努尔", "茹尔纳尔", "卡米诺", "坎德林格", "卡尼亚斯", "卡尔兰", "克卢布", "科德拉尔", "科里亚斯", "科维尔特", "克拉丹", "克雷德布洛", "克鲁夫", "库德里尔", "库伊雷", "库努尔", "库万", "克瓦罗普", "拉格特", "拉姆里斯", "兰德", "利布雷特", "洛哲伊", "卢姆斯特尔", "马卡维尔", "马克西佩斯", "马尔班", "马里斯特", "马尔萨乌斯", "马鲁姆", "梅赫卡普拉德", "梅莫林德", "蒙图姆", "蒙特里尔", "纳坎", "诺穆姆", "奥克托佩德", "奥斯图姆", "帕内尔", "帕努姆", "彭蒂乌姆", "彭特里斯特", "佩皮安", "皮尔克", "皮鲁伊", "普卢明格", "普卢穆伊", "普卢维尔", "波穆伊", "普雷哲伊", "普尔维尔", "拉夫尼尔", "拉尔凡", "拉斯塔甘", "拉齐尔", "雷格内斯特鲁姆", "雷古姆", "里楚尔", "鲁克斯平", "萨布雷尔", "萨朱尔", "萨古姆", "萨伊斯特", "斯卡托尔", "斯特卢姆", "苏克尔", "施洛西尔", "舒伊斯特", "坦德尔", "特里坎", "蒂普姆", "特拉曼", "特兰奇尔", "特拉维斯", "乌尔布姆", "温德伊", "维尔达日", "维鲁姆", "维拉南", "温贝鲁伊", "碧声", "沃尔特", "泽尔科斯", "齐亚特", "佐拉扬", "史密斯", "约翰逊", "威廉姆斯", "约翰", "布朗", "戴维斯", "米勒", "威尔逊", "摩尔", "泰勒", "安德森", "托马斯", "杰克逊", "怀特", "哈里斯", "马丁", "汤姆逊", "加西亚", "马丁内斯", "罗宾森", "克拉克", "罗德里格斯" ,"路易斯", "李", "沃克", "霍尔", "艾伦", "杨", "埃尔南德斯", "金", "赖特", "洛佩兹", "伊尔", "斯科特", "格林", "亚当", "贝克", "冈萨雷斯", "纳尔逊", "卡特", "米切尔", "佩雷斯", "罗伯特", "特纳", "菲利普", "坎贝尔", "帕克", "埃文斯", "爱德华", "柯林斯", "斯图尔特", "桑切斯", "莫里斯", "罗杰斯", "里德", "库克", "摩根", "贝尔", "墨菲", "贝利", "里韦拉", "库珀", "理查德森", "考克斯", "霍华德", "沃德", "托里斯", "彼得森", "格瑞", "拉米雷斯", "詹姆斯", "沃森", "布鲁克斯", "凯莉", "桑德斯", "普里斯", "班尼特", "伍德", "巴恩斯", "罗斯", "亨德森", "科尔曼", "詹金斯", "佩里", "鲍威尔", "隆", "帕特森", "休斯", "弗洛雷斯", "华盛顿", "巴特勒", "西蒙", "福斯特", "冈萨雷斯", "布赖恩特", "亚历克斯", "拉塞尔", "格里芬", "亚兹", "海斯" };
            }
            static String[] ARRAY_JapaneseSurname() {
                return new String[]{ "鹤田","香取","野泽","麻生","小田切","草翦","稻垣","木村","中居","濑户","山下","酒井","松本","石田","柴崎","藤原","福山","江口","唐泽","长泽","椎名","松岛","白石","铃木","堂本","仲间","织田","泷泽","妻夫木","药师丸","余贵","石黑","丰川","平宫","工藤","赤西","生田","高岛","松山","井之原","锦户","城田","竹野内","广末","二宫","石垣","小松","小栗","田中","滨崎","滨田","幸田","志田","香椎","山本","原田","永山","栗山","前田","冈部","忍成","寺岛","黑木","水野","伊势谷","野口","土屋","北乃","绫濑","泽尻","荣仓","加藤","宫崎","风间","户田","山口","井川","深田","米仓","佐藤","小池","上野","伊东","须藤","长濑","倍赏","岸谷","赤坂","中村","相叶","今井","黑川","伊藤","五十岚","冈田","野际","岛谷","堤","加濑","吉田","观月","深津","洼冢","役所","山田","吹石","吉冈","内田","阿部","吉泽","松田","长谷川","国仲","上川","北村","宝生","京野","天海","中山","中谷","香川","吉永","冈本","相武","向井","稻森","成海","市川","玉山","龟梨","松下","高桥","仲代","井上","吉川","手冢" ,"友坂","宫泽","樱井","大野","多部未","上户","平冈","能濑","手越","宇多田","仓木","安室奈","美木","小野","中岛","竹中","中井","吉高","安藤","川岛","菊川","管野","安倍","市原","小泉","苍井","加藤","浅野","冢本","筱原","白川","村川","矢田","三浦","入江","管谷","小仓","水岛","大政","上原","蛯原","津川","阵内","内山","江角","柳叶","西田","常盘","树木","高冈","泽口","南野","田口","相田","相马","押尾","佐佐木","秋山","北川","松坂","高仓","三船","栗原","松雪","横山","武田","岩佐","丹波","行定","渡部","本木","桃井","储形","乙羽","大冢","泽村","中越","夏川","森田","三宅","坂本","华原","细川","小林","渥美","泽田","北野","黑川","小室","寺尾","今村","小津","深作","大岛","玉置","田所","野兽","筱田","寺山","若松","黑泽","沟口","押井","岩井","谷村","宇津","西村","矢泽","稻山","吉武","八尾","古尾谷","贯地谷","不破","若月","高村","伊佐","牛岛","杉山","神木","松川","要","堺","本乡","水川","释由","石原","藤木","平山","笕利","饭田","饭岛","堀北","广濑","藤井","片濑","谷原","金子","江户川","福田","津岛","横沟","佐野","丸山","平井","柳井","有坂","水桥","铜谷","草野","内博","南泽","樱庭","新垣","末永","伊崎","森村","高木","川端","沟端","横光","芥川","矢井田","藤田","森山","持田","一青","松尾","尾崎","小川","大江","三岛","清少","夏目","清水","爱内","伴都","黑石","古谷","松浦","清浦","后藤","远藤","增田","小山","满","野间","村上","森","三枝","竹井","坂井","新居","石川","藤本","大仓","安部","池田","岸本","岩田","北原","宇德","上木","近江","水树","小出","冈崎","加护","玉木","奥井","中原","植田","植草","东山","梶浦","绀野","原田","原纱","金田" ,"锦织","能登","牧野","堀江","石松","堀内","广桥","池泽","南里","千叶","小西","近藤","三木","折笠","河原木","神田","野中","野岛","川澄","种村","桑岛","宫小路","福井","丰崎","藤堂","西门","花泽","桧山","新谷","高泽见","大谷","久川","早见","纪野","茅原","中森","大原","神谷","藤村","户松","阪口","桑谷","小林","小野","上杉","源","饭冢","菊地","生天目","名冢","武内","新井","横手","越智","松谷","岭","樱内","齐藤","斋藤","太田","木下","福永","千野","鸠山","渡边","菊池","美部浓","末弘","平山","石桥","大久保","秋月","竹内","武见","松冈","岸","犬神","金田一","竹下","内藤","柏原","泉谷","大泉","森高","森下","牛尾","安西","正田","小和田","黑田","森嘉","松崎","森永","加纳","野田","荒船","近卫","细川护","千","江崎","叶山","濑名","龟山","杉尾","臼井","久保田","奥泽","小石川","冰室","朝仓","杉崎","星野","矢吹","真壁","生野","沟口","冲岛","町田","田村","西川","小泽","池内","大淹","梅田","山崎","北田","小岩井","片桐","内野","水原","纯名","黑崎","森口","吉本","井筒","筒井","井之上","长岭","浅见","野村","品川","生濑","黑谷","游川","八木","土井","难波","片山","北井","别所","五代","田渊","小椋","高丸","市村","长冢","秋吉","吹越","日向志","藏原","长井","杉村","奥贯","望月","井田","桥爪","神尾","道明寺","美作","三条","大河原","青池","日向","中岛","重村","堀口","楠田","周防","宅间","小牧","重冈","星谷","佐伯","江黑","坂上","笹峰","浅井","利根川","山野","宫下","赤井","家富","飞松","樱田","山室","水黑","彩田","大卫","栗卷","佐田","石野","富浦","加贺","坪井","三城","武藤","佐竹","织部","鹤见","水月","桥田","田岛","岩本","西浦","叶野","泷村","日比","野弥","小柳","北岛","宫林","胜亦","大森","美山","大杉","中江","平野","堂岛","大泽","田山" };
            }
            static String[] ARRAY_JapaneseFirstName() {
                return new String[]{ "鮎美","真纪","美沙","翔子","里奈","凉","里代","千春","智沙","直子","友美","七恵","丽","里奈","真白","美砂","直子","纪子","希","梨香","工美","琴美","未央","佐和子","贵子","裕子","夏実","美恵子","峰子","真子","奈绪美","顺子","里奈","千裕","香织","加穂里","淳子","瞳","実岭","芽衣","奈未","沙树","裕子","丽子","里奈","有未","由贵","久美子","明美","恵利香","加奈子","留美","优子","典予","夏実","恵津子","一子","加奈子","弥代","奈々子","亜美","奈美","麻里","芳子","纪香","恭子","晴美","絵里","一恵","有美子","晶","美穂","芳香","纱菜","理沙","理恵子","美智子","恵","舞","美奈子","朱里","麻矢","美帆子","未来","千晶","史奈","久美子","志乃","香子","映莉子","奈美","茜","桃子","直美","美纪","美贵","芳美","未来","絵美","玲亜","朋美","菜穂子","加奈子","茧子","由香","真梨","绫","美保","美纪","凉子","树里","理沙","美佳","真由美","揺","香织","庆","真里","直美","未奈","华英","茜","薫","结花","夕纪","直子","五月","五月","真弓","清夏","花衣","恵理子","晶子","美沙","瞳","千鹤","絵里子","友香","朱美","理恵","奈绪","律子","雅美","翠","晴美","麻美","恵","直美","理絵","梨花","贵子","阳子","都记子","智子","里穂","丽矢","絵理香","美纪","亮子","由贵子","裕子","由美","麻衣","尚子","美和","毬絵","里沙","朋美","幸子","洋子","文子","志乃","美穂","瑞穂","贵子","美奈子","贵子","纪子","仁美","萌","理沙","真由美","奈保子","麻衣子","千里","理沙","恵美","佳子","亜希子","爱","范子","和津美","雏妃","花衣","典子","彩水","爱美","万理江","沙也果","佳乃","优","晶子","香","まゆ","由香利","恵理","美幸","里穂","晶","唯","绫乃","绢香","令子","有子","雪乃","仁见","亜弥","香织","飞香","智子","可奈子","玲子","里美","恭香","絵美","佐智子","千春","春奈","丽奈","比吕","絵真","先辈","浩二","莲","飒太","大翔","大和","翔太","凑","悠人","大辉","苍空","龙生","阳","阳斗","陆","陆斗","飒真","瑛太","悠真","飒汰","树","苍大","悠斗","阳太","一飒","结人","虎太郎","太阳","隼人","遥斗","阳向","飒","海翔","优心","阳翔","龙之介","翔","辉","结斗","春辉","晴","苍","苍介","智也","直辉","优希","悠翔","阳大","翼","琉生","飒介","绚斗","瑛斗","干太","空","春翔","晴琉","圣","奏太","苍真","苍天","大智","斗真","枫","佑真","优","勇斗","悠","雄大","凉太","煌","煌大","飒斗","葵","一辉","一真","瑛大","咏太","海音","岳","庆太","结翔","健","光希","航平","朔也","春斗","瞬","匠","渉","丈","奏音","苍汰","太一","泰生","大空","大悟","大晟","拓海","拓実","暖","直树","哲平","碧人","优斗","勇翔","悠雅","悠介","悠希","悠月","悠马","阳人","璃空","琉雅","琉斗","龙希","龙成","龙星","亮太","莲斗","和真","翔大","飒一","飒人" };
            }
            static String[] ARRAY_ChineseSurname(){
                return new String[]{ "赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许", "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎", "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安", "常", "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和", "穆", "萧", "尹", "姚", "邵", "舒", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊", "纪", "屈", "项", "祝", "董", "杜", "阮", "蓝", "闵", "席", "季", "麻", "强", "贾", "路", "娄", "危", "江", "童", "颜", "郭", "梅", "盛", "林", "刁", "钟", "徐", "邱", "骆", "高", "夏", "蔡", "田", "樊", "胡", "凌", "霍", "虞", "万", "支", "柯", "咎", "管", "卢", "莫", "经", "房", "裘", "缪", "干", "解", "应", "宗", "宣", "丁", "贲", "邓", "郁", "单", "杭", "洪", "包", "诸", "左", "石", "崔", "吉", "钮", "龚", "程", "嵇", "邢", "滑", "裴", "陆", "荣", "翁", "荀", "羊", "於", "惠", "甄", "加", "封", "芮", "羿", "储", "靳", "汲", "邴", "糜", "松", "井", "段", "富", "巫", "乌", "焦", "巴", "弓", "牧", "隗", "山", "谷", "车", "侯", "宓", "蓬", "全", "郗", "班", "仰", "秋", "仲", "伊", "宫", "宁", "仇", "栾", "暴", "甘", "钭", "厉", "戎", "祖", "武", "符", "刘", "詹", "束", "龙", "叶", "幸", "司", "韶", "郜", "黎", "蓟", "薄", "印", "宿", "白", "怀", "蒲", "台", "从", "鄂", "索", "咸", "籍", "赖", "卓", "蔺", "屠", "蒙", "池", "乔", "阴", "胥", "能", "苍", "双", "闻", "莘", "党", "翟", "谭", "贡", "劳", "逄", "姬", "申", "扶", "堵", "冉", "宰", "郦", "雍", "璩", "桑", "桂", "濮", "牛", "寿", "通", "边", "扈", "燕", "冀", "郏", "浦", "尚", "农", "温", "别", "庄", "晏", "柴", "瞿", "阎", "充", "慕", "连", "茹", "习", "宦", "艾", "鱼", "容", "向", "古", "易", "慎", "戈", "廖", "庚", "终", "暨", "居", "衡", "步", "都", "耿", "满", "弘", "匡", "国", "文", "寇", "广", "禄", "阙", "东", "殳", "沃", "利", "蔚", "越", "夔", "隆", "师", "巩", "厍", "聂", "晁", "勾", "敖", "融", "冷", "訾", "辛", "阚", "那", "简", "饶", "空", "曾", "毋", "沙", "乜", "养", "鞠", "须", "丰", "巢", "关", "蒯", "相", "查", "后", "红", "游", "竺", "权", "逯", "盖", "益", "桓", "公", "晋", "楚", "法", "汝", "鄢", "涂", "钦", "缑", "亢", "况", "有", "商", "牟", "佘", "佴", "伯", "赏", "墨", "哈", "谯", "笪", "年", "爱", "阳", "佟", "琴", "言", "福", "百", "家", "姓", "续", "岳", "帅","第五", "梁丘", "左丘", "东门", "百里", "东郭", "南门", "呼延", "万俟", "南宫", "段干", "西门", "司马", "上官", "欧阳", "夏侯", "诸葛", "闻人", "东方", "赫连", "皇甫", "尉迟", "公羊", "澹台", "公冶", "宗政", "濮阳", "淳于", "仲孙", "太叔", "申屠", "公孙", "乐正", "轩辕", "令狐", "钟离", "闾丘", "长孙", "慕容", "鲜于", "宇文", "司徒", "司空", "亓官", "司寇", "子车", "颛孙", "端木", "巫马", "公西", "漆雕", "壤驷", "公良", "夹谷", "宰父", "微生", "羊舌" };
            }
            static String[] ARRAY_ChineseFirstName(){
                return new String[]{ "夫秀", "夫琴", "夫艳", "夫霞", "夫文", "夫娜", "夫艳", "夫瑶", "夫红", "夫美", "夫萍", "夫娥", "夫玲", "夫琳", "夫颖", "夫娜", "夫蓉", "夫莉", "夫萍", "夫文", "夫英", "夫丽", "夫丽", "夫英", "夫丽", "夫蓉", "池悦", "池婷", "池秀", "池萍", "池文", "池莹", "池萍", "池文", "池娜", "池怡", "池丽", "池骅", "池冉", "池冉", "池婷", "池玉", "池娜", "池妍", "池颖", "池萍", "池琼", "池芳", "池瑶", "池悦", "池秀", "池颖", "池悦", "池芳", "池莹", "普娟", "普琳", "普文", "普艳", "普文", "普霞", "普娟", "普媛", "普玉", "普媛", "普娜", "普怡", "普芳", "普悦", "普英", "普丽", "普颖", "普莉", "普妍", "普英", "普文", "普英", "普英", "普萍", "普媛", "普文", "普芳", "普萍", "普瑛", "普文", "普文", "普悦", "普娟", "沙丽", "沙燕", "沙玲", "沙琴", "沙妍", "沙玉", "沙玉", "沙艳", "沙燕", "沙蓉", "沙丽", "沙丽", "沙芬", "沙玉", "沙妍", "沙娜", "沙娜", "沙莉", "沙娜", "沙莉", "沙妍", "沙倩", "沙丽", "沙琳", "沙妍", "沙丽", "沙丽", "沙娜", "沙莉", "沙蓉", "沙英", "沙娜", "沙莉", "沙娜", "沙艳", "沙丽", "沙婧", "滨秀", "滨娜", "滨霞", "滨茹", "滨玲", "滨花", "滨艳", "滨嫣", "滨莉", "滨玉", "滨倩", "滨媛", "滨倩", "滨霞", "滨倩", "滨婧", "滨瑛", "滨蓉", "滨妍", "滨琳", "滨悦", "滨怡", "滨媛", "滨莹", "滨媛", "滨英", "滨婷", "滨妍", "滨莹", "滨英", "滨霞", "滨芬", "滨莉", "滨莹", "滨悦", "滨玲", "滨红", "滨悦", "滨花", "滨萍", "滨玲", "滨燕", "滨茹", "滨雪", "滨瑛", "滨霞", "滨娟", "滨茹", "滨秀", "滨婷", "滨洁", "滨倩", "滨梅", "滨文", "滨倩", "滨妍", "滨霞", "滨莹", "滨婧", "滨文", "毓如", "丞熙", "胤霄", "丞祥", "禺琛", "毓立", "毓函", "毓轩", "剜庭", "毓华", "毓宏", "丞颢", "毓同", "毓宁", "丕兰", "卦岸", "毓欣", "胤旋", "禺昊", "丞献", "胤淇", "毓恬", "胤其", "攸越", "剞钧", "毓瀚", "毓铭", "卅卜", "毓健", "丌骊", "胤吕", "丞铭", "胤臻", "丞爨", "仄晟", "馗夕", "佚萌", "胤华", "赜琛", "毓涵", "毓宣", "胤杰", "仞烊", "乜瞑", "丕秀", "丕宏", "丕森", "丞熙", "毓雅", "胤谛", "攸月", "毓斐", "胤豪", "丕程", "佚桀", "丞叶", "卅戎", "禺笑", "丞贤", "剞海", "伉形", "胤椿", "匕海", "仃宇", "攸骊", "丕彬", "丞晟", "丞明", "毓曦", "兀韪", "禺辰", "丞睿", "攸书", "丕馨", "毓均", "丕云", "匕海", "丕裕", "仞冲", "丕昌", "丕络", "毓纯", "毓康", "毓真", "毓萱", "伛平", "剡立", "胤熹", "佚宏", "胤鑫", "仡葱", "佚轩", "亓亓", "毓晗", "毓馨", "毓宾", "丞晨", "馗三", "丞昊", "攸翔", "丞标", "胤睿", "佚伶", "胤臻", "毓淇", "佚炜", "佚然", "毓萱", "胤雯", "佚名", "丞宇", "乜罡", "亓鹂", "亘祺", "佚帅", "毓康", "毓华", "胤汀", "丞华", "毓萱", "毓璋", "毓辰", "丕凯", "胤江", "仡匆", "丕福", "仵臻", "毓蔓", "丞宽", "毓鑫", "胤栋", "丞秋", "仃囡", "毓含", "毓僮", "禺景", "剞作", "丞工", "丞锂", "胤辉", "丞彦", "毓聪", "毓清", "丞弘", "仉骅", "禺日", "佚海", "丞民", "乜栩", "毓才", "丞栊", "毓珈", "丌涵", "毓鸿", "佚菱", "毓辰", "丞杰", "乇旦", "丕力", "毓怀", "兀伊", "毓宁", "胤昊", "毓荭", "胤兆", "毓麟", "毓璐", "丞海", "馗圣", "毓辰", "丞第", "禺森", "冰怡", "冰婷", "冰悦", "冰燕", "冰颖", "冰雪", "冰洁", "冰倩", "冰瑶", "冰玉", "冰怡", "冰秀", "冰艳", "冰梅", "冰莹", "冰倩", "冰莉", "冰瑛", "冰文", "冰倩", "冰秀", "冰雪", "冰琼", "冰梅", "冰倩", "冰妍", "冰娟", "冰雪", "冰莹", "冰娜", "冰洁", "冰妍", "冰婷", "冰芬", "冰莹", "冰芳", "冰芬", "冰颖", "冰怡", "冰霞", "冰洁", "冰玉", "冰英", "冰琼", "冰冰", "冰娜", "冰洁", "冰茹", "冰怡", "冰莹", "冰梅", "冰妍", "冰洁", "冰玲", "冰玲", "冰洁", "冰倩", "冰玉", "冰倩", "冰颖", "冰芳", "冰秀", "冰雪", "冰颖", "冰艳", "冰妍", "冰玉", "冰琳", "冰洁", "冰芳", "冰梅", "冰妍", "冰艳", "冰妍", "冰洁", "冰洁", "冰茹", "冰莹", "冰妍", "冰颖", "冰艳", "冰怡", "冰雪", "冰玲", "冰花", "冰倩", "冰玲", "冰花", "冰倩", "冰茹", "冰洁", "冰芳", "冰洁", "冰莹", "冰颖", "冰悦", "冰玉", "冰倩", "冰玉", "冰燕", "冰艳", "冰洁", "冰婵", "冰丽", "冰琼", "冰芬", "冰怡", "冰倩", "冰琴", "冰洁", "冰婧", "冰瑶", "冰莹", "冰玲", "冰倩", "冰艳", "冰媛", "冰洁", "冰花", "冰倩", "冰洁", "冰倩", "冰婷", "冰妍", "冰倩", "冰洁", "冰冉", "冰文", "冰雪", "冰瑶", "冰妍", "冰莹", "冰芳", "冰芬", "冰裴", "冰艳", "冰倩", "冰艳", "冰洁", "冰芳", "冰莹", "冰霞", "冰倩", "冰雪", "冰文", "冰玉", "冰雪", "冰妍", "冰婵", "冰莉", "冰洁", "冰琳", "冰颖", "冰嫣", "冰洁", "冰妍", "冰嫣", "冰倩", "冰洁", "冰媛", "冰玉", "冰莹", "冰妍", "冰玉", "冰倩", "冰洁", "冰文", "冰雪", "冰怡", "冰玉", "冰雪", "冰洁", "冰秀", "冰琳", "冰倩", "冰莹", "冰颖", "冰雪", "冰玉", "冰芬", "冰倩", "冰茹", "冰娜", "冰艳", "冰艳", "冰梅", "冰莹", "冰洁", "冰倩", "冰洁", "冰雪", "冰雪", "冰怡", "冰瑶", "冰倩", "冰雪", "冰莹", "冰倩", "冰悦", "冰芳", "湘洁", "湘茹", "湘琴", "湘悦", "湘颖", "湘丽", "湘霞", "湘琳", "湘玉", "湘艳", "湘艳", "湘怡", "湘悦", "湘媛", "湘媛", "湘媛", "湘文", "湘媛", "湘悦", "湘丽", "湘燕", "湘琴", "湘莹", "湘婷", "湘婷", "湘婷", "湘秀", "湘婷", "湘玲", "湘怡", "湘文", "湘洁", "湘婷", "湘颖", "湘婷", "湘倩", "湘燕", "湘婷", "湘婷", "湘婷", "湘婧", "湘琼", "湘梅", "湘怡", "湘艳", "湘琼", "湘婷", "湘文", "湘茹", "湘梅", "湘雪", "湘雪", "湘琳", "湘琼", "湘茹", "湘梅", "湘婷", "湘婷", "湘雪", "湘莹", "湘艳", "湘琴", "湘玉", "湘婷", "湘玉", "湘悦", "湘悦", "湘婷", "湘丽", "湘文", "湘媛", "湘婷", "湘媛", "湘玉", "湘婷", "湘娜", "湘怡", "湘玉", "湘茹", "湘琼", "湘怡", "湘琴", "湘莹", "湘萍", "湘茹", "湘莹", "湘莹", "湘婷", "湘玉", "湘蓉", "湘芬", "湘英", "湘玉", "湘怡", "湘婷", "湘雪", "湘梅", "湘怡", "湘琼", "湘怡", "湘颖", "湘玲", "湘红", "湘红", "湘颖", "湘雪", "湘瑛", "湘悦", "湘媛", "湘英", "湘娟", "湘萍", "湘婷", "湘瑶", "湘婷", "湘琴", "湘颖", "湘丽", "湘媛", "湘婷", "湘颖", "湘红", "湘琳", "湘蓉", "湘琳", "湘茹", "湘婷", "湘洁", "湘婷", "湘艳", "湘颖", "湘萍", "湘玲", "湘茹", "湘娥", "湘玲", "湘悦", "湘萍", "湘莹", "湘媛", "湘婷", "湘燕", "湘琳", "湘玲", "湘茹", "湘蓉", "湘瑛", "湘婷", "湘娅", "湘霞", "湘瑶", "湘颖", "湘婷", "湘艳", "湘婷", "湘玉", "湘婧", "湘媛", "湘娟", "湘芳", "湘娜", "湘洁", "湘倩", "湘琳", "湘婷", "湘怡", "湘蓉", "湘颖", "湘梅", "湘琳", "湘婷", "湘蓉", "湘萍", "湘婷", "湘萍", "湘萍", "湘怡", "湘茹", "湘花", "湘妍", "湘玉", "湘婷", "湘玉", "湘蓉", "湘悦", "湘萍", "湘蓉", "湘嫣", "湘婷", "湘玲", "湘波", "湘怡", "湘倩", "湘梅", "湘美", "湘玉", "湘雪", "湘茹", "湘媛", "湘玉", "洋梅", "洋昊", "洋琴", "洋秀", "洋雪", "洋萍", "洋洁", "洋芳", "洋文", "洋红", "洋洁", "洋萍", "洋萍", "洋梅", "洋悦", "洋洁", "洋婵", "洋红", "洋玉", "洋美", "洋颖", "洋妍", "洋雪", "洋琴", "洋婧", "洋萍", "洋玉", "洋春", "洋洁", "洋婧", "洋倩", "洋文", "洋怡", "洋娜", "洋萍", "洋文", "洋洁", "洋燕", "洋芳", "洋洋", "洋瑞", "洋琳", "洋琳", "洋婷", "洋芮", "洋倩", "洋琳", "洋玉", "洋悦", "洋梅", "洋萍", "洋娜", "洋玉", "洋婷", "洋艳", "洋灏", "洋天", "洋茹", "洋琳", "洋婷", "洋怡", "洋瑛", "洋萍", "洋梓", "洋文", "洋霞", "洋萍", "洋蕊", "洋悦", "洋妞", "洋花", "洋洁", "洋锐", "洋琴", "洋茹", "洋琴", "洋丽", "洋婧", "洋婷", "洋文", "洋莹", "洋悦", "洋玉", "洋浩", "洋悦", "洋倩", "洋怡", "洋莹", "洋婷", "洋媛", "洋婷", "洋萍", "洋艳", "洋花", "洋娅", "洋瑶", "洋玉", "洋婧", "洋悦", "洋梅", "洋娟", "洋芬", "洋玲", "洋琳", "洋睿", "洋文", "洋嫣", "洋芳", "洋蓉", "洋丽", "洋娜", "洋豪", "洋梅", "洋倩", "洋悦", "洋莉", "洋冉", "洋婧", "洋怡", "洋燕", "洋文", "洋婷", "洋妍", "洋郝", "洋洁", "洋莹", "洋琳", "洋颖", "洋瑶", "洋霞", "洋妹", "洋玲", "洋美", "洋莉", "洋花", "洋萍", "洋丽", "洋悦", "洋漾", "洋颖", "洋婷", "洋玉", "洋梅", "洋琳", "洋玲", "妹员", "妹妤", "妹秀", "妹郡", "妹琪", "妹芳", "妹鸾", "妹兰", "妹仔", "妹霖", "妹仔", "妹洁", "妹孙", "妹馨", "妹娥", "妹子", "妹兰", "妹仔", "妹静", "妹娜", "妹文", "妹涵", "妹凝", "妹甬", "妹子", "妹妮", "妹利", "妹君", "妹燕", "妹雨", "妹芬", "妹那", "妹娜", "妹平", "妹如", "妹骄", "妹荣", "妹飞", "妹冰", "妹纯", "妹姨", "妹儿", "妹仪", "妹琴", "妹婷", "妹砷", "妹妍", "妹儿", "妹连", "妹俭", "妹加", "妹红", "妹黔", "妹卿", "妹璎", "妹聪", "妹衲", "妹华", "妹娴", "妹儿", "妹燕", "妹珠", "妹凤", "妹娥", "妹鹂", "妹兰", "妹蓉", "妹华", "妹淇", "妹仪", "妹乐", "妹英", "妹玫", "妹菊", "妹贤", "妹英", "妹好", "妹娘", "妹梅", "妹竹", "妹玲", "妹霞", "妹玲", "妹华", "妹萍", "妹眉", "妹英", "妹雨", "妹莲", "妹芝", "妹尔", "妹娇", "妹鹆", "妹凤", "妹娥", "妹狡", "妹伦", "妹君", "妹余", "妹兰", "妹秀", "妹好", "妹炎", "妹辉", "妹楣", "妹狗", "妹嫒", "妹沿", "妹暖", "妹老", "妹娥", "妹蓓", "毕雪", "毕琴", "毕红", "毕勇", "毕琴", "毕悦", "毕美", "毕冉", "毕莉", "毕霞", "毕芬", "毕霞", "毕丽", "毕颖", "毕红", "毕媛", "毕文", "毕文", "毕莉", "毕丽", "毕英", "毕红", "毕霞", "毕永", "毕婵", "毕泳", "毕咏", "毕婵", "毕怡", "雨琼", "雨莹", "雨颖", "雨琳", "雨娜", "雨燕", "雨妞", "雨桐", "雨婷", "雨霞", "雨婷", "雨洁", "雨媛", "雨娟", "雨娟", "雨莹", "雨婧", "雨婷", "雨嫣", "雨洁", "雨洁", "雨颖", "雨婷", "雨怡", "雨婷", "雨婷", "雨洁", "雨琳", "雨梅", "雨瑶", "雨琳", "雨妍", "雨霞", "雨娟", "雨倩", "雨婷", "雨妍", "雨洁", "雨玲", "雨燕", "雨冉", "雨悦", "雨琳", "雨艳", "雨洁", "雨洁", "雨婷", "雨妍", "雨洁", "雨倩", "雨琴", "雨妍", "雨蓉", "雨霞", "雨嫣", "雨婷", "雨琳", "雨莹", "雨英", "雨琳", "雨妍", "雨洁", "雨颖", "雨娥", "雨萍", "雨嫣", "雨婵", "雨妍", "雨莹", "雨茹", "雨燕", "雨嫣", "雨文", "雨琴", "雨婧", "雨婷", "雨婧", "雨婧", "雨文", "雨嫣", "雨雪", "雨婷", "雨瑶", "雨玲", "雨倩", "雨嫣", "雨妍", "雨琼", "雨嫣", "雨婷", "雨洁", "雨萍", "雨莉", "雨婵", "雨梅", "雨娟", "雨英", "雨莹", "雨婷", "雨英", "雨花", "雨秀", "雨琳", "雨燕", "雨嫣", "雨婷", "雨秀", "雨婷", "雨娟", "雨悦", "雨妍", "雨艳", "雨嫣", "雨英", "雨梅", "雨倩", "雨娟", "雨嫣", "雨芳", "雨洁", "雨妍", "雨婷", "雨霞", "雨媛", "雨萍", "雨婷", "雨洁", "雨嫣", "雨瑛", "雨悦", "雨琴", "雨雪", "雨滩", "雨英", "雨倩", "雨玲", "雨秀", "雨芬", "雨嫣", "雨洁", "雨婵", "雨悦", "雨霞", "雨嫣", "雨琳", "雨文", "雨萍", "雨芳", "雨燕", "雨洁", "雨文", "雨玲", "雨芬", "雨霞", "雨瑶", "雨琳", "雨燕", "雨颖", "雨婷", "雨菁", "雨涵", "雨倩", "雨蓉", "雨琼", "雨婷", "雨娟", "雨萍", "雨瑶", "雨霞", "雨艳", "雨婷", "雨怡", "雨倩", "雨嫣", "雨嫣", "雨怡", "雨梅", "雨玲", "雨娟", "雨妍", "雨莹", "雨婷", "雨芬", "雨嫣", "雨燕", "雨婷", "雨婷", "雨倩", "雨娟", "雨瑶", "雨嫣", "雨媛", "雨倩", "雨莹", "雨玲", "雨玲", "雨婷", "雨琴", "雨嫣", "佩悦", "佩婷", "佩梅", "佩玉", "佩燕", "佩怡", "佩芳", "佩英", "佩蓉", "佩英", "佩瑶", "佩琳", "佩莉", "佩琳", "佩玉", "佩琳", "佩琴", "佩莉", "佩娅", "佩娟", "佩丽", "佩文", "佩莹", "佩玉", "佩琳", "佩婷", "佩玲", "佩琳", "佩琼", "佩怡", "佩芳", "佩茹", "佩瑶", "佩玲", "佩霞", "佩文", "佩英", "佩婷", "佩英", "佩茹", "佩莉", "佩英", "佩媛", "佩芳", "佩丽", "佩琼", "佩芬", "佩娥", "佩琳", "佩芳", "佩芳", "佩莉", "佩红", "佩红", "佩瑶", "佩琳", "佩文", "佩文", "佩洁", "佩琳", "佩霞", "佩颖", "佩红", "佩玉", "佩媛", "佩瑶", "佩英", "佩燕", "佩玲", "佩瑶", "佩莉", "佩瑶", "佩妍", "佩文", "佩雪", "佩玲", "佩莹", "佩玉", "佩霞", "佩洁", "佩媛", "佩娟", "佩娟", "佩瑶", "佩颖", "佩芬", "佩玲", "佩妍", "佩莹", "佩瑶", "佩琳", "佩娜", "佩莉", "佩红", "佩婷", "佩琳", "佩玉", "佩婵", "佩琳", "佩颖", "佩娥", "佩玉", "佩芳", "佩茹", "佩娟", "佩怡", "佩茹", "佩倩", "佩琴", "佩芳", "佩琳", "佩芬", "佩怡", "佩妍", "佩玲", "佩燕", "佩茹", "佩芬", "佩英", "佩艳", "佩颖", "佩瑶", "佩怡", "佩玉", "佩莹", "佩蓉", "佩玲", "佩婷", "佩婷", "佩洁", "佩婵", "佩芬", "佩英", "佩丽", "佩文", "佩娟", "佩玲", "佩嫣", "佩雪", "佩婷", "佩琳", "佩艳", "佩玲", "佩玲", "佩蓉", "佩婷", "佩芬", "佩玉", "佩文", "佩瑶", "佩琳", "佩琴", "佩瑶", "佩妍", "佩怡", "佩瑶", "佩丽", "佩怡", "佩红", "佩玲", "佩颖", "佩莉", "佩芬", "佩婷", "佩怡", "佩洁", "佩茹", "佩文", "佩莉", "佩文", "佩玲", "佩蓉", "佩玲", "佩洁", "佩瑶", "佩琳", "佩红", "佩瑶", "佩婷", "佩颖", "佩颖", "佩瑶", "佩文", "佩琳", "佩瑶", "佩文", "佩琳", "佩娜", "佩洁", "佩玉", "佩琪", "佩丽", "佩洁", "佩颖", "佩霞", "佩瑶", "佩玲", "佩红", "佩文", "佩裴", "娥妹", "娥仙", "娥世", "娥福", "娥芳", "娥眉", "娥英", "娥佳", "娥秀", "娥如", "娥斯", "娥妹", "娥翔", "娥玉", "娥英", "娥洁", "娥娉", "娥嘉", "娥兰", "娥巧", "娥连", "娥娇", "娥辉", "娥英", "娥华", "娥蕴", "娥静", "娥妹", "娥怡", "娥媛", "娥娱", "娥娥", "娥仙", "娥冰", "娥英", "娥福", "娥晗", "娥如", "娥英", "娥娥", "娥子", "娥凤", "娥滢", "娥酏", "娥娇", "娥玉", "娥玫", "娥英", "娥娣", "娥力", "娥平", "娥茹", "娥媚", "娥婷", "娥颜", "娥小", "娥霖", "娥华", "娥凤", "娥英", "娥荣", "娥芬", "娥窕", "娥月", "娥先", "娥枝", "娥珍", "娥瑞", "娥妤", "娥芬", "娥元", "娥慧", "娥菁", "娥棋", "娥欢", "娥飞", "娥瑰", "娥骝", "娥梅", "娥娥", "娥聪", "娥强", "娥英", "娥平", "娥娈", "娥水", "娥翠", "娥芳", "娥福", "娥银", "娥湖", "娥仟", "娥瑗", "娥滇", "娥嘉", "娥秀", "娥仙", "娥露", "娥梅", "娥惠", "娥菲", "娥香", "娥淬", "娥翔", "娥资", "甫文", "甫文", "甫妍", "甫文", "甫玲", "甫英", "甫雪", "甫文", "甫斌", "甫文", "甫文", "甫娟", "甫红", "甫彬", "甫文", "甫琼", "甫玉", "甫芳", "甫文", "甫文", "甫秀", "汝琳", "汝芬", "汝颖", "汝芳", "汝雪", "汝玲", "汝秀", "汝秀", "汝剀", "汝洁", "汝瑶", "汝梅", "汝玉", "汝玉", "汝怡", "汝玉", "汝琳", "汝萍", "汝茹", "汝琴", "汝洁", "汝玉", "汝婷", "汝美", "汝茹", "汝琳", "汝琳", "汝英", "汝芳", "汝艳", "汝丽", "汝霞", "汝洁", "汝文", "汝玉", "汝婷", "汝洁", "汝雪", "汝琳", "汝芬", "汝洁", "汝莉", "汝玉", "汝玉", "汝蓉", "汝霞", "汝洁", "汝萍", "汝婷", "汝媛", "汝琼", "汝婷", "汝洁", "汝文", "汝英", "汝怡", "汝文", "汝萍", "汝玉", "汝婷", "汝瑶", "汝琴", "汝颖", "汝婵", "汝妍", "汝娟", "汝瑶", "汝萍", "汝芬", "汝婷", "汝悦", "汝琳", "汝英", "汝艳", "汝红", "汝燕", "汝红", "汝花", "汝秀", "汝瑶", "汝婷", "汝文", "汝琼", "汝玉", "汝垲", "汝蓉", "汝婷", "汝恺", "汝芬", "汝芳", "汝洁", "汝玉", "汝雪", "汝妍", "汝玉", "汝瑛", "汝萍", "汝莹", "汝梅", "汝萍", "汝瑶", "汝瑛", "汝文", "汝秀", "汝琳", "汝琳", "汝玉", "汝红", "汝洁", "汝玲", "汝英", "汝娜", "汝婷", "汝霞", "汝萍", "汝妍", "汝婷", "汝雪", "汝倩", "汝玲", "汝玉", "汝燕", "汝洁", "汝芳", "汝妍", "汝倩", "汝萍", "汝怡", "汝文", "汝芬", "汝英", "汝英", "汝文", "汝悦", "汝婷", "汝悦", "汝玉", "汝英", "汝雪", "汝英", "汝琳", "汝瑛", "汝莹", "汝红", "汝洁", "汝婧", "汝霞", "汝艳", "汝芬", "汝玉", "汝红", "汝芳", "汝婷", "汝艳", "汝秀", "汝裴", "汝艳", "汝萍", "汝雪", "汝瑛", "汝娜", "汝燕", "汝婷", "汝悦", "汝颖", "汝雪", "汝楷", "汝琳", "汝萍", "汝芳", "汝英", "汝颖", "汝婷", "汝梅", "汝嫣", "汝霞", "汝琼", "汝芳", "汝琼", "汝红", "汝洁", "汝怡", "汝花", "汝瑶", "汝婷", "汝娟", "汝琳", "汝洁", "汝琴", "汝妹", "汝燕", "汝洁", "汝红", "汝雪", "汝英", "汝丽", "汝燕", "汝婧", "汝萍", "汝洁", "饫平", "恺琪", "怿东", "怛瀚", "饴海", "怿慎", "恺琦", "庥隆", "忪韬", "怿琨", "猗轩", "恺槊", "怿峰", "恺潞", "饴逸", "膺正", "恺凝", "恺韵", "恺扬", "恺宜", "怿航", "恺芸", "怅兰", "忡希", "怿浩", "忤盈", "庾锐", "怿哲", "怿江", "恺洌", "恺天", "怿浦", "怩欣", "恺佳", "恺青", "忏灵", "恺颢", "忪铃", "恺晟", "恺翎", "忏忏", "赓炀", "怿锐", "恺泽", "恺豪", "恺闻", "怿铭", "恺明", "怿盛", "庾诚", "恺箫", "恺煜", "恺微", "恺叶", "膺任", "恺支", "恺谛", "忏倪", "馑菘", "恺涵", "恺诺", "庠菊", "怿琪", "恺圣", "忾铭", "怿鑫", "夤瀑", "恺行", "恺海", "恺航", "怿伟", "恺海", "恺漾", "怿彬", "怅次", "恺诚", "恺熙", "恺雯", "恺栎", "忾岩", "怿辰", "恺儒", "恺芮", "饪萌", "恂津", "恺洋", "膺洙", "恺悌", "恺仪", "怆爨", "怿含", "怿颢", "馑厦", "恺丞", "忸鸿", "恺廷", "怿飞", "恺轩", "恺彤", "恺沁", "恺雯", "怿芸", "恺丰", "忮垲", "怿非", "忪俞", "忪棂", "猥亿", "怿唐", "怿漩", "怿昀", "恺悌", "恺信", "恺瑜", "忉鸾", "怿清", "馍自", "恺擎", "赓泽", "怿亮", "忏恩", "恺乐", "恺成", "恺茵", "怿海", "庾轹", "饴润", "恺疑", "恺晟", "庾成", "忏艺", "恺晨", "忤爽", "恺的", "怿铎", "恺薇", "怿泽", "恺稷", "恺盈", "恺宇", "恺茵", "恺汶", "怿华", "恺海", "恺勤", "恺锐", "忾浩", "恺源", "恺汶", "忡衍", "恺恩", "怿羚", "庾轩", "恺心", "恺闻", "怿旗", "廪俊", "恺苒", "馀鲽", "霆玉", "霆婷", "霆英", "霆英", "霆悦", "霆悦", "霆玉", "霆倩", "霆燕", "霆芳", "霆颖", "霆婷", "霆玉", "霆玉", "霆婷", "霆玉", "霆婷", "霆玉", "霆文", "盼怡", "盼怡", "盼婷", "盼红", "盼颖", "盼红", "盼玲", "盼倩", "盼瑶", "盼莹", "盼瑶", "盼怡", "盼娜", "盼娜", "盼倩", "盼雪", "盼妍", "盼霞", "盼莹", "盼悦", "盼琳", "盼红", "盼悦", "盼玉", "盼琳", "盼琳", "盼丽", "盼悦", "盼悦", "盼雪", "盼霞", "盼冉", "盼莹", "盼玉", "盼媛", "盼红", "盼文", "盼文", "盼颖", "盼琳", "瀛婷", "瀛文", "瀛霞", "瀛秀", "瀛莹", "瀛萍", "瀛文", "瀛茹", "瀛莹", "瀛文", "瀛芳", "瀛霞", "瀛婷", "瀛文", "瀛莹", "瀛莹", "瀛文", "瀛悦", "瀛莹", "瀛琳", "瀛芳", "瀛雪", "默妍", "默丽", "默冉", "默婷", "默娜", "默妍", "默玉", "默文", "默冉", "默蓉", "默文", "默冉", "默文", "默茹", "默妍", "默萍", "默冉", "默茹", "默琳", "默丽", "默冉", "默瑶", "默文", "默文", "默颖", "默琳", "默妍", "默怡", "默茹", "默文", "默文", "默瑶", "默悦", "默冉", "默琼", "默妍", "默琳", "默瑶", "默婷", "默燕", "默颖", "默怡", "默妍", "默妍", "默妍", "默颖", "默嫣", "默霞", "沅媛", "沅婷", "沅媛", "沅丽", "沅丽", "沅莉", "沅洁", "沅霞", "沅媛", "沅媛", "沅洁", "沅婷", "沅莹", "沅婧", "沅雪", "沅婷", "沅茹", "沅媛", "沅媛", "沅洁", "沅英", "沅琼", "沅洁", "沅瑶", "沅媛", "沅莹", "沅洁", "沅媛", "沅婷", "沅媛", "沅怡", "沅燕", "沅丽", "沅文", "沅琳", "沅妍", "沅婧", "沅婷", "沅琳", "沅蓉", "沅洁", "沅媛", "沅萍", "沅倩", "沅怡", "沅文", "沅蓉", "沅萍", "沅艳", "沅玲", "沅婷", "沅娜", "沅芳", "沅娜", "沅萍", "沅芳", "沅萍", "沅莉", "沅婷", "沅秀", "沅莹", "沅芳", "沅芳", "沅倩", "沅茹", "沅莹", "沅颖", "沅怡", "沅玲", "沅颖", "沅洁", "沅悦", "沅淇", "沅洁", "沅颖", "沅文", "沅玲", "沅怡", "沅艳", "沅妍", "沅悦", "沅玉", "沅琳", "沅琳", "沅蓉", "沅芳", "沅蓉", "沅婷", "沅倩", "沅芳", "沅芳", "沅玲", "沅媛", "沅娟", "沅萍", "沅红", "沅玉", "沅媛", "沅莉", "沅娅", "赋文", "赋媛", "赋文", "赋文", "赋瑶", "赋文", "赋琼", "赋颖", "赋婷", "赋燕", "赋怡", "赋蓉", "赋洁", "赋文", "赋蓉", "赋文", "赋艳", "赋莹", "赋妍", "赋茹", "赋玉", "赋梅", "珩瑛", "珩洁", "珩婷", "珩冉", "珩文", "珩婧", "珩玉", "珩玉", "珩艳", "珩嫣", "珩涛", "珩颖", "珩秀", "珩嫣", "珩婧", "珩雪", "珩颖", "珩艳", "珩芳", "珩文", "珩媛", "珩珩", "珩婷", "珩艳", "珩嫣", "珩玉", "珩文", "珩艳", "珩文", "珩婷", "珩玉", "珩玉", "珩婉", "珩倩", "潇瑶", "潇冉", "潇颖", "潇冉", "潇茹", "潇洁", "潇艳", "潇玲", "潇莉", "潇文", "潇雪", "潇怡", "潇文", "潇玲", "潇颖", "潇雪", "潇婧", "潇倩", "潇琳", "潇雪", "潇茹", "潇雪", "潇怡", "潇莹", "潇琳", "潇文", "潇婷", "潇琴", "潇芳", "潇瑛", "潇倩", "潇艳", "潇玉", "潇芳", "潇茹", "潇婷", "潇莹", "潇冉", "潇婷", "潇燕", "潇冉", "潇颖", "潇颖", "潇文", "潇芬", "潇莹", "潇霞", "潇莉", "潇文", "潇秀", "潇茹", "潇颖", "潇雪", "潇燕", "潇燕", "潇妹", "潇文", "潇文", "潇琳", "潇婷", "潇冉", "潇妞", "潇娜", "潇玉", "潇芬", "潇悦", "潇萍", "潇莉", "潇莹", "潇丽", "潇颖", "潇莉", "潇琳", "潇颖", "潇洁", "潇颖", "潇瑶", "潇婷", "潇洁", "潇雪", "潇婧", "潇冉", "潇悦", "潇瑶", "潇婷", "潇文", "潇悦", "潇怡", "潇文", "潇怡", "潇娜", "潇玉", "潇颖", "潇洁", "潇洁", "潇莹", "潇文", "潇悦", "潇媛", "潇艳", "潇茹", "潇玲", "潇颖", "潇文", "潇雪", "潇文", "潇丽", "潇艳", "潇媛", "潇冉", "潇娜", "潇悦", "潇萍", "潇芬", "潇媛", "潇怡", "潇冉", "潇文", "潇颖", "潇雪", "潇洁", "潇艳", "潇茹", "潇萍", "潇丽", "潇莹", "潇英", "潇媛", "潇洁", "潇瑶", "潇洁", "潇怡", "潇怡", "潇琼", "潇颖", "潇怡", "潇婷", "潇梅", "潇莉", "潇艳", "潇妍", "潇怡", "潇瑶", "潇芳", "潇秀", "潇娜", "潇芳", "潇文", "潇婷", "潇婧", "潇倩", "潇玉", "潇琼", "潇洁", "潇文", "潇芳", "潇文", "潇怡", "潇娜", "潇萍", "潇莹", "潇颖", "潇娅", "潇琳", "潇妍", "潇冉", "潇雪", "潇萍", "潇文", "潇婷", "潇雪", "潇洁", "潇媛", "潇莹", "潇文", "潇琳", "潇婷", "潇文", "潇洁", "潇霞", "潇婷", "潇娜", "潇洁", "潇燕", "潇颖", "潇莹", "潇婷", "潇怡", "潇艳", "潇瑶", "潇霞", "潇玲", "潇怡", "潇颖", "潇雪", "潇琴", "潇洁", "潇涵", "潇雪", "纬军", "嘻熙", "维福", "文涵", "文轩", "熙然", "文霆", "唯辰", "西宙", "文娴", "文璨", "文沛", "闻哲", "卫军", "文君", "维山", "文旭", "文清", "伟祥", "文雄", "文海", "伟甫", "唯译", "熙儒", "卫华", "伟禺", "文君", "闻陶", "伟龙", "文海", "伟伟", "伟联", "文金", "舞眩", "文海", "文浩", "伟雄", "闻涛", "文力", "文君", "文傲", "文琦", "文亮", "维梁", "文宏", "文嘉", "魏禹", "文铖", "为清", "伟杰", "伟侨", "文皆", "熙城", "伟辰", "熙焕", "文锦", "伟伟", "析曦", "文婕", "熙施", "未轩", "西纯", "无咎", "伟添", "武漱", "文超", "文睿", "卫林", "武军", "文标", "熙溪", "熙炅", "文东", "文昌", "维宝", "西睿", "文皓", "文杰", "文茜", "熙璇", "文兵", "文明", "委刚", "卫理", "文刚", "文贵", "熙鬓", "闻生", "文睿", "文俊", "伟东", "文敏", "伟欢", "伟华", "伟鸿", "文彤", "文锋", "伟元", "维拓", "昔函", "锡杉", "文斐", "文举", "胃康", "文哲", "文妲", "文兵", "文芸", "闻亚", "稳程", "文琦", "伟根", "为仙", "文萧", "熙迩", "卫权", "锡波", "文浩", "未央", "闻楠", "蔚岚", "文铭", "熙轩", "文祥", "卫宏", "文舟", "伟鹏", "伟明", "伟伟", "务平", "伟烨", "文明", "卫华", "魏朋", "未昀", "卫达", "维楷", "卫军", "务业", "文枢", "位峰", "文田", "文生", "维秋", "文越", "武河", "熙淳", "韦弘", "维乐", "文龙", "熙瑷", "武隆", "文泰", "伟苹", "文康", "蔚博", "文平", "卫明", "文礼", "文勇", "紊军", "熙睿", "文跞", "文利", "魏瓶", "戊娣", "文清", "维淇", "闻熹", "武裕", "韦鹏", "熙杰", "卫星", "郗怡", "郗颖", "郗茹", "郗瑶", "郗怡", "郗娜", "郗文", "郗文", "郗倩", "郗嫣", "郗媛", "郗莹", "郗娅", "郗瑶", "郗媛", "郗娅", "郗莹", "盟洁", "盟洁", "盟怡", "盟美", "盟玉", "盟媛", "盟文", "盟杨", "盟娟", "盟婷", "盟英", "盟瑶", "盟茹", "盟琳", "盟婷", "盟洁", "盟娜", "盟美", "盟怡", "盟琳", "盟娜", "盟梅", "盟冉", "盟文", "妃嫣", "妃玉", "妃娜", "妃婷", "妃莹", "妃艳", "妃娜", "妃妍", "妃燕", "妃婵", "妃萍", "妃悦", "妃花", "妃琴", "妃媛", "妃玉", "妃冉", "妃嫣", "妃茹", "妃娜", "妃琳", "妃悦", "妃悦", "妃倩", "妃玲", "妃丽", "妃丽", "妃娟", "妃悦", "妃妍", "妃雪", "妃颖", "妃颖", "妃雪", "妃婷", "妃娥", "妃瑶", "妃倩", "妃娜", "妃英", "妃艳", "妃娟", "妃怡", "妃茹", "妃莹", "妃玉", "妃悦", "妃燕", "妃妍", "妃萍", "妃媛", "妃婷", "妃婷", "妃怡", "妃婷", "妃娜", "妃茹", "妃艳", "妃雪", "妃妍", "妃红", "妃雪", "妃琳", "妃嫣", "妃玉", "妃娜", "妃娜", "妃娅", "妃蓉", "妃萍", "妃悦", "妃英", "妃雪", "妃瑶", "妃雪", "妃婷", "妃婷", "妃妍", "妃怡", "妃莉", "妃琼", "妃雪", "妃艳", "妃洁", "妃悦", "妃雪", "妃梅", "妃丽", "妃艳", "妃妍", "妃花", "妃茹", "妃燕", "妃婷", "妃娅", "妃艳", "妃妍", "妃琳", "妃妍", "妃嫣", "妃雪", "妃燕", "妃倩", "妃萍", "妃妍", "妃秀", "妃婵", "妃雪", "妃倩", "妃红", "妃芳", "妃婷", "妃怡", "妃倩", "妃雪", "妃娟", "妃冉", "漫妍", "漫雪", "漫怡", "漫燕", "漫莉", "漫琳", "漫莉", "漫婷", "漫茹", "漫秀", "漫丽", "漫琳", "漫红", "漫丽", "漫怡", "漫琳", "漫婷", "漫蓉", "漫洁", "漫婷", "漫婷", "漫怡", "漫莉", "漫怡", "漫婧", "漫琳", "漫莉", "漫瑛", "漫雪", "漫花", "漫莉", "漫洁", "漫茹", "漫蓉", "漫婷", "漫霞", "漫莉", "漫丽", "漫怡", "漫秀", "漫婷", "漫玉", "漫婧", "漫莉", "漫玲", "漫红", "漫婷", "漫婷", "漫颖", "漫雪", "漫丽", "漫雪", "漫霞", "漫倩", "漫玉", "漫英", "漫怡", "漫颖", "漫玲", "漫华", "漫丽", "漫雪", "漫婷", "漫茹", "漫琳", "漫玉", "漫琳", "漫丽", "漫莹", "漫娜", "漫婷", "漫瑶", "漫洁", "漫婷", "漫红", "漫洁", "漫琴", "漫婷", "漫琳", "漫玉", "漫芳", "漫琳", "漫莉", "漫琳", "漫婷", "漫婷", "漫婷", "漫莉", "漫蓉", "漫琳", "漫琳", "漫莉", "漫丽", "漫丽", "漫琳", "漫玲", "漫琳", "漫丽", "漫莉", "漫婷", "漫琳", "漫洁", "漫玉", "漫霞", "漫颖", "漫霞", "漫莹", "漫莉", "漫琳", "漫婷", "漫琳", "漫雪", "漫琳", "漫莉", "漫文", "漫琳", "漫婷", "漫婷", "漫媛", "漫英", "漫茹", "漫琳", "漫娜", "漫雪", "漫玲", "漫婷", "漫颖", "漫霞", "漫琳", "漫洁", "漫婷", "漫玉", "漫嫣", "漫颖", "漫琳", "漫莹", "漫莹", "漫丽", "漫玲", "漫娟", "漫婷", "漫洁", "漫玲", "漫娜", "漫蓉", "漫婷", "漫妍", "漫玉", "漫雪", "漫丽", "漫玉", "漫娜", "漫莉", "漫红", "漫萍", "漫瑶", "漫丽", "漫芳", "漫琳", "漫颖", "漫萍", "漫丽", "漫娜", "漫艳", "漫莉", "漫婷", "漫雪", "漫玉", "漫莉", "漫婷", "漫萍", "漫婷", "公冶", "伯赏", "轩辕", "长孙", "司马", "鲜于", "欧阳", "司空", "单于", "夏侯", "上官", "皇甫", "南宫", "诸葛", "巫马", "阳佟", "太叔", "东方", "尉迟", "呼延", "慕容", "宇文", "淳于", "子车", "闾丘", "东郭", "归海", "赫连", "司空", "乐正", "濮阳", "西门", "百里", "司徒", "令狐", "左丘", "公西", "谷粱", "拓跋", "名字男", "之玉", "越泽", "锦程", "修杰", "烨伟", "尔曼", "立辉", "致远", "天思", "友绿", "聪健", "修洁", "访琴", "初彤", "谷雪", "平灵", "源智", "烨华", "振家", "越彬", "乞", "子轩", "伟宸", "晋鹏", "觅松", "海亦", "戾", "嵩", "邑", "瑛", "鸿", "卿", "裘", "契", "涛", "疾", "驳", "凛", "逊", "鹰", "威", "紊", "阁", "康", "焱", "城", "誉", "祥", "虔", "胜", "穆", "豁", "匪", "霆", "凡", "枫", "豪", "铭", "罡", "扬", "垣", "师", "翼", "秋", "傥", "雨珍", "浩宇", "嘉熙", "志泽", "苑博", "念波", "峻熙", "俊驰", "聪展", "南松", "问旋", "黎昕", "谷波", "凝海", "靖易", "芷烟", "渊思", "煜祺", "乐驹", "风华", "箴", "睿渊", "博超", "天磊", "夜白", "初晴", "雍", "达", "乾", "鑫", "萧", "鲂", "冥", "翰", "丑", "隶", "钧", "坤", "荆", "蹇", "骁", "沅", "剑", "勒", "筮", "磬", "戎", "翎", "函", "嚣", "炳", "耷", "惮", "鞯", "擎", "烙", "靖", "遥", "斩", "颤", "孱", "续", "岩", "奄", "秋白", "瑾瑜", "鹏飞", "弘文", "伟泽", "迎松", "雨泽", "鹏笑", "诗云", "白易", "远航", "笑白", "映 波", "代桃", "晓啸", "智宸", "晓博", "靖琪", "十八", "君浩", "绍辉", "冷安", "盼旋", "博", "鹤", "绯", "匕", "奎", "仰", "霸", "乌", "邴", "败", "捕", "糜", "汲", "涔", "班", "悲", "臻", "厉", "栾", "井", "伊", "储", "羿", "富", "稀", "松", "寇", "碧", "珩", "靳", "鞅", "弼", "焦", "天德", "铁身", "老黑", "半邪", "半山", "一江", "冰安", "皓轩", "子默", "熠彤", "青寒", "烨磊", "愚志", "飞风", "问筠", "旭尧", "妙海", "平文", "冷之", "尔阳", "天宇", "正豪", "文博", "明辉", "行恶", "哲瀚", "子骞", "泽洋", "灵竹", "幼旋", "百招", "不斜", "擎汉", "千万", "高烽", "大开", "不正", "伟帮", "如豹", "三德", "三毒", "连虎", "十三", "酬海", "天川", "一德", "复天", "牛青", "羊青", "大楚", "傀斗", "老五", "老九", "定帮", "自中", "开山", "似狮", "无声", "一手", "严青", "老四", "不可", "随阴", "大有", "中恶", "延恶", "百川", "世倌", "连碧", "岱周", "擎苍", "思远", "嘉懿", "鸿煊", "笑天", "晟睿", "强炫", "寄灵", "听白", "鸿涛", "孤风", "青文", "盼秋", "怜烟", "浩然", "明杰", "昊焱", "伟诚", "剑通", "鹏涛", "鑫磊", "醉薇", "尔蓝", "靖仇", "成风", "豪英", "若风", "难破", "德地", "无施", "追命", "成协", "人达", "亿先", "不评", "成威", "成败", "难胜", "人英", "忘幽", "世德", "世平", "广山", "德天", "人雄", "人杰", "不言", "难摧", "世立", "老三", "若之", "成危", "元龙", "成仁", "若剑", "难敌", "浩阑", "士晋", "铸海", "人龙", "伯云", "老头", "南风", "擎宇", "浩轩", "煜城", "博涛", "问安", "烨霖", "天佑", "明雪", "书芹", "半雪", "伟祺", "从安", "寻菡", "秋寒", "谷槐", "文轩", "立诚", "立果", "明轩", "楷瑞", "炎彬", "鹏煊", "幼南", "沛山", "不尤", "道天", "剑愁", "千筹", "广缘", "天奇", "道罡", "远望", "乘风", "剑心", "道之", "乘云", "绝施", "冥幽", "天抒", "剑成", "士萧", "文龙", "一鸣", "剑鬼", "半仙", "万言", "剑封", "远锋", "天与", "元正", "世开", "不凡", "断缘", "中道", "绝悟", "道消", "断秋", "远山", "无招", "无极", "鬼神", "满天", "飞扬", "醉山", "语堂", "懿轩", "雅阳", "鑫鹏", "文昊", "松思", "水云", "山柳", "荣轩", "绮彤", "沛白", "慕蕊", "觅云", "鹭洋", "立轩", "金鑫", "健柏", "建辉", "鹤轩", "昊强", "凡梦", "代丝", "远侵", "一斩", "一笑", "一刀", "行天", "无血", "无剑", "无敌", "万怨", "万天", "万声", "万恶", "万仇", "天问", "天寿", "送终", "山河", "三问", "如花", "灭龙", "聋五", "绝义", "绝山", "剑身", "浩天", "非笑", "恶天", "断天", "仇血", "仇天", "沧海", "不二", "碧空", "半鬼", "海", "文涛", "刚", "纲", "晓刚", "洪纲", "砖家", "叫兽", "囧", "名字女", "醉易", "紫萱", "紫霜", "紫南", "紫菱", "紫蓝", "紫翠", "紫安", "姿", "芷天", "芷容", "芷巧", "芷卉", "芷荷", "芷", "芝", "之桃", "筝", "真", "珍", "贞", "元霜", "元绿", "元槐", "元枫", "语雪", "语山", "语蓉", "语琴", "语海", "语芙", "语儿", "语蝶", "雨雪", "雨文", "雨梅", "雨莲", "雨兰", "幼丝", "幼枫", "又菡", "友梅", "友儿", "映萱", "映安", "迎梦", "迎波", "婴", "易巧", "亦丝", "亦巧", "忆雪", "忆文", "忆梅", "忆枫", "以丹", "依丝", "夜玉", "夜梦", "夜春", "雁荷", "雁风", "雅彤", "雅琴", "寻梅", "寻冬", "雪珍", "雪瑶", "雪旋", "雪卉", "秀", "笑旋", "笑蓝", "笑翠", "晓亦", "晓夏", "向梦", "香萱", "香岚", "夏真", "夏山", "夏兰", "惜雪", "惜蕊", "惜灵", "问夏", "问蕊", "问梅", "雯", "纹", "菀", "莞", "宛", "桐", "彤", "听筠", "听枫", "天曼", "愫", "素", "涑", "思松", "思菱", "水瑶", "水彤", "姝", "书竹", "书易", "诗桃", "诗双", "诗珊", "诗蕊", "山菡", "山蝶", "弱", "若雁", "若菱", "若", "如风", "如冬", "如波", "蓉", "秋柔", "清", "青雪", "青曼", "青", "巧蕊", "千亦", "千柔", "千柳", "绮琴", "绮梅", "莆", "萍", "平萱", "平露", "颦", "沛儿", "盼烟", "凝雁", "凝安", "念之", "念柏", "茗", "敏", "妙之", "妙梦", "妙柏", "娩", "梦之", "梦桃", "梦琪", "梦露", "梦凡", "曼容", "曼荷", "曼寒", "曼安", "绿真", "凌文", "凌青", "凌波", "怜阳", "怜珊", "冷雪", "冷荷", "乐萱", "乐天", "乐松", "乐枫", "斓", "澜", "蓝", "兰", "静芙", "靖柏", "寄真", "寄文", "寄琴", "惠", "荟", "幻天", "幻珊", "寒天", "寒凝", "寒梦", "寒荷", "涵易", "涵菱", "含玉", "含烟", "含灵", "含蕾", "海云", "海冬", "涫", "谷蕊", "谷兰", "飞珍", "飞槐", "访云", "访烟", "访天", "访风", "凡阳", "凡旋", "凡梅", "凡灵", "凡蕾", "尔丝", "尔柳", "尔芙", "尔白", "孤菱", "沛萍", "梦柏", "从阳", "绿海", "白梅", "秋烟", "访旋", "元珊", "凌旋", "依珊", "寻凝", "幻柏", "雨寒", "寒安", "芙", "怀绿", "书琴", "水香", "向彤", "曼冬", "璎", "姒", "苠", "淇", "绮", "怜梦", "安珊", "映阳", "思天", "初珍", "冷珍", "海安", "从彤", "灵珊", "夏彤", "映菡", "青筠", "易真", "幼荷", "冷霜", "凝旋", "夜柳", "紫文", "凡桃", "醉蝶", "从云", "冰萍", "小萱", "白筠", "依云", "元柏", "丹烟", "雁", "念云", "易蓉", "青易", "友卉", "若山", "涵柳", "映菱", "依凝", "怜南", "水儿", "从筠", "千秋", "代芙", "之卉", "幻丝", "书瑶", "含之", "雪珊", "海之", "寄云", "盼海", "谷梦", "襄", "雁兰", "晓灵", "向珊", "宛筠", "笑南", "梦容", "寄柔", "静枫", "尔容", "沛蓝", "宛海", "迎彤", "梦易", "惜海", "灵阳", "念寒", "紫", "芯", "沂", "衣", "荠", "莺", "萤", "采梦", "夜绿", "又亦", "怡", "苡", "悒", "梦山", "醉波", "慕晴", "安彤", "荧", "半烟", "翠桃", "书蝶", "寻云", "冰绿", "山雁", "南莲", "夜梅", "翠阳", "芷文", "茈", "南露", "向真", "又晴", "香", "又蓝", "绫", "灵", "雅旋", "千儿", "玲", "听安", "凌蝶", "向露", "从凝", "雨双", "依白", "樱", "颜", "以筠", "含巧", "艳", "晓瑶", "忆山", "以莲", "冰海", "盼芙", "冰珍", "颖", "盈", "半双", "以冬", "千凝", "琦", "笑阳", "香菱", "友蕊", "若云", "天晴", "笑珊", "凡霜", "南珍", "晓霜", "芷云", "谷芹", "芷蝶", "雨柏", "之云", "靖巧", "寄翠", "涵菡", "雁卉", "涵山", "念薇", "忻", "芸", "笙", "芳", "绮兰", "迎蕾", "秋荷", "代天", "采波", "丝", "诗兰", "谷丝", "凝琴", "凝芙", "尔风", "觅双", "忆灵", "水蓝", "书蕾", "访枫", "涵双", "初阳", "从梦", "凝天", "秋灵", "湘", "笑槐", "灵凡", "冰夏", "听露", "翠容", "绮晴", "静柏", "天亦", "冷玉", "以亦", "盼曼", "乐蕊", "凡柔", "曼凝", "沛柔", "迎蓉", "映真", "采文", "曼文", "新筠", "碧玉", "秋柳", "白莲", "亦玉", "幻波", "忆之", "孤丝", "妙竹", "傲柏", "元风", "易烟", "怀蕊", "萃", "寻桃", "映之", "小玉", "尔槐", "翠", "萝", "听荷", "赛君", "闭月", "不愁", "羞花", "紫寒", "夏之", "飞薇", "如松", "白安", "秋翠", "夜蓉", "傲晴", "凝丹", "凌瑶", "初曼", "夜安", "安荷", "青柏", "向松", "绿旋", "芷珍", "凌晴", "新儿", "亦绿", "雁丝", "惜霜", "紫青", "冰双", "映冬", "代萱", "梦旋", "毒娘", "紫萍", "冰真", "幻翠", "向秋", "海蓝", "凌兰", "如柏", "千山", "半凡", "雁芙", "白秋", "平松", "代梅", "香之", "梦寒", "小蕊", "慕卉", "映梦", "绿蝶", "芹", "凌翠", "夜蕾", "含双", "慕灵", "碧琴", "夏旋", "冷雁", "乐双", "念梦", "静丹", "之柔", "新瑶", "亦旋", "雪巧", "中蓝", "莹芝", "一兰", "清涟", "盛男", "竺", "洙", "凝莲", "雪莲", "依琴", "绣连", "友灵", "醉柳", "秋双", "珠", "绮波", "寄瑶", "冰蝶", "孤丹", "半梅", "友菱", "飞双", "醉冬", "寡妇", "沛容", "南晴", "太兰", "紫易", "从蓉", "友易", "衫", "尔竹", "莛", "琳", "巧荷", "寻双", "珊", "芷雪", "又夏", "梦玉", "安梦", "凝荷", "凤", "外绣", "忆曼", "不平", "凝蝶", "以寒", "安南", "思山", "嫣", "芫", "若翠", "曼青", "小珍", "青荷", "代容", "孤云", "慕青", "寄凡", "元容", "丹琴", "寒珊", "飞雪", "妙芙", "碧凡", "思柔", "雁桃", "丹南", "雁菡", "翠丝", "幻梅", "海莲", "宛秋", "问枫", "靖雁", "蛟凤", "大凄", "金连", "梦安", "碧曼", "代珊", "惜珊", "元冬", "葶", "芮", "青梦", "书南", "绮山", "白桃", "从波", "访冬", "含卉", "平蝶", "海秋", "沛珊", "沁", "飞兰", "凝云", "亦竹", "梦岚", "寒凡", "傲柔", "凌丝", "觅风", "平彤", "念露", "翠彤", "秋玲", "安蕾", "若蕊", "灵萱", "含雁", "思真", "盼山", "香薇", "碧萱", "夏柳", "白风", "安双", "凌萱", "盼夏", "幻巧", "怜寒", "傲儿", "冰枫", "如萱", "妖丽", "元芹", "涵阳", "涵蕾", "以旋", "高丽", "灭男", "代玉", "可仁", "可兰", "可愁", "可燕", "妙彤", "易槐", "小凝", "妙晴", "冰薇", "涵柏", "语兰", "小蕾", "忆翠", "听云", "觅海", "静竹", "初蓝", "迎丝", "幻香", "含芙", "夏波", "冰香", "凌香", "妙菱", "访彤", "凡雁", "紫真", "书双", "问晴", "惜萱", "白萱", "靖柔", "凡白", "晓曼", "曼岚", "雁菱", "雨安", "谷菱", "夏烟", "问儿", "青亦", "夏槐", "含蕊", "迎南", "又琴", "冷松", "安雁", "飞荷", "踏歌", "秋莲", "盼波", "以蕊", "盼兰", "之槐", "飞柏", "孤容", "白玉", "傲南", "山芙", "夏青", "雁山", "曼梅", "如霜", "沛芹", "丹萱", "翠霜", "玉兰", "汝燕", "不乐", "不悔", "可冥", "若男", "素阴", "元彤", "从丹", "曼彤", "惋庭", "起眸", "香芦", "绿竹", "雨真", "乐巧", "亚男", "小之", "如曼", "山槐", "谷蓝", "笑容", "香露", "白薇", "凝丝", "雨筠", "秋尽", "婷冉", "冰凡", "亦云", "芙蓉", "天蓝", "沉鱼", "东蒽", "飞丹", "涵瑶", "雁开", "以松", "南烟", "傲霜", "香旋", "觅荷", "幼珊", "无色", "凤灵", "新竹", "半莲", "媚颜", "紫雪", "寒香", "幼晴", "宛菡", "采珊", "凝蕊", "无颜", "莫言", "初兰", "冷菱", "妙旋", "梨愁", "友琴", "水蓉", "尔岚", "怜蕾", "怀蕾", "惜天", "谷南", "雪兰", "语柳", "夏菡", "巧凡", "映雁", "之双", "梦芝", "傲白", "觅翠", "如凡", "傲蕾", "傲旋", "以柳", "从寒", "双双", "无春", "紫烟", "飞凤", "紫丝", "思卉", "初雪", "向薇", "落雁", "凡英", "海菡", "白晴", "映天", "静白", "雨旋", "安卉", "依柔", "半兰", "灵雁", "雅蕊", "初丹", "寒云", "念烟", "代男", "笑卉", "曼云", "飞莲", "幻竹", "晓绿", "寄容", "小翠", "小霜", "语薇", "芷蕾", "谷冬", "血茗", "天荷", "问丝", "沛凝", "翠绿", "寒松", "思烟", "雅寒", "以南", "碧蓉", "绮南", "白凡", "安莲", "访卉", "元瑶", "水风", "凡松", "友容", "访蕊", "若南", "涵雁", "雪一", "怀寒", "幻莲", "碧菡", "绿蕊", "如雪", "珊珊", "念珍", "莫英", "朝雪", "茹嫣", "老太", "曼易", "宛亦", "映寒", "谷秋", "诗槐", "如之", "水桃", "又菱", "迎夏", "幻灵", "初夏", "晓槐", "代柔", "忆安", "迎梅", "夜云", "傲安", "雨琴", "听芹", "依玉", "冬寒", "绿柏", "梦秋", "千青", "念桃", "苑睐", "夏蓉", "诗蕾", "友安", "寻菱", "绮烟", "若枫", "凝竹", "听莲", "依波", "飞松", "依秋", "绿柳", "元菱", "念芹", "如彤", "香彤", "涵梅", "映容", "平安", "赛凤", "书桃", "梦松", "以云", "映易", "小夏", "元灵", "天真", "晓蕾", "问玉", "问薇", "笑晴", "亦瑶", "半芹", "幼萱", "凡双", "夜香", "阑香", "阑悦", "溪灵", "冥茗", "丹妗", "妙芹", "飞飞", "觅山", "沛槐", "太英", "惋清", "太清", "灵安", "觅珍", "依风", "若颜", "觅露", "问柳", "以晴", "山灵", "晓兰", "梦菡", "思萱", "半蕾", "紫伊", "山兰", "初翠", "岂愈", "海雪", "向雁", "冬亦", "柏柳", "青枫", "宝莹", "宝川", "若灵", "冷梅", "艳一", "梦槐", "依霜", "凡之", "忆彤", "英姑", "清炎", "绮露", "醉卉", "念双", "小凡", "尔琴", "冬卉", "初柳", "天玉", "千愁", "稚晴", "怀曼", "雪曼", "雪枫", "缘郡", "雁梅", "雅容", "雁枫", "灵寒", "寻琴", "慕儿", "雅霜", "含莲", "曼香", "慕山", "书兰", "凡波", "又莲", "沛春", "语梦", "青槐", "新之", "含海", "觅波", "嫣然", "善愁", "善若", "善斓", "千雁", "白柏", "雅柏", "冬灵", "平卉", "不弱", "不惜", "灵槐", "海露", "白梦", "尔蓉", "芷珊", "迎曼", "问兰", "又柔", "雪青", "傲之", "绿兰", "听兰", "冰旋", "白山", "荧荧", "迎荷", "丹彤", "海白", "谷云", "以菱", "以珊", "雪萍", "千兰", "大娘", "思枫", "白容", "翠芙", "寻雪", "冰岚", "新晴", "绿蓉", "傲珊", "安筠", "怀亦", "安寒", "青丝", "灵枫", "芷蕊", "寻真", "以山", "菲音", "寒烟", "易云", "夜山", "映秋", "唯雪", "嫣娆", "梦菲", "凤凰", "一寡", "幻然", "颜演", "白翠", "傲菡", "妙松", "忆南", "醉蓝", "碧彤", "水之", "怜菡", "雅香", "雅山", "丹秋", "盼晴", "听双", "冷亦", "依萱", "静槐", "冰之", "曼柔", "夏云", "凌寒", "夜天", "小小", "如南", "寻绿", "诗翠", "丹翠", "从蕾", "忆丹", "傲薇", "宛白", "幻枫", "晓旋", "初瑶", "如蓉", "海瑶", "代曼", "靖荷", "采枫", "书白", "凝阳", "孤晴", "如音", "傲松", "书雪", "怜翠", "雪柳", "安容", "以彤", "翠琴", "安萱", "寄松", "雨灵", "新烟", "妙菡", "雪晴", "友瑶", "丹珍", "白凝", "孤萍", "寒蕾", "妖妖", "藏花", "葵阴", "幻嫣", "幻悲", "若冰", "藏鸟", "又槐", "夜阑", "灭绝", "藏今", "凌柏", "向雪", "丹雪", "无心", "夜雪", "幻桃", "念瑶", "白卉", "飞绿", "怀梦", "幼菱", "芸遥", "芷波", "灵波", "一凤", "尔蝶", "问雁", "一曲", "问芙", "涔雨", "宫苴", "尔云", "秋凌", "灵煌", "寒梅", "灵松", "安柏", "晓凡", "冰颜", "行云", "觅儿", "天菱", "舞仙", "念真", "代亦", "飞阳", "迎天", "摇伽", "菲鹰", "惜萍", "安白", "幻雪", "友桃", "飞烟", "沛菡", "水绿", "天薇", "依瑶", "夏岚", "晓筠", "若烟", "寄风", "思雁", "乐荷", "雨南", "乐蓉", "易梦", "凡儿", "翠曼", "静曼", "魂幽", "茹妖", "香魔", "幻姬", "凝珍", "怜容", "惜芹", "笑柳", "太君", "莫茗", "忆秋", "代荷", "尔冬", "山彤", "盼雁", "山晴", "乐瑶", "灵薇", "盼易", "听蓉", "宛儿", "从灵", "如娆", "南霜", "元蝶", "忆霜", "冬云", "访文", "紫夏", "新波", "千萍", "凤妖", "水卉", "靖儿", "青烟", "千琴", "问凝", "如冰", "半梦", "怀莲", "傲芙", "静蕾", "艳血", "绾绾", "绝音", "若血", "若魔", "虔纹", "涟妖", "雪冥", "邪欢", "冰姬", "四娘", "二娘", "三娘", "老姆", "黎云", "青旋", "语蕊", "代灵", "紫山", "傲丝", "听寒", "秋珊", "代云", "代双", "晓蓝", "茗茗", "天蓉", "南琴", "寻芹", "诗柳", "冬莲", "问萍", "忆寒", "尔珍", "新梅", "白曼", "一一", "安波", "醉香", "紫槐", "傲易", "冰菱", "访曼", "冷卉", "乐儿", "幼翠", "孤兰", "绮菱", "觅夏", "三颜", "千风", "碧灵", "雨竹", "平蓝", "尔烟", "冬菱", "笑寒", "冰露", "诗筠", "鸣凤", "沛文", "易文", "绿凝", "雁玉", "梦曼", "凌雪", "怜晴", "傲玉", "柔", "幻儿", "书萱", "绮玉", "诗霜", "惜寒", "惜梦", "乐安", "以蓝", "之瑶", "夏寒", "妍", "丹亦", "凌珍", "问寒", "访梦", "新蕾", "书文", "平凡", "如天", "怀柔", "语柔", "芾", "宛丝", "南蕾", "迎海", "代芹", "巧曼", "代秋", "慕梅", "幼蓉", "亦寒", "莹", "冬易", "丹云", "丹寒", "丹蝶", "代真", "翠梅", "翠风", "翠柏", "翠安", "从霜", "从露", "初之", "初柔", "初露", "初蝶", "采萱", "采蓝", "采白", "冰烟", "冰彤", "冰巧", "斌", "傲云", "凝冬", "雁凡", "书翠", "千凡", "半青", "惜儿", "曼凡", "乐珍", "新柔", "翠萱", "飞瑶", "幻露", "梦蕊", "安露", "晓露", "白枫", "怀薇", "雁露", "梦竹", "盼柳", "沛岚", "夜南", "香寒", "山柏", "雁易", "静珊", "雁蓉", "千易", "笑萍", "从雪", "书雁", "曼雁", "晓丝", "念蕾", "雅柔", "采柳", "易绿", "向卉", "惜文", "冰兰", "尔安", "语芹", "晓山", "秋蝶", "曼卉", "凝梦", "向南", "念文", "冰蓝", "听南", "慕凝", "如容", "亦凝", "乐菱", "怀蝶", "惜筠", "冬萱", "初南", "含桃", "语风", "白竹", "夏瑶", "雅绿", "怜雪", "从菡", "访波", "安青", "觅柔", "雅青", "白亦", "宛凝", "安阳", "苞络","安邦", "安福", "安歌", "安国", "安和", "安康", "安澜", "安民", "安宁", "安平", "安然", "安顺", "安翔", "安宜", "安易", "安志", "安怡", "安晏", "昂杰", "昂然", "昂熙", "昂雄", "彬彬", "彬炳", "彬郁", "斌斌", "斌蔚", "滨海", "宾白", "宾鸿", "宾实", "炳君", "波光", "波鸿", "波峻", "波涛", "博超", "博达", "博厚", "博简", "博明", "博容", "博赡", "博涉", "博实", "博涛", "博文", "博学", "博雅", "博延", "博艺", "博易", "博裕", "博远", "博耘", "博瀚", "才捷", "才俊", "才良", "才艺", "才英", "才哲", "昌翰", "昌黎", "昌燎", "昌茂", "昌盛", "昌勋", "昌淼", "长平", "长卿", "长星", "长兴", "长旭", "长逸", "长岳", "长运", "辰骏", "辰良", "辰龙", "辰铭", "辰沛", "辰韦", "辰阳", "辰宇", "辰钊", "辰锟", "辰皓", "晨朗", "晨涛", "晨潍", "晨轩", "晨濡", "成和", "成弘", "成化", "成济", "成礼", "成龙", "成仁", "成双", "成天", "成文", "成业", "成益", "成荫", "成周", "澄邈", "承安", "承德", "承恩", "承福", "承基", "承教", "承平", "承嗣", "承天", "承望", "承宣", "承颜", "承业", "承悦", "承允", "承运", "承载", "承泽", "承志", "承弼", "驰海", "驰翰", "驰鸿", "驰轩", "驰逸", "驰皓", "德本", "德昌", "德海", "德厚", "德华", "德辉", "德惠", "德明", "德容", "德润", "德寿", "德水", "德业", "德义", "德庸", "德佑", "德宇", "德元", "德运", "德泽", "德馨", "德曜", "范明", "飞昂", "飞白", "飞掣", "飞尘", "飞沉", "飞驰", "飞光", "飞翰", "飞航", "飞鸿", "飞虎", "飞捷", "飞龙", "飞鸣", "飞鹏", "飞文", "飞翔", "飞星", "飞扬", "飞翼", "飞英", "飞雨", "飞宇", "飞语", "飞羽", "飞跃", "飞章", "飞舟", "飞飙", "飞鸾", "飞翮", "丰茂", "丰羽", "风华", "刚豪", "刚捷", "刚洁", "刚毅", "高峯", "高旻", "高昂", "高畅", "高超", "高驰", "高达", "高飞", "高芬", "高峰", "高歌", "高格", "高寒", "高翰", "高杰", "高洁", "高峻", "高朗", "高丽", "高明", "高爽", "高兴", "高轩", "高雅", "高扬", "高阳", "高逸", "高义", "高谊", "高原", "高远", "高韵", "高卓", "高懿", "高岑", "高澹", "高邈", "冠宇", "冠玉", "光赫", "光华", "光辉", "光济", "光亮", "光临", "光明", "光启", "光熙", "光耀", "光誉", "光远", "光霁", "广君", "国安", "国兴", "国源", "海昌", "海超", "海荣", "海阳", "海逸", "涵畅", "涵涤", "涵涵", "涵亮", "涵忍", "涵容", "涵润", "涵蓄", "涵衍", "涵意", "涵映", "涵育", "涵煦", "翰采", "翰池", "翰飞", "翰海", "翰林", "翰墨", "翰学", "翰音", "翰藻", "翰翮", "浩涆", "浩皛", "浩波", "浩博", "浩初", "浩大", "浩荡", "浩歌", "浩广", "浩浩", "浩慨", "浩旷", "浩阔", "浩漫", "浩渺", "浩气", "浩然", "浩壤", "浩思", "浩言", "浩瀚", "浩宕", "浩邈", "浩淼", "浩穰", "和蔼", "和安", "和畅", "和风", "和歌", "和光", "和平", "和洽", "和顺", "和硕", "和颂", "和泰", "和通", "和同", "和雅", "和宜", "和玉", "和裕", "和豫", "和悦", "和韵", "和泽", "和正", "和志", "和怡", "和悌", "和惬", "和璧", "和昶", "和煦", "鹤轩", "鹤骞", "鸿宝", "鸿波", "鸿博", "鸿才", "鸿彩", "鸿畅", "鸿畴", "鸿达", "鸿德", "鸿飞", "鸿风", "鸿福", "鸿光", "鸿朗", "鸿文", "鸿熙", "鸿信", "鸿轩", "鸿雪", "鸿羽", "鸿远", "鸿云", "鸿运", "鸿哲", "鸿振", "鸿志", "鸿卓", "鸿骞", "鸿晖", "鸿煊", "鸿祯", "鸿禧", "鸿羲", "宏博", "宏伯", "宏才", "宏畅", "宏达", "宏大", "宏放", "宏富", "宏峻", "宏浚", "宏旷", "宏阔", "宏朗", "宏茂", "宏儒", "宏深", "宏盛", "宏胜", "宏爽", "宏硕", "宏伟", "宏扬", "宏逸", "宏毅", "宏义", "宏远", "宏壮", "宏恺", "宏邈", "弘博", "弘大", "弘方", "弘光", "弘和", "弘厚", "弘化", "弘济", "弘阔", "弘量", "弘亮", "弘深", "弘盛", "弘图", "弘伟", "弘文", "弘新", "弘雅", "弘扬", "弘业", "弘毅", "弘义", "弘益", "弘致", "弘壮", "弘懿", "华奥", "华采", "华彩", "华灿", "华藏", "华池", "华翰", "华辉", "华茂", "华美", "华清", "华荣", "华容", "华晖", "华皓", "吉星", "季萌", "季同", "嘉赐", "嘉德", "嘉福", "嘉良", "嘉茂", "嘉慕", "嘉木", "嘉纳", "嘉年", "嘉平", "嘉庆", "嘉荣", "嘉容", "嘉瑞", "嘉胜", "嘉石", "嘉实", "嘉树", "嘉熙", "嘉祥", "嘉许", "嘉勋", "嘉言", "嘉谊", "嘉颖", "嘉佑", "嘉玉", "嘉誉", "嘉悦", "嘉运", "嘉泽", "嘉珍", "嘉志", "嘉致", "嘉懿", "嘉澍", "嘉歆", "嘉祯", "嘉禧", "家骏", "坚白", "坚壁", "坚秉", "坚成", "坚诚", "健柏", "建安", "建白", "建柏", "建本", "建德", "建华", "建明", "建木", "建树", "建同", "建修", "建业", "建义", "建元", "建章", "建中", "建茗", "建弼", "金鹏", "金鑫", "锦程", "晋鹏", "经赋", "经国", "经略", "经纶", "经纬", "经武", "经业", "经艺", "经义", "经亘", "景澄", "景福", "景浩", "景焕", "景辉", "景龙", "景明", "景平", "景山", "景胜", "景天", "景同", "景行", "景逸", "景彰", "景中", "景曜", "景铄", "敬曦", "靖琪", "君博", "君豪", "君浩", "君之", "君昊", "峻熙", "俊喆", "俊艾", "俊拔", "俊材", "俊才", "俊驰", "俊楚", "俊达", "俊德", "俊发", "俊风", "俊豪", "俊健", "俊杰", "俊捷", "俊郎", "俊力", "俊良", "俊迈", "俊茂", "俊美", "俊民", "俊明", "俊名", "俊能", "俊人", "俊爽", "俊晤", "俊悟", "俊侠", "俊贤", "俊雄", "俊雅", "俊彦", "俊逸", "俊英", "俊友", "俊语", "俊誉", "俊远", "俊哲", "俊智", "俊弼", "俊楠", "俊晖", "骏喆", "骏俊", "骏年", "骏奇", "骏伟", "骏祥", "骏逸", "骏哲", "骏琛", "骏桀", "开畅", "开诚", "开济", "开朗", "开宇", "开霁", "凯安", "凯唱", "凯定", "凯风", "凯复", "凯歌", "凯捷", "凯凯", "凯康", "凯乐", "凯旋", "凯泽", "康安", "康伯", "康成", "康德", "康复", "康健", "康乐", "康宁", "康平", "康盛", "康胜", "康时", "康适", "康顺", "康泰", "康裕", "康震", "昆峰", "昆卉", "昆杰", "昆纶", "昆明", "昆鹏", "昆锐", "昆纬", "昆雄", "昆谊", "昆宇", "昆琦", "昆皓", "昆颉", "乐安", "乐邦", "乐成", "乐池", "乐和", "乐家", "乐康", "乐人", "乐容", "乐山", "乐生", "乐圣", "乐水", "乐天", "乐童", "乐贤", "乐欣", "乐心", "乐逸", "乐意", "乐音", "乐咏", "乐游", "乐语", "乐悦", "乐湛", "乐章", "乐正", "乐志", "黎明", "黎昕", "理全", "理群", "礼骞", "立诚", "立果", "立辉", "立群", "立人", "立轩", "力夫", "力强", "力勤", "力行", "力学", "力言", "良奥", "良材", "良才", "良策", "良畴", "良工", "良翰", "良吉", "良俊", "良骏", "良朋", "良平", "良哲", "良弼", "良骥", "令璟", "令枫", "令锋", "令秋", "令雪", "令羽", "令梓", "令飒", "茂材", "茂才", "茂德", "茂典", "茂实", "茂学", "茂勋", "茂彦", "孟君", "敏叡", "敏博", "敏才", "敏达", "敏学", "敏智", "明喆", "明诚", "明达", "明德", "明辉", "明杰", "明俊", "明朗", "明亮", "明旭", "明轩", "明远", "明哲", "明知", "明志", "明智", "明珠", "明煦", "铭晨", "澎湃", "彭薄", "彭勃", "彭湃", "彭彭", "彭魄", "彭越", "彭泽", "彭祖", "朋兴", "朋义", "鹏程", "鹏池", "鹏飞", "鹏赋", "鹏海", "鹏鲸", "鹏举", "鹏涛", "鹏天", "鹏翼", "鹏云", "鹏运", "鹏煊", "鹏鲲", "鹏鹍", "浦和", "浦泽", "奇略", "奇迈", "奇胜", "奇水", "奇思", "奇伟", "奇文", "奇希", "奇逸", "奇正", "奇志", "奇致", "奇邃", "奇玮", "起运", "庆生", "荣轩", "瑞渊", "锐达", "锐锋", "锐翰", "锐进", "锐精", "锐利", "锐立", "锐思", "锐逸", "锐意", "锐藻", "锐泽", "锐阵", "锐志", "锐智", "绍辉", "绍钧", "绍元", "绍晖", "绍祺", "升荣", "圣杰", "书君", "斯伯", "斯年", "思博", "思聪", "思源", "思远", "思淼", "泰初", "泰和", "泰河", "泰鸿", "泰华", "泰宁", "泰平", "泰清", "泰然", "腾骏", "腾逸", "腾骞", "天材", "天成", "天赋", "天干", "天工", "天翰", "天和", "天华", "天骄", "天空", "天路", "天禄", "天瑞", "天逸", "天佑", "天宇", "天元", "天韵", "天泽", "天纵", "天睿", "天罡", "同方", "同甫", "同光", "同和", "同化", "同济", "巍昂", "巍然", "巍奕", "维运", "伟博", "伟才", "伟诚", "伟茂", "伟彦", "伟毅", "伟泽", "伟兆", "伟志", "伟晔", "伟祺", "伟懋", "温纶", "温茂", "温书", "温韦", "温文", "温瑜", "文昂", "文柏", "文彬", "文斌", "文滨", "文昌", "文成", "文德", "文栋", "文赋", "文光", "文翰", "文虹", "文华", "文景", "文康", "文乐", "文林", "文敏", "文瑞", "文山", "文石", "文星", "文轩", "文宣", "文彦", "文耀", "文曜", "侠骞", "翔飞", "翔宇", "项明", "项禹", "向晨", "向笛", "向明", "向荣", "向文", "向阳", "晓博", "欣德", "欣嘉", "欣可", "欣然", "欣荣", "欣悦", "欣怿", "欣怡", "新翰", "新觉", "新立", "新荣", "新知", "新曦", "新霁", "心水", "心思", "心远", "信鸿", "信厚", "信鸥", "信然", "信瑞", "星波", "星辰", "星驰", "星光", "星海", "星汉", "星河", "星华", "星火", "星剑", "星津", "星爵", "星阑", "星鹏", "星然", "星腾", "星纬", "星文", "星雨", "星宇", "星渊", "星泽", "星洲", "星晖", "星睿", "兴安", "兴邦", "兴昌", "兴朝", "兴德", "兴发", "兴国", "兴怀", "兴平", "兴庆", "兴生", "兴思", "兴腾", "兴旺", "兴为", "兴文", "兴贤", "兴修", "兴学", "兴言", "兴业", "兴运", "修诚", "修德", "修杰", "修洁", "修谨", "修明", "修能", "修平", "修齐", "修然", "修为", "修伟", "修文", "修贤", "修雅", "修永", "修远", "修真", "修竹", "修筠", "旭彬", "旭东", "旭鹏", "旭炎", "旭尧", "轩昂", "宣朗", "学博", "学海", "学林", "学民", "学名", "学文", "学义", "学真", "学智", "雪峰", "雪风", "雪松", "雅珺", "雅畅", "雅达", "雅惠", "雅健", "雅逸", "雅志", "雅懿", "雅昶", "颜骏", "炎彬", "彦昌", "彦君", "阳冰", "阳波", "阳伯", "阳成", "阳德", "阳华", "阳辉", "阳嘉", "阳平", "阳秋", "阳荣", "阳舒", "阳朔", "阳文", "阳夏", "阳旭", "阳炎", "阳羽", "阳云", "阳泽", "阳州", "阳晖", "阳曜", "阳曦", "阳飙", "阳焱", "阳煦", "阳飇", "宜春", "宜民", "宜年", "宜然", "宜人", "宜修", "逸春", "逸明", "逸仙", "意远", "意蕴", "意致", "意智", "毅君", "毅然", "寅骏", "英叡", "英喆", "英博", "英才", "英达", "英发", "英范", "英光", "英豪", "英华", "英杰", "英朗", "英锐", "英韶", "英卫", "英武", "英悟", "英勋", "英彦", "英耀", "英逸", "英毅", "英哲", "英卓", "英资", "英纵", "英奕", "英飙", "英睿", "咏德", "咏歌", "咏思", "咏志", "永安", "永昌", "永长", "永春", "永丰", "永福", "永嘉", "永康", "永年", "永宁", "永寿", "永思", "永望", "永新", "永言", "永逸", "永元", "永贞", "永怡", "勇捷", "勇军", "勇男", "勇锐", "勇毅", "佑运", "雨伯", "雨华", "雨石", "雨信", "雨星", "雨泽", "宇达", "宇航", "宇文", "宇荫", "宇寰", "玉成", "玉龙", "玉泉", "玉山", "玉石", "玉书", "玉树", "玉堂", "玉轩", "玉宇", "玉韵", "玉泽", "玉宸", "元白", "元德", "元化", "元基", "元嘉", "元甲", "元驹", "元凯", "元魁", "元良", "元亮", "元龙", "元明", "元青", "元思", "元纬", "元武", "元勋", "元正", "元忠", "元洲", "元恺", "远航", "远骞", "苑博", "苑杰", "越彬", "越泽", "耘豪", "耘涛", "耘志", "云天", "允晨", "运珹", "运诚", "运发", "运凡", "运锋", "运浩", "运恒", "运鸿", "运华", "运杰", "运骏", "运凯", "运莱", "运良", "运鹏", "运乾", "运升", "运盛", "运珧", "运晟", "蕴涵", "蕴和", "蕴藉", "泽民", "泽洋", "泽雨", "泽宇", "泽语", "曾琪", "展鹏", "哲茂", "哲圣", "哲彦", "哲瀚", "震博", "震轩", "振翱", "振博", "振国", "振海", "振华", "振凯", "振平", "振强", "振荣", "振锐", "振宇", "正诚", "正初", "正德", "正豪", "正浩", "正平", "正奇", "正青", "正卿", "正文", "正祥", "正信", "正雅", "正阳", "正业", "正谊", "正真", "正志", "职君", "志诚", "志国", "志明", "志强", "志尚", "志文", "志新", "志行", "志学", "志业", "志义", "志勇", "志用", "志泽", "志专", "致远", "智刚", "智杰", "智敏", "智明", "智伟", "智阳", "智勇", "智宇", "智渊", "智志", "智晖", "智鑫", "中震", "卓君", "子安", "子昂", "子辰", "子晋", "子民", "子明", "子墨", "子默", "子平", "子石", "子实", "子轩", "子真", "子濯", "子琪", "子瑜", "自明", "自强", "自珍", "自怡", "作人", "胤运", "胤骞", "懿轩", "恺歌", "恺乐", "溥心", "濮存", "瀚玥", "瀚昂", "瀚海", "瀚漠", "瀚彭", "瀚文", "瀚钰", "骞北", "骞魁", "骞仕", "骞信", "骞尧", "骞泽", "琪睿", "瑾瑜", "璞玉", "璞瑜", "昊苍", "昊东", "昊嘉", "昊空", "昊磊", "昊明", "昊乾", "昊然", "昊硕", "昊天", "昊伟", "昊英", "昊宇", "昊昊", "昊焱", "昊穹", "晟睿", "晗日", "晗昱", "曜灿", "曜栋", "曜坤", "曜瑞", "曜文", "曜曦", "曦晨", "曦哲", "曦之", "炫明", "烨赫", "烨华", "烨磊", "烨霖", "烨然", "烨烁", "烨伟", "烨烨", "烨煜", "烨熠", "煜祺", "熠彤", "祺福", "祺然", "祺瑞", "祺祥", "睿博", "睿才", "睿诚", "睿慈", "睿聪", "睿达", "睿德", "睿范", "睿广", "睿好", "睿明", "睿识", "睿思", "稷骞", "皓君", "皓轩", "鑫鹏" };
            }
        }
        public static class status{
            static String[] LI,TI;
            static String[] LI(){
                if(LI!=null)
                    return LI;
                return LI=new String[]{
                        "失忆：回过神来，调查员们发现自己身处一个陌生的地方，并忘记了自己是谁。记忆会随时间恢复。",
                        "被窃：调查员在 %s 小时后恢复清醒，发觉自己被盗，身体毫发无损。如果调查员携带着宝贵之物（见调查员背景），做幸运检定来决定其是否被盗。所有有价值的东西无需检定自动消失。",
                        "遍体鳞伤：调查员在 %s 小时后恢复清醒，发现自己身上满是拳痕和瘀伤。生命值减少到疯狂前的一半，但这不会造成重伤。调查员没有被窃。这种伤害如何持续到现在由守秘人决定。",
                        "暴力倾向：调查员陷入强烈的暴力与破坏欲之中。调查员回过神来可能会理解自己做了什么也可能毫无印象。调查员对谁或何物施以暴力，他们是杀人还是仅仅造成了伤害，由守秘人决定。",
                        "极端信念：查看调查员背景中的思想信念，调查员会采取极端和疯狂的表现手段展示他们的思想信念之一。比如一个信教者会在地铁上高声布道。",
                        "重要之人：考虑调查员背景中的重要之人，及其重要的原因。在 %s 小时或更久的时间中，调查员将不顾一切地接近那个人，并为他们之间的关系做出行动。",
                        "被收容：调查员在精神病院病房或警察局牢房中回过神来，他们可能会慢慢回想起导致自己被关在这里的事情。",
                        "逃避行为：调查员恢复清醒时发现自己在很远的地方，也许迷失在荒郊野岭，或是在驶向远方的列车或长途汽车上。",
                        "恐惧：调查员患上一个新的恐惧症状。在恐惧症状表上骰 1 个 D100 来决定症状，或由守秘人选择一个。调查员在 %s 小时后回过神来，并开始为避开恐惧源而采取任何措施。",
                        "狂躁：调查员患上一个新的狂躁症状。在狂躁症状表上骰 1 个 d100 来决定症状，或由守秘人选择一个。调查员会在 %s 小时后恢复理智。在这次疯狂发作中，调查员将完全沉浸于其新的狂躁症状。这症状是否会表现给旁人则取决于守秘人和此调查员。"};
            }
            static String[] TI(){
                if(TI!=null)
                    return TI;
                return TI=new String[]{
                        "记忆丢失：调查员会发现自己只记得最后身处的安全地点，却没有任何来到这里的记忆。例如，调查员前一刻还在家中吃着早饭，下一刻就已经直面着不知名的怪物。这将会持续 %s 轮。",
                        "假性残疾：调查员陷入了心理性的失明，失聪以及躯体缺失感中，持续 %s 轮。",
                        "暴力倾向：调查员陷入了六亲不认的暴力行为中，对周围的敌人与友方进行着无差别的攻击，持续 %s 轮。",
                        "偏执：调查员陷入了严重的偏执妄想之中，持续 %s 轮。有人在暗中窥视着他们，同伴中有人背叛了他们，没有人可以信任，万事皆虚。",
                        "人际依赖：守秘人适当参考调查员的背景中重要之人的条目，调查员因为一些原因而降他人误认为了他重要的人并且努力的会与那个人保持那种关系，持续 %s 轮",
                        "昏厥：调查员当场昏倒，并需要 %s 轮才能苏醒。",
                        "逃避行为：调查员会用任何的手段试图逃离现在所处的位置，即使这意味着开走唯一一辆交通工具并将其它人抛诸脑后，调查员会试图逃离 %s轮。",
                        "竭嘶底里：调查员表现出大笑，哭泣，嘶吼，害怕等的极端情绪表现，持续 %s 轮。",
                        "恐惧：调查员通过一次 D100 或者由守秘人选择，来从恐惧症状表中选择一个恐惧源，就算这一恐惧的事物是并不存在的，调查员的症状会持续 %s 轮。",
                        "躁狂：调查员通过一次 D100 或者由守秘人选择，来从躁狂症状表中选择一个躁狂的诱因，这个症状将会持续 %s 轮。"};
            }
        }
        public static class roomRules{
            //房规
            static final String[] roomReules=new String[]{
                    "出1大成功\n不满50出96 - 100大失败，满50出100大失败",
                    "不满50出1大成功，满50出1 - 5大成功\n不满50出96 - 100大失败，满50出100大失败",
                    "出1 - 5且 <= 成功率大成功\n出100或出96 - 99且 > 成功率大失败",
                    "出1 - 5大成功\n出96 - 100大失败",
                    "出1 - 5且 <= 十分之一大成功\n不满50出 >= 96 + 十分之一大失败，满50出100大失败",
                    "出1 - 2且 < 五分之一大成功\n不满50出96 - 100大失败，满50出99 - 100大失败"
            };
            static String getRoomReulesText() {
                AwLog.Log("正在生成房规数据");
                StringBuilder rules=new StringBuilder();
                for(int i=0;i<roomReules.length;i++) {
                    rules.append(String.format(Locale.US,"房规[%d]:\n",i));
                    rules.append(roomReules[i]);
                    rules.append("\n");
                }
                AwLog.Log("房规数据生成："+rules);
                return rules.toString();
            }
        }
        static JSONObject default_abilities(){
            /*
            if(default_abilities!=null)
                return default_abilities;*/
            try {
                return new JSONObject("{\"会计\":5,\"人类学\":1,\"估价\":5,\"考古学\":1,\"作画\":5,\"摄影\":5,\"表演\":5,\"伪造\":5,\"文学\":5,\"书法\":5,\"乐理\":5,\"厨艺\":5,\"裁缝\":5,\"理发\":5,\"建筑\":5,\"舞蹈\":5,\"酿酒\":5,\"捕鱼\":5,\"歌唱\":5,\"制陶\":5,\"雕塑\":5,\"杂技\":5,\"风水\":5,\"技术制图\":5,\"耕作\":5,\"打字\":5,\"速记\":5,\"魅惑\":15,\"攀爬\":20,\"计算机使用\":5,\"克苏鲁神话\":0,\"乔装\":5,\"汽车驾驶\":20,\"电气维修\":10,\"电子学\":1,\"话术\":5,\"鞭子\":5,\"电锯\":10,\"斧\":15,\"剑\":20,\"绞具\":25,\"链枷\":25,\"矛\":25,\"手枪\":20,\"步枪/霰弹枪\":25,\"冲锋枪\":15,\"弓术\":15,\"火焰喷射器\":10,\"机关枪\":10,\"重武器\":10,\"急救\":30,\"历史\":5,\"恐吓\":15,\"跳跃\":20,\"法律\":5,\"图书馆使用\":20,\"聆听\":20,\"锁匠\":1,\"机械维修\":10,\"医学\":1,\"自然学\":10,\"领航\":10,\"神秘学\":5,\"操作重型机械\":1,\"说服\":10,\"飞行器驾驶\":1,\"船驾驶\":1,\"精神分析\":1,\"心理学\":10,\"骑乘\":5,\"地质学\":1,\"化学\":1,\"生物学\":1,\"数学\":1,\"天文学\":1,\"物理学\":1,\"药学\":1,\"植物学\":1,\"动物学\":1,\"密码学\":1,\"工程学\":1,\"气象学\":1,\"司法科学\":1,\"妙手\":10,\"侦查\":25,\"潜行\":20,\"游泳\":20,\"投掷\":20,\"追踪\":10,\"驯兽\":5,\"潜水\":1,\"爆破\":1,\"读唇\":1,\"催眠\":1,\"炮术\":1,\"斗殴\":25}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new JSONObject();

        }
        static JSONObject get_similar_abilities(){
            if(similar_abilities!=null)
                return similar_abilities;
            try {
                return similar_abilities=new JSONObject("{\"str\":\"力量\",\"dex\":\"敏捷\",\"pow\":\"意志\",\"siz\":\"体型\",\"app\":\"外貌\",\"luck\":\"幸运\",\"luk\":\"幸运\",\"lck\":\"幸运\",\"con\":\"体质\",\"int\":\"智力/灵感\",\"智力\":\"智力/灵感\",\"灵感\":\"智力/灵感\",\"idea\":\"智力/灵感\",\"edu\":\"教育\",\"mov\":\"移动力\",\"san\":\"理智\",\"hp\":\"体力\",\"血\":\"体力\",\"血量\":\"体力\",\"mp\":\"魔法\",\"侦察\":\"侦查\",\"计算机\":\"计算机使用\",\"电脑\":\"计算机使用\",\"电脑使用\":\"计算机使用\",\"信誉\":\"信用评级\",\"信誉度\":\"信用评级\",\"信用度\":\"信用评级\",\"信用\":\"信用评级\",\"驾驶\":\"汽车驾驶\",\"驾驶汽车\":\"汽车驾驶\",\"驾驶(汽车)\":\"汽车驾驶\",\"驾驶（汽车）\":\"汽车驾驶\",\"驾驶:汽车\":\"汽车驾驶\",\"驾驶：汽车\":\"汽车驾驶\",\"快速交谈\":\"话术\",\"步枪\":\"步枪/霰弹枪\",\"霰弹枪\":\"步枪/霰弹枪\",\"散弹枪\":\"步枪/霰弹枪\",\"步霰\":\"步枪/霰弹枪\",\"步/霰\":\"步枪/霰弹枪\",\"步散\":\"步枪/霰弹枪\",\"步/散\":\"步枪/霰弹枪\",\"图书馆\":\"图书馆使用\",\"机修\":\"机械维修\",\"电器维修\":\"电气维修\",\"cm\":\"克苏鲁神话\",\"克苏鲁\":\"克苏鲁神话\",\"唱歌\":\"歌唱\",\"做画\":\"作画\",\"耕做\":\"耕作\",\"机枪\":\"机关枪\",\"导航\":\"领航\",\"船\":\"船驾驶\",\"驾驶船\":\"船驾驶\",\"驾驶(船)\":\"船驾驶\",\"驾驶（船）\":\"船驾驶\",\"驾驶:船\":\"船驾驶\",\"驾驶：船\":\"船驾驶\",\"飞行器\":\"飞行器驾驶\",\"驾驶飞行器\":\"飞行器驾驶\",\"驾驶:飞行器\":\"飞行器驾驶\",\"驾驶：飞行器\":\"飞行器驾驶\",\"驾驶(飞行器)\":\"飞行器驾驶\",\"驾驶（飞行器）\":\"飞行器驾驶\"}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }
        static JSONObject get_default_global_settings(){
            if(default_global_settings!=null)
                return default_global_settings;
            try {
                return default_global_settings=new JSONObject("{\n" +
                        "\"SENTENCE_ROLL\":\"咕噜咕噜，小赵发出骰子的声音～～啪！是...\",\n" +
                        "\"SENTENCE_ILLEGAL_TOO_MUCH\":\"恩？骰这些吗..等等啊让我数数，1,2,3,4...\",\n" +
                        "\"SENTENCE_CHANGE_NAME\":\"啊，你要改新名字吗...小赵记住了！（#完全没记住）\",\n" +
                        "\"SENTENCE_CHANGE_CARD\":\"离家太远会忘记故乡，车卡太多会忘记自己～\",\n" +
                        "\"SENTENCE_ILLEGAL\":\"恩恩，小赵完全听懂了！->完全不懂\",\n" +
                        "\"SENTENCE_GET_PAYER_INFO\":\"恩哼？要查看属性嘛？我查查...\",\n" +
                        "\"SENTENCE_SET_PAYER_INFO\":\"奇怪的知识增加了！（bushi\",\n" +
                        "\"SENTENCE_PROMOTION_SUCCESS\":\"奇怪的属性增加了！\",\n" +
                        "\"SENTENCE_PROMOTION_FAILURE\":\"全部木大！#{VOICE-the flower of hope.amr}\",\n" +
                        "\"SENTENCE_JRRP\":\"恩...要我说的话，你今天大概能遇到%d只小啊呜吧～\",\n" +
                        "\"SENTENCE_BIG_FAILURE\":\"啊调查员你怎么了！不要停下来啊！#{VOICE-the flower of hope.amr}\",\n" +
                        "\"SENTENCE_FAILURE\":\"你骰出了成功它妈！是的，因为失败是成功的妈妈！（强行解释\",\n" +
                        "\"SENTENCE_BIG_SUCCESS\":\"啊！这是什么骰子，好白！这就是欧洲人的实力吗（非酋脸）\",\n" +
                        "\"SENTENCE_VERY_HARD_SUCCESS\":\"极难诶！什么？差点大成功？！好的马上给你（#悄悄把骰数改成100）\",\n" +
                        "\"SENTENCE_HARD_SUCCESS\":\"困难成功还不错嘛，继续前进吧！奥里给！\",\n" +
                        "\"SENTENCE_SUCCESS\":\"是的，很普通的成功了，你应该感到满足，毕竟没有过于透支你的欧气（#吹口哨）\",\n" +
                        "\"PREFIX\":\".\",\n"+
                        "\"WHITE_LIST\":\"\"\n"+
                        "}");
            } catch (JSONException e) {
                return new JSONObject();
            }
        }
    }
    static class helper_name extends helper_constant_data.name {
        static final int GEN_JP_NAME =0;//日本名
        static final int GEN_CNEN_NAME =1;//中文英文名
        static final int GEN_CN_NAME =2;//中文名
        static final int GEN_EN_NAME =3;//英文名
        static String genNames(int mode){
            String[] lastname,firstname;
            String split;
            switch (mode) {
                default:
                case GEN_CN_NAME:
                    lastname=ARRAY_ChineseSurname();
                    firstname=ARRAY_ChineseFirstName();
                    split="";
                    break;
                case GEN_EN_NAME:
                    lastname=ARRAY_EnglishLastName();
                    firstname=ARRAY_EnglishFirstName();
                    split=" ";
                    break;
                case GEN_CNEN_NAME:
                    lastname=ARRAY_EnglishLastNameChineseTranslation();
                    firstname=ARRAY_EnglishFirstNameChineseTranslation();
                    split="·";
                    break;
                case GEN_JP_NAME:
                    lastname=ARRAY_JapaneseSurname();
                    firstname=ARRAY_JapaneseFirstName();
                    split="";
                    break;
            }
            StringBuilder names=new StringBuilder();
            for(int i=0;i<10;i++) {
                names.append(genName(lastname, firstname, split));
                names.append("、");
            }
            names.append("...");
            return names.toString();
        }
        static String genName(String[] lastname,String[] firstname,String split){
            return lastname[helper_calculation.getRandomInt(0,lastname.length-1)]+split+firstname[helper_calculation.getRandomInt(0,firstname.length-1)];
        }
    }
    static class helper_calculation{
        static class Result{
            int number;//数字类结果
            String notice;//文本类结果
            Result(int number,String notice){
                this.number=number;
                this.notice=notice;
            }
            //@Override
            //public String toString() {
            //    return "notice="+notice+",number="+number;
            //}
            public static class ResultComparator implements Comparator<Result> {
                @Override
                public int compare(Result a, Result b) {
                    String var1=a.notice;
                    String var2=b.notice;
                    return Integer.compare(var2.length(), var1.length());
                }
            }
        }
        static class Judger{
            static class JudegeResult{
                static final int JUDGE_BIG_SUCCESS =1;
                static final int JUDGE_VERY_HARD_SUCCESS =2;
                static final int JUDGE_HARD_SUCCESS =3;
                static final int JUDGE_SUCCESS =4;
                static final int JUDGE_BIG_FAILURE =5;
                static final int JUDGE_FAILURE =6;
                int code;
                String sentence;
                JudegeResult(int code,String selfid){
                    //if selfid==null ,return a simplistic result
                    this.code=code;
                    if(selfid==null)
                        switch (code){
                            case JUDGE_BIG_FAILURE:
                                sentence="大失败";
                                break;
                            case JUDGE_FAILURE:
                                sentence="失败";
                                break;
                            case JUDGE_BIG_SUCCESS:
                                sentence="大成功";
                                break;
                            case JUDGE_VERY_HARD_SUCCESS:
                                sentence="极难成功";
                                break;
                            case JUDGE_HARD_SUCCESS:
                                sentence="困难成功";
                                break;
                            case JUDGE_SUCCESS:
                                sentence="成功";
                                break;
                        }
                    else
                        switch (code){
                            case JUDGE_BIG_FAILURE:
                                sentence=helper_storage.getGlobalInfo(selfid,"SENTENCE_BIG_FAILURE");
                                break;
                            case JUDGE_FAILURE:
                                sentence=helper_storage.getGlobalInfo(selfid,"SENTENCE_FAILURE");
                                break;
                            case JUDGE_BIG_SUCCESS:
                                sentence=helper_storage.getGlobalInfo(selfid,"SENTENCE_BIG_SUCCESS");
                                break;
                            case JUDGE_VERY_HARD_SUCCESS:
                                sentence=helper_storage.getGlobalInfo(selfid,"SENTENCE_VERY_HARD_SUCCESS");
                                break;
                            case JUDGE_HARD_SUCCESS:
                                sentence=helper_storage.getGlobalInfo(selfid,"SENTENCE_HARD_SUCCESS");
                                break;
                            case JUDGE_SUCCESS:
                                sentence=helper_storage.getGlobalInfo(selfid,"SENTENCE_SUCCESS");
                                break;
                        }
                }
            }
            static int getRoomRuleID(String groupid){
                return helper_calculation.StringToInt(helper_storage.getGroupInfo(groupid,"ROOM_RULE_CODE",null),2);
            }
            //成功判断 selfid==null返回极简模式
            static JudegeResult resultJudger(String selfid,int rand, int ability,int ruleID){
                switch (ruleID){
                    case 0:{
                        if(rand==1)
                            return new JudegeResult(JudegeResult.JUDGE_BIG_SUCCESS,selfid);
                        if(ability<50)
                            if(rand>95)
                                return new JudegeResult(JudegeResult.JUDGE_BIG_FAILURE,selfid);
                        if(rand==100)
                            return new JudegeResult(JudegeResult.JUDGE_BIG_FAILURE,selfid);
                        break;
                    }
                    case 1:{
                        if(ability<50) {
                            if (rand == 1)
                                return new JudegeResult(JudegeResult.JUDGE_BIG_SUCCESS, selfid);
                            if (rand > 95)
                                return new JudegeResult(JudegeResult.JUDGE_BIG_FAILURE, selfid);
                        }else{
                            if(rand<6)
                                return new JudegeResult(JudegeResult.JUDGE_BIG_SUCCESS,selfid);
                            if (rand==100)
                                return new JudegeResult(JudegeResult.JUDGE_BIG_FAILURE, selfid);
                        }
                        break;
                    }
                    case 2: {
                        if(rand>ability){
                            if(rand>95)
                                return new JudegeResult(JudegeResult.JUDGE_BIG_FAILURE,selfid);
                        }else{
                            if(rand<6)
                                return new JudegeResult(JudegeResult.JUDGE_BIG_SUCCESS,selfid);
                        }
                        break;
                    }
                    case 3:{
                        if(rand>95)
                            return new JudegeResult(JudegeResult.JUDGE_BIG_FAILURE,selfid);
                        else if(rand<6)
                            return new JudegeResult(JudegeResult.JUDGE_BIG_SUCCESS,selfid);
                        break;
                    }
                    case 4:{
                        if(rand<6 && rand<=ability/10){
                            return new JudegeResult(JudegeResult.JUDGE_BIG_SUCCESS,selfid);
                        }else if(ability<50) {
                            if(rand>=96+ability/10){
                                return new JudegeResult(JudegeResult.JUDGE_BIG_FAILURE,selfid);
                            }
                        }else if(rand==100)
                            return new JudegeResult(JudegeResult.JUDGE_BIG_FAILURE, selfid);
                        break;
                    }
                    case 5:{
                        if(rand<3&&rand<ability/5){
                            return new JudegeResult(JudegeResult.JUDGE_BIG_SUCCESS,selfid);
                        }else if(ability<50) {
                            if(rand>=96){
                                return new JudegeResult(JudegeResult.JUDGE_BIG_FAILURE, selfid);
                            }
                        }else{
                            if(rand>=99){
                                return new JudegeResult(JudegeResult.JUDGE_BIG_FAILURE, selfid);
                            }
                        }
                        break;
                    }
                }
                if(rand>ability)
                    return new JudegeResult(JudegeResult.JUDGE_FAILURE,selfid);
                else if(rand<=ability/5)
                    return new JudegeResult(JudegeResult.JUDGE_VERY_HARD_SUCCESS,selfid);
                else if(rand<=ability/2)
                    return new JudegeResult(JudegeResult.JUDGE_HARD_SUCCESS,selfid);
                else
                    return new JudegeResult(JudegeResult.JUDGE_SUCCESS,selfid);
            }
        }
        static class PlayerNameDecoder{
            String PlayerName;
            int hp;
            //int hp_full;
            int san;
            //int san_full;
            static PlayerNameDecoder decode(String playerName){
                Matcher m=expression_pattern_PAYERNAME.matcher(playerName);
                if(!m.find())
                    return null;
                PlayerNameDecoder playerNameDecoder=new PlayerNameDecoder();
                playerNameDecoder.PlayerName=m.group(1);
                playerNameDecoder.hp=helper_calculation.StringToInt(m.group(2),0);
                //playerNameDecoder.hp_full=helper_calculation.StringToInt(m.group(3),0);
                playerNameDecoder.san=helper_calculation.StringToInt(m.group(4),0);
                //playerNameDecoder.san_full=helper_calculation.StringToInt(m.group(5),0);
                return playerNameDecoder;
            }
        }
        static class CmdGenerator{
            static String CHANGE_MEMBER_NICK(String id,String newName){
                return String.format("#{CHANGE MEMBER NICK-%s %s}",id,newName);
            }
        }
        static class Array{
            private static <T> void swap(T[] a, int i, int j){
                T temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
            //数组打乱
            public static <T> void shuffle(T[] arr) {
                int length = arr.length;
                for ( int i = length; i > 0; i-- ){
                    int randInd = mersenneTwister.nextInt(i);
                    swap(arr, randInd, i - 1);
                }
            }
        }
        static boolean isAutoChangePlayerNameEnabled(String groupid){
            return "on".equals(helper_storage.getGroupInfo(groupid, "AUTO_SET_NAME", "off"));
        }
        static String GeneratePlayerName(String playerName,JSONObject abilityInfo,int hp,int san){
            int hp_full=helper_calculation.abilities_get_MaxHp(abilityInfo);
            int san_full=helper_calculation.abilities_get_MaxSan(abilityInfo);
            return String.format(Locale.US,"%s hp%d/%d san%d/%d",playerName,hp,hp_full,san,san_full);
        }
        static String ToDBC(String input) {//全角转半角
            char[] c = input.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (c[i] == '\u3000') {
                    c[i] = ' ';
                } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                    c[i] = (char) (c[i] - 65248);
                }
            }
            return new String(c).replace("。",".");
        }
        static boolean textIsEmpty(String text){
            return text == null || "".equals(text);
        }
        static int getRandomInt(int min,int max){
            return mersenneTwister.nextInt(max-min+1)+min;
            //return new Random().nextInt(max-min+1)+min;
        }
        static Result XdXCalculation(int times,int maxValue,int k,int q){
            //AwLog.Log("times="+times+",maxValue="+maxValue+",k="+k+",q="+q);
            Integer[] randnums=new Integer[times];
            int total=0;
            StringBuilder process=new StringBuilder();
            for(int i=0;i<times;i++) {
                int rand_1_n = getRandomInt(1, maxValue);
                randnums[i] = rand_1_n;
            }
            if(k>0 || q>0){
                int number;
                if(k>0) {
                    number=k;
                    Arrays.sort(randnums, new Comparator<Integer>() {
                        @Override
                        public int compare(Integer o1, Integer o2) {
                            return o2.compareTo(o1);
                        }
                    });
                }else {//q>0
                    number=q;
                    Arrays.sort(randnums);
                }
                if(number>randnums.length)
                    number=randnums.length;
                process.append("(");
                for (int i=0;i<number;i++) {
                    total += randnums[i];
                    if(i>0) process.append("+");
                    process.append(randnums[i]);
                }
                process.append(")");
                process.append("[");
                for (int i=0;i<randnums.length;i++) {
                    if(i>0) process.append(",");
                    process.append(randnums[i]);
                }
                process.append("]");
            }else{
                for (int i=0;i<randnums.length;i++) {
                    total += randnums[i];
                    if(i>0) process.append("+");
                    process.append(randnums[i]);
                }
            }
            AwLog.Log("process="+process.toString());
            return new Result(total,process.toString());
        }
        static Result XdXCalculation(String expression){
            return XdXCalculation(expression,100,0);//默认普通模式
        }
        static Result XdXCalculation(String expression,int mode){
            return XdXCalculation(expression,100,mode);//默认普通模式
        }
        static Result XdXCalculation(String expression,int defaultNumber,int mode){
            //mode =0普通模式
            //mode =1取最大值
            //mode =2取最小值
            if(helper_calculation.textIsEmpty(expression))
                expression="1d"+defaultNumber;
            //else if(helper_calculation.isNumber(expression))
            //    expression="1d"+expression;
            //Pattern XdX_pattern = Pattern.compile("(\\d+)");
            Matcher expression_matcher = expression_pattern_NdNkqN.matcher(expression);
            ArrayList<Result> XdX= new ArrayList<>();
            //String process="";
            int count=0;
            while (expression_matcher.find()) {
                //Log("count="+count);
                String XdX_string=expression_matcher.group(0);
                Result result=new Result(0,XdX_string);
                XdX.add(result);
                int times,maxValue,number_k=0,number_q=0;
                times= helper_calculation.StringToInt(expression_matcher.group(1),1);
                maxValue= helper_calculation.StringToInt(expression_matcher.group(2),defaultNumber);
                String kqN=expression_matcher.group(3);
                //AwLog.Log("expression="+expression+",roll="+XdX_string+",kqN="+kqN);
                if(kqN!=null){
                    if(kqN.startsWith("k")){
                        number_k=helper_calculation.StringToInt(kqN.substring(1),0);
                    }else if(kqN.startsWith("q")){
                        number_q=helper_calculation.StringToInt(kqN.substring(1),0);
                    }
                }
                switch (mode){
                    case 0:
                        if(times>50)
                            return new Result(0,"!!![无法计算,骰子数量>50]");
                        result.number=XdXCalculation(times,maxValue,number_k,number_q).number;
                        break;
                    case 1:
                        result.number=times*maxValue;
                        break;
                    case 2:
                        result.number=times;
                        break;
                }
                count++;
                //Log("XdX_string="+times+","+maxValue);
            }
            Collections.sort(XdX,new Result.ResultComparator());
            String calculation_expression = expression;
            for (int i = 0; i < count; i++) {
                Result XdX_single = XdX.get(i);calculation_expression = helper_calculation.replaceOnce(calculation_expression,XdX_single.notice, String.valueOf(XdX_single.number));
            }
            if(helper_calculation.isNumber(calculation_expression)) {
                int result_int=helper_calculation.StringToInt(calculation_expression,0);
                if(result_int<0)
                    result_int=0;
                return new Result(result_int, expression);
            }
            int result_int=Caculate.calFinalResult(calculation_expression);
            if(result_int<0)
                result_int=0;
            return new Result(result_int,expression+"="+calculation_expression);
        }
        static String replaceOnce(String str, String fstr, String pstr){
            int position=str.indexOf(fstr);
            if(position==-1)
                return str;
            String stra=str.substring(0,position);
            String strb=str.substring(position+fstr.length());
            return stra+pstr+strb;
        }
        /*
        public static int getFirstNumberPosition(String str){
            Pattern numberPattern = Pattern.compile("(\\d+)");
            Matcher mt=numberPattern.matcher(str);
            if(mt.find()){
                return mt.start();
            }
            return -1;
        }*/
        /**
         * 判断是否为数字格式不限制位数
         * @param o
         *     待校验参数
         * @return
         *     如果全为数字，返回true；否则，返回false
         */
        static boolean isNumber(Object o){
            return  o!=null&&(Pattern.compile("[0-9]*")).matcher(String.valueOf(o)).matches();
        }
        //判断字符串是不是以数字开头
        static boolean isStartWithNumber(String str) {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str.charAt(0)+"");
            return isNum.matches();
        }
        //判断字符串是不是以数字结尾
        static boolean isEndWithNumber(String str) {
            Pattern pattern = Pattern.compile("[0-9]$");
            Matcher isNum = pattern.matcher(str.charAt(0)+"");
            return isNum.matches();
        }
        static int StringToInt(Object num, int def){
            if(num==null || "".equals(num))
                return def;
            if(num.getClass()==Integer.class)
                return (Integer)num;
            try {
                return Integer.parseInt((String) num);
            }catch (Exception e){
                return def;
            }
        }
        static String Date(){
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd",Locale.US);
            Date date = new Date(System.currentTimeMillis());
            return formatter.format(date);
        }
        //相似技能名称翻译
        static String abilities_name_translate(String input){
            if(input==null)
                return null;
            return abilities_name_translate(input,helper_constant_data.get_similar_abilities());
        }
        static String abilities_name_translate(String input, JSONObject similarList){
            //get_similiar_abilities
            if(similarList.has(input)){
                try {
                    input=similarList.getString(input);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //替换掉所有特殊字符
            input=expression_pattern_SpecialChars.matcher(input).replaceAll("").trim();
            return input;
        }
        static int abilities_get_MaxSan(JSONObject abilities){//计算最大san
            int cthulhu=abilities.optInt("克苏鲁神话",0);
            if(cthulhu>99)
                cthulhu=99;
            return 99-cthulhu;
        }
        static int abilities_get_MaxHp(JSONObject abilities) {//计算最大hp
            int body_quality=abilities.optInt("体质",0);
            int size=abilities.optInt("体型",0);
            return abilities_get_MaxHp(body_quality,size);
        }
        static int abilities_get_MaxHp(int body_quality,int size) {//计算最大hp
            return (body_quality+size)/10;
        }
        static String abilities_get_db(JSONObject abilities) {//计算db
            int power=abilities.optInt("力量",0);
            int size=abilities.optInt("体型",0);
            return abilities_get_db(power,size);
        }
        static String abilities_get_db(int power,int size){//计算db
            int mix=power+size;//力量与体型相加
            if(mix<=64){
                return "-2";
            }else if(mix<=84){
                return "-1";
            }else if(mix<=124){
                return "0";
            }else if(mix<=164){
                return "1d4";
            }else{
                return String.format(Locale.US,"%dd6",((mix-204)/80)+1);
            }
        }
    }
    public static class helper_interface_in{
        String msg;//消息内容
        String id;//用户标识
        String groupid;//群标识
        String selfid;//自身标识(骰子标识)
        long time;//消息时间
        Context context;
        Adaptation adaptation;
        boolean is_dice_open;
        boolean is_admin;
        boolean is_publicMode;
        String nickName;
        public helper_interface_in(Adaptation adaptation,String msg,String groupid, String id,String selfid,String nickName, long time,boolean is_dice_open,boolean is_admin,boolean is_publicMode){
            this.msg=msg.trim();
            this.time=time;
            this.id=id;
            this.selfid=selfid;
            this.groupid=groupid;
            this.adaptation=adaptation;
            this.context=adaptation.context;
            this.is_dice_open=is_dice_open;
            this.is_admin=is_admin;
            this.is_publicMode=is_publicMode;
            this.nickName=nickName;
        }
        public void setMsg(String msg){
            this.msg=msg;
        }
    }
    public static class helper_interface_do{
        public String id;
        String groupid;
        String cmd;
        String selfid;
        Context context;
        String nickName;
        boolean is_admin;
        boolean is_master;
        boolean is_diceopen;
        boolean no_sentence;
        boolean is_publicMode;
        Adaptation adaptation;
        helper_interface_do(helper_interface_in in,String cmd){
            this.adaptation=in.adaptation;
            this.cmd=cmd;
            this.id=in.id;
            this.selfid=in.selfid;
            this.groupid=in.groupid;
            this.context=in.context;
            this.is_admin=in.is_admin;
            this.is_diceopen=in.is_dice_open;
            this.nickName=in.nickName;
            this.is_publicMode=in.is_publicMode;
            this.is_master= helper_do.util.is_master_QQ(this);
            this.no_sentence=false;
        }
    }
    public static class helper_interface_out{
        public String msg;//消息内容
        public boolean isrelay;//是否使用回复形式
        public boolean forcePrivateChat;//是否强制私聊
        public helper_interface_out(String msg, boolean isrelay){
            this.msg=msg.trim();
            this.isrelay=isrelay;
            this.forcePrivateChat=false;
        }
        helper_interface_out(String msg,boolean isrelay,boolean forcePrivateChat){
            this.msg=msg.trim();
            this.isrelay=isrelay;
            this.forcePrivateChat=forcePrivateChat;
        }
    }
    private static class helper_do{
        private static class util{
            //判断qq号是不是matser
            private static boolean is_master_QQ(helper_interface_do in){
                boolean master=false;
                String[] masterQQ=helper_storage.getGlobalInfo(in.selfid,"MASTER_QQ").split("\n");
                for (String qq : masterQQ) {
                    if (qq.trim().equals(in.id)) {
                        //在master名单里
                        master = true;
                        break;
                    }
                }
                return master;
            }
        }
        private static class firstlyAttackingValue implements Comparable<firstlyAttackingValue>{
            private final int enforce;
            String id;
            int d20;
            int buff;
            int mix;
            firstlyAttackingValue(String id,int d20,int buff,int enforce){
                this.id=id;
                this.d20=d20;
                this.buff=buff;
                this.mix=d20+buff;
                this.enforce=enforce;
            }
            int getValue(){
                if(enforce>0)
                    return enforce;
                return mix;
            }
            boolean isEnforce(){
                return enforce>0;
            }

            //返回负数代表o要排后
            @Override
            public int compareTo(firstlyAttackingValue o) {
                if(o.getValue()<getValue()){
                    return -1;
                }else if(o.getValue()>getValue()){
                    return 1;
                }else{
                    if(o.buff<buff){
                        return -1;
                    }else if(o.buff>buff){
                        return 1;
                    }
                }
                return 0;
            }

        }
        public static String debug(helper_interface_do in) {
            return "null";
        }
        public static String dismiss(final helper_interface_do in) {
            if(!in.is_publicMode)
                return "在没有开启公骰模式下,本帐号属于个人使用帐号,不能擅自退群.";
            if(!(in.is_admin || in.is_master )){//不是管理员 而且不是master
                return helper_storage.getGlobalInfo(in.selfid,"DICE_DISMISS_DENIED","无权退群，你是管理员或master吗？");
            }
            if(in.groupid==null)
                return "请在群内使用本命令.";
            new Thread(){
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    QQFunction.Troop.Set.dismiss(in.adaptation,in.groupid);
                    super.run();
                }
            }.start();
            return helper_storage.getGlobalInfo(in.selfid,"DICE_DISMISS_AGREE","此处不留赵,自有留赵处 #溜走");
        }
        public static String welcome(helper_interface_do in) {
            if(in.groupid==null)
                return "welcome 指令仅限群聊/群私聊使用";
            if(!(in.is_admin || in.is_master )){//不是管理员 而且不是master
                return "入群欢迎语只有管理员或群主才能设置";
            }
            String cmd=in.cmd.trim();
            boolean is_off=cmd.equals("off");
            boolean is_on=cmd.equals("on");
            if(is_on){
                helper_storage.saveGroupInfo(in.groupid,"DICE_WELCOME_SWITCH","on");
                return "入群欢迎语已打开";
            }else if(is_off){
                helper_storage.saveGroupInfo(in.groupid,"DICE_WELCOME_SWITCH","off");
                return "入群欢迎语已关闭";
            }else{//修改欢迎语
                if(TextUtils.isEmpty(in.cmd)){
                    return "新人入群自动发言设定:\nwelcome on 打开\nwelcome off 关闭\n welcome {自定义语句内容}";
                }
                helper_storage.saveGroupInfo(in.groupid,"DICE_WELCOME_CONTENT",cmd);
                return "入群欢迎语已设定成 welcome {自定义语句内容},如需生效还需 welcome on";
            }
            //helper_storage.saveGroupInfo(in.groupid,"DEFAULT_DICE_NUMBER",number_str);
        }
        public static String set(helper_interface_do in) {
            if(helper_calculation.textIsEmpty(in.cmd)){
                return "设置默认骰点的点数 1d?,如 set 50,则默认骰点为 1d50";
            }
            int number=helper_calculation.StringToInt(in.cmd,100);
            if(number<=0)
                number=100;
            String number_str=String.valueOf(number);
            if(in.groupid==null){
                helper_storage.savePersonInfo(in.id,"DEFAULT_DICE_NUMBER",number_str);
            }else{
                helper_storage.saveGroupInfo(in.groupid,"DEFAULT_DICE_NUMBER",number_str);
            }
            return String.format("已将默认骰面改为d%s",number_str);
        }

        //缺点:不支持自定义名称 如 .ri+5名字
        static String ri(helper_interface_do in) {
            if(in.groupid==null)
                return "ri 指令仅限群聊/群私聊使用";
            String buff=in.cmd;
            StringBuilder print=new StringBuilder();
            int buff_int=0;
            int firstlyAttackingValue;//先攻点
            String playerName=helper_storage.getPlayerName(in.id);
            String listName=playerName;
            Matcher mth=expression_pattern_complex.matcher(buff);
            if(mth.find()){
                String expression=mth.group(0);
                AwLog.Log("expression="+expression);
                String name=null;
                if(expression!=null) {
                    name = buff.substring(expression.length());
                    helper_calculation.Result result = helper_calculation.XdXCalculation(expression);
                    buff_int = result.number;
                }
                if(!helper_calculation.textIsEmpty(name))
                    listName=String.format("%s(%s)",name,playerName);
            }
            helper_calculation.Result d20=helper_calculation.XdXCalculation("d20");
            firstlyAttackingValue=buff_int+d20.number;
            print.append(listName).append("的先攻点为:\n");
            print.append(String.format(Locale.US,"d20+buff=%d+%d=%d",d20.number,buff_int,firstlyAttackingValue));
            JSONObject firstlyAttackingValuesJson;
            {
                String firstlyAttackingValues = helper_storage.getGroupInfo(in.groupid, "FIRSTLY_ATTACKING_VALUES", "{}");
                try {
                    firstlyAttackingValuesJson=new JSONObject(firstlyAttackingValues);
                } catch (JSONException e) {
                    firstlyAttackingValuesJson=new JSONObject();
                    e.printStackTrace();
                }
            }
            try {
                JSONArray arr=new JSONArray();
                arr.put(d20.number);//d20值
                arr.put(buff_int);//buff值
                arr.put(-1);//强制指定值
                firstlyAttackingValuesJson.put(listName,arr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper_storage.saveGroupInfo(in.groupid, "FIRSTLY_ATTACKING_VALUES",firstlyAttackingValuesJson.toString() );
            return print.toString();
        }
        public static String init(helper_interface_do in) {
            if(in.groupid==null)
                return "init 指令仅限群聊/群私聊使用";
            String will_del=null;
            StringBuilder print=new StringBuilder();
            if(in.cmd.startsWith("clr")){
                helper_storage.saveGroupInfo(in.groupid, "FIRSTLY_ATTACKING_VALUES",null);
                return "放空先攻点列表.";
            }
            //String playerName=helper_storage.getPlayerName(in.id);
            JSONObject firstlyAttackingValuesJson;
            {
                String firstlyAttackingValues = helper_storage.getGroupInfo(in.groupid, "FIRSTLY_ATTACKING_VALUES", "{}");
                try {
                    firstlyAttackingValuesJson=new JSONObject(firstlyAttackingValues);
                } catch (JSONException e) {
                    firstlyAttackingValuesJson=new JSONObject();
                    e.printStackTrace();
                }
            }
            if(in.cmd.startsWith("set")){
                String values=in.cmd.substring(3);
                Matcher mth=expression_pattern_SN.matcher(values);
                String name;
                if(!mth.find() || (name=mth.group(1))==null){
                    return "使用范例: ri set 名字 数值\n如 ri set 张果老 50";
                }
                int value=helper_calculation.StringToInt(mth.group(2),0);
                try {
                    JSONArray arr=new JSONArray();
                    arr.put(0);
                    arr.put(0);
                    arr.put(value);
                    firstlyAttackingValuesJson.put(name,arr);
                    helper_storage.saveGroupInfo(in.groupid, "FIRSTLY_ATTACKING_VALUES",firstlyAttackingValuesJson.toString());
                    return String.format(Locale.US,"已设置%s的先攻点为%d",name,value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(in.cmd.startsWith("del")){
                will_del=in.cmd.substring(3);
            }

            ArrayList<firstlyAttackingValue> firstlyAttackingValues=new ArrayList<firstlyAttackingValue>();
            Iterator<String> it = firstlyAttackingValuesJson.keys();
            while(it.hasNext()){
                String key=it.next();
                try {
                    JSONArray arr = firstlyAttackingValuesJson.getJSONArray(key);
                    int d20= (int) arr.get(0);
                    int buff_int= (int) arr.get(1);
                    int enforce= (int) arr.get(2);
                    firstlyAttackingValues.add(new firstlyAttackingValue(key,d20,buff_int,enforce));
                    //print.append(String.format(Locale.US,"%s的先攻点 d20+buff=%d+(%d)=%d\n",helper_storage.getPlayerName(key),d20,buff_int,d20+buff_int));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Object[] firstlyAttackingArray=firstlyAttackingValues.toArray();
            Arrays.sort(firstlyAttackingArray);
            for(int i=0;i<firstlyAttackingArray.length;i++){
                firstlyAttackingValue value= (firstlyAttackingValue) firstlyAttackingArray[i];
                if(will_del!=null && value.id.contains(will_del)){
                    print.append("[数据删除]\n");
                    firstlyAttackingValuesJson.remove(value.id);
                }else {
                    if (value.isEnforce()) {
                        print.append(String.format(Locale.US, "%d.%s的先攻点强制为%d\n", i + 1, value.id, value.getValue()));
                    } else {
                        print.append(String.format(Locale.US, "%d.%s的先攻点 d20+buff=%d+(%d)=%d\n", i + 1, value.id, value.d20, value.buff, value.getValue()));
                    }
                }
            }
            if(will_del!=null)
                helper_storage.saveGroupInfo(in.groupid, "FIRSTLY_ATTACKING_VALUES",firstlyAttackingValuesJson.toString());
            if(firstlyAttackingValues.size()==0)
                print.append("空的先攻点列表. 支持命令\n强制修改: init set 设置名 设置值\n强制清空: init clr\n查找并删除: init del 关键词");
            return print.toString();

        }
        static String who(helper_interface_do in){
            String input=in.cmd.trim();
            String[] names=input.split(" ");
            if(names.length<2)
                return "成员数需>=2,例如 who A B ";
            helper_calculation.Array.shuffle(names);
            StringBuilder sb=new StringBuilder();
            sb.append("随机分配结果:\n");
            for(int i=0;i< names.length;i++) {
                sb.append(i).append(".").append(names[i]).append("\n");
            }
            return sb.toString();
        }
        static String sn(helper_interface_do in) {
            if(TextUtils.isEmpty(in.groupid))
                return "私聊不能改群备注";
            String newName;
            if(helper_calculation.textIsEmpty(in.cmd)) {
                //根据人物卡信息生成群昵称
                String playerName=helper_storage.getPlayerName(in.id);
                JSONObject abilityInfo=helper_storage.getAbilityInfo(in.id,playerName);
                int hp=abilityInfo.optInt("体力",0);
                int san=abilityInfo.optInt("理智",0);
                newName=helper_calculation.GeneratePlayerName(playerName,abilityInfo,hp,san);
            }else{
                newName=in.cmd;
            }
            //视为改备注
            return "群备注更新成功！"+helper_calculation.CmdGenerator.CHANGE_MEMBER_NICK(in.id, newName);
        }
        static String setasn(helper_interface_do in) {
            if(!(in.is_admin || in.is_master )){//不是管理员 而且不是master
                return "无权设置自动改群名片，你是管理员或master吗？";
            }
            String status;
            if(in.cmd.contains("y")){
                status="on";
            }else if(in.cmd.contains("n")){
                status="off";
            }else{
                return "是否自动更新群名片? y/n \n 是请输入 setasn y 或 否 setasn n";
            }
            helper_storage.saveGroupInfo(in.groupid, "AUTO_SET_NAME", status);
            return String.format("已设置 是否自动更新群名片? y/n:\n %s",status);
        }
        static String setcoc(helper_interface_do in) {
            int ruleID=helper_calculation.StringToInt(in.cmd.trim(),-1);
            int currentRuleID=helper_calculation.Judger.getRoomRuleID(in.groupid);
            if(helper_calculation.textIsEmpty(in.cmd)){
                return String.format(Locale.US,"为每个群或讨论组设置COC房规，如.setcoc 1,当前房规%d \n%s",currentRuleID,helper_constant_data.roomRules.getRoomReulesText());
            }else {
                if (ruleID >= 0 && ruleID <= 5) {//房规0～5
                    if(!(in.is_admin || in.is_master ))//不是管理员 而且不是master
                        return helper_storage.getGlobalInfo(in.selfid,"SENTENCE_SETCOC_DENIED","无权设置房规");
                    helper_storage.saveGroupInfo(in.groupid, "ROOM_RULE_CODE", String.valueOf(ruleID));
                    return String.format(Locale.US, "房规变更为 \n%s", helper_constant_data.roomRules.roomReules[ruleID]);
                } else {
                    return "非法操作！房规代码的定义域为 0≤X≤5";
                }
            }
        }
        private static String master(helper_interface_do in){
            return String.format("Master信息:%s",helper_storage.getGlobalInfo(in.selfid,"MASTER_INFO","\n 赵怡然 QQ：2135983891 \n QQ群 : 323956301"));
        }
        private static String draw(helper_interface_do in){
            //no_sentence 无自定义语句
            String draw_content=helper_draw.draw(in.cmd.trim());
            if(draw_content==null)
                draw_content=helper_storage.getGlobalInfo(in.selfid,"SENTENCE_DRAW_FAILURE",String.format("找不到指定牌堆，请确认将shiki系牌堆(json文件)/sitanya系牌堆(yaml文件)放入 %s 文件夹中",ConfigReader.PATH_DRAW));
            else {
                if(!in.no_sentence)
                    draw_content = helper_storage.getGlobalInfo(in.selfid, "SENTENCE_DRAW_SUCCESS", "抽到了这个：") + "\n" + draw_content;
            }
            //一些动态参数的替换（如骰娘名称）
            String diceName=helper_storage.getGlobalInfo(in.selfid,"DICE_NAME","赵怡然");
            draw_content=draw_content.replace("{nick}",diceName);//溯洄
            draw_content=draw_content.replace("【name】",diceName);//塔骰
            return draw_content;
        }
        private static String nn(helper_interface_do in){
            StringBuilder sb=new StringBuilder();
            String inputPlayerName=in.cmd;
            if(helper_calculation.textIsEmpty(inputPlayerName))
                return nnshow(in);
            JSONObject playerInfo= helper_storage.getAbilityInfo(in.id,inputPlayerName,true);
            String notice;
            if(playerInfo==null){
                //档位不存在，则视作改名
                String oldPlayerName= helper_storage.getPlayerName(in.id);//取原来的名字
                JSONObject oldplayerInfo= helper_storage.getAbilityInfo(in.id,oldPlayerName);//取原来角色的信息
                helper_storage.saveAbilityInfo(in.id,inputPlayerName,oldplayerInfo);//新名字 老档案
                helper_storage.deletePlayerInfo(in.id,oldPlayerName);//删除老信息
                notice=String.format(Locale.US,helper_storage.getGlobalInfo(in.selfid,"SENTENCE_CHANGE_NAME")+" \n %s 已改名为 %s",oldPlayerName,inputPlayerName);
            }else{
                //档位存在，直接设置
                notice=String.format(Locale.US,helper_storage.getGlobalInfo(in.selfid,"SENTENCE_CHANGE_CARD")+" \n 已切换现有档位 %s",inputPlayerName);
            }
            helper_storage.savePlayerName(in.id,inputPlayerName);

            //自动更新群昵称
            if(!TextUtils.isEmpty(in.groupid)) {
                if(helper_calculation.isAutoChangePlayerNameEnabled(in.groupid)) {
                    JSONObject abilities = helper_storage.getAbilityInfo(in.id, inputPlayerName);
                    int hp = abilities.optInt("体力", -1);
                    int san = abilities.optInt("理智", -1);
                    if (hp != -1 && san != -1) {//如果录入了体力和理智，自动更新昵称
                        String newName=helper_calculation.GeneratePlayerName(inputPlayerName,abilities,hp,san);
                        sb.append(helper_calculation.CmdGenerator.CHANGE_MEMBER_NICK(in.id, newName));
                    }
                }
            }

            sb.append(notice);
            return sb.toString();
        }
        private static String sc(helper_interface_do in,int inputsan){
            String[] sc;
            sc=in.cmd.split("/",2);
            if(sc.length!=2)
                return helper_storage.getGlobalInfo(in.selfid,"SENTENCE_ILLEGAL")+"\n格式错误 无法SanCheck.";
            String success_expression=sc[0];
            String failure_expression=sc[1];
            String playername= helper_storage.getPlayerName(in.id);
            JSONObject abilities= helper_storage.getAbilityInfo(in.id,playername);
            StringBuilder sb=new StringBuilder();
            int san;
            if(inputsan>0)
                san=inputsan;
            else
                san=abilities.optInt("理智",0);
            int ori_san=san;
            if(san==0){
                return helper_storage.getGlobalInfo(in.selfid,"SENTENCE_ILLEGAL")+"\nsan为0 无法SanCheck.";
            }else{
                int random_1_100=COCHelper.helper_calculation.getRandomInt(1,100);
                //random_1_100=1;
                int ruleID=helper_calculation.Judger.getRoomRuleID(in.groupid);
                helper_calculation.Judger.JudegeResult result= helper_calculation.Judger.resultJudger(in.no_sentence?null:in.selfid,random_1_100,ori_san,ruleID);
                helper_calculation.Result scResult;
                switch (result.code){
                    case helper_calculation.Judger.JudegeResult.JUDGE_BIG_FAILURE:
                        scResult= helper_calculation.XdXCalculation(failure_expression,1);
                        break;
                    case helper_calculation.Judger.JudegeResult.JUDGE_BIG_SUCCESS:
                        scResult= helper_calculation.XdXCalculation(success_expression,2);
                        break;
                    case helper_calculation.Judger.JudegeResult.JUDGE_FAILURE:
                        scResult= helper_calculation.XdXCalculation(failure_expression,0);
                        break;
                    default:
                        scResult= helper_calculation.XdXCalculation(success_expression,0);
                        break;
                }
                san-=scResult.number;
                if(san<0){
                    san=0;
                }
                try {
                    abilities.put("理智",san);
                    helper_storage.saveAbilityInfo(in.id,playername,abilities);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(!TextUtils.isEmpty(in.groupid)) {
                    //只有群聊才能改群昵称
                    if (scResult.number > 0) {
                        //只有掉san了才更新群昵称
                        helper_calculation.PlayerNameDecoder decode = helper_calculation.PlayerNameDecoder.decode(in.nickName);
                        if (decode != null) {
                            String newName=helper_calculation.GeneratePlayerName(playername,abilities,decode.hp,san);
                            sb.append(helper_calculation.CmdGenerator.CHANGE_MEMBER_NICK(in.id, newName));
                        }
                    }
                }
                if(in.no_sentence)
                    sb.append(String.format(Locale.US,"%d/%d \n %s \n 减少了 %s=%d 还剩下 %d san",random_1_100,ori_san,result.sentence,scResult.notice,scResult.number,san));
                else
                    sb.append(String.format(Locale.US,"%s的Sancheck: %d/%d \n %s \n 减少了 %s=%d 还剩下 %d san",playername,random_1_100,ori_san,result.sentence,scResult.notice,scResult.number,san));
                return sb.toString(); //result.notice+" 减少了 "+ scResult.number+" san="+san;
            }
        }
        private static String stshow(helper_interface_do in){
            String action=in.cmd;
            String playername= helper_storage.getPlayerName(in.id);
            JSONObject abilities= helper_storage.getAbilityInfo(in.id,playername);

            StringBuilder strinfo=new StringBuilder();
            strinfo.append(String.format(Locale.US,"%s \n %s的信息:\n",helper_storage.getGlobalInfo(in.selfid,"SENTENCE_GET_PAYER_INFO"),playername));
            if("".equals(action)){
                Iterator<String> it = abilities.keys();
                while(it.hasNext()) {// 获得key
                    String key = it.next();
                    int value = abilities.optInt(key);
                    strinfo.append(String.format(Locale.US,"%s:%d ",key,value));
                }
            }else{
                action=helper_calculation.abilities_name_translate(action);
                int value = abilities.optInt(action);
                strinfo.append(String.format(Locale.US,"%s=%d",action,value));
            }
            return strinfo.toString();
        }
        private static String st(helper_interface_do in){
            //情况1 赵怡然-斗殴50魅惑30

            //情况2
            // 斗殴-50
            // 斗殴+50

            //情况3 斗殴50魅惑30
            String[] cmd_split=in.cmd.split("-",2);
            String playername;
            String content;//去st后的基本内容
            if(cmd_split.length==2){
                if(helper_calculation.isStartWithNumber(cmd_split[1])){
                    //情况2 增量编辑
                    playername= helper_storage.getPlayerName(in.id);
                    content=in.cmd;
                }else{
                    //情况1 带名字的批量设置属性
                    playername=cmd_split[0];
                    content=cmd_split[1];
                }
            }else{
                //情况3 当前名字的批量设置属性
                playername= helper_storage.getPlayerName(in.id);
                content=in.cmd;
            }
            String regEx = "([^0-9+\\-]+)([+\\-]?\\d+)";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(content);
            //Log(content);
            JSONObject abilities= helper_storage.getAbilityInfo(in.id,playername);
            StringBuilder all_operation=new StringBuilder();
            StringBuilder resultstr=new StringBuilder();
            int count=0;
            JSONObject abilities_name_translate_json=helper_constant_data.get_similar_abilities();
            while(m.find()){
                String value=m.group(2);
                if(value==null)
                    return helper_storage.getGlobalInfo(in.selfid,"SENTENCE_ILLEGAL");

                String action=m.group(1);
                int number= helper_calculation.StringToInt(value,0);
                String firstCharNumber=value.substring(0,1);
                String operation;
                //AwLog.Log("-st-:"+action+" / "+number);
                action=helper_calculation.abilities_name_translate(action,abilities_name_translate_json);
                //AwLog.Log("-Fst-:"+action+" / "+number);
                if("-".equals(firstCharNumber) || "+".equals(firstCharNumber)){
                    //增量
                    int orgint=abilities.optInt(action,0);
                    int total=orgint+ number;
                    if(total<0)
                        total=0;
                    operation=String.format(Locale.US,"%s %d+(%d)=%d\n",action,orgint, number,total);
                    try {
                        abilities.put(action,total);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    //覆盖
                    operation=String.format(Locale.US,"%s =%d\n",action,number);
                    //AwLog.Log("-st-:"+action+" / "+number);
                    try {
                        abilities.put(action,number);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                all_operation.append(operation);
                count++;
            }
            if(count>0) {
                String SENTENCE_SET_PAYER_INFO=helper_storage.getGlobalInfo(in.selfid,"SENTENCE_SET_PAYER_INFO");
                helper_storage.saveAbilityInfo(in.id, playername, abilities);//设置玩家技能档案信息
                helper_storage.savePlayerName(in.id,playername);//设置当前玩家名字

                if (count > 5) {
                    resultstr.append(SENTENCE_SET_PAYER_INFO);
                } else {
                    resultstr.append(SENTENCE_SET_PAYER_INFO);
                    resultstr.append("\n");
                    resultstr.append(all_operation);
                }

                //自动更新群昵称
                if(!TextUtils.isEmpty(in.groupid)) {
                    if(helper_calculation.isAutoChangePlayerNameEnabled(in.groupid)) {
                        int hp=abilities.optInt("体力",0);
                        int san=abilities.optInt("理智",0);
                        String newName=helper_calculation.GeneratePlayerName(playername,abilities,hp,san);
                        resultstr.append(helper_calculation.CmdGenerator.CHANGE_MEMBER_NICK(in.id, newName));
                    }
                }
            }else{
                resultstr.append(helper_storage.getGlobalInfo(in.selfid,"SENTENCE_ILLEGAL"));
            }
            return resultstr.toString();

        }
        private static String help(helper_interface_do in){

            String prefix=helper_storage.getGlobalInfo(in.selfid,"PREFIX");
            return String.format("Dice made in java By 赵怡然\n" +
                    "当前前缀为[%s]\n" +
                    "模块版本[%s]\n" +
                    "编程日期[%s]\n" +
                    "反馈交流群:323956301\n", prefix, BuildConfig.VERSION_NAME, BuildConfig.BUILD_TIMESTAMP) +
                    "welcome 新人入群欢迎词\n" +
                    "runtime 显示系统运行环境\n" +
                    "stshow 显示技能数值\n" +
                    "nnshow 显示当前档位/角色名称\n" +
                    "master 骰主信息\n" +
                    "setcoc 设置房规\n" +
                    "robot/about 关于程序\n" +
                    "bot/robot off/on 开关骰\n" +
                    "name jp 生成人物名(日)\n" +
                    "name cn 生成人物名(中)\n" +
                    "name en 生成人物名(欧)\n" +
                    "name encn 生成人物名(中欧)\n" +
                    "draw/deck 牌堆抽取\n" +
                    "setasn y/n 允许(y)/不允许(n)自动更新群名片\n" +
                    "jrrp 今日人品\n" +
                    "help 帮助\n" +
                    "log 记录聊天内容\n" +
                    "coc 生成人物卡\n" +
                    "who A B ... 随机打乱序列\n"+
                    "stclr/del 删除角色数据\n" +
                    "sn 修改PL的群备注\n" +
                    "ti 临时症状抽取\n" +
                    "li 总结症状抽取\n" +
                    "sc SanCheck\n" +
                    "nn 改名或切换到新角色\n" +
                    "st 设置技能\n" +
                    "rp/rap/rcp 惩罚骰鉴定\n" +
                    "rb/rab/rcb 奖励骰鉴定\n" +
                    "ra/rc 房规/规则书 普通鉴定\n" +
                    "rh 暗骰(收不到需要加好友)\n" +
                    "en 成长鉴定\n" +
                    "r 获取随机数\n"+
                    "set 设置默认骰 1d?\n"+
                    "init 先攻列表\n"+
                    "ri 先攻骰点\n";
        }
        private static String runtime(){
            return "手机型号:" + Build.MODEL +"\n"+
                    "系统信息:" + Build.FINGERPRINT +"\n"+
                    "SDK版本:" + Build.VERSION.SDK_INT +"\n"+
                    "模块名称:" + BuildConfig.APPNAME+"\n"+
                    "模块版本:" + BuildConfig.VERSION_NAME +"\n"+
                    "BuildTime:"+BuildConfig.BUILD_TIMESTAMP;
        }
        private static String about(){
            return String.format("Zhao Dice! %s V%s Build-%s , BASE ON ANDROID!\n" +
                    " QQ群: 323956301 \n" +
                    " QQ: 2135983891 \n" +
                    " 开发者:赵怡然\n" +
                    " 保留所有解释权",BuildConfig.BUILD_TYPE,BuildConfig.VERSION_NAME,BuildConfig.BUILD_TIMESTAMP);
        }
        private static String li(){
            String[] ARRAY_LI=helper_constant_data.status.LI();
            return String.format(ARRAY_LI[helper_calculation.getRandomInt(0, ARRAY_LI.length-1)], helper_calculation.getRandomInt(1,10));
        }
        private static String ti(){
            String[] ARRAY_TI=helper_constant_data.status.TI();
            return String.format(ARRAY_TI[helper_calculation.getRandomInt(0, ARRAY_TI.length-1)], helper_calculation.getRandomInt(1,10));
        }
        private static String r(helper_interface_do in){
            // return helper_storage.getGlobalInfo(in.selfid,"SENTENCE_ILLEGAL")+"\nr指令格式不识别";
            // helper_storage.getGlobalInfo(in.selfid,"SENTENCE_ROLL")
            //([ca])?([bp])?(\d+)?(#\d+)?([^0-9+\-]+)?([+\-])?(\d+)?
            int defaultDiceNumber;{//默认骰点数
                String defaultDiceNumberStr;
                if(in.groupid==null){
                    defaultDiceNumberStr=helper_storage.getPersonInfo(in.id,"DEFAULT_DICE_NUMBER","100");
                }else{
                    defaultDiceNumberStr=helper_storage.getGroupInfo(in.groupid,"DEFAULT_DICE_NUMBER","100");
                }
                defaultDiceNumber=helper_calculation.StringToInt(defaultDiceNumberStr,100);
            }
            String cmd=in.cmd;
            String playerName=helper_storage.getPlayerName(in.id);
            Matcher m;
            try {
                m = expression_pattern_Roll2.matcher(cmd);
                if (m.find()) {
                    String action = helper_calculation.abilities_name_translate(m.group(2));
                    if(!(action!=null && (action.startsWith("a") || action.startsWith("b") || action.startsWith("c") || action.startsWith("p")))){
                        int times;
                        {
                            String _times = m.group(1);
                            if (_times == null)
                                times = 1;
                            else
                                times = helper_calculation.StringToInt(_times.substring(0, _times.length() - 1), 0);
                        }
                        String expression = m.group(3);
                        StringBuilder result_str = new StringBuilder();
                        if (!helper_calculation.isNumber(expression)) {
                            //db解析替换
                            JSONObject abilities = helper_storage.getAbilityInfo(in.id);
                            if(helper_calculation.textIsEmpty(expression))
                                expression="1d"+defaultDiceNumber;
                            else if (expression.contains("db")) {
                                String db = helper_calculation.abilities_get_db(abilities);
                                expression = expression.replace("db", String.format(Locale.US, "(%s)", db));
                            }
                            if (!in.no_sentence)
                                result_str.append(helper_storage.getGlobalInfo(in.selfid, "SENTENCE_ROLL"));
                            result_str.append("\n");
                            if (action != null)
                                if (!in.no_sentence)
                                    result_str.append(String.format("%s进行的%s骰点结果:\n", playerName, action));
                            if (times == 1) {
                                helper_calculation.Result result = helper_calculation.XdXCalculation(expression,defaultDiceNumber,0);
                                result_str.append(result.notice);
                                result_str.append("=");
                                result_str.append(result.number);
                            } else {
                                result_str.append(expression);
                                result_str.append("=");
                                for (int i = 0; i < times; i++) {
                                    helper_calculation.Result result = helper_calculation.XdXCalculation(expression,defaultDiceNumber,0);
                                    result_str.append(result.number);
                                    result_str.append(" ");
                                }
                            }
                            result_str.append("\n");
                            return result_str.toString();
                        }
                    }
                }
            }catch (Throwable ignored){

            }
            m=expression_pattern_Roll.matcher(cmd);
            if(m.find()){
                boolean is_standard_rule="c".equals(m.group(1));//是否为标准规则书
                String opration=m.group(2);//操作 a b p
                if(COCHelper.helper_calculation.textIsEmpty(opration))
                    opration="a";
                int mode=0;//普通
                if("p".equals(opration)){
                    mode=1;//惩罚
                }else if("b".equals(opration)){
                    mode=2;//奖励
                }
                String action=helper_calculation.abilities_name_translate(m.group(5));//鉴定内容
                int times;//循环次数
                {
                    String _times=m.group(3);
                    if(_times==null)
                        times=1;
                    else
                        times=COCHelper.helper_calculation.StringToInt(_times,1);
                }
                int mode_times;//惩罚/奖励 次数
                {
                    String _times=m.group(4);
                    if(_times==null) {
                        if(mode>0) {
                            //出现。rp5斗殴 这种情况时，5是times，但此时它应该是mode_times，并且只循环1次
                            mode_times = times;
                            times = 1;
                        }else{
                            mode_times=1;
                        }
                    }else
                        mode_times=COCHelper.helper_calculation.StringToInt(_times.substring(1),0);
                }
                String sign=m.group(6);//符号，代表在基础能力的增值或减值
                String ability_value=m.group(7);//基础能力值
                if(action==null && mode==0) {
                    String result=rabp(in, times, 0, 1, mode, is_standard_rule,in.no_sentence);
                    if(in.no_sentence)
                        return result;
                    return String.format("%s进行鉴定\n%s",playerName,result);
                }
                if(times>10|| mode_times>10)
                    return String.format("%s \n无法鉴定，惩罚/奖励/连续骰次数太高",helper_storage.getGlobalInfo(in.selfid,"SENTENCE_ILLEGAL_TOO_MUCH"));
                int ability_value_int,ability_value_int_det=0;
                int input_int=helper_calculation.StringToInt(ability_value, -1);
                ability_value_int=helper_storage.getAbilityInfo(in.id,playerName).optInt(action, 0);
                if(sign==null) {
                    if(input_int>0)
                        ability_value_int = input_int;
                }else{
                    if("+".equals(sign)){
                        ability_value_int_det=input_int;
                    }else if("-".equals(sign)){
                        ability_value_int_det=-input_int;
                    }
                }
                StringBuilder sb=new StringBuilder();
                boolean simplify=times>1||in.no_sentence;
                for(int i=0;i<times;i++) {
                    sb.append(rabp(in, ability_value_int, ability_value_int_det, mode_times, mode, is_standard_rule,simplify));
                    sb.append("\n");
                }
                if(action==null)
                    action="";
                if(in.no_sentence)
                    return sb.toString();
                return String.format("%s进行%s鉴定\n%s",playerName,action,sb);

            }
            return helper_storage.getGlobalInfo(in.selfid,"SENTENCE_ILLEGAL")+"\nr指令格式不识别";
        }
        private static String en(helper_interface_do in){
            //斗殴 or 斗殴50
            Matcher m = expression_pattern_SN.matcher(in.cmd);
            String playname= helper_storage.getPlayerName(in.id);
            String resultstr;
            if(m.find()){
                String action=helper_calculation.abilities_name_translate(m.group(1));
                JSONObject abilities= helper_storage.getAbilityInfo(in.id,playname);
                int value= helper_calculation.StringToInt(m.group(2),abilities.optInt(action,0));
                int random_1_100=COCHelper.helper_calculation.getRandomInt(1,100);
                if(random_1_100>value){
                    int random_1_10=COCHelper.helper_calculation.getRandomInt(1,10);
                    int new_value=value+random_1_10;
                    try {
                        abilities.put(action,new_value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    helper_storage.saveAbilityInfo(in.id,playname,abilities);
                    resultstr=String.format(Locale.US,"%s \n 成长%s成功 %d/%d ，属性变化 %d+%d=%d",helper_storage.getGlobalInfo(in.selfid,"SENTENCE_PROMOTION_SUCCESS"),action,random_1_100,value,value,random_1_10,new_value);
                }else
                    resultstr=String.format(Locale.US,"%s \n 成长%s失败 %d/%d ，没有变化。",helper_storage.getGlobalInfo(in.selfid,"SENTENCE_PROMOTION_FAILURE"),action,random_1_100,value);
            }else
                resultstr="格式错误";
            return resultstr;
        }
        private static String del(helper_interface_do in) {
            String inputPlayerName=in.cmd;
            if(helper_calculation.textIsEmpty(inputPlayerName))
                inputPlayerName = helper_storage.getPlayerName(in.id);
            helper_storage.deletePlayerInfo(in.id,inputPlayerName);
            return String.format("%s已删档",inputPlayerName);
        }
        private static String nnshow(helper_interface_do in) {
            String inputPlayerName = helper_storage.getPlayerName(in.id);
            return String.format("当前角色名称: %s",inputPlayerName);
        }
        private static String rabp(helper_interface_do in,int ability,int ability_det,int mode_times, int mode,boolean is_standard_rule,boolean simplify){
            //action=鉴定内容 ability=能力值 ability_det=能力值增量 mode_times=惩罚/奖励次数 mode=0普通 1惩罚 2奖励 ruleID=规则ID simplify=是否简化判定结果
            int ruleID;
            if(is_standard_rule)
                ruleID=0;
            else
                ruleID=helper_calculation.Judger.getRoomRuleID(in.groupid);
            int random_1_100 = COCHelper.helper_calculation.getRandomInt(1, 100);
            StringBuilder vs=new StringBuilder();
            StringBuilder _vs=new StringBuilder();
            if(mode>0) {//奖励或惩罚
                int random_ten_position = random_1_100 / 10;
                //Log("random_ten_position="+random_ten_position);
                _vs.append("1d100=");
                _vs.append(random_1_100);
                _vs.append(" ");
                if(mode==1)
                    _vs.append("惩罚");
                else
                    _vs.append("奖励");
                _vs.append("[");
                {
                    StringBuilder adds = new StringBuilder();
                    for (int i = 0; i < mode_times; i++) {
                        int random_1_10 = helper_calculation.getRandomInt(0, 9);
                        if ((mode == 2 && random_1_10 < random_ten_position) || (mode == 1 && random_1_10 > random_ten_position)) {
                            //替换十位
                            random_1_100 = random_1_10 * 10 + random_1_100 % 10;
                            random_ten_position = random_1_10;
                        }
                        adds.append(random_1_10);
                        adds.append(" ");
                    }
                    String adds_string=adds.toString();
                    _vs.append(adds_string.substring(0,adds_string.length()-1));
                }
                _vs.append("] ");
                if(ability<=0 && ability_det<=0) {
                    vs.append(_vs);
                    vs.append(String.format(" 结果为: %s", random_1_100));
                    return vs.toString();
                }
            }
            String self_id=simplify?null:in.selfid;//if simplify==true,set self_id==null,it causes a simple result
            helper_calculation.Judger.JudegeResult result = helper_calculation.Judger.resultJudger(self_id, random_1_100, ability + ability_det, ruleID);
            if (Math.abs(ability_det) > 0)
                vs.append(String.format("%s/%s+(%s) %s %s", random_1_100, ability, ability_det,_vs.toString().trim(), result.sentence));
            else
                vs.append(String.format("%s/%s %s %s", random_1_100, ability,_vs.toString().trim(), result.sentence));
            return vs.toString();
        }
        private static String jrrp(helper_interface_do in){
            int jrrp=0;
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] bytes = md.digest((helper_calculation.Date()+in.id).getBytes());
                AwLog.Log("jrrp算子："+bytes[0]);
                jrrp=(bytes[0]&0xff);//有符号char转换为无符号char（int型）
                jrrp= (int) (((float)jrrp/0xff)*99+1);//然后根据占了0xff的比例乘以99获取0-99的数字，再加1得1-100的数字
            } catch (NoSuchAlgorithmException ignored) {

            }
            return String.format(Locale.US,helper_storage.getGlobalInfo(in.selfid,"SENTENCE_JRRP"),jrrp);
            /*
            String dataDate= (String) helper_storage.getBaseInfo(in.id,"jrrp_date");
            int jrrp= helper_calculation.StringToInt(COCHelper.helper_storage.getBaseInfo(in.id,"jrrp"),0);
            if(!helper_calculation.Date().equals(dataDate)){
                jrrp = helper_calculation.getRandomInt(1, 100);
                COCHelper.helper_storage.saveBaseInfo(in.id, "jrrp_date", helper_calculation.Date());
                COCHelper.helper_storage.saveBaseInfo(in.id, "jrrp", jrrp);
            }
            return String.format(Locale.US,helper_storage.getGlobalInfo(in.selfid,"SENTENCE_JRRP"),jrrp);
            */

        }
        private static String robot(helper_interface_do in){
            boolean is_dice_open=in.is_diceopen;
            boolean will_open_dice;
            boolean will_close_dice=in.cmd.contains("off") && is_dice_open;
            if(will_close_dice)
                will_open_dice=false;
            else
                will_open_dice=in.cmd.contains("on") && !is_dice_open;
            if(will_open_dice || will_close_dice){//打开骰或关闭骰
                if(TextUtils.isEmpty(in.groupid))//命令不为空 且 群号为空（私聊
                    return "非法操作，因为无法通过私聊会话判断是哪个群号，请群聊操作 ，例:\n@机器人。bot off";
                if(!in.is_publicMode && !(in.is_admin || in.is_master))//不是管理员 而且不是master
                    return helper_storage.getGlobalInfo(in.selfid,"SENTENCE_DICE_DENIED","没有权限");
                String white=COCHelper.helper_storage.getGlobalInfo(in.selfid,"WHITE_LIST").trim();
                String[] white_list;
                StringBuilder new_white=new StringBuilder();
                AwLog.Log("骰子开关：试图操作骰子的打开状态，群号："+in.groupid+",状态："+will_open_dice);

                white_list=white.split("\n");
                boolean in_list=false;
                for (String s : white_list) {
                    String groupid=s;
                    if (groupid.startsWith("#")) {
                        groupid = groupid.substring(1);
                    }

                    if(groupid.equals(in.groupid)) {
                        if(will_close_dice) {
                            new_white.append("#");
                        }
                        new_white.append(groupid);
                        new_white.append("\n");
                        in_list=true;
                    }else{
                        new_white.append(s);
                        new_white.append("\n");
                    }

                }

                if(!in_list) {//如果未在列表
                    if (in.is_publicMode) {//在公骰模式下，发现群未在白，则加一条在底部。
                        if (will_close_dice)
                            new_white.append("#");
                        new_white.append(in.groupid);
                        new_white.append("\n");
                    } else {
                        return "本群未在白名单内，无法操作本骰";
                    }
                }

                String new_white_text=new_white.toString().trim();
                helper_storage.saveGlobalInfo(in.selfid,"WHITE_LIST",new_white_text);

                if(will_open_dice)
                    return helper_storage.getGlobalInfo(in.selfid,"SENTENCE_DICE_OPEN","打开成功");
                else
                    return helper_storage.getGlobalInfo(in.selfid,"SENTENCE_DICE_CLOSE","关闭成功");
            }else
                return about();
        }
        private static String coc(helper_interface_do in){
            StringBuilder returnstr=new StringBuilder();
            returnstr.append("(算法修正于2020/04/17)\n人物作成：\n");
            int times= helper_calculation.StringToInt(in.cmd,1);
            if(times>10)
                return helper_storage.getGlobalInfo(in.selfid,"SENTENCE_ILLEGAL_TOO_MUCH");
            for(int i=0;i<times;i++){
                int[] arr=new int[9];
                int total=0;
                for(int j=0;j<9;j++){
                    if(j==2||j==5||j==7)//体型 智力 教育
                        arr[j]= helper_calculation.XdXCalculation("(2d6+6)*5").number;
                    else
                        arr[j]= helper_calculation.XdXCalculation("3d6*5").number;
                    total+=arr[j];
                }
                String db=helper_calculation.abilities_get_db(arr[0],arr[2]);
                int hp=helper_calculation.abilities_get_MaxHp(arr[1],arr[2]);
                returnstr.append(String.format(Locale.US,
                            "力量:%d 体质:%d 体型:%d \n" +
                                    "敏捷:%d 外貌:%d 智力:%d \n" +
                                    "意志:%d 教育:%d 幸运:%d \n" +
                                    "DB:%s HP:%d 总和:%d/%d \n\n",
                        arr[0],arr[1],arr[2],
                        arr[3],arr[4],arr[5],
                        arr[6],arr[7],arr[8],
                        db,hp, total-arr[8],total
                ));
            }
            return returnstr.toString();
        }
        private static String name(helper_interface_do in) {
            String names;
            if(in.cmd.contains("en")){
                if(in.cmd.contains("cn"))
                    names=helper_name.genNames(helper_name.GEN_CNEN_NAME);
                else
                    names=helper_name.genNames(helper_name.GEN_EN_NAME);
            }else if(in.cmd.contains("jp"))
                names=helper_name.genNames(helper_name.GEN_JP_NAME);
            else
                names=helper_name.genNames(helper_name.GEN_CN_NAME);
            return String.format(Locale.US,"生成的人物名字: \n%s",names);
        }

        private static String log(helper_interface_do in) {
            if (in.groupid == null)
                return "仅限群聊log";
            if(!in.id.equals(in.selfid)) {//自我触发不鉴权
                if (!in.is_publicMode && !in.is_master)//不是masterQQ则拒绝访问
                    return helper_storage.getGlobalInfo(in.selfid,"SENTENCE_LOG_DENIED","无法log，因为你Q并不在master清单，请在骰娘程序内的masterQQ里加入你Q");
            }
            String log_status=helper_storage.getGroupInfo(in.groupid, "logon", null);
            boolean logon_stop="stop".equals(log_status);
            boolean logon_on="on".equals(log_status);
            boolean logon = logon_stop || logon_on;//无论是开始还是暂停状态都算已经打开了log
            boolean is_off = in.cmd.contains("off");
            boolean is_on = in.cmd.contains("on");
            boolean is_stop = in.cmd.contains("stop");
            if (is_off|is_stop) {//试图关闭log
                if (logon) {
                    if(is_stop){//is_stop=true
                        helper_storage.saveGroupInfo(in.groupid, "logon", "stop");
                        return "log已暂停，可以使用log on继续";
                    }else{//is_off=true
                        helper_storage.saveGroupInfo(in.groupid, "logon", "off");
                        String logfile=helper_storage.getGroupInfo(in.groupid,"logfile",null);
                        String path=helper_log.log_save_path+"/"+logfile;
                        return helper_storage.getGlobalInfo(in.selfid,"SENTENCE_LOG_CLOSE",String.format("关闭成功！文件已保存在%s 文件已发到群共享,如果没有，请确认是否可以上传群共享",path))+String.format("#{FILE-%s}",path);
                    }
                } else {
                    return "未曾开log。";
                }
            }
            if (is_on) {
                //试图打开log
                if (logon_on) {
                    return "log已开，无法再次开启。";
                } else {
                    if(!logon) {//只有之前的状态为（彻底关闭）时，才更新文件名
                        //更新文件名
                        helper_storage.saveGroupInfo(in.groupid, "logfile", "log_" + in.groupid + "_" + System.currentTimeMillis() + ".txt");
                    }
                    helper_storage.saveGroupInfo(in.groupid, "logon", "on");
                    return helper_storage.getGlobalInfo(in.selfid,"SENTENCE_LOG_OPEN","log打开成功。");
                }
            }
            return "请输入 log on(开始) 或 log off(终止) 或 log stop(暂停)";
        }
    }
    public static helper_interface_out cmd(helper_interface_in info){
        String cmd=helper_calculation.ToDBC(info.msg);
        helper_interface_do in=new helper_interface_do(info,null);
        boolean is_replyChecked=ConfigReader.readBoolean(in.adaptation,ConfigReader.CONFIG_KEY_SWITCH_WAY_TO_REPLY,true);
        boolean reply=is_replyChecked&&cmd.length()<200;
        if((System.currentTimeMillis())/1000-info.time>60) {
            AwLog.Log(String.format("无法处理消息%s，因为超过了60秒",info.msg));
            return null;
        }
        helper_log.onMessageReceived(info);
        String prefix=helper_calculation.ToDBC(helper_storage.getGlobalInfo(info.selfid,"PREFIX")).trim();
        if(helper_calculation.textIsEmpty(prefix))
            prefix=".";
        if(cmd.startsWith(prefix)){
            cmd=cmd.substring(prefix.length());
            if(cmd.startsWith("x")){//最简化开关
                cmd=cmd.substring(1);//去掉x
                in.no_sentence=true;
            }
            cmd=cmd.trim();
            try{
                cmd=helper_legacy.cmd_transformation(cmd);//其他骰系的指令兼容
                if(cmd.startsWith("bot")){//同robot
                    in.cmd=cmd.substring(3);
                    return new helper_interface_out(helper_do.robot(in),reply);
                }else if(cmd.startsWith("robot")){
                    in.cmd=cmd.substring(5);
                    return new helper_interface_out(helper_do.robot(in),reply);
                }
                //以下指令必须是 骰打开的情况下才会响应
                if(!info.is_dice_open) return null;
                if(cmd.startsWith("draw") || cmd.startsWith("deck")){//对draw和deck命令特许空格
                    in.cmd=cmd.substring(4);
                    return new helper_interface_out(helper_do.draw(in),reply);
                }else if(cmd.startsWith("sc")){//对sc命令特许空格
                    cmd=cmd.substring(2);
                    cmd=cmd.trim();
                    String[] saninput =cmd.split(" ",2);
                    int san=0;
                    if(saninput.length==2 && helper_calculation.isNumber(saninput[1])){
                        cmd=saninput[0];
                        san=helper_calculation.StringToInt(saninput[1],0);
                    }else
                        cmd=cmd.replace(" ","");
                    in.cmd=cmd;
                    return new helper_interface_out(helper_do.sc(in,san), reply);
                }else if(cmd.startsWith("who")){
                    in.cmd=cmd.substring(3);
                    return new helper_interface_out(helper_do.who(in),reply);
                }else if(cmd.startsWith("debug")){
                    in.cmd=cmd.substring(5);
                    return new helper_interface_out(helper_do.debug(in),reply);
                }else if(cmd.startsWith("welcome")){
                    in.cmd=cmd.substring(7);
                    return new helper_interface_out(helper_do.welcome(in),reply);
                }
                cmd=cmd.replace("\r","");
                cmd=cmd.replace("\n","");
                cmd=cmd.replace("×","*");
                cmd=cmd.replace("÷","/");
                cmd=cmd.replace("\t"," ");
                cmd=cmd.replace(" ","").toLowerCase();
                in.cmd=cmd;
                if(cmd.startsWith("runtime")){
                    return new helper_interface_out(helper_do.runtime(),reply);
                }else if(cmd.startsWith("master")){
                    in.cmd=cmd.substring(6);
                    return new helper_interface_out(helper_do.master(in),reply);
                }else if(cmd.startsWith("about")){
                    return new helper_interface_out(helper_do.about(),reply);
                }else if(cmd.startsWith("nnshow")){
                    //cmd=cmd.substring(6);
                    return new helper_interface_out(helper_do.nnshow(in),reply);
                }else if(cmd.startsWith("stshow")){
                    in.cmd=cmd.substring(6);
                    return new helper_interface_out(helper_do.stshow(in),reply);
                }else if(cmd.startsWith("setcoc")){
                    in.cmd=cmd.substring(6);
                    return new helper_interface_out(helper_do.setcoc(in),reply);
                }else if(cmd.startsWith("setasn")){
                    in.cmd=cmd.substring(6);
                    return new helper_interface_out(helper_do.setasn(in),reply);
                }else if(cmd.startsWith("dismiss")){
                    in.cmd=cmd.substring(7);
                    return new helper_interface_out(helper_do.dismiss(in),reply);
                }else if(cmd.startsWith("init")){//先攻列表
                    in.cmd=cmd.substring(4);
                    return new helper_interface_out(helper_do.init(in),reply);
                }else if(cmd.startsWith("help")){
                    //cmd=cmd.substring(4);
                    return new helper_interface_out(helper_do.help(in),reply);
                }else if(cmd.startsWith("name")){
                    in.cmd=cmd.substring(4);
                    return new helper_interface_out(helper_do.name(in),reply);
                }else if(cmd.startsWith("jrrp")){
                    return new helper_interface_out(helper_do.jrrp(in),reply);
                }else if(cmd.startsWith("stclr")){
                    in.cmd=cmd.substring(5);
                    return new helper_interface_out(helper_do.del(in),reply);
                }else if(cmd.startsWith("log")){
                    in.cmd=cmd.substring(3);
                    return new helper_interface_out(helper_do.log(in),reply);
                }else if(cmd.startsWith("set")){
                    in.cmd=cmd.substring(3);
                    return new helper_interface_out(helper_do.set(in),reply);
                }else if(cmd.startsWith("del")){
                    in.cmd=cmd.substring(3);
                    return new helper_interface_out(helper_do.del(in),reply);
                }else if(cmd.startsWith("coc")){
                    in.cmd=cmd.substring(3);
                    return new helper_interface_out(helper_do.coc(in),reply);
                }else if(cmd.startsWith("ri")){//先攻
                    in.cmd=cmd.substring(2);
                    return new helper_interface_out(helper_do.ri(in),reply);
                }else if(cmd.startsWith("sn")){
                    in.cmd=cmd.substring(2);
                    return new helper_interface_out(helper_do.sn(in),reply);
                }else if(cmd.startsWith("nn")){
                    in.cmd=cmd.substring(2);
                    return new helper_interface_out(helper_do.nn(in),reply);
                }else if(cmd.startsWith("en")){
                    in.cmd=cmd.substring(2);
                    return new helper_interface_out(helper_do.en(in),reply);
                }else if(cmd.startsWith("st")){
                    in.cmd=cmd.substring(2);
                    return new helper_interface_out(helper_do.st(in),reply);
                }else if(cmd.startsWith("rh")){//暗骰
                    in.cmd=cmd.substring(2);
                    String result=helper_do.r(in);
                    result=String.format("在群[%s]的暗骰结果\n",in.groupid)+result;
                    return new helper_interface_out(result,false,true);//发起群私聊
                }else if(cmd.startsWith("li")){
                    return new helper_interface_out(helper_do.li(),reply);
                }else if(cmd.startsWith("ti")){
                    return new helper_interface_out(helper_do.ti(),reply);
                }else if(cmd.startsWith("r")){
                    in.cmd=cmd.substring(1);
                    return new helper_interface_out(helper_do.r(in),reply);
                }
            }catch (Throwable e){
                AwLog.Log("骰子错误!!"+e.getMessage());
                return new helper_interface_out("ERROR.",reply);
            }
        }
        AwLog.Log("COCHelper unknown"+info.msg);

        boolean key_auto_reply=ConfigReader.readBoolean(info.adaptation,ConfigReader.CONFIG_KEY_SWITCH_KEY_AUTO_REPLY,false);
        //自动回复条件 1.打开了自动回复开关 2.触发者不是自己 3.骰打开了
        if(key_auto_reply && !info.selfid.equals(info.id) && info.is_dice_open){
            String replies=COCHelper.helper_storage.getGlobalInfo(info.selfid,"REPLY_EQU").trim();
            if(!TextUtils.isEmpty(replies)) {
                //设置了匹配词自动回复
                String[] list=replies.split("\n");
                for (String s : list) {
                    String[] p=s.trim().split("/",2);
                    if(p.length!=2)
                        continue;
                    String key=p[0].trim();
                    String replycontent=p[1];
                    if(info.msg.trim().equals(key))//自动回复关键词
                        return new helper_interface_out(replycontent,reply);
                }
            }
            replies=COCHelper.helper_storage.getGlobalInfo(info.selfid,"REPLY").trim();
            if(!TextUtils.isEmpty(replies)) {
                //设置了自动模糊词回复
                String[] list=replies.split("\n");
                for (String s : list) {
                    String[] p=s.trim().split("/",2);
                    if(p.length!=2)
                        continue;
                    String key=p[0].trim();
                    String replycontent=p[1];
                    if(info.msg.contains(key))//自动回复关键词
                        return new helper_interface_out(replycontent, reply);
                }
            }
        }
        return null;
    }
}
