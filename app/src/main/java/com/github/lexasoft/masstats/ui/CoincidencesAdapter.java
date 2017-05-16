package com.github.lexasoft.masstats.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lexasoft.masstats.R;
import com.github.lexasoft.masstats.models.Coincidence;

import java.util.List;

public class CoincidencesAdapter extends RecyclerView.Adapter<CoincidencesAdapter.ViewHolder> {
    private List<Coincidence> coincidences;

    public CoincidencesAdapter(List<Coincidence> coincidences) {
        this.coincidences = coincidences;
    }

    @Override
    public CoincidencesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_person, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Coincidence coincidence = coincidences.get(position);
        holder.date.setText(coincidence.getDate());
        holder.coincidences.setText(String.valueOf(coincidence.getCoincidences()));
    }

    @Override
    public int getItemCount() {
        return coincidences.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date, coincidences;

        public ViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.name);
            coincidences = (TextView) view.findViewById(R.id.coincidences);
        }
    }

    public void clear() {
        coincidences.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Coincidence> coincidences) {
        this.coincidences.addAll(0, coincidences);
        notifyItemInserted(0);
        notifyDataSetChanged();
    }
}
