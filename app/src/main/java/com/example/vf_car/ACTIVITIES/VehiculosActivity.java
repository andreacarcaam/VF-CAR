package com.example.vf_car.ACTIVITIES;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vf_car.ADAPTERS.VehiculoAdapter;
import com.example.vf_car.DAO.ClienteDAO;
import com.example.vf_car.DAO.VehiculoDAO;
import com.example.vf_car.DB.DataBaseHelper;
import com.example.vf_car.MODELS.Cliente;
import com.example.vf_car.MODELS.Vehiculo;
import com.example.vf_car.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class VehiculosActivity extends AppCompatActivity implements VehiculoAdapter.OnVehiculoClickListener {

    private RecyclerView recyclerView;
    private VehiculoAdapter adapter;
    private DataBaseHelper dbHelper;
    private VehiculoDAO vehiculoDAO;
    private ClienteDAO clienteDAO;
    private List<Vehiculo> listaVehiculos;
    private List<Cliente> listaClientes;
    private int selectedClienteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);

        try {
            // Inicialización de componentes
            dbHelper = DataBaseHelper.getInstance(this);
            vehiculoDAO = new VehiculoDAO(dbHelper);
            clienteDAO = new ClienteDAO(dbHelper);

            // Configurar Toolbar
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

            // Configurar RecyclerView
            recyclerView = findViewById(R.id.rvVehiculos);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);

            // Configurar botón añadir vehículo
            FloatingActionButton fab = findViewById(R.id.fabAddVehiculo);
            fab.setOnClickListener(v -> showAddVehiculoDialog());

            loadVehiculos();
            loadClientes();
        } catch (Exception e) {
            Toast.makeText(this, "Error al iniciar actividad: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }
    private void loadVehiculos() {
        try {
            listaVehiculos = vehiculoDAO.getAllVehiculos();
            VehiculoAdapter adapter = new VehiculoAdapter(listaVehiculos, this, dbHelper);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar vehículos: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void loadClientes() {
        try {
            listaClientes = clienteDAO.getAllClientes();
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar clientes: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onVehiculoClick(Vehiculo vehiculo) {
        if (vehiculo != null) {
            showEditVehiculoDialog(vehiculo);
        }
    }

    @Override
    public void onVehiculoLongClick(Vehiculo vehiculo) {
        if (vehiculo != null) {
            showDeleteConfirmationDialog(vehiculo);
        }
    }
    //ventana para crear nuevo vehiculo
    private void showAddVehiculoDialog() {
        try {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_vehiculo, null);
            TextInputEditText etMatricula = dialogView.findViewById(R.id.etMatricula);
            TextInputEditText etMarca = dialogView.findViewById(R.id.etMarca);
            TextInputEditText etModelo = dialogView.findViewById(R.id.etModelo);
            TextInputEditText etAno = dialogView.findViewById(R.id.etAno);
            Spinner spinnerClientes = dialogView.findViewById(R.id.spinnerClientes);

            // Configurar el Spinner con los nombres de los clientes
            List<String> nombresClientes = new ArrayList<>();
            for (Cliente cliente : listaClientes) {
                nombresClientes.add(cliente.getNombre() + " " + cliente.getApellidos());
            }

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, nombresClientes);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerClientes.setAdapter(spinnerAdapter);

            spinnerClientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedClienteId = listaClientes.get(position).getId_cliente();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedClienteId = -1;
                }
            });

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Nuevo Vehículo")
                    .setView(dialogView)
                    .setPositiveButton("Guardar", null)
                    .setNegativeButton("Cancelar", null)
                    .create();

            dialog.setOnShowListener(dialogInterface -> {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                    try {
                        String matricula = etMatricula.getText().toString().trim();
                        String marca = etMarca.getText().toString().trim();
                        String modelo = etModelo.getText().toString().trim();
                        String anoStr = etAno.getText().toString().trim();

                        if (validarCampos(matricula, marca, modelo, anoStr, selectedClienteId,
                                etMatricula, etMarca, etModelo, etAno)) {
                            int ano = Integer.parseInt(anoStr);

                            Vehiculo nuevoVehiculo = new Vehiculo(0, matricula, marca, modelo, ano, selectedClienteId);
                            vehiculoDAO.insertVehiculo(nuevoVehiculo);
                            loadVehiculos();
                            dialog.dismiss();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });

            dialog.show();
        } catch (Exception e) {
            Toast.makeText(this, "Error al mostrar diálogo: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
//ventana para editar el vehiculo
    private void showEditVehiculoDialog(Vehiculo vehiculo) {
        try {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_vehiculo, null);
            TextInputEditText etMatricula = dialogView.findViewById(R.id.etMatricula);
            TextInputEditText etMarca = dialogView.findViewById(R.id.etMarca);
            TextInputEditText etModelo = dialogView.findViewById(R.id.etModelo);
            TextInputEditText etAno = dialogView.findViewById(R.id.etAno);
            Spinner spinnerClientes = dialogView.findViewById(R.id.spinnerClientes);

            // Configurar el Spinner con los nombres de los clientes
            List<String> nombresClientes = new ArrayList<>();
            int selectedPosition = 0;
            for (int i = 0; i < listaClientes.size(); i++) {
                Cliente cliente = listaClientes.get(i);
                nombresClientes.add(cliente.getNombre() + " " + cliente.getApellidos());
                if (cliente.getId_cliente() == vehiculo.getId_cliente()) {
                    selectedPosition = i;
                }
            }

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, nombresClientes);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerClientes.setAdapter(spinnerAdapter);
            spinnerClientes.setSelection(selectedPosition);

            spinnerClientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedClienteId = listaClientes.get(position).getId_cliente();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedClienteId = -1;
                }
            });

            etMatricula.setText(vehiculo.getMatricula());
            etMarca.setText(vehiculo.getMarca());
            etModelo.setText(vehiculo.getModelo());
            etAno.setText(String.valueOf(vehiculo.getAno()));

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Editar Vehículo")
                    .setView(dialogView)
                    .setPositiveButton("Actualizar", null)
                    .setNegativeButton("Cancelar", null)
                    .create();

            dialog.setOnShowListener(dialogInterface -> {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                    try {
                        String matricula = etMatricula.getText().toString().trim();
                        String marca = etMarca.getText().toString().trim();
                        String modelo = etModelo.getText().toString().trim();
                        String anoStr = etAno.getText().toString().trim();

                        if (validarCampos(matricula, marca, modelo, anoStr, selectedClienteId,
                                etMatricula, etMarca, etModelo, etAno)) {
                            int ano = Integer.parseInt(anoStr);

                            vehiculo.setMatricula(matricula);
                            vehiculo.setMarca(marca);
                            vehiculo.setModelo(modelo);
                            vehiculo.setAno(ano);
                            vehiculo.setId_cliente(selectedClienteId);

                            vehiculoDAO.updateVehiculo(vehiculo);
                            loadVehiculos();
                            dialog.dismiss();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });

            dialog.show();
        } catch (Exception e) {
            Toast.makeText(this, "Error al mostrar diálogo: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean validarCampos(String matricula, String marca, String modelo, String anoStr, int idCliente,
                                  TextInputEditText etMatricula, TextInputEditText etMarca,
                                  TextInputEditText etModelo, TextInputEditText etAno) {
        boolean valido = true;

        if (matricula.isEmpty()) {
            etMatricula.setError("Campo obligatorio");
            valido = false;
        }

        if (marca.isEmpty()) {
            etMarca.setError("Campo obligatorio");
            valido = false;
        }

        if (modelo.isEmpty()) {
            etModelo.setError("Campo obligatorio");
            valido = false;
        }

        if (anoStr.isEmpty()) {
            etAno.setError("Campo obligatorio");
            valido = false;
        } else {
            try {
                Integer.parseInt(anoStr);
            } catch (NumberFormatException e) {
                etAno.setError("Año inválido");
                valido = false;
            }
        }

        if (idCliente == -1) {
            Toast.makeText(this, "Debe seleccionar un cliente", Toast.LENGTH_SHORT).show();
            valido = false;
        }

        return valido;
    }
//confirmacion para eliminar el vehiculo

    private void showDeleteConfirmationDialog(Vehiculo vehiculo) {
        try {
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar Vehículo")
                    .setMessage("¿Estás seguro de eliminar el vehículo " + vehiculo.getMarca() + " " + vehiculo.getModelo() + "?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        try {
                            vehiculoDAO.deleteVehiculo(vehiculo.getId_vehiculo());
                            loadVehiculos();
                        } catch (Exception e) {
                            Toast.makeText(this, "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        } catch (Exception e) {
            Toast.makeText(this, "Error al mostrar diálogo: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vehiculos, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        if (searchView != null) {
            searchView.setQueryHint("Buscar vehículos...");
            searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterVehiculos(newText);
                    return true;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_clear_db) {
            showClearDatabaseDialog();
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //eliminar todos los vehiculos
    private void showClearDatabaseDialog() {
        try {
            new AlertDialog.Builder(this)
                    .setTitle("Limpiar base de datos")
                    .setMessage("¿Estás seguro de eliminar TODOS los vehículos?")
                    .setPositiveButton("Limpiar", (dialog, which) -> {
                        try {
                            vehiculoDAO.deleteAllVehiculos();
                            loadVehiculos();
                            Toast.makeText(this, "Base de datos limpiada", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(this, "Error al limpiar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        } catch (Exception e) {
            Toast.makeText(this, "Error al mostrar diálogo: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void filterVehiculos(String query) {
        try {
            List<Vehiculo> filteredList = vehiculoDAO.searchVehiculos(query);
            adapter.updateList(filteredList);
        } catch (Exception e) {
            Toast.makeText(this, "Error al filtrar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}