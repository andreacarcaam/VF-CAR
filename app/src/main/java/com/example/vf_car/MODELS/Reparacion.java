package com.example.vf_car.MODELS;

public class Reparacion {
    private int id_reparacion;
    private int id_vehiculo;
    private String fecha;
    private String descripcion;
    private double horasTrabajo;
    private double costoPorHora;
    private double costoTotal;

    public Reparacion() {}

    public Reparacion(int id_reparacion, int id_vehiculo, String fecha, String descripcion,
                      double horasTrabajo, double costoPorHora) {
        this.id_reparacion = id_reparacion;
        this.id_vehiculo = id_vehiculo;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.horasTrabajo = horasTrabajo;
        this.costoPorHora = costoPorHora;
        this.costoTotal = horasTrabajo * costoPorHora;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public int getId_reparacion() { return id_reparacion; }
    public void setId_reparacion(int id_reparacion) { this.id_reparacion = id_reparacion; }
    public int getId_vehiculo() { return id_vehiculo; }
    public void setId_vehiculo(int id_vehiculo) { this.id_vehiculo = id_vehiculo; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getHorasTrabajo() { return horasTrabajo; }
    public void setHorasTrabajo(double horasTrabajo) {
        this.horasTrabajo = horasTrabajo;
        this.costoTotal = horasTrabajo * costoPorHora;
    }
    public double getCostoPorHora() { return costoPorHora; }
    public void setCostoPorHora(double costoPorHora) {
        this.costoPorHora = costoPorHora;
        this.costoTotal = horasTrabajo * costoPorHora;
    }
    public double getCostoTotal() { return costoTotal; }
}