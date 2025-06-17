package com.example.vf_car.ACTIVITIES;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.vf_car.DB.DataBaseHelper;
import com.example.vf_car.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StatsActivity extends AppCompatActivity {

    private TextView tvGanancias, tvPerdidas, tvBalance;
    private Spinner spinnerMes;
    private DataBaseHelper dbHelper;
    private int mesSeleccionado;
    private TabLayout tabLayout;
    private boolean mostrarDatosAnuales = false;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Inicializar vistas
        tvGanancias = findViewById(R.id.tvGanancias);
        tvPerdidas = findViewById(R.id.tvPerdidas);
        tvBalance = findViewById(R.id.tvBalance);
        spinnerMes = findViewById(R.id.spinnerMes);
        tabLayout = findViewById(R.id.tabLayout);
        progressBar = findViewById(R.id.progressBar);

        dbHelper = DataBaseHelper.getInstance(this);

        // Configurar toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Configurar spinner de meses
        configurarSpinnerMeses();

        // Configurar tabs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mostrarDatosAnuales = tab.getPosition() == 1;
                actualizarSelectorMes();
                actualizarDatosSegunVista();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Configurar FAB
        FloatingActionButton fab = findViewById(R.id.fabActualizar);
        fab.setOnClickListener(v -> actualizarDatosSegunVista());

        // Cargar datos iniciales
        mostrarProgreso(true);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            actualizarDatosSegunVista();
            mostrarProgreso(false);
        }, 300);
    }

    private void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        findViewById(R.id.contentLayout).setVisibility(mostrar ? View.GONE : View.VISIBLE);
    }

    private void actualizarSelectorMes() {
        Animation fadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeOut.setDuration(200);
        fadeIn.setDuration(200);

        if (mostrarDatosAnuales) {
            spinnerMes.startAnimation(fadeOut);
            findViewById(R.id.layoutSelectorMes).startAnimation(fadeOut);
            spinnerMes.setVisibility(View.GONE);
            findViewById(R.id.layoutSelectorMes).setVisibility(View.GONE);
        } else {
            spinnerMes.setVisibility(View.VISIBLE);
            findViewById(R.id.layoutSelectorMes).setVisibility(View.VISIBLE);
            spinnerMes.startAnimation(fadeIn);
            findViewById(R.id.layoutSelectorMes).startAnimation(fadeIn);
        }
    }

    private void actualizarDatosSegunVista() {
        mostrarProgreso(true);
        if (mostrarDatosAnuales) {
            cargarEstadisticasAnuales();
        } else {
            if (spinnerMes.getSelectedItemPosition() != -1) {
                cargarEstadisticas(mesSeleccionado);
            }
        }
    }

    private void cargarEstadisticasAnuales() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            String queryGanancias = "SELECT SUM(costoTotal) FROM reparaciones WHERE pagado = 1";
            String queryPerdidas = "SELECT SUM(costoTotal) FROM reparaciones WHERE pagado = 0";

            double ganancias = ejecutarConsultaSuma(db, queryGanancias, null);
            double perdidas = ejecutarConsultaSuma(db, queryPerdidas, null);

            actualizarUI(ganancias, perdidas);

        } catch (Exception e) {
            Log.e("StatsActivity", "Error en consulta anual: " + e.getMessage());
            Toast.makeText(this, "Error al cargar estadísticas anuales", Toast.LENGTH_SHORT).show();
        } finally {
            mostrarProgreso(false);
        }
    }

    private void configurarSpinnerMeses() {
        List<String> meses = new ArrayList<>();
        meses.add("Enero");
        meses.add("Febrero");
        meses.add("Marzo");
        meses.add("Abril");
        meses.add("Mayo");
        meses.add("Junio");
        meses.add("Julio");
        meses.add("Agosto");
        meses.add("Septiembre");
        meses.add("Octubre");
        meses.add("Noviembre");
        meses.add("Diciembre");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, meses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMes.setAdapter(adapter);

        spinnerMes.setSelection(Calendar.getInstance().get(Calendar.MONTH));

        spinnerMes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mesSeleccionado = position + 1;
                if (!mostrarDatosAnuales) {
                    cargarEstadisticas(mesSeleccionado);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void cargarEstadisticas(int mes) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String mesFormateado = String.format(Locale.getDefault(), "%02d", mes);

        try {
            String queryGanancias = "SELECT SUM(costoTotal) FROM reparaciones " +
                    "WHERE pagado = 1 AND (" +
                    "strftime('%m', fecha) = ? OR " +
                    "substr(fecha, 4, 2) = ? OR " +
                    "(length(fecha) >= 6 AND substr(fecha, 6, 2) = ?))";

            String queryPerdidas = "SELECT SUM(costoTotal) FROM reparaciones " +
                    "WHERE pagado = 0 AND (" +
                    "strftime('%m', fecha) = ? OR " +
                    "substr(fecha, 4, 2) = ? OR " +
                    "(length(fecha) >= 6 AND substr(fecha, 6, 2) = ?))";

            String[] params = {mesFormateado, mesFormateado, mesFormateado};

            double ganancias = ejecutarConsultaSuma(db, queryGanancias, params);
            double perdidas = ejecutarConsultaSuma(db, queryPerdidas, params);

            actualizarUI(ganancias, perdidas);

        } catch (Exception e) {
            Log.e("StatsActivity", "Error en consulta SQL: " + e.getMessage());
            Toast.makeText(this, "Error al cargar estadísticas", Toast.LENGTH_SHORT).show();
        } finally {
            mostrarProgreso(false);
        }
    }

    private double ejecutarConsultaSuma(SQLiteDatabase db, String query, String[] params) {
        double resultado = 0;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, params);
            if (cursor != null && cursor.moveToFirst()) {
                resultado = cursor.isNull(0) ? 0 : cursor.getDouble(0);
            }
        } catch (Exception e) {
            Log.e("StatsActivity", "Error en consulta: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return resultado;
    }

    private void actualizarUI(double ganancias, double perdidas) {
        runOnUiThread(() -> {
            Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
            fadeIn.setDuration(300);

            double balance = ganancias - perdidas;
            NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.getDefault());

            tvGanancias.startAnimation(fadeIn);
            tvPerdidas.startAnimation(fadeIn);
            tvBalance.startAnimation(fadeIn);

            tvGanancias.setText(formatoMoneda.format(ganancias));
            tvPerdidas.setText(formatoMoneda.format(perdidas));
            tvBalance.setText(formatoMoneda.format(balance));

            int colorBalance = balance >= 0 ?
                    ContextCompat.getColor(this, android.R.color.holo_green_dark) :
                    ContextCompat.getColor(this, android.R.color.holo_red_dark);

            tvBalance.setTextColor(colorBalance);

            Snackbar.make(findViewById(android.R.id.content),
                            "Datos " + (mostrarDatosAnuales ? "anuales" : "mensuales") + " actualizados",
                            Snackbar.LENGTH_SHORT)
                    .setAnchorView(R.id.fabActualizar)
                    .show();
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}