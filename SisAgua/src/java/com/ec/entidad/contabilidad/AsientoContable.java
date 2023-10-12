/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.entidad.contabilidad;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HC
 */
@Entity
@Table(name = "asiento_contable", catalog = "deckxel_agua", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsientoContable.findAll", query = "SELECT a FROM AsientoContable a")
    , @NamedQuery(name = "AsientoContable.findByIdAc", query = "SELECT a FROM AsientoContable a WHERE a.idAc = :idAc")
    , @NamedQuery(name = "AsientoContable.findByFechaDc", query = "SELECT a FROM AsientoContable a WHERE a.fechaDc = :fechaDc")
    , @NamedQuery(name = "AsientoContable.findByReferenciaDc", query = "SELECT a FROM AsientoContable a WHERE a.referenciaDc = :referenciaDc")
    , @NamedQuery(name = "AsientoContable.findByDocumentoDc", query = "SELECT a FROM AsientoContable a WHERE a.documentoDc = :documentoDc")
    , @NamedQuery(name = "AsientoContable.findByObservacionDc", query = "SELECT a FROM AsientoContable a WHERE a.observacionDc = :observacionDc")
    , @NamedQuery(name = "AsientoContable.findByTotalDebeDc", query = "SELECT a FROM AsientoContable a WHERE a.totalDebeDc = :totalDebeDc")
    , @NamedQuery(name = "AsientoContable.findByTotalHaberDc", query = "SELECT a FROM AsientoContable a WHERE a.totalHaberDc = :totalHaberDc")})
public class AsientoContable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_ac", nullable = false)
    private Integer idAc;
    @Column(name = "fecha_dc")
    @Temporal(TemporalType.DATE)
    private Date fechaDc;
    @Size(max = 100)
    @Column(name = "referencia_dc", length = 100)
    private String referenciaDc;
    @Size(max = 100)
    @Column(name = "documento_dc", length = 100)
    private String documentoDc;
    @Size(max = 100)
    @Column(name = "observacion_dc", length = 100)
    private String observacionDc;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_debe_dc", precision = 10, scale = 4)
    private BigDecimal totalDebeDc;
    @Column(name = "total_haber_dc", precision = 10, scale = 4)
    private BigDecimal totalHaberDc;
    
     @OneToMany(mappedBy = "idAc")
    private Collection<AcSubCuenta> acSubCuentaCollection;

    public AsientoContable() {
    }

    public AsientoContable(Integer idAc, Date fechaDc, String referenciaDc, String documentoDc, String observacionDc, BigDecimal totalDebeDc, BigDecimal totalHaberDc) {
        this.idAc = idAc;
        this.fechaDc = fechaDc;
        this.referenciaDc = referenciaDc;
        this.documentoDc = documentoDc;
        this.observacionDc = observacionDc;
        this.totalDebeDc = totalDebeDc;
        this.totalHaberDc = totalHaberDc;
    }

    
    
    public AsientoContable(Integer idAc) {
        this.idAc = idAc;
    }

    public Integer getIdAc() {
        return idAc;
    }

    public void setIdAc(Integer idAc) {
        this.idAc = idAc;
    }

    public Date getFechaDc() {
        return fechaDc;
    }

    public void setFechaDc(Date fechaDc) {
        this.fechaDc = fechaDc;
    }

    public String getReferenciaDc() {
        return referenciaDc;
    }

    public void setReferenciaDc(String referenciaDc) {
        this.referenciaDc = referenciaDc;
    }

    public String getDocumentoDc() {
        return documentoDc;
    }

    public void setDocumentoDc(String documentoDc) {
        this.documentoDc = documentoDc;
    }

    public String getObservacionDc() {
        return observacionDc;
    }

    public void setObservacionDc(String observacionDc) {
        this.observacionDc = observacionDc;
    }

    public BigDecimal getTotalDebeDc() {
        return totalDebeDc;
    }

    public void setTotalDebeDc(BigDecimal totalDebeDc) {
        this.totalDebeDc = totalDebeDc;
    }

    public BigDecimal getTotalHaberDc() {
        return totalHaberDc;
    }

    public void setTotalHaberDc(BigDecimal totalHaberDc) {
        this.totalHaberDc = totalHaberDc;
    }

    public Collection<AcSubCuenta> getAcSubCuentaCollection() {
        return acSubCuentaCollection;
    }

    public void setAcSubCuentaCollection(Collection<AcSubCuenta> acSubCuentaCollection) {
        this.acSubCuentaCollection = acSubCuentaCollection;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAc != null ? idAc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsientoContable)) {
            return false;
        }
        AsientoContable other = (AsientoContable) object;
        if ((this.idAc == null && other.idAc != null) || (this.idAc != null && !this.idAc.equals(other.idAc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ec.entidad.contabilidad.AsientoContable[ idAc=" + idAc + " ]";
    }
    
}
