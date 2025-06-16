package com.example.vf_car.ADAPTERS;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vf_car.MODELS.Servicio;
import com.example.vf_car.MODELS.ServicioReparacion;
import com.example.vf_car.R;
import java.util.ArrayList;
import java.util.List;

public class ServicioReparacionAdapter extends RecyclerView.Adapter<ServicioReparacionAdapter.ViewHolder> {

    public interface HorasTotalesListener {
        void onHorasTotalesChanged(double total);
    }

    private List<Servicio> servicios;
    private List<ServicioReparacion> serviciosSeleccionados = new ArrayList<>();
    private Context context;
    private HorasTotalesListener horasTotalesListener;

    public ServicioReparacionAdapter(List<Servicio> servicios, Context context) {
        this.servicios = servicios;
        this.context = context;
    }

    public void setHorasTotalesListener(HorasTotalesListener listener) {
        this.horasTotalesListener = listener;
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
        holder.tvNombreServicio.setText(servicio.getNombre());

        ServicioReparacion servicioExistente = findServicioSeleccionado(servicio.getId_servicio());
        double horas = servicioExistente != null ? servicioExistente.getHoras() : 0;
        holder.btnAsignarHoras.setText(horas > 0 ? String.format("%.2f horas", horas) : "Asignar horas");

        holder.btnAsignarHoras.setOnClickListener(v -> showHorasDialog(servicio, holder));
        holder.itemView.setOnClickListener(v -> showHorasDialog(servicio, holder));
    }

    private void showHorasDialog(Servicio servicio, ViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_input_horas, null);
        builder.setView(dialogView);

        EditText etHoras = dialogView.findViewById(R.id.etHorasDialog);
        ServicioReparacion servicioExistente = findServicioSeleccionado(servicio.getId_servicio());

        if (servicioExistente != null) {
            etHoras.setText(String.valueOf(servicioExistente.getHoras()));
        }

        AlertDialog dialog = builder.setTitle("Horas para " + servicio.getNombre())
                .setPositiveButton("Guardar", (d, which) -> {
                    try {
                        String horasText = etHoras.getText().toString();
                        if (!horasText.isEmpty()) {
                            double horas = Double.parseDouble(horasText);
                            if (horas > 0) {
                                updateServicio(servicio.getId_servicio(), horas);
                                holder.btnAsignarHoras.setText(String.format("%.2f horas", horas));
                                notifyHorasTotalesChanged();
                            } else {
                                removeServicio(servicio.getId_servicio());
                                holder.btnAsignarHoras.setText("Asignar horas");
                                notifyHorasTotalesChanged();
                            }
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(context, "Ingrese un valor válido", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.show();

        // Mostrar teclado automáticamente
        etHoras.postDelayed(() -> {
            etHoras.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(etHoras, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);
    }

    private void notifyHorasTotalesChanged() {
        if (horasTotalesListener != null) {
            horasTotalesListener.onHorasTotalesChanged(getHorasTotales());
        }
    }

    private void updateServicio(int idServicio, double horas) {
        removeServicio(idServicio);
        serviciosSeleccionados.add(new ServicioReparacion(idServicio, horas));
    }

    private void removeServicio(int idServicio) {
        serviciosSeleccionados.removeIf(sr -> sr.getIdServicio() == idServicio);
    }

    private ServicioReparacion findServicioSeleccionado(int idServicio) {
        for (ServicioReparacion sr : serviciosSeleccionados) {
            if (sr.getIdServicio() == idServicio) {
                return sr;
            }
        }
        return null;
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
        public TextView tvNombreServicio;
        public Button btnAsignarHoras;

        public ViewHolder(View view) {
            super(view);
            tvNombreServicio = view.findViewById(R.id.tvNombreServicio);
            btnAsignarHoras = view.findViewById(R.id.btnAsignarHoras);
        }
    }
}