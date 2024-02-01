/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Factura;
import com.ec.entidad.Medidor;
import com.ec.servicio.ServicioMedidor;
import com.ec.untilitario.ArchivoUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.internet.ParseException;
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
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author gato
 */
public class AdmMedidor {

    ServicioMedidor servicioMedidor = new ServicioMedidor();

    private List<Medidor> listaDatos = new ArrayList<Medidor>();

    private String buscarNombre = "";
    private String buscarNumero = "";
    
    private Boolean activoInactivo=Boolean.TRUE;

    public AdmMedidor() {

        findMedidorPorNombreApellidoCedula();

    }

    private void findMedidorPorNombreApellidoCedula() {
        listaDatos = servicioMedidor.findLikeNombreApellidoCedula(buscarNombre);
    }

    private void findMedidorPorNumero() {
        listaDatos = servicioMedidor.findMedidorNumero(buscarNumero);
    }

    @Command
    @NotifyChange({"listaDatos", "buscarNombre"})
    public void buscarMedidorNombreApellidoNumero() {
        findMedidorPorNombreApellidoCedula();
    }

    @Command
    @NotifyChange({"listaDatos", "buscarNumero"})
    public void buscarMedidorNumero() {
        findMedidorPorNumero();
    }
    @Command
    @NotifyChange({"listaDatos", "buscarNumero"})
    public void  buscarActivoIncativo() {
        findActivoIncativo();
    }

    @Command
    @NotifyChange({"listaDatos", "buscarNombre"})
    public void nuevo() {
        buscarNombre = "";
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/nuevo/medidor.zul", null, null);
        window.doModal();
        findMedidorPorNombreApellidoCedula();
    }

    @Command
    @NotifyChange({"listaDatos", "buscarNombre"})
    public void actualizar(@BindingParam("valor") Medidor valor) {
        buscarNombre = "";
        final HashMap<String, Medidor> map = new HashMap<String, Medidor>();
        map.put("valor", valor);
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/nuevo/medidor.zul", null, map);
        window.doModal();
        findMedidorPorNombreApellidoCedula();
    }

    @Command
    @NotifyChange({"listaDatos", "buscarNombre"})
    public void activarDesactivar(@BindingParam("valor") Medidor valor) {      
                servicioMedidor.modificar(valor);
    }

    public String getBuscarNombre() {
        return buscarNombre;
    }

    public List<Medidor> getListaDatos() {
        return listaDatos;
    }

    public void setListaDatos(List<Medidor> listaDatos) {
        this.listaDatos = listaDatos;
    }

    public void setBuscarNombre(String buscarNombre) {
        this.buscarNombre = buscarNombre;
    }

    public String getBuscarNumero() {
        return buscarNumero;
    }

    public void setBuscarNumero(String buscarNumero) {
        this.buscarNumero = buscarNumero;
    }
    private void findActivoIncativo() {
           listaDatos = servicioMedidor.findEactivoInactivo(activoInactivo);
       }

    public Boolean getActivoInactivo() {
        return activoInactivo;
    }

    public void setActivoInactivo(Boolean activoInactivo) {
        this.activoInactivo = activoInactivo;
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

        String pathSalida = directorioReportes + File.separator + "Medidores_act_inact.xls";
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
            HSSFSheet s = wb.createSheet("Medidores");

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
            chfe.setCellValue(new HSSFRichTextString("Cedula"));
            chfe.setCellStyle(estiloCelda);

            HSSFCell chfe1 = r.createCell(j++);
            chfe1.setCellValue(new HSSFRichTextString("Nombre"));
            chfe1.setCellStyle(estiloCelda);

            HSSFCell chfe11 = r.createCell(j++);
            chfe11.setCellValue(new HSSFRichTextString("Apellido"));
            chfe11.setCellStyle(estiloCelda);

            HSSFCell ch1 = r.createCell(j++);
            ch1.setCellValue(new HSSFRichTextString("Numero"));
            ch1.setCellStyle(estiloCelda);

//            HSSFCell ch2 = r.createCell(j++);
//            ch2.setCellValue(new HSSFRichTextString("Subtotal"));
//            ch2.setCellStyle(estiloCelda);

            HSSFCell ch22 = r.createCell(j++);
            ch22.setCellValue(new HSSFRichTextString("Ubicacion"));
            ch22.setCellStyle(estiloCelda);

            HSSFCell ch23 = r.createCell(j++);
            ch23.setCellValue(new HSSFRichTextString("Tarifa"));
            ch23.setCellStyle(estiloCelda);

           

            int rownum = 1;
            int i = 0;
            BigDecimal subTotal = BigDecimal.ZERO;
            BigDecimal subTotal12 = BigDecimal.ZERO;
            BigDecimal subTotal0 = BigDecimal.ZERO;
            BigDecimal IVATotal = BigDecimal.ZERO;
            BigDecimal total = BigDecimal.ZERO;

            for (Medidor item : listaDatos) {
                i = 0;

                r = s.createRow(rownum);

                HSSFCell cf = r.createCell(i++);
                cf.setCellValue(new HSSFRichTextString(item.getIdPredio().getIdPropietario().getPorpCedula().toString()));

                HSSFCell cf1 = r.createCell(i++);
                cf1.setCellValue(new HSSFRichTextString(item.getIdPredio().getIdPropietario().getPropNombre().toString()));

                HSSFCell cf11 = r.createCell(i++);
                cf11.setCellValue(new HSSFRichTextString(item.getIdPredio().getIdPropietario().getPropApellido().toString()));

                HSSFCell c0 = r.createCell(i++);
                c0.setCellValue(new HSSFRichTextString(item.getMedNumero()));

                HSSFCell c1 = r.createCell(i++);
                c1.setCellValue(new HSSFRichTextString(item.getIdUbicacionMedidor()!=null?item.getIdUbicacionMedidor().getUbimNombre():""));

               

                HSSFCell c11 = r.createCell(i++);
                c11.setCellValue(new HSSFRichTextString(item.getIdTarifa()!=null?item.getIdTarifa().getTariDescripcion():""));

              

                /*autemta la siguiente fila*/
                rownum += 1;

            }

            

            for (int k = 0; k <= listaDatos.size(); k++) {
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
