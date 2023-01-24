/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.vistas;

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
import com.ec.vistas.Acumuladopordia;
import com.ec.vistas.ListadoDetallado;
import com.ec.vistas.ListadoDetalladoOrdenado;
import com.ec.vistas.ListadoItems;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

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

    private List<Acumuladopordia> listaAcumuladopordias = new ArrayList<Acumuladopordia>();
    ServicioAcumuladoVentas servicioAcumuladoVentas = new ServicioAcumuladoVentas();
    private Date fechainicioDiaria = new Date();

    ServicioAcumuladoDiarioUsuario servicioAcumuladoDiarioUsuario = new ServicioAcumuladoDiarioUsuario();
    UserCredential credential = new UserCredential();
    private BigDecimal totFactura = BigDecimal.ZERO;

    ServicioComprobanteDiario servicioComprobanteDiario = new ServicioComprobanteDiario();
    private List<ComprobanteDiario> listaComprobanteDiario = new ArrayList<ComprobanteDiario>();

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
    @NotifyChange({"listaAcumuladopordias", "fechainicioDiaria","listaComprobanteDiario"})
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
        listaListItemsOrd = servicioDetalladoOrdenado.findByMes(inicio, fin);

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

    public List<ComprobanteDiario> getListaComprobanteDiario() {
        return listaComprobanteDiario;
    }

    public void setListaComprobanteDiario(List<ComprobanteDiario> listaComprobanteDiario) {
        this.listaComprobanteDiario = listaComprobanteDiario;
    }

}
