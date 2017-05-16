package com.github.lexasoft.masstats.ui;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.github.lexasoft.masstats.models.Site;

import java.util.List;

public class SpinnerSitesAdapter extends ArrayAdapter<Site> {
    private final List<Site> sites;

    public SpinnerSitesAdapter(Context context, List<Site> sites) {
        super(context, android.R.layout.simple_spinner_item, sites);
        this.sites = sites;
    }

    public int getCount(){
        return sites.size();
    }

    public Site getItem(int position){
        return sites.get(position);
    }

    public long getItemId(int position){
        return position;
    }
}
