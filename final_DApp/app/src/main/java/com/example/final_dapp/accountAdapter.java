package com.example.final_dapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class accountAdapter extends BaseAdapter {

    /* 아이템을 세트로 담기 위한 어레이 */
    private List<accountInfo> mData = new ArrayList<>();

    public accountAdapter() {}


    public accountAdapter(List<accountInfo> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_account, parent, false);
        }

        TextView date =  convertView.findViewById(R.id.date) ;
        TextView cost =  convertView.findViewById(R.id.cost) ;
        TextView place =  convertView.findViewById(R.id.place) ;
        TextView type =  convertView.findViewById(R.id.type) ;

        accountInfo info = mData.get(position);
        date.setText(info.getDate());
        cost.setText(info.getCost());
        place.setText(info.getPlace());
        type.setText(info.getType());
        return convertView;
    }

    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem(String num, String date, String cost, String place, String type) {

        accountInfo mItem = new accountInfo();

        /* MyItem에 아이템을 setting한다. */
        mItem.setNum(num);
        mItem.setDate(date);
        mItem.setCost(cost);
        mItem.setPlace(place);
        mItem.setType(type);

        /* mItems에 MyItem을 추가한다. */
        mData.add(0, mItem);

    }
}
