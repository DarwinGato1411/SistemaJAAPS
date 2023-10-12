/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Cliente;
import com.ec.entidad.DetalleFactura;
import com.ec.entidad.Factura;
import com.ec.entidad.Tipoambiente;
import com.ec.servicio.HelperPersistencia;
import com.ec.servicio.ServicioCliente;
import com.ec.servicio.ServicioDetalleFactura;
import com.ec.servicio.ServicioFactura;
import com.ec.servicio.ServicioTipoAmbiente;
import com.ec.untilitario.ArchivoUtils;
import com.ec.untilitario.AutorizarDocumentos;
import com.ec.untilitario.MailerClass;
import com.ec.untilitario.ParamFactura;
import com.ec.untilitario.ParametroLote;
import com.ec.untilitario.XAdESBESSignature;
import ec.gob.sri.comprobantes.exception.RespuestaAutorizacionException;
import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;
import ec.gob.sri.comprobantes.ws.aut.Autorizacion;
import ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante;
import java.awt.Color;
import java.awt.image.BufferedImage;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.internet.ParseException;
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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author gato
 */
public class ListaNotaEntregaAll {

    /*RUTAS PARA LOS ARCHIVPOS XML SRI*/
    private static String PATH_BASE = "";
    ServicioTipoAmbiente servicioTipoAmbiente = new ServicioTipoAmbiente();
    ServicioFactura servicioFactura = new ServicioFactura();
    ServicioCliente servicioCliente = new ServicioCliente();
    ServicioDetalleFactura servicioDetalleFactura = new ServicioDetalleFactura();
    private List<Factura> lstFacturas = new ArrayList<Factura>();
    //reporte
    AMedia fileContent = null;
    Connection con = null;
    private String buscarCliente = "";
    private String buscarCedula = "";
    private String buscarNumFactura = "";
    private String estadoBusqueda = "N";
    private BigDecimal porCobrar = BigDecimal.ZERO;
    //tabla para los parametros del SRI
    private Tipoambiente amb = new Tipoambiente();
    private Date fechainicio = new Date();
    private Date fechafin = new Date();

    public ListaNotaEntregaAll() {

        amb = servicioTipoAmbiente.FindALlTipoambiente();
        //OBTIENE LAS RUTAS DE ACCESO A LOS DIRECTORIOS DE LA TABLA TIPOAMBIENTE
        PATH_BASE = amb.getAmDirBaseArchivos() + File.separator
                    + amb.getAmDirXml();
        consultarFacturaFecha();
    }

    public List<Factura> getLstFacturas() {
        return lstFacturas;
    }

    public void setLstFacturas(List<Factura> lstFacturas) {
        this.lstFacturas = lstFacturas;
    }

    public String getBuscarCliente() {
        return buscarCliente;
    }

    public void setBuscarCliente(String buscarCliente) {
        this.buscarCliente = buscarCliente;
    }

    public String getEstadoBusqueda() {
        return estadoBusqueda;
    }

    public void setEstadoBusqueda(String estadoBusqueda) {
        this.estadoBusqueda = estadoBusqueda;
    }

    public BigDecimal getPorCobrar() {
        return porCobrar;
    }

    public void setPorCobrar(BigDecimal porCobrar) {
        this.porCobrar = porCobrar;
    }

    @Command
    public void reporteNotaVenta(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        reporteGeneral(valor.getFacNumero(), "FACT");
    }

    @Command
    public void reporteComprobante(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        reporteGeneral(valor.getFacNumero(), "COMP");
    }

    @Command
    public void reporteFacturaPerzonalizada(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        reporteGeneral(valor.getFacNumero(), "FACT5");
    }

    @Command
    public void reporteNotaVentaTicket(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        reporteGeneral(valor.getFacNumNotaVenta(), "NTV");
    }

    @Command
    @NotifyChange({"lstFacturas", "buscarCliente"})
    public void modificarCotizacion(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        try {
            ParamFactura param = new ParamFactura();
            param.setIdFactura(valor.getIdFactura().toString());
            param.setBusqueda("modificar");
            param.setTipoDoc("FACT");
            final HashMap<String, ParamFactura> map = new HashMap<String, ParamFactura>();

            map.put("valor", param);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/modificar/factura.zul", null, map);
            window.doModal();
//            window.detach();
            buscarFechas();

        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Command
    public void verDetallePago(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        try {
            final HashMap<String, Factura> map = new HashMap<String, Factura>();

            map.put("valor", valor);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/venta/detallepago.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }

    }

    @Command
    public void crearNotaCredito(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        try {
            ParamFactura param = new ParamFactura();
            param.setIdFactura(valor.getIdFactura().toString());
            param.setBusqueda("modificar");
            param.setTipoDoc("FACT");
            final HashMap<String, ParamFactura> map = new HashMap<String, ParamFactura>();

            map.put("valor", param);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/nuevo/notacrdb.zul", null, map);
            window.doModal();
//            window.detach();
        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Command
    @NotifyChange({"lstFacturas", "fechafin", "fechainicio"})
    public void eliminarNotaEntrega(@BindingParam("valor") Factura valor) {

        if (Messagebox.show("¿Esta seguro de eliminar la nota de entrega?", "Question", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION) == Messagebox.OK) {
            servicioFactura.eliminar(valor);
            buscarFechas();
        }
    }

    @Command
    @NotifyChange({"lstFacturas", "fechafin", "fechainicio"})
    public void cambiarestado(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        try {
            if (Messagebox.show("Desea cambiar el Estado", "Question", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION) == Messagebox.OK) {
                if (valor.getFacNotaEntregaProcess().equals("S")) {
                    valor.setFacNotaEntregaProcess("N");
                    servicioFactura.modificar(valor);
                } else {
                    valor.setFacNotaEntregaProcess("S");
                    servicioFactura.modificar(valor);

                }
                buscarFechas();
            } else {
                Clients.showNotification("Solicitud cancelada",
                            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 1000, true);
            }
        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    public void reporteGeneral(Integer numeroFactura, String tipo) throws JRException, IOException, NamingException, SQLException {

        EntityManager emf = HelperPersistencia.getEMF();

        try {
            emf.getTransaction().begin();
            con = emf.unwrap(Connection.class);

            String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                        .getRealPath("/reportes");
            String reportPath = "";
            if (tipo.equals("COMP")) {
                reportPath = reportFile + File.separator + "puntoventa.jasper";
            } else if (tipo.equals("FACT")) {
                reportPath = reportFile + File.separator + "factura.jasper";
            } else if (tipo.equals("FACT5")) {
                reportPath = reportFile + File.separator + "facturaa5.jasper";
            } else if (tipo.equals("NTV")) {
                reportPath = reportFile + File.separator + "notaventaticket.jasper";
            }

            Map<String, Object> parametros = new HashMap<String, Object>();

            //  parametros.put("codUsuario", String.valueOf(credentialLog.getAdUsuario().getCodigoUsuario()));
            parametros.put("numfactura", numeroFactura);

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

    //buscart notas de venta
    @Command
    @NotifyChange({"lstFacturas", "buscarCliente"})
    public void buscarLikeCliente() {

        consultarFacturas();

    }

    //buscart notas de venta
    @Command
    @NotifyChange({"lstFacturas", "buscarNumFactura"})
    public void buscarLikeNumFactura() {

        consultarFacturasNum();

    }

    private void consultarFacturasNum() {
        lstFacturas = servicioFactura.FindLikeNumeroFacturaText(buscarNumFactura);
        //saldoPorCobrar();
    }

    private void consultarFacturas() {
        lstFacturas = servicioFactura.FindLikeCliente(buscarCliente);
        //saldoPorCobrar();
    }

    private void saldoPorCobrar() {
        porCobrar = BigDecimal.ZERO;
        for (Factura factura : lstFacturas) {
            porCobrar = porCobrar.add(factura.getFacSaldoAmortizado());
        }
    }

    //buscart notas de venta
    @Command
    @NotifyChange({"lstFacturas", "buscarCliente"})
    public void buscarLikeCedula() {

        consultarFacturasForCedula();

    }

    private void consultarFacturasForCedula() {
        lstFacturas = servicioFactura.findLikeCedula(buscarCedula);
        //   saldoPorCobrar();
    }

    @Command
    @NotifyChange({"lstFacturas", "fechafin", "fechainicio"})
    public void buscarFechas() {
        consultarFacturaFecha();
        //saldoPorCobrar();
    }

    private void consultarFacturaFecha() {
        lstFacturas = servicioFactura.findNotaEntregaFecha(fechainicio, fechafin, estadoBusqueda);
    }

    private void consultarFacturasEstado() {
        lstFacturas = servicioFactura.findEstadoFactura(estadoBusqueda);
        //saldoPorCobrar();
    }
    //GRAFICA POR UBICACION
    JFreeChart jfreechartMes;
    private byte[] graficoBarrasMes;
    String pathSalidaMes = "";
    private AImage reporteMes;

    @Command
    @NotifyChange({"reporteUbicacion"})
    public void graficarForMes() throws IOException {
        List<Factura> lstModel = servicioFactura.FindALlFacturaMaxVeinte();

        //freechart
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();

        for (Factura item : lstModel) {

            defaultcategorydataset.addValue(item.getFacTotal(), item.getFacFecha(), item.getFacTipo());

        }

        jfreechartMes = ChartFactory.createBarChart(
                    "ESTADÍSTICA POR VENTA MENSUAL", // título del
                    // grafico
                    "", // título de las categorias(eje x)
                    "", // titulo de las series(eje y)
                    defaultcategorydataset, // conjunto de datos
                    PlotOrientation.VERTICAL, // orientación del gráfico
                    true, // incluye o no las series
                    false, // tooltips?
                    false // URLs?
        );
        jfreechartMes.setBackgroundPaint(Color.decode("#ffffff"));
        // plot maneja el dataset, axes(categories and series) y el rendered
        CategoryPlot plot = (CategoryPlot) jfreechartMes.getPlot();

        // renderer se uitiliza para getionar las barras
        CategoryItemRenderer renderer = plot.getRenderer();
        CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator(
                    "{2}", new DecimalFormat("0"));
        renderer.setBaseItemLabelGenerator(generator);

        BarRenderer rerender1 = (BarRenderer) plot.getRenderer();
        rerender1.setBaseItemLabelsVisible(true);
        rerender1.setItemMargin(0.0);
        rerender1.setShadowVisible(false);

        renderer.setSeriesPaint(0, Color.decode("#4198af"));
        renderer.setSeriesPaint(1, Color.decode("#91c3d5"));
        renderer.setBaseItemLabelPaint(Color.black);

//        plot.setBackgroundPaint(Color.WHITE);
//        plot.setDomainGridlinePaint(Color.white);
//        plot.setRangeGridlinePaint(Color.white);
//        plot.setDomainGridlinesVisible(true);
//        plot.setRangeGridlinesVisible(true);
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);

        // legendSeries para ubicar las series
        LegendTitle legendSeries = jfreechartMes.getLegend();
        RectangleEdge posicion = null;
        legendSeries.setPosition(posicion.RIGHT);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        BufferedImage image = jfreechartMes.createBufferedImage(650, 480);
        graficoBarrasMes = ChartUtilities.encodeAsPNG(image);
        reporteMes = new AImage("foto", graficoBarrasMes);

        String directorioReportes = Executions.getCurrent().getDesktop().getWebApp()
                    .getRealPath("/reportes");

        //crea la carpeta en el caso que no exista
        File baseDir = new File(directorioReportes);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        pathSalidaMes = directorioReportes + File.separator + "reportGenero.jpg";
        System.out.println("RUTA " + pathSalidaMes);
        ChartUtilities.saveChartAsJPEG(new File(pathSalidaMes), jfreechartMes, 500,
                    300);

    }

    public AMedia getFileContent() {
        return fileContent;
    }

    public void setFileContent(AMedia fileContent) {
        this.fileContent = fileContent;
    }

    public JFreeChart getJfreechartMes() {
        return jfreechartMes;
    }

    public void setJfreechartMes(JFreeChart jfreechartMes) {
        this.jfreechartMes = jfreechartMes;
    }

    public byte[] getGraficoBarrasMes() {
        return graficoBarrasMes;
    }

    public void setGraficoBarrasMes(byte[] graficoBarrasMes) {
        this.graficoBarrasMes = graficoBarrasMes;
    }

    public String getPathSalidaMes() {
        return pathSalidaMes;
    }

    public void setPathSalidaMes(String pathSalidaMes) {
        this.pathSalidaMes = pathSalidaMes;
    }

    public AImage getReporteMes() {
        return reporteMes;
    }

    public void setReporteMes(AImage reporteMes) {
        this.reporteMes = reporteMes;
    }

    public static String getPATH_BASE() {
        return PATH_BASE;
    }

    public static void setPATH_BASE(String PATH_BASE) {
        ListaNotaEntregaAll.PATH_BASE = PATH_BASE;
    }

    public Tipoambiente getAmb() {
        return amb;
    }

    public void setAmb(Tipoambiente amb) {
        this.amb = amb;
    }

    @Command
    @NotifyChange({"lstFacturas"})
    public void buscarPendientesEnvSRI() {
        pendientesSRIEnv();

    }

    @Command
    @NotifyChange({"lstFacturas"})
    public void buscarDevueltaSRIReenvio() {
        devueltaSRIEnvReenvio();

    }

    @Command
    @NotifyChange({"lstFacturas"})
    public void buscarDevueltaSRIPorCorregir() {
        devueltaSRIEnvPorCorregir();

    }

    private void pendientesSRIEnv() {
        lstFacturas = servicioFactura.findBetweenPendientesEnviarSRI(fechainicio, fechafin);
    }

    private void devueltaSRIEnvReenvio() {
        lstFacturas = servicioFactura.findBetweenDevueltaPorReenviarSRI(fechainicio, fechafin);
    }

    private void devueltaSRIEnvPorCorregir() {
        lstFacturas = servicioFactura.findBetweenDevueltaPorCorregirSRI(fechainicio, fechafin);
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public String getBuscarCedula() {
        return buscarCedula;
    }

    public void setBuscarCedula(String buscarCedula) {
        this.buscarCedula = buscarCedula;
    }

    @Command
    public void cambiarEstadoFact(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        try {
            final HashMap<String, Factura> map = new HashMap<String, Factura>();

            map.put("valor", valor);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/modificar/estadofact.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
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

    private String exportarExcel() throws FileNotFoundException, IOException, ParseException {
        String directorioReportes = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/reportes");

        Date date = new Date();
        SimpleDateFormat fhora = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sm = new SimpleDateFormat("yyy-MM-dd");
        String strDate = sm.format(date);

        String pathSalida = directorioReportes + File.separator + "Facturas.xls";
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
            HSSFSheet s = wb.createSheet("Emitidas");

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

            HSSFCell chfe = r.createCell(j++);
            chfe.setCellValue(new HSSFRichTextString("Factura"));
            chfe.setCellStyle(estiloCelda);

            HSSFCell chfe1 = r.createCell(j++);
            chfe1.setCellValue(new HSSFRichTextString("CI/RUC"));
            chfe1.setCellStyle(estiloCelda);

            HSSFCell chfe11 = r.createCell(j++);
            chfe11.setCellValue(new HSSFRichTextString("Cliente"));
            chfe11.setCellStyle(estiloCelda);

            HSSFCell ch1 = r.createCell(j++);
            ch1.setCellValue(new HSSFRichTextString("F Emision"));
            ch1.setCellStyle(estiloCelda);

            HSSFCell ch2 = r.createCell(j++);
            ch2.setCellValue(new HSSFRichTextString("Subtotal"));
            ch2.setCellStyle(estiloCelda);

            HSSFCell ch22 = r.createCell(j++);
            ch22.setCellValue(new HSSFRichTextString("Subtotal 12%"));
            ch22.setCellStyle(estiloCelda);

            HSSFCell ch23 = r.createCell(j++);
            ch23.setCellValue(new HSSFRichTextString("Subtotal 0%"));
            ch23.setCellStyle(estiloCelda);

            HSSFCell ch3 = r.createCell(j++);
            ch3.setCellValue(new HSSFRichTextString("Iva"));
            ch3.setCellStyle(estiloCelda);

            HSSFCell ch4 = r.createCell(j++);
            ch4.setCellValue(new HSSFRichTextString("Total"));
            ch4.setCellStyle(estiloCelda);
            HSSFCell ch5 = r.createCell(j++);
            ch5.setCellValue(new HSSFRichTextString("ESTADO"));
            ch5.setCellStyle(estiloCelda);

            int rownum = 1;
            int i = 0;
            BigDecimal subTotal = BigDecimal.ZERO;
            BigDecimal subTotal12 = BigDecimal.ZERO;
            BigDecimal subTotal0 = BigDecimal.ZERO;
            BigDecimal IVATotal = BigDecimal.ZERO;
            BigDecimal total = BigDecimal.ZERO;

            for (Factura item : lstFacturas) {
                i = 0;

                r = s.createRow(rownum);

                HSSFCell cf = r.createCell(i++);
                cf.setCellValue(new HSSFRichTextString(item.getFacNumero().toString()));

                HSSFCell cf1 = r.createCell(i++);
                cf1.setCellValue(new HSSFRichTextString(item.getIdCliente().getCliCedula().toString()));

                HSSFCell cf11 = r.createCell(i++);
                cf11.setCellValue(new HSSFRichTextString(item.getIdCliente().getCliNombre().toString()));

                HSSFCell c0 = r.createCell(i++);
                c0.setCellValue(new HSSFRichTextString(sm.format(item.getFacFecha())));

                HSSFCell c1 = r.createCell(i++);
                c1.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getFacSubtotal(), 2)).toString()));

                subTotal = subTotal.add(ArchivoUtils.redondearDecimales(item.getFacSubtotal(), 2));

                HSSFCell c11 = r.createCell(i++);
                c11.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getFacTotalBaseGravaba(), 2)).toString()));

                subTotal12 = subTotal12.add(ArchivoUtils.redondearDecimales(item.getFacTotalBaseGravaba(), 2));

                HSSFCell c12 = r.createCell(i++);
                c12.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getFacTotalBaseCero(), 2)).toString()));

                subTotal0 = subTotal0.add(ArchivoUtils.redondearDecimales(item.getFacTotalBaseCero(), 2));

                HSSFCell c2 = r.createCell(i++);
                c2.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getFacIva(), 2)).toString()));

                IVATotal = IVATotal.add(ArchivoUtils.redondearDecimales(item.getFacIva(), 2));

                HSSFCell c3 = r.createCell(i++);
                c3.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getFacTotal(), 2)).toString()));

                total = total.add(ArchivoUtils.redondearDecimales(item.getFacTotal(), 2));

                HSSFCell c4 = r.createCell(i++);
                c4.setCellValue(new HSSFRichTextString(""));

                /*autemta la siguiente fila*/
                rownum += 1;

            }

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

            HSSFCell chF5 = r.createCell(j++);
            chF5.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(subTotal, 2)).toString()));
            chF5.setCellStyle(estiloCelda);

            HSSFCell chF6 = r.createCell(j++);
            chF6.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(subTotal12, 2)).toString()));
            chF6.setCellStyle(estiloCelda);

            HSSFCell chF7 = r.createCell(j++);
            chF7.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(subTotal0, 2)).toString()));
            chF7.setCellStyle(estiloCelda);

            HSSFCell chF8 = r.createCell(j++);
            chF8.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(IVATotal, 2)).toString()));
            chF8.setCellStyle(estiloCelda);

            HSSFCell chF9 = r.createCell(j++);
            chF9.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(total, 2)).toString()));
            chF9.setCellStyle(estiloCelda);

            HSSFCell chF10 = r.createCell(j++);
            chF10.setCellValue(new HSSFRichTextString(""));
            chF10.setCellStyle(estiloCelda);

            for (int k = 0; k <= lstFacturas.size(); k++) {
                s.autoSizeColumn(k);
            }
            wb.write(archivo);
            archivo.close();

        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }
        return pathSalida;

    }

    @Command
    public void exportarRegXML(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        try {
            FileOutputStream out = null;
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

            StringBuilder build = new StringBuilder();
            String linea = "<?xml version=\"1.0\" encoding=\"windows-1252\"?>";

            build.append(linea);

            DetalleFactura detalle = servicioDetalleFactura.findDetalleForIdFactuta(valor).get(0);

            linea = ("<ventas>\n"
                        + "<datosRegistrador>\n"
                        + "<numeroRUC>" + amb.getAmRuc().trim() + "</numeroRUC> \n"
                        + "</datosRegistrador>\n"
                        + "<datosVentas>\n"
                        + "<venta>\n"
                        + "<rucComercializador>" + amb.getAmRuc().trim() + "</rucComercializador> \n"
                        + "<CAMVCpn>" + detalle.getDetCamvcpn() + "</CAMVCpn> \n"
                        + "<serialVin>" + detalle.getDetSerialvin() + "</serialVin> \n"
                        + "<nombrePropietario>" + valor.getIdCliente().getCliApellidos() + " " + valor.getIdCliente().getCliNombres() + "</nombrePropietario> \n"
                        + "<tipoIdentificacionPropietario>" + detalle.getTipoIdentificacionPropietario() + "</tipoIdentificacionPropietario> \n"
                        + "<numeroDocumentoPropietario>" + valor.getIdCliente().getCliCedula() + "</numeroDocumentoPropietario> \n"
                        + "<tipoComprobante>1</tipoComprobante> \n"
                        + "<establecimientoComprobante>" + amb.getAmEstab() + "</establecimientoComprobante> \n"
                        + "<puntoEmisionComprobante>" + amb.getAmPtoemi() + "</puntoEmisionComprobante> \n"
                        + "<numeroComprobante>" + valor.getFacNumero() + "</numeroComprobante> \n"
                        + "<numeroAutorizacion>" + valor.getFacClaveAutorizacion() + "</numeroAutorizacion> \n"
                        + "<fechaVenta>" + formato.format(valor.getFacFecha()) + "</fechaVenta> \n"
                        + "<precioVenta>" + ArchivoUtils.redondearDecimales(valor.getFacTotal(), 2) + "</precioVenta> \n"
                        + "<codigoCantonMatriculacion>" + detalle.getCodigoCantonMatriculacion() + "</codigoCantonMatriculacion> \n"
                        + "<datosDireccion>\n"
                        + "<tipo>" + detalle.getTipodir() + "</tipo> \n"
                        + "<calle>" + detalle.getCalle() + "</calle> \n"
                        + "<numero>" + detalle.getNumero() + "</numero> \n"
                        + "<interseccion>" + detalle.getInterseccion() + "</interseccion> \n"
                        + "</datosDireccion>\n"
                        + "<datosTelefono>\n"
                        + "<provincia>" + detalle.getProvincia() + "</provincia> \n"
                        + "<numero>" + detalle.getNumerotel() + "</numero> \n"
                        + "</datosTelefono>\n"
                        + "</venta>\n"
                        + "</datosVentas>\n"
                        + "</ventas>");

            build.append(linea);
            /*IMPRIME EL XML DE LA FACTURA*/
            System.out.println("XML " + build);
            String pathArchivoSalida = "";

            String folderGenerados = PATH_BASE + File.separator + amb.getAmGenerados()
                        + File.separator + new Date().getYear()
                        + File.separator + new Date().getMonth();

            String nombreArchivoXML = File.separator + "MATRI-"
                        + valor.getCodestablecimiento()
                        + valor.getPuntoemision()
                        + valor.getFacNumeroText() + ".xml";
            /*ruta de salida del archivo XML 
            generados o autorizados para enviar al cliente 
            dependiendo la ruta enviada en el parametro del metodo */
            pathArchivoSalida = folderGenerados
                        + nombreArchivoXML;

            //String pathArchivoSalida = "D:\\";
            out = new FileOutputStream(pathArchivoSalida);
            out.write(build.toString().getBytes());
            //GRABA DATOS EN FACTURA//
            File dosfile = new File(pathArchivoSalida);
            if (dosfile.exists()) {
                FileInputStream inputStream = new FileInputStream(dosfile);
                Filedownload.save(inputStream, new MimetypesFileTypeMap().getContentType(dosfile), dosfile.getName());
            }

        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    public String getBuscarNumFactura() {
        return buscarNumFactura;
    }

    public void setBuscarNumFactura(String buscarNumFactura) {
        this.buscarNumFactura = buscarNumFactura;
    }

}
