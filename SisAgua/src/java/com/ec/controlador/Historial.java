/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.HistorialPagoAgua;
import com.ec.entidad.Medidor;
import com.ec.servicio.ServicioHistorialVentaAgua;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

/**
 *
 * @author gato
 */
public class Historial {

    @Wire
    Window windowHistorico;
    
    ServicioHistorialVentaAgua servicioHistorialVentaAgua = new ServicioHistorialVentaAgua();
    private List<HistorialPagoAgua> listaDatosHistorial = new ArrayList<HistorialPagoAgua>();
    private String buscarHistorial = "";

    public Historial() {
        
    }
     @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") Medidor medidor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        listaDatosHistorial = servicioHistorialVentaAgua.findHistorial(medidor.getMedNumero());

    }

    public List<HistorialPagoAgua> getListaDatosHistorial() {
        return listaDatosHistorial;
    }

    public void setListaDatosHistorial(List<HistorialPagoAgua> listaDatosHistorial) {
        this.listaDatosHistorial = listaDatosHistorial;
    }

   

    public String getBuscarHistorial() {
        return buscarHistorial;
    }

    public void setBuscarHistorial(String buscarHistorial) {
        this.buscarHistorial = buscarHistorial;
    }

    
}
