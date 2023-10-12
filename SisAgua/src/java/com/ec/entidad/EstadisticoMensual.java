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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Darwin
 */
@Entity
@Table(name = "estadistico_mensual")
public class EstadisticoMensual implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_estadistico")
    private Integer idEstadistico;
    @Size(max = 300)
    @Column(name = "rubro")
    private String rubro;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "saldo_anterior")
    private BigDecimal saldoAnterior;
    @Column(name = "total_ingreso")
    private BigDecimal totalIngreso;
    @Column(name = "recaudo")
    private BigDecimal recaudo;
    @Column(name = "saldo_actual")
    private BigDecimal saldoActual;
    @Column(name = "porcentaje")
    private BigDecimal porcentaje;

    public EstadisticoMensual() {
    }

    public EstadisticoMensual(Integer idEstadistico) {
        this.idEstadistico = idEstadistico;
    }

    public Integer getIdEstadistico() {
        return idEstadistico;
    }

    public void setIdEstadistico(Integer idEstadistico) {
        this.idEstadistico = idEstadistico;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigDecimal getSaldoAnterior() {
        return saldoAnterior==null?BigDecimal.ZERO:saldoAnterior;
    }

    public void setSaldoAnterior(BigDecimal saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public BigDecimal getTotalIngreso() {
        return totalIngreso==null?BigDecimal.ZERO:totalIngreso;
    }

    public void setTotalIngreso(BigDecimal totalIngreso) {
        this.totalIngreso = totalIngreso;
    }

    public BigDecimal getRecaudo() {
        return recaudo==null?BigDecimal.ZERO:recaudo;
    }

    public void setRecaudo(BigDecimal recaudo) {
        this.recaudo = recaudo;
    }

    public BigDecimal getSaldoActual() {
        return saldoActual==null?BigDecimal.ZERO:saldoActual;
    }

    public void setSaldoActual(BigDecimal saldoActual) {
        this.saldoActual = saldoActual;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstadistico != null ? idEstadistico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadisticoMensual)) {
            return false;
        }
        EstadisticoMensual other = (EstadisticoMensual) object;
        if ((this.idEstadistico == null && other.idEstadistico != null) || (this.idEstadistico != null && !this.idEstadistico.equals(other.idEstadistico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ec.entidad.EstadisticoMensual[ idEstadistico=" + idEstadistico + " ]";
    }
    
}
