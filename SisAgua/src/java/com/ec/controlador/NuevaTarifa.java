/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Tarifa;
import com.ec.servicio.ServicioTarifa;
import java.util.Date;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author gato
 */
public class NuevaTarifa {

    private Tarifa entidad = new Tarifa();
    ServicioTarifa servicioTarifa = new ServicioTarifa();
    private String accion = "create";
    @Wire
    Window wTarifa;

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") Tarifa valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        if (valor != null) {
            this.entidad = valor;
            accion = "update";
        } else {
            this.entidad = new Tarifa();
            entidad.setTariEstado(Boolean.TRUE);
            accion = "create";

        }

    }

    @Command
    public void guardar() {
        if (entidad.getTariDescripcion() != null
                && entidad.getTariSigla() != null) {
            entidad.setTariFecha(new Date());

            if (accion.equals("create")) {
                servicioTarifa.crear(entidad);
                //  Messagebox.show("Guardado con exito");

                wTarifa.detach();
            } else {
                servicioTarifa.modificar(entidad);
                // Messagebox.show("Guardado con exito");

                wTarifa.detach();
            }

        } else {
            Messagebox.show("Verifique la informacion requerida", "Atención", Messagebox.OK, Messagebox.ERROR);
        }
    }

    public Tarifa getEntidad() {
        return entidad;
    }

    public void setEntidad(Tarifa entidad) {
        this.entidad = entidad;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

}
