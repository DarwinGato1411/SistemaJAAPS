/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Predio;
import com.ec.servicio.ServicioPredio;
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
public class AdmPredio {

    ServicioPredio servicioPredio = new ServicioPredio();

    private List<Predio> listaPredios = new ArrayList<Predio>();

    private String buscar = "";

    public AdmPredio() {

        findLikePreDireccionCedulaNombre();

    }

    private void findLikePreDireccionCedulaNombre() {
        listaPredios = servicioPredio.findLikePreDireccionCedulaNombre(buscar);
    }

    @Command
    @NotifyChange({"listaPredios", "buscar"})
    public void buscarPropietario() {
        findLikePreDireccionCedulaNombre();
    }

    @Command
    @NotifyChange({"listaPredios", "buscar"})
    public void nuevo() {
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/nuevo/predio.zul", null, null);
        window.doModal();
        findLikePreDireccionCedulaNombre();
    }

    @Command
    @NotifyChange({"listaPredios", "buscar"})
    public void actualizar(@BindingParam("valor") Predio valor) {

        final HashMap<String, Predio> map = new HashMap<String, Predio>();
        map.put("valor", valor);
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/nuevo/predio.zul", null, map);
        window.doModal();
        findLikePreDireccionCedulaNombre();
    }

    public List<Predio> getListaPredios() {
        return listaPredios;
    }

    public void setListaPredios(List<Predio> listaPredios) {
        this.listaPredios = listaPredios;
    }

    public String getBuscar() {
        return buscar;
    }

    public void setBuscar(String buscar) {
        this.buscar = buscar;
    }

}
