/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Cliente;
import com.ec.entidad.Medidor;
import com.ec.servicio.ServicioMedidor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.activation.MimetypesFileTypeMap;
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
    private String medidorActivo = "activo";

    public AdmMedidor() {
        findMedidorPorNombreApellidoCedula();
    }

    private void findMedidorPorNombreApellidoCedula() {
        boolean activo = false;
        if (medidorActivo.equals("activo")) {
            activo = true;
            medidorActivo = "activo";
            System.out.println(medidorActivo);
        } else {
            System.out.println(medidorActivo);
            activo = false;
            medidorActivo = "inactivo";
        }
        listaDatos = servicioMedidor.findLikeNombreApellidoCedulaActivo(buscarNombre, activo);
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
    @NotifyChange({"listaDatos", "buscarNombre", "medidorActivo"})
    public void actualizarActivo() {
        System.out.println("hola");
        findMedidorPorNombreApellidoCedula();
    }

    @Command
    @NotifyChange({"listaDatos", "buscarNumero"})
    public void buscarMedidorNumero() {
        findMedidorPorNumero();
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

    private String exportarExcel() throws FileNotFoundException, IOException {
        String directorioReportes = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/reportes");

        Date date = new Date();
        SimpleDateFormat fhora = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sm = new SimpleDateFormat("yyy-MM-dd");
        String strDate = sm.format(date);

        String pathSalida = directorioReportes + File.separator + "medidores.xls";
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

            HSSFCell chfe = r.createCell(0);
            chfe.setCellValue(new HSSFRichTextString("Num Medidor"));
            chfe.setCellStyle(estiloCelda);

            HSSFCell ch1 = r.createCell(j++);
            ch1.setCellValue(new HSSFRichTextString("Descripción"));
            ch1.setCellStyle(estiloCelda);

            HSSFCell ch2 = r.createCell(j++);
            ch2.setCellValue(new HSSFRichTextString("Dir Medidor"));
            ch2.setCellStyle(estiloCelda);

            HSSFCell ch21 = r.createCell(j++);
            ch21.setCellValue(new HSSFRichTextString("Cedula"));
            ch21.setCellStyle(estiloCelda);

            HSSFCell ch3 = r.createCell(j++);
            ch3.setCellValue(new HSSFRichTextString("Nombre"));
            ch3.setCellStyle(estiloCelda);

            HSSFCell ch4 = r.createCell(j++);
            ch4.setCellValue(new HSSFRichTextString("Apellido"));
            ch4.setCellStyle(estiloCelda);

            HSSFCell ch5 = r.createCell(j++);
            ch5.setCellValue(new HSSFRichTextString("Estado"));
            ch5.setCellStyle(estiloCelda);

            int rownum = 1;
            int i = 0;

            for (Medidor item : listaDatos) {
                i = 0;

                r = s.createRow(rownum);

                HSSFCell cf0 = r.createCell(i++);
                cf0.setCellValue(new HSSFRichTextString(item.getMedNumero()));

                HSSFCell cf1 = r.createCell(i++);
                cf1.setCellValue(new HSSFRichTextString(item.getMedDescripcion()));

                HSSFCell cf2 = r.createCell(i++);
                cf2.setCellValue(new HSSFRichTextString(item.getMedDireccion()));

                HSSFCell cf3 = r.createCell(i++);
                cf3.setCellValue(new HSSFRichTextString(item.getIdPredio().getIdPropietario().getPorpCedula()));

                HSSFCell cf4 = r.createCell(i++);
                cf4.setCellValue(new HSSFRichTextString(item.getIdPredio().getIdPropietario().getPropNombre()));

                HSSFCell cf5 = r.createCell(i++);
                cf5.setCellValue(new HSSFRichTextString(item.getIdPredio().getIdPropietario().getPropApellido()));

                HSSFCell cf6 = r.createCell(i++);
                cf6.setCellValue(new HSSFRichTextString(item.getMedActivo() ? "Activo" : "Inactivo"));


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

    public ServicioMedidor getServicioMedidor() {
        return servicioMedidor;
    }

    public void setServicioMedidor(ServicioMedidor servicioMedidor) {
        this.servicioMedidor = servicioMedidor;
    }

    public String getMedidorActivo() {
        return medidorActivo;
    }

    public void setMedidorActivo(String medidorActivo) {
        this.medidorActivo = medidorActivo;
    }

}
