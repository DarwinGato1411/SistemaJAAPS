/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.untilitario;

import com.ec.entidad.CabeceraCompra;
import com.ec.entidad.Producto;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Darwin Morocho
 */
public class DetalleCompraUtil implements Serializable {

    private String codigo = "";
    private BigDecimal cantidad = BigDecimal.ZERO;
    private String descripcion = "";
    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
    private Producto producto = new Producto();

    public DetalleCompraUtil() {
    }

    public DetalleCompraUtil(BigDecimal cantidad, String descripcion, BigDecimal subtotal, BigDecimal total) {
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.subtotal = subtotal;
        this.total = total;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
