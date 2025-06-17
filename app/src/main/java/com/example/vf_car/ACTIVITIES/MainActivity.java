package com.example.vf_car.ACTIVITIES;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vf_car.DB.DataBaseHelper;
import com.example.vf_car.R;

public class MainActivity extends AppCompatActivity {
    private DataBaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicializar la base de datos
        dbHelper = DataBaseHelper.getInstance(this);

        // Forzar la creación de la base de datos (solo para prueba)
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Verificación (puedes eliminar esto después)
        if(db != null) {
            Toast.makeText(this, "Base de datos creada correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al crear la base de datos", Toast.LENGTH_SHORT).show();
        }

        // Botón Clientes
        Button btnClientes = findViewById(R.id.btnClientes);
        btnClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir actividad de Clientes (debes crearla)
                Intent intent = new Intent(MainActivity.this, ClientesActivity.class);
                startActivity(intent);
            }
        });

        // Botón Vehículos
        Button btnVehiculos = findViewById(R.id.btnVehiculos);
        btnVehiculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir actividad de Vehículos (debes crearla)
                Intent intent = new Intent(MainActivity.this, VehiculosActivity.class);
                startActivity(intent);
            }
        });

        // Botón Trabajadores
//        Button btnTrabajadores = findViewById(R.id.btnTrabajadores);
//        btnTrabajadores.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Abrir actividad de Trabajadores (debes crearla)
//                Intent intent = new Intent(MainActivity.this, TrabajadoresActivity.class);
//                startActivity(intent);
//            }
//        });

     //    Botón Reparaciones
        Button btnReparaciones = findViewById(R.id.btnReparaciones);
        btnReparaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReparacionesActivity.class);
                startActivity(intent);
            }
        });

        // Botón Servicios
        Button btnServicios = findViewById(R.id.btnServicios);
        btnServicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ServicioActivity.class);
                startActivity(intent);
            }
        });
        // Botón Estadísticas
        Button btnEstadisticas = findViewById(R.id.btnEstadisticas);
        btnEstadisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StatsActivity.class);
                startActivity(intent);
            }
        });
    }
}