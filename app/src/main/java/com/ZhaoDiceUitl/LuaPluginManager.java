package com.ZhaoDiceUitl;

import androidx.annotation.Keep;

import com.zhao.dice.model.Adaptation;
import com.zhao.dice.model.AwLog;
import com.zhao.dice.model.BuildConfig;
import com.zhao.dice.model.QQFunction;
import com.zhao.dice.model.plugins.QQMessage.SpecialCodeExecutor;

import org.json.JSONException;
import org.json.JSONObject;
import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class LuaPluginManager {

    private String LogPath=COCHelper.helper_storage.storage_save_path+"/plugin_log";
    private static LuaPluginManager instance;
    private Adaptation adaptation;
    private ArrayList<LuaPlugin> plugins=new ArrayList<>();
    APIRegister APIregister=new APIRegister();
    public CallToPlugin callToPlugin=new CallToPlugin();
    public static LuaPluginManager getInstance(Adaptation adaptation) {
        if (instance == null) {
            instance = new LuaPluginManager(adaptation);
        }
        return instance;
    }
    private LuaPluginManager(Adaptation adaptation){
        this.adaptation=adaptation;
    }
    protected class APIRegister{
        // MESSAGE_SEND 消息发送 send_message(int_is_troop,string_id_qq,string_id_group,string_message)
        class SendMessageRegister extends JavaFunction {
                /**
                 * Constructor that receives a LuaState.
                 *
                 * @param L LuaState object associated with this JavaFunction object
                 */
                SendMessageRegister(LuaState L) {
                    super(L);
                }
                @Override
                public int execute() throws LuaException {
                    int int_is_troop = L.toInteger(2);
                    String string_id_qq = L.toString(3);
                    String string_id_group = L.toString(4);
                    String string_message = L.toString(5);

                    if(int_is_troop==1)
                        string_id_qq=string_id_group;

                    ArrayList<String> pictures=new ArrayList<>();
                    string_message= SpecialCodeExecutor.ExCode(adaptation,string_message,string_id_qq,int_is_troop,pictures);
                    QQFunction.Sender.textAndPic(adaptation,string_id_qq,adaptation.getAccount(),string_id_group,int_is_troop,string_message,pictures,null);
                    return 0;
                }
                void register() {
                    try {
                        // 注册为 Lua 全局函数
                        //register("PermissionRequired");
                        register("send_message");
                    } catch (LuaException e) {
                        e.printStackTrace();
                    }
                }
            }
        class DataStorageRegister{
            String datapath=COCHelper.helper_storage.storage_save_path+"/plugin_data";
            LuaState L;
            class DataStorage{
                private boolean initConfig(){
                    File f=new File(datapath);
                    if(f.exists())
                        return true;
                    return f.mkdirs();
                }
                private void saveConfig(String id, JSONObject data){
                    if(initConfig()) {
                        String config_path = datapath + "/" + id + ".json";
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
                private JSONObject readConfig(String id){
                    if(initConfig()) {
                        try {
                            String config_path = datapath + "/" + id + ".json";
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
                    return new JSONObject();
                }

                //导出方法
                //写配置
                @Keep
                public void put(String key,Object obj){
                    LuaPlugin luaPlugin=getPluginByState(L);
                    if(luaPlugin==null)
                        return;
                    String plugin_id=luaPlugin.plugin_id;
                    JSONObject configs=readConfig(plugin_id);
                    try {
                        configs.put(key,obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    saveConfig(plugin_id,configs);
                }
                //读配置
                @Keep
                public Object get(String key){
                    LuaPlugin luaPlugin=getPluginByState(L);
                    if(luaPlugin==null)
                        return null;
                    String plugin_id=luaPlugin.plugin_id;
                    JSONObject configs=readConfig(plugin_id);
                    return configs.opt(key);
                }
            }
            DataStorageRegister(LuaState L){
                this.L=L;
            }
            void register() {
                L.pushJavaObject(new DataStorage());
                L.setGlobal("DataStorage");
            }

        }
        void registerAPIByPermission(LuaState lua, String Permission){//函数注册器（根据权限注册）
            AwLog.Log("试图注册函数..."+Permission);
            switch (Permission) {
                case "MESSAGE_SEND":
                    new SendMessageRegister(lua).register();
                    break;
                case "CONFIG":
                    new DataStorageRegister(lua).register();
                    break;
            }
        }
    }
    //LuaState转LuaPlugin
    private LuaPlugin getPluginByState(LuaState luaState){
        for(LuaPlugin luaPlugin : plugins){
            if(luaPlugin.lua==luaState){
                return luaPlugin;
            }
        }
        return null;
    }
    //查询是否有指定插件id
    boolean hasPluginId(String id){
        for(LuaPlugin luaPlugin : plugins){
            if(luaPlugin.plugin_id.equals(id)){
                return true;
            }
        }
        return false;
    }
    public class CallToPlugin{
        public void event_message_handle(int is_troop,String id_qq,String id_group,String message){
            for(LuaPlugin plugin : plugins){
                if(plugin.hasPermission("MESSAGE_HANDLE")) {
                    AwLog.Log("开始调用 event_message_handle ...");
                    LuaState lua = plugin.lua;
                    lua.getGlobal("event_message_handle"); // 获取到函数入栈
                    lua.pushNumber(is_troop); // 依次压入三个参数
                    lua.pushString(id_qq);
                    lua.pushString(id_group);
                    lua.pushString(message);
                    int code=lua.pcall(4, 0, 0); // 调用函数
                    if(code!=0)
                        writeLog(plugin,"ERROR!event_message_handle:"+lua.toString(-1));
                }
            }
        }
    }
    public void load(String dir){
        AwLog.Log("正在加载插件...");
        File path=new File(dir);
        if(!path.exists()) {
            path.mkdirs();
            return;
        }
        String[] list=path.list();
        if(list==null)
            return;
        for(String filename:list) {
            filename=dir+"/"+filename;
            for (LuaPlugin plugin : plugins) {
                if (filename.equals(plugin.filename)) {
                    AwLog.Log("插件文件已经加载过了！");
                    return;
                }
            }
            LuaPlugin luaPlugin = new LuaPlugin(this,adaptation.context, filename);
            if (luaPlugin.init_code != 0) {//加载插件失败
                AwLog.Log("插件文件加载失败！");
                return;
            } else {
                AwLog.Log("插件文件加载成功！");
            }
            plugins.add(luaPlugin);
        }
    }
    void writeLog(LuaPlugin plugin, String content) {
        if(!BuildConfig.BUILD_TYPE.equals("debug"))
            return;
        File writefile;
        try {
            {
                File LogPathObj = new File(LogPath);
                if (!LogPathObj.exists())
                    LogPathObj.mkdirs();
            }
            String path=LogPath+"/"+plugin.plugin_id+".log";
            writefile = new File(path);

            // 如果文本文件不存在则创建它
            if (!writefile.exists()) {
                writefile.createNewFile();
                writefile = new File(path); // 重新实例化
            }

            FileOutputStream fw = new FileOutputStream(writefile,true);
            Writer out = new OutputStreamWriter(fw, "utf-8");
            out.write(content);
            String newline = System.getProperty("line.separator");
            //写入换行
            out.write(newline);
            out.close();
            fw.flush();
            fw.close();
        } catch (Exception ex) {
        }
    }
}
