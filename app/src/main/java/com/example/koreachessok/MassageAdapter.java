package com.example.koreachessok;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MassageAdapter extends BaseAdapter {
    ArrayList<Massage> massages;
    LayoutInflater inflater;
    String userNickname;

    MassageAdapter(ArrayList<Massage> ms, LayoutInflater li, String nick){
        massages = ms;
        inflater = li;
        userNickname = nick;
    }

    @Override
    public int getCount() {
        return massages.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Massage massage = massages.get(i);
        View itemView;

        if(massage.nickname.equals(userNickname)){
            itemView = inflater.inflate(R.layout.mymassage_layout, viewGroup,false);
            TextView tvNick = itemView.findViewById(R.id.myMassageNickName);
            TextView tvDate = itemView.findViewById(R.id.myMassageDate);
            TextView tvTime = itemView.findViewById(R.id.myMassageTime);
            TextView tvMsg = itemView.findViewById(R.id.myMassge);
            tvNick.setText(massage.getNickname());
            tvDate.setText(massage.getDate());
            tvTime.setText(massage.getTime());
            tvMsg.setText(massage.getMsg() + "           ");
        }else{
            itemView = inflater.inflate(R.layout.othermassage_layout,viewGroup,false);
            TextView tvNick = itemView.findViewById(R.id.OtherMassageNickName);
            TextView tvDate = itemView.findViewById(R.id.OtherMassageDate);
            TextView tvTime = itemView.findViewById(R.id.otherMassageTime);
            TextView tvMsg = itemView.findViewById(R.id.OtherMassage);
            tvNick.setText(massage.getNickname());
            tvDate.setText(massage.getDate());
            tvTime.setText(massage.getTime());
            tvMsg.setText("           " + massage.getMsg());
        }

        return itemView;
    }
}
