/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Medidor;
import com.ec.entidad.UbicacionMedidor;
import com.ec.servicio.ServicioUbicacionMedidor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;

/**
 *
 * @author gato
 */
public class AdmUbicacionMedidor {

    ServicioUbicacionMedidor servicioUbicacionMedidor = new ServicioUbicacionMedidor();

    private List<UbicacionMedidor> listaDatos = new ArrayList<UbicacionMedidor>();

    private String buscarNombre = "";    

    public AdmUbicacionMedidor() {

        findByNombre();

    }

    private void findByNombre() {
        listaDatos = servicioUbicacionMedidor.findByNombre(buscarNombre);
    }


    @Command
    @NotifyChange({"listaDatos", "buscarNombre"})
    public void buscarPorNombre() {
        findByNombre();
    }
   

    @Command
    @NotifyChange({"listaDatos", "buscarNombre"})
    public void nuevo() {
        buscarNombre = "";
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/nuevo/ubicacionmed.zul", null, null);
        window.doModal();
        findByNombre();
    }

    @Command
    @NotifyChange({"listaDatos", "buscarNombre"})
    public void actualizar(@BindingParam("valor") UbicacionMedidor valor) {
        buscarNombre = "";
        final HashMap<String, UbicacionMedidor> map = new HashMap<String, UbicacionMedidor>();
        map.put("valor", valor);
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/nuevo/ubicacionmed.zul", null, map);
        window.doModal();
        findByNombre();
    }

  
    public String getBuscarNombre() {
        return buscarNombre;
    }

    public List<UbicacionMedidor> getListaDatos() {
        return listaDatos;
    }

    public void setListaDatos(List<UbicacionMedidor> listaDatos) {
        this.listaDatos = listaDatos;
    }

    public void setBuscarNombre(String buscarNombre) {
        this.buscarNombre = buscarNombre;
    }


    

}
