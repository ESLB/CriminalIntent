package com.bignerdranch.android.criminalintent.activities;

import android.support.v4.app.Fragment;

import com.bignerdranch.android.criminalintent.fragments.CrimeListFragment;
import com.bignerdranch.android.criminalintent.fragments.SingleFragmentActivity;

//Esta es la activity en la que va a estar el fragment CrimeListFragment
//Como se puede ver, no hace nada más que proporcionar el fragment, el cual hace el resto de trabajo
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }
}
