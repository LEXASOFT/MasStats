package com.github.lexasoft.masstats.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lexasoft.masstats.R;
import com.github.lexasoft.masstats.models.Person;

import java.util.List;

public class PersonsAdapter extends RecyclerView.Adapter<PersonsAdapter.ViewHolder> {
    private List<Person> persons;

    public PersonsAdapter(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    public PersonsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_person, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Person person = persons.get(position);
        holder.name.setText(person.getName());
        holder.coincidences.setText(String.valueOf(person.getCoincidences()));
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, coincidences;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            coincidences = (TextView) view.findViewById(R.id.coincidences);
        }
    }

    public void clear() {
        persons.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Person> persons) {
        this.persons.addAll(0, persons);
        notifyItemInserted(0);
        notifyDataSetChanged();
    }
}
