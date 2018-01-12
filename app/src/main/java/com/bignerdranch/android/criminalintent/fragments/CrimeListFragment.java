package com.bignerdranch.android.criminalintent.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bignerdranch.android.criminalintent.model.Crime;
import com.bignerdranch.android.criminalintent.model.CrimeLab;
import com.bignerdranch.android.criminalintent.activities.CrimePagerActivity;
import com.bignerdranch.android.criminalintent.R;

import java.util.List;

//Esta es una clase super compleja, ya que aquí vamos a usar un RecyclerView y tenemos que
//especificar exactamente qué todas las instrucciones para que funcione
//Aunque parezca que se hace mucho, en realidad el celular o tablet lo hace rapidísimo
//Tener en cuenta Spotify, allí sí podemos ver cómo se demora en realizar todos estos pasos
public class CrimeListFragment extends Fragment {

    //Para el RecyclerView
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    //Para manejar el Toolbar
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Con esto y el theme que escogimos le decimos al fragment que el toolbar tiene Opciones
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //Pasamos el Layout que va a inflar este fragment, como vemos en el Layout, solo es un RecyclerView
        View view = inflater.inflate(R.layout.fragment_crime_list,container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //Para el toolbar
        if(savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    private void updateUI(){
        //Obtenemos los Crimes, los pasamos al Adapter y el adapter se lo pasamos al RecyclerView
        //Sino, simplemente actualizamos la lista de Crimes que están en el Adapter y notifyDataSetChanged
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if(mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    //El Holder se encarga de proporcionar un itemView para que luego
    //el Adapter lo una con el Layout
    //Al parecer, aquí se tienen que manejar todas las reacciones que deben ocurrir al presionar un botón
    private class CrimeHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        //Widgets
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        private Crime mCrime;

        //Constructor y cableado de los Widgets con el itemView
        public CrimeHolder(View itemView){
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
            mSolvedCheckBox.setOnCheckedChangeListener(this);

            itemView.setOnClickListener(this);
        }

        //Con este método hacemos más sencillo la implementación de los widget con respecto a la información
        //Del Crime
        public void bindCrime(Crime crime)
        {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDateText(mCrime.getDate()));
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View view) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(mSolvedCheckBox.isChecked())
            {
                mCrime.setSolved(true);
            }
            else
            {
                mCrime.setSolved(false);
            }
        }
    }

    //Adapter: Cogemos los objetos los cuales vamos a utilizar,
    //creamos los ViewHolder donde vamos a mostrar la información de estos objetos gracias al Holder
    //unimos los Crimes con los Holder
    //y por último, informamos cuántos items tenemos actualmente en este adapter
    //Nota: Cuando estos el número de items cambia, se debe llamar la funciones del adapter NotifyItemsSetChanged
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        //Este método sirve pare crear ViewHolder
        //Primero, creamos un LayoutInflater
        //Luego, creamos un View, es View debe ser el Layout el cual el Holder va a poner los datos
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        //Aquí unimos el el Crime con el Holder
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            //Con esta función actualizamos la información del Holder, para que, posiblemente,
            //Muestre otro Crime
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        //Pasamos la lista de Crimes
        public void setCrimes(List<Crime> crimes)
        {
            mCrimes = crimes;
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }


    //Las siguientes tres funciones sirven para manejar el toolbar
    //####################################################################
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        //El recurso que vamos a inflar es un Menu, que contiene items
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Aquí específicamos cómo responder al presionar un item del menu
        switch(item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        @SuppressLint("StringFormatMatches") String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, crimeCount);

        if(!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }
    //######################################################################


}
