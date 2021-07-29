/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.entidad;

import com.ec.untilitario.ModeloMeses;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Darwin
 */
@Entity
@Table(name = "lectura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lectura.findAll", query = "SELECT l FROM Lectura l")})
public class Lectura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_lectura")
    private Integer idLectura;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "lec_anterior")
    private BigDecimal lecAnterior;
    @Column(name = "lec_actual")
    private BigDecimal lecActual;

    @Column(name = "lec_metros_cubicos")
    private BigDecimal lecMetrosCubicos;
    @Column(name = "lec_fecha")
    @Temporal(TemporalType.DATE)
    private Date lecFecha;
    @Column(name = "lec_pagada")
    private String lecPagada;
    @Column(name = "lec_descripcion")
    private String lecDescripcion;
    @Column(name = "lec_mes")
    private Integer lecMes;
 
    @JoinColumn(name = "id_medidor", referencedColumnName = "id_medidor")
    @ManyToOne
    private Medidor idMedidor;
    @OneToMany(mappedBy = "idLectura")
    private Collection<DetalleFactura> detalleFacturaCollection;

    @Transient
    private static List<ModeloMeses> listaMeses;
    @Transient
    private ModeloMeses mesActual;

    
    
    static {
        listaMeses = new ArrayList<ModeloMeses>();
        listaMeses.add(new ModeloMeses(1, "ENERO"));
        listaMeses.add(new ModeloMeses(2, "FEBRERO"));
        listaMeses.add(new ModeloMeses(3, "MARZO"));
        listaMeses.add(new ModeloMeses(4, "ABRIL"));
        listaMeses.add(new ModeloMeses(5, "MAYO"));
        listaMeses.add(new ModeloMeses(6, "JUNIO"));
        listaMeses.add(new ModeloMeses(7, "JULIO"));
        listaMeses.add(new ModeloMeses(8, "AGOSTO"));
        listaMeses.add(new ModeloMeses(9, "SEPTIEMBRE"));
        listaMeses.add(new ModeloMeses(10, "OCTUBRE"));
        listaMeses.add(new ModeloMeses(11, "NOVIEMBRE"));
        listaMeses.add(new ModeloMeses(12, "DICIEMBRE"));

    }

    public Lectura() {
    }

    public Lectura(Integer idLectura) {
        this.idLectura = idLectura;
    }

    public Integer getIdLectura() {
        return idLectura;
    }

    public void setIdLectura(Integer idLectura) {
        this.idLectura = idLectura;
    }

    public BigDecimal getLecAnterior() {
        return lecAnterior;
    }

    public void setLecAnterior(BigDecimal lecAnterior) {
        this.lecAnterior = lecAnterior;
    }

    public BigDecimal getLecActual() {
        return lecActual;
    }

    public void setLecActual(BigDecimal lecActual) {
        this.lecActual = lecActual;
    }

    public BigDecimal getLecMetrosCubicos() {
        return lecMetrosCubicos;
    }

    public void setLecMetrosCubicos(BigDecimal lecMetrosCubicos) {
        this.lecMetrosCubicos = lecMetrosCubicos;
    }

    public Date getLecFecha() {
        return lecFecha;
    }

    public void setLecFecha(Date lecFecha) {
        this.lecFecha = lecFecha;
    }

    public String getLecPagada() {
        return lecPagada;
    }

    public void setLecPagada(String lecPagada) {
        this.lecPagada = lecPagada;
    }

    public String getLecDescripcion() {
        return lecDescripcion;
    }

    public void setLecDescripcion(String lecDescripcion) {
        this.lecDescripcion = lecDescripcion;
    }

    public Medidor getIdMedidor() {
        return idMedidor;
    }

    public void setIdMedidor(Medidor idMedidor) {
        this.idMedidor = idMedidor;
    }

    @XmlTransient
    public Collection<DetalleFactura> getDetalleFacturaCollection() {
        return detalleFacturaCollection;
    }

    public Integer getLecMes() {
        return lecMes;
    }

    public void setLecMes(Integer lecMes) {
        this.lecMes = lecMes;
    }

   
    
    
    public void setDetalleFacturaCollection(Collection<DetalleFactura> detalleFacturaCollection) {
        this.detalleFacturaCollection = detalleFacturaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLectura != null ? idLectura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lectura)) {
            return false;
        }
        Lectura other = (Lectura) object;
        if ((this.idLectura == null && other.idLectura != null) || (this.idLectura != null && !this.idLectura.equals(other.idLectura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ec.entidad.Lectura[ idLectura=" + idLectura + " ]";
    }

    public ModeloMeses getMesActual() {
        Integer numeroMes = new Date().getMonth() + 1;
        for (ModeloMeses listaMese : listaMeses) {
            if (listaMese.getNumero() == lecMes) {
                mesActual = listaMese;
                return mesActual;
            }
        }
        return mesActual;
    }

    public void setMesActual(ModeloMeses mesActual) {
        mesActual = mesActual;
    }

}
