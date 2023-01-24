/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.entidad.contabilidad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Darwin
 */
@Entity
@Table(name = "comprobante_diario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComprobanteDiario.findAll", query = "SELECT c FROM ComprobanteDiario c")
    , @NamedQuery(name = "ComprobanteDiario.findBySubcNumero", query = "SELECT c FROM ComprobanteDiario c WHERE c.subcNumero = :subcNumero")
    , @NamedQuery(name = "ComprobanteDiario.findBySubcNombre", query = "SELECT c FROM ComprobanteDiario c WHERE c.subcNombre = :subcNombre")
    , @NamedQuery(name = "ComprobanteDiario.findByHaber", query = "SELECT c FROM ComprobanteDiario c WHERE c.haber = :haber")
    , @NamedQuery(name = "ComprobanteDiario.findByDebe", query = "SELECT c FROM ComprobanteDiario c WHERE c.debe = :debe")
    , @NamedQuery(name = "ComprobanteDiario.findByFechaAcSubcuenta", query = "SELECT c FROM ComprobanteDiario c WHERE c.fechaAcSubcuenta = :fechaAcSubcuenta")
    , @NamedQuery(name = "ComprobanteDiario.findByDocumento", query = "SELECT c FROM ComprobanteDiario c WHERE c.documento = :documento")})
public class ComprobanteDiario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    private static final long serialVersionUID = 1L;
    @Column(name = "subc_numero")
    private String subcNumero;
    @Column(name = "subc_nombre")
    private String subcNombre;
    @Column(name = "haber")
    private BigDecimal haber;
    @Column(name = "debe")
    private BigDecimal debe;
    @Column(name = "fecha_ac_subcuenta")
    @Temporal(TemporalType.DATE)
    private Date fechaAcSubcuenta;
    @Column(name = "documento")
    private String documento;

    public ComprobanteDiario() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    

    public String getSubcNumero() {
        return subcNumero;
    }

    public void setSubcNumero(String subcNumero) {
        this.subcNumero = subcNumero;
    }

    public String getSubcNombre() {
        return subcNombre;
    }

    public void setSubcNombre(String subcNombre) {
        this.subcNombre = subcNombre;
    }

    public BigDecimal getHaber() {
        return haber;
    }

    public void setHaber(BigDecimal haber) {
        this.haber = haber;
    }

    public BigDecimal getDebe() {
        return debe;
    }

    public void setDebe(BigDecimal debe) {
        this.debe = debe;
    }

    public Date getFechaAcSubcuenta() {
        return fechaAcSubcuenta;
    }

    public void setFechaAcSubcuenta(Date fechaAcSubcuenta) {
        this.fechaAcSubcuenta = fechaAcSubcuenta;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

}
