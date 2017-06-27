package com.projekt.intervalnastoperica;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.gc.materialdesign.views.Button;
import com.shawnlin.numberpicker.NumberPicker;



public class PostavkeVjezbe extends Activity {

    Context ctx;
    boolean isEdit;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postavke_vjezbe);

        i = getIntent();

        LinearLayout priprema = (LinearLayout) findViewById(R.id.layout1);
        LinearLayout vjezba = (LinearLayout) findViewById(R.id.layout2);
        LinearLayout ponavljanja = (LinearLayout) findViewById(R.id.layout3);
        LinearLayout odmor = (LinearLayout) findViewById(R.id.layout4);
        LinearLayout setovi = (LinearLayout) findViewById(R.id.layout5);


        final TextView imeVal = (TextView) findViewById(R.id.title);
        final TextView pripremaVal = (TextView) findViewById(R.id.pripremaVal);
        final TextView vjezbaVal = (TextView) findViewById(R.id.vjezbaVal);
        final TextView ponavljanjaVal = (TextView) findViewById(R.id.ponavljanjaVal);
        final TextView odmorVal = (TextView) findViewById(R.id.odmorVal);
        final TextView setoviVal = (TextView) findViewById(R.id.setoviVal);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_return);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PostavkeVjezbe.this, PopisVjezbi.class);
                startActivity(intent);
            }
        });
        toolbar.inflateMenu(R.menu.toolbar_menu_save);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_save:

                        if(isEdit){

                            String ime = imeVal.getText().toString();
                            if(ime.length()>0){
                                try {
                                    int id = i.getIntExtra("id", 0);
                                    BazaFunkcije bf;
                                    bf = new BazaFunkcije(ctx);
                                    bf.open();
                                    bf.IzvrsiUpit("UPDATE Vjezbe set ime='" + ime + "', priprema='" + Integer.parseInt(pripremaVal.getText().toString()) + "', vjezba='" + Integer.parseInt(vjezbaVal.getText().toString()) + "', odmor='" + Integer.parseInt(odmorVal.getText().toString()) + "', ponavljanja='" + Integer.parseInt(ponavljanjaVal.getText().toString()) + "', setovi='" + Integer.parseInt(setoviVal.getText().toString()) + "' where id='" + id + "'");

                                    finish();
                                    Intent intent = new Intent(PostavkeVjezbe.this, PopisVjezbi.class);
                                    startActivity(intent);
                                }catch (Exception e){
                                    Toast.makeText(ctx, "Došlo je do pogreške! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(ctx, "Unesite ime vjezbe!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            String ime = imeVal.getText().toString();
                            if(ime.length()>0){
                                try {
                                    BazaFunkcije bf;
                                    bf = new BazaFunkcije(ctx);
                                    bf.open();
                                    bf.IzvrsiUpit("INSERT INTO Vjezbe (ime, priprema, vjezba, odmor, ponavljanja, setovi) values ('" + ime + "','" + Integer.parseInt(pripremaVal.getText().toString()) + "','" + Integer.parseInt(vjezbaVal.getText().toString()) +  "','" + Integer.parseInt(odmorVal.getText().toString()) + "','" + Integer.parseInt(ponavljanjaVal.getText().toString()) + "','" + Integer.parseInt(setoviVal.getText().toString()) + "')");

                                    finish();
                                    Intent intent = new Intent(PostavkeVjezbe.this, PopisVjezbi.class);
                                    startActivity(intent);
                                }catch (Exception e){
                                    Toast.makeText(ctx, "Došlo je do pogreške! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(ctx, "Unesite ime vjezbe!", Toast.LENGTH_SHORT).show();
                            }
                        }



                        break;
                }

                return false;
            }
        });

        isEdit = i.getBooleanExtra("isEdit", false);
        if(isEdit){
            imeVal.setText(i.getStringExtra("ime"));
            pripremaVal.setText(String.valueOf(i.getIntExtra("priprema", 0)));
            vjezbaVal.setText(String.valueOf(i.getIntExtra("vjezba", 0)));
            ponavljanjaVal.setText(String.valueOf(i.getIntExtra("ponavljanja", 0)));
            odmorVal.setText(String.valueOf(i.getIntExtra("odmor", 0)));
            setoviVal.setText(String.valueOf(i.getIntExtra("setovi", 0)));
        }else{
            imeVal.setText("Nova vjezba");
        }




        ctx = this;


        priprema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                otvoriDialog("Priprema", pripremaVal, 0, 150);
            }
        });

        vjezba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                otvoriDialog("Vjezba", vjezbaVal, 0, 150);
            }
        });

        ponavljanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                otvoriDialog("Ponavljanja", ponavljanjaVal, 0, 10);
            }
        });

        odmor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                otvoriDialog("Odmor", odmorVal, 0, 150);
            }
        });

        setovi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                otvoriDialog("Setovi", setoviVal, 0, 10);
            }
        });
    }

    private void otvoriDialog(String ime, final TextView polje, int min, int max){

        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.spinnerdialog);
        dialog.show();

        final TextView naslov = (TextView) dialog.findViewById(R.id.naslov);
        naslov.setText(ime);

        final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.number_picker);
        numberPicker.setMinValue(min);
        numberPicker.setMaxValue(max);
        numberPicker.setValue(Integer.parseInt(polje.getText().toString()));

        final Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        final Button save = (Button) dialog.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                polje.setText(String.valueOf(numberPicker.getValue()));
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(PostavkeVjezbe.this, PopisVjezbi.class);
        startActivity(intent);
    }

}
