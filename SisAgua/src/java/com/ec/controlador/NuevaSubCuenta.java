/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;


import com.ec.entidad.contabilidad.CuCuenta;
import com.ec.entidad.contabilidad.CuSubCuenta;


import com.ec.servicio.contabilidad.ServicioCuenta;
import com.ec.servicio.contabilidad.ServicioSubCuenta;

import com.ec.untilitario.ParamCuenta;
import com.ec.untilitario.ParamSubCuenta;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

/**
 *
 * @author HC
 */
public class NuevaSubCuenta {
    
    @Wire
    Window wSubCuenta;
    private CuSubCuenta entidad = new CuSubCuenta();
    private CuCuenta cuCuenta = new CuCuenta();
    private String accion = "create";
    
    ServicioSubCuenta servicioSubCuenta = new ServicioSubCuenta();
    
    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") ParamSubCuenta valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        
        if (!valor.getAccion().equals("create")) {
            this.entidad = valor.getCuSubCuenta();
            System.out.println(" UPDATE  SubCuenta " + valor.getCuCuenta().getGrupNombre());
            accion = "update";
        } else {
            this.entidad = new CuSubCuenta();
            cuCuenta = valor.getCuCuenta();
            System.out.println(" CREATE Cuenta " + valor.getCuCuenta().getGrupNombre());
            accion = "create";
        }
        
    }
    
    @Command
    public void guardar() {
        if (entidad.getSubcNumero() != null
                && entidad.getSubcNombre() != null) {
            if (accion.equals("create")) {
                entidad.setIdCuenta(cuCuenta);
                servicioSubCuenta.crear(entidad);
                
                Clients.showNotification("Guardado correctamente",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
                wSubCuenta.detach();
            } else {
                servicioSubCuenta.modificar(entidad);
                Clients.showNotification("Modificado correctamente",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
                wSubCuenta.detach();
            }
            
        } else {
            Clients.showNotification("Verifique la informacion",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 3000, true);
        }
    }
    
    public CuSubCuenta getEntidad() {
        return entidad;
    }
    
    public void setEntidad(CuSubCuenta entidad) {
        this.entidad = entidad;
    }
    
    public String getAccion() {
        return accion;
    }
    
    public void setAccion(String accion) {
        this.accion = accion;
    }
    
}
