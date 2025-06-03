package com.example.vf_car.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.vf_car.DB.DataBaseHelper;
import com.example.vf_car.MODELS.Vehiculo;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    private DataBaseHelper dbHelper;

    public VehiculoDAO(DataBaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // Operación para insertar un nuevo vehículo
    public long insertVehiculo(Vehiculo vehiculo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("matricula", vehiculo.getMatricula());
        values.put("marca", vehiculo.getMarca());
        values.put("modelo", vehiculo.getModelo());
        values.put("ano", vehiculo.getAno());
        values.put("id_cliente", vehiculo.getId_cliente());
        return db.insert("vehiculo", null, values);
    }

    // Operación para obtener todos los vehículos
    public List<Vehiculo> getAllVehiculos() {
        List<Vehiculo> listaVehiculos = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM vehiculo", null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Vehiculo vehiculo = new Vehiculo(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getInt(4),
                            cursor.getInt(5)
                    );
                    listaVehiculos.add(vehiculo);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return listaVehiculos;
    }

    // Operación para actualizar un vehículo
    public int updateVehiculo(Vehiculo vehiculo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("matricula", vehiculo.getMatricula());
        values.put("marca", vehiculo.getMarca());
        values.put("modelo", vehiculo.getModelo());
        values.put("ano", vehiculo.getAno());
        values.put("id_cliente", vehiculo.getId_cliente());
        return db.update("vehiculo", values, "id_vehiculo=?",
                new String[]{String.valueOf(vehiculo.getId_vehiculo())});
    }

    // Operación para eliminar un vehículo
    public int deleteVehiculo(int idVehiculo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("vehiculo", "id_vehiculo=?",
                new String[]{String.valueOf(idVehiculo)});
    }

    // Operación para eliminar todos los vehículos
    public int deleteAllVehiculos() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("vehiculo", null, null);
    }

    // Operación para buscar vehículos por matrícula, marca, modelo o año
    public List<Vehiculo> searchVehiculos(String query) {
        List<Vehiculo> filteredList = new ArrayList<>();
        String filterPattern = query.toLowerCase().trim();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM vehiculo WHERE " +
                        "LOWER(matricula) LIKE ? OR " +
                        "LOWER(marca) LIKE ? OR " +
                        "LOWER(modelo) LIKE ? OR " +
                        "ano LIKE ?",
                new String[]{"%" + filterPattern + "%",
                        "%" + filterPattern + "%",
                        "%" + filterPattern + "%",
                        "%" + filterPattern + "%"});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Vehiculo vehiculo = new Vehiculo(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getInt(4),
                            cursor.getInt(5)
                    );
                    filteredList.add(vehiculo);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return filteredList;
    }

    // Operación para obtener vehículos de un cliente específico
    public List<Vehiculo> getVehiculosByCliente(int idCliente) {
        List<Vehiculo> vehiculosCliente = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM vehiculo WHERE id_cliente = ?",
                new String[]{String.valueOf(idCliente)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Vehiculo vehiculo = new Vehiculo(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getInt(4),
                            cursor.getInt(5)
                    );
                    vehiculosCliente.add(vehiculo);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return vehiculosCliente;
    }
}