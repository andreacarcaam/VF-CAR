package com.example.vf_car.MODELS;

public class Servicio {
    private int id_servicio;
    private String nombre;
    private String descripcion;
    private int precio;

    public Servicio(String nombre, int id_servicio, String descripcion, int precio) {
        this.nombre = nombre;
        this.id_servicio = id_servicio;
        this.descripcion = descripcion;
        this.precio = precio;
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

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
}
