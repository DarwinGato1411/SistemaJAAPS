/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.untilitario;

import com.ec.entidad.contabilidad.CuClase;
import com.ec.entidad.contabilidad.CuGrupo;

/**
 *
 * @author HC
 */
public class ParamGrupo {
    private CuClase cuClase;
    private CuGrupo cuGrupo;
    private String accion;

    public ParamGrupo() {
    }

    
    
    public ParamGrupo(CuClase cuClase) {
        this.cuClase = cuClase;
    }

    
    public ParamGrupo(CuClase cuClase, CuGrupo cuGrupo) {
        this.cuClase = cuClase;
        this.cuGrupo = cuGrupo;
    }

    public CuClase getCuClase() {
        return cuClase;
    }

    public void setCuClase(CuClase cuClase) {
        this.cuClase = cuClase;
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
