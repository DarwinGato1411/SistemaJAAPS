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

/**
 *
 * @author Darwin
 */
@Entity
@Table(name = "ubicacion_medidor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UbicacionMedidor.findAll", query = "SELECT u FROM UbicacionMedidor u")
    , @NamedQuery(name = "UbicacionMedidor.findByIdUbicacionMedidor", query = "SELECT u FROM UbicacionMedidor u WHERE u.idUbicacionMedidor = :idUbicacionMedidor")
    , @NamedQuery(name = "UbicacionMedidor.findByUbimNombre", query = "SELECT u FROM UbicacionMedidor u WHERE u.ubimNombre = :ubimNombre")
    , @NamedQuery(name = "UbicacionMedidor.findByUbimSigla", query = "SELECT u FROM UbicacionMedidor u WHERE u.ubimSigla = :ubimSigla")
    , @NamedQuery(name = "UbicacionMedidor.findByUbimEstado", query = "SELECT u FROM UbicacionMedidor u WHERE u.ubimEstado = :ubimEstado")})
public class UbicacionMedidor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_ubicacion_medidor")
    private Integer idUbicacionMedidor;
    @Column(name = "ubim_nombre")
    private String ubimNombre;
    @Column(name = "ubim_sigla")
    private String ubimSigla;
    @Column(name = "ubim_estado")
    private Boolean ubimEstado;
    @OneToMany(mappedBy = "idUbicacionMedidor")
    private Collection<Medidor> medidorCollection;

    public UbicacionMedidor() {
    }

    public UbicacionMedidor(Integer idUbicacionMedidor) {
        this.idUbicacionMedidor = idUbicacionMedidor;
    }

    public Integer getIdUbicacionMedidor() {
        return idUbicacionMedidor;
    }

    public void setIdUbicacionMedidor(Integer idUbicacionMedidor) {
        this.idUbicacionMedidor = idUbicacionMedidor;
    }

    public String getUbimNombre() {
        return ubimNombre;
    }

    public void setUbimNombre(String ubimNombre) {
        this.ubimNombre = ubimNombre;
    }

    public String getUbimSigla() {
        return ubimSigla;
    }

    public void setUbimSigla(String ubimSigla) {
        this.ubimSigla = ubimSigla;
    }

    public Boolean getUbimEstado() {
        return ubimEstado;
    }

    public void setUbimEstado(Boolean ubimEstado) {
        this.ubimEstado = ubimEstado;
    }

    public Collection<Medidor> getMedidorCollection() {
        return medidorCollection;
    }

    public void setMedidorCollection(Collection<Medidor> medidorCollection) {
        this.medidorCollection = medidorCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUbicacionMedidor != null ? idUbicacionMedidor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UbicacionMedidor)) {
            return false;
        }
        UbicacionMedidor other = (UbicacionMedidor) object;
        if ((this.idUbicacionMedidor == null && other.idUbicacionMedidor != null) || (this.idUbicacionMedidor != null && !this.idUbicacionMedidor.equals(other.idUbicacionMedidor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ec.entidad.UbicacionMedidor[ idUbicacionMedidor=" + idUbicacionMedidor + " ]";
    }

}
