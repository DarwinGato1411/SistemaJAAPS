/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Propietario;
import com.ec.servicio.ServicioPropietario;
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
public class AdmPropietario {

    ServicioPropietario servicioPropietario = new ServicioPropietario();

    private List<Propietario> listaPropietarios = new ArrayList<Propietario>();

    private String buscar = "";

    public AdmPropietario() {

        findLikeCedulaNombre();

    }

    private void findLikeCedulaNombre() {
        listaPropietarios = servicioPropietario.findLikeCedulaNombre(buscar);
    }

    @Command
    @NotifyChange({"listaPropietarios", "buscar"})
    public void buscarPropietario() {
        findLikeCedulaNombre();
    }

    @Command
    @NotifyChange({"listaPropietarios", "buscar"})
    public void nuevo() {
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/nuevo/propietario.zul", null, null);
        window.doModal();
        findLikeCedulaNombre();
    }

    @Command
    @NotifyChange({"listaPropietarios", "buscar"})
    public void actualizar(@BindingParam("valor") Propietario valor) {

        final HashMap<String, Propietario> map = new HashMap<String, Propietario>();
        map.put("valor", valor);
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/nuevo/propietario.zul", null, map);
        window.doModal();
        findLikeCedulaNombre();
    }

    public List<Propietario> getListaPropietarios() {
        return listaPropietarios;
    }

    public void setListaPropietarios(List<Propietario> listaPropietarios) {
        this.listaPropietarios = listaPropietarios;
    }

    public String getBuscar() {
        return buscar;
    }

    public void setBuscar(String buscar) {
        this.buscar = buscar;
    }

}
