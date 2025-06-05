package com.example.vf_car.ADAPTERS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vf_car.MODELS.Reparacion;
import com.example.vf_car.R;
import java.util.ArrayList;
import java.util.List;

public class ReparacionAdapter extends RecyclerView.Adapter<ReparacionAdapter.ReparacionViewHolder> implements Filterable {

    private List<Reparacion> reparaciones;
    private List<Reparacion> reparacionesFiltradas;
    private OnReparacionClickListener listener;

    public interface OnReparacionClickListener {
        void onReparacionClick(Reparacion reparacion);
        void onReparacionLongClick(Reparacion reparacion);
    }

    public ReparacionAdapter(List<Reparacion> reparaciones, OnReparacionClickListener listener) {
        this.reparaciones = reparaciones;
        this.reparacionesFiltradas = new ArrayList<>(reparaciones);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReparacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reparacion, parent, false);
        return new ReparacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReparacionViewHolder holder, int position) {
        Reparacion reparacion = reparacionesFiltradas.get(position);
        holder.tvFecha.setText(reparacion.getFecha());
        holder.tvDescripcion.setText(reparacion.getDescripcion());
        holder.tvCosto.setText(String.format("$%.2f", reparacion.getCostoTotal()));

        holder.itemView.setOnClickListener(v -> listener.onReparacionClick(reparacion));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onReparacionLongClick(reparacion);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return reparacionesFiltradas.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Reparacion> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(reparaciones);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Reparacion reparacion : reparaciones) {
                        if (reparacion.getDescripcion().toLowerCase().contains(filterPattern) ||
                                reparacion.getFecha().toLowerCase().contains(filterPattern)) {
                            filteredList.add(reparacion);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                reparacionesFiltradas.clear();
                reparacionesFiltradas.addAll((List<Reparacion>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public void updateList(List<Reparacion> newList) {
        reparaciones = newList;
        reparacionesFiltradas = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    static class ReparacionViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvDescripcion, tvCosto;

        public ReparacionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvCosto = itemView.findViewById(R.id.tvCosto);
        }
    }
}