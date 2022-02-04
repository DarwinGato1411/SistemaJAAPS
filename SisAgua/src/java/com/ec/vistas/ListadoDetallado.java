/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.vistas;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author HC
 */
@Entity
@Table(name = "listado_detallado")
public class ListadoDetallado implements Serializable{
     
    private static final long serialVersionUID = 1L;
    @Column(name = "id_factura")
    @Id private BigInteger idFactura;
    
    @Column(name = "fac_numero")
    @Id private BigInteger facNumero;
    
    @Column(name = "fac_fecha")
    @Temporal(TemporalType.DATE)
    private Date facFecha;
    
    @Column(name = "med_numero")
    private String medNumero;
    
    @Column(name = "prop_nombre")
    private String propNombre;
        
    @Column(name = "prop_apellido")
    private String propApellido;
    
    @Column(name = "fac_metros_cubicos")
    private BigDecimal facMetrosCubicos;
    
    @Column(name = "det_descripcion")
    private String detDescripcion;
        
    @Column(name = "det_total")
    private BigDecimal detTotal;

    public ListadoDetallado() {
    }

    public ListadoDetallado(BigInteger idFactura, BigInteger facNumero, Date facFecha, String medNumero, String propNombre, String propApellido, BigDecimal facMetrosCubicos, String detDescripcion, BigDecimal detTotal) {
        this.idFactura = idFactura;
        this.facNumero = facNumero;
        this.facFecha = facFecha;
        this.medNumero = medNumero;
        this.propNombre = propNombre;
        this.propApellido = propApellido;
        this.facMetrosCubicos = facMetrosCubicos;
        this.detDescripcion = detDescripcion;
        this.detTotal = detTotal;
    }

    public BigInteger getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(BigInteger idFactura) {
        this.idFactura = idFactura;
    }

    public BigInteger getFacNumero() {
        return facNumero;
    }

    public void setFacNumero(BigInteger facNumero) {
        this.facNumero = facNumero;
    }

    public Date getFacFecha() {
        return facFecha;
    }

    public void setFacFecha(Date facFecha) {
        this.facFecha = facFecha;
    }

    public String getMedNumero() {
        return medNumero;
    }

    public void setMedNumero(String medNumero) {
        this.medNumero = medNumero;
    }

    public String getPropNombre() {
        return propNombre;
    }

    public void setPropNombre(String propNombre) {
        this.propNombre = propNombre;
    }

    public String getPropApellido() {
        return propApellido;
    }

    public void setPropApellido(String propApellido) {
        this.propApellido = propApellido;
    }

    public BigDecimal getFacMetrosCubicos() {
        return facMetrosCubicos;
    }

    public void setFacMetrosCubicos(BigDecimal facMetrosCubicos) {
        this.facMetrosCubicos = facMetrosCubicos;
    }

    public String getDetDescripcion() {
        return detDescripcion;
    }

    public void setDetDescripcion(String detDescripcion) {
        this.detDescripcion = detDescripcion;
    }

    public BigDecimal getDetTotal() {
        return detTotal;
    }

    public void setDetTotal(BigDecimal detTotal) {
        this.detTotal = detTotal;
    }
    


}
    


