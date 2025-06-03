package com.example.vf_car.MODELS;

public class Reparacion_servicio {
    private int id_reparacion;
    private int id_servicio;
    private int precio_real;
    private int tiempo_estimado;

    public Reparacion_servicio(int id_reparacion, int id_servicio, int precio_real, int tiempo_estimado) {
        this.id_reparacion = id_reparacion;
        this.id_servicio = id_servicio;
        this.precio_real = precio_real;
        this.tiempo_estimado = tiempo_estimado;
    }

    public int getId_reparacion() {
        return id_reparacion;
    }

    public void setId_reparacion(int id_reparacion) {
        this.id_reparacion = id_reparacion;
    }

    public int getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(int id_servicio) {
        this.id_servicio = id_servicio;
    }

    public int getPrecio_real() {
        return precio_real;
    }

    public void setPrecio_real(int precio_real) {
        this.precio_real = precio_real;
    }

    public int getTiempo_estimado() {
        return tiempo_estimado;
    }

    public void setTiempo_estimado(int tiempo_estimado) {
        this.tiempo_estimado = tiempo_estimado;
    }
}
