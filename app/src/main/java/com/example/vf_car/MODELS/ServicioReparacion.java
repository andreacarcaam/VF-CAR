package com.example.vf_car.MODELS;

public class ServicioReparacion {
    private int idServicio;
    private double horas;

    public ServicioReparacion(int idServicio, double horas) {
        this.idServicio = idServicio;
        this.horas = horas;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public double getHoras() {
        return horas;
    }

    public void setHoras(double horas) {
        this.horas = horas;
    }
}