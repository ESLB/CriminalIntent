package com.bignerdranch.android.criminalintent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.criminalintent.model.database.CrimeBaseHelper;
import com.bignerdranch.android.criminalintent.model.database.CrimeCursorWrapper;
import com.bignerdranch.android.criminalintent.model.database.CrimeDbSchema.Cols;
import com.bignerdranch.android.criminalintent.model.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//En esta clase sucede le manejo de toda la información, la comunicación con la base de datos,
//guardar los datos alterados y demás
public class CrimeLab {

    private static CrimeLab sCrimeLab;
    private Context mContext;
    //Objeto para manipular bases de datos
    private SQLiteDatabase mDatabase;

    //Este es un Singletone, por lo tanto, solo puede existir una instanciación de esta clase
    //Para ello, el constructor es privado. Ahora, para acceder a esta instancia usamos el método get()
    //Si ya fue creada una instancia, retornamos esa instancia, sino, creamos una nueva instancia con
    //el context que es pasado por parámetro
    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    //El constructor privado
    //Le da un contexto a la instanciación y crea el objeto para que podamos acceder a la base de datos
    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public void addCrime(Crime c){
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    public List<Crime> getCrimes(){

        List<Crime> crimes = new ArrayList<>();

        //Pedimos todos los Crimes de la base de datos
        //Los vamos guardando en el ArrayList de arriba
        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id){

        //Aquí hacemos una peticion a la base de datos, notar cómo está implementado CrimeCursorWrapper
        //Porque allí vemos cómo está implementada completamente la función
        CrimeCursorWrapper cursor =
                queryCrimes(Cols.UUID + "= ?",new String[] {id.toString()});

        //Verificamos que tengamos un resultado
        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCrime();
        }finally {
            cursor.close();
        }
    }

    public void updateCrime(Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME, values, Cols.UUID + " = ?", new String[]{uuidString});
    }

    //Con esto estamos transmitiendo todos los datos de un Crime en un ContentValues, que luego
    //nos servirá para guardar los datos en la base de datos. De este modo es más sencillo
    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, crime.getId().toString());
        values.put(Cols.TITLE, crime.getTitle());
        values.put(Cols.DATE, crime.getDate().getTime());
        values.put(Cols.SOLVED, crime.isSolved() ? 1 : 0);

        return values;
    }

    //Aquí estamos primero creando un cursos que obtenga los datos de la base de datos
    //Luego, con ese cursor creamos un CrimeCursorWrapper, que nos hace el trabajo de obtener datos
    //del Cursor más sencillo
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, //Columns, null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new CrimeCursorWrapper(cursor);
    }

    //TODO Tenemos que implementar el método para borrar un crime de la base de datos
    public void eraseCrime(UUID id) {
        /*for(Crime dCrime : mCrimes)
        {
            if(dCrime.getId().equals(id))
            {
                mCrimes.remove(dCrime);
            }
        }*/
    }
}
