/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ec.entidad;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Darwin
 */
@Entity
@Table(name = "retencion_venta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RetencionVenta.findAll", query = "SELECT r FROM RetencionVenta r"),
    @NamedQuery(name = "RetencionVenta.findByRveCodigo", query = "SELECT r FROM RetencionVenta r WHERE r.rveCodigo = :rveCodigo"),
    @NamedQuery(name = "RetencionVenta.findByRveAutorizacion", query = "SELECT r FROM RetencionVenta r WHERE r.rveAutorizacion = :rveAutorizacion"),
    @NamedQuery(name = "RetencionVenta.findByRveFecha", query = "SELECT r FROM RetencionVenta r WHERE r.rveFecha = :rveFecha"),
    @NamedQuery(name = "RetencionVenta.findByRvePuntoEmision", query = "SELECT r FROM RetencionVenta r WHERE r.rvePuntoEmision = :rvePuntoEmision"),
    @NamedQuery(name = "RetencionVenta.findByRveSecuencial", query = "SELECT r FROM RetencionVenta r WHERE r.rveSecuencial = :rveSecuencial"),
    @NamedQuery(name = "RetencionVenta.findByRveSerie", query = "SELECT r FROM RetencionVenta r WHERE r.rveSerie = :rveSerie"),
    @NamedQuery(name = "RetencionVenta.findByRveValorRetencionIva100", query = "SELECT r FROM RetencionVenta r WHERE r.rveValorRetencionIva100 = :rveValorRetencionIva100"),
    @NamedQuery(name = "RetencionVenta.findByRveValorRetencionIva30", query = "SELECT r FROM RetencionVenta r WHERE r.rveValorRetencionIva30 = :rveValorRetencionIva30"),
    @NamedQuery(name = "RetencionVenta.findByRveValorRetencionIva70", query = "SELECT r FROM RetencionVenta r WHERE r.rveValorRetencionIva70 = :rveValorRetencionIva70"),
    @NamedQuery(name = "RetencionVenta.findByRveValorRetencionRenta", query = "SELECT r FROM RetencionVenta r WHERE r.rveValorRetencionRenta = :rveValorRetencionRenta")})
public class RetencionVenta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rve_codigo")
    private Integer rveCodigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "rve_autorizacion")
    private String rveAutorizacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rve_fecha")
    @Temporal(TemporalType.DATE)
    private Date rveFecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "rve_punto_emision")
    private String rvePuntoEmision;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rve_secuencial")
    private int rveSecuencial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "rve_serie")
    private String rveSerie;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rve_valor_retencion_iva_100")
    private double rveValorRetencionIva100;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rve_valor_retencion_iva_30")
    private double rveValorRetencionIva30;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rve_valor_retencion_iva_70")
    private double rveValorRetencionIva70;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rve_valor_retencion_renta")
    private double rveValorRetencionRenta;
    @JoinColumn(name = "id_factura", referencedColumnName = "id_factura")
    @ManyToOne(optional = false)
    private Factura idFactura;

    public RetencionVenta() {
    }

    public RetencionVenta(Integer rveCodigo) {
        this.rveCodigo = rveCodigo;
    }

    public RetencionVenta(Integer rveCodigo, String rveAutorizacion, Date rveFecha, String rvePuntoEmision, int rveSecuencial, String rveSerie, double rveValorRetencionIva100, double rveValorRetencionIva30, double rveValorRetencionIva70, double rveValorRetencionRenta) {
        this.rveCodigo = rveCodigo;
        this.rveAutorizacion = rveAutorizacion;
        this.rveFecha = rveFecha;
        this.rvePuntoEmision = rvePuntoEmision;
        this.rveSecuencial = rveSecuencial;
        this.rveSerie = rveSerie;
        this.rveValorRetencionIva100 = rveValorRetencionIva100;
        this.rveValorRetencionIva30 = rveValorRetencionIva30;
        this.rveValorRetencionIva70 = rveValorRetencionIva70;
        this.rveValorRetencionRenta = rveValorRetencionRenta;
    }

    public Integer getRveCodigo() {
        return rveCodigo;
    }

    public void setRveCodigo(Integer rveCodigo) {
        this.rveCodigo = rveCodigo;
    }

    public String getRveAutorizacion() {
        return rveAutorizacion;
    }

    public void setRveAutorizacion(String rveAutorizacion) {
        this.rveAutorizacion = rveAutorizacion;
    }

    public Date getRveFecha() {
        return rveFecha;
    }

    public void setRveFecha(Date rveFecha) {
        this.rveFecha = rveFecha;
    }

    public String getRvePuntoEmision() {
        return rvePuntoEmision;
    }

    public void setRvePuntoEmision(String rvePuntoEmision) {
        this.rvePuntoEmision = rvePuntoEmision;
    }

    public int getRveSecuencial() {
        return rveSecuencial;
    }

    public void setRveSecuencial(int rveSecuencial) {
        this.rveSecuencial = rveSecuencial;
    }

    public String getRveSerie() {
        return rveSerie;
    }

    public void setRveSerie(String rveSerie) {
        this.rveSerie = rveSerie;
    }

    public double getRveValorRetencionIva100() {
        return rveValorRetencionIva100;
    }

    public void setRveValorRetencionIva100(double rveValorRetencionIva100) {
        this.rveValorRetencionIva100 = rveValorRetencionIva100;
    }

    public double getRveValorRetencionIva30() {
        return rveValorRetencionIva30;
    }

    public void setRveValorRetencionIva30(double rveValorRetencionIva30) {
        this.rveValorRetencionIva30 = rveValorRetencionIva30;
    }

    public double getRveValorRetencionIva70() {
        return rveValorRetencionIva70;
    }

    public void setRveValorRetencionIva70(double rveValorRetencionIva70) {
        this.rveValorRetencionIva70 = rveValorRetencionIva70;
    }

    public double getRveValorRetencionRenta() {
        return rveValorRetencionRenta;
    }

    public void setRveValorRetencionRenta(double rveValorRetencionRenta) {
        this.rveValorRetencionRenta = rveValorRetencionRenta;
    }

    public Factura getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Factura idFactura) {
        this.idFactura = idFactura;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rveCodigo != null ? rveCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RetencionVenta)) {
            return false;
        }
        RetencionVenta other = (RetencionVenta) object;
        if ((this.rveCodigo == null && other.rveCodigo != null) || (this.rveCodigo != null && !this.rveCodigo.equals(other.rveCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ec.entidad.RetencionVenta[ rveCodigo=" + rveCodigo + " ]";
    }
    
}
