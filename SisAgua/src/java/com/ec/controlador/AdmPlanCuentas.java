/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.ec.dao.DetalleFacturaDAO;
import com.ec.entidad.Clases;
import com.ec.entidad.Producto;
import com.ec.entidad.contabilidad.AsientoContable;
import com.ec.entidad.contabilidad.CuClase;
import com.ec.entidad.contabilidad.CuCuenta;
import com.ec.entidad.contabilidad.CuGrupo;
import com.ec.entidad.contabilidad.CuSubCuenta;
import com.ec.servicio.contabilidad.ServicioAsientoContable;
import com.ec.servicio.contabilidad.ServicioClase;
import com.ec.servicio.contabilidad.ServicioCuenta;
import com.ec.servicio.contabilidad.ServicioGrupo;
import com.ec.servicio.contabilidad.ServicioSubCuenta;
import com.ec.untilitario.ParamCuenta;
import com.ec.untilitario.ParamGrupo;
import com.ec.untilitario.ParamSubCuenta;
import com.ec.vistas.ListadoDetallado;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author gato
 */
public class AdmPlanCuentas {

    private String accion = "create";

    /*Model de clase*/
    ServicioClase servicioClase = new ServicioClase();
    private List<CuClase> listaCuClase = new ArrayList<CuClase>();
    private ListModelList<CuClase> listaClaseModel;
    private Set<CuClase> seleccionadosClase = new HashSet<CuClase>();
    private String buscarClase = "";
    private CuClase cSelected = null;
    private CuClase cuClases = new CuClase();
    
    /*Model de grupo*/
    ServicioGrupo servicioGrupo = new ServicioGrupo();
    private List<CuGrupo> listaCuGrupos = new ArrayList<CuGrupo>();
    private ListModelList<CuGrupo> listaGrupoModel;
    private Set<CuGrupo> seleccionadosGrupo = new HashSet<CuGrupo>();
    private CuGrupo gSelected = null;
    private String buscarGrupo = "";
    private Boolean activarGrupo = Boolean.FALSE;

    /*Model de Cuenta*/
    ServicioCuenta servicioCuenta = new ServicioCuenta();
    private List<CuCuenta> listaCuCuenta = new ArrayList<CuCuenta>();
    private ListModelList<CuCuenta> listaCuentaModel = new ListModelList<CuCuenta>();
    private Set<CuCuenta> seleccionadosCuenta = new HashSet<CuCuenta>();
    private CuCuenta cuSelected = null;
    private Boolean activarCuenta = Boolean.FALSE;

    /*Model de SubCuenta*/
    ServicioSubCuenta servicioSubCuenta = new ServicioSubCuenta();
    private List<CuSubCuenta> listaCuSubCuenta = new ArrayList<CuSubCuenta>();
    private ListModelList<CuSubCuenta> listaSubCuentaModel = new ListModelList<CuSubCuenta>();
    private Set<CuSubCuenta> seleccionadosSubCuenta = new HashSet<CuSubCuenta>();
    private Boolean activarSubCuenta = Boolean.FALSE;
//    private CuSubCuenta cuSelected = null;
    
    ServicioAsientoContable seervicioAsientoContable = new ServicioAsientoContable();
     private List<AsientoContable> listaAsientoContable = new ArrayList<AsientoContable>();
    private String buscarAsientoContable = "";
    
    private Date inicioAC = new Date();
    private Date finAC = new Date();

     ServicioSubCuenta servicioSubCuenta2 = new ServicioSubCuenta();
    private List<CuSubCuenta> listaCuSubCuenta2 = new ArrayList<CuSubCuenta>();


    public AdmPlanCuentas() {
          Calendar calendar = Calendar.getInstance(); //obtiene la fecha de hoy 
        calendar.add(Calendar.DATE, -6); //el -3 indica que se le restaran 3 dias 
        inicioAC = calendar.getTime();

        //fechainicioDiaria.setDate(-7);
        //consultaAC();
        consultaCuSubCuentas();
        
        getClaseModel();
    }
    

    private void getClaseModel() {
        findClase();
        setListaClaseModel(new ListModelList<CuClase>(getListaCuClase()));
        ((ListModelList<CuClase>) listaClaseModel).setMultiple(false);
    }

    @Command
    @NotifyChange({"listaGrupoModel","activarGrupo"})
    public void seleccionarClase() {

        seleccionadosClase = ((ListModelList<CuClase>) getListaClaseModel()).getSelection();

        for (CuClase cuClase : seleccionadosClase) {
            cSelected = cuClase;
            activarGrupo=Boolean.TRUE;

        }
        gSelected = null;
        cuSelected = null;

        getGrupoModel();
        listaCuentaModel.clear();
        listaSubCuentaModel.clear();
    }

    private void getGrupoModel() {
        findGrupo();
        setListaGrupoModel(new ListModelList<CuGrupo>(getListaCuGrupos()));
        ((ListModelList<CuGrupo>) listaGrupoModel).setMultiple(false);
    }

    @Command
    @NotifyChange({"listaCuentaModel","activarCuenta"})
    public void seleccionarGrupo() {

        seleccionadosGrupo = ((ListModelList<CuGrupo>) getListaGrupoModel()).getSelection();

        for (CuGrupo cuGrupo : seleccionadosGrupo) {
            gSelected = cuGrupo;
            activarCuenta=Boolean.TRUE;
        }
        cuSelected = null;
        getCuentaModel();
        listaSubCuentaModel.clear();

    }

    private void getCuentaModel() {
        findCuenta();
        setListaCuentaModel(new ListModelList<CuCuenta>(getListaCuCuenta()));
        ((ListModelList<CuCuenta>) listaCuentaModel).setMultiple(false);
    }

    @Command
    @NotifyChange({"listaSubCuentaModel","activarSubCuenta"})
    public void seleccionarCuenta() {

        seleccionadosCuenta = ((ListModelList<CuCuenta>) getListaCuentaModel()).getSelection();

        for (CuCuenta cuCuenta : seleccionadosCuenta) {
            cuSelected = cuCuenta;
            activarSubCuenta=Boolean.TRUE;
        }
        getSubCuentaModel();
    }

    private void getSubCuentaModel() {
        findSubCuenta();
        setListaSubCuentaModel(new ListModelList<CuSubCuenta>(getListaCuSubCuenta()));
        ((ListModelList<CuSubCuenta>) listaSubCuentaModel).setMultiple(false);
    }

    @Command
    public void seleccionarSubCuenta() {

        seleccionadosCuenta = ((ListModelList<CuCuenta>) getListaCuentaModel()).getSelection();

    }

    public ListModelList<CuClase> getListaClaseModel() {
        return listaClaseModel;
    }

    public void setListaClaseModel(ListModelList<CuClase> listaClaseModel) {
        this.listaClaseModel = listaClaseModel;
    }

    public Set<CuClase> getSeleccionadosClase() {
        return seleccionadosClase;
    }

    public void setSeleccionadosClase(Set<CuClase> seleccionadosClase) {
        this.seleccionadosClase = seleccionadosClase;
    }

    private void findClase() {
        listaCuClase = servicioClase.findByNombre(buscarClase);
    }

    private void findGrupo() {
        listaCuGrupos = servicioGrupo.findByIdClase(cSelected);
    }

    private void findCuenta() {
        listaCuCuenta = servicioCuenta.findByIdGrupo(gSelected);
    }

    private void findSubCuenta() {
        listaCuSubCuenta = servicioSubCuenta.findByIdCuenta(cuSelected);
    }

    public CuClase getcSelected() {
        return cSelected;
    }

    public void setcSelected(CuClase cSelected) {
        this.cSelected = cSelected;
    }

    public ListModelList<CuGrupo> getListaGrupoModel() {
        return listaGrupoModel;
    }

    public void setListaGrupoModel(ListModelList<CuGrupo> listaGrupoModel) {
        this.listaGrupoModel = listaGrupoModel;
    }

    public Set<CuGrupo> getSeleccionadosGrupo() {
        return seleccionadosGrupo;
    }

    public void setSeleccionadosGrupo(Set<CuGrupo> seleccionadosGrupo) {
        this.seleccionadosGrupo = seleccionadosGrupo;
    }

    public CuGrupo getgSelected() {
        return gSelected;
    }

    public void setgSelected(CuGrupo gSelected) {
        this.gSelected = gSelected;
    }

    public List<CuCuenta> getListaCuCuenta() {
        return listaCuCuenta;
    }

    public void setListaCuCuenta(List<CuCuenta> listaCuCuenta) {
        this.listaCuCuenta = listaCuCuenta;
    }

    public ListModelList<CuCuenta> getListaCuentaModel() {
        return listaCuentaModel;
    }

    public void setListaCuentaModel(ListModelList<CuCuenta> listaCuentaModel) {
        this.listaCuentaModel = listaCuentaModel;
    }

    public CuCuenta getCuSelected() {
        return cuSelected;
    }

    public void setCuSelected(CuCuenta cuSelected) {
        this.cuSelected = cuSelected;
    }

   

    public List<CuClase> getListaCuClase() {
        return listaCuClase;
    }

    public void setListaCuClase(List<CuClase> listaCuClase) {
        this.listaCuClase = listaCuClase;
    }

    public String getBuscarClase() {
        return buscarClase;
    }

    public void setBuscarClase(String buscarClase) {
        this.buscarClase = buscarClase;
    }

    public List<CuGrupo> getListaCuGrupos() {
        return listaCuGrupos;
    }

    public void setListaCuGrupos(List<CuGrupo> listaCuGrupos) {
        this.listaCuGrupos = listaCuGrupos;
    }

    public String getBuscarGrupo() {
        return buscarGrupo;
    }

    public void setBuscarGrupo(String buscarGrupo) {
        this.buscarGrupo = buscarGrupo;
    }

    public Set<CuCuenta> getSeleccionadosCuenta() {
        return seleccionadosCuenta;
    }

    public void setSeleccionadosCuenta(Set<CuCuenta> seleccionadosCuenta) {
        this.seleccionadosCuenta = seleccionadosCuenta;
    }

    public List<CuSubCuenta> getListaCuSubCuenta() {
        return listaCuSubCuenta;
    }

    public void setListaCuSubCuenta(List<CuSubCuenta> listaCuSubCuenta) {
        this.listaCuSubCuenta = listaCuSubCuenta;
    }

    public ListModelList<CuSubCuenta> getListaSubCuentaModel() {
        return listaSubCuentaModel;
    }

    public void setListaSubCuentaModel(ListModelList<CuSubCuenta> listaSubCuentaModel) {
        this.listaSubCuentaModel = listaSubCuentaModel;
    }
    
      public Boolean getActivarGrupo() {
        return activarGrupo;
    }

    public void setActivarGrupo(Boolean activarGrupo) {
        this.activarGrupo = activarGrupo;
    }

    public Boolean getActivarCuenta() {
        return activarCuenta;
    }

    public void setActivarCuenta(Boolean activarCuenta) {
        this.activarCuenta = activarCuenta;
    }

    public Boolean getActivarSubCuenta() {
        return activarSubCuenta;
    }

    public void setActivarSubCuenta(Boolean activarSubCuenta) {
        this.activarSubCuenta = activarSubCuenta;
    }

    public Date getInicioAC() {
        return inicioAC;
    }

    public void setInicioAC(Date inicioAC) {
        this.inicioAC = inicioAC;
    }

    public Date getFinAC() {
        return finAC;
    }

    public void setFinAC(Date finAC) {
        this.finAC = finAC;
    }
    
    
    
    @Command
    @NotifyChange({"listaClaseModel", "buscarClase"})
    public void nuevaclase() {
        buscarClase = "";
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/contabilidad/nuevaclase.zul", null, null);
        window.doModal();
         getClaseModel();
    }
    
     private void findLikeNombre() {
        listaCuClase = servicioClase.findByNombre(buscarClase);
    }
     
    @Command
    @NotifyChange({"listaClaseModel", "buscarClase"})
    public void modificarClase(@BindingParam("valor") CuClase valor) {
        
        buscarClase = "";
        final HashMap<String, CuClase> map = new HashMap<String, CuClase>();
        map.put("valor", valor);
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/contabilidad/nuevaclase.zul", null, map);
        window.doModal();
         getClaseModel();
    }
    @Command
    @NotifyChange({"listaGrupoModel", "buscarClase"})
    public void nuevoGrupo() {
        ParamGrupo paramGrupo= new ParamGrupo(cSelected);
        paramGrupo.setAccion("create");
         final HashMap<String, ParamGrupo> map = new HashMap<String, ParamGrupo>();
          map.put("valor", paramGrupo);
        buscarClase = "";
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/contabilidad/nuevogrupo.zul", null, map);
        window.doModal();
       seleccionarClase();
    }
    
      @Command
    @NotifyChange({"listaGrupoModel", "buscarClase"})
    public void editarGrupo(@BindingParam("valor") CuGrupo valor) {
        ParamGrupo paramGrupo= new ParamGrupo(cSelected, valor);
        paramGrupo.setAccion("update");
         final HashMap<String, ParamGrupo> map = new HashMap<String, ParamGrupo>();
          map.put("valor", paramGrupo);
        buscarClase = "";
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/contabilidad/nuevogrupo.zul", null, map);
        window.doModal();
       seleccionarClase();
    }
    
       @Command
    @NotifyChange({"listaCuentaModel", "buscarClase"})
    public void nuevaCuenta() {
        ParamCuenta paramCuenta= new ParamCuenta(gSelected);
        paramCuenta.setAccion("create");
         final HashMap<String, ParamCuenta> map = new HashMap<String, ParamCuenta>();
          map.put("valor", paramCuenta);
        buscarClase = "";
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/contabilidad/nuevacuenta.zul", null, map);
        window.doModal();
       seleccionarGrupo();
    }
    
      @Command
    @NotifyChange({"listaCuentaModel", "buscarClase"})
    public void editarCuenta(@BindingParam("valor") CuCuenta valor) {
        ParamCuenta paramCuenta= new ParamCuenta(gSelected, valor);
        paramCuenta.setAccion("update");
         final HashMap<String, ParamCuenta> map = new HashMap<String, ParamCuenta>();
          map.put("valor", paramCuenta);
        buscarClase = "";
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/contabilidad/nuevacuenta.zul", null, map);
        window.doModal();
       seleccionarGrupo();
    }
    
           @Command
    @NotifyChange({"listaSubCuentaModel", "buscarClase"})
    public void nuevaSubCuenta() {
        ParamSubCuenta paramSubCuenta= new ParamSubCuenta(cuSelected);
        paramSubCuenta.setAccion("create");
         final HashMap<String, ParamSubCuenta> map = new HashMap<String, ParamSubCuenta>();
          map.put("valor", paramSubCuenta);
        buscarClase = "";
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/contabilidad/nuevasubcuenta.zul", null, map);
        window.doModal();
       seleccionarCuenta();
    }
    
      @Command
    @NotifyChange({"listaCuentaModel", "buscarClase"})
    public void editarSubCuenta(@BindingParam("valor") CuSubCuenta valor) {
        ParamSubCuenta paramSubCuenta= new ParamSubCuenta(cuSelected, valor);
        paramSubCuenta.setAccion("update");
         final HashMap<String, ParamSubCuenta> map = new HashMap<String, ParamSubCuenta>();
          map.put("valor", paramSubCuenta);
        buscarClase = "";
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/contabilidad/nuevasubcuenta.zul", null, map);
        window.doModal();
       seleccionarCuenta();
    }
    
          @Command
    @NotifyChange({"listaAsientoContable", "buscarAsientoContable"})
    public void nuevoAsientoContable(@BindingParam("valor") AsientoContable valor) {

         final HashMap<String, AsientoContable> map = new HashMap<String, AsientoContable>();
          map.put("valor", valor);
       
       /* org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/contabilidad/nuevasubcuenta.zul", null, map);
        window.doModal();*/
   
    }
    
    /*    ServicioSubCuenta servicioSubCuenta = new ServicioSubCuenta();
    private List<CuSubCuenta> listaCuSubCuenta = new ArrayList<CuSubCuenta>();
    private ListModelList<CuSubCuenta> listaSubCuentaModel = new ListModelList<CuSubCuenta>();
    private Set<CuSubCuenta> seleccionadosSubCuenta = new HashSet<CuSubCuenta>();
    private Boolean activarSubCuenta = Boolean.FALSE;*/
    
    @Command
    @NotifyChange({"listaCuSubCuenta2"})
    public void buscarlistaCuSubCuenta2() {
    
        consultaCuSubCuentas();

    }
   private void consultaCuSubCuentas() {
      
        listaCuSubCuenta2  = servicioSubCuenta2.listadoTotal();

    }
    

    public ServicioSubCuenta getServicioSubCuenta2() {
        return servicioSubCuenta2;
    }

    public void setServicioSubCuenta2(ServicioSubCuenta servicioSubCuenta2) {
        this.servicioSubCuenta2 = servicioSubCuenta2;
    }

    public List<CuSubCuenta> getListaCuSubCuenta2() {
        return listaCuSubCuenta2;
    }

    public void setListaCuSubCuenta2(List<CuSubCuenta> listaCuSubCuenta2) {
        this.listaCuSubCuenta2 = listaCuSubCuenta2;
    }

    public List<AsientoContable> getListaAsientoContable() {
        return listaAsientoContable;
    }

    public void setListaAsientoContable(List<AsientoContable> listaAsientoContable) {
        this.listaAsientoContable = listaAsientoContable;
    }
    
    


       @Command
    @NotifyChange({"listaAsientoContable", "buscarAsientoContable"})
    public void nuevoasientocontable() {
        buscarClase = "";
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/contabilidad/nuevoplancuenta.zul", null, null);
        window.doModal();
         getClaseModel();
    }
    
        @Command
    @NotifyChange({"listaAsientoContable", "inicioAC", "finAC"})
    public void buscarAC() {

        consultaAC();

    }
     private void consultaAC() {
        //totalVenta = BigDecimal.ZERO;
        listaAsientoContable = seervicioAsientoContable.findByMes(inicioAC, finAC);

  
    }
 
 

}
