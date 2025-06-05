package com.example.vf_car.ADAPTERS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vf_car.DB.DataBaseHelper;
import com.example.vf_car.DAO.ClienteDAO;
import com.example.vf_car.MODELS.Cliente;
import com.example.vf_car.MODELS.Vehiculo;
import com.example.vf_car.R;
import java.util.List;

public class VehiculoAdapter extends RecyclerView.Adapter<VehiculoAdapter.VehiculoViewHolder> {

    private List<Vehiculo> vehiculos;
    private OnVehiculoClickListener listener;
    private DataBaseHelper dbHelper;
    private ClienteDAO clienteDAO;

    public interface OnVehiculoClickListener {
        void onVehiculoClick(Vehiculo vehiculo);
        void onVehiculoLongClick(Vehiculo vehiculo);
    }

    public VehiculoAdapter(List<Vehiculo> vehiculos, OnVehiculoClickListener listener, DataBaseHelper dbHelper) {
        this.vehiculos = vehiculos;
        this.listener = listener;
        this.dbHelper = dbHelper;
        this.clienteDAO = new ClienteDAO(dbHelper);
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
        TextView tvMarcaModelo, tvMatricula, tvAno, tvNombreCliente;

        public VehiculoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMarcaModelo = itemView.findViewById(R.id.tvMarcaModelo);
            tvMatricula = itemView.findViewById(R.id.tvMatricula);
            tvAno = itemView.findViewById(R.id.tvAno);
            tvNombreCliente = itemView.findViewById(R.id.tvIdCliente); // Cambia el ID si es necesario

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

            // Obtener el nombre del cliente
            Cliente cliente = clienteDAO.getClienteById(vehiculo.getId_cliente());
            if (cliente != null) {
                tvNombreCliente.setText("Cliente: " + cliente.getNombre() + " " + cliente.getApellidos());
            } else {
                tvNombreCliente.setText("Cliente: No encontrado");
            }
        }
    }
}