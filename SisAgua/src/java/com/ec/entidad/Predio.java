/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.entidad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Darwin
 */
@Entity
@Table(name = "predio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Predio.findAll", query = "SELECT p FROM Predio p")})
public class Predio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_predio")
    private Integer idPredio;
    @Column(name = "pred_numero")
    private Integer predNumero;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pre_area")
    private BigDecimal preArea;
    @Column(name = "pre_fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date preFechaRegistro;
    @Column(name = "pre_ubicacion")
    private String preUbicacion;
    @Column(name = "pred_direccion")
    private String predDireccion;
    @JoinColumn(name = "id_propietario", referencedColumnName = "id_propietario")
    @ManyToOne
    private Propietario idPropietario;
    @OneToMany(mappedBy = "idPredio")
    private Collection<Medidor> medidorCollection;

    public Predio() {
    }

    public Predio(Integer idPredio) {
        this.idPredio = idPredio;
    }

    public Integer getIdPredio() {
        return idPredio;
    }

    public void setIdPredio(Integer idPredio) {
        this.idPredio = idPredio;
    }

    public Integer getPredNumero() {
        return predNumero;
    }

    public void setPredNumero(Integer predNumero) {
        this.predNumero = predNumero;
    }

    public BigDecimal getPreArea() {
        return preArea;
    }

    public void setPreArea(BigDecimal preArea) {
        this.preArea = preArea;
    }

    public Date getPreFechaRegistro() {
        return preFechaRegistro;
    }

    public void setPreFechaRegistro(Date preFechaRegistro) {
        this.preFechaRegistro = preFechaRegistro;
    }

    public String getPreUbicacion() {
        return preUbicacion;
    }

    public void setPreUbicacion(String preUbicacion) {
        this.preUbicacion = preUbicacion;
    }

    public String getPredDireccion() {
        return predDireccion;
    }

    public void setPredDireccion(String predDireccion) {
        this.predDireccion = predDireccion;
    }

    public Propietario getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(Propietario idPropietario) {
        this.idPropietario = idPropietario;
    }

    @XmlTransient
    public Collection<Medidor> getMedidorCollection() {
        return medidorCollection;
    }

    public void setMedidorCollection(Collection<Medidor> medidorCollection) {
        this.medidorCollection = medidorCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPredio != null ? idPredio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Predio)) {
            return false;
        }
        Predio other = (Predio) object;
        if ((this.idPredio == null && other.idPredio != null) || (this.idPredio != null && !this.idPredio.equals(other.idPredio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ec.entidad.Predio[ idPredio=" + idPredio + " ]";
    }
    
}
