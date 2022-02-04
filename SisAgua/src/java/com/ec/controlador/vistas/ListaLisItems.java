/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.vistas;

import com.ec.vista.servicios.ServicioListadoItems;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import com.ec.untilitario.ArchivoUtils;
import com.ec.vistas.ListadoItems;

/**
 *
 * @author HC
 */
public class ListaLisItems {
    ServicioListadoItems servicioListadoItems = new ServicioListadoItems();
     private List<ListadoItems> listaListItems = new ArrayList<ListadoItems>();
    
    @Command
    @NotifyChange({"listaListItems"})
    public void buscarListadoItems() {

      consultaItems();

    }
    private void consultaItems() {
      
        listaListItems  = servicioListadoItems.listadoTotal();

    }

    public ServicioListadoItems getServicioListadoItems() {
        return servicioListadoItems;
    }

    public void setServicioListadoItems(ServicioListadoItems servicioListadoItems) {
        this.servicioListadoItems = servicioListadoItems;
    }

    public List<ListadoItems> getListaListItems() {
        return listaListItems;
    }

    public void setListaListItems(List<ListadoItems> listaListItems) {
        this.listaListItems = listaListItems;
    }


    
}
