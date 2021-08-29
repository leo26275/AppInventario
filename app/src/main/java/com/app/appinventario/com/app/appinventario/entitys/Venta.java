package com.app.appinventario.com.app.appinventario.entitys;

import java.io.Serializable;
import java.util.Date;

public class Venta implements Serializable {
    private int id_numero_venta;
    private String id_cliente;
    private int id_usuario;
    private Date fecha;
    private int estado;

    public Venta(int id_numero_venta, String id_cliente, int id_usuario, Date fecha, int estado) {
        this.id_numero_venta = id_numero_venta;
        this.id_cliente = id_cliente;
        this.id_usuario = id_usuario;
        this.fecha = fecha;
        this.estado = estado;
    }

    public Venta(int id_numero_venta, String id_cliente, Date fecha) {
        this.id_numero_venta = id_numero_venta;
        this.id_cliente = id_cliente;
        this.fecha = fecha;
    }

    public int getId_numero_venta() {
        return id_numero_venta;
    }

    public void setId_numero_venta(int id_numero_venta) {
        this.id_numero_venta = id_numero_venta;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
