package com.example.vf_car.MODELS;

public class Reparacion_servicio {
    private int id_reparacion;
    private int id_servicio;
    private double horas;

    public Reparacion_servicio(int id_reparacion, int id_servicio, double horas) {
        this.id_reparacion = id_reparacion;
        this.id_servicio = id_servicio;
        this.horas = horas;
    }

    public int getId_reparacion() { return id_reparacion; }
    public int getId_servicio() { return id_servicio; }
    public double getHoras() { return horas; }
    public void setHoras(double horas) { this.horas = horas; }
}