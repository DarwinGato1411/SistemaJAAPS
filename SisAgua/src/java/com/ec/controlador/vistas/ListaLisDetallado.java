/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.vistas;

import com.ec.entidad.Factura;
import com.ec.entidad.contabilidad.AcSubCuenta;
import com.ec.entidad.contabilidad.ComprobanteDiario;
import com.ec.entidad.contabilidad.CuSubCuenta;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.HelperPersistencia;
import com.ec.servicio.ServicioAcumuladoVentas;
import com.ec.servicio.contabilidad.ServicioAcSubcuenta;
import com.ec.servicio.contabilidad.ServicioSubCuenta;
import com.ec.untilitario.ArchivoUtils;
import com.ec.untilitario.ModeloAcumuladoDiaUsuario;
import com.ec.vista.servicios.ServicioAcumuladoDiarioUsuario;
import com.ec.vista.servicios.ServicioComprobanteDiario;
import com.ec.vista.servicios.ServicioListadoDetallado;
import com.ec.vista.servicios.ServicioListadoDetalladoOrdenado;
import com.ec.vista.servicios.ServicioListadoItems;
import com.ec.vista.servicios.ServicioMovimientoCartera;
import com.ec.vistas.Acumuladopordia;
import com.ec.vistas.ListadoDetallado;
import com.ec.vistas.ListadoDetalladoOrdenado;
import com.ec.vistas.ListadoItems;
import com.ec.vistas.MovimientoCartera;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.activation.MimetypesFileTypeMap;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Filedownload;

/**
 *
 * @author HC
 */
public class ListaLisDetallado {

    ServicioListadoDetallado servicioListadoDetallado = new ServicioListadoDetallado();

    private Date inicio = new Date();
    private Date fin = new Date();
    private Date fecha = new Date();
    // String sDate1="29/12/2022"; 
    private BigDecimal totalVenta = BigDecimal.ZERO;

    private List<ListadoDetallado> listaListDetallado = new ArrayList<ListadoDetallado>();

    ServicioListadoItems servicioListadoItems = new ServicioListadoItems();
    private List<ListadoItems> listaListItems = new ArrayList<ListadoItems>();

    ServicioSubCuenta servicioSubCuenta2 = new ServicioSubCuenta();
    private List<CuSubCuenta> listaCuSubCuenta2 = new ArrayList<CuSubCuenta>();

    ServicioListadoDetalladoOrdenado servicioDetalladoOrdenado = new ServicioListadoDetalladoOrdenado();
    private List<ListadoDetalladoOrdenado> listaListItemsOrd = new ArrayList<ListadoDetalladoOrdenado>();

    ServicioMovimientoCartera servicioMovimientoCartera = new ServicioMovimientoCartera();
    private List<MovimientoCartera> listaMovCartera = new ArrayList<MovimientoCartera>();

    private List<Acumuladopordia> listaAcumuladopordias = new ArrayList<Acumuladopordia>();
    ServicioAcumuladoVentas servicioAcumuladoVentas = new ServicioAcumuladoVentas();
    private Date fechainicioDiaria = new Date();

    ServicioAcumuladoDiarioUsuario servicioAcumuladoDiarioUsuario = new ServicioAcumuladoDiarioUsuario();
    UserCredential credential = new UserCredential();
    private BigDecimal totFactura = BigDecimal.ZERO;

    ServicioComprobanteDiario servicioComprobanteDiario = new ServicioComprobanteDiario();
    private List<ComprobanteDiario> listaComprobanteDiario = new ArrayList<ComprobanteDiario>();

    private String buscar = "";

    public ListaLisDetallado() {

        Calendar calendar = Calendar.getInstance(); //obtiene la fecha de hoy 
        calendar.add(Calendar.DATE, -6); //el -3 indica que se le restaran 3 dias 
        inicio = calendar.getTime();

        //fechainicioDiaria.setDate(-7);
        consultaCuSubCuentas();
        consultaDetalle();
        consultaItems();
        fechainicioDiaria = calendar.getTime();
        consultaVentasDiarias();
        comprobanteDiario();

    }

    @Command
    @NotifyChange({"listaListItemsOrd", "inicio", "fin"})
    public void buscarListadoDetallado() {

        consultaDetalle();

    }

    @Command
    @NotifyChange({"listaAcumuladopordias", "fechainicioDiaria", "listaComprobanteDiario"})
    public void buscarDiaria() {
        ServicioSubCuenta servicioSubcuenta = new ServicioSubCuenta();
        ServicioAcSubcuenta servicioAcSubcuenta = new ServicioAcSubcuenta();

        Session sess = Sessions.getCurrent();
        UserCredential cre = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        credential = cre;
        if (servicioAcumuladoDiarioUsuario.findCierrePorUsuario(fechainicioDiaria, credential.getUsuarioSistema()).size() > 0) {
            ModeloAcumuladoDiaUsuario acumuladoDiaUsuario = servicioAcumuladoDiarioUsuario.findCierrePorUsuario(fechainicioDiaria, credential.getUsuarioSistema()).get(0);
            totFactura = ArchivoUtils.redondearDecimales(acumuladoDiaUsuario.getValorFacturas(), 2);
        } else {
            totFactura = BigDecimal.ZERO;
        }

        CuSubCuenta valorCajaGeneral = servicioSubcuenta.findByNombre("CAJA GENERAL").get(0);
        BigDecimal valorTotal = totFactura;
        AcSubCuenta acCajaGeneral = new AcSubCuenta();

        valorCajaGeneral.setSubcTotal(valorTotal.add(valorTotal));
        servicioSubcuenta.modificar(valorCajaGeneral);
        acCajaGeneral.setIdSubCuenta(valorCajaGeneral);
        acCajaGeneral.setDebe(valorTotal);
        acCajaGeneral.setFechaAcSubcuenta(fecha);
        servicioAcSubcuenta.crear(acCajaGeneral);

        consultaVentasDiarias();
        comprobanteDiario();

    }

    private void consultaDetalle() {
        totalVenta = BigDecimal.ZERO;
        listaListItemsOrd = servicioDetalladoOrdenado.findByMes(inicio, fin, buscar);

        /*Calculo el total*/
        for (ListadoDetalladoOrdenado item : listaListItemsOrd) {
            totalVenta = totalVenta.add(item.getFacTotal());
        }
        /*coloco el porcentaje*/
        for (ListadoDetalladoOrdenado item : listaListItemsOrd) {
            BigDecimal itemporcient = BigDecimal.valueOf(100.0).multiply(item.getFacTotal());
            // BigDecimal porcentaje = itemporcient.divide(totalVenta, 4, RoundingMode.FLOOR);

            //item.setPorcentaje(ArchivoUtils.redondearDecimales(porcentaje, 2));
        }
    }

    @Command
    @NotifyChange({"listaMovCartera", "inicio", "fin"})
    public void buscarmovcartera() {

        consultaDetalleMovCartera();

    }

    private void consultaDetalleMovCartera() {
        totalVenta = BigDecimal.ZERO;
        listaMovCartera = servicioMovimientoCartera.findByMes(inicio, fin);

        /*Calculo el total*/
        for (ListadoDetalladoOrdenado item : listaListItemsOrd) {
            totalVenta = totalVenta.add(item.getFacTotal());
        }
        /*coloco el porcentaje*/
        for (ListadoDetalladoOrdenado item : listaListItemsOrd) {
            BigDecimal itemporcient = BigDecimal.valueOf(100.0).multiply(item.getFacTotal());
            // BigDecimal porcentaje = itemporcient.divide(totalVenta, 4, RoundingMode.FLOOR);

            //item.setPorcentaje(ArchivoUtils.redondearDecimales(porcentaje, 2));
        }
    }

    @Command
    @NotifyChange({"listaCuSubCuenta2"})
    public void buscarlistaCuSubCuenta2() {

        consultaCuSubCuentas();

    }
    //  @AfterCompose
/*    public void afterCompose(@ExecutionArgParam("valor") Producto producto, @ContextParam(ContextType.VIEW) Component view) throws ParseException {
        Selectors.wireComponents(view, this, false);
       // fecha= new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);  
        Session sess = Sessions.getCurrent();
        UserCredential cre = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        credential = cre;
            if (servicioAcumuladoDiarioUsuario.findCierrePorUsuario(fecha, credential.getUsuarioSistema()).size() > 0) {
            ModeloAcumuladoDiaUsuario acumuladoDiaUsuario = servicioAcumuladoDiarioUsuario.findCierrePorUsuario(fecha, credential.getUsuarioSistema()).get(0);
              totFactura = ArchivoUtils.redondearDecimales(acumuladoDiaUsuario.getValorFacturas(), 2);
         } else {
              totFactura = BigDecimal.ZERO;
          }

    }*/
 /*   @Command
    @NotifyChange({"listaAcumuladopordias","fechainicioDiaria"})
    public void cambioFecha() {

    }*/

    @Command
    @NotifyChange({"listaComprobanteDiario"})
    public void guardar() throws ParseException {

        ServicioSubCuenta servicioSubcuenta = new ServicioSubCuenta();
        ServicioAcSubcuenta servicioAcSubcuenta = new ServicioAcSubcuenta();

        Session sess = Sessions.getCurrent();
        UserCredential cre = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        credential = cre;
        if (servicioAcumuladoDiarioUsuario.findCierrePorUsuario(fechainicioDiaria, credential.getUsuarioSistema()).size() > 0) {
            ModeloAcumuladoDiaUsuario acumuladoDiaUsuario = servicioAcumuladoDiarioUsuario.findCierrePorUsuario(fechainicioDiaria, credential.getUsuarioSistema()).get(0);
            totFactura = ArchivoUtils.redondearDecimales(acumuladoDiaUsuario.getValorFacturas(), 2);
        } else {
            totFactura = BigDecimal.ZERO;
        }

        CuSubCuenta valorCajaGeneral = servicioSubcuenta.findByNombre("CAJA GENERAL").get(0);
        BigDecimal valorTotal = totFactura;
        AcSubCuenta acCajaGeneral = new AcSubCuenta();

        valorCajaGeneral.setSubcTotal(valorTotal.add(valorTotal));
        servicioSubcuenta.modificar(valorCajaGeneral);
        acCajaGeneral.setIdSubCuenta(valorCajaGeneral);
        acCajaGeneral.setDebe(valorTotal);
        acCajaGeneral.setFechaAcSubcuenta(fecha);
        servicioAcSubcuenta.crear(acCajaGeneral);
        /*   try {
                System.out.println("cierreCaja " + cierreCaja.getIdCierre());
                DispararReporte.reporteCierrecaja(cierreCaja.getIdCierre());
            } catch (JRException ex) {
                Logger.getLogger(CierreCajaVm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CierreCajaVm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NamingException ex) {
                Logger.getLogger(CierreCajaVm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(CierreCajaVm.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        windowCierre.detach();*/
        comprobanteDiario();

    }

    private void consultaCuSubCuentas() {

        listaCuSubCuenta2 = servicioSubCuenta2.listadoTotal();

    }

    private void comprobanteDiario() {

        listaComprobanteDiario = servicioComprobanteDiario.findByFecha(fechainicioDiaria);

    }

    private void consultaVentasDiarias() {
        listaAcumuladopordias = servicioAcumuladoVentas.findAcumuladoventasdiariatotal(fechainicioDiaria);
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

    public List<Acumuladopordia> getListaAcumuladopordias() {
        return listaAcumuladopordias;
    }

    public void setListaAcumuladopordias(List<Acumuladopordia> listaAcumuladopordias) {
        this.listaAcumuladopordias = listaAcumuladopordias;
    }

    public ServicioAcumuladoVentas getServicioAcumuladoVentas() {
        return servicioAcumuladoVentas;
    }

    public void setServicioAcumuladoVentas(ServicioAcumuladoVentas servicioAcumuladoVentas) {
        this.servicioAcumuladoVentas = servicioAcumuladoVentas;
    }

    public Date getFechainicioDiaria() {
        return fechainicioDiaria;
    }

    public void setFechainicioDiaria(Date fechainicioDiaria) {
        this.fechainicioDiaria = fechainicioDiaria;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public ServicioMovimientoCartera getServicioMovimientoCartera() {
        return servicioMovimientoCartera;
    }

    public void setServicioMovimientoCartera(ServicioMovimientoCartera servicioMovimientoCartera) {
        this.servicioMovimientoCartera = servicioMovimientoCartera;
    }

    public List<MovimientoCartera> getListaMovCartera() {
        return listaMovCartera;
    }

    public void setListaMovCartera(List<MovimientoCartera> listaMovCartera) {
        this.listaMovCartera = listaMovCartera;
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

    public ServicioListadoDetalladoOrdenado getServicioDetalladoOrdenado() {
        return servicioDetalladoOrdenado;
    }

    public void setServicioDetalladoOrdenado(ServicioListadoDetalladoOrdenado servicioDetalladoOrdenado) {
        this.servicioDetalladoOrdenado = servicioDetalladoOrdenado;
    }

    public List<ListadoDetalladoOrdenado> getListaListItemsOrd() {
        return listaListItemsOrd;
    }

    public void setListaListItemsOrd(List<ListadoDetalladoOrdenado> listaListItemsOrd) {
        this.listaListItemsOrd = listaListItemsOrd;
    }

    //reporte listado detallado ordenado
    AMedia fileContent = null;
    Connection con = null;

    @Command
    public void llamarReporteDetallado() throws JRException, IOException, NamingException, SQLException {
        reporteGeneral();
    }

    public void reporteGeneral() throws JRException, IOException, NamingException, SQLException {

        EntityManager emf = HelperPersistencia.getEMF();

        try {
            emf.getTransaction().begin();
            con = emf.unwrap(Connection.class);

            String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                        .getRealPath("/reportes");
            String reportPath = "";

            reportPath = reportFile + File.separator + "listadodetallado.jasper";

            Map<String, Object> parametros = new HashMap<String, Object>();

            //  parametros.put("codUsuario", String.valueOf(credentialLog.getAdUsuario().getCodigoUsuario()));
            parametros.put("inicio", inicio);
            parametros.put("fin", fin);

            if (con != null) {
                System.out.println("Conexión Realizada Correctamenteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            }
            FileInputStream is = null;
            is = new FileInputStream(reportPath);

            byte[] buf = JasperRunManager.runReportToPdf(is, parametros, con);
            InputStream mediais = new ByteArrayInputStream(buf);
            AMedia amedia = new AMedia("Reporte", "pdf", "application/pdf", mediais);
            fileContent = amedia;
            final HashMap<String, AMedia> map = new HashMap<String, AMedia>();
//para pasar al visor
            map.put("pdf", fileContent);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/venta/contenedorReporte.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            System.out.println("ERROR EL PRESENTAR EL REPORTE " + e.getMessage());
        } finally {
            if (emf != null) {
                emf.getTransaction().commit();
            }

        }

    }

    //reporte diario
    @Command
    public void llamarReporteDiario() throws JRException, IOException, NamingException, SQLException {
        reporteDiario();
    }

    public void reporteDiario() throws JRException, IOException, NamingException, SQLException {

        EntityManager emf = HelperPersistencia.getEMF();

        try {
            emf.getTransaction().begin();
            con = emf.unwrap(Connection.class);

            String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                        .getRealPath("/reportes");
            String reportPath = "";

            reportPath = reportFile + File.separator + "reportediario.jasper";

            Map<String, Object> parametros = new HashMap<String, Object>();

            //  parametros.put("codUsuario", String.valueOf(credentialLog.getAdUsuario().getCodigoUsuario()));
            parametros.put("inicio", fechainicioDiaria);

            if (con != null) {
                System.out.println("Conexión Realizada Correctamenteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            }
            FileInputStream is = null;
            is = new FileInputStream(reportPath);

            byte[] buf = JasperRunManager.runReportToPdf(is, parametros, con);
            InputStream mediais = new ByteArrayInputStream(buf);
            AMedia amedia = new AMedia("Reporte", "pdf", "application/pdf", mediais);
            fileContent = amedia;
            final HashMap<String, AMedia> map = new HashMap<String, AMedia>();
//para pasar al visor
            map.put("pdf", fileContent);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/venta/contenedorReporte.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            System.out.println("ERROR EL PRESENTAR EL REPORTE " + e.getMessage());
        } finally {
            if (emf != null) {
                emf.getTransaction().commit();
            }

        }

    }

    //movimiento de cartera 
    //reporte diario
    @Command
    public void llamarReportMovCartera() throws JRException, IOException, NamingException, SQLException {
        reporteMovCartera();
    }

    public void reporteMovCartera() throws JRException, IOException, NamingException, SQLException {

        EntityManager emf = HelperPersistencia.getEMF();

        try {
            emf.getTransaction().begin();
            con = emf.unwrap(Connection.class);

            String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                        .getRealPath("/reportes");
            String reportPath = "";

            reportPath = reportFile + File.separator + "movimientocartera.jasper";

            Map<String, Object> parametros = new HashMap<String, Object>();

            //  parametros.put("codUsuario", String.valueOf(credentialLog.getAdUsuario().getCodigoUsuario()));
            parametros.put("inicio", inicio);
            parametros.put("fin", fin);

            if (con != null) {
                System.out.println("Conexión Realizada Correctamenteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            }
            FileInputStream is = null;
            is = new FileInputStream(reportPath);

            byte[] buf = JasperRunManager.runReportToPdf(is, parametros, con);
            InputStream mediais = new ByteArrayInputStream(buf);
            AMedia amedia = new AMedia("Reporte", "pdf", "application/pdf", mediais);
            fileContent = amedia;
            final HashMap<String, AMedia> map = new HashMap<String, AMedia>();
//para pasar al visor
            map.put("pdf", fileContent);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/venta/contenedorReporte.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            System.out.println("ERROR EL PRESENTAR EL REPORTE " + e.getMessage());
        } finally {
            if (emf != null) {
                emf.getTransaction().commit();
            }

        }

    }

    public List<ComprobanteDiario> getListaComprobanteDiario() {
        return listaComprobanteDiario;
    }

    public void setListaComprobanteDiario(List<ComprobanteDiario> listaComprobanteDiario) {
        this.listaComprobanteDiario = listaComprobanteDiario;
    }

    /*EXPORTAR A EXCEL
    lstFacturas
     */
    @Command
    public void exportListboxToExcel() throws Exception {
        try {
            File dosfile = new File(exportarExcel());
            if (dosfile.exists()) {
                FileInputStream inputStream = new FileInputStream(dosfile);
                Filedownload.save(inputStream, new MimetypesFileTypeMap().getContentType(dosfile), dosfile.getName());
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR AL DESCARGAR EL ARCHIVO" + e.getMessage());
        }
    }

    private String exportarExcel() throws FileNotFoundException, IOException, javax.mail.internet.ParseException {
        String directorioReportes = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/reportes");

        Date date = new Date();
        SimpleDateFormat fhora = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sm = new SimpleDateFormat("yyy-MM-dd");
        String strDate = sm.format(date);

        String pathSalida = directorioReportes + File.separator + "Listado_detallado.xls";
        System.out.println("Direccion del reporte  " + pathSalida);
        try {
            int j = 0;
            File archivoXLS = new File(pathSalida);
            if (archivoXLS.exists()) {
                archivoXLS.delete();
            }
            archivoXLS.createNewFile();
            FileOutputStream archivo = new FileOutputStream(archivoXLS);
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet s = wb.createSheet("Listado detallado");

            HSSFFont fuente = wb.createFont();
            fuente.setBoldweight((short) 700);
            HSSFCellStyle estiloCelda = wb.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment((short) 2);
            estiloCelda.setFont(fuente);

            HSSFCellStyle estiloCeldaInterna = wb.createCellStyle();
            estiloCeldaInterna.setWrapText(true);
            estiloCeldaInterna.setAlignment((short) 5);
            estiloCeldaInterna.setFont(fuente);

            HSSFCellStyle estiloCelda1 = wb.createCellStyle();
            estiloCelda1.setWrapText(true);
            estiloCelda1.setFont(fuente);

            HSSFRow r = null;

            HSSFCell c = null;
            r = s.createRow(0);

            HSSFCell chfee = r.createCell(j++);
            chfee.setCellValue(new HSSFRichTextString("Cobro N°"));
            chfee.setCellStyle(estiloCelda);

            HSSFCell chfe = r.createCell(j++);
            chfe.setCellValue(new HSSFRichTextString("Factura"));
            chfe.setCellStyle(estiloCelda);

            HSSFCell ch1 = r.createCell(j++);
            ch1.setCellValue(new HSSFRichTextString("F Emision"));
            ch1.setCellStyle(estiloCelda);

            HSSFCell chfe1 = r.createCell(j++);
            chfe1.setCellValue(new HSSFRichTextString("Conexion"));
            chfe1.setCellStyle(estiloCelda);

            HSSFCell chfe11 = r.createCell(j++);
            chfe11.setCellValue(new HSSFRichTextString("Nombre"));
            chfe11.setCellStyle(estiloCelda);

            HSSFCell chfe11a = r.createCell(j++);
            chfe11a.setCellValue(new HSSFRichTextString("Apellido"));
            chfe11a.setCellStyle(estiloCelda);

            HSSFCell ch2a = r.createCell(j++);
            ch2a.setCellValue(new HSSFRichTextString("m3"));
            ch2a.setCellStyle(estiloCelda);

            HSSFCell ch2 = r.createCell(j++);
            ch2.setCellValue(new HSSFRichTextString("Agua"));
            ch2.setCellStyle(estiloCelda);

            HSSFCell ch22 = r.createCell(j++);
            ch22.setCellValue(new HSSFRichTextString("Excedente"));
            ch22.setCellStyle(estiloCelda);

            HSSFCell ch23 = r.createCell(j++);
            ch23.setCellValue(new HSSFRichTextString("Alcantarillado"));
            ch23.setCellStyle(estiloCelda);

            HSSFCell ch3 = r.createCell(j++);
            ch3.setCellValue(new HSSFRichTextString("Desechos"));
            ch3.setCellStyle(estiloCelda);

            HSSFCell ch4 = r.createCell(j++);
            ch4.setCellValue(new HSSFRichTextString("Ambiente"));
            ch4.setCellStyle(estiloCelda);

            HSSFCell ch5a = r.createCell(j++);
            ch5a.setCellValue(new HSSFRichTextString("Interes 1"));
            ch5a.setCellStyle(estiloCelda);

            HSSFCell ch5ab = r.createCell(j++);
            ch5ab.setCellValue(new HSSFRichTextString("Interes 2"));
            ch5ab.setCellStyle(estiloCelda);

            HSSFCell ch5abc = r.createCell(j++);
            ch5abc.setCellValue(new HSSFRichTextString("Total"));
            ch5abc.setCellStyle(estiloCelda);

//            HSSFCell ch5 = r.createCell(j++);
//            ch5.setCellValue(new HSSFRichTextString("ESTADO"));
//            ch5.setCellStyle(estiloCelda);
            int rownum = 1;
            int i = 0;
            BigDecimal subTotal = BigDecimal.ZERO;
            BigDecimal subTotal12 = BigDecimal.ZERO;
            BigDecimal subTotal0 = BigDecimal.ZERO;
            BigDecimal IVATotal = BigDecimal.ZERO;
            BigDecimal total = BigDecimal.ZERO;

            for (ListadoDetalladoOrdenado item : listaListItemsOrd) {
                i = 0;

                r = s.createRow(rownum);

                HSSFCell cf = r.createCell(i++);
                cf.setCellValue(new HSSFRichTextString(item.getIdFactura().toString()));

                HSSFCell cf1 = r.createCell(i++);
                cf1.setCellValue(new HSSFRichTextString(sm.format(item.getFacFecha())));

                HSSFCell cf11 = r.createCell(i++);
                cf11.setCellValue(new HSSFRichTextString(item.getMedNumero().toString()));

                HSSFCell c0 = r.createCell(i++);
                c0.setCellValue(new HSSFRichTextString(item.getPropNombre()));

                HSSFCell c0a = r.createCell(i++);
                c0a.setCellValue(new HSSFRichTextString(item.getPropApellido()));

                HSSFCell c1 = r.createCell(i++);
                c1.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getFacMetrosCubicos() == null ? BigDecimal.ZERO : item.getFacMetrosCubicos(), 2)).toString()));

                HSSFCell c11 = r.createCell(i++);
                c11.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getAgua() == null ? BigDecimal.ZERO : item.getAgua(), 2)).toString()));
                HSSFCell c11a = r.createCell(i++);
                c11a.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getExcedente() == null ? BigDecimal.ZERO : item.getExcedente(), 2)).toString()));
                HSSFCell c11ab = r.createCell(i++);
                c11ab.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getAlcantarrillado() == null ? BigDecimal.ZERO : item.getAlcantarrillado(), 2)).toString()));

                HSSFCell c12 = r.createCell(i++);
                c12.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getDesechos() == null ? BigDecimal.ZERO : item.getDesechos(), 2)).toString()));
                HSSFCell c12a = r.createCell(i++);
                c12a.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getMedioAmbiente() == null ? BigDecimal.ZERO : item.getMedioAmbiente(), 2)).toString()));

                HSSFCell c2 = r.createCell(i++);
                c2.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getInteres1() == null ? BigDecimal.ZERO : item.getInteres1(), 2)).toString()));

                HSSFCell c3 = r.createCell(i++);
                c3.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getInteres2() == null ? BigDecimal.ZERO : item.getInteres2(), 2)).toString()));
                HSSFCell c3a = r.createCell(i++);
                c3a.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getFacTotal() == null ? BigDecimal.ZERO : item.getFacTotal(), 2)).toString()));

//                HSSFCell c4 = r.createCell(i++);
//                c4.setCellValue(new HSSFRichTextString(item.g));

                /*autemta la siguiente fila*/
                rownum += 1;

            }

            for (int k = 0; k <= listaListItemsOrd.size(); k++) {
                s.autoSizeColumn(k);
            }
            wb.write(archivo);
            archivo.close();

        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }
        return pathSalida;

    }

}
