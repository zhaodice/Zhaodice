package com.zhao.dice.model.plugins;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.zhao.dice.model.AwLog;
import com.zhao.dice.model.R;

public class MainActivity extends Activity {
    LinearLayout mainLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.settings);
        AwLog.Log("Start MainActivity");
        Intent intent=getIntent();
        if(intent.hasExtra("showICO")) {
            boolean showico = intent.getBooleanExtra("showICO", true);
            showLauncherIcon(this, showico);
            finish();
        }
        mainLayout=findViewById(R.id.settings_layout);
        mainLayout.setEnabled(false);
        showAlterDialog();
        super.onCreate(savedInstanceState);
    }

    private void showAlterDialog(){
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(this);
        alterDiaglog.setTitle("本骰娘程序依赖修改版的TIM3.0.0!");//文字
        alterDiaglog.setMessage(this.getString(R.string.baseNotice_ERROR));//提示消息
        //积极的选择
        alterDiaglog.setPositiveButton("啊呜(民白)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });
        //显示
        alterDiaglog.show();
    }

    public static void showLauncherIcon(Context context,boolean isShow){
        PackageManager packageManager = context.getPackageManager();
        int show = isShow? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        packageManager.setComponentEnabledSetting(getAliseComponentName(context), show , PackageManager.DONT_KILL_APP);
    }
    private static ComponentName getAliseComponentName(Context context){
        return new ComponentName(context, "com.cocthulhu.eye.plugins.MainActivityAlias");
        // 在AndroidManifest.xml中为MainActivity定义了一个别名为MainActivity-Alias的activity，是默认启动activity、是点击桌面图标后默认程序入口
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
