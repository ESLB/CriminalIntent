package com.bignerdranch.android.criminalintent.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.bignerdranch.android.criminalintent.model.Crime;
import com.bignerdranch.android.criminalintent.model.CrimeLab;
import com.bignerdranch.android.criminalintent.R;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    //Para que no se pierda el Crime durate la rotación o demás
    private static final String ARG_CRIME_ID = "crime_id";
    //Para el DialogDate
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    //El Crime del cual vamos a extraer la información
    private Crime mCrime;
    //Widgets del Layout que estamos utilizando
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mDeleteButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeID = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState)
    {
        //Aquí especificamos cual Layout vamos a inflar, en este caso "fragment_crime"
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        //Cableando los widgets
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Left blan, too
            }
        });

        mDeleteButton = (Button) v.findViewById(R.id.delete_crime_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CrimeLab crimeLab = CrimeLab.get(getActivity());
                crimeLab.eraseCrime(mCrime.getId());
                getActivity().finish();

            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Set the crime's solved property
                mCrime.setSolved(b);
            }
        });

        //Retornamos la vista ya lista
        return v;
    }

    @Override
    public void onPause(){
        super.onPause();
        //Como estamos trabajando con la base de datos SQLite, tenemos que asegurarnos
        //de guardar el Crime al salir de esta pantalla, luego de editarla
        CrimeLab.get(getActivity()).updateCrime(mCrime);
}

    //Esto es para el Dialog
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_DATE)
        {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDateText(mCrime.getDate()));
    }

    //Con esto creamos una instancia de un CrimeFragment con un bundle donde guardar informacion
    //y lo devolvemos cuando lo llaman. Dónde se guardará esta instacia? Ya lo veremos más adelante

    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
