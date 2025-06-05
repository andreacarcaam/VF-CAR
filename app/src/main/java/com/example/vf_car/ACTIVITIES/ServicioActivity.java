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
import com.example.vf_car.ADAPTERS.ServicioAdapter;
import com.example.vf_car.DAO.ServicioDAO;
import com.example.vf_car.DB.DataBaseHelper;
import com.example.vf_car.MODELS.Servicio;
import com.example.vf_car.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class ServicioActivity extends AppCompatActivity implements ServicioAdapter.OnServicioClickListener {

    private RecyclerView recyclerView;
    private ServicioAdapter adapter;
    private DataBaseHelper dbHelper;
    private ServicioDAO servicioDAO;
    private List<Servicio> listaServicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio);

        try {
            dbHelper = DataBaseHelper.getInstance(this);
            servicioDAO = new ServicioDAO(dbHelper);

            Toolbar toolbar = findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
            }

            recyclerView = findViewById(R.id.rvServicios);
            if (recyclerView != null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setHasFixedSize(true);
            }

            FloatingActionButton fab = findViewById(R.id.fabAddServicio);
            if (fab != null) {
                fab.setOnClickListener(v -> showAddServicioDialog());
            }

            loadServicios();
        } catch (Exception e) {
            Toast.makeText(this, "Error al iniciar actividad: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void loadServicios() {
        try {
            listaServicios = servicioDAO.getAllServicios();
            adapter = new ServicioAdapter(listaServicios, this);
            if (recyclerView != null) {
                recyclerView.setAdapter(adapter);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar servicios: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onServicioClick(Servicio servicio) {
        if (servicio != null) {
            showEditServicioDialog(servicio);
        }
    }

    @Override
    public void onServicioLongClick(Servicio servicio) {
        if (servicio != null) {
            showDeleteConfirmationDialog(servicio);
        }
    }

    private void showAddServicioDialog() {
        try {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_servicio, null);
            TextInputEditText etNombre = dialogView.findViewById(R.id.etNombre);
            TextInputEditText etDescripcion = dialogView.findViewById(R.id.etDescripcion);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Nuevo Servicio")
                    .setView(dialogView)
                    .setPositiveButton("Guardar", null)
                    .setNegativeButton("Cancelar", null)
                    .create();

            dialog.setOnShowListener(dialogInterface -> {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                    try {
                        String nombre = etNombre.getText().toString().trim();
                        String descripcion = etDescripcion.getText().toString().trim();

                        if (validarCampos(nombre, etNombre)) {
                            Servicio nuevoServicio = new Servicio(nombre, 0, descripcion);
                            long result = servicioDAO.insertServicio(nuevoServicio);
                            if (result != -1) {
                                loadServicios();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(this, "Error al guardar servicio", Toast.LENGTH_SHORT).show();
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

    private void showEditServicioDialog(Servicio servicio) {
        try {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_servicio, null);
            TextInputEditText etNombre = dialogView.findViewById(R.id.etNombre);
            TextInputEditText etDescripcion = dialogView.findViewById(R.id.etDescripcion);

            etNombre.setText(servicio.getNombre());
            etDescripcion.setText(servicio.getDescripcion());

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Editar Servicio")
                    .setView(dialogView)
                    .setPositiveButton("Actualizar", null)
                    .setNegativeButton("Cancelar", null)
                    .create();

            dialog.setOnShowListener(dialogInterface -> {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                    try {
                        String nombre = etNombre.getText().toString().trim();
                        String descripcion = etDescripcion.getText().toString().trim();

                        if (validarCampos(nombre, etNombre)) {
                            servicio.setNombre(nombre);
                            servicio.setDescripcion(descripcion);
                            int rowsAffected = servicioDAO.updateServicio(servicio);
                            if (rowsAffected > 0) {
                                loadServicios();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(this, "Error al actualizar servicio", Toast.LENGTH_SHORT).show();
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

    private boolean validarCampos(String nombre, TextInputEditText etNombre) {
        boolean valido = true;

        if (nombre.isEmpty()) {
            etNombre.setError("Campo obligatorio");
            valido = false;
        }

        return valido;
    }

    private void showDeleteConfirmationDialog(Servicio servicio) {
        try {
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar Servicio")
                    .setMessage("¿Estás seguro de eliminar el servicio " + servicio.getNombre() + "?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        int rowsDeleted = servicioDAO.deleteServicio(servicio.getId_servicio());
                        if (rowsDeleted > 0) {
                            loadServicios();
                        } else {
                            Toast.makeText(this, "Error al eliminar servicio", Toast.LENGTH_SHORT).show();
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
            getMenuInflater().inflate(R.menu.menu_servicios, menu);
            MenuItem searchItem = menu.findItem(R.id.action_search);
            androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

            if (searchView != null) {
                searchView.setQueryHint("Buscar servicios...");
                searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filterServicios(newText);
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

    private void showClearDatabaseDialog() {
        try {
            new AlertDialog.Builder(this)
                    .setTitle("Limpiar base de datos")
                    .setMessage("¿Estás seguro de eliminar TODOS los servicios?")
                    .setPositiveButton("Limpiar", (dialog, which) -> {
                        int rowsDeleted = servicioDAO.deleteAllServicios();
                        loadServicios();
                        Toast.makeText(this, "Servicios eliminados: " + rowsDeleted, Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        } catch (Exception e) {
            Toast.makeText(this, "Error al mostrar diálogo: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void filterServicios(String query) {
        try {
            List<Servicio> filteredList = servicioDAO.searchServicios(query);
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