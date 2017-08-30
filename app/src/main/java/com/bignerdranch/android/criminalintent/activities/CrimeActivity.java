package com.bignerdranch.android.criminalintent.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bignerdranch.android.criminalintent.fragments.CrimeFragment;
import com.bignerdranch.android.criminalintent.fragments.SingleFragmentActivity;

import java.util.UUID;

//Bine, esta es la activity en la que vamos a hostear al fragment del crime

public class CrimeActivity extends SingleFragmentActivity {

    //Para el extra
    private static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";

    //Con esto proporcionamos un método sencillo de crear una instancia de esta activity (o sea un intent)
    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    //Cómo estamos usando SingleFragmentActivity
    //En una parte de la clase se nos pide proporcionar un fragment
    //Aquí lo estamos haciend, Creamos un CrimeFragment con la información del crime
    //obtenida a través del UUID
    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }

}
