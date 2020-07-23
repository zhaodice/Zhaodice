package com.ZhaoDiceUitl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.zhao.dice.model.AwLog;
import com.zhao.dice.model.BuildConfig;

import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

//https://github.com/liangchenhe55/Android-Lua
public class LuaPlugin {

    private final Context context;
    LuaPluginManager luaPluginManager;
    public LuaState lua;
    final String filename;
    int init_code;
    //plugin_description=插件描述
    //plugin_name=插件名称
    //plugin_version=插件版本
    //support_version=支持版本（本程序的版本）
    String plugin_description,plugin_name,plugin_id;
    int plugin_version,support_version;
    ArrayList<String> permissions =new ArrayList<>();
    //SUPPORTED PERMISSIONS:
    // MESSAGE_HANDLE 消息处理 event_message_handle(int_is_troop,string_id_qq,string_id_group,string_message)
    // MESSAGE_SEND 消息发送 send_message(int_is_troop,string_id_qq,string_id_group,string_message)

    LuaPlugin(LuaPluginManager luaPluginManager, Context context, String filename){
        this.context=context;
        this.luaPluginManager=luaPluginManager;
        this.filename=filename;
        this.lua = LuaStateFactory.newLuaState(); //创建栈
        this.lua.openLibs(); //加载标准库

        //注入变量信息
        setGlobalString("APP_VERSION_NAME", BuildConfig.VERSION_NAME);
        setGlobalInt("APP_VERSION_CODE", BuildConfig.VERSION_CODE);
        setGlobalString("APP_DATA_PATH", COCHelper.helper_storage.storage_save_path);//数据存储目录
        //注入权限申请函数
        new LuaPluginPermissionRequiredFunction(lua).register();
        //注入调试函数
        new LuaLogFunction(lua).register();

        //初始化Lua文件
        try {
            this.init_code = this.lua.LdoFile(filename);//载入文件
            if(this.init_code!=0){
                luaPluginManager.writeLog(this,lua.toString(-1));
                return;
            }
            this.support_version = readGlobalInt("support_version");
            if (this.support_version > BuildConfig.VERSION_CODE) {
                //版本错误，无法加载插件
                luaPluginManager.writeLog(this,"ERROR!support_version>"+BuildConfig.VERSION_CODE);
                this.init_code = -1;
                return;
            }

            this.plugin_id = readGlobalString("plugin_id");
            this.plugin_name = readGlobalString("plugin_name");
            this.plugin_description = readGlobalString("plugin_description");
            this.plugin_version = readGlobalInt("plugin_version");
            if (this.support_version==0 || this.plugin_id==null || this.plugin_name == null || this.plugin_description == null || this.plugin_version == 0) {
                //信息缺失，无法加载插件
                AwLog.Log("信息缺失，无法加载插件");
                luaPluginManager.writeLog(this,"ERROR!need support_version,plugin_id,plugin_name,plugin_description,plugin_version");
                this.init_code = -1;
                return;
            }
            if(luaPluginManager.hasPluginId(this.plugin_id)){
                //同一个插件ID已存在,无法加载
                luaPluginManager.writeLog(this,"ERROR! Same plugin id exists.");
                this.init_code = -1;
            }
        }catch (Throwable e){
            //程序错误，无法加载插件
            luaPluginManager.writeLog(this,"ERROR!"+e.getMessage());
            this.init_code = -1;
        }
    }
    class LuaPluginPermissionRequiredFunction extends JavaFunction {
        /**
         * Constructor that receives a LuaState.
         *
         * @param L LuaState object associated with this JavaFunction object
         */
        LuaPluginPermissionRequiredFunction(LuaState L) {
            super(L);
        }
        @Override
        public int execute() throws LuaException {
            // 获取Lua传入的参数，注意第一个参数固定为上下文环境。
            AwLog.Log("插件试图申请权限...");
            String str = L.toString(2);
            if(str==null)
                return 0;//权限不能为null
            if(hasPermission(str))
                return 0;//权限申请过了
            permissions.add(str);
            luaPluginManager.APIregister.registerAPIByPermission(L,str);//根据权限注册函数
            return 0; // 返回值的个数
        }
        public void register() {
            try {
                // 注册为 Lua 全局函数
                //register("PermissionRequired");
                register("PermissionRequired");
            } catch (LuaException e) {
                e.printStackTrace();
            }
        }
    }
    class LuaLogFunction extends JavaFunction {
        /**
         * Constructor that receives a LuaState.
         *
         * @param L LuaState object associated with this JavaFunction object
         */
        LuaLogFunction(LuaState L) {
            super(L);
        }
        @Override
        public int execute() throws LuaException {
            // 获取Lua传入的参数，注意第一个参数固定为上下文环境。
            String tag = L.toString(2);
            String content = L.toString(3);
            luaPluginManager.writeLog(LuaPlugin.this,"["+tag+"]"+content);
            return 0; // 返回值的个数
        }
        public void register() {
            try {
                // 注册为 Lua 全局函数
                //register("PermissionRequired");
                register("Log");
            } catch (LuaException e) {
                e.printStackTrace();
            }
        }
    }

    private String readGlobalString(String v){
        this.lua.getGlobal(v);
        String s = lua.toString(-1);
        lua.pop(1);
        return s;
    }
    private int readGlobalInt(String v){
        this.lua.getGlobal(v);
        int i = lua.toInteger(-1);
        lua.pop(1);
        return i;
    }
    private void setGlobalString(String v,String value){
        lua.pushString(value); //压入欲注入变量的值
        lua.setGlobal(v); // 设置变量名
    }
    private void setGlobalInt(String v,int value){
        lua.pushInteger(value); //压入欲注入变量的值
        lua.setGlobal(v); // 设置变量名
    }



    public boolean hasPermission(String permission){
        for(String _permission : permissions){
            if(permission.equals(_permission)){//权限已经申请过了
                return true;
            }
        }
        return false;
    }
    public void close(){
        this.lua.close(); //养成良好习惯，在执行完毕后销毁Lua栈。
    }
}
