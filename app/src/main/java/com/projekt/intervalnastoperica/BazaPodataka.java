package com.projekt.intervalnastoperica;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hugo Boss on 19.6.2017..
 */

public class BazaPodataka extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ISDatabase";
    private static final int DATABASE_VERSION = 1;
    private static BazaPodataka sInstance;

    public static synchronized BazaPodataka getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new BazaPodataka(context.getApplicationContext());
        }
        return sInstance;
    }

    public BazaPodataka(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE `Vjezbe` (" +
                "`id`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                "`ime`TEXT," +
                "`priprema`INTEGER," +
                "`vjezba`INTEGER," +
                "`odmor`INTEGER," +
                "`ponavljanja`INTEGER," +
                "`setovi`INTEGER" +
                ");"
        );


        db.execSQL("INSERT INTO Vjezbe (ime, priprema, vjezba, odmor, ponavljanja, setovi)" +
                   " values ('Vjezba1','" + 60 + "','" + 30 +  "','" + 15 + "','" + 3 + "','" + 2 + "')");

        db.execSQL("INSERT INTO Vjezbe (ime, priprema, vjezba, odmor, ponavljanja, setovi)" +
                " values ('Vjezba2','" + 90 + "','" + 45 +  "','" + 25 + "','" + 4 + "','" + 3 + "')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
