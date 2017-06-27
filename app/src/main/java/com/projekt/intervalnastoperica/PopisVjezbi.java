package com.projekt.intervalnastoperica;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.Button;
import com.gc.materialdesign.views.ButtonFloat;

import java.util.ArrayList;

public class PopisVjezbi extends Activity {

    Context ctx;
    ArrayList<VjezbaClass> listData;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popis_vjezbi);

        ctx = this;
        listData = new ArrayList<>();
        final TextView imeVal = (TextView) findViewById(R.id.title);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_return);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PopisVjezbi.this, Main.class);
                startActivity(intent);
            }
        });
        imeVal.setText("Popis vjezbi");

        getData();

        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(new PopisVjezbiAdapter(ctx, listData));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(PopisVjezbi.this, PostavkeVjezbe.class);
                i.putExtra("id", listData.get(position).getID());
                i.putExtra("ime", listData.get(position).getIme());
                i.putExtra("priprema", listData.get(position).getPriprema());
                i.putExtra("vjezba", listData.get(position).getVjezba());
                i.putExtra("odmor", listData.get(position).getOdmor());
                i.putExtra("ponavljanja", listData.get(position).getPonavljanja());
                i.putExtra("setovi", listData.get(position).getSetovi());
                i.putExtra("isEdit", true);
                startActivity(i);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                otvoriDialog("Zelite li obrisati ovu vjezbu?", position);
                return true;
            }
        });

        ButtonFloat dodajVjezbu = (ButtonFloat) findViewById(R.id.buttonFloat);
        dodajVjezbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PopisVjezbi.this, PostavkeVjezbe.class);
                startActivity(i);
            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(PopisVjezbi.this, Main.class);
        startActivity(intent);
    }

    private void otvoriDialog(String text,final int position){

        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.yesnodialog);
        dialog.show();

        final TextView naslov = (TextView) dialog.findViewById(R.id.tekst);
        naslov.setText(text);

        final Button cancel = (Button) dialog.findViewById(R.id.yes);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BazaFunkcije bf;
                    bf = new BazaFunkcije(ctx);
                    bf.open();
                    bf.IzvrsiUpit("DELETE from Vjezbe where id='" + listData.get(position).getID() + "'");
                }catch (Exception e){
                    Toast.makeText(ctx, "Došlo je do pogreške! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                getData();
                refreshList();
                dialog.dismiss();
            }
        });

        final Button save = (Button) dialog.findViewById(R.id.no);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });
    }

    private void getData(){

        try {
            listData.clear();

            SQLiteDatabase database;
            BazaPodataka dbHelper = BazaPodataka.getInstance(getApplicationContext());
            database = dbHelper.getWritableDatabase();
            Cursor cursor = database.rawQuery("SELECT ime, priprema, vjezba, odmor, ponavljanja, setovi, id from Vjezbe order by " +
                    "id asc", null);
            if(cursor != null){
                if(cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        VjezbaClass c = new VjezbaClass();
                        String ime = cursor.getString(0);
                        int priprema = cursor.getInt(1);
                        int vjezba = cursor.getInt(2);
                        int odmor = cursor.getInt(3);
                        int ponavljanja = cursor.getInt(4);
                        int setovi = cursor.getInt(5);
                        int id = cursor.getInt(6);

                        c.setID(id);
                        c.setIme(ime);
                        c.setPriprema(priprema);
                        c.setVjezba(vjezba);
                        c.setOdmor(odmor);
                        c.setPonavljanja(ponavljanja);
                        c.setSetovi(setovi);
                        listData.add(c);

                        cursor.moveToNext();
                    }
                }
                cursor.close();
            }

        }catch (Exception e){
            e.getMessage();
        }

    }

    private void refreshList(){
        PopisVjezbiAdapter a = (PopisVjezbiAdapter) lv.getAdapter();
        a.notifyDataSetChanged();
        int index = lv.getFirstVisiblePosition();
        View v = lv.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - lv.getPaddingTop());
        lv.setSelectionFromTop(index, top);
    }

}
