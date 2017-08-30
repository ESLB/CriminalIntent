package com.bignerdranch.android.criminalintent.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.bignerdranch.android.criminalintent.model.Crime;
import com.bignerdranch.android.criminalintent.model.CrimeLab;
import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.fragments.CrimeFragment;

import java.util.List;
import java.util.UUID;

//Según veo, esta es una activity completa
public class CrimePagerActivity extends AppCompatActivity {

    //Para saber en cuál Crime estamos actualmente
    private static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";
    //El Widget
    private ViewPager mViewPager;
    //La lista de Crimes
    private List<Crime> mCrimes;

    public static Intent newIntent(Context packageContext, UUID crimeId){
        //Nota, nos aseguramos de que cada vez que esta actividad sea inicia, se le proporcione un UUID
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);

        mCrimes = CrimeLab.get(this).getCrimes();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                //Nos sirve para obtener un Item/Fragment (usualmente el item que se está mostrando actualmente)
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                //Cuantos items hay en total, para saber qué tantos fragments poner
                return mCrimes.size();
            }
        });

        for(int i = 0; i <mCrimes.size(); i++)
        {
            if(mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }




}
