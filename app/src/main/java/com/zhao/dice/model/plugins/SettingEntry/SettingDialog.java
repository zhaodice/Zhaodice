package com.zhao.dice.model.plugins.SettingEntry;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.ZhaoDiceUitl.COCHelper;
import com.zhao.dice.model.R;

import java.util.ArrayList;

public class SettingDialog extends Dialog {
    private String selfuin;
    private SharedPreferences sharedPreferences;
    private Switch switch_openDice;
    private Switch switch_voiceRobot;
    private Switch switch_handleMySelf;
    private Switch switch_keyAutoReply;

    private Spinner spinner_values;
    private EditText editText_editValue;
    private SentencesAdapter adapter;
    private Sentences_them current_sentences_them;

    static private ArrayList<Sentences_them> spinner_values_text = new ArrayList<>();
    static {
        if(spinner_values_text.size()==0) {
            spinner_values_text.add(new Sentences_them("WHITE_LIST", "（×）群白名单一行一个——清空全局有效"));
            spinner_values_text.add(new Sentences_them("PREFIX", "（×）指令前缀"));
            spinner_values_text.add(new Sentences_them("REPLY", "（×）模糊词回复\n一行一个 关键词/内容 例:\n赵怡然/天才!"));
            spinner_values_text.add(new Sentences_them("REPLY_EQU", "（×）匹配词回复\n一行一个 关键词/内容 例:\n赵怡然/天才!"));
            spinner_values_text.add(new Sentences_them("MASTER_INFO", "（master）骰主信息"));
            spinner_values_text.add(new Sentences_them("SENTENCE_DRAW_FAILURE", "（draw）牌堆抽取失败——牌堆找不到或出错"));
            spinner_values_text.add(new Sentences_them("SENTENCE_DRAW_SUCCESS", "（draw）牌堆抽取成功"));
            spinner_values_text.add(new Sentences_them("SENTENCE_DICE_DENIED", "（bot/robot）无权开关骰子"));
            spinner_values_text.add(new Sentences_them("SENTENCE_DICE_OPEN", "（bot/robot on）骰子被打开"));
            spinner_values_text.add(new Sentences_them("SENTENCE_DICE_CLOSE", "（bot/robot off）骰子被关闭"));
            spinner_values_text.add(new Sentences_them("SENTENCE_BIG_FAILURE", "（ra/rb/rp/sc）骰出大失败"));
            spinner_values_text.add(new Sentences_them("SENTENCE_FAILURE", "（ra/rb/rp/sc）骰出失败"));
            spinner_values_text.add(new Sentences_them("SENTENCE_BIG_SUCCESS", "（ra/rb/rp/sc）骰出大成功"));
            spinner_values_text.add(new Sentences_them("SENTENCE_VERY_HARD_SUCCESS", "（ra/rb/rp/sc）骰出极难成功"));
            spinner_values_text.add(new Sentences_them("SENTENCE_HARD_SUCCESS", "（ra/rb/rp/sc）骰出困难成功"));
            spinner_values_text.add(new Sentences_them("SENTENCE_SUCCESS", "（ra/rb/rp/sc）骰出成功"));
            spinner_values_text.add(new Sentences_them("SENTENCE_ILLEGAL_TOO_MUCH", "（×）非法操作，超出资源限制"));
            spinner_values_text.add(new Sentences_them("SENTENCE_ILLEGAL", "（×）非法操作，指令不合规"));
            spinner_values_text.add(new Sentences_them("SENTENCE_ROLL", "（r）骰点"));
            spinner_values_text.add(new Sentences_them("SENTENCE_CHANGE_NAME", "（nn）修改名字成功"));
            spinner_values_text.add(new Sentences_them("SENTENCE_CHANGE_CARD", "（nn）设置现存档位成功"));
            spinner_values_text.add(new Sentences_them("SENTENCE_GET_PAYER_INFO", "（stshow）获取玩家属性"));
            spinner_values_text.add(new Sentences_them("SENTENCE_SET_PAYER_INFO", "（st）设置玩家属性"));
            spinner_values_text.add(new Sentences_them("SENTENCE_JRRP", "（jrrp）今日人品"));
            spinner_values_text.add(new Sentences_them("SENTENCE_PROMOTION_SUCCESS", "（en）技能成长鉴定成功"));
            spinner_values_text.add(new Sentences_them("SENTENCE_PROMOTION_FAILURE", "（en）技能成长鉴定失败"));
        }
    }
    static void OpenDialog(final Context context, String selfuin){
        SettingDialog dialog=new SettingDialog(context,selfuin);
        dialog.show();
    }
    private SettingDialog(@NonNull Context context,String selfuin) {
        super(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        this.selfuin=selfuin;
        sharedPreferences=context.getSharedPreferences(ConfigReader.CONFIG_NAME,Context.MODE_PRIVATE);
    }
    private void readConfigToUI(){
        switch_openDice.setChecked(sharedPreferences.getBoolean(ConfigReader.CONFIG_KEY_SWITCH_DICE,false));
        switch_voiceRobot.setChecked(sharedPreferences.getBoolean(ConfigReader.CONFIG_KEY_SWITCH_VOICE_ROBOT,false));
        switch_handleMySelf.setChecked(sharedPreferences.getBoolean(ConfigReader.CONFIG_KEY_SWITCH_HANDLE_MYSELF,false));
        switch_keyAutoReply.setChecked(sharedPreferences.getBoolean(ConfigReader.CONFIG_KEY_SWITCH_KEY_AUTO_REPLY,false));
    }
    private void saveConfigFromUI(){
        SharedPreferences.Editor sharedPreferencesEditor=sharedPreferences.edit();
        sharedPreferencesEditor.putBoolean(ConfigReader.CONFIG_KEY_SWITCH_DICE,switch_openDice.isChecked());
        sharedPreferencesEditor.putBoolean(ConfigReader.CONFIG_KEY_SWITCH_VOICE_ROBOT,switch_voiceRobot.isChecked());
        sharedPreferencesEditor.putBoolean(ConfigReader.CONFIG_KEY_SWITCH_HANDLE_MYSELF,switch_handleMySelf.isChecked());
        sharedPreferencesEditor.putBoolean(ConfigReader.CONFIG_KEY_SWITCH_KEY_AUTO_REPLY,switch_keyAutoReply.isChecked());
        sharedPreferencesEditor.apply();
        if(current_sentences_them!=null)
            COCHelper.helper_storage.saveGlobalInfo(selfuin,current_sentences_them.tag,editText_editValue.getText().toString());
    }
    @Override
    protected void onStop() {
        saveConfigFromUI();
        super.onStop();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context remoteContext=ConfigReader.getRemoteContext(getContext());
        //Context thisContext=getContext();
        View view = View.inflate(remoteContext, R.layout.settings,null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        //getContext().setTheme(AlertDialog.THEME_HOLO_LIGHT);
        switch_openDice=findViewById(R.id.switch_openDice);
        switch_voiceRobot=findViewById(R.id.switch_voiceRobot);
        switch_handleMySelf=findViewById(R.id.switch_handleMySelf);
        switch_keyAutoReply=findViewById(R.id.switch_keyAutoReply);
        spinner_values=findViewById(R.id.spinner_values);
        editText_editValue=findViewById(R.id.editText_editValue);
        adapter = new SentencesAdapter(remoteContext,spinner_values_text);
        //adapter.setDropDownViewTheme(Resources.Theme.);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_values.setAdapter(adapter);
        spinner_values.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                COCHelper.helper_storage.setGlobalInfoDefault();
                editText_editValue.setText(COCHelper.helper_storage.getGlobalInfo(selfuin,current_sentences_them.tag));
                Toast.makeText(getContext(),"当前设置已经设置为默认值！",Toast.LENGTH_LONG).show();
                return false;
            }
        });
        spinner_values.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saveConfigFromUI();
                current_sentences_them=adapter.getItem(position);
                String sentence= COCHelper.helper_storage.getGlobalInfo(selfuin,current_sentences_them.tag);
                editText_editValue.setText(sentence);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //spinner_values.setBackgroundColor(Color.rgb(255,255,255));

        readConfigToUI();
        super.onCreate(savedInstanceState);
    }
}