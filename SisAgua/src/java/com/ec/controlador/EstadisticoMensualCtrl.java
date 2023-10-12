/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.EstadisticoMensual;
import com.ec.entidad.EstadisticoMensualHistorico;
import com.ec.entidad.Factura;
import com.ec.entidad.RetencionCompra;
import com.ec.servicio.HelperPersistencia;
import com.ec.servicio.ServicioGeneral;
import java.util.Calendar;
import java.util.Date;
import com.ec.servicio.contabilidad.ServicioEstadisticoMensual;
import com.ec.servicio.contabilidad.ServicioEstadisticoMensualHistorico;
import com.ec.untilitario.ArchivoUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Filedownload;

/**
 *
 * @author gato
 */
public class EstadisticoMensualCtrl {

    ServicioEstadisticoMensualHistorico servicioEstadisticoMensual = new ServicioEstadisticoMensualHistorico();
    private List<EstadisticoMensualHistorico> listaEstadisticoMensual = new ArrayList<EstadisticoMensualHistorico>();

    private Date inicio = new Date();
    private Date fin = new Date();

    ServicioGeneral servicioGeneral = new ServicioGeneral();

    //reporte listado detallado ordenado
    AMedia fileContent = null;
    Connection con = null;

    public EstadisticoMensualCtrl() {
        Calendar calendar = Calendar.getInstance(); //obtiene la fecha de hoy 
        calendar.add(Calendar.DATE, -15);
//        calendar.//el -3 indica que se le restaran 3 dias 
        inicio = calendar.getTime();
        consultarEstadisticoMensual();
        //fechainicioDiaria.setDate(-7);
        //consultaAC();

    }

    private void consultarEstadisticoMensual() {
        listaEstadisticoMensual = servicioEstadisticoMensual.findEstadisticoMensual(inicio, fin);
    }

    @Command
    @NotifyChange({"listaEstadisticoMensual", "inicio", "fin"})
    public void consultarEstadistico() {
        servicioGeneral.generarEstadisticoMensual(inicio, fin);
        consultarEstadisticoMensual();
    }

    @Command
    @NotifyChange({"listaEstadisticoMensual", "inicio", "fin"})
    public void calcular(@BindingParam("valor") EstadisticoMensualHistorico valor) {
        BigDecimal saldo = valor.getSaldoAnterior().add(valor.getTotalIngreso()).subtract(valor.getRecaudo());
        valor.setSaldoActual(saldo);
        servicioEstadisticoMensual.modificar(valor);
        consultarEstadisticoMensual();
    }

    public List<EstadisticoMensualHistorico> getListaEstadisticoMensual() {
        return listaEstadisticoMensual;
    }

    public void setListaEstadisticoMensual(List<EstadisticoMensualHistorico> listaEstadisticoMensual) {
        this.listaEstadisticoMensual = listaEstadisticoMensual;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

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

        String pathSalida = directorioReportes + File.separator + "estadistico_mensual.xls";
        System.out.println("Direccion del reporte  " + pathSalida);
        try {
            int j = 1;
            File archivoXLS = new File(pathSalida);
            if (archivoXLS.exists()) {
                archivoXLS.delete();
            }
            archivoXLS.createNewFile();
            FileOutputStream archivo = new FileOutputStream(archivoXLS);
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet s = wb.createSheet("EstadisticoS");

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

            HSSFCell chfe = r.createCell(0);
            chfe.setCellValue(new HSSFRichTextString("Rubro"));
            chfe.setCellStyle(estiloCelda);

            HSSFCell ch1 = r.createCell(j++);
            ch1.setCellValue(new HSSFRichTextString("Saldo anterior"));
            ch1.setCellStyle(estiloCelda);

            HSSFCell ch2 = r.createCell(j++);
            ch2.setCellValue(new HSSFRichTextString("Total Ingreso"));
            ch2.setCellStyle(estiloCelda);

            HSSFCell ch3 = r.createCell(j++);
            ch3.setCellValue(new HSSFRichTextString("Cobrado"));
            ch3.setCellStyle(estiloCelda);

            HSSFCell ch4 = r.createCell(j++);
            ch4.setCellValue(new HSSFRichTextString("Saldo actual"));
            ch4.setCellStyle(estiloCelda);

//            HSSFCell ch5 = r.createCell(j++);
//            ch5.setCellValue(new HSSFRichTextString("Clace_Acceso"));
//            ch5.setCellStyle(estiloCelda);
            int rownum = 1;
            int i = 0;

            BigDecimal saldoAnt = BigDecimal.ZERO;
            BigDecimal totGen = BigDecimal.ZERO;
            BigDecimal cobrado = BigDecimal.ZERO;
            BigDecimal saldoActual = BigDecimal.ZERO;

            for (EstadisticoMensualHistorico item : listaEstadisticoMensual) {
                i = 0;

                r = s.createRow(rownum);

                HSSFCell cf = r.createCell(i++);
                cf.setCellValue(new HSSFRichTextString(item.getRubro()));

                HSSFCell c0 = r.createCell(i++);
                c0.setCellValue(new HSSFRichTextString(ArchivoUtils.redondearDecimales(item.getSaldoAnterior(), 3).toString()));
                saldoAnt = saldoAnt.add(ArchivoUtils.redondearDecimales(item.getSaldoAnterior(), 3));
                
                HSSFCell c1 = r.createCell(i++);
                c1.setCellValue(new HSSFRichTextString(ArchivoUtils.redondearDecimales(item.getTotalIngreso(), 3).toString()));
                totGen = totGen.add(ArchivoUtils.redondearDecimales(item.getTotalIngreso(), 3));
                
                HSSFCell c2 = r.createCell(i++);
                c2.setCellValue(new HSSFRichTextString(ArchivoUtils.redondearDecimales(item.getRecaudo(), 3).toString()));
                cobrado = cobrado.add(ArchivoUtils.redondearDecimales(item.getRecaudo(), 3));
                
                HSSFCell c3 = r.createCell(i++);
                c3.setCellValue(new HSSFRichTextString(ArchivoUtils.redondearDecimales(item.getSaldoActual(), 3).toString()));
                saldoActual = saldoActual.add(ArchivoUtils.redondearDecimales(item.getSaldoActual(), 3));
                
                rownum += 1;
                

            }
            j = 0;
            r = s.createRow(rownum);
            HSSFCell chfeF1 = r.createCell(j++);
            chfeF1.setCellValue(new HSSFRichTextString(""));
            chfeF1.setCellStyle(estiloCelda);

            HSSFCell chfeF2 = r.createCell(j++);
            chfeF2.setCellValue(new HSSFRichTextString(saldoAnt.toString()));
            chfeF2.setCellStyle(estiloCelda);

            HSSFCell chfeF3 = r.createCell(j++);
            chfeF3.setCellValue(new HSSFRichTextString(totGen.toString()));
            chfeF3.setCellStyle(estiloCelda);

            HSSFCell chF4 = r.createCell(j++);
            chF4.setCellValue(new HSSFRichTextString(cobrado.toString()));
            chF4.setCellStyle(estiloCelda);
            
            
             HSSFCell chF5 = r.createCell(j++);
            chF5.setCellValue(new HSSFRichTextString(saldoActual.toString()));
            chF5.setCellStyle(estiloCelda);
            
            
            for (int k = 0; k <= listaEstadisticoMensual.size(); k++) {
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
    public void estdisticoMensual() throws JRException, IOException, NamingException, SQLException {
        reporteEstadisticoMensual();
    }

    public void reporteEstadisticoMensual() throws JRException, IOException, NamingException, SQLException {

        EntityManager emf = HelperPersistencia.getEMF();

        try {
            emf.getTransaction().begin();
            con = emf.unwrap(Connection.class);

            String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                    .getRealPath("/reportes");
            String reportPath = "";

            reportPath = reportFile + File.separator + "estadisticamensual.jasper";

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

}
