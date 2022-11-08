/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.dao.CuSubCuentaDAO;
import com.ec.dao.DetalleFacturaDAO;
import com.ec.dao.NuevoAsientoManual;
import com.ec.entidad.contabilidad.AsientoContable;
import com.ec.entidad.contabilidad.CuSubCuenta;
import com.ec.servicio.contabilidad.ServicioAsientoContable;
import com.ec.servicio.contabilidad.ServicioSubCuenta;
import com.ec.untilitario.ParamPopupSubCuenta;
import java.math.BigDecimal;
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
import org.zkoss.xel.VariableResolver;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

/**
 *
 * @author HC
 */
public class NuevoIngresoCuenta extends SelectorComposer<Component> {

    @Wire
    Window windowSubcuentaBuscar;
    @Wire
    Window wIngresoCuenta;
    ServicioSubCuenta servicioSubCuenta = new ServicioSubCuenta();

    private List<CuSubCuenta> listaCuSubCuentaAll = new ArrayList<CuSubCuenta>();

    public static Integer idCodigoSubcuenta = 0;

    /*POPUP*/
    private List<CuSubCuenta> listaSubCuentas = new ArrayList<CuSubCuenta>();
    private CuSubCuenta cuSubCuentaSelected;
    private String buscarNombre = "";

    private ListModelList<CuSubCuentaDAO> listaCuSubCuentaDAOMOdel;
//    ServicioSubCuenta  servicioSubCuenta1= new ServicioSubCuenta();

    CuSubCuenta cusubcuentaBuscada = new CuSubCuenta();
    private NuevoAsientoManual asientoManual = new NuevoAsientoManual();

    /*Lista model*/
//        private ListModelList<CuSubCuentaDAO> listaNotaEntregaModel;
    private List<NuevoAsientoManual> listaDatosAsiento = new ArrayList<NuevoAsientoManual>();
    private ListModelList<NuevoAsientoManual> listaNuevoAsientoModel;
    public Set<NuevoAsientoManual> registrosSeleccionados = new HashSet<NuevoAsientoManual>();

    private BigDecimal totalDebe = BigDecimal.ZERO;
    private BigDecimal totalHaber = BigDecimal.ZERO;
    private String referencia = "";
    private String documento = "";
    private String observacion = "";
    private Date fechaDc = new Date();

    public NuevoIngresoCuenta() {
        getLlenarListaAsiento();
        FindCuSubCuentaLikeNombre();
    }

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") ParamPopupSubCuenta valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        if (valor == null) {
            ((ListModelList<NuevoAsientoManual>) listaNuevoAsientoModel).clear();
        }
        if (valor != null) {
            if (valor.getTipo().equals("SUB")) {
                FindCuSubCuentaLikeNombre();
            }
        }

    }

    private void getLlenarListaAsiento() {
        setListaNuevoAsientoModel(new ListModelList<NuevoAsientoManual>(getListaDatosAsiento()));
        ((ListModelList<NuevoAsientoManual>) listaNuevoAsientoModel).setMultiple(true);
    }


    /*metodo para selecionar en le popup*/
    @Command
    @NotifyChange({"listaCuSubCuentaAll", "buscarNombre"})
    public void buscaBucarSubcuentaNombre() {

        FindCuSubCuentaLikeNombre();
    }

    @Command
    @NotifyChange({"listaDetalleIngresoSubCuenta", "buscarNombre", "listaCuSubCuentaAll", "totalItems"})
    public void agregarItemLista(@BindingParam("valor") CuSubCuenta cuSubCuenta) {

        if (cusubcuentaBuscada != null) {
            CuSubCuentaDAO valor = new CuSubCuentaDAO();

            valor.setCusubcuenta(cusubcuentaBuscada);
            valor.setNumero(cusubcuentaBuscada.getSubcNumero());
            valor.setNombre(cusubcuentaBuscada.getSubcNombre());
        }
        List<CuSubCuentaDAO> listaCuSubcuenta = listaCuSubCuentaDAOMOdel.getInnerList();

        for (CuSubCuentaDAO item : listaCuSubcuenta) {
            if (item.getNombre() == null) {
                ((ListModelList<CuSubCuentaDAO>) listaCuSubCuentaDAOMOdel).remove(item);
                break;
            }
        }
    }

    @Command
    public void seleccionarSubCuenta(@BindingParam("valor") CuSubCuenta valor) {
        System.out.println("Subcuenta seleccionada " + valor.getIdSubCuenta());
        idCodigoSubcuenta = valor.getIdSubCuenta();

        windowSubcuentaBuscar.detach();

    }

    private void FindCuSubCuentaLikeNombre() {
        listaCuSubCuentaAll = servicioSubCuenta.findByNombre(buscarNombre);
    }

    public List<CuSubCuenta> getListaCuSubCuentaAll() {
        return listaCuSubCuentaAll;
    }

    public void setListaCuSubCuentaAll(List<CuSubCuenta> listaCuSubCuentaAll) {
        this.listaCuSubCuentaAll = listaCuSubCuentaAll;
    }

    public String getBuscarNombre() {
        return buscarNombre;
    }

    public void setBuscarNombre(String buscarNombre) {
        this.buscarNombre = buscarNombre;
    }

    @Command
    @NotifyChange({"cuSubCuentaSelected"})
    public void buscarSubCuenta() {
        final HashMap<String, ParamPopupSubCuenta> map = new HashMap<String, ParamPopupSubCuenta>();
        ParamPopupSubCuenta param = new ParamPopupSubCuenta();
        param.setTipo("SUB");
        map.put("valor", param);
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/contabilidad/buscarsubcuenta.zul", null, map);
        window.doModal();

        cuSubCuentaSelected = servicioSubCuenta.findById(idCodigoSubcuenta);

    }

    @Command
    @NotifyChange({"cuSubCuentaSelected"})
    public void cargarSubCuenta() {
        final HashMap<String, ParamPopupSubCuenta> map = new HashMap<String, ParamPopupSubCuenta>();
        ParamPopupSubCuenta param = new ParamPopupSubCuenta();
        param.setTipo("SUB");
        map.put("valor", param);
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/contabilidad/buscarsubcuenta.zul", null, map);
        window.doModal();

        cuSubCuentaSelected = servicioSubCuenta.findById(idCodigoSubcuenta);

    }

    private AsientoContable entidad = new AsientoContable();

    private String accion = "create";

    ServicioAsientoContable servicioAsientoContable = new ServicioAsientoContable();

    public AsientoContable getEntidad() {
        return entidad;
    }

    public void setEntidad(AsientoContable entidad) {
        this.entidad = entidad;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public static Integer getIdCodigoSubcuenta() {
        return idCodigoSubcuenta;
    }

    public static void setIdCodigoSubcuenta(Integer idCodigoSubcuenta) {
        NuevoIngresoCuenta.idCodigoSubcuenta = idCodigoSubcuenta;
    }

    public List<CuSubCuenta> getListaSubCuentas() {
        return listaSubCuentas;
    }

    public void setListaSubCuentas(List<CuSubCuenta> listaSubCuentas) {
        this.listaSubCuentas = listaSubCuentas;
    }

    public CuSubCuenta getCuSubCuentaSelected() {
        return cuSubCuentaSelected;
    }

    public void setCuSubCuentaSelected(CuSubCuenta cuSubCuentaSelected) {
        this.cuSubCuentaSelected = cuSubCuentaSelected;
    }

    public ListModelList<NuevoAsientoManual> getListaNuevoAsientoModel() {
        return listaNuevoAsientoModel;
    }

    public void setListaNuevoAsientoModel(ListModelList<NuevoAsientoManual> listaNuevoAsientoModel) {
        this.listaNuevoAsientoModel = listaNuevoAsientoModel;
    }

    public void setRegistrosSeleccionados(Set<NuevoAsientoManual> registrosSeleccionados) {
        this.registrosSeleccionados = registrosSeleccionados;
    }

    public BigDecimal getTotalDebe() {
        return totalDebe;
    }

    public void setTotalDebe(BigDecimal totalDebe) {
        this.totalDebe = totalDebe;
    }

    public BigDecimal getTotalHaber() {
        return totalHaber;
    }

    public void setTotalHaber(BigDecimal totalHaber) {
        this.totalHaber = totalHaber;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFechaDc() {
        return fechaDc;
    }

    public void setFechaDc(Date fechaDc) {
        this.fechaDc = fechaDc;
    }

    @Command
    public void seleccionarRegistros() {
        registrosSeleccionados = ((ListModelList<NuevoAsientoManual>) getListaNuevoAsientoModel()).getSelection();
    }

    public List<NuevoAsientoManual> getListaDatosAsiento() {
        return listaDatosAsiento;
    }

    public void setListaDatosAsiento(List<NuevoAsientoManual> listaDatosAsiento) {
        this.listaDatosAsiento = listaDatosAsiento;
    }

    /*Metodos listbox*/
    @Command
    @NotifyChange({"listaNuevoAsientoModel", "totalDebe", "totalHaber"})
    public void agregarAsiento() {

        if (referencia.equals("") || documento.equals("")
                    || observacion.equals("")) {
            Clients.showNotification("Verifique la referencia, documento u observación",
                        Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 2000, true);
            return;
        }
        NuevoAsientoManual asientoManual = new NuevoAsientoManual(cuSubCuentaSelected, referencia, documento, observacion, BigDecimal.ZERO, BigDecimal.ZERO);
        List<NuevoAsientoManual> listaNuevoAsiento = listaNuevoAsientoModel.getInnerList();
        for (NuevoAsientoManual item : listaNuevoAsiento) {
            if (item.getCuSubCuentaSelected() == cuSubCuentaSelected) {

                Clients.showNotification("La cuenta ya se encuentra registrada en el listado..",
                            Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 2000, true);
                return;
            }
        }
        listaNuevoAsientoModel.add(asientoManual);

    }

    @Command
    @NotifyChange({"listaNuevoAsientoModel", "totalDebe", "totalHaber"})
    public void eliminarRegistros() {
        if (registrosSeleccionados.size() > 0) {

            ((ListModelList<NuevoAsientoManual>) listaNuevoAsientoModel).removeAll(registrosSeleccionados);
            calcularTotales();

        } else {
            Clients.showNotification("Debe seleccionar almenos un registro",
                        Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 2000, true);
        }

    }

    @Command
    @NotifyChange({"listaNuevoAsientoModel", "totalDebe", "totalHaber"})
    public void calcularDebeHaber(@BindingParam("valor") NuevoAsientoManual valor) {
        if (valor.getDebe() == null) {
            valor.setDebe(BigDecimal.ZERO);

        }
        if (valor.getHaber() == null) {
            valor.setHaber(BigDecimal.ZERO);

        }
        calcularTotales();

    }

    private void calcularTotales() {
        List<NuevoAsientoManual> listaNuevoAsiento = listaNuevoAsientoModel.getInnerList();
        totalDebe = BigDecimal.ZERO;
        totalHaber = BigDecimal.ZERO;
        for (NuevoAsientoManual nuevoAsientoManual : listaNuevoAsiento) {
            if (nuevoAsientoManual.getDebe() == null) {
                nuevoAsientoManual.setDebe(BigDecimal.ZERO);
            }
            if (nuevoAsientoManual.getHaber() == null) {
                nuevoAsientoManual.setHaber(BigDecimal.ZERO);
            }
            totalDebe = totalDebe.add(nuevoAsientoManual.getDebe());
            totalHaber = totalHaber.add(nuevoAsientoManual.getHaber());
        }
    }
}
