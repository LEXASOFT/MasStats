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
import android.widget.Spinner;

import com.github.lexasoft.masstats.App;
import com.github.lexasoft.masstats.R;
import com.github.lexasoft.masstats.models.Person;
import com.github.lexasoft.masstats.models.Site;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllStatsFragment extends Fragment {
    private static final String TAG = AllStatsFragment.class.getCanonicalName();
    private PersonsAdapter personsAdapter;
    private List<Person> persons = new ArrayList<>();
    private List<Site> sites = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Spinner spinner;

    public AllStatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_stats, container, false);

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
                Site site = (Site) spinner.getSelectedItem();
                loadPersons(site.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        personsAdapter = new PersonsAdapter(persons);
        recyclerView.setAdapter(personsAdapter);

        loadSites();
        return view;
    }

    @Override
    public void onDestroy() {
        progressDialog.dismiss();
        super.onDestroy();
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
                Site site = (Site) spinner.getSelectedItem();
//                loadPersons(site.getId());
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<List<Site>> call, Throwable t) {
                progressDialog.hide();
                App.Log(TAG, "Error " + t.getMessage());
            }
        });
    }

    private void loadPersons(int siteId) {
        progressDialog.show();
        App.getApi().getPersons(siteId).enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                progressDialog.hide();
                if (!response.isSuccessful()) {
                    App.Log(TAG, "Error " + String.valueOf(response.code()));
                    return;
                }
                personsAdapter.clear();
                personsAdapter.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                progressDialog.hide();
                App.Log(TAG, "Error " + t.getMessage());
            }
        });
    }
}
