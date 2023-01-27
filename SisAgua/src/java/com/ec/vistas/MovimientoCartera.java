/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.vistas;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 *
 * @author HC
 */
@Entity
@Table(name = "movimiento_cartera")

public class MovimientoCartera implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Column(name = "id")
    @Id private BigInteger id;
    @Column(name = "det_descripcion")
    private String detDescripcion;
    @Column(name = "fac_total")
    private BigDecimal facTotal;
    @Column(name = "fac_fecha")
    @Temporal(TemporalType.DATE)
    private Date facFecha;

    public MovimientoCartera() {
    }

    public MovimientoCartera(BigInteger id, String detDescripcion, BigDecimal facTotal, Date facFecha) {
        this.id = id;
        this.detDescripcion = detDescripcion;
        this.facTotal = facTotal;
        this.facFecha = facFecha;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getDetDescripcion() {
        return detDescripcion;
    }

    public void setDetDescripcion(String detDescripcion) {
        this.detDescripcion = detDescripcion;
    }

    public BigDecimal getFacTotal() {
        return facTotal;
    }

    public void setFacTotal(BigDecimal facTotal) {
        this.facTotal = facTotal;
    }

    public Date getFacFecha() {
        return facFecha;
    }

    public void setFacFecha(Date facFecha) {
        this.facFecha = facFecha;
    }

    
    
}
