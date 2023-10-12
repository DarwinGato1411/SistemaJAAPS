/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.contabilidad.CuClase;
import com.ec.entidad.contabilidad.CuGrupo;
import com.ec.servicio.contabilidad.ServicioGrupo;
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
public class NuevoGrupo {

    @Wire
    Window wGrupo;
    private CuGrupo entidad = new CuGrupo();
    private CuClase cuClase = new CuClase();
    private String accion = "create";

    ServicioGrupo servicioGrupo = new ServicioGrupo();

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") ParamGrupo valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);

        if (!valor.getAccion().equals("create")) {
            this.entidad = valor.getCuGrupo();
            System.out.println(" UPDATE  CLASE " + valor.getCuClase().getClasNombre());
            accion = "update";
        } else {
            this.entidad = new CuGrupo();
            this.entidad.setGrupOtro(BigDecimal.ZERO);
            this.entidad.setGrupSaldo(BigDecimal.ZERO);
            this.entidad.setGrupTotal(BigDecimal.ZERO);
            cuClase = valor.getCuClase();
            System.out.println(" CREATE CLASE " + valor.getCuClase().getClasNombre());
            accion = "create";
        }

    }

    @Command
    public void guardar() {
        if (entidad.getGrupNumero() != null
                    && entidad.getGrupNombre() != null) {
            if (accion.equals("create")) {
                entidad.setCuClase(cuClase);
                servicioGrupo.crear(entidad);

                Clients.showNotification("Guardado correctamente",
                            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
                wGrupo.detach();
            } else {
                servicioGrupo.modificar(entidad);
                Clients.showNotification("Modificado correctamente",
                            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
                wGrupo.detach();
            }

        } else {
            Clients.showNotification("Verifique la informacion",
                        Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 3000, true);
        }
    }

    public CuGrupo getEntidad() {
        return entidad;
    }

    public void setEntidad(CuGrupo entidad) {
        this.entidad = entidad;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

}
