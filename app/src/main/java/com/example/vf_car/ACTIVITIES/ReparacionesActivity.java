package com.example.vf_car.ACTIVITIES;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vf_car.ADAPTERS.ReparacionAdapter;
import com.example.vf_car.ADAPTERS.ServicioReparacionAdapter;
import com.example.vf_car.DAO.ReparacionDAO;
import com.example.vf_car.DAO.ServicioDAO;
import com.example.vf_car.DAO.VehiculoDAO;
import com.example.vf_car.DB.DataBaseHelper;
import com.example.vf_car.MODELS.Reparacion;
import com.example.vf_car.MODELS.Servicio;
import com.example.vf_car.MODELS.ServicioReparacion;
import com.example.vf_car.MODELS.Vehiculo;
import com.example.vf_car.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReparacionesActivity extends AppCompatActivity implements ReparacionAdapter.OnReparacionClickListener {

    private RecyclerView recyclerView;
    private ReparacionAdapter adapter;
    private DataBaseHelper dbHelper;
    private ReparacionDAO reparacionDAO;
    private VehiculoDAO vehiculoDAO;
    private ServicioDAO servicioDAO;
    private List<Reparacion> listaReparaciones;
    private List<Vehiculo> listaVehiculos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reparaciones);

        dbHelper = DataBaseHelper.getInstance(this);
        reparacionDAO = new ReparacionDAO(dbHelper);
        vehiculoDAO = new VehiculoDAO(dbHelper);
        servicioDAO = new ServicioDAO(dbHelper);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = findViewById(R.id.rvReparaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fabAddReparacion);
        fab.setOnClickListener(v -> showAddReparacionDialog());

        loadReparaciones();
        loadVehiculos();
    }

    private void loadReparaciones() {
        listaReparaciones = reparacionDAO.getAllReparaciones();
        adapter = new ReparacionAdapter(listaReparaciones, this);
        recyclerView.setAdapter(adapter);
    }

    private void loadVehiculos() {
        listaVehiculos = vehiculoDAO.getAllVehiculos();
    }

    private void showAddReparacionDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_reparacion, null);
        Spinner spinnerVehiculos = dialogView.findViewById(R.id.spinnerVehiculos);
        TextInputEditText etFecha = dialogView.findViewById(R.id.etFecha);
        TextInputEditText etDescripcion = dialogView.findViewById(R.id.etDescripcion);
        TextInputEditText etCostoPorHora = dialogView.findViewById(R.id.etCostoPorHora);

        // Configurar el DatePickerDialog
        etFecha.setFocusable(false); 
        etFecha.setOnClickListener(v -> showDatePickerDialog(etFecha));

        ArrayAdapter<Vehiculo> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, listaVehiculos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehiculos.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Nueva Reparación")
                .setView(dialogView)
                .setPositiveButton("Siguiente", null)
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                try {
                    Vehiculo vehiculo = (Vehiculo) spinnerVehiculos.getSelectedItem();
                    String fecha = etFecha.getText().toString().trim();
                    String descripcion = etDescripcion.getText().toString().trim();
                    double costoPorHora = Double.parseDouble(etCostoPorHora.getText().toString());

                    if (validarCampos(vehiculo, fecha, descripcion, costoPorHora)) {
                        hideKeyboard(etCostoPorHora);

                        Reparacion nuevaReparacion = new Reparacion();
                        nuevaReparacion.setId_vehiculo(vehiculo.getId_vehiculo());
                        nuevaReparacion.setFecha(fecha);
                        nuevaReparacion.setDescripcion(descripcion);
                        nuevaReparacion.setCostoPorHora(costoPorHora);

                        new Handler().postDelayed(() -> {
                            showAddServiciosDialog(nuevaReparacion);
                            dialog.dismiss();
                        }, 200);
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Costo por hora inválido", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }
    private void showAddServiciosDialog(Reparacion reparacion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_servicios_reparacion, null);
        builder.setView(dialogView);

        RecyclerView rvServicios = dialogView.findViewById(R.id.rvServicios);
        TextView tvHorasTotales = dialogView.findViewById(R.id.tvHorasTotales);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
        Button btnGuardar = dialogView.findViewById(R.id.btnGuardar);

        List<Servicio> servicios = servicioDAO.getAllServicios();
        ServicioReparacionAdapter adapter = new ServicioReparacionAdapter(servicios, this);
        rvServicios.setAdapter(adapter);
        rvServicios.setLayoutManager(new LinearLayoutManager(this));

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Actualizar horas totales cuando cambien
        adapter.setHorasTotalesListener(total -> {
            tvHorasTotales.setText(String.format("Horas totales: %.2f", total));
        });

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        btnGuardar.setOnClickListener(v -> {
            double horasTotales = adapter.getHorasTotales();
            if (horasTotales > 0) {
                reparacion.setHorasTrabajo(horasTotales);
                long idReparacion = reparacionDAO.insertReparacion(reparacion);

                for (ServicioReparacion sr : adapter.getServiciosSeleccionados()) {
                    reparacionDAO.insertServicioEnReparacion(
                            (int) idReparacion,
                            sr.getIdServicio(),
                            sr.getHoras()
                    );
                }
                loadReparaciones();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Debe asignar horas al menos a un servicio", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }


    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private boolean validarCampos(Vehiculo vehiculo, String fecha, String descripcion, double costoPorHora) {
        if (vehiculo == null) {
            Toast.makeText(this, "Seleccione un vehículo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fecha.isEmpty()) {
            Toast.makeText(this, "Ingrese una fecha", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (descripcion.isEmpty()) {
            Toast.makeText(this, "Ingrese una descripción", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (costoPorHora <= 0) {
            Toast.makeText(this, "El costo por hora debe ser mayor a 0", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void showDatePickerDialog(TextInputEditText etFecha) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // formato dd/MM/yyyy
                    String formattedDate = String.format(Locale.getDefault(),
                            "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    etFecha.setText(formattedDate);
                },
                year, month, day);

        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reparaciones, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_clear_db) {
            new AlertDialog.Builder(this)
                    .setTitle("Limpiar reparaciones")
                    .setMessage("¿Eliminar todas las reparaciones?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        reparacionDAO.deleteAllReparaciones();
                        loadReparaciones();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReparacionClick(Reparacion reparacion) {
        // Implementar si es necesario
    }

    @Override
    public void onReparacionLongClick(Reparacion reparacion) {
        // Implementar si es necesario
    }
}