package com.app.appinventario.com.app.appinventario.entitys;

public class Ventas {
    private String name;
    private String preciototal;
    private String preciounitarip;
    private String cantidad;
    private String imagen;

    public Ventas(String name, String preciototal, String preciounitarip, String cantidad, String imagen) {
        this.name = name;
        this.preciototal = preciototal;
        this.preciounitarip = preciounitarip;
        this.cantidad = cantidad;
        this.imagen = imagen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreciototal() {
        return preciototal;
    }

    public void setPreciototal(String preciototal) {
        this.preciototal = preciototal;
    }

    public String getPreciounitarip() {
        return preciounitarip;
    }

    public void setPreciounitarip(String preciounitarip) {
        this.preciounitarip = preciounitarip;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}