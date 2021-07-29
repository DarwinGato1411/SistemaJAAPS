/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.entidad;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Darwin
 */
@Entity
@Table(name = "estado_lectura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoLectura.findAll", query = "SELECT e FROM EstadoLectura e")})
public class EstadoLectura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_estado_lectura")
    private Integer idEstadoLectura;
    @Column(name = "estl_nombre")
    private String estlNombre;
    @Column(name = "estl_sigla")
    private String estlSigla;
    @Column(name = "estl_estado")
    private Boolean estlEstado;
    @OneToMany(mappedBy = "idEstadoLectura")
    private Collection<Medidor> medidorCollection;

    public EstadoLectura() {
    }

    public EstadoLectura(Integer idEstadoLectura) {
        this.idEstadoLectura = idEstadoLectura;
    }

    public Integer getIdEstadoLectura() {
        return idEstadoLectura;
    }

    public void setIdEstadoLectura(Integer idEstadoLectura) {
        this.idEstadoLectura = idEstadoLectura;
    }

    public String getEstlNombre() {
        return estlNombre;
    }

    public void setEstlNombre(String estlNombre) {
        this.estlNombre = estlNombre;
    }

    public String getEstlSigla() {
        return estlSigla;
    }

    public void setEstlSigla(String estlSigla) {
        this.estlSigla = estlSigla;
    }

    public Boolean getEstlEstado() {
        return estlEstado;
    }

    public void setEstlEstado(Boolean estlEstado) {
        this.estlEstado = estlEstado;
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
        hash += (idEstadoLectura != null ? idEstadoLectura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoLectura)) {
            return false;
        }
        EstadoLectura other = (EstadoLectura) object;
        if ((this.idEstadoLectura == null && other.idEstadoLectura != null) || (this.idEstadoLectura != null && !this.idEstadoLectura.equals(other.idEstadoLectura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ec.entidad.EstadoLectura[ idEstadoLectura=" + idEstadoLectura + " ]";
    }
    
}
