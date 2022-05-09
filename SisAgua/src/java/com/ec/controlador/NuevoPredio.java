/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Predio;
import com.ec.entidad.Propietario;
import com.ec.servicio.ServicioPredio;
import com.ec.servicio.ServicioPropietario;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author gato
 */
public class NuevoPredio {

    private Predio entidad = new Predio();
    ServicioPredio servicioPredio = new ServicioPredio();
    ServicioPropietario servicioPropietario = new ServicioPropietario();
    private String accion = "create";

    private ListModelList<Propietario> listaPropietariosModel;
    private List<Propietario> listaPropietarios = new ArrayList<Propietario>();
    private Set<Propietario> registrosSeleccionados = new HashSet<Propietario>();

    private String buscarPropietario = "";

    @Wire
    Window wPredio;

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") Predio valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        if (valor != null) {
            this.entidad = valor;
            accion = "update";
            listaPropietarios.add(valor.getIdPropietario());
            setListaPropietariosModel(new ListModelList<Propietario>(getListaPropietarios()));
            ((ListModelList<Propietario>) listaPropietariosModel).setMultiple(false);
            listaPropietariosModel.setSelection(listaPropietarios);

        } else {
            this.entidad = new Predio();
            entidad.setPreFechaRegistro(new Date());
            accion = "create";
            obtenerPropietarios();
        }

    }

    @Command
    public void guardar() {
        if (entidad.getIdPropietario() != null
                && entidad.getPredDireccion() != null) {
            if (accion.equals("create")) {
                servicioPredio.crear(entidad);
                 Clients.showNotification("Guardado correctamente",
                            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
                wPredio.detach();
            } else {
                servicioPredio.modificar(entidad);
                 Clients.showNotification("Modificado correctamente",
                            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
                wPredio.detach();
            }

        } else {
            Messagebox.show("Verifique la informacion requerida", "Atención", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"listaPropietariosModel", "buscarPropietario"})
    public void buscarPropietario() {
        obtenerPropietarios();
    }

    private void findLikeCedulaNombre() {
        listaPropietarios = servicioPropietario.findLikeCedulaNombre(buscarPropietario);
    }

    private void obtenerPropietarios() {
        findLikeCedulaNombre();
        setListaPropietariosModel(new ListModelList<Propietario>(getListaPropietarios()));
        ((ListModelList<Propietario>) listaPropietariosModel).setMultiple(false);
        //listaPropietariosModel.addToSelection(entidad.getIdPropietario());
    }

    @Command
    @NotifyChange("entidad")
    public void seleccionarRegistros() {
        Propietario propietarioSelected = null;

        registrosSeleccionados = ((ListModelList<Propietario>) getListaPropietariosModel()).getSelection();
        for (Propietario propietario : registrosSeleccionados) {
            propietarioSelected = propietario;

        }
        entidad.setIdPropietario(propietarioSelected);

    }

    public Predio getEntidad() {
        return entidad;
    }

    public void setEntidad(Predio entidad) {
        this.entidad = entidad;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public List<Propietario> getListaPropietarios() {
        return listaPropietarios;
    }

    public void setListaPropietarios(List<Propietario> listaPropietarios) {
        this.listaPropietarios = listaPropietarios;
    }

    public ListModelList<Propietario> getListaPropietariosModel() {
        return listaPropietariosModel;
    }

    public void setListaPropietariosModel(ListModelList<Propietario> listaPropietariosModel) {
        this.listaPropietariosModel = listaPropietariosModel;
    }

    public Set<Propietario> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    public void setRegistrosSeleccionados(Set<Propietario> registrosSeleccionados) {
        this.registrosSeleccionados = registrosSeleccionados;
    }

    public String getBuscarPropietario() {
        return buscarPropietario;
    }

    public void setBuscarPropietario(String buscarPropietario) {
        this.buscarPropietario = buscarPropietario;
    }

}
