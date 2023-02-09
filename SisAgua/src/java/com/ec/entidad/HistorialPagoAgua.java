/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.entidad;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Darwin Morocho
 */
@Entity
@Table(name = "historial_pago_agua")
public class HistorialPagoAgua implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 12)
    @Column(name = "cli_cedula")
    private String cliCedula;
    @Column(name = "det_descripcion")
    private String detDescripcion;
    @Column(name = "fac_lec_anterior")
    private BigDecimal facLecAnterior;
    @Column(name = "fac_lec_actual")
    private BigDecimal facLecActual;
    @Column(name = "fac_metros_cubicos")
    private BigDecimal facMetrosCubicos;
    @Column(name = "fac_medidor")
    private String facMedidor;
    @Column(name = "fac_lec_mes")
    private Integer facLecMes;
    @Column(name = "mes_text")
    private String mesText;
    @Column(name = "anio")
    private String anio;
    
    @Column(name = "det_medidor")
    private String detMedidor;
    

    public HistorialPagoAgua() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCliCedula() {
        return cliCedula;
    }

    public void setCliCedula(String cliCedula) {
        this.cliCedula = cliCedula;
    }

 
    public BigDecimal getFacLecAnterior() {
        return facLecAnterior;
    }

    public void setFacLecAnterior(BigDecimal facLecAnterior) {
        this.facLecAnterior = facLecAnterior;
    }

    public BigDecimal getFacLecActual() {
        return facLecActual;
    }

    public void setFacLecActual(BigDecimal facLecActual) {
        this.facLecActual = facLecActual;
    }

    public BigDecimal getFacMetrosCubicos() {
        return facMetrosCubicos;
    }

    public void setFacMetrosCubicos(BigDecimal facMetrosCubicos) {
        this.facMetrosCubicos = facMetrosCubicos;
    }

    public String getFacMedidor() {
        return facMedidor;
    }

    public void setFacMedidor(String facMedidor) {
        this.facMedidor = facMedidor;
    }

    public Integer getFacLecMes() {
        return facLecMes;
    }

    public void setFacLecMes(Integer facLecMes) {
        this.facLecMes = facLecMes;
    }

    public String getMesText() {
        return mesText;
    }

    public void setMesText(String mesText) {
        this.mesText = mesText;
    }

    public String getDetMedidor() {
        return detMedidor;
    }

    public void setDetMedidor(String detMedidor) {
        this.detMedidor = detMedidor;
    }

    public String getDetDescripcion() {
        return detDescripcion;
    }

    public void setDetDescripcion(String detDescripcion) {
        this.detDescripcion = detDescripcion;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

   

   
}
