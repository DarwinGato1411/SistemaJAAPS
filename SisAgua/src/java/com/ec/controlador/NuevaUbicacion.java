/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.UbicacionMedidor;
import com.ec.servicio.ServicioCliente;
import com.ec.servicio.ServicioPredio;
import com.ec.servicio.ServicioUbicacionMedidor;
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
 * @author gato
 */
public class NuevaUbicacion {

    @Wire
    Window wUbicacionMed;
    private UbicacionMedidor entidad = new UbicacionMedidor();
    ServicioUbicacionMedidor servicioUbicacionMedidor = new ServicioUbicacionMedidor();
    
    private String accion = "create";

    ServicioCliente servicioCliente = new ServicioCliente();
    ServicioPredio servicioPredio = new ServicioPredio();

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") UbicacionMedidor valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
       
        if (valor != null) {
            this.entidad = valor;
            accion = "update";
        } else {
            this.entidad = new UbicacionMedidor();
            accion = "create";

        }
    }

   
    @Command
    public void guardar() {
        if (entidad.getUbimNombre()!= null) {
            
            if (accion.equals("create")) {
                servicioUbicacionMedidor.crear(entidad);
               
                wUbicacionMed.detach();
            } else {
                servicioUbicacionMedidor.modificar(entidad);
                // Messagebox.show("Guardado con exito");

                wUbicacionMed.detach();
            }

        } else {

            Clients.showNotification("Verifique la informacion requerida",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
        }
    }

   
    public UbicacionMedidor getEntidad() {
        return entidad;
    }

    public void setEntidad(UbicacionMedidor entidad) {
        this.entidad = entidad;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

}
