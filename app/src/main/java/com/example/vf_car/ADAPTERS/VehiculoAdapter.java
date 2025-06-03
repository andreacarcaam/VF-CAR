package com.example.vf_car.ADAPTERS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vf_car.MODELS.Vehiculo;
import com.example.vf_car.R;
import java.util.List;

public class VehiculoAdapter extends RecyclerView.Adapter<VehiculoAdapter.VehiculoViewHolder> {

    private List<Vehiculo> vehiculos;
    private OnVehiculoClickListener listener;

    public interface OnVehiculoClickListener {
        void onVehiculoClick(Vehiculo vehiculo);
        void onVehiculoLongClick(Vehiculo vehiculo);
    }

    public VehiculoAdapter(List<Vehiculo> vehiculos, OnVehiculoClickListener listener) {
        this.vehiculos = vehiculos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VehiculoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vehiculo, parent, false);
        return new VehiculoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehiculoViewHolder holder, int position) {
        Vehiculo vehiculo = vehiculos.get(position);
        holder.bind(vehiculo);
    }

    @Override
    public int getItemCount() {
        return vehiculos.size();
    }

    public void updateList(List<Vehiculo> newList) {
        vehiculos = newList;
        notifyDataSetChanged();
    }

    class VehiculoViewHolder extends RecyclerView.ViewHolder {
        TextView tvMarcaModelo, tvMatricula, tvAno, tvIdCliente;

        public VehiculoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMarcaModelo = itemView.findViewById(R.id.tvMarcaModelo);
            tvMatricula = itemView.findViewById(R.id.tvMatricula);
            tvAno = itemView.findViewById(R.id.tvAno);
            tvIdCliente = itemView.findViewById(R.id.tvIdCliente);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onVehiculoClick(vehiculos.get(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onVehiculoLongClick(vehiculos.get(position));
                    return true;
                }
                return false;
            });
        }

        public void bind(Vehiculo vehiculo) {
            tvMarcaModelo.setText(vehiculo.getMarca() + " " + vehiculo.getModelo());
            tvMatricula.setText("Matrícula: " + vehiculo.getMatricula());
            tvAno.setText("Año: " + vehiculo.getAno());
            tvIdCliente.setText("ID Cliente: " + vehiculo.getId_cliente());
        }
    }
}