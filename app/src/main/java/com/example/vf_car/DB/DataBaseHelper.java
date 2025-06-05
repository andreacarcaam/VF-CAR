package com.example.vf_car.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static DataBaseHelper sInstance;
    private final String [] nameTables={"Clientes","Vehiculos","Trabajadores","Servicios","Reparaciones","Reparacion_sevicio"};

    private static final int databaseVersion= 1;

    public DataBaseHelper(Context context) {
        super(context, "TALLER DE COCHES", null, databaseVersion);
    }
    public static synchronized DataBaseHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DataBaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CLIENTE_TABLE = "CREATE TABLE cliente ("
                + "id_cliente INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "nombre    TEXT,\n"
                + "apellidos    TEXT,\n"
                + "telefono  INTEGER\n"+
                ");";
        String CREATE_VEHICULO_TABLE = "CREATE TABLE vehiculo ("
                + "id_vehiculo INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "matricula    TEXT,\n"
                + "marca    TEXT,\n"
                + "modelo  TEXT,\n"
                +  "ano     INTEGER,\n"
                +  "Id_cliente     INTEGER\n"+
                ");";
        String CREATE_SERVICIO_TABLE = "CREATE TABLE servicio ("
                + "id_servicio INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "nombre    TEXT,\n"
                + "descripcion    TEXT\n"+
                ");";
        String CREATE_REPARACION_TABLE = "CREATE TABLE reparaciones ("
                + "id_reparacion INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "id_vehiculo INTEGER,\n"
                + "fecha TEXT,\n"
                + "descripcion TEXT,\n"
                + "horasTrabajo REAL,\n"
                + "costoPorHora REAL,\n"
                + "costoTotal REAL\n"
                + ");";
        String CREATE_REPARACION_SERVICIO_TABLE = "CREATE TABLE reparacion_servicio ("
                + "id_reparacion INTEGER,\n"
                + "id_servicio    INTEGER,\n"
                + "horas    INTEGER\n"+
                ");";
        sqLiteDatabase.execSQL(CREATE_CLIENTE_TABLE);
        sqLiteDatabase.execSQL(CREATE_VEHICULO_TABLE);
        sqLiteDatabase.execSQL(CREATE_SERVICIO_TABLE);
        sqLiteDatabase.execSQL(CREATE_REPARACION_TABLE);
        sqLiteDatabase.execSQL(CREATE_REPARACION_SERVICIO_TABLE);

    }
    public void resetDB(SQLiteDatabase sqLiteDatabase) {

        for (String table : nameTables) {
            sqLiteDatabase.delete(table, null, null);
        }


        sqLiteDatabase.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
