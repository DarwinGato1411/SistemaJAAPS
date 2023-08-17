/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.vistas;

import com.ec.servicio.HelperPersistencia;
import com.ec.servicio.ServicioAcumuladoVentas;
import com.ec.untilitario.ArchivoUtils;
import com.ec.vista.servicios.ServicioListadoDetallado;
import com.ec.vista.servicios.ServicioListadoDetalladoOrdenado;
import com.ec.vista.servicios.ServicioMovimientoCartera;
import com.ec.vistas.Acumuladopordia;
import com.ec.vistas.ListadoDetallado;
import com.ec.vistas.ListadoDetalladoOrdenado;
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
import org.zkoss.zul.Filedownload;

/**
 *
 * @author HC
 */
public class ListaLisDetalladoOrdenado {

    ServicioListadoDetallado servicioListadoDetallado = new ServicioListadoDetallado();

    private Date inicio = new Date();
    private Date fin = new Date();
    private Date fecha = new Date();
    // String sDate1="29/12/2022"; 
    private BigDecimal totalVenta = BigDecimal.ZERO;

    private List<ListadoDetallado> listaListDetallado = new ArrayList<ListadoDetallado>();

    ServicioListadoDetalladoOrdenado servicioDetalladoOrdenado = new ServicioListadoDetalladoOrdenado();
    private List<ListadoDetalladoOrdenado> listaListItemsOrd = new ArrayList<ListadoDetalladoOrdenado>();

    ServicioMovimientoCartera servicioMovimientoCartera = new ServicioMovimientoCartera();
    private List<MovimientoCartera> listaMovCartera = new ArrayList<MovimientoCartera>();

    private List<Acumuladopordia> listaAcumuladopordias = new ArrayList<Acumuladopordia>();
    ServicioAcumuladoVentas servicioAcumuladoVentas = new ServicioAcumuladoVentas();
    private Date fechainicioDiaria = new Date();
    private String buscar = "";

    public ListaLisDetalladoOrdenado() {

        Calendar calendar = Calendar.getInstance(); //obtiene la fecha de hoy 
        calendar.add(Calendar.DATE, -6); //el -3 indica que se le restaran 3 dias 
        inicio = calendar.getTime();

        //fechainicioDiaria.setDate(-7);
        consultaDetalle();

        fechainicioDiaria = calendar.getTime();

    }

    @Command
    @NotifyChange({"listaListItemsOrd", "inicio", "fin"})
    public void buscarListadoDetallado() {

        consultaDetalle();

    }

    private void consultaDetalle() {
        totalVenta = BigDecimal.ZERO;
        listaListItemsOrd = servicioDetalladoOrdenado.findByMes(inicio, fin, buscar);

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
            parametros.put("fac_numero", buscar);

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
            ch1.setCellValue(new HSSFRichTextString("F Factura"));
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
            ch5a.setCellValue(new HSSFRichTextString("Int Val Imp"));
            ch5a.setCellStyle(estiloCelda);

            HSSFCell ch5ab = r.createCell(j++);
            ch5ab.setCellValue(new HSSFRichTextString("Mult 2 Mes Imp"));
            ch5ab.setCellStyle(estiloCelda);
            HSSFCell ch5abc = r.createCell(j++);
            ch5abc.setCellValue(new HSSFRichTextString("Derechos"));
            ch5abc.setCellStyle(estiloCelda);
            HSSFCell ch5abca = r.createCell(j++);
            ch5abca.setCellValue(new HSSFRichTextString("Factibilidad"));
            ch5abca.setCellStyle(estiloCelda);
            HSSFCell ch5abd = r.createCell(j++);
            ch5abd.setCellValue(new HSSFRichTextString("Multas"));
            ch5abd.setCellStyle(estiloCelda);
            HSSFCell ch5abe = r.createCell(j++);
            ch5abe.setCellValue(new HSSFRichTextString("Materiales"));
            ch5abe.setCellStyle(estiloCelda);
            HSSFCell ch5abf = r.createCell(j++);
            ch5abf.setCellValue(new HSSFRichTextString("Garantia"));
            ch5abf.setCellStyle(estiloCelda);
            HSSFCell ch5abfa = r.createCell(j++);
            ch5abfa.setCellValue(new HSSFRichTextString("Otros"));
            ch5abfa.setCellStyle(estiloCelda);

            HSSFCell ch5abcg = r.createCell(j++);
            ch5abcg.setCellValue(new HSSFRichTextString("Total"));
            ch5abcg.setCellStyle(estiloCelda);

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

            BigDecimal totalAg = BigDecimal.ZERO;
            BigDecimal totalAl = BigDecimal.ZERO;
            BigDecimal totalEx = BigDecimal.ZERO;
            BigDecimal totalDes = BigDecimal.ZERO;
            BigDecimal totalAmb = BigDecimal.ZERO;
            BigDecimal totalInt1 = BigDecimal.ZERO;
            BigDecimal totalInt2 = BigDecimal.ZERO;
            BigDecimal totalDerech = BigDecimal.ZERO;
            BigDecimal totalfactibilidad = BigDecimal.ZERO;
            BigDecimal totalMulta = BigDecimal.ZERO;
            BigDecimal totalMaterial = BigDecimal.ZERO;
            BigDecimal totalGarant = BigDecimal.ZERO;
            BigDecimal totalOtros = BigDecimal.ZERO;
            BigDecimal totalTotal = BigDecimal.ZERO;

            for (ListadoDetalladoOrdenado item : listaListItemsOrd) {
                i = 0;

                r = s.createRow(rownum);

                HSSFCell cf = r.createCell(i++);
                cf.setCellValue(new HSSFRichTextString(item.getIdFactura().toString()));

                HSSFCell cf12 = r.createCell(i++);
                cf12.setCellValue(new HSSFRichTextString(item.getFacNumero().toString()));

                HSSFCell cf1 = r.createCell(i++);
                cf1.setCellValue(new HSSFRichTextString(sm.format(item.getFacFecha())));

                HSSFCell cf11 = r.createCell(i++);
                cf11.setCellValue(new HSSFRichTextString(item.getMedNumero() == null ? "" : item.getMedNumero().toString()));

                HSSFCell c0 = r.createCell(i++);
                c0.setCellValue(new HSSFRichTextString(item.getPropNombre() == null ? "" : item.getPropNombre()));

                HSSFCell c0a = r.createCell(i++);
                c0a.setCellValue(new HSSFRichTextString(item.getPropApellido() == null ? "" : item.getPropApellido()));

                HSSFCell c1 = r.createCell(i++);
                c1.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getFacMetrosCubicos() == null ? BigDecimal.ZERO : item.getFacMetrosCubicos(), 2)).toString()));

                HSSFCell c11 = r.createCell(i++);
                c11.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getAgua() == null ? BigDecimal.ZERO : item.getAgua(), 2)).toString()));
                totalAg = totalAg.add(ArchivoUtils.redondearDecimales(item.getAgua() == null ? BigDecimal.ZERO : item.getAgua(), 2));

                HSSFCell c11a = r.createCell(i++);
                c11a.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getExcedente() == null ? BigDecimal.ZERO : item.getExcedente(), 2)).toString()));
                totalEx = totalEx.add(ArchivoUtils.redondearDecimales(item.getExcedente() == null ? BigDecimal.ZERO : item.getExcedente(), 2));

                HSSFCell c11ab = r.createCell(i++);
                c11ab.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getAlcantarrillado() == null ? BigDecimal.ZERO : item.getAlcantarrillado(), 2)).toString()));
                totalAl = totalAl.add(ArchivoUtils.redondearDecimales(item.getAlcantarrillado() == null ? BigDecimal.ZERO : item.getAlcantarrillado(), 2));

                HSSFCell c12 = r.createCell(i++);
                c12.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getDesechos() == null ? BigDecimal.ZERO : item.getDesechos(), 2)).toString()));
                totalDes = totalDes.add(ArchivoUtils.redondearDecimales(item.getDesechos() == null ? BigDecimal.ZERO : item.getDesechos(), 2));

                HSSFCell c12a = r.createCell(i++);
                c12a.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getMedioAmbiente() == null ? BigDecimal.ZERO : item.getMedioAmbiente(), 2)).toString()));
                totalAmb = totalAmb.add(ArchivoUtils.redondearDecimales(item.getMedioAmbiente() == null ? BigDecimal.ZERO : item.getMedioAmbiente(), 2));

                HSSFCell c2 = r.createCell(i++);
                c2.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getInteres1() == null ? BigDecimal.ZERO : item.getInteres1(), 2)).toString()));
                totalInt1 = totalInt1.add(ArchivoUtils.redondearDecimales(item.getInteres1() == null ? BigDecimal.ZERO : item.getInteres1(), 2));

                HSSFCell c3 = r.createCell(i++);
                c3.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getInteres2() == null ? BigDecimal.ZERO : item.getInteres2(), 2)).toString()));
                totalInt2 = totalInt2.add(ArchivoUtils.redondearDecimales(item.getInteres2() == null ? BigDecimal.ZERO : item.getInteres2(), 2));

                HSSFCell c4 = r.createCell(i++);
                c4.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getDerechos() == null ? BigDecimal.ZERO : item.getDerechos(), 2)).toString()));
                totalDerech = totalDerech.add(ArchivoUtils.redondearDecimales(item.getDerechos() == null ? BigDecimal.ZERO : item.getDerechos(), 2));

                HSSFCell c44 = r.createCell(i++);
                c44.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getFactibilidad() == null ? BigDecimal.ZERO : item.getFactibilidad(), 2)).toString()));
                totalfactibilidad = totalfactibilidad.add(ArchivoUtils.redondearDecimales(item.getFactibilidad() == null ? BigDecimal.ZERO : item.getFactibilidad(), 2));

                HSSFCell c5 = r.createCell(i++);
                c5.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getMultas() == null ? BigDecimal.ZERO : item.getMultas(), 2)).toString()));
                totalMulta = totalMulta.add(ArchivoUtils.redondearDecimales(item.getMultas() == null ? BigDecimal.ZERO : item.getMultas(), 2));

                HSSFCell c6 = r.createCell(i++);
                c6.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getMaterial() == null ? BigDecimal.ZERO : item.getMaterial(), 2)).toString()));
                totalMaterial = totalMaterial.add(ArchivoUtils.redondearDecimales(item.getMaterial() == null ? BigDecimal.ZERO : item.getMaterial(), 2));

                HSSFCell c7 = r.createCell(i++);
                c7.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getGarantia() == null ? BigDecimal.ZERO : item.getGarantia(), 2)).toString()));
                totalGarant = totalGarant.add(ArchivoUtils.redondearDecimales(item.getGarantia() == null ? BigDecimal.ZERO : item.getGarantia(), 2));

                HSSFCell c8 = r.createCell(i++);
                c8.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getOtros() == null ? BigDecimal.ZERO : item.getOtros(), 2)).toString()));
                totalOtros = totalOtros.add(ArchivoUtils.redondearDecimales(item.getOtros() == null ? BigDecimal.ZERO : item.getOtros(), 2));

                HSSFCell c3a = r.createCell(i++);
                c3a.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getFacTotal() == null ? BigDecimal.ZERO : item.getFacTotal(), 2)).toString()));
                totalTotal = totalTotal.add(ArchivoUtils.redondearDecimales(item.getFacTotal() == null ? BigDecimal.ZERO : item.getFacTotal(), 2));

//                HSSFCell c4 = r.createCell(i++);
//                c4.setCellValue(new HSSFRichTextString(item.g));

                /*autemta la siguiente fila*/
                rownum += 1;

            }

            /*tottalizods*/
            j = 0;
            r = s.createRow(rownum);
            HSSFCell chfeF1 = r.createCell(j++);
            chfeF1.setCellValue(new HSSFRichTextString(""));
            chfeF1.setCellStyle(estiloCelda);

            HSSFCell chfeF2 = r.createCell(j++);
            chfeF2.setCellValue(new HSSFRichTextString(""));
            chfeF2.setCellStyle(estiloCelda);

            HSSFCell chfeF3 = r.createCell(j++);
            chfeF3.setCellValue(new HSSFRichTextString(""));
            chfeF3.setCellStyle(estiloCelda);

            HSSFCell chF4 = r.createCell(j++);
            chF4.setCellValue(new HSSFRichTextString(""));
            chF4.setCellStyle(estiloCelda);
            HSSFCell chF41 = r.createCell(j++);
            chF41.setCellValue(new HSSFRichTextString(""));
            chF41.setCellStyle(estiloCelda);
            HSSFCell chF42 = r.createCell(j++);
            chF42.setCellValue(new HSSFRichTextString(""));
            chF42.setCellStyle(estiloCelda);
            HSSFCell chF423 = r.createCell(j++);
            chF423.setCellValue(new HSSFRichTextString(""));
            chF423.setCellStyle(estiloCelda);

            HSSFCell chF5 = r.createCell(j++);
            chF5.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(totalAg, 2)).toString()));
            chF5.setCellStyle(estiloCelda);

            HSSFCell chF6 = r.createCell(j++);
            chF6.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(totalEx, 2)).toString()));
            chF6.setCellStyle(estiloCelda);

            HSSFCell chF7 = r.createCell(j++);
            chF7.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(totalAl, 2)).toString()));
            chF7.setCellStyle(estiloCelda);

            HSSFCell chF8 = r.createCell(j++);
            chF8.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(totalDes, 2)).toString()));
            chF8.setCellStyle(estiloCelda);

            HSSFCell chF9 = r.createCell(j++);
            chF9.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(totalAmb, 2)).toString()));
            chF9.setCellStyle(estiloCelda);

            HSSFCell chF91 = r.createCell(j++);
            chF91.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(totalInt1, 2)).toString()));
            chF91.setCellStyle(estiloCelda);

            HSSFCell chF92 = r.createCell(j++);
            chF92.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(totalInt2, 2)).toString()));
            chF92.setCellStyle(estiloCelda);

            HSSFCell chF93 = r.createCell(j++);
            chF93.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(totalDerech, 2)).toString()));
            chF93.setCellStyle(estiloCelda);

            HSSFCell chF94 = r.createCell(j++);
            chF94.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(totalfactibilidad, 2)).toString()));
            chF94.setCellStyle(estiloCelda);

            HSSFCell chF945 = r.createCell(j++);
            chF945.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(totalMulta, 2)).toString()));
            chF945.setCellStyle(estiloCelda);

            HSSFCell chF946 = r.createCell(j++);
            chF946.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(totalMaterial, 2)).toString()));
            chF946.setCellStyle(estiloCelda);

            HSSFCell chF9462 = r.createCell(j++);
            chF9462.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(totalGarant, 2)).toString()));
            chF9462.setCellStyle(estiloCelda);

            HSSFCell chF9461 = r.createCell(j++);
            chF9461.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(totalOtros, 2)).toString()));
            chF9461.setCellStyle(estiloCelda);

            HSSFCell chF9463 = r.createCell(j++);
            chF9463.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(totalTotal, 2)).toString()));
            chF9463.setCellStyle(estiloCelda);

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

    public String getBuscar() {
        return buscar;
    }

    public void setBuscar(String buscar) {
        this.buscar = buscar;
    }

}
