package com.daveanthonythomas.myapplication;

import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dave on 2016-09-08.
 */
public class CustomAdapter extends BaseAdapter {



    ArrayList<Memo> memos = new ArrayList<>();

    public void add(Memo memo) {
        this.memos.add(memo);
    }

    public void delete(int position) {
        memos.remove(position);
    }

    @Override
    public Memo getItem(int position) {
        return memos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return memos.size();
    }

    class MyViewHolder {
        public TextView header, bodyText;
        public ImageView imageView;

        public MyViewHolder(View view) {
            header = (TextView) view.findViewById(R.id.header);
            bodyText = (TextView) view.findViewById(R.id.bodyText);
            imageView = (ImageView) view.findViewById(R.id.primeImage);

        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        MyViewHolder viewHolder;
        if(null == convertView){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.custom_row, parent, false);
            viewHolder = new MyViewHolder(convertView);
            viewHolder.header.setTag(position);

            convertView.setTag(viewHolder);

        }
        else{
            viewHolder = (MyViewHolder) convertView.getTag();
        }

        Memo memo = getItem(position);
        viewHolder.header.setText(memo.header);
        viewHolder.bodyText.setText(memo.bodyText);
        if (memo.imageView.getDrawable() instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) memo.imageView.getDrawable();
            viewHolder.imageView.setImageBitmap(bitmapDrawable.getBitmap());
        }

        return convertView;
    }


}
