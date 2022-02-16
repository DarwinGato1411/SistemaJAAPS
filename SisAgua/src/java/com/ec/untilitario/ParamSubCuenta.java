/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.untilitario;

import com.ec.entidad.contabilidad.CuCuenta;
import com.ec.entidad.contabilidad.CuSubCuenta;



/**
 *
 * @author HC
 */
public class ParamSubCuenta {
    private CuCuenta cuCuenta;
    private CuSubCuenta cuSubCuenta;
    private String accion;

    public ParamSubCuenta() {
    }

    public ParamSubCuenta(CuCuenta cuCuenta, CuSubCuenta cuSubCuenta) {
        this.cuCuenta = cuCuenta;
        this.cuSubCuenta = cuSubCuenta;
    }

    public ParamSubCuenta(CuCuenta cuCuenta) {
        this.cuCuenta = cuCuenta;
    }
    
    

    public CuCuenta getCuCuenta() {
        return cuCuenta;
    }

    public void setCuCuenta(CuCuenta cuCuenta) {
        this.cuCuenta = cuCuenta;
    }

    public CuSubCuenta getCuSubCuenta() {
        return cuSubCuenta;
    }

    public void setCuSubCuenta(CuSubCuenta cuSubCuenta) {
        this.cuSubCuenta = cuSubCuenta;
    }

    
    

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }
    
    
    
}
