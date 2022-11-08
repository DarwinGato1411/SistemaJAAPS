/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.dao;

import com.ec.entidad.contabilidad.CuSubCuenta;
import java.math.BigDecimal;

/**
 *
 * @author Darwin
 */
public class NuevoAsientoManual {

    private CuSubCuenta cuSubCuentaSelected;
    private String referencia;
    private String documento;
    private String observacion;
    private BigDecimal debe;
    private BigDecimal haber;

    public NuevoAsientoManual() {
    }

    public NuevoAsientoManual(CuSubCuenta cuSubCuentaSelected, String referencia, String documento, String observacion) {
        this.cuSubCuentaSelected = cuSubCuentaSelected;
        this.referencia = referencia;
        this.documento = documento;
        this.observacion = observacion;
    }

    public NuevoAsientoManual(CuSubCuenta cuSubCuentaSelected, String referencia, String documento, String observacion, BigDecimal debe, BigDecimal haber) {
        this.cuSubCuentaSelected = cuSubCuentaSelected;
        this.referencia = referencia;
        this.documento = documento;
        this.observacion = observacion;
        this.debe = debe;
        this.haber = haber;
    }

    public CuSubCuenta getCuSubCuentaSelected() {
        return cuSubCuentaSelected;
    }

    public void setCuSubCuentaSelected(CuSubCuenta cuSubCuentaSelected) {
        this.cuSubCuentaSelected = cuSubCuentaSelected;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

}
