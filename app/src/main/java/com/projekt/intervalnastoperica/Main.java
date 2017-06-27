package com.projekt.intervalnastoperica;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gc.materialdesign.views.ButtonFloat;

import mehdi.sakout.fancybuttons.FancyButton;

public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FancyButton vjezba = (FancyButton) findViewById(R.id.buttonVjezba);
        vjezba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main.this, OdaberiVjezbu.class);
                startActivity(i);
            }
        });

        FancyButton postavke = (FancyButton) findViewById(R.id.buttonSettings);
        postavke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main.this, PopisVjezbi.class);
                startActivity(i);
            }
        });
    }
}
