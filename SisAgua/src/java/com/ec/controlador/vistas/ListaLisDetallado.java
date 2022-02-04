/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.vistas;

import com.ec.untilitario.ArchivoUtils;
import com.ec.vista.servicios.ServicioListadoDetallado;
import com.ec.vista.servicios.ServicioListadoItems;
import com.ec.vistas.ListadoDetallado;
import com.ec.vistas.ListadoItems;
import com.ec.vistas.RotacionProducto;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

/**
 *
 * @author HC
 */
public class ListaLisDetallado {

    ServicioListadoDetallado servicioListadoDetallado = new ServicioListadoDetallado();

    private Date inicio = new Date();
    private Date fin = new Date();
    private BigDecimal totalVenta = BigDecimal.ZERO;

    private List<ListadoDetallado> listaListDetallado = new ArrayList<ListadoDetallado>();

    ServicioListadoItems servicioListadoItems = new ServicioListadoItems();
    private List<ListadoItems> listaListItems = new ArrayList<ListadoItems>();

    public ListaLisDetallado() {

        Calendar calendar = Calendar.getInstance(); //obtiene la fecha de hoy 
        calendar.add(Calendar.DATE, -6); //el -3 indica que se le restaran 3 dias 
        inicio = calendar.getTime();

        //fechainicioDiaria.setDate(-7);
        consultaDetalle();
        consultaItems();
    }

    @Command
    @NotifyChange({"listaListDetallado", "inicio", "fin"})
    public void buscarListadoDetallado() {

        consultaDetalle();

    }

    private void consultaDetalle() {
        totalVenta = BigDecimal.ZERO;
        listaListDetallado = servicioListadoDetallado.findByMes(inicio, fin);

        /*Calculo el total*/
        for (ListadoDetallado item : listaListDetallado) {
            totalVenta = totalVenta.add(item.getDetTotal());
        }
        /*coloco el porcentaje*/
        for (ListadoDetallado item : listaListDetallado) {
            BigDecimal itemporcient = BigDecimal.valueOf(100.0).multiply(item.getDetTotal());
            // BigDecimal porcentaje = itemporcient.divide(totalVenta, 4, RoundingMode.FLOOR);

            //item.setPorcentaje(ArchivoUtils.redondearDecimales(porcentaje, 2));
        }
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date fechainicio) {
        this.inicio = fechainicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fechafin) {
        this.fin = fechafin;
    }

    public ServicioListadoDetallado getServicioListadoDetallado() {
        return servicioListadoDetallado;
    }

    public void setServicioListadoDetallado(ServicioListadoDetallado servicioListadoDetallado) {
        this.servicioListadoDetallado = servicioListadoDetallado;
    }

    public List<ListadoDetallado> getListaListDetallado() {
        return listaListDetallado;
    }

    public void setListaListDetallado(List<ListadoDetallado> listaListDetallado) {
        this.listaListDetallado = listaListDetallado;
    }

    public BigDecimal getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(BigDecimal totalVenta) {
        this.totalVenta = totalVenta;
    }

    @Command
    @NotifyChange({"listaListItems"})
    public void buscarListadoItems() {

        consultaItems();

    }

    private void consultaItems() {

        listaListItems = servicioListadoItems.listadoTotal();

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
