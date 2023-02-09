/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.dao;

import com.ec.entidad.contabilidad.CuSubCuenta;

/**
 *
 * @author HC
 */
public class CuSubCuentaDAO {
    private String codigo = "";
    private CuSubCuenta cusubcuenta = null;
    private String numero = "";
    private String nombre = "";

    public CuSubCuentaDAO() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public CuSubCuenta getCusubcuenta() {
        return cusubcuenta;
    }

    public void setCusubcuenta(CuSubCuenta cusubcuenta) {
        this.cusubcuenta = cusubcuenta;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
    
}
