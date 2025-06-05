package com.example.vf_car.ADAPTERS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vf_car.DAO.VehiculoDAO;
import com.example.vf_car.MODELS.Cliente;
import com.example.vf_car.MODELS.Vehiculo;
import com.example.vf_car.R;
import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    private List<Cliente> clientes;
    private OnClienteClickListener listener;
    private VehiculoDAO vehiculoDAO;

    public interface OnClienteClickListener {
        void onClienteClick(Cliente cliente);
        void onClienteLongClick(Cliente cliente);
    }

    public ClienteAdapter(List<Cliente> clientes, OnClienteClickListener listener, VehiculoDAO vehiculoDAO) {
        this.clientes = clientes;
        this.listener = listener;
        this.vehiculoDAO = vehiculoDAO;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cliente, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente cliente = clientes.get(position);
        holder.bind(cliente, listener, vehiculoDAO);
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public void updateList(List<Cliente> newClientes) {
        clientes = newClientes;
        notifyDataSetChanged();
    }

    static class ClienteViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombreCompleto;
        private TextView tvTelefono;
        private TextView tvVehiculos;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreCompleto = itemView.findViewById(R.id.tvNombreCompleto);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);
            tvVehiculos = itemView.findViewById(R.id.tvVehiculos);
        }

        public void bind(final Cliente cliente, final OnClienteClickListener listener, VehiculoDAO vehiculoDAO) {
            tvNombreCompleto.setText(cliente.getNombre() + " " + cliente.getApellidos());
            tvTelefono.setText("Teléfono: " + cliente.getTelefono());

            // Obtener y mostrar los vehículos del cliente
            List<Vehiculo> vehiculos = vehiculoDAO.getVehiculosByCliente(cliente.getId_cliente());
            if (vehiculos.isEmpty()) {
                tvVehiculos.setText("No tiene vehículos registrados");
            } else {
                StringBuilder vehiculosText = new StringBuilder();
                for (Vehiculo vehiculo : vehiculos) {
                    vehiculosText.append("- ")
                            .append(vehiculo.getMarca())
                            .append(" ")
                            .append(vehiculo.getModelo())
                            .append(" (")
                            .append(vehiculo.getMatricula())
                            .append(")\n");
                }
                tvVehiculos.setText(vehiculosText.toString());
            }

            itemView.setOnClickListener(v -> listener.onClienteClick(cliente));
            itemView.setOnLongClickListener(v -> {
                listener.onClienteLongClick(cliente);
                return true;
            });
        }
    }
}