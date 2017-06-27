package com.projekt.intervalnastoperica;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;
import com.gc.materialdesign.views.ButtonFloat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PopisVjezbiAdapter extends BaseAdapter {

    private Context ctx;
    private ArrayList<VjezbaClass> listData;
    private LayoutInflater layoutInflater;

    public PopisVjezbiAdapter(Context aContext, ArrayList<VjezbaClass> listData) {

        layoutInflater = LayoutInflater.from(aContext);
        this.ctx = aContext;
        this.listData = listData;

    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.vjezbe_row, null);
            holder = new ViewHolder();
            holder.rb = (TextView) convertView.findViewById(R.id.rowBroj);
            holder.ime = (TextView) convertView.findViewById(R.id.rowIme);
            holder.trajanje = (TextView) convertView.findViewById(R.id.rowTrajanje);
            holder.info = (ButtonFloat) convertView.findViewById(R.id.buttonInfo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.rb.setText(String.valueOf(position+1));
        holder.ime.setText(listData.get(position).getIme());
        holder.trajanje.setText(pretvoriUDatum(listData.get(position).getTrajanje()));
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otvoriDialog(listData.get(position).getIme(), listData.get(position).getID());
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView rb;
        TextView ime;
        TextView trajanje;
        ButtonFloat info;
    }

    private String pretvoriUDatum(int sekunde){
        Date date = new Date(sekunde*1000);
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        String formatted = formatter.format(date);
        return formatted;
    }

    private void otvoriDialog(String ime, int id){

        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.infodialog);
        dialog.show();

        final TextView naslov = (TextView) dialog.findViewById(R.id.naslov);
        naslov.setText(ime);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        String time = preferences.getString(id + "IS", "Niste još vježbali ovu vježbu!");
        final TextView vrijeme = (TextView) dialog.findViewById(R.id.vrijeme);
        vrijeme.setText(time);

        final Button save = (Button) dialog.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

}