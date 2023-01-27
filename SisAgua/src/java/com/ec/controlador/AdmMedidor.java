/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Medidor;
import com.ec.servicio.ServicioMedidor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author gato
 */
public class AdmMedidor {

    ServicioMedidor servicioMedidor = new ServicioMedidor();

    private List<Medidor> listaDatos = new ArrayList<Medidor>();

    private String buscarNombre = "";
    private String buscarNumero = "";

    public AdmMedidor() {

        findMedidorPorNombreApellidoCedula();

    }

    private void findMedidorPorNombreApellidoCedula() {
        listaDatos = servicioMedidor.findLikeNombreApellidoCedula(buscarNombre);
    }

    private void findMedidorPorNumero() {
        listaDatos = servicioMedidor.findMedidorNumero(buscarNumero);
    }

    @Command
    @NotifyChange({"listaDatos", "buscarNombre"})
    public void buscarMedidorNombreApellidoNumero() {
        findMedidorPorNombreApellidoCedula();
    }

    @Command
    @NotifyChange({"listaDatos", "buscarNumero"})
    public void buscarMedidorNumero() {
        findMedidorPorNumero();
    }

    @Command
    @NotifyChange({"listaDatos", "buscarNombre"})
    public void nuevo() {
        buscarNombre = "";
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/nuevo/medidor.zul", null, null);
        window.doModal();
        findMedidorPorNombreApellidoCedula();
    }

    @Command
    @NotifyChange({"listaDatos", "buscarNombre"})
    public void actualizar(@BindingParam("valor") Medidor valor) {
        buscarNombre = "";
        final HashMap<String, Medidor> map = new HashMap<String, Medidor>();
        map.put("valor", valor);
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/nuevo/medidor.zul", null, map);
        window.doModal();
        findMedidorPorNombreApellidoCedula();
    }

    @Command
    @NotifyChange({"listaDatos", "buscarNombre"})
    public void activarDesactivar(@BindingParam("valor") Medidor valor) {
//        if (Messagebox.show("¿Desea activar o desactiva el medidor?", "Question", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION) == Messagebox.OK) {

            
               
                servicioMedidor.modificar(valor);
            
           

//        }

    }

    public String getBuscarNombre() {
        return buscarNombre;
    }

    public List<Medidor> getListaDatos() {
        return listaDatos;
    }

    public void setListaDatos(List<Medidor> listaDatos) {
        this.listaDatos = listaDatos;
    }

    public void setBuscarNombre(String buscarNombre) {
        this.buscarNombre = buscarNombre;
    }

    public String getBuscarNumero() {
        return buscarNumero;
    }

    public void setBuscarNumero(String buscarNumero) {
        this.buscarNumero = buscarNumero;
    }

}
