package com.example.vf_car.ADAPTERS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vf_car.MODELS.Servicio;
import com.example.vf_car.MODELS.ServicioReparacion;
import com.example.vf_car.R;
import java.util.ArrayList;
import java.util.List;

public class ServicioReparacionAdapter extends RecyclerView.Adapter<ServicioReparacionAdapter.ViewHolder> {

    private List<Servicio> servicios;
    private List<ServicioReparacion> serviciosSeleccionados = new ArrayList<>();

    public ServicioReparacionAdapter(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_servicio_reparacion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Servicio servicio = servicios.get(position);
        holder.cbServicio.setText(servicio.getNombre());
        holder.etHoras.setEnabled(false);

        // Buscar si este servicio ya está seleccionado
        ServicioReparacion servicioExistente = null;
        for (ServicioReparacion sr : serviciosSeleccionados) {
            if (sr.getIdServicio() == servicio.getId_servicio()) {
                servicioExistente = sr;
                break;
            }
        }

        // Configurar el estado inicial
        if (servicioExistente != null) {
            holder.cbServicio.setChecked(true);
            holder.etHoras.setEnabled(true);
            holder.etHoras.setText(String.valueOf(servicioExistente.getHoras()));
        } else {
            holder.cbServicio.setChecked(false);
            holder.etHoras.setEnabled(false);
            holder.etHoras.setText("");
        }

        holder.cbServicio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            holder.etHoras.setEnabled(isChecked);
            if (isChecked) {
                serviciosSeleccionados.add(new ServicioReparacion(servicio.getId_servicio(), 0));
            } else {
                serviciosSeleccionados.removeIf(sr -> sr.getIdServicio() == servicio.getId_servicio());
            }
        });

        holder.etHoras.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(android.text.Editable s) {
                try {
                    double horas = s.toString().isEmpty() ? 0 : Double.parseDouble(s.toString());
                    for (ServicioReparacion sr : serviciosSeleccionados) {
                        if (sr.getIdServicio() == servicio.getId_servicio()) {
                            sr.setHoras(horas);
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                }
            }
        });
    }

    public double getHorasTotales() {
        double total = 0;
        for (ServicioReparacion sr : serviciosSeleccionados) {
            total += sr.getHoras();
        }
        return total;
    }

    public List<ServicioReparacion> getServiciosSeleccionados() {
        return new ArrayList<>(serviciosSeleccionados);
    }

    @Override
    public int getItemCount() {
        return servicios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox cbServicio;
        public EditText etHoras;

        public ViewHolder(View view) {
            super(view);
            cbServicio = view.findViewById(R.id.cbServicio);
            etHoras = view.findViewById(R.id.etHoras);
        }
    }
}