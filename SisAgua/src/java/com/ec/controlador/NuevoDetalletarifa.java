/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.DetalleTarifa;
import com.ec.entidad.Tarifa;
import com.ec.servicio.ServicioDetalleTarifas;
import com.ec.servicio.ServicioPropietario;
import com.ec.untilitario.ParamTarifaDetalle;
import java.math.BigDecimal;
import java.util.Date;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author gato
 */
public class NuevoDetalletarifa {

    private DetalleTarifa entidad = new DetalleTarifa();
    ServicioDetalleTarifas serServicioDetalleTarifa = new ServicioDetalleTarifas();
    ServicioPropietario servicioPropietario = new ServicioPropietario();
    private String accion = "create";
    private String precioPorcentaje = "TRUE";

    private Tarifa tarifaSelected;

    @Wire
    Window wDettarifa;

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") ParamTarifaDetalle valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        if (valor.getDetalleTarifa() != null) {
            this.entidad = valor.getDetalleTarifa();
            accion = "update";
            tarifaSelected = valor.getTarifa();
            precioPorcentaje = entidad.getDettValidadesecho() ? "TRUE" : "FALSE";

        } else {
            this.entidad = new DetalleTarifa();
            entidad.setDettFecha(new Date());
            entidad.setDettPorcentajeExcedente(BigDecimal.ZERO);
            tarifaSelected = valor.getTarifa();
            accion = "create";
//            entidad.setDettPorcentajeAlcantarillado(BigDecimal.valueOf(30));
//            entidad.setDettPorcentajeDesechos(BigDecimal.valueOf(30));
        }
    }

    @Command
    @NotifyChange({"entidad"})
    public void verificarPrecioPorcentaje() {
        if (precioPorcentaje.equals("TRUE")) {
            entidad.setDettValidadesecho(Boolean.TRUE);
        } else {
            entidad.setDettValidadesecho(Boolean.FALSE);
        }
    }

    @Command
    public void guardar() {
        if (tarifaSelected != null
                && entidad.getDettPrecioBase() != null
                && entidad.getDettPorcentajeExcedente() != null
                && entidad.getDettPorcentajeAlcantarillado() != null
                && entidad.getDettPorcentajeDesechos() != null
                && entidad.getDettDesechos() != null
                && entidad.getDettAmbiente() != null) {
            entidad.setIdTarifa(tarifaSelected);
            if (accion.equals("create")) {
                serServicioDetalleTarifa.crear(entidad);
                wDettarifa.detach();
            } else {
                serServicioDetalleTarifa.modificar(entidad);
                wDettarifa.detach();
            }

        } else {
            Messagebox.show("Verifique la informacion requerida", "Atención", Messagebox.OK, Messagebox.ERROR);
        }
    }

    public DetalleTarifa getEntidad() {
        return entidad;
    }

    public void setEntidad(DetalleTarifa entidad) {
        this.entidad = entidad;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Tarifa getTarifaSelected() {
        return tarifaSelected;
    }

    public void setTarifaSelected(Tarifa tarifaSelected) {
        this.tarifaSelected = tarifaSelected;
    }

    public String getPrecioPorcentaje() {
        return precioPorcentaje;
    }

    public void setPrecioPorcentaje(String precioPorcentaje) {
        this.precioPorcentaje = precioPorcentaje;
    }

}
