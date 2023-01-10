/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.CabeceraCompra;
import com.ec.entidad.DetalleRetencionCompra;
import com.ec.entidad.RetencionCompra;
import com.ec.entidad.TipoRetencion;
import com.ec.entidad.Tipoadentificacion;
import com.ec.entidad.Tipoambiente;
import com.ec.entidad.Tipoivaretencion;
import com.ec.entidad.Usuario;
import com.ec.servicio.ServicioDetalleRetencionCompra;
import com.ec.servicio.ServicioRetencionCompra;
import com.ec.servicio.ServicioTipoAmbiente;
import com.ec.servicio.ServicioTipoIdentificacion;
import com.ec.servicio.ServicioTipoIvaRetencion;
import com.ec.servicio.ServicioTipoRetencion;
import com.ec.servicio.ServicioUsuario;
import com.ec.untilitario.ArchivoUtils;
import com.ec.untilitario.AutorizarDocumentos;
import com.ec.untilitario.DetalleRetencionCompraDao;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
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
public class RetencionCompraVm {

    @Wire
    Window windowRetencionCom;

    ServicioUsuario servicioUsuario = new ServicioUsuario();
    ServicioRetencionCompra servicioRetencionCompra = new ServicioRetencionCompra();
    ServicioTipoRetencion servicioTipoRetencion = new ServicioTipoRetencion();
    ServicioDetalleRetencionCompra servicioDetalleRetencionCompra = new ServicioDetalleRetencionCompra();
    ServicioTipoIvaRetencion servicioTipoIvaRetencion = new ServicioTipoIvaRetencion();
    private Usuario usuarioSistema = new Usuario();
    private CabeceraCompra cabeceraCompra = new CabeceraCompra();
    private String tipoUSuario = "1";
    /*tipo de retencion*/
    private List<TipoRetencion> listaTipoRetencion = new ArrayList<TipoRetencion>();
    private TipoRetencion tipoRetencionSelected = new TipoRetencion();
    /*porcentaja de retencion del iva*/
    private List<Tipoivaretencion> listaTipoivaretencion = new ArrayList<Tipoivaretencion>();
    Tipoivaretencion tipoivaretencion;
    private BigDecimal baseImponible = BigDecimal.ZERO;
    private Integer numeroRetencion = 0;
    private String numeroRetencionText = "";
    private String accion = "NEW";
    private String codImpuestoAsignado = "2";
    private Boolean activaIvaRenta = Boolean.TRUE;
    private BigDecimal totalRetencion = BigDecimal.ZERO;
    /*cabecera retencion*/
    private RetencionCompra retencionCompra = new RetencionCompra();
    /*para agregar registros y administrarlo*/
    private DetalleRetencionCompraDao detalleRetencionCompra = new DetalleRetencionCompraDao();
    private ListModelList<DetalleRetencionCompraDao> listaDetalleRetencionCompraModel;
    private List<DetalleRetencionCompraDao> listaDetalleRetencionCompraDatos = new ArrayList<DetalleRetencionCompraDao>();
    private Set<DetalleRetencionCompraDao> registrosSeleccionados = new HashSet<DetalleRetencionCompraDao>();

    private Tipoambiente amb = new Tipoambiente();
    ServicioTipoAmbiente servicioTipoAmbiente = new ServicioTipoAmbiente();

    private List<Tipoadentificacion> listaIdentificion = new ArrayList();
    ServicioTipoIdentificacion servicioTipoIdentificacion = new ServicioTipoIdentificacion();
    Tipoadentificacion tipoadentificacionSelected = null;
    private String drcDocumento;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        amb = servicioTipoAmbiente.FindALlTipoambiente();
//          ((ListModelList<DetalleRetencionCompraDao>) listaDetalleRetencionCompraModel).clear();

//        this.cabeceraCompra = valor;
//        RetencionCompra compraret = servicioRetencionCompra.findByCabeceraCompra(valor);
//        if (retencionCompra != null) {
////            retencionCompra = compraret;
//            retencionCompra.setCabFechaEmision(new Date());
//            retencionCompra.setRcoFecha(new Date());
//            cargarRetencionExistente();
//            accion = "UPDATE";
////            numeroRetencion = compraret.getRcoSecuencial();
//            numeroRetencionText = retencionCompra.getRcoSecuencialText();
//        } else {
        retencionCompra = new RetencionCompra();
        retencionCompra.setRcoFecha(new Date());
//                retencionCompra.setIdCabecera(valor);
        retencionCompra.setRcoPorcentajeIva(12);
        retencionCompra.setRcoValorRetencionIva(BigDecimal.ZERO);
        retencionCompra.setRcoPuntoEmision("001");
        retencionCompra.setRcoSerie(tipoUSuario);
        retencionCompra.setCabFechaEmision(new Date());
        retencionCompra.setRcoFecha(new Date());
        accion = "NEW";
        numeroRetencion();
//        }
        getDetallefactura();

        baseImponible = cabeceraCompra.getCabIva();
        listaTipoRetencion = servicioTipoRetencion.findAllTipo("IVA");
        tipoRetencionSelected = listaTipoRetencion.get(0);
        listaTipoivaretencion = servicioTipoIvaRetencion.findALlTipoivaretencion();
        listaIdentificion = servicioTipoIdentificacion.FindALlTipoadentificacion();
    }

    private void cargarRetencionExistente() {

        List<DetalleRetencionCompra> detalleRetencionCompra = servicioDetalleRetencionCompra.findByCanRetencion(retencionCompra);
        DetalleRetencionCompraDao nuevoRegistro = null;
        listaDetalleRetencionCompraDatos.clear();
        for (DetalleRetencionCompra item : detalleRetencionCompra) {
            nuevoRegistro = new DetalleRetencionCompraDao();
            nuevoRegistro.setDrcBaseImponible(item.getDrcBaseImponible());
            nuevoRegistro.setDrcPorcentaje(item.getDrcPorcentaje());
            nuevoRegistro.setDrcValorRetenido(item.getDrcValorRetenido());
            nuevoRegistro.setRcoCodigo(retencionCompra);
            nuevoRegistro.setTireCodigo(item.getTireCodigo());
            nuevoRegistro.setDrcCodigo(item.getDrcCodigo());
            nuevoRegistro.setCodImpuestoAsignado(item.getDrcCodImpuestoAsignado());
            if (item.getDrcCodImpuestoAsignado() != null) {
                nuevoRegistro.setDrcDescripcion((item.getDrcCodImpuestoAsignado().equals("1") ? "RENTA" : "IVA"));
                nuevoRegistro.setTipoResgistro((item.getDrcCodImpuestoAsignado().equals("1") ? "R" : "IVA"));
            }
            nuevoRegistro.setTipoivaretencion(item.getIdTipoivaretencion());
            listaDetalleRetencionCompraDatos.add(nuevoRegistro);
        }

    }

    private void numeroRetencionTexto() {
        numeroRetencionText = "";
        for (int i = numeroRetencion.toString().length(); i < 9; i++) {
            numeroRetencionText = numeroRetencionText + "0";
        }
        numeroRetencionText = numeroRetencionText + numeroRetencion;
        System.out.println("nuemro texto " + numeroRetencionText);
    }

    private int numeroRetencion() {
        RetencionCompra recuperada = servicioRetencionCompra.findUtlimaRetencion();
        if (recuperada != null) {
            // System.out.println("numero de factura " + recuperada);
            numeroRetencion = recuperada.getRcoSecuencial() + 1;

        } else {
            numeroRetencion = 1;

        }
        numeroRetencionTexto();
        return numeroRetencion;
    }

    private void getDetallefactura() {
        setListaDetalleRetencionCompraModel(new ListModelList<DetalleRetencionCompraDao>(getListaDetalleRetencionCompraDatos()));
        ((ListModelList<DetalleRetencionCompraDao>) listaDetalleRetencionCompraModel).setMultiple(true);
    }

    @Command
    public void seleccionarRegistros() {
        registrosSeleccionados = ((ListModelList<DetalleRetencionCompraDao>) getListaDetalleRetencionCompraModel()).getSelection();
    }

    @Command
    @NotifyChange({"listaDetalleRetencionCompraModel"})
    public void eliminarRegistros() {
        if (registrosSeleccionados.size() > 0) {
            ((ListModelList<DetalleRetencionCompraDao>) listaDetalleRetencionCompraModel).removeAll(registrosSeleccionados);
            calcularValoresTotales();
//            if (listaDetalleRetencionCompraModel.size()<=0) {
//                servicioRetencionCompra.eliminar(retencionCompra);
//            }

        } else {
            Messagebox.show("Seleccione al menos un registro para eliminar", "Atención", Messagebox.OK, Messagebox.ERROR);
        }

    }

    @Command
    @NotifyChange({"listaDetalleRetencionCompraModel", "baseImponible", "tipoRetencionSelected", ""})
    public void agregarRegistro() {
//        if (servicioRetencionCompra.findByCompra(retencionCompra) != null) {
//            retencionCompra.setRcoSecuencial(numeroRetencion());
//            servicioRetencionCompra.crear(retencionCompra);
//
//        } else {
//            servicioRetencionCompra.modificar(retencionCompra);
//        }
        if (drcDocumento == null) {

            Clients.showNotification("Verifique el tipo de documento seleccionado", "error", null, "end_before", 2000, true);
            return;
        }
        if (baseImponible == null) {
            Clients.showNotification("Verifique la base imponible", "error", null, "end_before", 2000, true);
            return;
        }

        if (baseImponible != BigDecimal.ZERO && (tipoivaretencion != null || tipoRetencionSelected != null)) {
            retencionCompra.setRcoDetalle("COMPRA DE MERCADERIA");
            DetalleRetencionCompraDao nuevoRegistro = new DetalleRetencionCompraDao();
            nuevoRegistro.setDrcBaseImponible(ArchivoUtils.redondearDecimales(baseImponible, 2));
            nuevoRegistro.setDrcDocumento(drcDocumento);
            nuevoRegistro.setDrcPorcentaje((codImpuestoAsignado.equals("1") ? (BigDecimal.valueOf(tipoRetencionSelected.getTirePorcentajeRetencion())) : (BigDecimal.valueOf(Long.valueOf(tipoivaretencion.getTipivaretDescripcion())))));
            BigDecimal valorValorRetenido = BigDecimal.ZERO;
            BigDecimal valorValorRetenidoDos = BigDecimal.ZERO;
            if (codImpuestoAsignado.equals("1")) {
                valorValorRetenido = baseImponible.multiply((BigDecimal.valueOf(tipoRetencionSelected.getTirePorcentajeRetencion()))).divide(BigDecimal.valueOf(100.0), 5, RoundingMode.CEILING);
            } else {
                valorValorRetenidoDos = (baseImponible.multiply((BigDecimal.valueOf(Long.valueOf(tipoivaretencion.getTipivaretDescripcion()))))).divide(BigDecimal.valueOf(100.0), 5, RoundingMode.CEILING);
            }

            nuevoRegistro.setDrcValorRetenido(codImpuestoAsignado.equals("1") ? ArchivoUtils.redondearDecimales(valorValorRetenido, 2) : ArchivoUtils.redondearDecimales(valorValorRetenidoDos, 2));
            nuevoRegistro.setTireCodigo(tipoRetencionSelected);
            nuevoRegistro.setRcoCodigo(retencionCompra);

            nuevoRegistro.setTipoivaretencion(tipoivaretencion);
            nuevoRegistro.setCodImpuestoAsignado(codImpuestoAsignado);
            nuevoRegistro.setDrcDescripcion((codImpuestoAsignado.equals("1") ? "RENTA" : "IVA"));
            nuevoRegistro.setTipoResgistro((codImpuestoAsignado.equals("1") ? "R" : "IVA"));
            ((ListModelList<DetalleRetencionCompraDao>) listaDetalleRetencionCompraModel).add(nuevoRegistro);
            baseImponible = BigDecimal.ZERO;
            tipoRetencionSelected = null;
//            Clients.showNotification("Agregado correctamente", "info", null, "end_before", 1000, true);
        } else {
            Clients.showNotification("Verifique la base imponible", "error", null, "end_before", 2000, true);
        }

    }

    @Command
    @NotifyChange({"activaIvaRenta", "baseImponible", "listaTipoRetencion", "tipoRetencionSelected"})
    public void visualizarIvaFuente() {
        if (codImpuestoAsignado.equals("2")) {
            tipoRetencionSelected = null;
            activaIvaRenta = Boolean.TRUE;
            baseImponible = cabeceraCompra.getCabIva();
            listaTipoRetencion = servicioTipoRetencion.findAllTipo("IVA");
            tipoRetencionSelected = listaTipoRetencion.get(0);

        } else {
            tipoivaretencion = null;
            activaIvaRenta = Boolean.FALSE;
            baseImponible = cabeceraCompra.getCabSubTotal();
            listaTipoRetencion = servicioTipoRetencion.findAllTipo("RET");
        }
    }

    private void calcularValoresTotales() {
        totalRetencion = BigDecimal.ZERO;
        for (DetalleRetencionCompraDao item : listaDetalleRetencionCompraModel) {
            totalRetencion = totalRetencion.add(item.getDrcValorRetenido());
        }

    }

    public Usuario getUsuarioSistema() {
        return usuarioSistema;
    }

    public void setUsuarioSistema(Usuario usuarioSistema) {
        this.usuarioSistema = usuarioSistema;
    }

    public String getTipoUSuario() {
        return tipoUSuario;
    }

    public void setTipoUSuario(String tipoUSuario) {
        this.tipoUSuario = tipoUSuario;
    }

    @Command
    @NotifyChange("usuarioSistema")
    public void guardar() {

        try {
            if (tipoadentificacionSelected == null) {
                Clients.showNotification("Seleccione un tipo de documento", "error", null, "end_before", 2000, true);
                return;
            }

            if (numeroRetencion != 0) {
                if (retencionCompra.getRcoIdentificacion() != null
                            && retencionCompra.getRcoRazonSocial() != null
                            && retencionCompra.getRcoNumFactura() != null) {
                    if (retencionCompra != null) {

                        System.out.println("NUM DIG " + retencionCompra.getRcoNumFactura().length());
                        if (retencionCompra.getRcoNumFactura().length() != 15) {
                            Clients.showNotification("El numero de factura debe tener el establecimiento, punto de emision y secuencial (15 digitos)", "error", null, "end_before", 2000, true);
                            return;
                        }

                    }
                    if (accion.equals("UPDATE")) {

                        servicioRetencionCompra.eliminar(retencionCompra);
                    }
                    if (!accion.equals("UPDATE")) {
                        numeroRetencionTexto();
                    }

                    retencionCompra.setRcoTipoDocumento(tipoadentificacionSelected.getTidCodigo());

                    retencionCompra.setCodTipoambiente(amb);
                    retencionCompra.setRcoValorRetencionIva(totalRetencion);
                    retencionCompra.setRcoSecuencial(numeroRetencion);
                    retencionCompra.setRcoSecuencialText(numeroRetencionText);
                    retencionCompra.setRcoPuntoEmision("001");
                    retencionCompra.setRcoSerie("1");
//                retencionCompra.setCabFechaEmision(new Date());
                    retencionCompra.setDrcEstadosri("PENDIENTE");
                    retencionCompra.setRcoCodSustento("01");
                    AutorizarDocumentos ad = new AutorizarDocumentos();

                    String claveAcceso = ad.generaClave(retencionCompra.getCabFechaEmision(), "07", amb.getAmRuc(), amb.getAmCodigo(), amb.getAmEstab().trim() + amb.getAmPtoemi().trim(), retencionCompra.getRcoSecuencialText(), "12345678", "1");
                    retencionCompra.setRcoAutorizacion(claveAcceso);
                    servicioRetencionCompra.crearCabDetalle(retencionCompra, listaDetalleRetencionCompraModel.getInnerList());
                    Clients.showNotification("Guardado correctamente",
                                Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 1000, true);
                    Executions.sendRedirect("/contabilidad/retencion.zul");
                } else {
                    Clients.showNotification("Verifique la informacion del documento", "error", null, "start_before", 2000, true);
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR AL GUARADAR " + e.getMessage());
        }

    }

    public CabeceraCompra getCabeceraCompra() {
        return cabeceraCompra;
    }

    public void setCabeceraCompra(CabeceraCompra cabeceraCompra) {
        this.cabeceraCompra = cabeceraCompra;
    }

    public RetencionCompra getRetencionCompra() {
        return retencionCompra;
    }

    public void setRetencionCompra(RetencionCompra retencionCompra) {
        this.retencionCompra = retencionCompra;
    }

    public List<TipoRetencion> getListaTipoRetencion() {
        return listaTipoRetencion;
    }

    public void setListaTipoRetencion(List<TipoRetencion> listaTipoRetencion) {
        this.listaTipoRetencion = listaTipoRetencion;
    }

    public DetalleRetencionCompraDao getDetalleRetencionCompra() {
        return detalleRetencionCompra;
    }

    public void setDetalleRetencionCompra(DetalleRetencionCompraDao detalleRetencionCompra) {
        this.detalleRetencionCompra = detalleRetencionCompra;
    }

    public ListModelList<DetalleRetencionCompraDao> getListaDetalleRetencionCompraModel() {
        return listaDetalleRetencionCompraModel;
    }

    public void setListaDetalleRetencionCompraModel(ListModelList<DetalleRetencionCompraDao> listaDetalleRetencionCompraModel) {
        this.listaDetalleRetencionCompraModel = listaDetalleRetencionCompraModel;
    }

    public List<DetalleRetencionCompraDao> getListaDetalleRetencionCompraDatos() {
        return listaDetalleRetencionCompraDatos;
    }

    public void setListaDetalleRetencionCompraDatos(List<DetalleRetencionCompraDao> listaDetalleRetencionCompraDatos) {
        this.listaDetalleRetencionCompraDatos = listaDetalleRetencionCompraDatos;
    }

    public Set<DetalleRetencionCompraDao> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    public void setRegistrosSeleccionados(Set<DetalleRetencionCompraDao> registrosSeleccionados) {
        this.registrosSeleccionados = registrosSeleccionados;
    }

    public TipoRetencion getTipoRetencionSelected() {
        return tipoRetencionSelected;
    }

    public void setTipoRetencionSelected(TipoRetencion tipoRetencionSelected) {
        this.tipoRetencionSelected = tipoRetencionSelected;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    public Integer getNumeroRetencion() {
        return numeroRetencion;
    }

    public void setNumeroRetencion(Integer numeroRetencion) {
        this.numeroRetencion = numeroRetencion;
    }

    public String getNumeroRetencionText() {
        return numeroRetencionText;
    }

    public void setNumeroRetencionText(String numeroRetencionText) {
        this.numeroRetencionText = numeroRetencionText;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getCodImpuestoAsignado() {
        return codImpuestoAsignado;
    }

    public void setCodImpuestoAsignado(String codImpuestoAsignado) {
        this.codImpuestoAsignado = codImpuestoAsignado;
    }

    public Tipoivaretencion getTipoivaretencion() {
        return tipoivaretencion;
    }

    public void setTipoivaretencion(Tipoivaretencion tipoivaretencion) {
        this.tipoivaretencion = tipoivaretencion;
    }

    public List<Tipoivaretencion> getListaTipoivaretencion() {
        return listaTipoivaretencion;
    }

    public void setListaTipoivaretencion(List<Tipoivaretencion> listaTipoivaretencion) {
        this.listaTipoivaretencion = listaTipoivaretencion;
    }

    public Boolean getActivaIvaRenta() {
        return activaIvaRenta;
    }

    public void setActivaIvaRenta(Boolean activaIvaRenta) {
        this.activaIvaRenta = activaIvaRenta;
    }

    public BigDecimal getTotalRetencion() {
        return totalRetencion;
    }

    public void setTotalRetencion(BigDecimal totalRetencion) {
        this.totalRetencion = totalRetencion;
    }

    public Tipoambiente getAmb() {
        return amb;
    }

    public void setAmb(Tipoambiente amb) {
        this.amb = amb;
    }

    public List<Tipoadentificacion> getListaIdentificion() {
        return listaIdentificion;
    }

    public void setListaIdentificion(List<Tipoadentificacion> listaIdentificion) {
        this.listaIdentificion = listaIdentificion;
    }

    public Tipoadentificacion getTipoadentificacionSelected() {
        return tipoadentificacionSelected;
    }

    public void setTipoadentificacionSelected(Tipoadentificacion tipoadentificacionSelected) {
        this.tipoadentificacionSelected = tipoadentificacionSelected;
    }

    public String getDrcDocumento() {
        return drcDocumento;
    }

    public void setDrcDocumento(String drcDocumento) {
        this.drcDocumento = drcDocumento;
    }

}
