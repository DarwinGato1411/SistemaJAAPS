/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Lectura;
import com.ec.servicio.ServicioGeneral;
import com.ec.servicio.ServicioLectura;
import com.ec.untilitario.ListadoAnio;
import com.ec.untilitario.ListadoMeses;
import com.ec.untilitario.ModeloMeses;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author gato
 */
public class AdmLecturas {

    ServicioLectura servicioLectura = new ServicioLectura();

    private List<Lectura> listaDatos = new ArrayList<Lectura>();

    private String buscar = "";
    private ModeloMeses buscarMes = new ModeloMeses();
    private List<ModeloMeses> meses = new ArrayList<ModeloMeses>();
    private List<ModeloMeses> anios = new ArrayList<ModeloMeses>();
    private ModeloMeses buscarAnio = new ModeloMeses();
    private Date fechaCreacion = new Date();
    ServicioGeneral servicioGeneral = new ServicioGeneral();

    public AdmLecturas() {
        anios = ListadoAnio.getListaAnio();
        buscarAnio = ListadoAnio.getAnioActual();
        meses = ListadoMeses.getListaMeses();
        buscarMes = ListadoMeses.getMesActual();

        findMesAndNuMedidor();

    }

    private void findMesAndNuMedidor() {
        System.out.println("buscarMes.getMonth() " + buscarMes);
        listaDatos = servicioLectura.findMesAndNumMedidor(buscar, buscarMes.getNumero(), buscarAnio.getNumero());

    }

    @Command
    @NotifyChange({"listaDatos", "buscarMes"})
    public void buscarLecturas() {
        findMesAndNuMedidor();
    }

    @Command
    @NotifyChange({"listaDatos", "buscarMes"})
    public void generarLecturasMedidores() {
        if (Messagebox.show("Desea generar las lecturas de los medidores resagados en el mes de" + buscarMes.getNombre() + " Desea continuar?", "Question", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION) == Messagebox.OK) {
            fechaCreacion.setMonth(buscarMes.getNumero() - 1);
            System.out.println("fechaCreacion " + fechaCreacion);
            servicioGeneral.iniciarLecturaMedidor(buscarMes.getNumero(), fechaCreacion);
            findMesAndNuMedidor();
        }
    }

    @Command
    @NotifyChange({"listaDatos", "buscarMes"})
    public void iniciarMesSiguiente() {

        if (Messagebox.show("Al generar una nueva tabla de lecturas, los lecturas de " + buscarMes.getNombre() + " serán eliminadas" + "\n Desea continuar?", "Question", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION) == Messagebox.OK) {
            servicioLectura.iniciarProximoMes(buscarMes.getNumero(),fechaCreacion);
            findMesAndNuMedidor();
        } else {
            Clients.showNotification("Solicitud cancelada",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 1000, true);
        }
    }

    @Command
    @NotifyChange({"listaDatos", "buscar"})
    public void cambiarestado(@BindingParam("valor") Lectura valor) {

        if (Messagebox.show("Desea cambiar el Estado", "Question", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION) == Messagebox.OK) {
            if (valor.getLecPagada() == "S") {
                valor.setLecPagada("N");
                servicioLectura.modificar(valor);
            } else {
                valor.setLecPagada("S");
                servicioLectura.modificar(valor);
            }
        } else {
            Clients.showNotification("Solicitud cancelada",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 1000, true);
        }
    }

    @Command
    @NotifyChange({"listaDatos", "buscar"})
    public void nuevo() {
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/nuevo/propietario.zul", null, null);
        window.doModal();
        findMesAndNuMedidor();
    }

    @Command
    @NotifyChange({"listaDatos", "buscar"})
    public void calcularmetroCubico(@BindingParam("valor") Lectura valor) {
        if (valor.getLecAnterior() == null) {
            valor.setLecAnterior(BigDecimal.ZERO);
            Clients.showNotification("La lectura anterior no puede estar vacia",
                        Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 3000, true);
            return;
        }
        if (valor.getLecActual() == null) {
            valor.setLecAnterior(BigDecimal.ZERO);
            Clients.showNotification("La lectura actual no puede estar vacia",
                        Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 3000, true);
            return;
        }

        valor.setLecMetrosCubicos(valor.getLecActual().subtract(valor.getLecAnterior()));
        servicioLectura.modificar(valor);
    }

    @Command
    @NotifyChange({"listaDatos", "buscar"})
    public void actualizar(@BindingParam("valor") Lectura valor) {

        if (valor.getLecActual() != null
                    && valor.getLecAnterior() != null) {

            servicioLectura.modificar(valor);
            Clients.showNotification("Modificado correctamente",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
        } else {
            Clients.showNotification("La lectura anterior y actual no puede estar vacia",
                        Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 3000, true);
        }

    }

    public List<Lectura> getListaDatos() {
        return listaDatos;
    }

    public void setListaDatos(List<Lectura> listaDatos) {
        this.listaDatos = listaDatos;
    }

    public ModeloMeses getBuscarMes() {
        return buscarMes;
    }

    public void setBuscarMes(ModeloMeses buscarMes) {
        this.buscarMes = buscarMes;
    }

    public List<ModeloMeses> getMeses() {
        return meses;
    }

    public void setMeses(List<ModeloMeses> meses) {
        this.meses = meses;
    }

    public String getBuscar() {
        return buscar;
    }

    public void setBuscar(String buscar) {
        this.buscar = buscar;
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

        String pathSalida = directorioReportes + File.separator + "Lecturas.xls";
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
            HSSFSheet s = wb.createSheet("Lecturas");

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
            chfe.setCellValue(new HSSFRichTextString("# Medidor"));
            chfe.setCellStyle(estiloCelda);

            HSSFCell chfe1 = r.createCell(j++);
            chfe1.setCellValue(new HSSFRichTextString("Nombre"));
            chfe1.setCellStyle(estiloCelda);

            HSSFCell chfe11 = r.createCell(j++);
            chfe11.setCellValue(new HSSFRichTextString("Apellido"));
            chfe11.setCellStyle(estiloCelda);

            HSSFCell ch1 = r.createCell(j++);
            ch1.setCellValue(new HSSFRichTextString("Lectura anterior"));
            ch1.setCellStyle(estiloCelda);

            HSSFCell ch2 = r.createCell(j++);
            ch2.setCellValue(new HSSFRichTextString("Lectura actual"));
            ch2.setCellStyle(estiloCelda);

            HSSFCell ch22 = r.createCell(j++);
            ch22.setCellValue(new HSSFRichTextString("Metros cubicos"));
            ch22.setCellStyle(estiloCelda);

            int rownum = 1;
            int i = 0;

            for (Lectura item : listaDatos) {
                i = 0;

                r = s.createRow(rownum);

                HSSFCell cf = r.createCell(i++);
                cf.setCellValue(new HSSFRichTextString(item.getIdMedidor().getMedNumero().toString()));

                HSSFCell cf1 = r.createCell(i++);
                cf1.setCellValue(new HSSFRichTextString(item.getIdMedidor().getIdPredio().getIdPropietario().getPropNombre().toString()));

                HSSFCell cf11 = r.createCell(i++);
                cf11.setCellValue(new HSSFRichTextString(item.getIdMedidor().getIdPredio().getIdPropietario().getPropApellido().toString()));

                HSSFCell c0 = r.createCell(i++);
                c0.setCellValue(new HSSFRichTextString(item.getLecAnterior().toString()));

                HSSFCell c1 = r.createCell(i++);
                c1.setCellValue(new HSSFRichTextString(item.getLecActual().toString()));

                HSSFCell c11 = r.createCell(i++);
                c11.setCellValue(new HSSFRichTextString(item.getLecMetrosCubicos().toString()));

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

    public List<ModeloMeses> getAnios() {
        return anios;
    }

    public void setAnios(List<ModeloMeses> anios) {
        this.anios = anios;
    }

    public ModeloMeses getBuscarAnio() {
        return buscarAnio;
    }

    public void setBuscarAnio(ModeloMeses buscarAnio) {
        this.buscarAnio = buscarAnio;
    }

}
