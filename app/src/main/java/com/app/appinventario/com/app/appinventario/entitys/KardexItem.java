package com.app.appinventario.com.app.appinventario.entitys;

public class KardexItem {

    public String fecha;
    public String concepto;
    public String cantidad1;
    public String precio_u1;
    public String precio_t1;
    public String cantidad2;
    public String precio_u2;
    public String precio_t2;
    public String cantidad3;
    public String precio_u3;
    public String precio_t3;

    public KardexItem(String fecha, String concepto, String cantidad1, String precio_u1, String precio_t1, String cantidad2, String precio_u2, String precio_t2, String cantidad3, String precio_u3, String precio_t3) {
        this.fecha = fecha;
        this.concepto = concepto;
        this.cantidad1 = cantidad1;
        this.precio_u1 = precio_u1;
        this.precio_t1 = precio_t1;
        this.cantidad2 = cantidad2;
        this.precio_u2 = precio_u2;
        this.precio_t2 = precio_t2;
        this.cantidad3 = cantidad3;
        this.precio_u3 = precio_u3;
        this.precio_t3 = precio_t3;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getCantidad1() {
        return cantidad1;
    }

    public void setCantidad1(String cantidad1) {
        this.cantidad1 = cantidad1;
    }

    public String getPrecio_u1() {
        return precio_u1;
    }

    public void setPrecio_u1(String precio_u1) {
        this.precio_u1 = precio_u1;
    }

    public String getPrecio_t1() {
        return precio_t1;
    }

    public void setPrecio_t1(String precio_t1) {
        this.precio_t1 = precio_t1;
    }

    public String getCantidad2() {
        return cantidad2;
    }

    public void setCantidad2(String cantidad2) {
        this.cantidad2 = cantidad2;
    }

    public String getPrecio_u2() {
        return precio_u2;
    }

    public void setPrecio_u2(String precio_u2) {
        this.precio_u2 = precio_u2;
    }

    public String getPrecio_t2() {
        return precio_t2;
    }

    public void setPrecio_t2(String precio_t2) {
        this.precio_t2 = precio_t2;
    }

    public String getCantidad3() {
        return cantidad3;
    }

    public void setCantidad3(String cantidad3) {
        this.cantidad3 = cantidad3;
    }

    public String getPrecio_u3() {
        return precio_u3;
    }

    public void setPrecio_u3(String precio_u3) {
        this.precio_u3 = precio_u3;
    }

    public String getPrecio_t3() {
        return precio_t3;
    }

    public void setPrecio_t3(String precio_t3) {
        this.precio_t3 = precio_t3;
    }
}
