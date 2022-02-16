/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.dao.DetalleFacturaDAO;
import com.ec.entidad.Producto;
import com.ec.entidad.contabilidad.CuClase;
import com.ec.entidad.contabilidad.CuCuenta;
import com.ec.entidad.contabilidad.CuGrupo;
import com.ec.entidad.contabilidad.CuSubCuenta;
import com.ec.servicio.contabilidad.ServicioClase;
import com.ec.servicio.contabilidad.ServicioCuenta;
import com.ec.servicio.contabilidad.ServicioGrupo;
import com.ec.servicio.contabilidad.ServicioSubCuenta;
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
public class AdmPlanCuentas {

    /*Model de clase*/
    ServicioClase servicioClase = new ServicioClase();
    private List<CuClase> listaCuClase = new ArrayList<CuClase>();
    private ListModelList<CuClase> listaClaseModel;
    private Set<CuClase> seleccionadosClase = new HashSet<CuClase>();
    private String buscarClase = "";
    private CuClase cSelected = null;
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
    private ListModelList<CuCuenta> listaCuentaModel= new ListModelList<CuCuenta>();
    private Set<CuCuenta> seleccionadosCuenta = new HashSet<CuCuenta>();
    private CuCuenta cuSelected = null;

    /*Model de SubCuenta*/
    ServicioSubCuenta servicioSubCuenta = new ServicioSubCuenta();
    private List<CuSubCuenta> listaCuSubCuenta = new ArrayList<CuSubCuenta>();
    private ListModelList<CuSubCuenta> listaSubCuentaModel= new ListModelList<CuSubCuenta>();
    private Set<CuSubCuenta> seleccionadosSubCuenta = new HashSet<CuSubCuenta>();
//    private CuSubCuenta cuSelected = null;

    public AdmPlanCuentas() {
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
        gSelected=null;
        cuSelected=null;
        
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
    @NotifyChange("listaCuentaModel")
    public void seleccionarGrupo() {

        seleccionadosGrupo = ((ListModelList<CuGrupo>) getListaGrupoModel()).getSelection();

        for (CuGrupo cuGrupo : seleccionadosGrupo) {
            gSelected = cuGrupo;
        }
          cuSelected=null;
        getCuentaModel();
        listaSubCuentaModel.clear();

    }

    private void getCuentaModel() {
        findCuenta();
        setListaCuentaModel(new ListModelList<CuCuenta>(getListaCuCuenta()));
        ((ListModelList<CuCuenta>) listaCuentaModel).setMultiple(false);
    }

    @Command
    @NotifyChange("listaSubCuentaModel")
    public void seleccionarCuenta() {

        seleccionadosCuenta = ((ListModelList<CuCuenta>) getListaCuentaModel()).getSelection();

        for (CuCuenta cuCuenta : seleccionadosCuenta) {
            cuSelected = cuCuenta;
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

    @Command
    @NotifyChange({"listaCuGrupos", "buscarGrupo"})
    public void actualizar(@BindingParam("valor") CuGrupo valor) {

        final HashMap<String, CuGrupo> map = new HashMap<String, CuGrupo>();
        map.put("valor", valor);
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/nuevo/grupo.zul", null, map);
        window.doModal();
        findGrupo();
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

}
