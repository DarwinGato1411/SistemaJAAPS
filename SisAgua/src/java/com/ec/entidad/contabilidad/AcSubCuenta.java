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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HC
 */

@Entity
@Table(name = "ac_subcuenta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AcSubCuenta.findAll", query = "SELECT c FROM AcSubCuenta c")
    , @NamedQuery(name = "AcSubCuenta.findByIdAcSubcuenta", query = "SELECT c FROM AcSubCuenta c WHERE c.idAcSubcuenta = :idAcSubcuenta")
    , @NamedQuery(name = "AcSubCuenta.findByDebe", query = "SELECT c FROM AcSubCuenta c WHERE c.debe = :debe")
    , @NamedQuery(name = "AcSubCuenta.findByHaber", query = "SELECT c FROM AcSubCuenta c WHERE c.haber = :haber")
         , @NamedQuery(name = "AcSubCuenta.findByDescripcion", query = "SELECT c FROM AcSubCuenta c WHERE c.descripcion = :descripcion")
    , @NamedQuery(name = "AcSubCuenta.findByDocumento", query = "SELECT c FROM AcSubCuenta c WHERE c.documento = :documento")
        , @NamedQuery(name = "AcSubCuenta.findByFechaAcSubcuenta", query = "SELECT c FROM AcSubCuenta c WHERE c.fechaAcSubcuenta = :fechaAcSubcuenta")
   

})

public class AcSubCuenta implements Serializable{
     private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_ac_subcuenta")
    private Integer idAcSubcuenta;
    @Column(name = "documento")
    private String documento;
    @Column(name = "descripcion")
    private String descripcion;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "debe")
    private BigDecimal debe;
    @Column(name = "haber")
    private BigDecimal haber;
    
    @Column(name = "fecha_ac_subcuenta")
    @Temporal(TemporalType.DATE)
    private Date fechaAcSubcuenta;
    //id_ac
    @JoinColumn(name = "id_ac", referencedColumnName = "id_ac")
    @ManyToOne
    private AsientoContable idAc;

    @JoinColumn(name = "id_sub_cuenta", referencedColumnName = "id_sub_cuenta")
    @ManyToOne
    private CuSubCuenta idSubCuenta;

    public AcSubCuenta() {
    }

    public Integer getIdAcSubcuenta() {
        return idAcSubcuenta;
    }

    public void setIdAcSubcuenta(Integer idAcSubcuenta) {
        this.idAcSubcuenta = idAcSubcuenta;
    }

    public BigDecimal getDebe() {
        return debe;
    }

    public void setDebe(BigDecimal debe) {
        this.debe = debe;
    }

    public BigDecimal getHaber() {
        return haber;
    }

    public void setHaber(BigDecimal haber) {
        this.haber = haber;
    }

    public AsientoContable getIdAc() {
        return idAc;
    }

    public void setIdAc(AsientoContable idAc) {
        this.idAc = idAc;
    }

    public CuSubCuenta getIdSubCuenta() {
        return idSubCuenta;
    }

    public void setIdSubCuenta(CuSubCuenta idSubCuenta) {
        this.idSubCuenta = idSubCuenta;
    }





    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaAcSubcuenta() {
        return fechaAcSubcuenta;
    }

    public void setFechaAcSubcuenta(Date fechaAcSubcuenta) {
        this.fechaAcSubcuenta = fechaAcSubcuenta;
    }



    @Override
    public String toString() {
        return "AcSubCuenta [ idAcSubcuenta=" + idAcSubcuenta + " ]";
    }

       @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAcSubcuenta != null ? idAcSubcuenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AcSubCuenta)) {
            return false;
        }
        AcSubCuenta other = (AcSubCuenta) object;
        if ((this.idAcSubcuenta == null && other.idAcSubcuenta != null) || (this.idAcSubcuenta != null && !this.idAcSubcuenta.equals(other.idAcSubcuenta))) {
            return false;
        }
        return true;
    }    
    
}
