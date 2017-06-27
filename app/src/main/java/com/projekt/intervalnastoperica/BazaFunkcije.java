package com.projekt.intervalnastoperica;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;


public class BazaFunkcije {

    private SQLiteDatabase database;
    private BazaPodataka dbHelper;

    public BazaFunkcije(Context ctx) {
        dbHelper = BazaPodataka.getInstance(ctx.getApplicationContext());
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public Boolean IzvrsiUpit(String Upit)
    {
        Boolean odgovor;

        try
        {
            database.execSQL(Upit);
            odgovor = true;
        }
        catch (Exception e)
        {
            odgovor = false;
        }

        return odgovor;
    }

}
