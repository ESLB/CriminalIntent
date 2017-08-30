package com.bignerdranch.android.criminalintent.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.bignerdranch.android.criminalintent.R;

//Bine, en esta clase estamos creando actividades que contengan un solo fragment
public abstract class SingleFragmentActivity extends AppCompatActivity {

    //Con este método esperamos que nos den un fragment adecuado
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //Aquí tenemos el lugar donde va a ir al fragment
        //Al parecer los fragments pueden ir en FrameLayouts
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = createFragment();
            //Y aquí ponemos el fragment en ese lugar :)
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
