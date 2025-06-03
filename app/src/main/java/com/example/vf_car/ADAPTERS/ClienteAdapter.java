package com.example.vf_car.ADAPTERS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vf_car.MODELS.Cliente;
import com.example.vf_car.R;
import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    private List<Cliente> clientes;
    private OnClienteClickListener listener;

    public interface OnClienteClickListener {
        void onClienteClick(Cliente cliente);
        void onClienteLongClick(Cliente cliente);
    }

    public ClienteAdapter(List<Cliente> clientes, OnClienteClickListener listener) {
        this.clientes = clientes;
        this.listener = listener;
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
        holder.bind(cliente, listener);
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

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreCompleto = itemView.findViewById(R.id.tvNombreCompleto);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);
        }

        public void bind(final Cliente cliente, final OnClienteClickListener listener) {
            tvNombreCompleto.setText(cliente.getNombre() + " " + cliente.getApellidos());
            tvTelefono.setText(String.valueOf(cliente.getTelefono()));

            itemView.setOnClickListener(v -> listener.onClienteClick(cliente));
            itemView.setOnLongClickListener(v -> {
                listener.onClienteLongClick(cliente);
                return true;
            });
        }
    }
}