package com.zhao.dice.model.plugins.SettingEntry;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.ZhaoDiceUitl.COCHelper;
import com.zhao.dice.model.Adaptation;
import com.zhao.dice.model.AwLog;
import com.zhao.dice.model.BuildConfig;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import static android.content.Context.CONTEXT_IGNORE_SECURITY;
import static android.content.Context.CONTEXT_INCLUDE_CODE;

public class ConfigReader {
    public final static String CONFIG_KEY_SWITCH_DICE="openDice";
    public final static String CONFIG_KEY_SWITCH_VOICE_ROBOT="voiceRobot";
    public final static String CONFIG_KEY_SWITCH_HANDLE_MYSELF="handleMySelf";
    public final static String CONFIG_KEY_SWITCH_KEY_AUTO_REPLY="keyAutoReply";

    public final static String CONFIG_NAME="dice_model_settings";
    public final static String PATH_SOUND_HUMAN =COCHelper.helper_storage.storage_save_path+"/sound_human";//语音（如群里有人发 #{VOICE-the flower of hope.amr}
    public final static String PATH_SOUND_ROBOT =COCHelper.helper_storage.storage_save_path+"/sound_robot";//语音(如机器人发 #{VOICE-the flower of hope.amr}
    public final static File PATH_DRAW=new File(COCHelper.helper_storage.storage_save_path+"/draw");//牌堆文件夹


    //public final static String SOUND_FLOWER_OF_HOPE=SOUND_PATH+"/the flower of hope.amr";
    //public final static String SOUND_RUNNER=SOUND_PATH+"/Runner.amr";
    public static Context getRemoteContext(Context context){
        try {
            return context.createPackageContext(BuildConfig.APPLICATION_ID, CONTEXT_INCLUDE_CODE | CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static SharedPreferences getSharedPreferences(Adaptation adaptation){
        return getSharedPreferences(adaptation.context);
    }
    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(CONFIG_NAME,Context.MODE_PRIVATE);
    }
    public static String readString(Adaptation adaptation, String key){
        return readString(adaptation,key,null);
    }
    public static boolean readBoolean(Adaptation adaptation, String key){
        return readBoolean(adaptation,key,false);
    }
    public static long readLong(Adaptation adaptation, String key){
        return readLong(adaptation,key,0);
    }
    public static String readString(Adaptation adaptation, String key, String def){
        return adaptation.sharedPreferences.getString(key,def);
    }
    public static boolean readBoolean(Adaptation adaptation, String key, boolean def){
        return adaptation.sharedPreferences.getBoolean(key,def);
    }
    public static long readLong(Adaptation adaptation, String key, long def){
        return adaptation.sharedPreferences.getLong(key,def);
    }

    public static String readMethodsJSON(Adaptation adaptation){
        Context remoteContext = null;
        //AwLog.Log("readMethodsJSON.."+adaptation.QQpackagename);
        try {
            remoteContext = getRemoteContext(adaptation.context);
            InputStream in=remoteContext.getResources().getAssets().open(adaptation.QQpackagename+"_methods.json");
            BufferedReader inr=new BufferedReader(new InputStreamReader(in));
            String line,s="";
            while((line=inr.readLine())!=null){
                s+=line;
            }
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void initDataFiles(Context context){
        //AwLog.Log("initAssets : ->"+SOUND_FLOWER_OF_HOPE);
        CopyAssets(context, PATH_SOUND_ROBOT,"the flower of hope.amr");
        CopyAssets(context, PATH_SOUND_ROBOT,"Runner.amr");
        new File(PATH_SOUND_HUMAN).mkdirs();
        PATH_DRAW.mkdirs();
        CopyAssets(context, PATH_DRAW.getPath(),"default_draw.json");
    }
    private static void CopyAssets(Context context, String dir, String fileName){
        //String[] files;
        File mWorkingPath = new File(dir);
        if (!mWorkingPath.exists()) {
            if (!mWorkingPath.mkdirs()) {
                AwLog.Log( "cannot create directory.");
            }
        }
        try {
            File outFile = new File(mWorkingPath, fileName);
            if(outFile.exists())
                return;
            InputStream in = getRemoteContext(context).getAssets().open(fileName);
            System.err.println("");
            OutputStream out = new FileOutputStream(outFile);
            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
