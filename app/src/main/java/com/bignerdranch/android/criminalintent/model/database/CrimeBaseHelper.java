package com.bignerdranch.android.criminalintent.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.criminalintent.model.database.CrimeDbSchema.CrimeTable;

import static com.bignerdranch.android.criminalintent.model.database.CrimeDbSchema.*;

public class CrimeBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATA_BASE = "crimeBase.db";

    //Constructor de la clase
    public CrimeBaseHelper (Context context)
    {
        super(context, DATA_BASE, null, VERSION);
    }

    //Con esto vamos a ver si existe una base de datos, si no existe, la creamos
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table "+ CrimeTable.NAME + "("+
        "_id integer primary key autoincrement, " + Cols.UUID + ", " +
        Cols.TITLE + " , " + Cols.DATE +" , " + Cols.SOLVED + ")");
    }

    //Este método no lo vamos a utilizar en mucho tiempo
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

}
