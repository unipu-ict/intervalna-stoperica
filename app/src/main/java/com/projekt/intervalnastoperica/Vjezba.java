package com.projekt.intervalnastoperica;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.Button;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import java.text.DateFormat;
import java.util.Date;

import at.grabner.circleprogress.CircleProgressView;
import mehdi.sakout.fancybuttons.FancyButton;

public class Vjezba extends AppCompatActivity {

    Intent i;
    Context ctx;
    CountDownTimer ct;
    Intent test;
    CircleProgressView progress;
    TextView timer;
    TextView status;
    FancyButton playpause;
    int start = -1;
    long pauseVal;
    int pauseStatus;
    long aaa;

    int def_priprema;
    int def_vjezba;
    int def_odmor;
    int def_ponavljanja;
    int def_setovi;

    int gotova_ponavljanja = 0;
    int gotovi_setovi = 0;

    float trajanjePriprema = 0;
    float trajanjeVjezba = 0;
    float trajanjeOdmor = 0;
    float trajanjeUkupno = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vjezba);

        i = getIntent();
        ctx = this;

        def_priprema = i.getIntExtra("priprema", 0);
        def_vjezba = i.getIntExtra("vjezba", 0);
        def_odmor = i.getIntExtra("odmor", 0);
        def_ponavljanja = i.getIntExtra("ponavljanja", 0);
        def_setovi = i.getIntExtra("setovi", 0);

        status = (TextView) findViewById(R.id.status);
        timer = (TextView) findViewById(R.id.timer);
        final TextView imeVal = (TextView) findViewById(R.id.title);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_return);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Vjezba.this, OdaberiVjezbu.class);
                startActivity(intent);
            }
        });
        imeVal.setText(i.getStringExtra("ime"));

        progress = (CircleProgressView) findViewById(R.id.circleView);
        progress.setMaxValue(100);
        progress.setValue(100);
        progress.setBarColor(ContextCompat.getColor(ctx, R.color.orange));

        final FancyButton next = (FancyButton) findViewById(R.id.buttonNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ct != null) {
                    ct.cancel();
                    timerResume(1);
                }
            }
        });

        playpause = (FancyButton) findViewById(R.id.buttonPlayPause);
        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start == -1){ //start
                    zapocniPripremu();
                    start = 0;
                    playpause.setIconResource(R.drawable.pause);
                    trajanjePriprema = 0;
                    trajanjeVjezba = 0;
                    trajanjeOdmor = 0;
                    trajanjeUkupno = 0;
                }else if(start == 0){ //pause
                    ct.cancel();
                    start = 1;
                    playpause.setIconResource(R.drawable.play);
                }else{ //resume
                    timerResume(pauseVal);
                    start = 0;
                    playpause.setIconResource(R.drawable.pause);
                }

            }
        });



    }

    private void zapocniPripremu(){

        pauseStatus = 0;
        progress.setMaxValue(def_priprema);
        progress.setValue(def_priprema);
        progress.setBarColor(ContextCompat.getColor(ctx, R.color.orange));
        status.setText("Priprema");

        ct = new CountDownTimer(def_priprema * 1000 + 500, 1000){
            public void onTick(long millisUntilFinished){
                pauseVal = millisUntilFinished;
                trajanjePriprema = trajanjePriprema + 1000;
                trajanjeUkupno = trajanjeUkupno + 1000;
                timer.setText(String.valueOf(millisUntilFinished/1000));
                progress.setValue(millisUntilFinished / 1000);
            }
            public  void onFinish(){
                zapocniVjezbu();
            }
        }.start();

    }

    private void zapocniVjezbu(){

        pauseStatus = 1;
        progress.setMaxValue(def_vjezba);
        progress.setValue(100);
        progress.setBarColor(ContextCompat.getColor(ctx, R.color.prva));
        status.setText("Vježba");

        ct = new CountDownTimer(def_vjezba * 1000 + 500, 1000){
            public void onTick(long millisUntilFinished){
                trajanjeVjezba = trajanjeVjezba + 1000;
                trajanjeUkupno = trajanjeUkupno + 1000;
                pauseVal = millisUntilFinished;
                timer.setText(String.valueOf(millisUntilFinished/1000));
                progress.setValue(millisUntilFinished / 1000);
            }
            public  void onFinish(){
                zapocniOdmor();
            }
        }.start();

    }

    private void zapocniOdmor(){

        pauseStatus = 2;
        progress.setMaxValue(def_odmor);
        progress.setValue(def_odmor);
        progress.setBarColor(ContextCompat.getColor(ctx, R.color.red));
        status.setText("Odmor");

        ct = new CountDownTimer(def_odmor * 1000 + 500, 1000){
            public void onTick(long millisUntilFinished){
                pauseVal = millisUntilFinished;
                trajanjeOdmor = trajanjeOdmor + 1000;
                trajanjeUkupno = trajanjeUkupno + 1000;
                timer.setText(String.valueOf(millisUntilFinished/1000));
                progress.setValue(millisUntilFinished / 1000);
            }
            public  void onFinish(){
                gotova_ponavljanja++;
                if(gotova_ponavljanja < def_ponavljanja){
                    zapocniVjezbu();
                }else{
                    gotovi_setovi++;
                    if(gotovi_setovi < def_setovi){
                        gotova_ponavljanja = 0;
                        zapocniPripremu();
                    }else {
                        //gotova vjezba
                        reset();
                        spremiVrijeme();
                        otvoriDialog();
                    }
                }
            }
        }.start();
    }

    void timerResume(long val){
        if(pauseStatus == 0) {
            ct = new CountDownTimer(val, 1000){
                public void onTick(long millisUntilFinished){
                    pauseVal = millisUntilFinished;
                    trajanjePriprema = trajanjePriprema + 1000;
                    trajanjeUkupno = trajanjeUkupno + 1000;
                    timer.setText(String.valueOf(millisUntilFinished/1000));
                    progress.setValue(millisUntilFinished / 1000);
                }
                public  void onFinish(){
                    zapocniVjezbu();
                }
            }.start();
        }else if (pauseStatus == 1) {
            ct = new CountDownTimer(val, 1000){
                public void onTick(long millisUntilFinished){
                    pauseVal = millisUntilFinished;
                    trajanjeVjezba = trajanjeVjezba + 1000;
                    trajanjeUkupno = trajanjeUkupno + 1000;
                    timer.setText(String.valueOf(millisUntilFinished/1000));
                    progress.setValue(millisUntilFinished / 1000);
                }
                public  void onFinish(){
                    zapocniOdmor();
                }
            }.start();
        }else {
            ct = new CountDownTimer(val, 1000){
                public void onTick(long millisUntilFinished){
                    pauseVal = millisUntilFinished;
                    trajanjeOdmor = trajanjeOdmor + 1000;
                    trajanjeUkupno = trajanjeUkupno + 1000;
                    timer.setText(String.valueOf(millisUntilFinished/1000));
                    progress.setValue(millisUntilFinished / 1000);
                }
                public  void onFinish(){
                    gotova_ponavljanja++;
                    if(gotova_ponavljanja < def_ponavljanja){
                        zapocniVjezbu();
                    }else{
                        gotovi_setovi++;
                        if(gotovi_setovi < def_setovi){
                            gotova_ponavljanja = 0;
                            zapocniPripremu();
                        }else {
                            //gotova vjezba
                            reset();
                            spremiVrijeme();
                            otvoriDialog();
                        }
                    }
                }
            }.start();
        }
    }

    void reset(){
        ct.cancel();
        ct = null;
        progress.setValue(0);
        start = -1;
        playpause.setIconResource(R.drawable.play);
        gotova_ponavljanja = 0;
        gotovi_setovi = 0;

    }

    void spremiVrijeme(){
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(String.valueOf(i.getIntExtra("id", 0))+ "IS", currentDateTimeString);
        editor.apply();
    }

    private void otvoriDialog(){

        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.enddialog);
        dialog.setCancelable(false);
        dialog.show();

        final TextView pripremaTotal = (TextView) dialog.findViewById(R.id.pripremaTotal);
        final TextView vjezbaTotal = (TextView) dialog.findViewById(R.id.vjezbaTotal);
        final TextView odmorTotal = (TextView) dialog.findViewById(R.id.odmorTotal);
        final TextView ukupnoTotal = (TextView) dialog.findViewById(R.id.ukupnoTotal);
        pripremaTotal.setText(String.valueOf(Math.round(trajanjePriprema/1000)) + "s");
        vjezbaTotal.setText(String.valueOf(Math.round(trajanjeVjezba/1000))+ "s");
        odmorTotal.setText(String.valueOf(Math.round(trajanjeOdmor/1000))+ "s");
        ukupnoTotal.setText(String.valueOf(Math.round(trajanjeUkupno/1000))+ "s");

        final Button save = (Button) dialog.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(Vjezba.this, OdaberiVjezbu.class);
        startActivity(intent);
    }
}
