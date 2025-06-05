package com.example.vf_car.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.vf_car.DB.DataBaseHelper;
import com.example.vf_car.MODELS.Cliente;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private DataBaseHelper dbHelper;

    public ClienteDAO(DataBaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // insertar un nuevo cliente
    public long insertCliente(Cliente cliente) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", cliente.getNombre());
        values.put("apellidos", cliente.getApellidos());
        values.put("telefono", cliente.getTelefono());
        return db.insert("cliente", null, values);
    }

    // obtener todos los clientes
    public List<Cliente> getAllClientes() {
        List<Cliente> listaClientes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cliente", null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Cliente cliente = new Cliente(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getInt(3)
                    );
                    listaClientes.add(cliente);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return listaClientes;
    }

    // actualizar un cliente
    public int updateCliente(Cliente cliente) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", cliente.getNombre());
        values.put("apellidos", cliente.getApellidos());
        values.put("telefono", cliente.getTelefono());
        return db.update("cliente", values, "id_cliente=?",
                new String[]{String.valueOf(cliente.getId_cliente())});
    }

    //  eliminar un cliente
    public int deleteCliente(int idCliente) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("cliente", "id_cliente=?",
                new String[]{String.valueOf(idCliente)});
    }

    // eliminar todos los clientes
    public int deleteAllClientes() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("cliente", null, null);
    }

    //  buscar clientes por nombre, apellidos o teléfono
    public List<Cliente> searchClientes(String query) {
        List<Cliente> filteredList = new ArrayList<>();
        String filterPattern = query.toLowerCase().trim();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM cliente WHERE " +
                        "LOWER(nombre) LIKE ? OR " +
                        "LOWER(apellidos) LIKE ? OR " +
                        "telefono LIKE ?",
                new String[]{"%" + filterPattern + "%",
                        "%" + filterPattern + "%",
                        "%" + filterPattern + "%"});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Cliente cliente = new Cliente(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getInt(3)
                    );
                    filteredList.add(cliente);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return filteredList;
    }
    // En ClienteDAO.java
    public Cliente getClienteById(int idCliente) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cliente WHERE id_cliente = ?",
                new String[]{String.valueOf(idCliente)});

        Cliente cliente = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                cliente = new Cliente(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3)
                );
            }
            cursor.close();
        }
        return cliente;
    }
}