package com.example.vf_car.ACTIVITIES;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vf_car.ADAPTERS.ClienteAdapter;
import com.example.vf_car.DAO.ClienteDAO;
import com.example.vf_car.DB.DataBaseHelper;
import com.example.vf_car.MODELS.Cliente;
import com.example.vf_car.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class ClientesActivity extends AppCompatActivity implements ClienteAdapter.OnClienteClickListener {

    private RecyclerView recyclerView;
    private ClienteAdapter adapter;
    private DataBaseHelper dbHelper;
    private ClienteDAO clienteDAO;
    private List<Cliente> listaClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        try {
            // Inicialización de componentes
            dbHelper = DataBaseHelper.getInstance(this);
            clienteDAO = new ClienteDAO(dbHelper);

            // Configurar Toolbar
            Toolbar toolbar = findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
            }

            // Configurar RecyclerView
            recyclerView = findViewById(R.id.rvClientes);
            if (recyclerView != null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setHasFixedSize(true);
            }

            // Configurar botón añadir cliente
            FloatingActionButton fab = findViewById(R.id.fabAddCliente);
            if (fab != null) {
                fab.setOnClickListener(v -> showAddClienteDialog());
            }

            loadClientes();
        } catch (Exception e) {
            Toast.makeText(this, "Error al iniciar actividad: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void loadClientes() {
        try {
            listaClientes = clienteDAO.getAllClientes();
            adapter = new ClienteAdapter(listaClientes, this);
            if (recyclerView != null) {
                recyclerView.setAdapter(adapter);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar clientes: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClienteClick(Cliente cliente) {
        if (cliente != null) {
            showEditClienteDialog(cliente);
        }
    }

    @Override
    public void onClienteLongClick(Cliente cliente) {
        if (cliente != null) {
            showDeleteConfirmationDialog(cliente);
        }
    }
//ventana para crear nuevo cliente
    private void showAddClienteDialog() {
        try {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_cliente, null);
            TextInputEditText etNombre = dialogView.findViewById(R.id.etNombre);
            TextInputEditText etApellidos = dialogView.findViewById(R.id.etApellidos);
            TextInputEditText etTelefono = dialogView.findViewById(R.id.etTelefono);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Nuevo Cliente")
                    .setView(dialogView)
                    .setPositiveButton("Guardar", null)
                    .setNegativeButton("Cancelar", null)
                    .create();

            dialog.setOnShowListener(dialogInterface -> {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                    try {
                        String nombre = etNombre.getText().toString().trim();
                        String apellidos = etApellidos.getText().toString().trim();
                        String telefonoStr = etTelefono.getText().toString().trim();

                        if (validarCampos(nombre, apellidos, telefonoStr, etNombre, etApellidos, etTelefono)) {
                            int telefono = Integer.parseInt(telefonoStr);
                            Cliente nuevoCliente = new Cliente(0, nombre, apellidos, telefono);
                            long result = clienteDAO.insertCliente(nuevoCliente);
                            if (result != -1) {
                                loadClientes();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(this, "Error al guardar cliente", Toast.LENGTH_SHORT).show();
                            }
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
//ventana para editar el cliente
    private void showEditClienteDialog(Cliente cliente) {
        try {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_cliente, null);
            TextInputEditText etNombre = dialogView.findViewById(R.id.etNombre);
            TextInputEditText etApellidos = dialogView.findViewById(R.id.etApellidos);
            TextInputEditText etTelefono = dialogView.findViewById(R.id.etTelefono);

            etNombre.setText(cliente.getNombre());
            etApellidos.setText(cliente.getApellidos());
            etTelefono.setText(String.valueOf(cliente.getTelefono()));

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Editar Cliente")
                    .setView(dialogView)
                    .setPositiveButton("Actualizar", null)
                    .setNegativeButton("Cancelar", null)
                    .create();

            dialog.setOnShowListener(dialogInterface -> {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                    try {
                        String nombre = etNombre.getText().toString().trim();
                        String apellidos = etApellidos.getText().toString().trim();
                        String telefonoStr = etTelefono.getText().toString().trim();

                        if (validarCampos(nombre, apellidos, telefonoStr, etNombre, etApellidos, etTelefono)) {
                            int telefono = Integer.parseInt(telefonoStr);
                            cliente.setNombre(nombre);
                            cliente.setApellidos(apellidos);
                            cliente.setTelefono(telefono);
                            int rowsAffected = clienteDAO.updateCliente(cliente);
                            if (rowsAffected > 0) {
                                loadClientes();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(this, "Error al actualizar cliente", Toast.LENGTH_SHORT).show();
                            }
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

    private boolean validarCampos(String nombre, String apellidos, String telefonoStr,
                                  TextInputEditText etNombre, TextInputEditText etApellidos,
                                  TextInputEditText etTelefono) {
        boolean valido = true;

        if (nombre.isEmpty()) {
            etNombre.setError("Campo obligatorio");
            valido = false;
        }

        if (apellidos.isEmpty()) {
            etApellidos.setError("Campo obligatorio");
            valido = false;
        }

        if (telefonoStr.isEmpty()) {
            etTelefono.setError("Campo obligatorio");
            valido = false;
        } else {
            try {
                Integer.parseInt(telefonoStr);
            } catch (NumberFormatException e) {
                etTelefono.setError("Número inválido");
                valido = false;
            }
        }

        return valido;
    }
//confirmacion para eliminar el cliente
    private void showDeleteConfirmationDialog(Cliente cliente) {
        try {
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar Cliente")
                    .setMessage("¿Estás seguro de eliminar a " + cliente.getNombre() + " " + cliente.getApellidos() + "?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        int rowsDeleted = clienteDAO.deleteCliente(cliente.getId_cliente());
                        if (rowsDeleted > 0) {
                            loadClientes();
                        } else {
                            Toast.makeText(this, "Error al eliminar cliente", Toast.LENGTH_SHORT).show();
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
        try {
            getMenuInflater().inflate(R.menu.menu_clientes, menu);
            MenuItem searchItem = menu.findItem(R.id.action_search);
            androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

            if (searchView != null) {
                searchView.setQueryHint("Buscar clientes...");
                searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filterClientes(newText);
                        return true;
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al crear menú: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == R.id.action_clear_db) {
                showClearDatabaseDialog();
                return true;
            } else if (id == android.R.id.home) {
                onBackPressed();
                return true;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error en menú: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    //eliminar todos los clientes
    private void showClearDatabaseDialog() {
        try {
            new AlertDialog.Builder(this)
                    .setTitle("Limpiar base de datos")
                    .setMessage("¿Estás seguro de eliminar TODOS los clientes?")
                    .setPositiveButton("Limpiar", (dialog, which) -> {
                        int rowsDeleted = clienteDAO.deleteAllClientes();
                        loadClientes();
                        Toast.makeText(this, "Clientes eliminados: " + rowsDeleted, Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        } catch (Exception e) {
            Toast.makeText(this, "Error al mostrar diálogo: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void filterClientes(String query) {
        try {
            List<Cliente> filteredList = clienteDAO.searchClientes(query);
            if (adapter != null) {
                adapter.updateList(filteredList);
            }
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