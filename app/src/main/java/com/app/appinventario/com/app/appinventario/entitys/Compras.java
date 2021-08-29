package com.app.appinventario.com.app.appinventario.entitys;

public class Compras {
    private String name;
    private String preciocompra;
    private String precioventa;
    private String cantidad;
    private String imagen;

    public Compras(String name, String preciocompra, String precioventa, String cantidad, String imagen) {
        this.name = name;
        this.preciocompra = preciocompra;
        this.precioventa = precioventa;
        this.cantidad = cantidad;
        this.imagen = imagen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreciocompra() {
        return preciocompra;
    }

    public void setPreciocompra(String preciocompra) {
        this.preciocompra = preciocompra;
    }

    public String getPrecioventa() {
        return precioventa;
    }

    public void setPrecioventa(String precioventa) {
        this.precioventa = precioventa;
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
