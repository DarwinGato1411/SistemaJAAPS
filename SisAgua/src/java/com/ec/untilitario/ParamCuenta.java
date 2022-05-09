/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.untilitario;

import com.ec.entidad.contabilidad.CuCuenta;
import com.ec.entidad.contabilidad.CuGrupo;

/**
 *
 * @author HC
 */
public class ParamCuenta {
       private CuGrupo cuGrupo;
    private CuCuenta cuCuenta;
    private String accion;
    

    public ParamCuenta() {
    }

    public ParamCuenta(CuGrupo cuGrupo) {
        this.cuGrupo = cuGrupo;
    }

    public ParamCuenta(CuGrupo cuGrupo, CuCuenta cuCuenta) {
        this.cuGrupo = cuGrupo;
        this.cuCuenta = cuCuenta;
    }



    public CuCuenta getCuCuenta() {
        return cuCuenta;
    }

    public void setCuCuenta(CuCuenta cuCuenta) {
        this.cuCuenta = cuCuenta;
    }


    public CuGrupo getCuGrupo() {
        return cuGrupo;
    }

    public void setCuGrupo(CuGrupo cuGrupo) {
        this.cuGrupo = cuGrupo;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

}
