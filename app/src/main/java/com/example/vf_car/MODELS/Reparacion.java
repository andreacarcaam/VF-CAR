package com.example.vf_car.MODELS;

public class Reparacion {
    private int id_reparacion;
    private int id_vehiculo;
    private String fecha_entrada;
    private String fecha_salida;
    private String estado;
    private String observaciones;
    private Boolean pagado;

    public Reparacion(int id_reparacion, int id_vehiculo, String fecha_entrada, String fecha_salida, String estado, String observaciones, Boolean pagado) {
        this.id_reparacion = id_reparacion;
        this.id_vehiculo = id_vehiculo;
        this.fecha_entrada = fecha_entrada;
        this.fecha_salida = fecha_salida;
        this.estado = estado;
        this.observaciones = observaciones;
        this.pagado = pagado;
    }

    public int getId_reparacion() {
        return id_reparacion;
    }

    public void setId_reparacion(int id_reparacion) {
        this.id_reparacion = id_reparacion;
    }

    public int getId_vehiculo() {
        return id_vehiculo;
    }

    public void setId_vehiculo(int id_vehiculo) {
        this.id_vehiculo = id_vehiculo;
    }



    public String getFecha_entrada() {
        return fecha_entrada;
    }

    public void setFecha_entrada(String fecha_entrada) {
        this.fecha_entrada = fecha_entrada;
    }

    public String getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(String fecha_salida) {
        this.fecha_salida = fecha_salida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public boolean isPagado() {
        return pagado;
    }

    public void setPagada(boolean pagada) {
        this.pagado = pagado;
    }
}
