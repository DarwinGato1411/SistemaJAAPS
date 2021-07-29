/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Medidor;
import com.ec.entidad.Predio;
import com.ec.entidad.Tarifa;
import com.ec.entidad.UbicacionMedidor;
import com.ec.servicio.ServicioMedidor;
import com.ec.servicio.ServicioPredio;
import com.ec.servicio.ServicioTarifa;
import com.ec.servicio.ServicioUbicacionMedidor;
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
import org.zkoss.zul.Window;

/**
 *
 * @author gato
 */
public class NuevoMedidor {
    
    @Wire
    Window wMedidor;
    private ListModelList<Predio> listaPredioModel;
    private List<Predio> listaPredios = new ArrayList<Predio>();
    private Set<Predio> registrosSeleccionados = new HashSet<Predio>();
    ServicioPredio servicioPredio = new ServicioPredio();
    private Medidor entidad = new Medidor();
    ServicioMedidor servicioMedidor = new ServicioMedidor();
    private String accion = "create";
    private String buscarPredio = "";
    
    ServicioTarifa servicioTarifa = new ServicioTarifa();
    private List<Tarifa> listatarifas = new ArrayList<Tarifa>();
    private Tarifa tarifaSelected = null;
    
    ServicioUbicacionMedidor servicioUbicacionMedidor = new ServicioUbicacionMedidor();
    private List<UbicacionMedidor> listaUbicacion = new ArrayList<UbicacionMedidor>();
    private UbicacionMedidor ubicacionSelected=null;
    
    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") Medidor valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        if (valor != null) {
            this.entidad = valor;
            accion = "update";
            tarifaSelected = valor.getIdTarifa();
            listaPredios.add(valor.getIdPredio());
            setListaPredioModel(new ListModelList<Predio>(getListaPredios()));
            ((ListModelList<Predio>) listaPredioModel).setMultiple(false);
            listaPredioModel.setSelection(listaPredios);
            ubicacionSelected=valor.getIdUbicacionMedidor()!=null?valor.getIdUbicacionMedidor():null;
        } else {
            this.entidad = new Medidor();
            entidad.setMedFechaRegistro(new Date());
            entidad.setMedNumero("");
            accion = "create";
            Medidor ultimoMedidor = servicioMedidor.findUltimoMedidorRegistrado();
            if (ultimoMedidor != null) {
                try {
                    entidad.setMedNumero(String.valueOf(Integer.valueOf(ultimoMedidor.getMedNumero().trim()) + 1));
                } catch (NumberFormatException e) {
                    entidad.setMedNumero("");
                }
            }
            obtenerPredios();
           
        }
        
        listatarifas = servicioTarifa.findLikeTariDescripcion("");
         listaUbicacion = servicioUbicacionMedidor.findByNombre("");
    }
    
    private void findLikePredio() {
        listaPredios = servicioPredio.findLikePreDireccionCedulaNombre(buscarPredio);
    }
    
    private void obtenerPredios() {
        findLikePredio();
        setListaPredioModel(new ListModelList<Predio>(getListaPredios()));
        ((ListModelList<Predio>) listaPredioModel).setMultiple(false);
        //listaPropietariosModel.addToSelection(entidad.getIdPropietario());
    }
    
    @Command
    @NotifyChange("entidad")
    public void seleccionarRegistros() {
        Predio predioSelected = null;
        
        registrosSeleccionados = ((ListModelList<Predio>) getListaPredioModel()).getSelection();
        for (Predio item : registrosSeleccionados) {
            predioSelected = item;
            
        }
        entidad.setIdPredio(predioSelected);
        
    }
    
    @Command
    @NotifyChange({"listaPredioModel", "buscarPredio"})
    public void buscarPropietario() {
        obtenerPredios();
    }
    
    @Command
    public void guardar() {
        entidad.setIdTarifa(tarifaSelected);
        if (entidad.getIdTarifa() != null
                && entidad.getIdPredio() != null
                && !entidad.getMedNumero().equals("")
                &&ubicacionSelected!=null) {
            entidad.setIdUbicacionMedidor(ubicacionSelected);
            entidad.setMedBarrio(ubicacionSelected.getUbimNombre());
            if (accion.equals("ceate")) {
                
                servicioMedidor.crear(entidad);
            } else {
                servicioMedidor.modificar(entidad);
            }
            wMedidor.detach();
        } else {
            
            Clients.showNotification("Verifique la informacion requerida",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
        }
    }
    
    public ListModelList<Predio> getListaPredioModel() {
        return listaPredioModel;
    }
    
    public void setListaPredioModel(ListModelList<Predio> listaPredioModel) {
        this.listaPredioModel = listaPredioModel;
    }
    
    public List<Predio> getListaPredios() {
        return listaPredios;
    }
    
    public void setListaPredios(List<Predio> listaPredios) {
        this.listaPredios = listaPredios;
    }
    
    public Set<Predio> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }
    
    public void setRegistrosSeleccionados(Set<Predio> registrosSeleccionados) {
        this.registrosSeleccionados = registrosSeleccionados;
    }
    
    public String getBuscarPredio() {
        return buscarPredio;
    }
    
    public void setBuscarPredio(String buscarPredio) {
        this.buscarPredio = buscarPredio;
    }
    
    public Medidor getEntidad() {
        return entidad;
    }
    
    public void setEntidad(Medidor entidad) {
        this.entidad = entidad;
    }
    
    public String getAccion() {
        return accion;
    }
    
    public void setAccion(String accion) {
        this.accion = accion;
    }
    
    public List<Tarifa> getListatarifas() {
        return listatarifas;
    }
    
    public void setListatarifas(List<Tarifa> listatarifas) {
        this.listatarifas = listatarifas;
    }
    
    public Tarifa getTarifaSelected() {
        return tarifaSelected;
    }
    
    public void setTarifaSelected(Tarifa tarifaSelected) {
        this.tarifaSelected = tarifaSelected;
    }
    
    public List<UbicacionMedidor> getListaUbicacion() {
        return listaUbicacion;
    }
    
    public void setListaUbicacion(List<UbicacionMedidor> listaUbicacion) {
        this.listaUbicacion = listaUbicacion;
    }
    
    public UbicacionMedidor getUbicacionSelected() {
        return ubicacionSelected;
    }
    
    public void setUbicacionSelected(UbicacionMedidor ubicacionSelected) {
        this.ubicacionSelected = ubicacionSelected;
    }
    
}
