/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.entidad;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Darwin
 */
@Entity
@Table(name = "detalle_tarifa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleTarifa.findAll", query = "SELECT d FROM DetalleTarifa d")})
public class DetalleTarifa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_detalle_tar")
    private Integer idDetalleTar;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "dett_metro_inicial")
    private BigDecimal dettMetroInicial;
    @Column(name = "dett_metro_final")
    private BigDecimal dettMetroFinal;
    @Column(name = "dett_fecha")
    @Temporal(TemporalType.DATE)
    private Date dettFecha;
    @Column(name = "dett_precio_base")
    private BigDecimal dettPrecioBase;
    @Column(name = "dett_precio_excedente")
    private BigDecimal dettPrecioExcedente;
    @Column(name = "dett_alcantarillado")
    private BigDecimal dettAlcantarillado;
    @Column(name = "dett_ambiente")
    private BigDecimal dettAmbiente;
    @Column(name = "dett_gestion")
    private BigDecimal dettGestion;
    @Column(name = "dett_industria")
    private BigDecimal dettIndustria;
    @Column(name = "dett_descuento")
    private BigDecimal dettDescuento;
    @Column(name = "dett_desechos")
    private BigDecimal dettDesechos;
    @Column(name = "dett_porcentaje_excedente")
    private BigDecimal dettPorcentajeExcedente;
    @Column(name = "dett_porcentaje_alcantarillado")
    private BigDecimal dettPorcentajeAlcantarillado;
    @Column(name = "dett_porcentaje_desechos")
    private BigDecimal dettPorcentajeDesechos;
    @Column(name = "dett_validadesecho")
    private Boolean dettValidadesecho;
    @JoinColumn(name = "id_tarifa", referencedColumnName = "id_tarifa")
    @ManyToOne
    private Tarifa idTarifa;

    public DetalleTarifa() {
    }

    public DetalleTarifa(Integer idDetalleTar) {
        this.idDetalleTar = idDetalleTar;
    }

    public Integer getIdDetalleTar() {
        return idDetalleTar;
    }

    public void setIdDetalleTar(Integer idDetalleTar) {
        this.idDetalleTar = idDetalleTar;
    }

    public BigDecimal getDettMetroInicial() {
        return dettMetroInicial;
    }

    public void setDettMetroInicial(BigDecimal dettMetroInicial) {
        this.dettMetroInicial = dettMetroInicial;
    }

    public BigDecimal getDettMetroFinal() {
        return dettMetroFinal;
    }

    public void setDettMetroFinal(BigDecimal dettMetroFinal) {
        this.dettMetroFinal = dettMetroFinal;
    }

    public Date getDettFecha() {
        return dettFecha;
    }

    public void setDettFecha(Date dettFecha) {
        this.dettFecha = dettFecha;
    }

    public BigDecimal getDettPrecioBase() {
        return dettPrecioBase;
    }

    public void setDettPrecioBase(BigDecimal dettPrecioBase) {
        this.dettPrecioBase = dettPrecioBase;
    }

    public BigDecimal getDettPrecioExcedente() {
        return dettPrecioExcedente;
    }

    public void setDettPrecioExcedente(BigDecimal dettPrecioExcedente) {
        this.dettPrecioExcedente = dettPrecioExcedente;
    }

    public BigDecimal getDettAlcantarillado() {
        return dettAlcantarillado;
    }

    public void setDettAlcantarillado(BigDecimal dettAlcantarillado) {
        this.dettAlcantarillado = dettAlcantarillado;
    }

    public BigDecimal getDettAmbiente() {
        return dettAmbiente;
    }

    public void setDettAmbiente(BigDecimal dettAmbiente) {
        this.dettAmbiente = dettAmbiente;
    }

    public BigDecimal getDettGestion() {
        return dettGestion;
    }

    public void setDettGestion(BigDecimal dettGestion) {
        this.dettGestion = dettGestion;
    }

    public BigDecimal getDettIndustria() {
        return dettIndustria;
    }

    public void setDettIndustria(BigDecimal dettIndustria) {
        this.dettIndustria = dettIndustria;
    }

    public BigDecimal getDettDescuento() {
        return dettDescuento;
    }

    public void setDettDescuento(BigDecimal dettDescuento) {
        this.dettDescuento = dettDescuento;
    }

    public Tarifa getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Tarifa idTarifa) {
        this.idTarifa = idTarifa;
    }

    public BigDecimal getDettDesechos() {
        return dettDesechos;
    }

    public void setDettDesechos(BigDecimal dettDesechos) {
        this.dettDesechos = dettDesechos;
    }

    public BigDecimal getDettPorcentajeExcedente() {
        return dettPorcentajeExcedente;
    }

    public void setDettPorcentajeExcedente(BigDecimal dettPorcentajeExcedente) {
        this.dettPorcentajeExcedente = dettPorcentajeExcedente;
    }

    public BigDecimal getDettPorcentajeAlcantarillado() {
        return dettPorcentajeAlcantarillado;
    }

    public void setDettPorcentajeAlcantarillado(BigDecimal dettPorcentajeAlcantarillado) {
        this.dettPorcentajeAlcantarillado = dettPorcentajeAlcantarillado;
    }

    public BigDecimal getDettPorcentajeDesechos() {
        return dettPorcentajeDesechos;
    }

    public void setDettPorcentajeDesechos(BigDecimal dettPorcentajeDesechos) {
        this.dettPorcentajeDesechos = dettPorcentajeDesechos;
    }

    public Boolean getDettValidadesecho() {
        if (dettValidadesecho == null) {
            dettValidadesecho = Boolean.FALSE;
        }
        return dettValidadesecho;
    }

    public void setDettValidadesecho(Boolean dettValidadesecho) {
        this.dettValidadesecho = dettValidadesecho;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleTar != null ? idDetalleTar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleTarifa)) {
            return false;
        }
        DetalleTarifa other = (DetalleTarifa) object;
        if ((this.idDetalleTar == null && other.idDetalleTar != null) || (this.idDetalleTar != null && !this.idDetalleTar.equals(other.idDetalleTar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ec.entidad.DetalleTarifa[ idDetalleTar=" + idDetalleTar + " ]";
    }

}
