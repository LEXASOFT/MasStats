package com.github.lexasoft.masstats.ui;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.github.lexasoft.masstats.models.Person;

import java.util.List;

public class SpinnerPersonsAdapter extends ArrayAdapter<Person> {
    private final List<Person> persons;

    public SpinnerPersonsAdapter(Context context, List<Person> persons) {
        super(context, android.R.layout.simple_spinner_item, persons);
        this.persons = persons;
    }

    public int getCount(){
        return persons.size();
    }

    public Person getItem(int position){
        return persons.get(position);
    }

    public long getItemId(int position){
        return position;
    }
}
