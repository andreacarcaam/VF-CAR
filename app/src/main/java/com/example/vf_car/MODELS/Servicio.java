package com.example.vf_car.MODELS;

public class Servicio {
    private int id_servicio;
    private String nombre;
    private String descripcion;

    public Servicio(String nombre, int id_servicio, String descripcion) {
        this.nombre = nombre;
        this.id_servicio = id_servicio;
        this.descripcion = descripcion;

    }

    public int getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(int id_servicio) {
        this.id_servicio = id_servicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
