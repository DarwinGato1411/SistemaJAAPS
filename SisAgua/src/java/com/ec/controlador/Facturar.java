/*
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.dao.DetalleFacturaDAO;
import com.ec.entidad.CierreCaja;
import com.ec.entidad.Cliente;
import com.ec.entidad.DetalleFactura;
import com.ec.entidad.DetalleGuiaremision;
import com.ec.entidad.DetalleKardex;
import com.ec.entidad.DetalleTarifa;
import com.ec.entidad.Factura;
import com.ec.entidad.FormaPago;
import com.ec.entidad.Guiaremision;
import com.ec.entidad.Kardex;
import com.ec.entidad.Lectura;
import com.ec.entidad.Medidor;
import com.ec.entidad.Parametrizar;
import com.ec.entidad.Producto;
import com.ec.entidad.Tipoambiente;
import com.ec.entidad.Tipocomprobante;
import com.ec.entidad.Tipokardex;
import com.ec.entidad.Transportista;
import com.ec.entidad.Usuario;
import com.ec.entidad.contabilidad.AcSubCuenta;
import com.ec.entidad.contabilidad.CuSubCuenta;
import com.ec.servicio.contabilidad.ServicioAcSubcuenta;
import com.ec.seguridad.AutentificadorLogeo;
import com.ec.seguridad.AutentificadorService;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.HelperPersistencia;
import com.ec.servicio.ServicioCierreCaja;
import com.ec.servicio.ServicioCliente;
import com.ec.servicio.ServicioDetalleFactura;
import com.ec.servicio.ServicioDetalleGuia;
import com.ec.servicio.ServicioDetalleKardex;
import com.ec.servicio.ServicioDetalleTarifas;
import com.ec.servicio.ServicioEstadoFactura;
import com.ec.servicio.ServicioFactura;
import com.ec.servicio.ServicioFormaPago;
import com.ec.servicio.ServicioGuia;
import com.ec.servicio.ServicioKardex;
import com.ec.servicio.ServicioLectura;
import com.ec.servicio.ServicioMedidor;
import com.ec.servicio.ServicioParametrizar;
import com.ec.servicio.ServicioProducto;
import com.ec.servicio.ServicioTipoAmbiente;
import com.ec.servicio.ServicioTipoKardex;
import com.ec.servicio.ServicioTransportista;
import com.ec.servicio.ServicioUsuario;
import com.ec.servicio.contabilidad.ServicioSubCuenta;
import com.ec.untilitario.ArchivoUtils;
import com.ec.untilitario.ParamFactura;
import com.ec.untilitario.TotalKardex;
import com.ec.untilitario.UtilitarioAutorizarSRI;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author gato
 */
public class Facturar extends SelectorComposer<Component> {

//    @Wire
//    Window windowNotaEntrega;
    @Wire
    Window windowClienteBuscar;
    @Wire
    Textbox txtBuscarNombre;
    @Wire
    Window windowProductoBuscar;
    @Wire
    Textbox idBusquedaProd;
    @Wire
    Window windowModCotizacionFact;
    @Wire
    Window windowCambioPrecio;

    @Wire
    Window windowValidaBorra;

    /*DETALLE DEL KARDEX Y DETALLE KARDEX*/
    ServicioKardex servicioKardex = new ServicioKardex();
    ServicioDetalleKardex servicioDetalleKardex = new ServicioDetalleKardex();
    ServicioTipoKardex servicioTipoKardex = new ServicioTipoKardex();
    ServicioDetalleFactura servicioDetalleFactura = new ServicioDetalleFactura();
    //buscar cliente
    ServicioParametrizar servicioParametrizar = new ServicioParametrizar();
    ServicioFormaPago servicioFormaPago = new ServicioFormaPago();
    ServicioTipoAmbiente servicioTipoAmbiente = new ServicioTipoAmbiente();
    ServicioCliente servicioCliente = new ServicioCliente();
    ServicioEstadoFactura servicioEstadoFactura = new ServicioEstadoFactura();
    ServicioFactura servicioFactura = new ServicioFactura();
    ServicioTransportista servicioTransportista = new ServicioTransportista();

    /*SERVICIOS DE LA GUIA*/
    ServicioGuia servicioGuia = new ServicioGuia();
    ServicioDetalleGuia servicioDetalleGuia = new ServicioDetalleGuia();
    List<Transportista> listaTransportistas = new ArrayList<Transportista>();
    public Cliente clienteBuscado = new Cliente("");
    private List<Cliente> listaClientesAll = new ArrayList<Cliente>();
    private String buscarNombre = "";
    private String buscarRazonSocial = "";
    private String buscarCedula = "";
    public static String buscarCliente = "";
    //busacar producto
    ServicioProducto servicioProducto = new ServicioProducto();
    private List<Producto> listaProducto = new ArrayList<Producto>();

    private String buscarNombreProd = "";
    private String buscarCodigoProd = "";
    private Producto productoBuscado = new Producto();
    public static String codigoBusqueda = "";
    //crear un factura nueva
    private Factura factura = new Factura();
    private DetalleFacturaDAO detalleFacturaDAO = new DetalleFacturaDAO();
    private ListModelList<DetalleFacturaDAO> listaDetalleFacturaDAOMOdel;
    private List<DetalleFacturaDAO> listaDetalleFacturaDAODatos = new ArrayList<DetalleFacturaDAO>();
    private Set<DetalleFacturaDAO> registrosSeleccionados = new HashSet<DetalleFacturaDAO>();
    //valorTotalCotizacion
    private BigDecimal valorTotalCotizacion = BigDecimal.ZERO;
    private BigDecimal valorTotalInicialVent = BigDecimal.ZERO;
    private BigDecimal descuentoValorFinal = BigDecimal.ZERO;
    private BigDecimal subTotalCotizacion = BigDecimal.ZERO;
    private BigDecimal subTotalBaseCero = BigDecimal.ZERO;
    private BigDecimal ivaCotizacion = BigDecimal.ZERO;
    private BigDecimal totalDescuento = BigDecimal.ZERO;
    //Cabecera de la factura
    private String estdoFactura = "PA";
    private String tipoVentaAnterior = "FACT";
    private String tipoVenta = "FACT";
    private String facturaDescripcion = "";
    private Integer numeroFactura = 0;
    private String numeroFacturaText = "";

    /*GUIA DE REMISION*/
    private Integer numeroGuia = 0;
    private String numeroGuiaText = "";
    private Transportista transportista = null;
    private String numeroPlaca = "";
    private Date incioTraslado = new Date();
    private Date finTraslado = new Date();
    private String motivoGuia = "";
    private String partida = "";
    private String llegada = "";

    private Integer numeroProforma = 0;
    private Date fechafacturacion = new Date();
    private static BigDecimal DESCUENTOGENERAL = BigDecimal.valueOf(5.0);
    //usuario que factura
    UserCredential credential = new UserCredential();
    private Parametrizar parametrizar = null;
//reporte
    AMedia fileContent = null;
    Connection con = null;
    //cambio
    private BigDecimal cobro = BigDecimal.ZERO;
    private BigDecimal cambio = BigDecimal.ZERO;
    private BigDecimal saldoFacturas = BigDecimal.ZERO;
    private BigDecimal subsidioTotal = BigDecimal.ZERO;


    /*forma de pago*/
    private List<FormaPago> listaFormaPago = new ArrayList<FormaPago>();
    private FormaPago formaPagoSelected = null;

    /*KARDEX*/
    private static Tipocomprobante tipocomprobante = null;
    /*RECUPERA EL ID DE LA FACTURA*/
    private Integer idFactuta = 0;
    private String accion = "create";
    private String tipoDoc = "";
    private String clietipo = "0";

    private List<Producto> listaProductoCmb = new ArrayList<Producto>();
    private String codigo = "";

    private Boolean descargarKardex = Boolean.TRUE;
    /*GESTIONA NOTAS DE ENTREGA*/
    //crear un factura nueva        
    private ListModelList<Factura> listaNotaEntregaModel;
    private List<Factura> listalistaNotaEntregaDatos = new ArrayList<Factura>();
    public static Set<Factura> seleccionNotaEntregaProcesada = new HashSet<Factura>();
    public static Set<Factura> seleccionNotaEntrega = new HashSet<Factura>();

    /*RUTAS PARA LOS ARCHIVPOS XML SRI*/
    private static String PATH_BASE = "";
    private Tipoambiente amb = new Tipoambiente();

    /*si es con o sin guia*/
    private String facConSinGuia = "";
    private String facplazo = "30";

    /*CUENTA LOS ITEMS DE LA FACTURA*/
    private String totalItems = "";
    private List<Kardex> listaKardexProducto = new ArrayList<Kardex>();
    /*para apertura de caja*/
    ServicioCierreCaja servicioCierreCaja = new ServicioCierreCaja();
    AutentificadorService authService = new AutentificadorLogeo();

    /*servicio para eliminar producto */
    ServicioUsuario servicioUsuario = new ServicioUsuario();
    public static Boolean validaBorrado = Boolean.FALSE;
    private String usuLoginVal = "";
    private String usuPasswordVal = "";

    /*cambio de precio*/
    public static String TIPOPRECIO = "NORMAL";

    public Producto PRODUCTOCAMBIO = null;

    public String numeroMedidor = "";
    public String buscarMedidor = "";
    public String buscarMedidorNumero = "";

//    MEdidor
    ServicioMedidor servicioMedidor = new ServicioMedidor();
    private List<Medidor> listMedidores = new ArrayList<Medidor>();
    private Medidor medidorEncontrado = null;

    ServicioLectura servicioLectura = new ServicioLectura();
    private List<Lectura> listaDatosLectura = new ArrayList<Lectura>();

    ServicioDetalleTarifas servicioDetalleTarifas = new ServicioDetalleTarifas();

//    control de cobro de la mukta una sola vez en la factura
    private boolean multaCobrada = Boolean.FALSE;

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") ParamFactura valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);

        if (valor == null) {

            accion = "create";
            fechafacturacion = new Date();
            ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).clear();
            verificarSecNumeracion();
            clienteBuscado = servicioCliente.findClienteLikeCedula("999999999");

            //List<Factura> listaFacturasPendientes = servicioFactura.findEstadoCliente("PE", clienteBuscado);
            saldoFacturas = BigDecimal.ZERO;
        } else if (valor.getBusqueda().equals("producto") || valor.getBusqueda().equals("cliente")) {

        } else if (valor.getBusqueda().equals("cambio")) {
            PRODUCTOCAMBIO = servicioProducto.findByProdCodigo(valor.getCodigo());
        } else if (valor.getBusqueda().equals("nte")) {
            cargaNotaEntrega();
        } else {

            accion = "update";
            idFactuta = Integer.valueOf(valor.getIdFactura());
            tipoVentaAnterior = valor.getTipoDoc();
            tipoVenta = valor.getTipoDoc();
            System.out.println("tipoVenta " + tipoVenta);
            System.out.println("idFactuta " + idFactuta);
            recuperFactura();

        }
        parametrizar = servicioParametrizar.FindALlParametrizar();
        DESCUENTOGENERAL = parametrizar.getParDescuentoGeneral();
        validaBorrado = parametrizar.getParBorraItemsFac();

        FindClienteLikeNombre();
        findKardexProductoLikeNombre();
        //para establecer el cliente final

        agregarRegistroVacio();
        buscarCliente = clienteBuscado.getCliCedula();
        llegada = clienteBuscado.getCliDireccion();
        listaTransportistas = servicioTransportista.findTransportista("");
    }
//<editor-fold defaultstate="collapsed" desc="Facturar">

    @Command
    public void aperturaCaja() {
        if (servicioCierreCaja.findALlCierreCajaForFechaIdUsuario(new Date(), credential.getUsuarioSistema()).isEmpty()
                    && credential.getUsuarioSistema().getUsuNivel() != 1) {
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/nuevo/aperturacaja.zul", null, null);
            window.doModal();
            if (servicioCierreCaja.findALlCierreCajaForFechaIdUsuario(new Date(), credential.getUsuarioSistema()).isEmpty()) {
                authService.logout();
                Executions.sendRedirect("/");
            }
        } else {
            if (servicioCierreCaja.findALlCierreCajaForFechaIdUsuario(new Date(), credential.getUsuarioSistema()).isEmpty()) {
                CierreCaja cierreCaja = new CierreCaja();
                cierreCaja.setCieValorInicio(BigDecimal.ZERO);
                cierreCaja.setCieCuadre(BigDecimal.ZERO);
                cierreCaja.setCieDiferencia(BigDecimal.ZERO);
                cierreCaja.setCieValor(BigDecimal.ZERO);
                cierreCaja.setCieFecha(new Date());
                cierreCaja.setIdUsuario(credential.getUsuarioSistema());
                servicioCierreCaja.crear(cierreCaja);
            }

        }

    }

    public Facturar() {

        Session sess = Sessions.getCurrent();
        //sess.setMaxInactiveInterval(10000);
        UserCredential cre = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        credential = cre;

        getDetallefactura();
        parametrizar = servicioParametrizar.FindALlParametrizar();
        listaFormaPago = servicioFormaPago.FindALlFormaPago();
        formaPagoSelected = servicioFormaPago.finPrincipal();
        if (accion.equals("create")) {
            numeroFactura();

        } else {

        }
        amb = servicioTipoAmbiente.FindALlTipoambiente();
        PATH_BASE = amb.getAmDirBaseArchivos() + File.separator
                    + amb.getAmDirXml();
        partida = amb.getAmDireccionMatriz();

    }
// </editor-fold>

    private void recuperFactura() {

        if (tipoVenta.equals("FACT")) {
            factura = servicioFactura.findFirIdFact(idFactuta);
            facConSinGuia = factura.getFaConSinGuia();
            facplazo = factura.getFacPlazo() == null ? "30" : factura.getFacPlazo().setScale(0).toString();
        } else {
            factura = servicioFactura.findByIdCotizacion(idFactuta);
            facConSinGuia = "SG";
        }

        if (tipoVenta.equals("NTV")) {
            factura = servicioFactura.findFirIdFactNTV(idFactuta);
        }
        clienteBuscado = factura.getIdCliente();
        /*RECUPERA EL SALDO DE CREDITO*/
        List<Factura> listaFacturasPendientes = servicioFactura.findEstadoCliente("PE", clienteBuscado);
        saldoFacturas = BigDecimal.ZERO;

        if (tipoVenta.equals("FACT") && accion.equals("update")) {
            numeroFactura = factura.getFacNumero();
        } else if (tipoVenta.equals("PROF") && accion.equals("update")) {
            numeroFactura = factura.getFacNumProforma();
        } else if (tipoVenta.equals("NTE") && accion.equals("update")) {
            numeroFactura = factura.getFacNumNotaEntrega();
        } else if (tipoVenta.equals("NTV") && accion.equals("update")) {
            numeroFactura = factura.getFacNumNotaVenta();
        }
        fechafacturacion = factura.getFacFecha();
        /*RECUPERA LOS VALORES TOTALES DE LA FACTURA*/
        subTotalCotizacion = factura.getFacSubtotal();
        ivaCotizacion = factura.getFacIva();
        valorTotalCotizacion = factura.getFacTotal();
        totalDescuento = factura.getFacDescuento();
        List<DetalleFactura> detalleFac = servicioDetalleFactura.findDetalleForIdFac(idFactuta);
        DetalleFacturaDAO nuevoRegistro;
        listaDetalleFacturaDAODatos.clear();
        for (DetalleFactura det : detalleFac) {
            nuevoRegistro = new DetalleFacturaDAO();
            nuevoRegistro.setCodigo(det.getIdProducto().getProdCodigo());
            nuevoRegistro.setCantidad(det.getDetCantidad());
            nuevoRegistro.setProducto(det.getIdProducto());
            nuevoRegistro.setDescripcion(det.getDetDescripcion());
            nuevoRegistro.setSubTotal(det.getDetSubtotal());
            nuevoRegistro.setTotal(det.getDetTotal());
            nuevoRegistro.setDetIva(det.getDetIva());
            nuevoRegistro.setDetTotalconiva(det.getDetTotalconiva());
            nuevoRegistro.setTipoVenta(det.getDetTipoVenta());
            //valores con descuentos
            nuevoRegistro.setSubTotalDescuento(det.getDetSubtotaldescuento());
            nuevoRegistro.setDetTotaldescuento(det.getDetTotaldescuento());
            nuevoRegistro.setDetPordescuento(det.getDetPordescuento());
            nuevoRegistro.setDetValdescuento(det.getDetValdescuento());
            nuevoRegistro.setDetTotalconivadescuento(det.getDetTotaldescuentoiva());
            nuevoRegistro.setDetCantpordescuento(det.getDetCantpordescuento());
            nuevoRegistro.setDetIvaDesc(det.getDetIva());
            nuevoRegistro.setCodTipoVenta(det.getDetCodTipoVenta());
            nuevoRegistro.setDetSubtotaldescuentoporcantidad(det.getDetSubtotaldescuentoporcantidad());
            nuevoRegistro.setTotalInicial(det.getDetTotal());
            nuevoRegistro.setEsProducto(det.getIdProducto().getProdEsproducto());
            clietipo = det.getDetCodTipoVenta();
//            calcularValores(nuevoRegistro);
            listaDetalleFacturaDAODatos.add(nuevoRegistro);
        }

        getDetallefactura();
        calcularValoresTotales();
    }

    @Command
    @NotifyChange({"numeroFactura"})
    public void calcularNumeroFactTexto() {
        numeroFacturaTexto();
    }

    private void numeroFacturaTexto() {
        numeroFacturaText = "";
        for (int i = numeroFactura.toString().length(); i < 9; i++) {
            numeroFacturaText = numeroFacturaText + "0";
        }
        numeroFacturaText = numeroFacturaText + numeroFactura;
        System.out.println("nuemro texto " + numeroFacturaText);
    }

    private void numeroFactura() {
        Factura recuperada = servicioFactura.FindUltimaFactura();
        if (recuperada != null) {
            // System.out.println("numero de factura " + recuperada);
            numeroFactura = recuperada.getFacNumero() + 1;
            numeroFacturaTexto();
        } else {
            numeroFactura = 1;
            numeroFacturaText = "000000001";
        }
    }
//numero de guia

    private void numeroGuia() {
        Guiaremision recuperada = servicioGuia.findUltimaGuiaremision();
        if (recuperada != null) {
            // System.out.println("numero de factura " + recuperada);
            numeroGuia = recuperada.getFacNumero() + 1;
            numeroTexto(numeroGuia);
        } else {
            numeroGuia = 1;
            numeroGuiaText = "000000001";
        }
    }

    private void numeroTexto(Integer valor) {
        numeroGuiaText = "";
        for (int i = valor.toString().length(); i < 9; i++) {
            numeroGuiaText = numeroGuiaText + "0";
        }
        numeroGuiaText = numeroGuiaText + valor;
        System.out.println("numero texto guia  " + numeroGuiaText);
        //  return numeroGuiaText;
    }

    private void numeroProforma() {
        Factura recuperada = servicioFactura.FindUltimaProforma();
        if (recuperada != null) {
            System.out.println("numero de factura PROOOO " + recuperada);
            numeroFactura = recuperada.getFacNumProforma() + 1;
            System.out.println("numeroFactura PROF " + numeroFactura);
        } else {
            numeroFactura = 1;
        }
    }

    private void numeroNotaEntrega() {
        Factura recuperada = servicioFactura.findUltimaNotaEnt();
        if (recuperada != null) {
            // System.out.println("numero de factura " + recuperada);
            numeroFactura = recuperada.getFacNumNotaEntrega() + 1;
        } else {
            numeroFactura = 1;
        }
    }

    private void numeroNotaVenta() {
        Factura recuperada = servicioFactura.findUltimaNotaVent();
        if (recuperada != null) {
            // System.out.println("numero de factura " + recuperada);
            numeroFactura = recuperada.getFacNumNotaVenta() + 1;
        } else {
            numeroFactura = 1;
        }
    }

    private void verificarSecNumeracion() {

        if (tipoVenta.equals("FACT")) {
            numeroFactura();
        } else if (tipoVenta.equals("PROF")) {
            numeroProforma();
        } else if (tipoVenta.equals("NTE")) {
            numeroNotaEntrega();
        } else if (tipoVenta.equals("NTV")) {
            numeroNotaVenta();
        } else {

            System.out.println("cliente  " + clienteBuscado);
            numeroFactura = 0;

        }

    }

    @Command
    @NotifyChange({"listMedidores", "buscarMedidor"})
    public void buscarMedidores() {

        listMedidores = servicioMedidor.findByNombreApellido(buscarMedidor);
    }

    @Command
    @NotifyChange({"listMedidores", "buscarMedidor"})
    public void buscarMedidoresNumero() {

        listMedidores = servicioMedidor.findMedidorForNumero(buscarMedidorNumero);
    }

    /*AGREGAMOS DESDE LA LSITA */
    @Command
    @NotifyChange({"listaDetalleFacturaDAOMOdel", "subTotalCotizacion", "ivaCotizacion", "valorTotalCotizacion", "totalDescuento", "buscarNombreProd", "valorTotalInicialVent", "descuentoValorFinal", "subTotalBaseCero", "listaProducto", "totalItems"})
    public void agregarItemLista(@BindingParam("valor") Producto producto) {

        if (parametrizar.getParNumRegistrosFactura().intValue() <= listaDetalleFacturaDAOMOdel.size()) {
            Clients.showNotification("Numero de registros permitidos imprima y genere otra factura",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 5000, true);
            return;
        }
        /*calcula con el iva para todo el 12%*/
//        BigDecimal factorIva = (parametrizar.getParIva().divide(BigDecimal.valueOf(100.0)));
/*calcula con el iva para todo el 12%*/
        BigDecimal factorIva = (producto.getProdIva().divide(BigDecimal.valueOf(100.0)));
        BigDecimal factorSacarSubtotal = (factorIva.add(BigDecimal.ONE));

        List<DetalleFacturaDAO> listaPedido = listaDetalleFacturaDAOMOdel.getInnerList();

        for (DetalleFacturaDAO item : listaPedido) {
            if (item.getProducto() == null) {
                ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).remove(item);
                break;
            }
        }
        productoBuscado = producto;
        if (parametrizar.getParActivaKardex() && producto.getProdEsproducto()) {
            Kardex kardex = servicioKardex.FindALlKardexs(productoBuscado);
            if (kardex.getKarTotal().intValue() < 1) {
                Clients.showNotification("Verifique el stock del producto cuenta con " + kardex.getKarTotal().intValue() + " en estock",
                            Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
                agregarRegistroVacio();
                return;
            }
        }

        System.out.println("cliente panel  " + clienteBuscado);
        if (productoBuscado != null) {
            DetalleFacturaDAO valor = new DetalleFacturaDAO();
            valor.setCantidad(BigDecimal.ONE);
            valor.setProducto(productoBuscado);
            valor.setDescripcion(productoBuscado.getProdNombre());
            valor.setDetPordescuento(DESCUENTOGENERAL);
            valor.setCodigo(productoBuscado.getProdCodigo());
            valor.setEsProducto(producto.getProdEsproducto());

//            valor.setMesCobro(medidorEncontrado.getMesActual().getNombre());
            /*GREGA LECTURA AL REGISTRO*/
//            valor.setLectura(lectura);
            BigDecimal costVentaTipoCliente = BigDecimal.ZERO;
            BigDecimal costVentaTipoClienteInicial = BigDecimal.ZERO;
            String tipoVenta = "NORMAL";
            if (clienteBuscado.getClietipo() == 0) {
                tipoVenta = "NORMAL";
                if (clietipo.equals("0")) {
                    costVentaTipoClienteInicial = productoBuscado.getPordCostoVentaFinal();
                    costVentaTipoCliente = productoBuscado.getPordCostoVentaFinal();
                } else if (clietipo.equals("1")) {
                    tipoVenta = "PREFERENCIAL 1";
                    costVentaTipoClienteInicial = productoBuscado.getProdCostoPreferencial();
                    costVentaTipoCliente = productoBuscado.getProdCostoPreferencial();
                } else if (clietipo.equals("2")) {
                    tipoVenta = "PREFERENCIAL 2";
                    costVentaTipoClienteInicial = productoBuscado.getProdCostoPreferencialDos();
                    costVentaTipoCliente = productoBuscado.getProdCostoPreferencialDos();
                }

                valor.setTotalInicial(costVentaTipoClienteInicial.setScale(4, RoundingMode.FLOOR));
                BigDecimal porcentajeDesc = valor.getDetPordescuento().divide(BigDecimal.valueOf(100.0), 5, RoundingMode.FLOOR);
                BigDecimal valorDescuentoIva = costVentaTipoCliente.multiply(porcentajeDesc).setScale(5, RoundingMode.FLOOR);;
                //valor unitario con descuento ioncluido iva
                BigDecimal valorTotalIvaDesc = costVentaTipoCliente.subtract(valorDescuentoIva).setScale(5, RoundingMode.FLOOR);
                //valor unit sin iva sin descuento
                BigDecimal subTotal = costVentaTipoCliente.divide(factorSacarSubtotal, 5, RoundingMode.FLOOR);
                valor.setSubTotal(subTotal);
                //valor unitario sin iva con descuento
                BigDecimal subTotalDescuento = valorTotalIvaDesc.divide(factorSacarSubtotal, 5, RoundingMode.FLOOR);
                valor.setSubTotalDescuento(subTotalDescuento);
                //valor del descuento
                BigDecimal valorDescuento = valor.getSubTotal().subtract(valor.getSubTotalDescuento()).setScale(5, RoundingMode.FLOOR);
                valor.setDetValdescuento(valorDescuento);
                BigDecimal valorIva = subTotal.multiply(factorIva).multiply(valor.getCantidad());
//                valor.setDetIva(valorIva);
                //valor del iva con descuento
                BigDecimal valorIvaDesc = subTotalDescuento.multiply(factorIva).multiply(valor.getCantidad());
                valor.setDetIva(valorIvaDesc);
                //valor total sin decuento y con iva
                valor.setTotal(valorTotalIvaDesc.setScale(5, RoundingMode.FLOOR));
                //valor total con decuento y con iva
                valor.setDetTotaldescuento(valorTotalIvaDesc);
                valor.setDetTotalconiva(valor.getCantidad().multiply(costVentaTipoCliente));
                valor.setDetTotalconivadescuento(valor.getCantidad().multiply(valorTotalIvaDesc));
                valor.setDetCantpordescuento(valorDescuento.multiply(valor.getCantidad()));
                //cantidad por subtotal con descuento
                valor.setDetSubtotaldescuentoporcantidad(subTotalDescuento.multiply(valor.getCantidad()));
                valor.setTipoVenta("NORMAL");
                valor.setCodTipoVenta(clietipo);
            }
            //nuevoRegistro.setSubTotal(productoBuscado.getPordCostoVentaFinal());
            ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).add(valor);

            //ingresa un registro vacio
            boolean registroVacio = true;
            List<DetalleFacturaDAO> listaPedidoPost = listaDetalleFacturaDAOMOdel.getInnerList();

            for (DetalleFacturaDAO item : listaPedidoPost) {
                if (item.getProducto() == null) {
                    registroVacio = false;
                    break;
                }
            }

            System.out.println("existe un vacio " + registroVacio);
            if (registroVacio) {
                DetalleFacturaDAO nuevoRegistroPost = new DetalleFacturaDAO();
//                nuevoRegistroPost.setProducto(productoBuscado);
                nuevoRegistroPost.setCantidad(BigDecimal.ZERO);
                nuevoRegistroPost.setSubTotal(BigDecimal.ZERO);
                nuevoRegistroPost.setDetIva(BigDecimal.ZERO);
                nuevoRegistroPost.setDetTotalconiva(BigDecimal.ZERO);
                nuevoRegistroPost.setDescripcion("");
                nuevoRegistroPost.setProducto(null);
                ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).add(nuevoRegistroPost);
            }
        }
        calcularValoresTotales();
        codigoBusqueda = "";

        buscarNombreProd = "";
        idBusquedaProd.setFocus(Boolean.TRUE);
        /*COLOCA EL FOCO EN EL BUSCADOR*/
//        idBusquedaProd.setFocus(Boolean.TRUE);
    }

    @Command
    @NotifyChange({"listaDetalleFacturaDAOMOdel", "subTotalCotizacion", "ivaCotizacion", "valorTotalCotizacion", "totalDescuento", "buscarNombreProd", "valorTotalInicialVent", "descuentoValorFinal", "subTotalBaseCero", "listaProducto", "totalItems"})
    public void agregarLectura(@BindingParam("valor") Lectura lectura) {

        BigDecimal baseImponibleMulta = BigDecimal.ZERO;

        if (parametrizar.getParNumRegistrosFactura().intValue() <= listaDetalleFacturaDAOMOdel.size()) {
            Clients.showNotification("Numero de registros permitidos imprima y genere otra factura",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 5000, true);
            return;
        }
        //consulta el item tipo agua potable 
        Producto producto = servicioProducto.productoAguaPotable().size() > 0 ? servicioProducto.productoAguaPotable().get(0) : null;
        Producto prodMulta = servicioProducto.productoAguaPotable().size() > 0 ? servicioProducto.productoAguaPotable().get(0) : null;
        Producto prodAlcantarillado = servicioProducto.productoAguaPotable().size() > 0 ? servicioProducto.productoAguaPotable().get(0) : null;
        Producto prodAmbiente = servicioProducto.productoAguaPotable().size() > 0 ? servicioProducto.productoAguaPotable().get(0) : null;
        Producto prodDesechos = servicioProducto.productoAguaPotable().size() > 0 ? servicioProducto.productoAguaPotable().get(0) : null;

        /*calcula con el iva para todo el 12%*/
        // BigDecimal factorIva = (parametrizar.getParIva().divide(BigDecimal.valueOf(100.0)));
        /*calcula con el iva para todo el 12%*/
        BigDecimal factorIva = (parametrizar.getParIva().divide(BigDecimal.valueOf(100.0)));
        //no grava Iva el agua
        BigDecimal factorSacarSubtotal = BigDecimal.ONE;

        List<DetalleFacturaDAO> listaPedido = listaDetalleFacturaDAOMOdel.getInnerList();

        for (DetalleFacturaDAO item : listaPedido) {
            if (item.getProducto() == null) {
                ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).remove(item);
                break;
            }
        }

        productoBuscado = producto;
//        if (parametrizar.getParActivaKardex() && producto.getProdEsproducto()) {
//            Kardex kardex = servicioKardex.FindALlKardexs(productoBuscado);
//            if (kardex.getKarTotal().intValue() < 1) {
//                Clients.showNotification("Verifique el stock del producto cuenta con " + kardex.getKarTotal().intValue() + " en estock",
//                        Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
//                agregarRegistroVacio();
//                return;
//            }
//        }

        BigDecimal cobroTotal = BigDecimal.ZERO;
        BigDecimal valorCobroBase = BigDecimal.ZERO;
        BigDecimal valorCobroExce = BigDecimal.ZERO;
        BigDecimal basicaMasExcedente = BigDecimal.ZERO;
        BigDecimal alcantarillado = BigDecimal.ZERO;
        BigDecimal desechos = BigDecimal.ZERO;
        BigDecimal ambiente = BigDecimal.ZERO;
        System.out.println("cliente panel  " + clienteBuscado);
        if (productoBuscado != null) {
            DetalleFacturaDAO valor = new DetalleFacturaDAO();
            valor.setCantidad(BigDecimal.ONE);
            valor.setProducto(productoBuscado);
            valor.setDescripcion(productoBuscado.getProdNombre());
            valor.setMesCobro(lectura.getMesActual().getNombre());
            valor.setDetPordescuento(DESCUENTOGENERAL);
            valor.setCodigo(productoBuscado.getProdCodigo());
            valor.setEsProducto(producto.getProdEsproducto());
            DetalleTarifa detalleTarifa = null;
            if (lectura.getLecMetrosCubicos().doubleValue() <= 40) {
                //obtiene la tarif del medidor            
                // Tarifa tarifa = lectura.getIdMedidor().getIdTarifa();
                detalleTarifa = servicioDetalleTarifas.findIdTarifaAndMetros(lectura.getIdMedidor().getIdTarifa(), lectura.getLecMetrosCubicos());
                if (detalleTarifa == null) {
                    Clients.showNotification("El medidor no esta asignado una tarifa...",
                                Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 2000, true);
                    return;
                }

                BigDecimal adicionales = lectura.getLecMetrosCubicos().doubleValue() <= detalleTarifa.getDettMetroFinal().doubleValue() ? lectura.getLecMetrosCubicos().subtract(detalleTarifa.getIdTarifa().getTariMetrosBase()) : BigDecimal.ZERO;
                valorCobroBase = detalleTarifa.getDettPrecioBase();
                BigDecimal precioExcedente = detalleTarifa.getDettPrecioExcedente();
                valorCobroExce = adicionales.multiply(precioExcedente);
                basicaMasExcedente = valorCobroBase.add(valorCobroExce);
                alcantarillado = detalleTarifa.getDettAlcantarillado();
            } else {

                detalleTarifa = servicioDetalleTarifas.findIdTarifaAndMetros(lectura.getIdMedidor().getIdTarifa(), lectura.getLecMetrosCubicos());
                BigDecimal adicionales = lectura.getLecMetrosCubicos();
                valorCobroBase = detalleTarifa.getDettPrecioBase();
                BigDecimal precioExcedente = detalleTarifa.getDettPrecioExcedente();
                valorCobroExce = adicionales.multiply(precioExcedente);
                basicaMasExcedente = valorCobroBase.add(valorCobroExce);
                alcantarillado = detalleTarifa.getDettAlcantarillado();
            }


            /*AMBIENTE*/
            BigDecimal metrosBaseAmbienteUno = BigDecimal.valueOf(30);
            BigDecimal metrosBaseAmbienteDos = BigDecimal.valueOf(31);
            BigDecimal excedenteAmbiente = BigDecimal.ZERO;
            Boolean baseUno = Boolean.FALSE;
            Boolean baseDos = Boolean.FALSE;

            BigDecimal adicionalesAmbiente = lectura.getLecMetrosCubicos().subtract(metrosBaseAmbienteUno);

            BigDecimal adicionalesAmbienteDos = lectura.getLecMetrosCubicos().subtract(metrosBaseAmbienteDos);

            if (adicionalesAmbiente.doubleValue() <= 0) {
                excedenteAmbiente = BigDecimal.ZERO;
                baseUno = Boolean.TRUE;
            } else if (adicionalesAmbienteDos.doubleValue() == 0) {
                excedenteAmbiente = BigDecimal.ZERO;
                baseDos = Boolean.TRUE;
            } else if (adicionalesAmbienteDos.doubleValue() > 0) {
                excedenteAmbiente = adicionalesAmbienteDos;
                baseDos = Boolean.TRUE;
            } else {
                excedenteAmbiente = BigDecimal.ZERO;
                baseUno = Boolean.TRUE;
            }

//            if (detalleTarifa.getDettValidadesecho()) {
//                desechos = (basicaMasExcedente.multiply(detalleTarifa.getDettPorcentajeDesechos())).divide(BigDecimal.valueOf(100));
//                desechos = ArchivoUtils.redondearDecimales(desechos, 2);
//            } else {
            desechos = detalleTarifa.getDettDesechos();
//            }

//            if (baseUno) {
            ambiente = detalleTarifa.getDettAmbiente();
//            }
//            if (baseDos) {
//                ambiente = BigDecimal.valueOf(1);
//            }

//            if (excedenteAmbiente.doubleValue() > 0) {
//                BigDecimal valorExedenteAmb = excedenteAmbiente.multiply(BigDecimal.valueOf(0.02));
//                ambiente = BigDecimal.valueOf(1).add(valorExedenteAmb);
//            }
            ambiente = ArchivoUtils.redondearDecimales(ambiente, 2);
            alcantarillado = ArchivoUtils.redondearDecimales(alcantarillado, 2);

            cobroTotal = valorCobroBase;
//            cobroTotal = valorCobroBase.add(valorCobroExce);
            cobroTotal = ArchivoUtils.redondearDecimales(cobroTotal, 2);
//            cobroTotal = valorCobroBase.add(valorCobroExce).add(alcantarillado).add(desechos).add(ambiente);
            valor.setTotalInicial(cobroTotal);
            valor.setTotal(cobroTotal);

            /*valor para la multa*/
            baseImponibleMulta = cobroTotal.add(alcantarillado).add(ambiente).add(desechos).add(valorCobroExce);
            //para llenar lectura como producto
            if (valor.getCantidad() == null) {
                return;
            }

            if (valor.getCantidad().doubleValue() <= 0) {
                return;
            }

            if (valor.getProducto() == null) {
                return;
            }
            /*SERVICOS */
            if (!valor.getEsProducto()) {

                valor.setTotalInicial(valor.getTotal());
            }

            if (valor.getCantidad().doubleValue() > 0) {
                /*CALCULO DEL PORCENTAJE DE DESCUENTO*/
                BigDecimal porcentajeDesc = BigDecimal.ZERO;
                BigDecimal valorPorcentaje = BigDecimal.ZERO;
                BigDecimal valorDescuentoIva = BigDecimal.ZERO;
                if (valor.getEsProducto()) {
                    porcentajeDesc = valor.getTotal().multiply(BigDecimal.valueOf(100.0));
                    valorPorcentaje = valor.getTotalInicial().intValue() > 0 ? porcentajeDesc.divide(valor.getTotalInicial(), 7, RoundingMode.FLOOR) : BigDecimal.ZERO;
                    valorDescuentoIva = valor.getTotalInicial().subtract(valor.getTotal());
                }

                /*COLOCAMOS EN EL CAMPO DE DESCUENTO*/
                BigDecimal porcentajeDiferencia = BigDecimal.valueOf(100.0).subtract(valorPorcentaje).setScale(5, RoundingMode.FLOOR);
                valor.setDetPordescuento(porcentajeDiferencia);
                //valor unitario con descuento ioncluido iva
                BigDecimal valorTotalIvaDesc = valor.getTotalInicial().subtract(valorDescuentoIva);

                //valor unitario sin iva con descuento
                BigDecimal subTotalDescuento = valorTotalIvaDesc.divide(factorSacarSubtotal, 7, RoundingMode.FLOOR);

                valor.setSubTotalDescuento(subTotalDescuento);
                //valor del descuento
                BigDecimal valorDescuento = BigDecimal.ZERO;
//                if (!valor.getEsProducto()) {
//                    valor.setSubTotal(valor.getSubTotalDescuento());
//                }
//                if (valor.getEsProducto()) {
//                    valorDescuento = ArchivoUtils.redondearDecimales(valor.getSubTotal(), 5).subtract(ArchivoUtils.redondearDecimales(valor.getSubTotalDescuento(), 5));
//                }
                valor.setDetValdescuento(BigDecimal.ZERO);
                //valor del iva con descuento
//                BigDecimal valorIvaDesc = subTotalDescuento.multiply(factorIva).multiply(valor.getCantidad());
                BigDecimal valorIvaDesc = BigDecimal.ZERO;

                valor.setDetIva(valorIvaDesc);

                //valor total con decuento y con iva
                valor.setDetTotaldescuento(valorDescuento.multiply(valor.getCantidad()));
                //cantidad por subtotal con descuento
                valor.setDetSubtotaldescuentoporcantidad(subTotalDescuento.multiply(valor.getCantidad()));
                valor.setDetTotalconivadescuento(valor.getCantidad().multiply(valorTotalIvaDesc));
                valor.setDetTotalconiva(valor.getCantidad().multiply(valor.getTotal()));

                valor.setDetCantpordescuento(valorDescuento.multiply(valor.getCantidad()));
                /*GREGA LECTURA AL REGISTRO*/
                valor.setLectura(lectura);

            }

            /*EXCEDENTE*/
            DetalleFacturaDAO valorExcedente = new DetalleFacturaDAO();
            valorExcedente.setCantidad(BigDecimal.ONE);
            valorExcedente.setProducto(productoBuscado);
            valorExcedente.setDescripcion("EXCEDENTE");
            valorExcedente.setDetPordescuento(DESCUENTOGENERAL);
            valorExcedente.setCodigo(productoBuscado.getProdCodigo());
            valorExcedente.setEsProducto(producto.getProdEsproducto());
            valorExcedente.setTotalInicial(valorCobroExce);
            valorExcedente.setTotal(ArchivoUtils.redondearDecimales(valorCobroExce, 2));
            // valorExcedente.setLectura(lectura);
            //para llenar lectura como producto
            if (valorExcedente.getCantidad() == null) {
                return;
            }

            if (valorExcedente.getCantidad().doubleValue() <= 0) {
                return;
            }

            if (valorExcedente.getProducto() == null) {
                return;
            }
            /*SERVICOS */
            if (!valorExcedente.getEsProducto()) {

                valorExcedente.setTotalInicial(valorExcedente.getTotal());
            }

            if (valorExcedente.getCantidad().doubleValue() > 0) {
                /*CALCULO DEL PORCENTAJE DE DESCUENTO*/
                BigDecimal porcentajeDesc = BigDecimal.ZERO;
                BigDecimal valorPorcentaje = BigDecimal.ZERO;
                BigDecimal valorDescuentoIva = BigDecimal.ZERO;
                if (valorExcedente.getEsProducto()) {
                    porcentajeDesc = valorExcedente.getTotal().multiply(BigDecimal.valueOf(100.0));
                    valorPorcentaje = valorExcedente.getTotalInicial().intValue() > 0 ? porcentajeDesc.divide(valorExcedente.getTotalInicial(), 7, RoundingMode.FLOOR) : BigDecimal.ZERO;
                    valorDescuentoIva = valorExcedente.getTotalInicial().subtract(valorExcedente.getTotal());
                }

                /*COLOCAMOS EN EL CAMPO DE DESCUENTO*/
                BigDecimal porcentajeDiferencia = BigDecimal.valueOf(100.0).subtract(valorPorcentaje).setScale(5, RoundingMode.FLOOR);
                valorExcedente.setDetPordescuento(porcentajeDiferencia);
                //valor unitario con descuento ioncluido iva
                BigDecimal valorTotalIvaDesc = valorExcedente.getTotalInicial().subtract(valorDescuentoIva);

                //valor unitario sin iva con descuento
                BigDecimal subTotalDescuento = valorTotalIvaDesc.divide(factorSacarSubtotal, 7, RoundingMode.FLOOR);

                valorExcedente.setSubTotalDescuento(subTotalDescuento);
                //valor del descuento
                BigDecimal valorDescuento = BigDecimal.ZERO;
//                if (!valor.getEsProducto()) {
//                    valor.setSubTotal(valor.getSubTotalDescuento());
//                }
//                if (valor.getEsProducto()) {
//                    valorDescuento = ArchivoUtils.redondearDecimales(valor.getSubTotal(), 5).subtract(ArchivoUtils.redondearDecimales(valor.getSubTotalDescuento(), 5));
//                }
                valorExcedente.setDetValdescuento(BigDecimal.ZERO);
                //valor del iva con descuento
//                BigDecimal valorIvaDesc = subTotalDescuento.multiply(factorIva).multiply(valor.getCantidad());
                BigDecimal valorIvaDesc = BigDecimal.ZERO;

                valorExcedente.setDetIva(valorIvaDesc);

                //valor total con decuento y con iva
                valorExcedente.setDetTotaldescuento(valorDescuento.multiply(valorExcedente.getCantidad()));
                //cantidad por subtotal con descuento
                valorExcedente.setDetSubtotaldescuentoporcantidad(subTotalDescuento.multiply(valorExcedente.getCantidad()));
                valorExcedente.setDetTotalconivadescuento(valorExcedente.getCantidad().multiply(valorTotalIvaDesc));
                valorExcedente.setDetTotalconiva(valorExcedente.getCantidad().multiply(valorExcedente.getTotal()));

                valorExcedente.setDetCantpordescuento(valorDescuento.multiply(valorExcedente.getCantidad()));
                valorExcedente.setLectura(lectura);
            }

            /*FIN EXCEDENTE*/
 /*ALCANTARILLADO*/
            DetalleFacturaDAO valorAlcantarillado = new DetalleFacturaDAO();
            valorAlcantarillado.setCantidad(BigDecimal.ONE);
            valorAlcantarillado.setProducto(productoBuscado);
            valorAlcantarillado.setDescripcion("ALCANTARILLADO");
            valorAlcantarillado.setDetPordescuento(DESCUENTOGENERAL);
            valorAlcantarillado.setCodigo(productoBuscado.getProdCodigo());
            valorAlcantarillado.setEsProducto(producto.getProdEsproducto());
            valorAlcantarillado.setTotalInicial(alcantarillado);
            valorAlcantarillado.setTotal(alcantarillado);
            // valorAlcantarillado.setLectura(lectura);
            //para llenar lectura como producto
            if (valorAlcantarillado.getCantidad() == null) {
                return;
            }

            if (valorAlcantarillado.getCantidad().doubleValue() <= 0) {
                return;
            }

            if (valorAlcantarillado.getProducto() == null) {
                return;
            }
            /*SERVICOS */
            if (!valorAlcantarillado.getEsProducto()) {

                valorAlcantarillado.setTotalInicial(valorAlcantarillado.getTotal());
            }

            if (valorAlcantarillado.getCantidad().doubleValue() > 0) {
                /*CALCULO DEL PORCENTAJE DE DESCUENTO*/
                BigDecimal porcentajeDesc = BigDecimal.ZERO;
                BigDecimal valorPorcentaje = BigDecimal.ZERO;
                BigDecimal valorDescuentoIva = BigDecimal.ZERO;
                if (valorAlcantarillado.getEsProducto()) {
                    porcentajeDesc = valorAlcantarillado.getTotal().multiply(BigDecimal.valueOf(100.0));
                    valorPorcentaje = valorAlcantarillado.getTotalInicial().intValue() > 0 ? porcentajeDesc.divide(valorAlcantarillado.getTotalInicial(), 7, RoundingMode.FLOOR) : BigDecimal.ZERO;
                    valorDescuentoIva = valorAlcantarillado.getTotalInicial().subtract(valorAlcantarillado.getTotal());
                }

                /*COLOCAMOS EN EL CAMPO DE DESCUENTO*/
                BigDecimal porcentajeDiferencia = BigDecimal.valueOf(100.0).subtract(valorPorcentaje).setScale(5, RoundingMode.FLOOR);
                valorAlcantarillado.setDetPordescuento(porcentajeDiferencia);
                //valor unitario con descuento ioncluido iva
                BigDecimal valorTotalIvaDesc = valorAlcantarillado.getTotalInicial().subtract(valorDescuentoIva);

                //valor unitario sin iva con descuento
                BigDecimal subTotalDescuento = valorTotalIvaDesc.divide(factorSacarSubtotal, 7, RoundingMode.FLOOR);

                valorAlcantarillado.setSubTotalDescuento(subTotalDescuento);
                //valor del descuento
                BigDecimal valorDescuento = BigDecimal.ZERO;
//                if (!valor.getEsProducto()) {
//                    valor.setSubTotal(valor.getSubTotalDescuento());
//                }
//                if (valor.getEsProducto()) {
//                    valorDescuento = ArchivoUtils.redondearDecimales(valor.getSubTotal(), 5).subtract(ArchivoUtils.redondearDecimales(valor.getSubTotalDescuento(), 5));
//                }
                valorAlcantarillado.setDetValdescuento(BigDecimal.ZERO);
                //valor del iva con descuento
//                BigDecimal valorIvaDesc = subTotalDescuento.multiply(factorIva).multiply(valor.getCantidad());
                BigDecimal valorIvaDesc = BigDecimal.ZERO;

                valorAlcantarillado.setDetIva(valorIvaDesc);

                //valor total con decuento y con iva
                valorAlcantarillado.setDetTotaldescuento(valorDescuento.multiply(valorAlcantarillado.getCantidad()));
                //cantidad por subtotal con descuento
                valorAlcantarillado.setDetSubtotaldescuentoporcantidad(subTotalDescuento.multiply(valorAlcantarillado.getCantidad()));
                valorAlcantarillado.setDetTotalconivadescuento(valorAlcantarillado.getCantidad().multiply(valorTotalIvaDesc));
                valorAlcantarillado.setDetTotalconiva(valorAlcantarillado.getCantidad().multiply(valorAlcantarillado.getTotal()));

                valorAlcantarillado.setDetCantpordescuento(valorDescuento.multiply(valorAlcantarillado.getCantidad()));
                /*GREGA LECTURA AL REGISTRO*/
                valorAlcantarillado.setLectura(lectura);

            }

            /*FIN ALCANTARILLADO*/
 /*DESECHOS*/
            DetalleFacturaDAO valorDesechos = new DetalleFacturaDAO();
            valorDesechos.setCantidad(BigDecimal.ONE);
            valorDesechos.setProducto(productoBuscado);
            valorDesechos.setDescripcion("DESECHOS SOLIDOS");
            valorDesechos.setDetPordescuento(DESCUENTOGENERAL);
            valorDesechos.setCodigo(productoBuscado.getProdCodigo());
            valorDesechos.setEsProducto(producto.getProdEsproducto());
            // valorDesechos.setLectura(lectura);
            desechos = ArchivoUtils.redondearDecimales(desechos, 2);
            valorDesechos.setTotalInicial(desechos);
            valorDesechos.setTotal(desechos);
            //para llenar lectura como producto
            if (valorDesechos.getCantidad() == null) {
                return;
            }

            if (valorDesechos.getCantidad().doubleValue() <= 0) {
                return;
            }

            if (valorDesechos.getProducto() == null) {
                return;
            }
            /*SERVICOS */
            if (!valorDesechos.getEsProducto()) {

                valorDesechos.setTotalInicial(valorDesechos.getTotal());
            }

            if (valorDesechos.getCantidad().doubleValue() > 0) {
                /*CALCULO DEL PORCENTAJE DE DESCUENTO*/
                BigDecimal porcentajeDesc = BigDecimal.ZERO;
                BigDecimal valorPorcentaje = BigDecimal.ZERO;
                BigDecimal valorDescuentoIva = BigDecimal.ZERO;
                if (valorDesechos.getEsProducto()) {
                    porcentajeDesc = valorDesechos.getTotal().multiply(BigDecimal.valueOf(100.0));
                    valorPorcentaje = valorDesechos.getTotalInicial().intValue() > 0 ? porcentajeDesc.divide(valorDesechos.getTotalInicial(), 7, RoundingMode.FLOOR) : BigDecimal.ZERO;
                    valorDescuentoIva = valorDesechos.getTotalInicial().subtract(valorDesechos.getTotal());
                }

                /*COLOCAMOS EN EL CAMPO DE DESCUENTO*/
                BigDecimal porcentajeDiferencia = BigDecimal.valueOf(100.0).subtract(valorPorcentaje).setScale(5, RoundingMode.FLOOR);
                valorDesechos.setDetPordescuento(porcentajeDiferencia);
                //valor unitario con descuento ioncluido iva
                BigDecimal valorTotalIvaDesc = valorDesechos.getTotalInicial().subtract(valorDescuentoIva);

                //valor unitario sin iva con descuento
                BigDecimal subTotalDescuento = valorTotalIvaDesc.divide(factorSacarSubtotal, 7, RoundingMode.FLOOR);

                valorDesechos.setSubTotalDescuento(subTotalDescuento);
                //valor del descuento
                BigDecimal valorDescuento = BigDecimal.ZERO;
//                if (!valor.getEsProducto()) {
//                    valor.setSubTotal(valor.getSubTotalDescuento());
//                }
//                if (valor.getEsProducto()) {
//                    valorDescuento = ArchivoUtils.redondearDecimales(valor.getSubTotal(), 5).subtract(ArchivoUtils.redondearDecimales(valor.getSubTotalDescuento(), 5));
//                }
                valorDesechos.setDetValdescuento(BigDecimal.ZERO);
                //valor del iva con descuento
//                BigDecimal valorIvaDesc = subTotalDescuento.multiply(factorIva).multiply(valor.getCantidad());
                BigDecimal valorIvaDesc = BigDecimal.ZERO;

                valorDesechos.setDetIva(valorIvaDesc);

                //valor total con decuento y con iva
                valorDesechos.setDetTotaldescuento(valorDescuento.multiply(valorDesechos.getCantidad()));
                //cantidad por subtotal con descuento
                valorDesechos.setDetSubtotaldescuentoporcantidad(subTotalDescuento.multiply(valorDesechos.getCantidad()));
                valorDesechos.setDetTotalconivadescuento(valorDesechos.getCantidad().multiply(valorTotalIvaDesc));
                valorDesechos.setDetTotalconiva(valorDesechos.getCantidad().multiply(valorDesechos.getTotal()));

                valorDesechos.setDetCantpordescuento(valorDescuento.multiply(valorDesechos.getCantidad()));
                /*GREGA LECTURA AL REGISTRO*/
                valorDesechos.setLectura(lectura);

            }

            /*FIN DESECHOS*/
 /*AMBIENTE*/
            DetalleFacturaDAO valorAmbiente = new DetalleFacturaDAO();
            valorAmbiente.setCantidad(BigDecimal.ONE);
            valorAmbiente.setProducto(productoBuscado);
            valorAmbiente.setDescripcion("MEDIO AMBIENTE");
            valorAmbiente.setDetPordescuento(DESCUENTOGENERAL);
            valorAmbiente.setCodigo(productoBuscado.getProdCodigo());
            valorAmbiente.setEsProducto(producto.getProdEsproducto());
            valorAmbiente.setTotalInicial(ambiente);
            valorAmbiente.setTotal(ambiente);
            //valorAmbiente.setLectura(lectura);
            //para llenar lectura como producto
            if (valorAmbiente.getCantidad() == null) {
                return;
            }

            if (valorAmbiente.getCantidad().doubleValue() <= 0) {
                return;
            }

            if (valorAmbiente.getProducto() == null) {
                return;
            }
            /*SERVICOS */
            if (!valorAmbiente.getEsProducto()) {

                valorAmbiente.setTotalInicial(valorAmbiente.getTotal());
            }

            if (valorAmbiente.getCantidad().doubleValue() > 0) {
                /*CALCULO DEL PORCENTAJE DE DESCUENTO*/
                BigDecimal porcentajeDesc = BigDecimal.ZERO;
                BigDecimal valorPorcentaje = BigDecimal.ZERO;
                BigDecimal valorDescuentoIva = BigDecimal.ZERO;
                if (valorAmbiente.getEsProducto()) {
                    porcentajeDesc = valorAmbiente.getTotal().multiply(BigDecimal.valueOf(100.0));
                    valorPorcentaje = valorAmbiente.getTotalInicial().intValue() > 0 ? porcentajeDesc.divide(valorAmbiente.getTotalInicial(), 7, RoundingMode.FLOOR) : BigDecimal.ZERO;
                    valorDescuentoIva = valorAmbiente.getTotalInicial().subtract(valorAmbiente.getTotal());
                }

                /*COLOCAMOS EN EL CAMPO DE DESCUENTO*/
                BigDecimal porcentajeDiferencia = BigDecimal.valueOf(100.0).subtract(valorPorcentaje).setScale(5, RoundingMode.FLOOR);
                valorAmbiente.setDetPordescuento(porcentajeDiferencia);
                //valor unitario con descuento ioncluido iva
                BigDecimal valorTotalIvaDesc = valorAmbiente.getTotalInicial().subtract(valorDescuentoIva);

                //valor unitario sin iva con descuento
                BigDecimal subTotalDescuento = valorTotalIvaDesc.divide(factorSacarSubtotal, 7, RoundingMode.FLOOR);

                valorAmbiente.setSubTotalDescuento(subTotalDescuento);
                //valor del descuento
                BigDecimal valorDescuento = BigDecimal.ZERO;
//                if (!valor.getEsProducto()) {
//                    valor.setSubTotal(valor.getSubTotalDescuento());
//                }
//                if (valor.getEsProducto()) {
//                    valorDescuento = ArchivoUtils.redondearDecimales(valor.getSubTotal(), 5).subtract(ArchivoUtils.redondearDecimales(valor.getSubTotalDescuento(), 5));
//                }
                valorAmbiente.setDetValdescuento(BigDecimal.ZERO);
                //valor del iva con descuento
//                BigDecimal valorIvaDesc = subTotalDescuento.multiply(factorIva).multiply(valor.getCantidad());
                BigDecimal valorIvaDesc = BigDecimal.ZERO;

                valorAmbiente.setDetIva(valorIvaDesc);

                //valor total con decuento y con iva
                valorAmbiente.setDetTotaldescuento(valorDescuento.multiply(valorAmbiente.getCantidad()));
                //cantidad por subtotal con descuento
                valorAmbiente.setDetSubtotaldescuentoporcantidad(subTotalDescuento.multiply(valorAmbiente.getCantidad()));
                valorAmbiente.setDetTotalconivadescuento(valorAmbiente.getCantidad().multiply(valorTotalIvaDesc));
                valorAmbiente.setDetTotalconiva(valorAmbiente.getCantidad().multiply(valorAmbiente.getTotal()));

                valorAmbiente.setDetCantpordescuento(valorDescuento.multiply(valorAmbiente.getCantidad()));
                /*GREGA LECTURA AL REGISTRO*/
                valorAmbiente.setLectura(lectura);

            }

            /*FIN AMBIENTE*/
            //nuevoRegistro.setSubTotal(productoBuscado.getPordCostoVentaFinal());
            /*Calculo de la multa*/
            Integer dias = ArchivoUtils.direfenciaFechaDias(lectura.getLecFecha(), new Date());
            DetalleFacturaDAO valorMulta = new DetalleFacturaDAO();
            DetalleFacturaDAO valorMultaInt = new DetalleFacturaDAO();
            if (lectura.getLecFecha() != null) {

//                 BigDecimal baseImponibleMulta=valor.getTotal().add(valorAmbiente.getTotal());
                /*PARA LOS 60*/
                if (dias >= 90) {

                    /*MULTA POR MAS DE 60 DIAS*/
                    BigDecimal cobroTotalMulta =parametrizar.getParMultaCorte() != null ? BigDecimal.valueOf(3).multiply(parametrizar.getParMultaCorte()) : BigDecimal.ZERO;

                    if (!multaCobrada) {
                        multaCobrada = Boolean.TRUE;
                        valorMulta.setCantidad(BigDecimal.ONE);
                        valorMulta.setProducto(prodMulta);
                        valorMulta.setDescripcion("RECONEXION POR SEGUNDA NOTIFICACION");
                        valorMulta.setDetPordescuento(BigDecimal.ZERO);
                        valorMulta.setCodigo(prodMulta.getProdCodigo());
                        valorMulta.setEsProducto(producto.getProdEsproducto());
//                    valorMulta.setTotalInicial(cobroTotal);
                        cobroTotalMulta = ArchivoUtils.redondearDecimales(cobroTotalMulta, 2);
                        valorMulta.setTotalInicial(cobroTotalMulta);
                        valorMulta.setTotal(cobroTotalMulta);
                        valorMulta.setLectura(lectura);
                        /*CALUCLOS DE FACTURA*/
                        BigDecimal porcentajeDesc = BigDecimal.ZERO;
                        BigDecimal valorPorcentaje = BigDecimal.ZERO;
                        BigDecimal valorDescuentoIva = BigDecimal.ZERO;
                        if (valorMulta.getEsProducto()) {
                            porcentajeDesc = valorMulta.getTotal().multiply(BigDecimal.valueOf(100.0));
                            valorPorcentaje = valorMulta.getTotalInicial().intValue() > 0 ? porcentajeDesc.divide(valorMulta.getTotalInicial(), 7, RoundingMode.FLOOR) : BigDecimal.ZERO;
                            valorDescuentoIva = valorMulta.getTotalInicial().subtract(valorMulta.getTotal());
                        }

                        /*COLOCAMOS EN EL CAMPO DE DESCUENTO*/
                        BigDecimal porcentajeDiferencia = BigDecimal.valueOf(100.0).subtract(valorPorcentaje).setScale(5, RoundingMode.FLOOR);
                        valorMulta.setDetPordescuento(porcentajeDiferencia);
                        //valor unitario con descuento ioncluido iva
                        BigDecimal valorTotalIvaDesc = valorMulta.getTotalInicial().subtract(valorDescuentoIva);

                        //valor unitario sin iva con descuento
                        BigDecimal subTotalDescuento = valorTotalIvaDesc.divide(factorSacarSubtotal, 7, RoundingMode.FLOOR);

                        valorMulta.setSubTotalDescuento(subTotalDescuento);
                        //valor del descuento
                        BigDecimal valorDescuento = BigDecimal.ZERO;
                        valorMulta.setDetValdescuento(BigDecimal.ZERO);
                        //valor del iva con descuento
                        BigDecimal valorIvaDesc = BigDecimal.ZERO;

                        valorMulta.setDetIva(valorIvaDesc);

                        //valor total con decuento y con iva
                        valorMulta.setDetTotaldescuento(valorDescuento.multiply(valorMulta.getCantidad()));
                        //cantidad por subtotal con descuento
                        valorMulta.setDetSubtotaldescuentoporcantidad(subTotalDescuento.multiply(valorMulta.getCantidad()));
                        valorMulta.setDetTotalconivadescuento(valorMulta.getCantidad().multiply(valorTotalIvaDesc));
                        valorMulta.setDetTotalconiva(valorMulta.getCantidad().multiply(valorMulta.getTotal()));

                        valorMulta.setDetCantpordescuento(valorDescuento.multiply(valorMulta.getCantidad()));

                    }
                    
                } else if (dias >= 60) {
                     BigDecimal cobroTotalMulta =parametrizar.getParMultaCorte() != null ? parametrizar.getParMultaCorte() : BigDecimal.ZERO;

//                    BigDecimal cobroTotalMulta = (baseImponibleMulta.divide(BigDecimal.valueOf(100.0))).multiply(porcentMulta);
                    cobroTotalMulta = ArchivoUtils.redondearDecimales(cobroTotalMulta, 2);
                    valorMulta.setCantidad(BigDecimal.ONE);
                    valorMulta.setProducto(prodMulta);
                    valorMulta.setDescripcion("RECONEXXION POR PRIMERA NOTIFICACION");
                    valorMulta.setDetPordescuento(BigDecimal.ZERO);
                    valorMulta.setCodigo(prodMulta.getProdCodigo());
                    valorMulta.setEsProducto(producto.getProdEsproducto());
                    valorMulta.setTotalInicial(cobroTotalMulta);
                    valorMulta.setTotal(cobroTotalMulta);
                    valorMulta.setLectura(lectura);
                    /*CALUCLOS DE FACTURA*/
                    BigDecimal porcentajeDesc = BigDecimal.ZERO;
                    BigDecimal valorPorcentaje = BigDecimal.ZERO;
                    BigDecimal valorDescuentoIva = BigDecimal.ZERO;
                    if (valorMulta.getEsProducto()) {
                        porcentajeDesc = valorMulta.getTotal().multiply(BigDecimal.valueOf(100.0));
                        valorPorcentaje = valorMulta.getTotalInicial().intValue() > 0 ? porcentajeDesc.divide(valorMulta.getTotalInicial(), 7, RoundingMode.FLOOR) : BigDecimal.ZERO;
                        valorDescuentoIva = valorMulta.getTotalInicial().subtract(valorMulta.getTotal());
                    }

                    /*COLOCAMOS EN EL CAMPO DE DESCUENTO*/
                    BigDecimal porcentajeDiferencia = BigDecimal.valueOf(100.0).subtract(valorPorcentaje).setScale(5, RoundingMode.FLOOR);
                    valorMulta.setDetPordescuento(porcentajeDiferencia);
                    //valor unitario con descuento ioncluido iva
                    BigDecimal valorTotalIvaDesc = valorMulta.getTotalInicial().subtract(valorDescuentoIva);

                    //valor unitario sin iva con descuento
                    BigDecimal subTotalDescuento = valorTotalIvaDesc.divide(factorSacarSubtotal, 7, RoundingMode.FLOOR);

                    valorMulta.setSubTotalDescuento(subTotalDescuento);
                    //valor del descuento
                    BigDecimal valorDescuento = BigDecimal.ZERO;
                    valorMulta.setDetValdescuento(BigDecimal.ZERO);
                    //valor del iva con descuento
                    BigDecimal valorIvaDesc = BigDecimal.ZERO;

                    valorMulta.setDetIva(valorIvaDesc);

                    //valor total con decuento y con iva
                    valorMulta.setDetTotaldescuento(valorDescuento.multiply(valorMulta.getCantidad()));
                    //cantidad por subtotal con descuento
                    valorMulta.setDetSubtotaldescuentoporcantidad(subTotalDescuento.multiply(valorMulta.getCantidad()));
                    valorMulta.setDetTotalconivadescuento(valorMulta.getCantidad().multiply(valorTotalIvaDesc));
                    valorMulta.setDetTotalconiva(valorMulta.getCantidad().multiply(valorMulta.getTotal()));

                    valorMulta.setDetCantpordescuento(valorDescuento.multiply(valorMulta.getCantidad()));
                } else {

                }

            } else {
                Clients.showNotification("Verifique la fecha de la lectura", Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
            }

            if (valor.getTotalInicial().doubleValue() > 0) {
                ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).add(valor);
            }
            if (valorExcedente.getTotalInicial().doubleValue() > 0) {
                ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).add(valorExcedente);
            }

            if (valorAlcantarillado.getTotalInicial().doubleValue() > 0) {
                ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).add(valorAlcantarillado);
            }
            if (valorDesechos.getTotalInicial().doubleValue() > 0) {
                ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).add(valorDesechos);
            }
            if (valorAmbiente.getTotalInicial().doubleValue() > 0) {
                ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).add(valorAmbiente);
            }

            if (dias >= 60) {
                if (valorMulta.getTotalInicial().doubleValue() > 0) {
                    ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).add(valorMulta);
                }
                if (valorMultaInt.getTotalInicial().doubleValue() > 0) {
                    ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).add(valorMultaInt);
                }

            } else if (dias >= 30) {
                if (valorMulta.getTotalInicial().doubleValue() > 0) {
                    ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).add(valorMulta);
                }

            }

            //ingresa un registro vacio
            boolean registroVacio = true;
            List<DetalleFacturaDAO> listaPedidoPost = listaDetalleFacturaDAOMOdel.getInnerList();

            for (DetalleFacturaDAO item : listaPedidoPost) {
                if (item.getProducto() == null) {
                    registroVacio = false;
                    break;
                }
            }

            System.out.println("existe un vacio " + registroVacio);
            if (registroVacio) {
                DetalleFacturaDAO nuevoRegistroPost = new DetalleFacturaDAO();
//                nuevoRegistroPost.setProducto(productoBuscado);
                nuevoRegistroPost.setCantidad(BigDecimal.ZERO);
                nuevoRegistroPost.setSubTotal(BigDecimal.ZERO);
                nuevoRegistroPost.setDetIva(BigDecimal.ZERO);
                nuevoRegistroPost.setDetTotalconiva(BigDecimal.ZERO);
                nuevoRegistroPost.setDescripcion("");
                nuevoRegistroPost.setProducto(null);
                ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).add(nuevoRegistroPost);
            }
        }
        calcularValoresTotales();
        codigoBusqueda = "";

        buscarNombreProd = "";
//        idBusquedaProd.setFocus(Boolean.TRUE);
        /*COLOCA EL FOCO EN EL BUSCADOR*/
//        idBusquedaProd.setFocus(Boolean.TRUE);
    }

    @Command
    @NotifyChange({"listaDetalleFacturaDAOMOdel", "subTotalCotizacion", "ivaCotizacion", "valorTotalCotizacion", "totalDescuento", "valorTotalInicialVent", "descuentoValorFinal", "subTotalBaseCero"})
    public void cambiarRegistro(@BindingParam("valor") DetalleFacturaDAO valor) {
        if (parametrizar.getParNumRegistrosFactura().intValue() <= listaDetalleFacturaDAOMOdel.size()) {
            Clients.showNotification("Numero de registros permitidos, imprima y genere otra factura", Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
            return;
        }

        if (!clienteBuscado.getCliCedula().equals("")) {
            ParamFactura paramFactura = new ParamFactura();
            paramFactura.setBusqueda("producto");
            final HashMap<String, ParamFactura> map = new HashMap<String, ParamFactura>();
            map.put("valor", paramFactura);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/venta/buscarproducto.zul", null, map);
            window.doModal();
            productoBuscado = servicioProducto.findByProdCodigo(codigoBusqueda);
            if (productoBuscado == null) {
                return;
            }
//verifica el kardex
            if (parametrizar.getParActivaKardex() && productoBuscado.getProdEsproducto()) {
                Kardex kardex = servicioKardex.FindALlKardexs(productoBuscado);
                if (kardex.getKarTotal().intValue() < 1) {
                    Clients.showNotification("Verifique el stock del producto cuenta con " + kardex.getKarTotal().intValue() + " en estock",
                                Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
                    agregarRegistroVacio();
                    return;
                }
            }

            if (productoBuscado != null) {
                valor.setCantidad(BigDecimal.ONE);
                valor.setProducto(productoBuscado);
                valor.setDescripcion(productoBuscado.getProdNombre());
                valor.setDetPordescuento(DESCUENTOGENERAL);
                valor.setCodigo(productoBuscado.getProdCodigo());
                valor.setEsProducto(productoBuscado.getProdEsproducto());

                BigDecimal costVentaTipoCliente = BigDecimal.ZERO;
                BigDecimal costVentaTipoClienteInicial = BigDecimal.ZERO;
                String tipoVenta = "NORMAL";
                if (clienteBuscado.getClietipo() == 0) {
                    tipoVenta = "NORMAL";
                    if (clietipo.equals("0")) {
                        costVentaTipoClienteInicial = productoBuscado.getPordCostoVentaFinal();
                        costVentaTipoCliente = productoBuscado.getPordCostoVentaFinal();
                    } else if (clietipo.equals("1")) {
                        tipoVenta = "PREFERENCIAL 1";
                        costVentaTipoClienteInicial = productoBuscado.getProdCostoPreferencial();
                        costVentaTipoCliente = productoBuscado.getProdCostoPreferencial();
                    } else if (clietipo.equals("2")) {
                        tipoVenta = "PREFERENCIAL 2";
                        costVentaTipoClienteInicial = productoBuscado.getProdCostoPreferencialDos();
                        costVentaTipoCliente = productoBuscado.getProdCostoPreferencialDos();
                    }
                    /*OBTIENE LOS VALORES LUEGO DE LA BUSQUEDA*/
                    //        BigDecimal factorIva = (parametrizar.getParIva().divide(BigDecimal.valueOf(100.0)));
                    BigDecimal factorIva = (valor.getProducto().getProdIva().divide(BigDecimal.valueOf(100.0)));
                    BigDecimal factorSacarSubtotal = (factorIva.add(BigDecimal.ONE));

                    valor.setTotalInicial(costVentaTipoClienteInicial);
                    BigDecimal porcentajeDesc = valor.getDetPordescuento().divide(BigDecimal.valueOf(100.0), 4, RoundingMode.FLOOR);
                    BigDecimal valorDescuentoIva = costVentaTipoCliente.multiply(porcentajeDesc);
                    //valor unitario con descuento ioncluido iva
                    BigDecimal valorTotalIvaDesc = costVentaTipoCliente.subtract(valorDescuentoIva);
                    //valor unit sin iva sin descuento
                    BigDecimal subTotal = costVentaTipoCliente.divide(factorSacarSubtotal, 4, RoundingMode.FLOOR);
                    valor.setSubTotal(subTotal);
                    //valor unitario sin iva con descuento
                    BigDecimal subTotalDescuento = valorTotalIvaDesc.divide(factorSacarSubtotal, 4, RoundingMode.FLOOR);
                    valor.setSubTotalDescuento(subTotalDescuento);
                    //valor del descuento
                    BigDecimal valorDescuento = valor.getSubTotal().subtract(valor.getSubTotalDescuento());
                    valor.setDetValdescuento(valorDescuento);
                    BigDecimal valorIva = subTotal.multiply(factorIva).multiply(valor.getCantidad());
//                valor.setDetIva(valorIva);
                    //valor del iva con descuento
                    BigDecimal valorIvaDesc = subTotalDescuento.multiply(factorIva).multiply(valor.getCantidad());
                    valor.setDetIva(valorIvaDesc);
                    //valor total sin decuento y con iva
                    valor.setTotal(costVentaTipoCliente);
                    //valor total con decuento y con iva
                    valor.setDetTotaldescuento(valorTotalIvaDesc);
                    valor.setDetTotalconiva(valor.getCantidad().multiply(costVentaTipoCliente));
                    valor.setDetTotalconivadescuento(valor.getCantidad().multiply(valorTotalIvaDesc));
                    valor.setDetCantpordescuento(valorDescuento.multiply(valor.getCantidad()));
                    //cantidad por subtotal con descuento
                    valor.setDetSubtotaldescuentoporcantidad(subTotalDescuento.multiply(valor.getCantidad()));
                    valor.setTipoVenta("NORMAL");
                    valor.setCodTipoVenta(clietipo);
                }

                //ingresa un registro vacio
                boolean registroVacio = true;
                List<DetalleFacturaDAO> listaPedidoPost = listaDetalleFacturaDAOMOdel.getInnerList();

                for (DetalleFacturaDAO item : listaPedidoPost) {
                    if (item.getProducto() == null) {
                        registroVacio = false;
                        break;
                    }
                }

                System.out.println("existe un vacio " + registroVacio);
                if (registroVacio) {
                    DetalleFacturaDAO nuevoRegistroPost = new DetalleFacturaDAO();
                    nuevoRegistroPost.setProducto(productoBuscado);
                    nuevoRegistroPost.setCantidad(BigDecimal.ZERO);
                    nuevoRegistroPost.setSubTotal(BigDecimal.ZERO);
                    nuevoRegistroPost.setDetIva(BigDecimal.ZERO);
                    nuevoRegistroPost.setDetTotalconiva(BigDecimal.ZERO);
                    nuevoRegistroPost.setDescripcion("");
                    nuevoRegistroPost.setProducto(null);
                    ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).add(nuevoRegistroPost);
                }
            }

            calcularValoresTotales();
            codigoBusqueda = "";
        } else {
            Messagebox.show("Verifique el cliente", "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Command
    @NotifyChange({"listaDetalleFacturaDAOMOdel", "subTotalCotizacion", "ivaCotizacion", "valorTotalCotizacion", "totalDescuento", "valorTotalInicialVent", "descuentoValorFinal", "subTotalBaseCero"})
    public void actualizarCostoVenta() {

        BigDecimal factorIva = (parametrizar.getParIva().divide(BigDecimal.valueOf(100.0)));
        BigDecimal factorSacarSubtotal = (factorIva.add(BigDecimal.ONE));
        List<DetalleFacturaDAO> listaPedido = listaDetalleFacturaDAOMOdel.getInnerList();
        for (DetalleFacturaDAO valor : listaPedido) {

            Producto buscadoPorCodigo = valor.getProducto();

            if (buscadoPorCodigo != null) {
//                valor.setCantidad(BigDecimal.ONE);
                valor.setProducto(buscadoPorCodigo);
                valor.setDescripcion(buscadoPorCodigo.getProdNombre());
                valor.setDetPordescuento(DESCUENTOGENERAL);
                valor.setCodigo(buscadoPorCodigo.getProdCodigo());
                // valor.setCantidad(valor.getCantidad());
                BigDecimal costVentaTipoCliente = BigDecimal.ZERO;
                BigDecimal costVentaTipoClienteInicial = BigDecimal.ZERO;
                String tipoVenta = "NORMAL";
                if (clienteBuscado.getClietipo() == 0) {
                    tipoVenta = "NORMAL";
                    if (clietipo.equals("0")) {
                        costVentaTipoClienteInicial = buscadoPorCodigo.getPordCostoVentaFinal();
                        costVentaTipoCliente = buscadoPorCodigo.getPordCostoVentaFinal();
                    } else if (clietipo.equals("1")) {
                        tipoVenta = "PREFERENCIAL 1";
                        costVentaTipoClienteInicial = buscadoPorCodigo.getProdCostoPreferencial();
                        costVentaTipoCliente = buscadoPorCodigo.getProdCostoPreferencial();
                    } else if (clietipo.equals("2")) {
                        tipoVenta = "PREFERENCIAL 2";
                        costVentaTipoClienteInicial = buscadoPorCodigo.getProdCostoPreferencialDos();
                        costVentaTipoCliente = buscadoPorCodigo.getProdCostoPreferencialDos();
                    }

                    valor.setTotalInicial(costVentaTipoClienteInicial);
                    BigDecimal porcentajeDesc = valor.getDetPordescuento().divide(BigDecimal.valueOf(100.0), 4, RoundingMode.FLOOR);
                    BigDecimal valorDescuentoIva = costVentaTipoCliente.multiply(porcentajeDesc);
                    //valor unitario con descuento ioncluido iva
                    BigDecimal valorTotalIvaDesc = costVentaTipoCliente.subtract(valorDescuentoIva);
                    //valor unit sin iva sin descuento
                    BigDecimal subTotal = costVentaTipoCliente.divide(factorSacarSubtotal, 4, RoundingMode.FLOOR);
                    valor.setSubTotal(subTotal);
                    //valor unitario sin iva con descuento
                    BigDecimal subTotalDescuento = valorTotalIvaDesc.divide(factorSacarSubtotal, 4, RoundingMode.FLOOR);
                    valor.setSubTotalDescuento(subTotalDescuento);
                    //valor del descuento
                    BigDecimal valorDescuento = valor.getSubTotal().subtract(valor.getSubTotalDescuento());
                    valor.setDetValdescuento(valorDescuento);
                    BigDecimal valorIva = subTotal.multiply(factorIva).multiply(valor.getCantidad());
//                valor.setDetIva(valorIva);
                    //valor del iva con descuento
                    BigDecimal valorIvaDesc = subTotalDescuento.multiply(factorIva).multiply(valor.getCantidad());
                    valor.setDetIva(valorIvaDesc);
                    //valor total sin decuento y con iva
                    valor.setTotal(costVentaTipoCliente);
                    //valor total con decuento y con iva
                    valor.setDetTotaldescuento(valorTotalIvaDesc);
                    valor.setDetTotalconiva(valor.getCantidad().multiply(costVentaTipoCliente));
                    valor.setDetTotalconivadescuento(valor.getCantidad().multiply(valorTotalIvaDesc));
                    valor.setDetCantpordescuento(valorDescuento.multiply(valor.getCantidad()));
                    //cantidad por subtotal con descuento
                    valor.setDetSubtotaldescuentoporcantidad(subTotalDescuento.multiply(valor.getCantidad()));
                    valor.setTipoVenta("NORMAL");
                    valor.setCodTipoVenta(clietipo);
                }

            }
        }
        //ingresa un registro vacio
        boolean registroVacio = true;
        List<DetalleFacturaDAO> listaPedidoPost = listaDetalleFacturaDAOMOdel.getInnerList();

        for (DetalleFacturaDAO item : listaPedidoPost) {
            if (item.getProducto() == null) {
                registroVacio = false;
                break;
            }
        }

        System.out.println("existe un vacio " + registroVacio);
        if (registroVacio) {
            DetalleFacturaDAO nuevoRegistroPost = new DetalleFacturaDAO();
            nuevoRegistroPost.setProducto(productoBuscado);
            nuevoRegistroPost.setCantidad(BigDecimal.ZERO);
            nuevoRegistroPost.setSubTotal(BigDecimal.ZERO);
            nuevoRegistroPost.setDetIva(BigDecimal.ZERO);
            nuevoRegistroPost.setDetTotalconiva(BigDecimal.ZERO);
            nuevoRegistroPost.setDescripcion("");
            nuevoRegistroPost.setProducto(null);
            ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).add(nuevoRegistroPost);
        }
        calcularValoresTotales();
    }

    @Command
    @NotifyChange({"listaDetalleFacturaDAOMOdel", "subTotalCotizacion", "ivaCotizacion", "valorTotalCotizacion", "totalDescuento", "valorTotalInicialVent", "descuentoValorFinal", "subTotalBaseCero"})
    public void buscarPorCodigo(@BindingParam("valor") DetalleFacturaDAO valor) {
        if (parametrizar.getParNumRegistrosFactura().intValue() <= listaDetalleFacturaDAOMOdel.size()) {
            Clients.showNotification("Numero de registros permitidos, imprima y genere otra factura", Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
            return;
        }

//        BigDecimal factorIva = (parametrizar.getParIva().divide(BigDecimal.valueOf(100.0)));
        Producto buscadoPorCodigo = servicioProducto.findByProdCodigo(valor.getCodigo());

        if (buscadoPorCodigo == null) {
            valor.setCodigo("");
            Clients.showNotification("No existe el producto",
                        Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 2000, true);
            return;
        }

        BigDecimal factorIva = (buscadoPorCodigo.getProdIva().divide(BigDecimal.valueOf(100.0)));

        BigDecimal factorSacarSubtotal = (factorIva.add(BigDecimal.ONE));

//verifica el kardex
        if (parametrizar.getParActivaKardex()) {
            Kardex kardex = servicioKardex.FindALlKardexs(productoBuscado);
            if (kardex.getKarTotal().intValue() < 1) {
                Clients.showNotification("Verifique el stock del producto cuenta con " + kardex.getKarTotal().intValue() + " en estock",
                            Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
                agregarRegistroVacio();
                return;
            }
        }

        if (buscadoPorCodigo != null) {
            valor.setCantidad(BigDecimal.ONE);
            valor.setProducto(buscadoPorCodigo);
            valor.setDescripcion(buscadoPorCodigo.getProdNombre());
            valor.setDetPordescuento(DESCUENTOGENERAL);
            valor.setCodigo(buscadoPorCodigo.getProdCodigo());

            BigDecimal costVentaTipoCliente = BigDecimal.ZERO;
            BigDecimal costVentaTipoClienteInicial = BigDecimal.ZERO;
            String tipoVenta = "NORMAL";

            if (clienteBuscado.getClietipo() == 0) {
                tipoVenta = "NORMAL";
                if (clietipo.equals("0")) {
                    costVentaTipoClienteInicial = buscadoPorCodigo.getPordCostoVentaFinal();
                    costVentaTipoCliente = buscadoPorCodigo.getPordCostoVentaFinal();
                } else if (clietipo.equals("1")) {
                    tipoVenta = "PREFERENCIAL 1";
                    costVentaTipoClienteInicial = buscadoPorCodigo.getProdCostoPreferencial();
                    costVentaTipoCliente = buscadoPorCodigo.getProdCostoPreferencial();
                } else if (clietipo.equals("2")) {
                    tipoVenta = "PREFERENCIAL 2";
                    costVentaTipoClienteInicial = buscadoPorCodigo.getProdCostoPreferencialDos();
                    costVentaTipoCliente = buscadoPorCodigo.getProdCostoPreferencialDos();
                }

                valor.setTotalInicial(costVentaTipoClienteInicial);
                BigDecimal porcentajeDesc = valor.getDetPordescuento().divide(BigDecimal.valueOf(100.0), 4, RoundingMode.FLOOR);
                BigDecimal valorDescuentoIva = costVentaTipoCliente.multiply(porcentajeDesc);
                //valor unitario con descuento ioncluido iva
                BigDecimal valorTotalIvaDesc = costVentaTipoCliente.subtract(valorDescuentoIva);
                //valor unit sin iva sin descuento
                BigDecimal subTotal = costVentaTipoCliente.divide(factorSacarSubtotal, 4, RoundingMode.FLOOR);
                valor.setSubTotal(subTotal);
                //valor unitario sin iva con descuento
                BigDecimal subTotalDescuento = valorTotalIvaDesc.divide(factorSacarSubtotal, 4, RoundingMode.FLOOR);
                valor.setSubTotalDescuento(subTotalDescuento);
                //valor del descuento
                BigDecimal valorDescuento = valor.getSubTotal().subtract(valor.getSubTotalDescuento());
                valor.setDetValdescuento(valorDescuento);
                BigDecimal valorIva = subTotal.multiply(factorIva).multiply(valor.getCantidad());
//                valor.setDetIva(valorIva);
                //valor del iva con descuento
                BigDecimal valorIvaDesc = subTotalDescuento.multiply(factorIva).multiply(valor.getCantidad());
                valor.setDetIva(valorIvaDesc);
                //valor total sin decuento y con iva
                valor.setTotal(costVentaTipoCliente);
                //valor total con decuento y con iva
                valor.setDetTotaldescuento(valorTotalIvaDesc);
                valor.setDetTotalconiva(valor.getCantidad().multiply(costVentaTipoCliente));
                valor.setDetTotalconivadescuento(valor.getCantidad().multiply(valorTotalIvaDesc));
                valor.setDetCantpordescuento(valorDescuento.multiply(valor.getCantidad()));
                //cantidad por subtotal con descuento
                valor.setDetSubtotaldescuentoporcantidad(subTotalDescuento.multiply(valor.getCantidad()));
                valor.setTipoVenta("NORMAL");
                valor.setCodTipoVenta(clietipo);
            }
            //nuevoRegistro.setSubTotal(productoBuscado.getPordCostoVentaFinal());
//            ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).add(valor);

            //ingresa un registro vacio
            boolean registroVacio = true;
            List<DetalleFacturaDAO> listaPedidoPost = listaDetalleFacturaDAOMOdel.getInnerList();

            for (DetalleFacturaDAO item : listaPedidoPost) {
                if (item.getProducto() == null) {
                    registroVacio = false;
                    break;
                }
            }
            /*
            System.out.println("existe un vacio " + registroVacio);
            if (registroVacio) {
                DetalleFacturaDAO nuevoRegistroPost = new DetalleFacturaDAO();
                nuevoRegistroPost.setProducto(null);
                nuevoRegistroPost.setCantidad(BigDecimal.ZERO);
                nuevoRegistroPost.setSubTotal(BigDecimal.ZERO);
                nuevoRegistroPost.setDetIva(BigDecimal.ZERO);
                nuevoRegistroPost.setDetTotalconiva(BigDecimal.ZERO);
                nuevoRegistroPost.setDescripcion("");
                ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).add(nuevoRegistroPost);
            }
             */
        }
        calcularValoresTotales();
    }

    @Command
    @NotifyChange({"listaDetalleFacturaDAOMOdel", "subTotalCotizacion", "ivaCotizacion", "valorTotalCotizacion", "totalDescuento", "valorTotalInicialVent", "descuentoValorFinal", "subTotalBaseCero"})
    public void calcularValores(@BindingParam("valor") DetalleFacturaDAO valor) {
        try {
            BigDecimal factorIva = (parametrizar.getParIva().divide(BigDecimal.valueOf(100.0)));
            BigDecimal factorSacarSubtotal = (factorIva.add(BigDecimal.ONE));
//            Kardex kardex = servicioKardex.FindALlKardexs(valor.getProducto());
//            if (kardex.getKarTotal().intValue() < valor.getCantidad().intValue()) {
//                valor.setCantidad(kardex.getKarTotal());
//                Clients.showNotification("Verifique el stock del producto cuenta con " + kardex.getKarTotal().intValue() + " en estock",
//                        Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
//
//            }

            if (valor.getCantidad().intValue() > 0) {
                BigDecimal porcentajeDesc = valor.getDetPordescuento().divide(BigDecimal.valueOf(100.0), 7, RoundingMode.FLOOR);
                BigDecimal valorDescuentoIva = valor.getTotal().multiply(porcentajeDesc);

                BigDecimal valorIva = valor.getSubTotalDescuento().multiply(factorIva).multiply(valor.getCantidad());
//                valor.setDetIva(valorIva);
                //valor unitario con descuento ioncluido iva
                BigDecimal valorTotalIvaDesc = valor.getTotal().subtract(valorDescuentoIva);

                //valor unitario sin iva con descuento
                BigDecimal subTotalDescuento = valorTotalIvaDesc.divide(factorSacarSubtotal, 7, RoundingMode.FLOOR);
                valor.setSubTotalDescuento(subTotalDescuento);
                //valor del descuento
                BigDecimal valorDescuento = valor.getSubTotal().subtract(valor.getSubTotalDescuento());
                valor.setDetValdescuento(valorDescuento);
                //valor del iva con descuento
                BigDecimal valorIvaDesc = subTotalDescuento.multiply(factorIva).multiply(valor.getCantidad());

                valor.setDetIva(valorIvaDesc);

                //valor total con decuento y con iva
                valor.setDetTotaldescuento(valorTotalIvaDesc);
                //cantidad por subtotal con descuento
                valor.setDetSubtotaldescuentoporcantidad(subTotalDescuento.multiply(valor.getCantidad()));
                valor.setDetTotalconivadescuento(valor.getCantidad().multiply(valorTotalIvaDesc));
                valor.setDetTotalconiva(valor.getCantidad().multiply(valor.getTotal()));
                valor.setDetCantpordescuento(valorDescuento.multiply(valor.getCantidad()));

            }
            calcularValoresTotales();
        } catch (Exception e) {
            Messagebox.show("Ocurrio un error al calcular los valores" + e, "Atención", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"listaDetalleFacturaDAOMOdel", "subTotalCotizacion", "ivaCotizacion", "valorTotalCotizacion", "totalDescuento", "valorTotalInicialVent", "descuentoValorFinal", "subTotalBaseCero"})
    public void calcularValoresDesCantidad(@BindingParam("valor") DetalleFacturaDAO valor) {
        try {

            if (valor.getCantidad() == null) {
                return;
            }

            if (valor.getCantidad().doubleValue() <= 0) {
                return;
            }

            if (valor.getProducto() == null) {
                return;
            }
            /*SERVICOS */
            if (!valor.getEsProducto()) {

                valor.setTotalInicial(valor.getTotal());
            }
            BigDecimal factorIva = (valor.getProducto().getProdIva().divide(BigDecimal.valueOf(100.0)));
            BigDecimal factorSacarSubtotal = (factorIva.add(BigDecimal.ONE));

            if (valor.getCantidad().doubleValue() > 0) {
                /*CALCULO DEL PORCENTAJE DE DESCUENTO*/
                BigDecimal porcentajeDesc = BigDecimal.ZERO;
                BigDecimal valorPorcentaje = BigDecimal.ZERO;
                BigDecimal valorDescuentoIva = BigDecimal.ZERO;
                if (valor.getEsProducto()) {
                    porcentajeDesc = valor.getTotal().multiply(BigDecimal.valueOf(100.0));
                    valorPorcentaje = porcentajeDesc.divide(valor.getTotalInicial(), 7, RoundingMode.FLOOR);
                    valorDescuentoIva = valor.getTotalInicial().subtract(valor.getTotal());
                }

                /*COLOCAMOS EN EL CAMPO DE DESCUENTO*/
                BigDecimal porcentajeDiferencia = BigDecimal.valueOf(100.0).subtract(valorPorcentaje).setScale(5, RoundingMode.FLOOR);
                valor.setDetPordescuento(porcentajeDiferencia);
                //valor unitario con descuento ioncluido iva
                BigDecimal valorTotalIvaDesc = valor.getTotalInicial().subtract(valorDescuentoIva);

                //valor unitario sin iva con descuento
                BigDecimal subTotalDescuento = valorTotalIvaDesc.divide(factorSacarSubtotal, 7, RoundingMode.FLOOR);

                valor.setSubTotalDescuento(subTotalDescuento);
                //valor del descuento
                BigDecimal valorDescuento = BigDecimal.ZERO;
                if (!valor.getEsProducto()) {
                    valor.setSubTotal(valor.getSubTotalDescuento());
                }
                if (valor.getEsProducto()) {
                    valorDescuento = ArchivoUtils.redondearDecimales(valor.getSubTotal(), 5).subtract(ArchivoUtils.redondearDecimales(valor.getSubTotalDescuento(), 5));
                }
                valor.setDetValdescuento(valorDescuento);
                //valor del iva con descuento
                BigDecimal valorIvaDesc = subTotalDescuento.multiply(factorIva).multiply(valor.getCantidad());

                valor.setDetIva(valorIvaDesc);

                //valor total con decuento y con iva
                valor.setDetTotaldescuento(valorDescuento.multiply(valor.getCantidad()));
                //cantidad por subtotal con descuento
                valor.setDetSubtotaldescuentoporcantidad(subTotalDescuento.multiply(valor.getCantidad()));
                valor.setDetTotalconivadescuento(valor.getCantidad().multiply(valorTotalIvaDesc));
                valor.setDetTotalconiva(valor.getCantidad().multiply(valor.getTotal()));

                valor.setDetCantpordescuento(valorDescuento.multiply(valor.getCantidad()));

            }
            calcularValoresTotales();
            //ingresa un registro vacio
            boolean registroVacio = true;
            List<DetalleFacturaDAO> listaPedidoPost = listaDetalleFacturaDAOMOdel.getInnerList();

            for (DetalleFacturaDAO item : listaPedidoPost) {
                if (item.getProducto() == null) {
                    registroVacio = false;
                    break;
                }
            }

            System.out.println("existe un vacio " + registroVacio);
            if (registroVacio) {
                DetalleFacturaDAO nuevoRegistroPost = new DetalleFacturaDAO();
                nuevoRegistroPost.setProducto(null);
                nuevoRegistroPost.setCantidad(BigDecimal.ZERO);
                nuevoRegistroPost.setSubTotal(BigDecimal.ZERO);
                nuevoRegistroPost.setDetIva(BigDecimal.ZERO);
                nuevoRegistroPost.setDetTotalconiva(BigDecimal.ZERO);
                nuevoRegistroPost.setDescripcion("");
                ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).add(nuevoRegistroPost);
            }

        } catch (Exception e) {
            Messagebox.show("Ocurrio un error al calcular los valores" + e, "Atención", Messagebox.OK, Messagebox.ERROR);
        }
    }

    /*CALCULAR EL DESCUENTO EN FUNCION DEL PORCENTAJE*/
    @Command
    @NotifyChange({"listaDetalleFacturaDAOMOdel", "subTotalCotizacion", "ivaCotizacion", "valorTotalCotizacion", "totalDescuento", "valorTotalInicialVent", "descuentoValorFinal", "subTotalBaseCero"})
    public void calcularValoresDesCantidadPorPorcentaje(@BindingParam("valor") DetalleFacturaDAO valor) {
        try {
            if (valor.getProducto() == null) {
                return;
            }

            BigDecimal factorIva = (valor.getProducto().getProdIva().divide(BigDecimal.valueOf(100.0)));
            BigDecimal factorSacarSubtotal = (factorIva.add(BigDecimal.ONE));

//            Kardex kardex = servicioKardex.FindALlKardexs(valor.getProducto());
//            if (kardex.getKarTotal().intValue() < valor.getCantidad().intValue()) {
//                valor.setCantidad(kardex.getKarTotal());
//                Clients.showNotification("Verifique el stock del producto cuenta con " + kardex.getKarTotal().intValue() + " en estock",
//                        Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
//
//            }
            if (valor.getCantidad().intValue() > 0) {
                BigDecimal porcentajeDesc = valor.getDetPordescuento().divide(BigDecimal.valueOf(100.0), 5, RoundingMode.FLOOR);
//                BigDecimal valorDescuentoIva = valor.getTotalInicial().subtract(valor.getTotal());
                BigDecimal valorDescuentoIva = valor.getTotalInicial().multiply(porcentajeDesc);
                //valor unitario con descuento ioncluido iva
                BigDecimal valorTotalIvaDesc = valor.getTotalInicial().subtract(valorDescuentoIva);
                /*VALOR DEL DETALLE MENOS EL DESCUENTO*/
                valor.setTotal((valor.getTotalInicial().subtract(valorDescuentoIva)).setScale(5, RoundingMode.FLOOR));
                //valor unitario sin iva con descuento
                BigDecimal subTotalDescuento = valorTotalIvaDesc.divide(factorSacarSubtotal, 5, RoundingMode.FLOOR);
                valor.setSubTotalDescuento(subTotalDescuento);
                //valor del descuento
                BigDecimal valorDescuento = valor.getSubTotal().subtract(valor.getSubTotalDescuento());
                valor.setDetValdescuento(valorDescuento);

                //valor del iva con descuento
                BigDecimal valorIvaDesc = subTotalDescuento.multiply(factorIva).multiply(valor.getCantidad());

                valor.setDetIva(valorIvaDesc);

                //valor total con decuento y con iva
                valor.setDetTotaldescuento(valorDescuento.multiply(valor.getCantidad()));
                //cantidad por subtotal con descuento
                valor.setDetSubtotaldescuentoporcantidad(subTotalDescuento.multiply(valor.getCantidad()));
                valor.setDetTotalconivadescuento(valor.getCantidad().multiply(valorTotalIvaDesc));
                valor.setDetTotalconiva(valor.getCantidad().multiply(valor.getTotal()));

                valor.setDetCantpordescuento(valorDescuento.multiply(valor.getCantidad()));

            }
            calcularValoresTotales();
        } catch (Exception e) {
            Messagebox.show("Ocurrio un error al calcular los valores" + e, "Atención", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"numeroFactura", "numeroProforma", "clienteBuscado"})
    public void verificarNumeracion() {
        verificarSecNumeracion();
    }

    private void getDetallefactura() {
        setListaDetalleFacturaDAOMOdel(new ListModelList<DetalleFacturaDAO>(getListaDetalleFacturaDAODatos()));
        ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).setMultiple(true);
    }

    @Command
    public void seleccionarRegistros() {
        registrosSeleccionados = ((ListModelList<DetalleFacturaDAO>) getListaDetalleFacturaDAOMOdel()).getSelection();
    }

    public List<Producto> getListaProducto() {
        return listaProducto;
    }

    public void setListaProducto(List<Producto> listaProducto) {
        this.listaProducto = listaProducto;
    }

    public String getBuscarNombreProd() {
        return buscarNombreProd;
    }

    public void setBuscarNombreProd(String buscarNombreProd) {
        this.buscarNombreProd = buscarNombreProd;
    }

    private void FindClienteLikeNombre() {
        listaClientesAll = servicioCliente.FindClienteLikeNombre(buscarNombre);
    }

    private void FindClienteLikeRazon() {
        listaClientesAll = servicioCliente.FindClienteLikeRazonSocial(buscarRazonSocial);
    }

    private void FindClienteLikeCedula() {
        listaClientesAll = servicioCliente.FindClienteLikeCedula(buscarCedula);
    }

    public Cliente getClienteBuscado() {
        return clienteBuscado;
    }

    public void setClienteBuscado(Cliente clienteBuscado) {
        this.clienteBuscado = clienteBuscado;
    }

    public String getBuscarNombre() {
        return buscarNombre;
    }

    public void setBuscarNombre(String buscarNombre) {
        this.buscarNombre = buscarNombre;
    }

    public String getBuscarRazonSocial() {
        return buscarRazonSocial;
    }

    public void setBuscarRazonSocial(String buscarRazonSocial) {
        this.buscarRazonSocial = buscarRazonSocial;
    }

    public String getBuscarCedula() {
        return buscarCedula;
    }

    public void setBuscarCedula(String buscarCedula) {
        this.buscarCedula = buscarCedula;
    }

    public static String getBuscarCliente() {
        return buscarCliente;
    }

    public static void setBuscarCliente(String buscarCliente) {
        Facturar.buscarCliente = buscarCliente;
    }

    public List<Cliente> getListaClientesAll() {
        return listaClientesAll;
    }

    public void setListaClientesAll(List<Cliente> listaClientesAll) {
        this.listaClientesAll = listaClientesAll;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public DetalleFacturaDAO getDetalleFacturaDAO() {
        return detalleFacturaDAO;
    }

    public void setDetalleFacturaDAO(DetalleFacturaDAO detalleFacturaDAO) {
        this.detalleFacturaDAO = detalleFacturaDAO;
    }

    public ListModelList<DetalleFacturaDAO> getListaDetalleFacturaDAOMOdel() {
        return listaDetalleFacturaDAOMOdel;
    }

    public void setListaDetalleFacturaDAOMOdel(ListModelList<DetalleFacturaDAO> listaDetalleFacturaDAOMOdel) {
        this.listaDetalleFacturaDAOMOdel = listaDetalleFacturaDAOMOdel;
    }

    public List<DetalleFacturaDAO> getListaDetalleFacturaDAODatos() {
        return listaDetalleFacturaDAODatos;
    }

    public void setListaDetalleFacturaDAODatos(List<DetalleFacturaDAO> listaDetalleFacturaDAODatos) {
        this.listaDetalleFacturaDAODatos = listaDetalleFacturaDAODatos;
    }

    public Set<DetalleFacturaDAO> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    public void setRegistrosSeleccionados(Set<DetalleFacturaDAO> registrosSeleccionados) {
        this.registrosSeleccionados = registrosSeleccionados;
    }

    public Producto getProductoBuscado() {
        return productoBuscado;
    }

    public void setProductoBuscado(Producto productoBuscado) {
        this.productoBuscado = productoBuscado;
    }

    public static String getCodigoBusqueda() {
        return codigoBusqueda;
    }

    public static void setCodigoBusqueda(String codigoBusqueda) {
        Facturar.codigoBusqueda = codigoBusqueda;
    }

    public String getBuscarCodigoProd() {
        return buscarCodigoProd;
    }

    public void setBuscarCodigoProd(String buscarCodigoProd) {
        this.buscarCodigoProd = buscarCodigoProd;
    }

    public BigDecimal getValorTotalCotizacion() {
        return valorTotalCotizacion;
    }

    public void setValorTotalCotizacion(BigDecimal valorTotalCotizacion) {
        this.valorTotalCotizacion = valorTotalCotizacion;
    }

    public BigDecimal getSubTotalCotizacion() {
        return subTotalCotizacion;
    }

    public void setSubTotalCotizacion(BigDecimal subTotalCotizacion) {
        this.subTotalCotizacion = subTotalCotizacion;
    }

    public BigDecimal getIvaCotizacion() {
        return ivaCotizacion;
    }

    public void setIvaCotizacion(BigDecimal ivaCotizacion) {
        this.ivaCotizacion = ivaCotizacion;
    }

    public String getEstdoFactura() {
        return estdoFactura;
    }

    public void setEstdoFactura(String estdoFactura) {
        this.estdoFactura = estdoFactura;
    }

    public UserCredential getCredential() {
        return credential;
    }

    public void setCredential(UserCredential credential) {
        this.credential = credential;
    }

    public Integer getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(Integer numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Date getFechafacturacion() {
        return fechafacturacion;
    }

    public void setFechafacturacion(Date fechafacturacion) {
        this.fechafacturacion = fechafacturacion;
    }

    public AMedia getFileContent() {
        return fileContent;
    }

    public void setFileContent(AMedia fileContent) {
        this.fileContent = fileContent;
    }

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public BigDecimal getCobro() {
        return cobro;
    }

    public void setCobro(BigDecimal cobro) {
        this.cobro = cobro;
    }

    public BigDecimal getCambio() {
        return cambio;
    }

    public void setCambio(BigDecimal cambio) {
        this.cambio = cambio;
    }

    public String getFacturaDescripcion() {
        return facturaDescripcion;
    }

    public void setFacturaDescripcion(String facturaDescripcion) {
        this.facturaDescripcion = facturaDescripcion;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    @Command
    @NotifyChange({"listaClientesAll", "clienteBuscado", "fechaEmision", "saldoFacturas", "llegada"})
    public void buscarClienteEnLista() {
        ParamFactura paramFactura = new ParamFactura();
        paramFactura.setBusqueda("cliente");
        final HashMap<String, ParamFactura> map = new HashMap<String, ParamFactura>();
        map.put("valor", paramFactura);
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/venta/buscarcliente.zul", null, map);
        window.doModal();
        System.out.println("clinete de la lsitas buscarCliente " + buscarCliente);
        clienteBuscado = servicioCliente.FindClienteForCedula(buscarCliente);
        if (clienteBuscado == null) {
            clienteBuscado = servicioCliente.findClienteLikeCedula("999999999");
        }
        List<Factura> listaFacturasPendientes = servicioFactura.findEstadoCliente("PE", clienteBuscado);
//        saldoFacturas = BigDecimal.ZERO;
//        BigDecimal sumaPendientes = BigDecimal.ZERO;
//        for (Factura listaFacturasPendiente : listaFacturasPendientes) {
//
//            sumaPendientes = sumaPendientes.add(listaFacturasPendiente.getFacSaldoAmortizado());
//        }
//        if (clienteBuscado != null) {
//            saldoFacturas = clienteBuscado.getCliMontoAsignado().subtract(sumaPendientes);
//            saldoFacturas.setScale(2, RoundingMode.FLOOR);
//        }
        if (clienteBuscado != null) {
            llegada = clienteBuscado.getCliDireccion();
        }

    }

    @Command
    @NotifyChange({"clienteBuscado", "fechaEmision", "saldoFacturas", "llegada"})
    public void buscarClienteDni(@BindingParam("valor") Cliente valor) {
        if (valor.getCliCedula() == null) {
            //  Clients.showNotification("Ingrese un valor ", "error", null, "end_before", 3000, true);
            clienteBuscado = servicioCliente.findClienteLikeCedula("999999999");
            return;
        }
        if (valor.getCliCedula().equals("")) {
            //  Clients.showNotification("Ingrese un valor ", "error", null, "end_before", 3000, true);
            clienteBuscado = servicioCliente.findClienteLikeCedula("999999999");
            return;
        }

        clienteBuscado = servicioCliente.FindClienteForCedula(valor.getCliCedula());
        if (clienteBuscado == null) {
            clienteBuscado = servicioCliente.findClienteLikeCedula("999999999");
        }

    }

    @NotifyChange({"listaDetalleFacturaDAOMOdel", "subTotalCotizacion", "ivaCotizacion", "valorTotalCotizacion", "totalDescuento", "buscarNombreProd", "valorTotalInicialVent", "descuentoValorFinal", "subTotalBaseCero"})
    private void verNotasEntrega() {
//        ParamFactura paramFactura = new ParamFactura();
//        paramFactura.setBusqueda("nte");
//        paramFactura.setCedula(buscarCliente);
//        final HashMap<String, ParamFactura> map = new HashMap<String, ParamFactura>();
//        map.put("valor", paramFactura);
//        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
//                    "/venta/listanotaentrega.zul", null, map);
//        window.doModal();
//        buscarCliente = paramFactura.getCedula();
        listaDetalleFacturaDAODatos.clear();
        if (seleccionNotaEntrega != null) {
            for (Factura fac : seleccionNotaEntrega) {
                System.out.println("FAct " + fac.getIdFactura());

                List<DetalleFactura> detalleFac = servicioDetalleFactura.findDetalleForIdFac(fac.getIdFactura());
                DetalleFacturaDAO nuevoRegistro;

                for (DetalleFactura det : detalleFac) {
                    nuevoRegistro = new DetalleFacturaDAO();
                    nuevoRegistro.setCodigo(det.getIdProducto().getProdCodigo());
                    nuevoRegistro.setCantidad(det.getDetCantidad());
                    nuevoRegistro.setProducto(det.getIdProducto());
                    nuevoRegistro.setDescripcion(det.getDetDescripcion());
                    nuevoRegistro.setSubTotal(det.getDetSubtotal());
                    nuevoRegistro.setTotal(det.getDetTotal());
                    nuevoRegistro.setDetIva(det.getDetIva());
                    nuevoRegistro.setDetTotalconiva(det.getDetTotalconiva());
                    nuevoRegistro.setTipoVenta(det.getDetTipoVenta());
                    //valores con descuentos
                    nuevoRegistro.setSubTotalDescuento(det.getDetSubtotaldescuento());
                    nuevoRegistro.setDetTotaldescuento(det.getDetTotaldescuento());
                    nuevoRegistro.setDetPordescuento(det.getDetPordescuento());
                    nuevoRegistro.setDetValdescuento(det.getDetValdescuento());
                    nuevoRegistro.setDetTotalconivadescuento(det.getDetTotaldescuentoiva());
                    nuevoRegistro.setDetCantpordescuento(det.getDetCantpordescuento());
                    nuevoRegistro.setDetIvaDesc(det.getDetIva());
                    nuevoRegistro.setCodTipoVenta(det.getDetCodTipoVenta());
                    nuevoRegistro.setDetSubtotaldescuentoporcantidad(det.getDetSubtotaldescuentoporcantidad());
                    nuevoRegistro.setTotalInicial(det.getDetTotal());
                    clietipo = det.getDetCodTipoVenta();
//            calcularValores(nuevoRegistro);
                    listaDetalleFacturaDAODatos.add(nuevoRegistro);
                }

            }
            getDetallefactura();
            calcularValoresTotales();
            tipoVentaAnterior = "NTE";
        } else {
            listaDetalleFacturaDAODatos.clear();
            getDetallefactura();
            calcularValoresTotales();
        }

    }

    @Command
    public void nuevoCliente() {

        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/nuevo/cliente.zul", null, null);
        window.doModal();
    }

    @Command
    @NotifyChange({"listaClientesAll", "buscarRazonSocial"})
    public void buscarClienteRazon() {

        FindClienteLikeRazon();
    }

    @Command
    @NotifyChange({"listaClientesAll", "buscarNombre"})
    public void buscarClienteNombre() {

        FindClienteLikeNombre();
    }

    @Command
    @NotifyChange({"listaClientesAll", "buscarCedula"})
    public void buscarClienteCedula() {

        FindClienteLikeCedula();
    }

    @Command
    @NotifyChange("clienteBuscado")
    public void seleccionarClienteLista(@BindingParam("cliente") Cliente valor) {
        System.out.println("cliente seleccionado " + valor.getCliCedula());
        buscarCliente = valor.getCliCedula();
        windowClienteBuscar.detach();

    }

    //busqueda del producto
    @Command
    @NotifyChange({"listaDetalleFacturaDAOMOdel", "subTotalCotizacion", "ivaCotizacion", "valorTotalCotizacion", "subTotalBaseCero", "totalItems", "valorTotalInicialVent"})
    public void eliminarRegistros() {
        if (registrosSeleccionados.size() > 0) {
//
//            ParamFactura paramFactura = new ParamFactura();
//            paramFactura.setBusqueda("cliente");
//            final HashMap<String, ParamFactura> map = new HashMap<String, ParamFactura>();
//            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
//                    "/venta/confirmaborrado.zul", null, map);
//            window.doModal();

            if (true) {
                ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).removeAll(registrosSeleccionados);
                calcularValoresTotales();
            } else {
                Clients.showNotification("No tiene permisos para eliminar, verifique el usuario y contraseña",
                            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 5000, true);
            }

        } else {
            Messagebox.show("Seleccione al menos un registro para eliminar", "Atención", Messagebox.OK, Messagebox.ERROR);
        }

    }

    @Command
    public void validarBorrado() {
        Usuario usuRec = servicioUsuario.FindUsuarioPorNombre(usuLoginVal);
        if (usuRec != null) {
            if (usuRec.getUsuNivel() == 1) {
                if (usuRec.getUsuLogin().equals(usuLoginVal) && usuRec.getUsuPassword().equals(usuPasswordVal)) {
                    validaBorrado = Boolean.TRUE;

                } else {
                    validaBorrado = Boolean.FALSE;
                }
            } else {
                validaBorrado = Boolean.FALSE;
            }

        } else {
            validaBorrado = Boolean.FALSE;
        }

        windowValidaBorra.detach();
    }

    @Command
    @NotifyChange({"listaDetalleFacturaDAOMOdel"})
    public void agregarRegistroVacio() {

        DetalleFacturaDAO nuevoRegistro = new DetalleFacturaDAO();
        nuevoRegistro.setProducto(productoBuscado);
        nuevoRegistro.setCantidad(BigDecimal.ZERO);
        nuevoRegistro.setSubTotal(BigDecimal.ZERO);
        nuevoRegistro.setDetIva(BigDecimal.ZERO);
        nuevoRegistro.setDetTotalconiva(BigDecimal.ZERO);
        nuevoRegistro.setDescripcion("");
        nuevoRegistro.setProducto(null);
        ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).add(nuevoRegistro);

    }

    private void calcularValoresTotales() {
        BigDecimal factorIva = (parametrizar.getParIva().divide(BigDecimal.valueOf(100.0)));
        BigDecimal facturIvaMasBase = (factorIva.add(BigDecimal.ONE));
//        BigDecimal descuentoValorFinal = BigDecimal.ZERO;
        BigDecimal valorTotalInicial = BigDecimal.ZERO;
        BigDecimal valorTotal = BigDecimal.ZERO;
        BigDecimal valorTotalConIva = BigDecimal.ZERO;
        BigDecimal valorIva = BigDecimal.ZERO;
        BigDecimal valorDescuento = BigDecimal.ZERO;
        BigDecimal baseCero = BigDecimal.ZERO;
        BigDecimal sumaSubsidio = BigDecimal.ZERO;
        BigDecimal sumaDeItems = BigDecimal.ZERO;

        List<DetalleFacturaDAO> listaPedido = listaDetalleFacturaDAOMOdel.getInnerList();
        if (listaPedido.size() > 0) {
            for (DetalleFacturaDAO item : listaPedido) {
                sumaDeItems = sumaDeItems.add(BigDecimal.ONE);
                if (item.getProducto() != null) {
                    valorTotal = valorTotal.add(item.getProducto().getProdGrabaIva() ? item.getSubTotalDescuento().multiply(item.getCantidad()) : BigDecimal.ZERO);
                    valorIva = valorIva.add(item.getDetIva());
//                    valorTotalConIva = valorTotalConIva.add(item.getDetTotalconivadescuento());
                    valorDescuento = valorDescuento.add(item.getDetCantpordescuento());
                    valorTotalInicial = valorTotalInicial.add(item.getTotalInicial().multiply(item.getCantidad()));
                    baseCero = baseCero.add(!item.getProducto().getProdGrabaIva() ? item.getSubTotalDescuento().multiply(item.getCantidad()) : BigDecimal.ZERO);
                    /*COSTO SIN SUBSIDIO*/

                    if (item.getProducto().getProdTieneSubsidio().equals("S")) {
                        BigDecimal precioSinSubporcantidad = item.getProducto().getProdSubsidio().multiply(item.getCantidad());
                        sumaSubsidio = sumaSubsidio.add(precioSinSubporcantidad.setScale(5, RoundingMode.FLOOR));
                    }

                }
            }

            totalItems = "ITEMS: " + (sumaDeItems.intValue() - 1);
            System.out.println("**********************************************************");
            System.out.println("valor total:::: subTotalCotizacion " + valorTotal);
            //valorTotal.setScale(5, RoundingMode.UP);
            try {
                subsidioTotal = sumaSubsidio;
                subTotalCotizacion = valorTotal;
                // subTotalCotizacion.setScale(5, RoundingMode.UP);
                subTotalBaseCero = baseCero;
                /*Obtiene el porcentaje del IVA*/
//                BigDecimal valorIva = subTotalCotizacion.multiply(parametrizar.getParIva());

                ivaCotizacion = valorIva;
                // ivaCotizacion.setScale(5, RoundingMode.UP);

                valorTotalCotizacion = valorTotal.add(baseCero.add(valorIva));
                // valorTotalCotizacion.setScale(5, RoundingMode.UP);

                valorTotalInicialVent = valorTotalInicial;
                //  valorTotalInicialVent.setScale(5, RoundingMode.UP);

                descuentoValorFinal = valorDescuento.multiply(facturIvaMasBase);
                //  descuentoValorFinal.setScale(5, RoundingMode.UP);
                totalDescuento = valorDescuento;
                //descuentoValorFinal.setScale(5, RoundingMode.UP);

                subTotalCotizacion = ArchivoUtils.redondearDecimales(subTotalCotizacion, 2);
                subTotalBaseCero = ArchivoUtils.redondearDecimales(subTotalBaseCero, 2);
                valorTotalCotizacion = ArchivoUtils.redondearDecimales(valorTotalCotizacion, 2);
                valorTotalInicialVent = ArchivoUtils.redondearDecimales(valorTotalInicialVent, 2);
                ivaCotizacion = ArchivoUtils.redondearDecimales(ivaCotizacion, 2);
                descuentoValorFinal = ArchivoUtils.redondearDecimales(descuentoValorFinal, 2);

            } catch (Exception e) {
                System.out.println("error de calculo de valores " + e);
            }

        }
    }

    private void guardarFactura(String valor) {

        try {

            String folderGenerados = PATH_BASE + File.separator + amb.getAmGenerados()
                        + File.separator + new Date().getYear()
                        + File.separator + new Date().getMonth();
            String folderEnviarCliente = PATH_BASE + File.separator + amb.getAmEnviocliente()
                        + File.separator + new Date().getYear()
                        + File.separator + new Date().getMonth();
            String folderFirmado = PATH_BASE + File.separator + amb.getAmFirmados()
                        + File.separator + new Date().getYear()
                        + File.separator + new Date().getMonth();

            String foldervoAutorizado = PATH_BASE + File.separator + amb.getAmAutorizados()
                        + File.separator + new Date().getYear()
                        + File.separator + new Date().getMonth();

            String folderNoAutorizados = PATH_BASE + File.separator + amb.getAmNoAutorizados()
                        + File.separator + new Date().getYear()
                        + File.separator + new Date().getMonth();

            /*EN EL CASO DE NO EXISTIR LOS DIRECTORIOS LOS CREA*/
            File folderGen = new File(folderGenerados);
            if (!folderGen.exists()) {
                folderGen.mkdirs();
            }
            File folderFirm = new File(folderFirmado);
            if (!folderFirm.exists()) {
                folderFirm.mkdirs();
            }

            File folderAu = new File(foldervoAutorizado);
            if (!folderAu.exists()) {
                folderAu.mkdirs();
            }

            File folderCliente = new File(folderEnviarCliente);
            if (!folderCliente.exists()) {
                folderCliente.mkdirs();
            }
            File folderNoAut = new File(folderNoAutorizados);
            if (!folderNoAut.exists()) {
                folderNoAut.mkdirs();
            }
            /*Ubicacion del archivo firmado para obtener la informacion*/

//            if (!parametrizar.getParCreditoClientes()) {
//                if (saldoFacturas.doubleValue() < valorTotalCotizacion.doubleValue()) {
//                    Clients.showNotification("Excedio el monto asignado al cliente",
//                            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
//                    return;
//                }
//            }
            if (valor.equals("CG")) {
                if (transportista == null || numeroPlaca.equals("")) {

                    Clients.showNotification("Para generar una guia debe seleccionar un conductor e ingresar la placa",
                                Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
                    return;

                }
            }
            /*VERIFICA SI ES FACTURA O PROFORMA Y COLOCAL EL NUMERO*/
            if ((accion.equals("create")) || (tipoVentaAnterior.equals("PROF") && (tipoVenta.equals("FACT")))) {
                verificarSecNumeracion();
                descargarKardex = Boolean.TRUE;

            } else {

                Boolean verificaEntraSecuen = Boolean.FALSE;

                if (accion.equals("create") && tipoVentaAnterior.equals("PROF")) {
                    verificarSecNumeracion();
                } else if (tipoVenta.equals("FACT") && (tipoVentaAnterior.equals("NTV"))) {
                    verificarSecNumeracion();
                    // numeroFactura = factura.getFacNumProforma();
                } else if (tipoVenta.equals("NTV") && (tipoVentaAnterior.equals("NTV"))) {
                    numeroFactura = factura.getFacNumNotaVenta();
                    // numeroFactura = factura.getFacNumProforma();
                } else if (tipoVenta.equals("PROF") && (tipoVentaAnterior.equals("FACT"))) {
                    verificarSecNumeracion();
                    // numeroFactura = factura.getFacNumProforma();
                } else if (tipoVenta.equals("NTE") && (!tipoVentaAnterior.equals("FACT"))) {
                    //
                    verificarSecNumeracion();
                } else if (tipoVenta.equals("NTV") && (!tipoVentaAnterior.equals("FACT"))) {
                    verificarSecNumeracion();
                } else {

                    if (tipoVenta.equals("NTV")) {
                        numeroFactura = factura.getFacNumNotaVenta();
                    }
                    if (tipoVenta.equals("PROF")) {

                        numeroFactura = factura.getFacNumProforma();
                    }
                    if (tipoVenta.equals("NTE")) {
                        numeroFactura = factura.getFacNumNotaEntrega();
                    }

//                    if (tipoVenta.equals("FACT")) {
//                    descargarKardex = Boolean.FALSE;
//                    numeroFactura = factura.getFacNumero();
//                } else
                    if (tipoVenta.equals("FACT")) {
                        descargarKardex = Boolean.TRUE;
                        numeroFactura = factura.getFacNumero();
                    }
                }

            }

            if (tipoVentaAnterior.equals("NTE") && (tipoVenta.equals("FACT"))) {
                descargarKardex = Boolean.FALSE;
                for (Factura factura1 : seleccionNotaEntrega) {
                    factura1.setFacNotaEntregaProcess("S");
                    servicioFactura.modificar(factura1);
                }
            }
            if (tipoVentaAnterior.equals("NTV") && (tipoVenta.equals("FACT"))) {
                descargarKardex = Boolean.FALSE;
            }
            numeroFacturaTexto();

            //guarda con o sin guia de remision 
            facConSinGuia = valor;

            Tipoambiente amb = servicioTipoAmbiente.FindALlTipoambiente();
            //armar la cabecera de la factura
//Coloca la fecha para el cobro de la totalidad de la factura
            Calendar calendar = Calendar.getInstance(); //obtiene la fecha de hoy 
            calendar.add(Calendar.DATE, Integer.valueOf(facplazo)); //el -3 indica que se le restaran 3 dias 
            Date fechaPagoPlazo = calendar.getTime();

            factura.setFacTipo(tipoVenta);
            factura.setFacDescripcion(facturaDescripcion);
            factura.setFacFecha(fechafacturacion);
            factura.setFacEstado(estdoFactura);
            factura.setFacNumeroText(numeroFacturaText);
            factura.setPuntoemision(amb.getAmPtoemi());
            factura.setEstadosri("PENDIENTE");
            factura.setCodestablecimiento(amb.getAmEstab());
            factura.setCod_tipoambiente(amb);
            factura.setFaConSinGuia(facConSinGuia);
            factura.setFacSubsidio(subsidioTotal);
            factura.setFacFechaCobroPlazo(fechaPagoPlazo);

            if (tipoVenta.equals("SINF")) {
                factura.setFacNumero(0);
                factura.setFacNumProforma(0);
            } else if (tipoVenta.equals("FACT")) {
                factura.setFacNumero(numeroFactura);
                factura.setFacNumProforma(0);
                factura.setFacNumNotaEntrega(0);
                /*PARA EL SRI*/
                factura.setTipodocumento("01");
            } else if (tipoVenta.equals("PROF")) {
                descargarKardex = Boolean.FALSE;
                factura.setFacNumero(0);
                factura.setFacNumNotaEntrega(0);
                factura.setFacNumProforma(numeroFactura);
            } else if (tipoVenta.equals("NTE")) {
                factura.setFacNotaEntregaProcess("N");
                factura.setFacNumero(0);
                factura.setFacNumProforma(0);
                factura.setFacNumNotaEntrega(numeroFactura);
            } else if (tipoVenta.equals("NTV")) {

                factura.setFacNumero(0);
                factura.setFacNumProforma(0);
                factura.setFacNumNotaEntrega(0);
                factura.setFacNumNotaVenta(numeroFactura);
            }

            factura.setIdCliente(clienteBuscado);
            factura.setIdUsuario(credential.getUsuarioSistema());
            factura.setFacSubtotal(subTotalCotizacion.add(subTotalBaseCero));
            factura.setFacIva(ivaCotizacion);
            factura.setFacTotal(valorTotalCotizacion);
            factura.setFacSaldoAmortizado(valorTotalCotizacion);
            factura.setFacDescuento(totalDescuento);
            factura.setFacCodIce("3");
            factura.setFacCodIva("2");
            factura.setFacTotalBaseCero(subTotalBaseCero);
            /*0 SI NO LLEVA IVA Y 2 SI LLEVA IVA*/
            factura.setCodigoPorcentaje(parametrizar.getParCodigoIva());
            factura.setFacPorcentajeIva(parametrizar.getParIva().toString());
            factura.setFacMoneda("DOLAR");
            factura.setIdFormaPago(formaPagoSelected);
            factura.setFacPlazo(BigDecimal.valueOf(Double.valueOf(facplazo)));
            factura.setFacUnidadTiempo(formaPagoSelected.getUnidadTiempo());
            factura.setIdEstado(servicioEstadoFactura.findByEstCodigo(estdoFactura));

            factura.setFacTotalBaseGravaba(subTotalCotizacion);
//            factura.setFacTotalBaseGravaba(subTotalBaseCero);

            if (factura.getFacEstado().equals("PE")) {
                factura.setFacAbono(cobro);
                factura.setFacSaldo(cambio.negate());
            } else {
                factura.setFacAbono(BigDecimal.ZERO);
                factura.setFacSaldo(BigDecimal.ZERO);
            }
            //armar el detalle de la factura
            List<DetalleFacturaDAO> detalleFactura = new ArrayList<DetalleFacturaDAO>();
            List<DetalleFacturaDAO> listaPedido = listaDetalleFacturaDAOMOdel.getInnerList();
            ServicioSubCuenta servicioSubcuenta = new ServicioSubCuenta();

            ServicioAcSubcuenta servicioAcSubcuenta = new ServicioAcSubcuenta();

            CuSubCuenta consumoAguaPotable = servicioSubcuenta.findByNombre("TARIFA BASICA DE AGUA POTABLE").get(0);
            CuSubCuenta excedente = servicioSubcuenta.findByNombre("EXCEDENTE").get(0);
            CuSubCuenta alcantarillado = servicioSubcuenta.findByNombre("ALCANTARILLADO").get(0);
            CuSubCuenta derechoAcometida = servicioSubcuenta.findByNombre("DERECHO DE ACOMETIDA").get(0);
            CuSubCuenta multas = servicioSubcuenta.findByNombre("MULTAS").get(0);
            CuSubCuenta material = servicioSubcuenta.findByNombre("VENTA DE MATERIALES").get(0);
            CuSubCuenta desechosSolidos = servicioSubcuenta.findByNombre("DESECHOS SOLIDOS").get(0);
            CuSubCuenta medioAmbiente = servicioSubcuenta.findByNombre("MEDIO AMBIENTE").get(0);
            CuSubCuenta valoresImpagos = servicioSubcuenta.findByNombre("TARIFA POR VALORES IMPAGOS INTERES").get(0);
            CuSubCuenta valoresImpagos2 = servicioSubcuenta.findByNombre("TARIFA MULTAS POR DOS MESES IMPAGOS").get(0);

            BigDecimal valorConsumo = consumoAguaPotable.getSubcTotal();
            BigDecimal valorExcedente = excedente.getSubcTotal();
            BigDecimal valorAlcantarillado = alcantarillado.getSubcTotal();
            BigDecimal valorDerechoAcometida = derechoAcometida.getSubcTotal();
            BigDecimal valorMultas = multas.getSubcTotal();
            BigDecimal valorMaterial = material.getSubcTotal();
            BigDecimal valorDesechosSolidos = desechosSolidos.getSubcTotal();
            BigDecimal valorMedioAmbiente = medioAmbiente.getSubcTotal();
            BigDecimal valorValoresImpagos = valoresImpagos.getSubcTotal();
            BigDecimal valorValoresImpagos2 = valoresImpagos2.getSubcTotal();

            AcSubCuenta acConsumoAguaPotable = new AcSubCuenta();
            AcSubCuenta acExcedente = new AcSubCuenta();
            AcSubCuenta acAlcantarillado = new AcSubCuenta();
            AcSubCuenta acDerechoAcometida = new AcSubCuenta();
            AcSubCuenta acMultas = new AcSubCuenta();
            AcSubCuenta acMaterial = new AcSubCuenta();
            AcSubCuenta acDesechosSolidos = new AcSubCuenta();
            AcSubCuenta acMedioAmbiente = new AcSubCuenta();
            AcSubCuenta acValoresImpagos = new AcSubCuenta();
            AcSubCuenta acValoresImpagos2 = new AcSubCuenta();

            java.util.Date fecha = new Date();
            if (listaPedido.size() > 0) {
                for (DetalleFacturaDAO item : listaPedido) {
                    if (item.getProducto() != null) {
                        detalleFactura.add(item);
                        BigDecimal totalItem = item.getTotal();

                        if (item.getDescripcion().equals("TARIFA BASICA DE AGUA POTABLE")) {
                            consumoAguaPotable.setSubcTotal(valorConsumo.add(totalItem));
                            servicioSubcuenta.modificar(consumoAguaPotable);
                            acConsumoAguaPotable.setIdSubCuenta(consumoAguaPotable);
                            acConsumoAguaPotable.setHaber(totalItem);
                            acConsumoAguaPotable.setFechaAcSubcuenta(fecha);
                            servicioAcSubcuenta.crear(acConsumoAguaPotable);
                        } else if (item.getDescripcion().equals("EXCEDENTE")) {
                            excedente.setSubcTotal(valorExcedente.add(totalItem));
                            servicioSubcuenta.modificar(excedente);
                            acExcedente.setIdSubCuenta(excedente);
                            acExcedente.setHaber(totalItem);
                            acExcedente.setFechaAcSubcuenta(fecha);
                            servicioAcSubcuenta.crear(acExcedente);
                        } else if (item.getDescripcion().equals("ALCANTARILLADO")) {
                            alcantarillado.setSubcTotal(valorAlcantarillado.add(totalItem));
                            servicioSubcuenta.modificar(alcantarillado);
                            acAlcantarillado.setIdSubCuenta(alcantarillado);
                            acAlcantarillado.setHaber(totalItem);
                            acAlcantarillado.setFechaAcSubcuenta(fecha);
                            servicioAcSubcuenta.crear(acAlcantarillado);
                        } else if (item.getDescripcion().equals("MULTAS")) {
                            multas.setSubcTotal(valorMultas.add(totalItem));
                            servicioSubcuenta.modificar(multas);
                            acMultas.setIdSubCuenta(multas);
                            acMultas.setHaber(totalItem);
                            acMultas.setFechaAcSubcuenta(fecha);
                            servicioAcSubcuenta.crear(acMultas);
                        } else if (item.getDescripcion().equals("DESECHOS SOLIDOS")) {
                            desechosSolidos.setSubcTotal(valorDesechosSolidos.add(totalItem));
                            servicioSubcuenta.modificar(desechosSolidos);
                            acDesechosSolidos.setIdSubCuenta(desechosSolidos);
                            acDesechosSolidos.setHaber(totalItem);
                            acDesechosSolidos.setFechaAcSubcuenta(fecha);
                            servicioAcSubcuenta.crear(acDesechosSolidos);
                        } else if (item.getDescripcion().equals("MEDIO AMBIENTE")) {
                            medioAmbiente.setSubcTotal(valorMedioAmbiente.add(totalItem));
                            servicioSubcuenta.modificar(medioAmbiente);
                            acMedioAmbiente.setIdSubCuenta(medioAmbiente);
                            acMedioAmbiente.setHaber(totalItem);
                            acMedioAmbiente.setFechaAcSubcuenta(fecha);
                            servicioAcSubcuenta.crear(acMedioAmbiente);
                        } else if (item.getDescripcion().equals("INTERES VALORES IMPAGOS")) {
                            valoresImpagos.setSubcTotal(valorValoresImpagos.add(totalItem));
                            servicioSubcuenta.modificar(valoresImpagos);
                            acValoresImpagos.setIdSubCuenta(valoresImpagos);
                            acValoresImpagos.setHaber(totalItem);
                            acValoresImpagos.setFechaAcSubcuenta(fecha);
                            servicioAcSubcuenta.crear(acValoresImpagos);
                        } else if (item.getDescripcion().equals("MULTA POR 2 MESES IMPAGOS")) {
                            valoresImpagos2.setSubcTotal(valorValoresImpagos2.add(totalItem));
                            servicioSubcuenta.modificar(valoresImpagos2);
                            acValoresImpagos2.setIdSubCuenta(valoresImpagos2);
                            acValoresImpagos2.setHaber(totalItem);
                            acValoresImpagos2.setFechaAcSubcuenta(fecha);
                            servicioAcSubcuenta.crear(acValoresImpagos2);
                        } else {
                            material.setSubcTotal(valorMaterial.add(item.getTotal()));
                            servicioSubcuenta.modificar(material);
                            acMaterial.setIdSubCuenta(material);
                            acMaterial.setHaber(totalItem);
                            acMaterial.setFechaAcSubcuenta(fecha);
                            servicioAcSubcuenta.crear(acMaterial);
                        }
                    }

                }

                if (tipoVenta.equals("SINF")) {

                    Factura ultimaVenta = new Factura();
                    Factura verificarFact = servicioFactura.ultimaVentaDiaria(fechafacturacion);

                    if (verificarFact == null) {
                        Factura facturaNueva = new Factura();
                        facturaNueva.setFacTipo("SINF");
                        facturaNueva.setFacFecha(fechafacturacion);
                        facturaNueva.setFacEstado(estdoFactura);
                        facturaNueva.setFacNumero(0);
                        facturaNueva.setIdCliente(clienteBuscado);
                        facturaNueva.setIdUsuario(credential.getUsuarioSistema());
                        facturaNueva.setFacSubtotal(subTotalCotizacion);
                        // facturaNueva.setFacSubtotal(valorTotalCotizacion);
                        facturaNueva.setFacIva(ivaCotizacion);

                        facturaNueva.setFacTotal(valorTotalCotizacion);
                        facturaNueva.setFacDescuento(totalDescuento);
                        facturaNueva.setFacCodIce("3");
                        facturaNueva.setFacCodIva("2");
                        facturaNueva.setFacTotalBaseCero(BigDecimal.ZERO);
                        facturaNueva.setCodigoPorcentaje("2");
                        facturaNueva.setFacPorcentajeIva(parametrizar.getParIva().toString());
                        facturaNueva.setFacMoneda("DOLAR");
                        facturaNueva.setIdFormaPago(formaPagoSelected);
                        facturaNueva.setFacPlazo(BigDecimal.valueOf(Double.valueOf(formaPagoSelected.getPlazo())));
                        facturaNueva.setFacUnidadTiempo(formaPagoSelected.getUnidadTiempo());
                        facturaNueva.setIdEstado(servicioEstadoFactura.findByEstCodigo(estdoFactura));
                        facturaNueva.setFacTotalBaseGravaba(facturaNueva.getFacSubtotal());
                        //servicioFactura.crear(facturaNueva);
                        if (detalleFactura.size() > 0) {
                            DetalleFacturaDAO recuAO = detalleFactura.get(0);
                            if (recuAO.getLectura() != null) {
                                facturaNueva.setFacLecAnterior(recuAO.getLectura() != null ? recuAO.getLectura().getLecAnterior() : BigDecimal.ZERO);
                                facturaNueva.setFacLecActual(recuAO.getLectura() != null ? recuAO.getLectura().getLecActual() : BigDecimal.ZERO);
                                facturaNueva.setFacMetrosCubicos(recuAO.getLectura() != null ? recuAO.getLectura().getLecMetrosCubicos() : BigDecimal.ZERO);
                                facturaNueva.setFacLecMes(recuAO.getLectura() != null ? recuAO.getLectura().getLecMes() : 0);
                                facturaNueva.setFacMedidor(recuAO.getLectura() != null ? recuAO.getLectura().getIdMedidor().getMedNumero() : "S/N");
                                facturaNueva.setFacDirMedidor(recuAO.getLectura() != null ? recuAO.getLectura().getIdMedidor().getMedDireccion() : "S/N");
                            } else {
                                facturaNueva.setFacLecAnterior(BigDecimal.ZERO);
                                facturaNueva.setFacLecActual(BigDecimal.ZERO);
                                facturaNueva.setFacMetrosCubicos(BigDecimal.ZERO);
                                facturaNueva.setFacLecMes(new Date().getMonth());
                                facturaNueva.setFacMedidor(medidorEncontrado != null ? medidorEncontrado.getMedNumero() : "S/N");
                                facturaNueva.setFacDirMedidor(medidorEncontrado != null ? medidorEncontrado.getMedDireccion() : "S/N");
                            }
                        }

                        servicioFactura.guardarFactura(detalleFactura, facturaNueva);

                    } else {

                        BigDecimal total = verificarFact.getFacTotal().add(valorTotalCotizacion);
                        BigDecimal subTotal = total.divide(BigDecimal.valueOf(1.14), 4, RoundingMode.UP);
                        BigDecimal iva = subTotal.multiply(BigDecimal.valueOf(0.14));
                        iva.setScale(4, RoundingMode.UP);
                        subTotal.setScale(4, RoundingMode.UP);
                        total.setScale(4, RoundingMode.UP);
                        verificarFact.setFacTipo("SINF");
                        verificarFact.setFacFecha(fechafacturacion);
                        verificarFact.setFacEstado(factura.getFacEstado());
                        verificarFact.setFacNumero(0);
                        verificarFact.setIdCliente(factura.getIdCliente());
                        verificarFact.setIdUsuario(credential.getUsuarioSistema());
                        verificarFact.setFacSubtotal(subTotal);
                        verificarFact.setFacIva(iva);
                        verificarFact.setFacTotal(total);
                        verificarFact.setFacAbono(BigDecimal.ZERO);
                        verificarFact.setFacSaldo(BigDecimal.ZERO);

                        verificarFact.setFacDescuento(BigDecimal.ZERO);
                        verificarFact.setFacCodIce("3");
                        verificarFact.setFacCodIva("2");
                        verificarFact.setFacTotalBaseCero(BigDecimal.ZERO);
                        verificarFact.setCodigoPorcentaje("2");
                        verificarFact.setFacPorcentajeIva(parametrizar.getParIva().toString());
                        verificarFact.setFacMoneda("DOLAR");
                        verificarFact.setIdFormaPago(formaPagoSelected);
                        verificarFact.setFacPlazo(BigDecimal.valueOf(Double.valueOf(formaPagoSelected.getPlazo())));
                        verificarFact.setFacUnidadTiempo(formaPagoSelected.getUnidadTiempo());
                        verificarFact.setIdEstado(servicioEstadoFactura.findByEstCodigo(estdoFactura));
                        verificarFact.setFacTotalBaseGravaba(verificarFact.getFacSubtotal());

                        if (detalleFactura.size() > 0) {
                            DetalleFacturaDAO recuAO = detalleFactura.get(0);
                            if (recuAO.getLectura() != null) {
                                verificarFact.setFacLecAnterior(recuAO.getLectura() != null ? recuAO.getLectura().getLecAnterior() : BigDecimal.ZERO);
                                verificarFact.setFacLecActual(recuAO.getLectura() != null ? recuAO.getLectura().getLecActual() : BigDecimal.ZERO);
                                verificarFact.setFacMetrosCubicos(recuAO.getLectura() != null ? recuAO.getLectura().getLecMetrosCubicos() : BigDecimal.ZERO);
                                verificarFact.setFacLecMes(recuAO.getLectura() != null ? recuAO.getLectura().getLecMes() : 0);
                                verificarFact.setFacMedidor(recuAO.getLectura() != null ? recuAO.getLectura().getIdMedidor().getMedNumero() : "S/N");
                                verificarFact.setFacDirMedidor(recuAO.getLectura() != null ? recuAO.getLectura().getIdMedidor().getMedDireccion() : "S/N");
                            } else {
                                verificarFact.setFacLecAnterior(BigDecimal.ZERO);
                                verificarFact.setFacLecActual(BigDecimal.ZERO);
                                verificarFact.setFacMetrosCubicos(BigDecimal.ZERO);
                                verificarFact.setFacLecMes(new Date().getMonth());
                                verificarFact.setFacMedidor(medidorEncontrado != null ? medidorEncontrado.getMedNumero() : "S/N");
                                verificarFact.setFacDirMedidor(medidorEncontrado != null ? medidorEncontrado.getMedDireccion() : "S/N");
                            }
                        }

                        servicioFactura.guardarFacturaVentaDiaria(detalleFactura, verificarFact);
                    }

                } else {
                    System.out.println("  factura.setIdCliente(clienteBuscado); " + clienteBuscado.getCliCedula() + " " + clienteBuscado.getCliApellidos());
                    factura.setIdCliente(clienteBuscado);
                    /*GENERAMOS LA CLAVE DE ACCESO PARA ENVIAR LA FACTURA DIRECTAMENTE ASI NO ESTE 
                    AUTORIZADA*/

                    String claveAcceso = ArchivoUtils.generaClave(factura.getFacFecha(), "01", amb.getAmRuc(), amb.getAmCodigo(), amb.getAmEstab() + amb.getAmPtoemi(), factura.getFacNumeroText(), "12345678", "1");

                    factura.setFacClaveAcceso(claveAcceso);
                    factura.setFacClaveAutorizacion(claveAcceso);

                    if (accion.equals("create")) {

                        for (DetalleFacturaDAO itemFactura : detalleFactura) {
                            if (itemFactura.getLectura() != null) {
                                Lectura actualiza = itemFactura.getLectura();
                                actualiza.setLecPagada("S");
                                servicioLectura.modificar(actualiza);
                            }

                        }
                        servicioFactura.guardarFactura(detalleFactura, factura);

                    } else {

                        servicioFactura.eliminar(factura);
                        servicioDetalleKardex.eliminarKardexVenta(factura.getIdFactura());
                        servicioFactura.guardarFactura(detalleFactura, factura);
                    }
                    if (valor.equalsIgnoreCase("CG")) {

                        numeroGuia();
                        Guiaremision guiaremision = new Guiaremision();
                        guiaremision.setFacNumero(numeroGuia);
                        guiaremision.setFacNumeroText(numeroGuiaText);
                        guiaremision.setIdFactura(factura);
                        guiaremision.setIdUsuario(credential.getUsuarioSistema());
                        guiaremision.setFacFecha(new Date());
                        guiaremision.setFacEstado("PENDIENTE");
                        guiaremision.setTipodocumento("06");
                        guiaremision.setPuntoemision(factura.getPuntoemision());
                        guiaremision.setCodestablecimiento(factura.getCodestablecimiento());
                        guiaremision.setEstadosri("PENDIENTE");

                        String claveAccesoGuia = ArchivoUtils.generaClave(guiaremision.getFacFecha(), "06", amb.getAmRuc(), amb.getAmCodigo(), amb.getAmEstab() + amb.getAmPtoemi(), guiaremision.getFacNumeroText(), "12345678", "1");

                        guiaremision.setFacClaveAcceso(claveAccesoGuia);
                        guiaremision.setFacClaveAutorizacion(claveAccesoGuia);
                        guiaremision.setCodTipoambiente(factura.getCod_tipoambiente().getCodTipoambiente());
                        guiaremision.setFacFechaSustento(factura.getFacFecha());
                        guiaremision.setIdTransportista(transportista);
                        guiaremision.setNumplacaguia(numeroPlaca);
                        guiaremision.setIdCliente(factura.getIdCliente());
                        guiaremision.setFechainitranspguia(incioTraslado);
                        guiaremision.setFechafintranspguia(finTraslado);
                        guiaremision.setMotivoGuia(motivoGuia);
                        guiaremision.setPartida(partida);
                        guiaremision.setLlegada(llegada);
                        List<DetalleGuiaremision> detalleGuia = new ArrayList<DetalleGuiaremision>();
                        for (DetalleFacturaDAO itemDet : detalleFactura) {
                            detalleGuia.add(new DetalleGuiaremision(itemDet.getCantidad(), itemDet.getDescripcion(), itemDet.getProducto(), guiaremision));
                        }
                        servicioGuia.guardarGuiaremision(detalleGuia, guiaremision);

                    }
                    /*VERIFICA SI EL CLINETE QUIERE AUTORIZAR LA FACTURA*/
                    if (!parametrizar.getParEstado() || tipoVenta.equals("PROF")) {
                        /*en el caso que no se desee autorizar la factura*/
                    } else {
                        UtilitarioAutorizarSRI autorizarSRI = new UtilitarioAutorizarSRI();
                        autorizarSRI.autorizarSRI(factura);
                    }

                }

            }
            //ejecutamos el mensaje 
//            Clients.showNotification("Factura registrada con éxito", Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 5000, true);
            /*VERIFICA QUE NO SEA UNA PROFORMA QUE SE CONVERTIRA EN FACTURA, VERIFICA SI ES NOT DE ENTREGA 
            NINGUNA PROFORMA DESCARGA*/
            if (descargarKardex) {
                /*INGRESAMOS LO MOVIMIENTOS AL KARDEX*/
                Kardex kardex = null;
                DetalleKardex detalleKardex = null;

                for (DetalleFacturaDAO item : listaPedido) {
                    if (item.getProducto() != null) {

                        Tipokardex tipokardex = servicioTipoKardex.findByTipkSigla("SAL");
                        if (servicioKardex.FindALlKardexs(item.getProducto()) == null) {
                            kardex = new Kardex();
                            kardex.setIdProducto(item.getProducto());
                            kardex.setKarDetalle("Inicio de inventario desde la facturacion para el producto: " + item.getProducto().getProdNombre());
                            kardex.setKarFecha(new Date());
                            kardex.setKarFechaKardex(new Date());
                            kardex.setKarTotal(BigDecimal.ZERO);
                            servicioKardex.crear(kardex);
                        }
                        detalleKardex = new DetalleKardex();
                        kardex = servicioKardex.FindALlKardexs(item.getProducto());
                        detalleKardex.setIdKardex(kardex);
                        detalleKardex.setDetkFechakardex(fechafacturacion);
                        detalleKardex.setDetkFechacreacion(new Date());
                        detalleKardex.setIdTipokardex(tipokardex);
                        detalleKardex.setDetkKardexmanual(Boolean.FALSE);
                        detalleKardex.setDetkDetalles("Disminuye al kardex desde facturacion con: " + tipoVenta + "-" + factura.getFacNumeroText());
                        detalleKardex.setIdFactura(factura);
                        detalleKardex.setDetkCantidad(item.getCantidad());
                        servicioDetalleKardex.crear(detalleKardex);
                        /*ACTUALIZA EL TOTAL DEL KARDEX*/
                        TotalKardex totales = servicioKardex.totalesForKardex(kardex);
                        BigDecimal total = totales.getTotalKardex();
                        kardex.setKarTotal(total);
                        servicioKardex.modificar(kardex);

                    }
                }

            }

            reporteGeneral();

            if (accion.equals("create")) {
                Executions.sendRedirect("/venta/facturar.zul");
            } else {
//                Executions.sendRedirect("/venta/listafacturas.zul");
                windowModCotizacionFact.detach();
            }

        } catch (IOException e) {
            System.out.println("ERROR FACTURA " + e.getMessage());
            Messagebox.show("Ocurrio un error guardar la factura ", "Atención", Messagebox.OK, Messagebox.ERROR);
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR FACTURA " + e.getMessage());
            Messagebox.show("Ocurrio un error guardar la factura ", "Atención", Messagebox.OK, Messagebox.ERROR);
        } catch (IllegalAccessException e) {
            System.out.println("ERROR FACTURA " + e.getMessage());
            Messagebox.show("Ocurrio un error guardar la factura ", "Atención", Messagebox.OK, Messagebox.ERROR);
        } catch (InstantiationException e) {
            System.out.println("ERROR FACTURA " + e.getMessage());
            Messagebox.show("Ocurrio un error guardar la factura ", "Atención", Messagebox.OK, Messagebox.ERROR);
        } catch (NumberFormatException e) {
            System.out.println("ERROR FACTURA " + e.getMessage());
            Messagebox.show("Ocurrio un error guardar la factura ", "Atención", Messagebox.OK, Messagebox.ERROR);
        } catch (SQLException e) {
            System.out.println("ERROR FACTURA " + e.getMessage());
            Messagebox.show("Ocurrio un error guardar la factura ", "Atención", Messagebox.OK, Messagebox.ERROR);
        } catch (NamingException e) {
            System.out.println("ERROR FACTURA " + e.getMessage());
            Messagebox.show("Ocurrio un error guardar la factura ", "Atención", Messagebox.OK, Messagebox.ERROR);
        } catch (JRException e) {
            System.out.println("ERROR FACTURA " + e.getMessage());
            Messagebox.show("Ocurrio un error guardar la factura ", "Atención", Messagebox.OK, Messagebox.ERROR);
        }

    }

    @Command
    @NotifyChange({"listaDetalleFacturaDAOMOdel", "subTotalCotizacion", "ivaCotizacion", "valorTotalCotizacion"})
    public void Guardar(@BindingParam("valor") String valor) {
        System.out.println("formaPagoSelected " + formaPagoSelected);
        facConSinGuia = valor;
        if (!clienteBuscado.getCliCedula().equals("") && formaPagoSelected != null) {
            if (valorTotalCotizacion.intValue() >= 200 && clienteBuscado.getCliCedula().contains("999999999")) {
                Clients.showNotification("El valor de la factura no puede pasar de $200 para enviarla como Consumidor Final ", "error", null, "end_before", 3000, true);
                return;
            }
            if (listaDetalleFacturaDAOMOdel.size() > 0) {
                if (!listaDetalleFacturaDAOMOdel.get(0).getDescripcion().equals("")) {
                    guardarFactura(valor);

                } else {
                    Messagebox.show("Registre un producto para proceder a la facturación", "Atención", Messagebox.OK, Messagebox.ERROR);
                }

            } else {
                Messagebox.show("Registre un producto para proceder a la facturación", "Atención", Messagebox.OK, Messagebox.ERROR);
            }

        } else {
            Messagebox.show("Verifique el cliente y la forma de pago", "Atención", Messagebox.OK, Messagebox.ERROR);
        }

    }

    @Command
    @NotifyChange({"listaProducto", "buscarNombreProd"})
    public void buscarLikeNombreProd() {

        findProductoLikeNombre();
    }

    @Command
    @NotifyChange({"listaProducto", "buscarCodigoProd"})
    public void buscarLikeCodigoProd() {

        findProductoLikeCodigo();
    }

    @Command
    @NotifyChange({"listaKardexProducto", "buscarNombreProd"})
    public void buscarLikeKardexNombreProd() {

        findKardexProductoLikeNombre();
    }

    @Command
    @NotifyChange({"listaKardexProducto", "buscarCodigoProd"})
    public void buscarLikeKardexCodigoProd() {

        findKardexProductoLikeCodigo();
    }

    private void findKardexProductoLikeNombre() {
        listaKardexProducto = servicioKardex.findByCodOrName(buscarCodigoProd, buscarNombreProd);
    }

    private void findKardexProductoLikeCodigo() {
        listaKardexProducto = servicioKardex.findByCodOrName(buscarCodigoProd, buscarNombreProd);
    }

    @Command
    @NotifyChange({"listaProductoCmb", "codigo"})
    public void buscarInternoCodigo(@BindingParam("valor") DetalleFacturaDAO valor) {
        System.out.println("valor codigo " + valor.getCodigo());
//        findProductoLikeCodigo();
        if (codigo.length() >= 3) {
            listaProductoCmb = servicioProducto.findLikeProdNombre(codigo.toUpperCase());
        }

//        valor.setListaProductoCmb(listaProductoCmb);
    }

    private void findProductoLikeNombre() {
        listaProducto = servicioProducto.findLikeProdNombre(buscarNombreProd);
    }

    private void findProductoLikeCodigo() {
        listaProducto = servicioProducto.findLikeProdCodigo(buscarCodigoProd);
    }

    @Command
    @NotifyChange({"subTotalCotizacion", "ivaCotizacion", "valorTotalCotizacion"})
    public void refrescarPagina() {
        calcularValoresTotales();
//        Clients.showNotification("Actaliza", Clients.NOTIFICATION_TYPE_INFO, null, "end_before", 100, true);
    }

    @Command
    @NotifyChange("clienteBuscado")
    public void seleccionarProductoLista(@BindingParam("valor") Producto valor) {
        System.out.println("producto seleccionado " + valor.getProdCodigo());
        codigoBusqueda = valor.getProdCodigo();
        windowProductoBuscar.detach();

    }

    @Command
    @NotifyChange("clienteBuscado")
    public void mensaje(@BindingParam("valor") DetalleFacturaDAO valor) {
        Messagebox.show("Fucniona " + valor.getDescripcion(), "Atención", Messagebox.OK, Messagebox.INFORMATION);

    }

    public void reporteGeneral() throws JRException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, NamingException {
        EntityManager emf = HelperPersistencia.getEMF();

        try {
            String reporte = parametrizar.getParImprimeFactura().trim();
            emf.getTransaction().begin();
            /*CONEXION A LA BASE DE DATOS*/
            con = emf.unwrap(Connection.class);
            if (!tipoVenta.equals("SINF")) {

                //  con = emf.unwrap(Connection.class);
                String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                            .getRealPath("/reportes");
                String reportPath = "";
//                con = ConexionReportes.Conexion.conexion();

                if (tipoVenta.equals("FACT")) {
//                    reportPath = reportFile + File.separator + "puntoventa.jasper";
//                    reportPath = reportFile + File.separator + "factura.jasper";
                    reportPath = reportFile + File.separator + reporte;

                } else if (tipoVenta.equals("PROF")) {
                    /*ES EL PATH DONDE SE ENCUENTRA EL REPORTE EN MI CASO*/
                    reportPath = reportFile + File.separator + "proforma.jasper";
                } else if (tipoVenta.equals("NTE")) {
                    /*ES EL PATH DONDE SE ENCUENTRA EL REPORTE EN MI CASO*/
                    reportPath = reportFile + File.separator + "notaentrega.jasper";
                } else if (tipoVenta.equals("NTV")) {
                    /*ES EL PATH DONDE SE ENCUENTRA EL REPORTE EN MI CASO*/
                    reportPath = reportFile + File.separator + "notaventaticket.jasper";
                }
                /*PARAMETROS DEL REPORTE*/
                Map<String, Object> parametros = new HashMap<String, Object>();

                //  parametros.put("codUsuario", String.valueOf(credentialLog.getAdUsuario().getCodigoUsuario()));
                parametros.put("numfactura", numeroFactura);

                if (con != null) {
                    System.out.println("Conexión Realizada Correctamenteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                }

                if (parametrizar.getParImpFactura()) {
                    FileInputStream is = null;
                    is = new FileInputStream(reportPath);
                    /*COMPILAS EL ARCHIVO.JASPER*/
                    byte[] buf = JasperRunManager.runReportToPdf(is, parametros, con);
                    /*EN MI CASO LO PRESENTO EN UNA VENTANA EMERGENTE  PERO LO ANTERIOR SERIA TODO*/
                    InputStream mediais = new ByteArrayInputStream(buf);

                    AMedia amedia = new AMedia("Reporte", "pdf", "application/pdf", mediais);
                    fileContent = amedia;
                    final HashMap<String, AMedia> map = new HashMap<String, AMedia>();
                    //para pasar al visor
                    map.put("pdf", fileContent);

                    org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                                "/venta/contenedorReporte.zul", null, map);
                    window.doModal();

                }

                if (parametrizar.getParImpAutomatico()) {
                    /*imprime la factura */
 /*para la factura*/
                    FileInputStream is1 = null;
                    is1 = new FileInputStream(reportFile + File.separator + "puntoventa.jasper");
                    JasperPrint jasperprint = JasperFillManager.fillReport(is1, parametros, con);
                    PrinterJob pj = PrinterJob.getPrinterJob();
                    // 
                    PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
                    /*ESCOGE LA IMPRESORA */
                    for (PrintService printService : services) {
                        System.out.println("printService.getName() " + printService.getName());
                        if (printService.getName().equals(parametrizar.getParNombreImpresora())) {

                            System.out.println("printService.getName() " + printService.getName());
//                    if (printService.getName().equals("Microsoft Print to PDF")) {
                            pj.setPrintService(printService);
                            //JasperPrintManager.printReport(print, false);
                        }
                    }

                    imprimirTecket(pj, jasperprint);
                }
                /*ESCOGE LA IMPRESORA */
//                for (PrintService printService : services) {
//                    if (printService.getName().equals("LR2000")) {
////                    if (printService.getName().equals("Microsoft Print to PDF")) {
//                        pj.setPrintService(printService);
//                        //JasperPrintManager.printReport(print, false);
//                    }
//                }
//                if (parametrizar.getParImpAutomatico()) {
//                     
//                }
            }
        } catch (PrinterException e) {
            System.out.println("Error PrinterException en generar el reporte " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("Error FileNotFoundException en generar el reporte " + e.getMessage());
        } catch (JRException e) {
            System.out.println("Error JRException en generar el reporte " + e.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
            if (emf != null) {
                emf.close();
                System.out.println("cerro entity");
            }
        }

    }

    private void imprimirTecket(PrinterJob pj, JasperPrint jasperprint) {
        try {
            /*REALIZA LA IMPRESION DE LA FACTURA*/
            JRPrintServiceExporter exporter;
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            printRequestAttributeSet.add(MediaSizeName.NA_LETTER);
            printRequestAttributeSet.add(new Copies(1));

            // these are deprecated
            exporter = new JRPrintServiceExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperprint);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, pj.getPrintService());
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, pj.getPrintService().getAttributes());
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);

            exporter.exportReport();

            /*REALIZAE EL CORTE DE PAPEL*/
 /*
            PrintService printService = PrinterOutputStream.getPrintServiceByName("LR2000");
            PrinterOutputStream printerOutputStream = new PrinterOutputStream(printService);
            EscPos escpos = new EscPos(printerOutputStream);
            escpos.writeLF("");
            escpos.writeLF("");
            escpos.feed(5);
            escpos.cut(EscPos.CutMode.PART);
            escpos.close();
             */
        } catch (IllegalArgumentException e) {
            System.out.println("ERRO AL IMPRIMIR LA FACTURA " + e.getMessage());
        } catch (JRException e) {
            System.out.println("ERRO AL IMPRIMIR LA FACTURA " + e.getMessage());
        }

    }

    public void reporteGeneralPdfMail(String pathPDF) throws JRException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, NamingException {
        EntityManager emf = HelperPersistencia.getEMF();

        try {
            emf.getTransaction().begin();
            con = emf.unwrap(Connection.class);
            if (!tipoVenta.equals("SINF")) {

                //  con = emf.unwrap(Connection.class);
                String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                            .getRealPath("/reportes");
                String reportPath = "";
//                con = ConexionReportes.Conexion.conexion();

                if (tipoVenta.equals("FACT")) {
                    reportPath = reportFile + File.separator + "puntoventa.jasper";
                } else if (tipoVenta.equals("PROF")) {
                    reportPath = reportFile + File.separator + "proforma.jasper";
                }

                Map<String, Object> parametros = new HashMap<String, Object>();

                //  parametros.put("codUsuario", String.valueOf(credentialLog.getAdUsuario().getCodigoUsuario()));
                parametros.put("numfactura", numeroFactura);

                if (con != null) {
                    System.out.println("Conexión Realizada Correctamenteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                }
                FileInputStream is = null;
                is = new FileInputStream(reportPath);

//                byte[] buf = JasperRunManager.runReportToPdf(is, parametros, con);
                JasperPrint print = JasperFillManager.fillReport(reportPath, parametros, con);
                JasperExportManager.exportReportToPdfFile(print, pathPDF);
            }
        } catch (Exception e) {
            System.out.println("Error en generar el reporte " + e.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
            if (emf != null) {
                emf.close();
                System.out.println("cerro entity");
            }
        }

    }

    @Command
    @NotifyChange({"cambio"})
    public void calcularCambio() {
        cambio = cobro.add(valorTotalCotizacion.negate());
        cambio.setScale(2, RoundingMode.FLOOR);
    }

    public Integer getNumeroProforma() {
        return numeroProforma;
    }

    public void setNumeroProforma(Integer numeroProforma) {
        this.numeroProforma = numeroProforma;
    }

    public List<FormaPago> getListaFormaPago() {
        return listaFormaPago;
    }

    public void setListaFormaPago(List<FormaPago> listaFormaPago) {
        this.listaFormaPago = listaFormaPago;
    }

    public FormaPago getFormaPagoSelected() {
        return formaPagoSelected;
    }

    public void setFormaPagoSelected(FormaPago formaPagoSelected) {
        this.formaPagoSelected = formaPagoSelected;
    }

    public Integer getIdFactuta() {
        return idFactuta;
    }

    public void setIdFactuta(Integer idFactuta) {
        this.idFactuta = idFactuta;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public BigDecimal getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(BigDecimal totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

    public String getClietipo() {
        return clietipo;
    }

    public void setClietipo(String clietipo) {
        this.clietipo = clietipo;
    }

    public List<Producto> getListaProductoCmb() {
        return listaProductoCmb;
    }

    public void setListaProductoCmb(List<Producto> listaProductoCmb) {
        this.listaProductoCmb = listaProductoCmb;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getValorTotalInicialVent() {
        return valorTotalInicialVent;
    }

    public void setValorTotalInicialVent(BigDecimal valorTotalInicialVent) {
        this.valorTotalInicialVent = valorTotalInicialVent;
    }

    public BigDecimal getDescuentoValorFinal() {
        return descuentoValorFinal;
    }

    public void setDescuentoValorFinal(BigDecimal descuentoValorFinal) {
        this.descuentoValorFinal = descuentoValorFinal;
    }

    /*crea la tabla de amortizacion*/
    @Command
    public void verDetallePago() throws JRException, IOException, NamingException, SQLException {
        try {
            final HashMap<String, Factura> map = new HashMap<String, Factura>();

            map.put("valor", factura);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/venta/detallepago.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    public BigDecimal getSaldoFacturas() {
        return saldoFacturas;
    }

    public void setSaldoFacturas(BigDecimal saldoFacturas) {
        this.saldoFacturas = saldoFacturas;
    }

    /*carga LAS NOTAS DE ENTREGA*/
    private void cargaNotaEntrega() {
        String clienteCedula = buscarCliente;
        listalistaNotaEntregaDatos = servicioFactura.findAllNotaEnt(clienteBuscado);
        setListaNotaEntregaModel(new ListModelList<Factura>(listalistaNotaEntregaDatos));
        ((ListModelList<Factura>) listaNotaEntregaModel).setMultiple(true);
    }

    @Command
    public void seleccionarRegistrosNotaEntrega() {
        seleccionNotaEntrega = ((ListModelList<Factura>) getListaNotaEntregaModel()).getSelection();
    }

    @Command
    @NotifyChange({"listaDetalleFacturaDAOMOdel", "subTotalCotizacion", "ivaCotizacion", "valorTotalCotizacion", "totalDescuento", "buscarNombreProd", "valorTotalInicialVent", "descuentoValorFinal", "subTotalBaseCero", "listaProducto", "totalItems"})
    public void seleccionarNotaEntrega() {
//        windowNotaEntrega.detach();
        boolean registroVacio = true;
        List<DetalleFacturaDAO> listaPedidoPost = listaDetalleFacturaDAOMOdel.getInnerList();

        for (DetalleFacturaDAO item : listaPedidoPost) {
            if (item.getProducto() == null) {

                listaDetalleFacturaDAOMOdel.remove(item);
                break;
            }
        }
        listaDetalleFacturaDAODatos = listaDetalleFacturaDAOMOdel.getInnerList();
        //ingresa un registro vacio

        for (DetalleFacturaDAO item : listaPedidoPost) {

            for (Factura fac : seleccionNotaEntrega) {

                if (item.getNumeroNtv().compareTo(fac.getFacNumNotaEntrega()) == 0) {
                    Clients.showNotification("La nota de entrega ya fue agregada", Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 3000, true);
                    return;
                }
            }
        }
        if (seleccionNotaEntrega != null) {
            for (Factura fac : seleccionNotaEntrega) {
                System.out.println("FAct " + fac.getIdFactura());

                List<DetalleFactura> detalleFac = servicioDetalleFactura.findDetalleForIdFac(fac.getIdFactura());
                DetalleFacturaDAO nuevoRegistro;

                for (DetalleFactura det : detalleFac) {
                    nuevoRegistro = new DetalleFacturaDAO();
                    nuevoRegistro.setCodigo(det.getIdProducto().getProdCodigo());
                    nuevoRegistro.setCantidad(det.getDetCantidad());
                    nuevoRegistro.setProducto(det.getIdProducto());
                    nuevoRegistro.setDescripcion(det.getDetDescripcion());
                    nuevoRegistro.setSubTotal(det.getDetSubtotal());
                    nuevoRegistro.setTotal(det.getDetTotal());
                    nuevoRegistro.setDetIva(det.getDetIva());
                    nuevoRegistro.setDetTotalconiva(det.getDetTotalconiva());
                    nuevoRegistro.setTipoVenta(det.getDetTipoVenta());
                    //valores con descuentos
                    nuevoRegistro.setSubTotalDescuento(det.getDetSubtotaldescuento());
                    nuevoRegistro.setDetTotaldescuento(det.getDetTotaldescuento());
                    nuevoRegistro.setDetPordescuento(det.getDetPordescuento());
                    nuevoRegistro.setDetValdescuento(det.getDetValdescuento());
                    nuevoRegistro.setDetTotalconivadescuento(det.getDetTotaldescuentoiva());
                    nuevoRegistro.setDetCantpordescuento(det.getDetCantpordescuento());
                    nuevoRegistro.setDetIvaDesc(det.getDetIva());
                    nuevoRegistro.setCodTipoVenta(det.getDetCodTipoVenta());
                    nuevoRegistro.setDetSubtotaldescuentoporcantidad(det.getDetSubtotaldescuentoporcantidad());
                    nuevoRegistro.setTotalInicial(det.getDetTotal());
                    nuevoRegistro.setNumeroNtv(fac.getFacNumNotaEntrega());
                    clietipo = det.getDetCodTipoVenta();
                    //calcularValores(nuevoRegistro);
                    listaDetalleFacturaDAODatos.add(nuevoRegistro);
//                    seleccionNotaEntregaProcesada=seleccionNotaEntrega;
//                    listaNotaEntregaModel.removeAll(seleccionNotaEntrega);
                }

            }
            getDetallefactura();
            calcularValoresTotales();
            tipoVentaAnterior = "NTE";
        }
    }

    public ListModelList<Factura> getListaNotaEntregaModel() {
        return listaNotaEntregaModel;
    }

    public void setListaNotaEntregaModel(ListModelList<Factura> listaNotaEntregaModel) {
        this.listaNotaEntregaModel = listaNotaEntregaModel;
    }

    public List<Factura> getListalistaNotaEntregaDatos() {
        return listalistaNotaEntregaDatos;
    }

    public void setListalistaNotaEntregaDatos(List<Factura> listalistaNotaEntregaDatos) {
        this.listalistaNotaEntregaDatos = listalistaNotaEntregaDatos;
    }

    public static Set<Factura> getSeleccionNotaEntrega() {
        return seleccionNotaEntrega;
    }

    public static void setSeleccionNotaEntrega(Set<Factura> seleccionNotaEntrega) {
        Facturar.seleccionNotaEntrega = seleccionNotaEntrega;
    }

    public BigDecimal getSubTotalBaseCero() {
        return subTotalBaseCero;
    }

    public void setSubTotalBaseCero(BigDecimal subTotalBaseCero) {
        this.subTotalBaseCero = subTotalBaseCero;
    }

    public Textbox getTxtBuscarNombre() {
        return txtBuscarNombre;
    }

    public void setTxtBuscarNombre(Textbox txtBuscarNombre) {
        this.txtBuscarNombre = txtBuscarNombre;
    }

    public String getTipoVentaAnterior() {
        return tipoVentaAnterior;
    }

    public void setTipoVentaAnterior(String tipoVentaAnterior) {
        this.tipoVentaAnterior = tipoVentaAnterior;
    }

    public String getNumeroFacturaText() {
        return numeroFacturaText;
    }

    public void setNumeroFacturaText(String numeroFacturaText) {
        this.numeroFacturaText = numeroFacturaText;
    }

    public Integer getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(Integer numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public String getNumeroGuiaText() {
        return numeroGuiaText;
    }

    public void setNumeroGuiaText(String numeroGuiaText) {
        this.numeroGuiaText = numeroGuiaText;
    }

    public Transportista getTransportista() {
        return transportista;
    }

    public void setTransportista(Transportista transportista) {
        this.transportista = transportista;
    }

    public String getNumeroPlaca() {
        return numeroPlaca;
    }

    public void setNumeroPlaca(String numeroPlaca) {
        this.numeroPlaca = numeroPlaca;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public List<Transportista> getListaTransportistas() {
        return listaTransportistas;
    }

    public void setListaTransportistas(List<Transportista> listaTransportistas) {
        this.listaTransportistas = listaTransportistas;
    }

    public Date getIncioTraslado() {
        return incioTraslado;
    }

    public void setIncioTraslado(Date incioTraslado) {
        this.incioTraslado = incioTraslado;
    }

    public Date getFinTraslado() {
        return finTraslado;
    }

    public void setFinTraslado(Date finTraslado) {
        this.finTraslado = finTraslado;
    }

    public String getMotivoGuia() {
        return motivoGuia;
    }

    public void setMotivoGuia(String motivoGuia) {
        this.motivoGuia = motivoGuia;
    }

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }

    public String getLlegada() {
        return llegada;
    }

    public void setLlegada(String llegada) {
        this.llegada = llegada;
    }

    public BigDecimal getSubsidioTotal() {
        return subsidioTotal;
    }

    public void setSubsidioTotal(BigDecimal subsidioTotal) {
        this.subsidioTotal = subsidioTotal;
    }

    public String getFacConSinGuia() {
        return facConSinGuia;
    }

    public void setFacConSinGuia(String facConSinGuia) {
        this.facConSinGuia = facConSinGuia;
    }

    public String getFacplazo() {
        return facplazo;
    }

    public void setFacplazo(String facplazo) {
        this.facplazo = facplazo;
    }

    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }

    @Command
    public void datosMoto(@BindingParam("valor") DetalleFacturaDAO valor) throws JRException, IOException, NamingException, SQLException {
        try {

            final HashMap<String, DetalleFacturaDAO> map = new HashMap<String, DetalleFacturaDAO>();

            map.put("valor", valor);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/modificar/motocicleta.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    public List<Kardex> getListaKardexProducto() {
        return listaKardexProducto;
    }

    public void setListaKardexProducto(List<Kardex> listaKardexProducto) {
        this.listaKardexProducto = listaKardexProducto;
    }

    public static Boolean getValidaBorrado() {
        return validaBorrado;
    }

    public static void setValidaBorrado(Boolean validaBorrado) {
        Facturar.validaBorrado = validaBorrado;
    }

    public String getUsuLoginVal() {
        return usuLoginVal;
    }

    public void setUsuLoginVal(String usuLoginVal) {
        this.usuLoginVal = usuLoginVal;
    }

    public String getUsuPasswordVal() {
        return usuPasswordVal;
    }

    public void setUsuPasswordVal(String usuPasswordVal) {
        this.usuPasswordVal = usuPasswordVal;
    }

    /*CAMBIAR DE PRECIO */
    @Command
    @NotifyChange({"listaDetalleFacturaDAOMOdel", "subTotalCotizacion", "ivaCotizacion", "valorTotalCotizacion", "totalDescuento", "valorTotalInicialVent", "descuentoValorFinal", "subTotalBaseCero"})
    public void cambioPrecio(@BindingParam("valor") DetalleFacturaDAO valor) {
        if (parametrizar.getParNumRegistrosFactura().intValue() <= listaDetalleFacturaDAOMOdel.size()) {
            Clients.showNotification("Numero de registros permitidos, imprima y genere otra factura", Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
            return;
        }

        if (!clienteBuscado.getCliCedula().equals("")) {
            ParamFactura paramFactura = new ParamFactura();
            paramFactura.setCodigo(valor.getCodigo());
            paramFactura.setBusqueda("cambio");
            final HashMap<String, ParamFactura> map = new HashMap<String, ParamFactura>();
            map.put("valor", paramFactura);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/venta/cambioprecio.zul", null, map);
            window.doModal();
            productoBuscado = valor.getProducto();
            if (productoBuscado == null) {
                return;
            }
            //verifica el kardex
            if (parametrizar.getParActivaKardex() && productoBuscado.getProdEsproducto()) {
                Kardex kardex = servicioKardex.FindALlKardexs(productoBuscado);
                if (kardex.getKarTotal().intValue() < 1) {
                    Clients.showNotification("Verifique el stock del producto cuenta con " + kardex.getKarTotal().intValue() + " en estock",
                                Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
                    agregarRegistroVacio();
                    return;
                }
            }

            if (productoBuscado != null) {
                valor.setCantidad(valor.getCantidad());
                valor.setProducto(productoBuscado);
                valor.setDescripcion(productoBuscado.getProdNombre());
                valor.setDetPordescuento(DESCUENTOGENERAL);
                valor.setCodigo(productoBuscado.getProdCodigo());
                valor.setEsProducto(productoBuscado.getProdEsproducto());

                BigDecimal costVentaTipoCliente = BigDecimal.ZERO;
                BigDecimal costVentaTipoClienteInicial = BigDecimal.ZERO;
                String tipoVenta = TIPOPRECIO;
                if (clienteBuscado.getClietipo() == 0) {
                    tipoVenta = "NORMAL";
                    if (TIPOPRECIO.equals("NORMAL")) {
                        costVentaTipoClienteInicial = productoBuscado.getPordCostoVentaFinal();
                        costVentaTipoCliente = productoBuscado.getPordCostoVentaFinal();
                    } else if (TIPOPRECIO.equals("PREFERENCIAL 1")) {
                        tipoVenta = "PREFERENCIAL 1";
                        costVentaTipoClienteInicial = productoBuscado.getProdCostoPreferencial();
                        costVentaTipoCliente = productoBuscado.getProdCostoPreferencial();
                    } else if (TIPOPRECIO.equals("PREFERENCIAL 2")) {
                        tipoVenta = "PREFERENCIAL 2";
                        costVentaTipoClienteInicial = productoBuscado.getProdCostoPreferencialDos();
                        costVentaTipoCliente = productoBuscado.getProdCostoPreferencialDos();
                    } else if (TIPOPRECIO.equals("PREFERENCIAL 3")) {
                        tipoVenta = "PREFERENCIAL 3";
                        costVentaTipoClienteInicial = productoBuscado.getProdCostoPreferencialTres();
                        costVentaTipoCliente = productoBuscado.getProdCostoPreferencialTres();
                    }
                    /*OBTIENE LOS VALORES LUEGO DE LA BUSQUEDA*/
                    //        BigDecimal factorIva = (parametrizar.getParIva().divide(BigDecimal.valueOf(100.0)));
                    BigDecimal factorIva = (valor.getProducto().getProdIva().divide(BigDecimal.valueOf(100.0)));
                    BigDecimal factorSacarSubtotal = (factorIva.add(BigDecimal.ONE));

                    valor.setTotalInicial(costVentaTipoClienteInicial);
                    BigDecimal porcentajeDesc = valor.getDetPordescuento().divide(BigDecimal.valueOf(100.0), 5, RoundingMode.FLOOR);
                    BigDecimal valorDescuentoIva = costVentaTipoCliente.multiply(porcentajeDesc);
                    //valor unitario con descuento ioncluido iva
                    BigDecimal valorTotalIvaDesc = costVentaTipoCliente.subtract(valorDescuentoIva);
                    //valor unit sin iva sin descuento
                    BigDecimal subTotal
                                = costVentaTipoCliente.divide(factorSacarSubtotal, 5, RoundingMode.FLOOR);
                    valor.setSubTotal(subTotal);
                    //valor unitario sin iva con descuento
                    BigDecimal subTotalDescuento
                                = valorTotalIvaDesc.divide(factorSacarSubtotal, 5, RoundingMode.FLOOR);
                    valor.setSubTotalDescuento(subTotalDescuento);
                    //valor del descuento
                    BigDecimal valorDescuento = valor.getSubTotal().subtract(valor.getSubTotalDescuento());
                    valor.setDetValdescuento(valorDescuento);
                    BigDecimal valorIva = subTotal.multiply(factorIva).multiply(valor.getCantidad());
//                valor.setDetIva(valorIva);
                    //valor del iva con descuento
                    BigDecimal valorIvaDesc = subTotalDescuento.multiply(factorIva).multiply(valor.getCantidad());
                    valor.setDetIva(valorIvaDesc);
                    //valor total sin decuento y con iva
                    valor.setTotal(costVentaTipoCliente);
                    //valor total con decuento y con iva
                    valor.setDetTotaldescuento(valorTotalIvaDesc);
                    valor.setDetTotalconiva(valor.getCantidad().multiply(costVentaTipoCliente));
                    valor.setDetTotalconivadescuento(valor.getCantidad().multiply(valorTotalIvaDesc));
                    valor.setDetCantpordescuento(valorDescuento.multiply(valor.getCantidad()));
                    //cantidad por subtotal con descuento
                    valor.setDetSubtotaldescuentoporcantidad(subTotalDescuento.multiply(valor.getCantidad()));
                    valor.setTipoVenta("NORMAL");
                    valor.setCodTipoVenta(clietipo);
                }

                //ingresa un registro vacio
                boolean registroVacio = true;
                List<DetalleFacturaDAO> listaPedidoPost = listaDetalleFacturaDAOMOdel.getInnerList();

                for (DetalleFacturaDAO item : listaPedidoPost) {
                    if (item.getProducto() == null) {
                        registroVacio = false;
                        break;
                    }
                }

                System.out.println("existe un vacio " + registroVacio);
                if (registroVacio) {
                    DetalleFacturaDAO nuevoRegistroPost = new DetalleFacturaDAO();
                    nuevoRegistroPost.setProducto(productoBuscado);
                    nuevoRegistroPost.setCantidad(BigDecimal.ZERO);
                    nuevoRegistroPost.setSubTotal(BigDecimal.ZERO);
                    nuevoRegistroPost.setDetIva(BigDecimal.ZERO);
                    nuevoRegistroPost.setDetTotalconiva(BigDecimal.ZERO);
                    nuevoRegistroPost.setDescripcion("");
                    nuevoRegistroPost.setProducto(null);
                    ((ListModelList<DetalleFacturaDAO>) listaDetalleFacturaDAOMOdel).add(nuevoRegistroPost);
                }
            }

            calcularValoresTotales();
            codigoBusqueda = "";
        } else {
            Messagebox.show("Verifique el cliente", "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    public static String getTIPOPRECIO() {
        return TIPOPRECIO;
    }

    public static void setTIPOPRECIO(String TIPOPRECIO) {
        Facturar.TIPOPRECIO = TIPOPRECIO;
    }

    public Producto getPRODUCTOCAMBIO() {
        return PRODUCTOCAMBIO;
    }

    public void setPRODUCTOCAMBIO(Producto PRODUCTOCAMBIO) {
        this.PRODUCTOCAMBIO = PRODUCTOCAMBIO;
    }

    @Command
    @NotifyChange("clienteBuscado")
    public void seleccionPrecioNorm() {
        System.out.println("TIPOPRECIO NORMAL");
        TIPOPRECIO = "NORMAL";
        windowCambioPrecio.detach();

    }

    @Command
    @NotifyChange("clienteBuscado")
    public void seleccionPrecioPref1() {
        System.out.println("TIPOPRECIO PREFERENCIAL 1");
        TIPOPRECIO = "PREFERENCIAL 1";
        windowCambioPrecio.detach();

    }

    @Command
    @NotifyChange("clienteBuscado")
    public void seleccionPrecioPref2() {
        System.out.println("TIPOPRECIO PREFERENCIAL 2");
        TIPOPRECIO = "PREFERENCIAL 2";
        windowCambioPrecio.detach();

    }

    @Command
    @NotifyChange("clienteBuscado")
    public void seleccionPrecioPref3() {
        System.out.println("TIPOPRECIO PREFERENCIAL 3");
        TIPOPRECIO = "PREFERENCIAL 3";
        windowCambioPrecio.detach();

    }

//    medidor 
    @Command
    @NotifyChange({"clienteBuscado", "numeroMedidor", "clienteBuscado", "listaDatosLectura", "listaNotaEntregaModel"})
    public void buscarLecturasPendientes(@BindingParam("valor") Medidor valor) {

        medidorEncontrado = servicioMedidor.findMedNumero(valor.getMedNumero());
        if (medidorEncontrado != null) {
            clienteBuscado = servicioCliente.FindClienteForCedula(medidorEncontrado.getIdPredio().getIdPropietario().getPorpCedula());
            listaDatosLectura = servicioLectura.finbByMedidor(medidorEncontrado);
            cargaNotaEntrega();
        } else {
            Clients.showNotification("No se encontro el medidor...",
                        Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 1000, true);
        }
    }

    @Command
    @NotifyChange({"clienteBuscado", "numeroMedidor", "clienteBuscado", "listaDatosLectura", "listaNotaEntregaModel"})
    public void eliminarNotaEntrega(@BindingParam("valor") Factura valor) {

        if (Messagebox.show("¿Esta seguro de eliminar la nota de entrega?", "Question", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION) == Messagebox.OK) {
            servicioFactura.eliminar(valor);
            cargaNotaEntrega();
        }
    }

    public String getNumeroMedidor() {
        return numeroMedidor;
    }

    public void setNumeroMedidor(String numeroMedidor) {
        this.numeroMedidor = numeroMedidor;
    }

    public Medidor getMedidorEncontrado() {
        return medidorEncontrado;
    }

    public void setMedidorEncontrado(Medidor medidorEncontrado) {
        this.medidorEncontrado = medidorEncontrado;
    }

    public List<Lectura> getListaDatosLectura() {
        return listaDatosLectura;
    }

    public void setListaDatosLectura(List<Lectura> listaDatosLectura) {
        this.listaDatosLectura = listaDatosLectura;
    }

    public String getBuscarMedidor() {
        return buscarMedidor;
    }

    public void setBuscarMedidor(String buscarMedidor) {
        this.buscarMedidor = buscarMedidor;
    }

    public List<Medidor> getListMedidores() {
        return listMedidores;
    }

    public void setListMedidores(List<Medidor> listMedidores) {
        this.listMedidores = listMedidores;
    }

    public String getBuscarMedidorNumero() {
        return buscarMedidorNumero;
    }

    public void setBuscarMedidorNumero(String buscarMedidorNumero) {
        this.buscarMedidorNumero = buscarMedidorNumero;
    }

    public boolean isMultaCobrada() {
        return multaCobrada;
    }

    public void setMultaCobrada(boolean multaCobrada) {
        this.multaCobrada = multaCobrada;
    }

    @Command
    public void verHistoricoFac(@BindingParam("valor") Medidor valor) {

        try {
            final HashMap<String, Medidor> map = new HashMap<String, Medidor>();
            map.put("valor", valor);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/reportevistas/verhistorico.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Command
    @NotifyChange({"listaDatos", "buscar", "listaDatosLectura"})
    public void cambiarestado(@BindingParam("valor") Lectura valor) {

        if (Messagebox.show("Desea cambiar a estado pagado", "Question", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION) == Messagebox.OK) {
            if (valor.getLecPagada().equals("S")) {
                valor.setLecPagada("N");
                servicioLectura.modificar(valor);
            } else {
                valor.setLecPagada("S");
                servicioLectura.modificar(valor);
            }

            medidorEncontrado = servicioMedidor.findMedNumero(valor.getIdMedidor().getMedNumero());
            if (medidorEncontrado != null) {
                clienteBuscado = servicioCliente.FindClienteForCedula(medidorEncontrado.getIdPredio().getIdPropietario().getPorpCedula());
                listaDatosLectura = servicioLectura.finbByMedidor(medidorEncontrado);
            } else {
                Clients.showNotification("No se encontro el medidor...",
                            Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 1000, true);
            }
        } else {
            Clients.showNotification("Solicitud cancelada",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 1000, true);
        }
    }
}
