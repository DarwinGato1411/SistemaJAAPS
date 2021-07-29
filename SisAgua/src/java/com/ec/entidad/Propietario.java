/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.entidad;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
@Table(name = "propietario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Propietario.findAll", query = "SELECT p FROM Propietario p")})
public class Propietario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_propietario")
    private Integer idPropietario;
    @Column(name = "porp_cedula")
    private String porpCedula;
    @Column(name = "prop_nombre")
    private String propNombre;
    @Column(name = "prop_apellido")
    private String propApellido;
    @Column(name = "prop_direccion")
    private String propDireccion;
    @Column(name = "prop_sector")
    private String propSector;
    @Column(name = "porp_fecha_nac")
    @Temporal(TemporalType.DATE)
    private Date porpFechaNac;
    @Column(name = "prop_edad")
    private Integer propEdad;
    @Column(name = "prop_local")
    private String propLocal;
    @Transient
    private Boolean esAdultoMayor = Boolean.FALSE;
    @OneToMany(mappedBy = "idPropietario")
    private Collection<Predio> predioCollection;

    public Propietario() {
    }

    public Propietario(Integer idPropietario) {
        this.idPropietario = idPropietario;
    }

    public Integer getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(Integer idPropietario) {
        this.idPropietario = idPropietario;
    }

    public String getPorpCedula() {
        return porpCedula;
    }

    public void setPorpCedula(String porpCedula) {
        this.porpCedula = porpCedula;
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

    public String getPropDireccion() {
        return propDireccion;
    }

    public void setPropDireccion(String propDireccion) {
        this.propDireccion = propDireccion;
    }

    public String getPropSector() {
        return propSector;
    }

    public void setPropSector(String propSector) {
        this.propSector = propSector;
    }

    public Date getPorpFechaNac() {
        return porpFechaNac;
    }

    public void setPorpFechaNac(Date porpFechaNac) {
        this.porpFechaNac = porpFechaNac;
    }

    public Integer getPropEdad() {
        return propEdad;
    }

    public void setPropEdad(Integer propEdad) {
        this.propEdad = propEdad;
    }

    public String getPropLocal() {
        return propLocal;
    }

    public void setPropLocal(String propLocal) {
        this.propLocal = propLocal;
    }

    @XmlTransient
    public Collection<Predio> getPredioCollection() {
        return predioCollection;
    }

    public void setPredioCollection(Collection<Predio> predioCollection) {
        this.predioCollection = predioCollection;
    }

    public Boolean getEsAdultoMayor() {
        if (propEdad == null) {
            propEdad = 0;
        }

        esAdultoMayor = (propEdad < 60 ? Boolean.FALSE : Boolean.TRUE);

        return esAdultoMayor;
    }

    public void setEsAdultoMayor(Boolean esAdultoMayor) {
        this.esAdultoMayor = esAdultoMayor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPropietario != null ? idPropietario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Propietario)) {
            return false;
        }
        Propietario other = (Propietario) object;
        if ((this.idPropietario == null && other.idPropietario != null) || (this.idPropietario != null && !this.idPropietario.equals(other.idPropietario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ec.entidad.Propietario[ idPropietario=" + idPropietario + " ]";
    }

}
