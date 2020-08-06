package com.zhao.dice.model.plugins.SettingEntry;

import androidx.annotation.NonNull;

public class Sentences_them {
    String tag;
    String tag_view;
    Sentences_them(String tag,String tag_view){
        this.tag=tag;
        this.tag_view=tag_view;
    }
    @NonNull
    @Override
    public String toString() {
        return tag_view;
    }
}
