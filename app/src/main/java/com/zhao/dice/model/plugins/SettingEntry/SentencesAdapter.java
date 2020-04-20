package com.zhao.dice.model.plugins.SettingEntry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhao.dice.model.R;

import java.util.ArrayList;

public class SentencesAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater li;
    private ArrayList<Sentences_them> dataList;

    public SentencesAdapter(Context ctx,ArrayList<Sentences_them> dataList) {
        this.ctx = ctx;
        this.li = LayoutInflater.from(ctx);
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Sentences_them getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.sentences_them, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();// get convertView's holder

        holder.sentence_name.setText(getItem(position).tag_view);
        return convertView;
    }
    class ViewHolder {
        TextView sentence_name;
        public ViewHolder(View convertView){
            sentence_name = convertView.findViewById(R.id.sentence_name);
            convertView.setTag(this);//set a viewholder
        }
    }
}

