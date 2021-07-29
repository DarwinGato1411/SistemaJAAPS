/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.dao.DetalleFacturaDAO;
import com.ec.entidad.DetalleTarifa;
import com.ec.entidad.Tarifa;
import com.ec.servicio.ServicioDetalleTarifas;
import com.ec.servicio.ServicioTarifa;
import com.ec.untilitario.ParamTarifaDetalle;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;

/**
 *
 * @author gato
 */
public class AdmTarifa {

    ServicioTarifa servicioTarifa = new ServicioTarifa();
    private List<Tarifa> listaTarifa = new ArrayList<Tarifa>();
    private ListModelList<Tarifa> listaDetalleFacturaModel;
    private Set<Tarifa> registrosSeleccionados = new HashSet<Tarifa>();
    private String buscarNombre = "";
    private Tarifa tarifaSeleted = null;

    ServicioDetalleTarifas servicioDetalleTarifas = new ServicioDetalleTarifas();
    private List<DetalleTarifa> listaDetalleTarifa = new ArrayList<DetalleTarifa>();
    private String detalleNombre = "";

    public AdmTarifa() {

        getTarifas();

    }

    private void findLikeTifa() {
        listaTarifa = servicioTarifa.findLikeTariDescripcion(buscarNombre);
    }

    private void getTarifas() {
        findLikeTifa();
        setListaDetalleFacturaModel(new ListModelList<Tarifa>(getListaTarifa()));
        ((ListModelList<Tarifa>) listaDetalleFacturaModel).setMultiple(false);
        if (tarifaSeleted != null) {
            listaDetalleFacturaModel.addToSelection(tarifaSeleted);
        }
    }

    @Command
    @NotifyChange({"listaDetalleTarifa", "tarifaSeleted"})
    public void seleccionarRegistros() {
        registrosSeleccionados = ((ListModelList<Tarifa>) getListaDetalleFacturaModel()).getSelection();
        for (Tarifa registrosSeleccionado : registrosSeleccionados) {
            tarifaSeleted = registrosSeleccionado;
            break;
        }
        findDetalleLikeTifa();
    }

    @Command
    @NotifyChange({"listaDetalleFacturaModel", "buscarNombre"})
    public void buscarTarifa() {
        getTarifas();
    }

    @Command
    @NotifyChange({"listaDetalleFacturaModel", "buscarNombre"})
    public void nuevo() {
        buscarNombre = "";
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/nuevo/tarifa.zul", null, null);
        window.doModal();
        getTarifas();
    }

    @Command
    @NotifyChange({"listaDetalleFacturaModel", "buscarNombre"})
    public void actualizar(@BindingParam("valor") Tarifa valor) {
        buscarNombre = "";
        final HashMap<String, Tarifa> map = new HashMap<String, Tarifa>();
        map.put("valor", valor);
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/nuevo/tarifa.zul", null, map);
        window.doModal();
        getTarifas();
    }

    public List<Tarifa> getListaTarifa() {
        return listaTarifa;
    }

    public void setListaTarifa(List<Tarifa> listaTarifa) {
        this.listaTarifa = listaTarifa;
    }

    public String getBuscarNombre() {
        return buscarNombre;
    }

    public void setBuscarNombre(String buscarNombre) {
        this.buscarNombre = buscarNombre;
    }

    /*para detalle de tarifas*/
    private void findDetalleLikeTifa() {
        listaDetalleTarifa = servicioDetalleTarifas.findIdTarifa(tarifaSeleted);
    }

    @Command
    @NotifyChange({"listaTarifa", "buscarNombre"})
    public void buscarDetalleTarifa() {
        findLikeTifa();
    }

    @Command
    @NotifyChange({"listaDetalleTarifa", "buscarNombre"})
    public void nuevoDetalle() {
        buscarNombre = "";
        ParamTarifaDetalle valor = new ParamTarifaDetalle();
        valor.setTarifa(tarifaSeleted);

        final HashMap<String, ParamTarifaDetalle> map = new HashMap<String, ParamTarifaDetalle>();
        map.put("valor", valor);
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/nuevo/dettarifa.zul", null, map);
        window.doModal();
        findDetalleLikeTifa();
    }

    @Command
    @NotifyChange({"listaDetalleTarifa", "buscarNombre"})
    public void actualizarDetalle(@BindingParam("valor") DetalleTarifa valor) {
        buscarNombre = "";
        ParamTarifaDetalle valorparam = new ParamTarifaDetalle();
        valorparam.setTarifa(valor.getIdTarifa());
        valorparam.setDetalleTarifa(valor);
        final HashMap<String, ParamTarifaDetalle> map = new HashMap<String, ParamTarifaDetalle>();
        map.put("valor", valorparam);
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/nuevo/dettarifa.zul", null, map);
        window.doModal();
        findDetalleLikeTifa();
    }

    public ListModelList<Tarifa> getListaDetalleFacturaModel() {
        return listaDetalleFacturaModel;
    }

    public void setListaDetalleFacturaModel(ListModelList<Tarifa> listaDetalleFacturaModel) {
        this.listaDetalleFacturaModel = listaDetalleFacturaModel;
    }

    public Set<Tarifa> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    public void setRegistrosSeleccionados(Set<Tarifa> registrosSeleccionados) {
        this.registrosSeleccionados = registrosSeleccionados;
    }

    public List<DetalleTarifa> getListaDetalleTarifa() {
        return listaDetalleTarifa;
    }

    public void setListaDetalleTarifa(List<DetalleTarifa> listaDetalleTarifa) {
        this.listaDetalleTarifa = listaDetalleTarifa;
    }

    public String getDetalleNombre() {
        return detalleNombre;
    }

    public void setDetalleNombre(String detalleNombre) {
        this.detalleNombre = detalleNombre;
    }

    public Tarifa getTarifaSeleted() {
        return tarifaSeleted;
    }

    public void setTarifaSeleted(Tarifa tarifaSeleted) {
        this.tarifaSeleted = tarifaSeleted;
    }

}
