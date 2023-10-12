/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.RetencionCompra;
import com.ec.servicio.ServicioRetencionCompra;
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
public class CambioEstadoRetencion {

    @Wire
    Window windowEstFact;
    private RetencionCompra entidad;
    private String estado;
    private String descripcionAnula;
    ServicioRetencionCompra servicioRetencionCompra = new ServicioRetencionCompra();

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") RetencionCompra valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        this.entidad = valor;
        estado = valor.getDrcEstadosri() != null ? valor.getDrcEstadosri() : "";
    }

    @Command
    public void guardar() {

//            facturar.setEstadosri(estado);
//            facturar.setMensajesri(descripcionAnula);
        servicioRetencionCompra.modificar(entidad);

        Clients.showNotification("Guardado correctamente",
                    Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 1000, true);
        windowEstFact.detach();
//        } else {
//            Clients.showNotification("Verifique el estado de la factura",
//                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
//            windowEstFact.detach();
//        }

    }

    public RetencionCompra getEntidad() {
        return entidad;
    }

    public void setEntidad(RetencionCompra entidad) {
        this.entidad = entidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcionAnula() {
        return descripcionAnula;
    }

    public void setDescripcionAnula(String descripcionAnula) {
        this.descripcionAnula = descripcionAnula;
    }

}
