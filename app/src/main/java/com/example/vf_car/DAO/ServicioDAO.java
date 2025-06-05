package com.example.vf_car.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.vf_car.DB.DataBaseHelper;
import com.example.vf_car.MODELS.Servicio;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAO {
    private DataBaseHelper dbHelper;

    public ServicioDAO(DataBaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insertServicio(Servicio servicio) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", servicio.getNombre());
        values.put("descripcion", servicio.getDescripcion());
        return db.insert("servicio", null, values);
    }

    public List<Servicio> getAllServicios() {
        List<Servicio> listaServicios = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM servicio", null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Servicio servicio = new Servicio(
                            cursor.getString(1),
                            cursor.getInt(0),
                            cursor.getString(2)
                    );
                    listaServicios.add(servicio);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return listaServicios;
    }

    public int updateServicio(Servicio servicio) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", servicio.getNombre());
        values.put("descripcion", servicio.getDescripcion());
        return db.update("servicio", values, "id_servicio=?",
                new String[]{String.valueOf(servicio.getId_servicio())});
    }

    public int deleteServicio(int idServicio) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("servicio", "id_servicio=?",
                new String[]{String.valueOf(idServicio)});
    }

    public int deleteAllServicios() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("servicio", null, null);
    }

    public List<Servicio> searchServicios(String query) {
        List<Servicio> filteredList = new ArrayList<>();
        String filterPattern = query.toLowerCase().trim();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM servicio WHERE " +
                        "LOWER(nombre) LIKE ? OR " +
                        "LOWER(descripcion) LIKE ?",
                new String[]{"%" + filterPattern + "%",
                        "%" + filterPattern + "%",
                        "%" + filterPattern + "%"});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Servicio servicio = new Servicio(
                            cursor.getString(1),
                            cursor.getInt(0),
                            cursor.getString(2)
                    );
                    filteredList.add(servicio);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return filteredList;
    }
}