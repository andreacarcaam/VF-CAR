package com.example.vf_car.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.vf_car.DB.DataBaseHelper;
import com.example.vf_car.MODELS.Reparacion;
import com.example.vf_car.MODELS.Reparacion_servicio;
import java.util.ArrayList;
import java.util.List;

public class ReparacionDAO {
    private SQLiteDatabase db;
    private DataBaseHelper dbHelper;

    public ReparacionDAO(DataBaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insertReparacion(Reparacion reparacion) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_vehiculo", reparacion.getId_vehiculo());
        values.put("fecha", reparacion.getFecha());
        values.put("descripcion", reparacion.getDescripcion());
        values.put("horasTrabajo", reparacion.getHorasTrabajo());
        values.put("costoPorHora", reparacion.getCostoPorHora());
        values.put("costoTotal", reparacion.getCostoTotal());

        long id = db.insert("reparaciones", null, values);
        db.close();
        return id;
    }

    public void insertServicioEnReparacion(int idReparacion, int idServicio, double horas) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_reparacion", idReparacion);
        values.put("id_servicio", idServicio);
        values.put("horas", horas);
        db.insert("reparacion_servicio", null, values);
        db.close();
    }

    public List<Reparacion> getAllReparaciones() {
        List<Reparacion> reparaciones = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM reparaciones", null);

        if (cursor.moveToFirst()) {
            do {
                Reparacion reparacion = new Reparacion(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4),
                        cursor.getDouble(5)
                );
                reparaciones.add(reparacion);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return reparaciones;
    }

    public List<Reparacion_servicio> getServiciosDeReparacion(int idReparacion) {
        List<Reparacion_servicio> servicios = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM reparacion_servicio WHERE id_reparacion = ?",
                new String[]{String.valueOf(idReparacion)}
        );

        if (cursor.moveToFirst()) {
            do {
                servicios.add(new Reparacion_servicio(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getDouble(2))
                );
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return servicios;
    }

    public int deleteAllReparaciones() {
        db = dbHelper.getWritableDatabase();
        int count = db.delete("reparaciones", null, null);
        db.delete("reparacion_servicio", null, null);
        db.close();
        return count;
    }
    public int deleteReparacion(int idReparacion) {
        db = dbHelper.getWritableDatabase();

        db.delete("reparacion_servicio", "id_reparacion = ?",
                new String[]{String.valueOf(idReparacion)});

        int count = db.delete("reparaciones", "id_reparacion = ?",
                new String[]{String.valueOf(idReparacion)});

        db.close();
        return count;
    }

    public void deleteServiciosDeReparacion(int idReparacion) {
        db = dbHelper.getWritableDatabase();
        db.delete("reparacion_servicio", "id_reparacion = ?",
                new String[]{String.valueOf(idReparacion)});
        db.close();
    }

    public int updateReparacion(Reparacion reparacion) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_vehiculo", reparacion.getId_vehiculo());
        values.put("fecha", reparacion.getFecha());
        values.put("descripcion", reparacion.getDescripcion());
        values.put("horasTrabajo", reparacion.getHorasTrabajo());
        values.put("costoPorHora", reparacion.getCostoPorHora());
        values.put("costoTotal", reparacion.getCostoTotal());

        int count = db.update("reparaciones", values, "id_reparacion = ?",
                new String[]{String.valueOf(reparacion.getId_reparacion())});
        db.close();
        return count;
    }
}