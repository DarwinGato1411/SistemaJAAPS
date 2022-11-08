/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.contabilidad.CuClase;
import com.ec.servicio.contabilidad.ServicioClase;
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
public class NuevaClase {

    @Wire
    Window wClase;
    private CuClase entidad = new CuClase();
    private String accion = "create";

    ServicioClase servicioClase = new ServicioClase();

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") CuClase valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);

        if (valor != null) {
            this.entidad = valor;

            accion = "update";
        } else {
            this.entidad = new CuClase();
            this.entidad.setClasOtro(BigDecimal.ZERO);
            this.entidad.setClasSaldo(BigDecimal.ZERO);
            this.entidad.setClasTotal(BigDecimal.ZERO);
            accion = "create";
        }

    }

    @Command
    public void guardar() {
        if (entidad.getClasNumero() != null
                    && entidad.getClasNombre() != null) {
            if (accion.equals("create")) {
                servicioClase.crear(entidad);
                Clients.showNotification("Guardado correctamente",
                            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
                wClase.detach();
            } else {
                servicioClase.modificar(entidad);
                Clients.showNotification("Modificado correctamente",
                            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
                wClase.detach();
            }

        } else {
            Clients.showNotification("Verifique la informacion",
                        Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 3000, true);
        }
    }

    public CuClase getEntidad() {
        return entidad;
    }

    public void setEntidad(CuClase entidad) {
        this.entidad = entidad;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

}
