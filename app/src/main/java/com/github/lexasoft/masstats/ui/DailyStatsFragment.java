package com.github.lexasoft.masstats.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.github.lexasoft.masstats.App;
import com.github.lexasoft.masstats.R;
import com.github.lexasoft.masstats.models.Coincidence;
import com.github.lexasoft.masstats.models.Person;
import com.github.lexasoft.masstats.models.Site;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyStatsFragment extends Fragment implements
        DatePickerDialog.OnDateSetListener{

    private static final String TAG = DailyStatsFragment.class.getCanonicalName();
    private TextView dateTextView;
    private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private List<Person> persons = new ArrayList<>();
    private List<Site> sites = new ArrayList<>();
    private List<Coincidence> coincidences = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Spinner spinner;
    private Spinner spinnerPerson;
    private CoincidencesAdapter coincidencesAdapter;

    public DailyStatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialy_stats, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.fetching));
        progressDialog.setCancelable(false);

        spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter = new SpinnerSitesAdapter(getActivity(), sites);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadPersons();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerPerson = (Spinner) view.findViewById(R.id.spinner_person);
        ArrayAdapter personAdapter = new SpinnerPersonsAdapter(getActivity(), persons);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPerson.setAdapter(personAdapter);
        spinnerPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadStats();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button = (Button) view.findViewById(R.id.date_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        DailyStatsFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setAutoHighlight(true);
                dpd.show(getActivity().getFragmentManager(), TAG);
            }
        });

        dateTextView = (TextView) view.findViewById(R.id.textView);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        coincidencesAdapter = new CoincidencesAdapter(coincidences);
        recyclerView.setAdapter(coincidencesAdapter);

        loadSites();
        return view;
    }

    private void loadPersons() {
        if (dateTextView.getText().equals("")) {
            progressDialog.show();
            App.getApi().getPersons().enqueue(new Callback<List<Person>>() {
                @Override
                public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                    if (!response.isSuccessful()) {
                        App.Log(TAG, "Error " + String.valueOf(response.code()));
                        return;
                    }
                    persons = response.body();
                    ArrayAdapter arrayAdapter = new SpinnerPersonsAdapter(getActivity(), persons);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPerson.setAdapter(arrayAdapter);

                    Calendar now = Calendar.getInstance();
                    String date = format1.format(now.getTime()) + "/" + format1.format(now.getTime());
                    dateTextView.setText(date);
                }

                @Override
                public void onFailure(Call<List<Person>> call, Throwable t) {
                    progressDialog.hide();
                    App.Log(TAG, "Error " + t.getMessage());
                }
            });

//            loadStats();
            return;
        }
        loadStats();
    }

    private void loadStats() {
        progressDialog.show();
        Site site = (Site) spinner.getSelectedItem();
        Person person = (Person) spinnerPerson.getSelectedItem();
        String dates = dateTextView.getText().toString();
        App.getApi().getPersons(dates, site.getId(), person.getId()).enqueue(new Callback<List<Coincidence>>() {
            @Override
            public void onResponse(Call<List<Coincidence>> call, Response<List<Coincidence>> response) {
                progressDialog.hide();
                if (!response.isSuccessful()) {
                    App.Log(TAG, "Error " + String.valueOf(response.code()));
                    return;
                }
                coincidencesAdapter.clear();
                coincidencesAdapter.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<Coincidence>> call, Throwable t) {
                progressDialog.hide();
                App.Log(TAG, "Error " + t.getMessage());
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        Calendar from = Calendar.getInstance();
        from.set(year, monthOfYear, dayOfMonth);
        Calendar to = Calendar.getInstance();
        to.set(yearEnd, monthOfYearEnd, dayOfMonthEnd);
        String date = format1.format(from.getTime()) + "/" + format1.format(to.getTime());
        dateTextView.setText(date);
        loadStats();
    }

    private void loadSites() {
        progressDialog.show();
        App.getApi().getSites().enqueue(new Callback<List<Site>>() {
            @Override
            public void onResponse(Call<List<Site>> call, Response<List<Site>> response) {
                if (!response.isSuccessful()) {
                    App.Log(TAG, "Error " + String.valueOf(response.code()));
                    return;
                }
                sites = response.body();
                ArrayAdapter arrayAdapter = new SpinnerSitesAdapter(getActivity(), sites);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
//                loadPersons(site.getId());
//                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<List<Site>> call, Throwable t) {
                progressDialog.hide();
                App.Log(TAG, "Error " + t.getMessage());
            }
        });
    }
}
