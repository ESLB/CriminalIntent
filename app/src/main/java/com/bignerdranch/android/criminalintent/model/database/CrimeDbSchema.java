package com.bignerdranch.android.criminalintent.model.database;

//Aquí manejamos el nombre, así como los campos de la base de datos
//De este modo es más seguro interactuar con ella

public class CrimeDbSchema {

    public static final class CrimeTable{
        public static final String NAME = "crimes";
    }

    public static final class Cols{
        public static final String UUID = "uuid";
        public static final String TITLE = "title";
        public static final String DATE = "date";
        public static final String SOLVED = "solved";
        public static final String SUSPECT = "suspect";
    }


}
