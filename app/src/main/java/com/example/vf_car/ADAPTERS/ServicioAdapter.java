package com.example.vf_car.ADAPTERS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vf_car.MODELS.Servicio;
import com.example.vf_car.R;
import java.util.List;

public class ServicioAdapter extends RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder> {

    private List<Servicio> servicios;
    private OnServicioClickListener listener;

    public interface OnServicioClickListener {
        void onServicioClick(Servicio servicio);
        void onServicioLongClick(Servicio servicio);
    }

    public ServicioAdapter(List<Servicio> servicios, OnServicioClickListener listener) {
        this.servicios = servicios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_servicio, parent, false);
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, int position) {
        Servicio servicio = servicios.get(position);
        holder.bind(servicio, listener);
    }

    @Override
    public int getItemCount() {
        return servicios.size();
    }

    public void updateList(List<Servicio> newServicios) {
        servicios = newServicios;
        notifyDataSetChanged();
    }

    static class ServicioViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre;
        private TextView tvDescripcion;
        private TextView tvPrecio;

        public ServicioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
        }

        public void bind(final Servicio servicio, final OnServicioClickListener listener) {
            tvNombre.setText(servicio.getNombre());
            tvDescripcion.setText(servicio.getDescripcion());

            itemView.setOnClickListener(v -> listener.onServicioClick(servicio));
            itemView.setOnLongClickListener(v -> {
                listener.onServicioLongClick(servicio);
                return true;
            });
        }
    }
}