/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.vistas;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author HC
 */
@Entity
@Table(name = "listado_detallado_ordenado")
public class ListadoDetalladoOrdenado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "id_factura")
    @Id
    private BigInteger idFactura;

    @Column(name = "fac_numero")
    @Id
    private BigInteger facNumero;

    @Column(name = "fac_fecha")
    @Temporal(TemporalType.DATE)
    private Date facFecha;

    @Column(name = "med_numero")
    private String medNumero;

    @Column(name = "prop_nombre")
    private String propNombre;

    @Column(name = "prop_apellido")
    private String propApellido;

    @Column(name = "fac_metros_cubicos")
    private BigDecimal facMetrosCubicos;

    @Column(name = "agua")
    private BigDecimal agua;

    @Column(name = "excedente")
    private BigDecimal excedente;

    @Column(name = "alcantarrillado")
    private BigDecimal alcantarrillado;

    @Column(name = "desechos")
    private BigDecimal desechos;

    @Column(name = "medio_ambiente")
    private BigDecimal medioAmbiente;

    @Column(name = "interes1")
    private BigDecimal interes1;

    @Column(name = "interes2")
    private BigDecimal interes2;

    @Column(name = "derechos")
    private BigDecimal derechos;
    @Column(name = "factibilidad")
    private BigDecimal factibilidad;
    @Column(name = "multas")
    private BigDecimal multas;
    @Column(name = "material")
    private BigDecimal material;
    @Column(name = "garantia")
    private BigDecimal garantia;
    @Column(name = "fac_total")
    private BigDecimal facTotal;
    @Column(name = "otros")
    private BigDecimal otros;

    public ListadoDetalladoOrdenado() {
    }

    public ListadoDetalladoOrdenado(BigInteger idFactura, BigInteger facNumero, Date facFecha, String medNumero, String propNombre, String propApellido, BigDecimal facMetrosCubicos, BigDecimal agua, BigDecimal excedente, BigDecimal alcantarrillado, BigDecimal desechos, BigDecimal medioAmbiente, BigDecimal interes1, BigDecimal interes2, BigDecimal facTotal) {
        this.idFactura = idFactura;
        this.facNumero = facNumero;
        this.facFecha = facFecha;
        this.medNumero = medNumero;
        this.propNombre = propNombre;
        this.propApellido = propApellido;
        this.facMetrosCubicos = facMetrosCubicos;
        this.agua = agua;
        this.excedente = excedente;
        this.alcantarrillado = alcantarrillado;
        this.desechos = desechos;
        this.medioAmbiente = medioAmbiente;
        this.interes1 = interes1;
        this.interes2 = interes2;
        this.facTotal = facTotal;
    }

    public ListadoDetalladoOrdenado(BigInteger idFactura, BigInteger facNumero, Date facFecha, String medNumero, String propNombre, String propApellido, BigDecimal facMetrosCubicos, BigDecimal agua, BigDecimal excedente, BigDecimal alcantarrillado, BigDecimal desechos, BigDecimal medioAmbiente, BigDecimal interes1, BigDecimal interes2, BigDecimal facTotal,
                BigDecimal derechos,
                BigDecimal multas,
                BigDecimal material,
                BigDecimal garantia
    ) {
        this.idFactura = idFactura;
        this.facNumero = facNumero;
        this.facFecha = facFecha;
        this.medNumero = medNumero;
        this.propNombre = propNombre;
        this.propApellido = propApellido;
        this.facMetrosCubicos = facMetrosCubicos;
        this.agua = agua;
        this.excedente = excedente;
        this.alcantarrillado = alcantarrillado;
        this.desechos = desechos;
        this.medioAmbiente = medioAmbiente;
        this.interes1 = interes1;
        this.interes2 = interes2;
        this.facTotal = facTotal;
        this.derechos = derechos;
        this.multas = multas;
        this.material = material;
        this.garantia = garantia;
    }

    public ListadoDetalladoOrdenado(BigInteger idFactura, BigInteger facNumero, Date facFecha, String medNumero, String propNombre, String propApellido, BigDecimal facMetrosCubicos, BigDecimal agua, BigDecimal excedente, BigDecimal alcantarrillado, BigDecimal desechos, BigDecimal medioAmbiente, BigDecimal interes1, BigDecimal interes2, BigDecimal facTotal,
                BigDecimal derechos,
                BigDecimal multas,
                BigDecimal material,
                BigDecimal garantia,
                BigDecimal otros
    ) {
        this.idFactura = idFactura;
        this.facNumero = facNumero;
        this.facFecha = facFecha;
        this.medNumero = medNumero;
        this.propNombre = propNombre;
        this.propApellido = propApellido;
        this.facMetrosCubicos = facMetrosCubicos;
        this.agua = agua;
        this.excedente = excedente;
        this.alcantarrillado = alcantarrillado;
        this.desechos = desechos;
        this.medioAmbiente = medioAmbiente;
        this.interes1 = interes1;
        this.interes2 = interes2;
        this.facTotal = facTotal;
        this.derechos = derechos;
        this.multas = multas;
        this.material = material;
        this.garantia = garantia;
        this.otros = otros;
    }

    public ListadoDetalladoOrdenado(BigInteger idFactura, BigInteger facNumero, Date facFecha, String medNumero, String propNombre, String propApellido, BigDecimal facMetrosCubicos, BigDecimal agua, BigDecimal excedente, BigDecimal alcantarrillado, BigDecimal desechos, BigDecimal medioAmbiente, BigDecimal interes1, BigDecimal interes2, BigDecimal facTotal,
                BigDecimal derechos,
                BigDecimal multas,
                BigDecimal material,
                BigDecimal garantia,
                BigDecimal otros,
                BigDecimal factibilidad
    ) {
        this.idFactura = idFactura;
        this.facNumero = facNumero;
        this.facFecha = facFecha;
        this.medNumero = medNumero;
        this.propNombre = propNombre;
        this.propApellido = propApellido;
        this.facMetrosCubicos = facMetrosCubicos;
        this.agua = agua;
        this.excedente = excedente;
        this.alcantarrillado = alcantarrillado;
        this.desechos = desechos;
        this.medioAmbiente = medioAmbiente;
        this.interes1 = interes1;
        this.interes2 = interes2;
        this.facTotal = facTotal;
        this.derechos = derechos;
        this.multas = multas;
        this.material = material;
        this.garantia = garantia;
        this.otros = otros;
        this.factibilidad = factibilidad;
    }

    public BigInteger getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(BigInteger idFactura) {
        this.idFactura = idFactura;
    }

    public BigInteger getFacNumero() {
        return facNumero;
    }

    public void setFacNumero(BigInteger facNumero) {
        this.facNumero = facNumero;
    }

    public Date getFacFecha() {
        return facFecha;
    }

    public void setFacFecha(Date facFecha) {
        this.facFecha = facFecha;
    }

    public String getMedNumero() {
        return medNumero;
    }

    public void setMedNumero(String medNumero) {
        this.medNumero = medNumero;
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

    public BigDecimal getFacMetrosCubicos() {
        return facMetrosCubicos;
    }

    public void setFacMetrosCubicos(BigDecimal facMetrosCubicos) {
        this.facMetrosCubicos = facMetrosCubicos;
    }

    public BigDecimal getAgua() {
        return agua;
    }

    public void setAgua(BigDecimal agua) {
        this.agua = agua;
    }

    public BigDecimal getExcedente() {
        return excedente;
    }

    public void setExcedente(BigDecimal excedente) {
        this.excedente = excedente;
    }

    public BigDecimal getAlcantarrillado() {
        return alcantarrillado;
    }

    public void setAlcantarrillado(BigDecimal alcantarrillado) {
        this.alcantarrillado = alcantarrillado;
    }

    public BigDecimal getDesechos() {
        return desechos;
    }

    public void setDesechos(BigDecimal desechos) {
        this.desechos = desechos;
    }

    public BigDecimal getMedioAmbiente() {
        return medioAmbiente;
    }

    public void setMedioAmbiente(BigDecimal medioAmbiente) {
        this.medioAmbiente = medioAmbiente;
    }

    public BigDecimal getInteres1() {
        return interes1;
    }

    public void setInteres1(BigDecimal interes1) {
        this.interes1 = interes1;
    }

    public BigDecimal getInteres2() {
        return interes2;
    }

    public void setInteres2(BigDecimal interes2) {
        this.interes2 = interes2;
    }

    public BigDecimal getFacTotal() {
        return facTotal;
    }

    public void setFacTotal(BigDecimal facTotal) {
        this.facTotal = facTotal;
    }

    public BigDecimal getDerechos() {
        return derechos;
    }

    public void setDerechos(BigDecimal derechos) {
        this.derechos = derechos;
    }

    public BigDecimal getMultas() {
        return multas;
    }

    public void setMultas(BigDecimal multas) {
        this.multas = multas;
    }

    public BigDecimal getMaterial() {
        return material;
    }

    public void setMaterial(BigDecimal material) {
        this.material = material;
    }

    public BigDecimal getGarantia() {
        return garantia;
    }

    public void setGarantia(BigDecimal garantia) {
        this.garantia = garantia;
    }

    public BigDecimal getOtros() {
        return otros;
    }

    public void setOtros(BigDecimal otros) {
        this.otros = otros;
    }

    public BigDecimal getFactibilidad() {
        return factibilidad;
    }

    public void setFactibilidad(BigDecimal factibilidad) {
        this.factibilidad = factibilidad;
    }

}
