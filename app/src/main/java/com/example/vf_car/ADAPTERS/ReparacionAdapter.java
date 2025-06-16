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
import com.example.vf_car.MODELS.Reparacion_servicio;
import com.example.vf_car.MODELS.Servicio;
import com.example.vf_car.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReparacionAdapter extends RecyclerView.Adapter<ReparacionAdapter.ReparacionViewHolder> implements Filterable {

    private List<Reparacion> reparaciones;
    private List<Reparacion> reparacionesFiltradas;
    private OnReparacionClickListener listener;
    private List<Reparacion_servicio> serviciosPorReparacion;
    private List<Servicio> todosServicios;

    public interface OnReparacionClickListener {
        void onReparacionClick(Reparacion reparacion);
        void onReparacionLongClick(Reparacion reparacion);
    }

    public ReparacionAdapter(List<Reparacion> reparaciones, OnReparacionClickListener listener) {
        this.reparaciones = reparaciones;
        this.reparacionesFiltradas = new ArrayList<>(reparaciones);
        this.listener = listener;
    }
    public void setServiciosData(List<Reparacion_servicio> serviciosReparacion, List<Servicio> servicios) {
        this.serviciosPorReparacion = serviciosReparacion;
        this.todosServicios = servicios;
        notifyDataSetChanged();
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
        holder.tvCosto.setText(String.format(Locale.getDefault(), "%.2f€", reparacion.getCostoTotal()));

        StringBuilder serviciosText = new StringBuilder();
        if (serviciosPorReparacion != null && todosServicios != null) {
            for (Reparacion_servicio rs : serviciosPorReparacion) {
                if (rs.getId_reparacion() == reparacion.getId_reparacion()) {

                    String nombreServicio = getNombreServicio(rs.getId_servicio());
                    serviciosText.append("• ")
                            .append(nombreServicio)
                            .append(" (")
                            .append(rs.getHoras())
                            .append(" horas)\n");
                }
            }
        }

        if (serviciosText.length() > 0) {
            holder.tvServicios.setText(serviciosText.toString().trim());
        } else {
            holder.tvServicios.setText("No hay servicios asociados");
        }

        holder.itemView.setOnClickListener(v -> listener.onReparacionClick(reparacion));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onReparacionLongClick(reparacion);
            return true;
        });
    }
    private String getNombreServicio(int idServicio) {
        for (Servicio servicio : todosServicios) {
            if (servicio.getId_servicio() == idServicio) {
                return servicio.getNombre();
            }
        }
        return "Servicio desconocido";
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


    static class ReparacionViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvDescripcion, tvServicios, tvCosto;

        public ReparacionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvServicios = itemView.findViewById(R.id.tvServicios);
            tvCosto = itemView.findViewById(R.id.tvCosto);
        }
    }
}