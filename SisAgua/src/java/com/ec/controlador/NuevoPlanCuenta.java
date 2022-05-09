/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Clases;
import com.ec.entidad.contabilidad.CuClase;
import com.ec.entidad.contabilidad.CuGrupo;




import com.ec.servicio.ServicioPlanCuentas;
import com.ec.servicio.contabilidad.ServicioClase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author HC
 */
public class NuevoPlanCuenta {

    private Clases clases = new Clases();
    ServicioPlanCuentas servicioPlanCuentas = new ServicioPlanCuentas();
    private String accion = "create";
    private String buscarNombre = "";
     ServicioClase servicioClase = new ServicioClase();
    private List<CuClase> listaCuClase = new ArrayList<CuClase>();
    private ListModelList<CuClase> listaClaseModel;
    private Set<CuClase> seleccionadosClase = new HashSet<CuClase>();
    private String buscarClase = "";
    private CuClase cSelected = null;
    
    @Wire
    Window windowCliente;
    @Wire    
    Textbox txtIvaRec;
    
    
    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") Clases clases, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        
        if (clases != null) {
            this.clases = clases;
            
            accion = "update";
        } else {
            this.clases = new Clases();
            this.clases.setClasnumero("");
            this.clases.setClasnombre("");
            this.clases.setClastotal(BigDecimal.ZERO);
            this.clases.setClassaldo(BigDecimal.ZERO);
            this.clases.setClasotro(BigDecimal.ZERO);
            
            accion = "create";
        }
        
    }    
    
    @Command
    public void guardarClase() {
        if (clases.getClasnumero()!= null
                && clases.getClasnombre() != null
                && clases.getClastotal() != null
                && clases.getClassaldo() != null
                && clases.getClasotro()!= null) {
            
            if (accion.equals("create")) {
                clases = new Clases();
                    clases.setClasnumero("");
                    clases.setClasnombre("");
                    clases.setClastotal(BigDecimal.ZERO);
                    clases.setClassaldo(BigDecimal.ZERO);
                    clases.setClasotro(BigDecimal.ZERO);
               windowCliente.detach();
                }
                
               
             }else {
            clases.setClasnumero(clases.getClasnumero());
                    clases.setClasnombre(clases.getClasnombre());
                    clases.setClastotal(clases.getClastotal());
                    clases.setClassaldo(clases.getClassaldo());
                    clases.setClasotro(clases.getClasotro());
                    
                    servicioPlanCuentas.modificar(clases);
               
                
                windowCliente.detach();
             }
        

}
     @Command
    @NotifyChange({"listaCuClase", "buscarClase"})
    public void nuevaClase() {
        buscarNombre = "";
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/contabilidad/clase.zul", null, null);
        window.doModal();

    }
  
    @Command
    @NotifyChange({"listaCuClase", "buscarClase"})
    public void actualizar(@BindingParam("valor") CuClase valor) {

        final HashMap<String, CuClase> map = new HashMap<String, CuClase>();
        map.put("valor", valor);
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/contabilidad/clase.zul", null, map);
        window.doModal();
        findClase();
    }
    
     private void findClase() {
        listaCuClase = servicioClase.findByNombre(buscarClase);
    }

}

    