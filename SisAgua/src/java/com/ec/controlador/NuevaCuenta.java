/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;


import com.ec.entidad.contabilidad.CuCuenta;
import com.ec.entidad.contabilidad.CuGrupo;

import com.ec.servicio.contabilidad.ServicioCuenta;
import com.ec.servicio.contabilidad.ServicioGrupo;
import com.ec.untilitario.ParamCuenta;
import com.ec.untilitario.ParamGrupo;
import java.math.BigDecimal;
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
public class NuevaCuenta {
    
    @Wire
    Window wCuenta;
    private CuCuenta entidad = new CuCuenta();
    private CuGrupo cuGrupo = new CuGrupo();
    private String accion = "create";
    
    ServicioCuenta servicioCuenta = new ServicioCuenta();
    
    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") ParamCuenta valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        
        if (!valor.getAccion().equals("create")) {
            this.entidad = valor.getCuCuenta();
            System.out.println(" UPDATE  Cuenta " + valor.getCuGrupo().getGrupNombre());
            accion = "update";
        } else {
            this.entidad = new CuCuenta();
            this.entidad.setGrupOtro(BigDecimal.ZERO);
            this.entidad.setGrupTotal(BigDecimal.ZERO);
            this.entidad.setGrupSaldo(BigDecimal.ZERO);
            cuGrupo = valor.getCuGrupo();
            System.out.println(" CREATE Cuenta " + valor.getCuGrupo().getGrupNombre());
            accion = "create";
        }
        
    }
    
    @Command
    public void guardar() {
        if (entidad.getGrupNumero() != null
                && entidad.getGrupNombre() != null) {
            if (accion.equals("create")) {
                entidad.setIdGrupo(cuGrupo);
                servicioCuenta.crear(entidad);
                
                Clients.showNotification("Guardado correctamente",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
                wCuenta.detach();
            } else {
                servicioCuenta.modificar(entidad);
                Clients.showNotification("Modificado correctamente",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
                wCuenta.detach();
            }
            
        } else {
            Clients.showNotification("Verifique la informacion",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 3000, true);
        }
    }
    
    public CuCuenta getEntidad() {
        return entidad;
    }
    
    public void setEntidad(CuCuenta entidad) {
        this.entidad = entidad;
    }
    
    public String getAccion() {
        return accion;
    }
    
    public void setAccion(String accion) {
        this.accion = accion;
    }
    
}
