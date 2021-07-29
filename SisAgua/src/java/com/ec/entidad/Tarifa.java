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
@Table(name = "tarifa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tarifa.findAll", query = "SELECT t FROM Tarifa t")
    , @NamedQuery(name = "Tarifa.findByIdTarifa", query = "SELECT t FROM Tarifa t WHERE t.idTarifa = :idTarifa")
    , @NamedQuery(name = "Tarifa.findByTariDescripcion", query = "SELECT t FROM Tarifa t WHERE t.tariDescripcion = :tariDescripcion")
    , @NamedQuery(name = "Tarifa.findByTariFecha", query = "SELECT t FROM Tarifa t WHERE t.tariFecha = :tariFecha")
    , @NamedQuery(name = "Tarifa.findByTariSigla", query = "SELECT t FROM Tarifa t WHERE t.tariSigla = :tariSigla")
    , @NamedQuery(name = "Tarifa.findByTariEstado", query = "SELECT t FROM Tarifa t WHERE t.tariEstado = :tariEstado")})
public class Tarifa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tarifa")
    private Integer idTarifa;
    @Column(name = "tari_descripcion")
    private String tariDescripcion;
    @Column(name = "tari_fecha")
    @Temporal(TemporalType.DATE)
    private Date tariFecha;
    @Column(name = "tari_sigla")
    private String tariSigla;
    @Column(name = "tari_estado")
    private Boolean tariEstado;
    @Column(name = "tari_metros_base")
    private BigDecimal tariMetrosBase;
    @OneToMany(mappedBy = "idTarifa")
    private Collection<Medidor> medidorCollection;
    @OneToMany(mappedBy = "idTarifa")
    private Collection<DetalleTarifa> detalleTarifaCollection;

    public Tarifa() {
    }

    public Tarifa(Integer idTarifa) {
        this.idTarifa = idTarifa;
    }

    public Integer getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Integer idTarifa) {
        this.idTarifa = idTarifa;
    }

    public String getTariDescripcion() {
        return tariDescripcion;
    }

    public void setTariDescripcion(String tariDescripcion) {
        this.tariDescripcion = tariDescripcion;
    }

    public Date getTariFecha() {
        return tariFecha;
    }

    public void setTariFecha(Date tariFecha) {
        this.tariFecha = tariFecha;
    }

    public String getTariSigla() {
        return tariSigla;
    }

    public void setTariSigla(String tariSigla) {
        this.tariSigla = tariSigla;
    }

    public Boolean getTariEstado() {
        return tariEstado;
    }

    public void setTariEstado(Boolean tariEstado) {
        this.tariEstado = tariEstado;
    }

    public BigDecimal getTariMetrosBase() {
        return tariMetrosBase;
    }

    public void setTariMetrosBase(BigDecimal tariMetrosBase) {
        this.tariMetrosBase = tariMetrosBase;
    }

    

    @XmlTransient
    public Collection<Medidor> getMedidorCollection() {
        return medidorCollection;
    }

    public void setMedidorCollection(Collection<Medidor> medidorCollection) {
        this.medidorCollection = medidorCollection;
    }

    @XmlTransient
    public Collection<DetalleTarifa> getDetalleTarifaCollection() {
        return detalleTarifaCollection;
    }

    public void setDetalleTarifaCollection(Collection<DetalleTarifa> detalleTarifaCollection) {
        this.detalleTarifaCollection = detalleTarifaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTarifa != null ? idTarifa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tarifa)) {
            return false;
        }
        Tarifa other = (Tarifa) object;
        if ((this.idTarifa == null && other.idTarifa != null) || (this.idTarifa != null && !this.idTarifa.equals(other.idTarifa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ec.entidad.Tarifa[ idTarifa=" + idTarifa + " ]";
    }
    
}
