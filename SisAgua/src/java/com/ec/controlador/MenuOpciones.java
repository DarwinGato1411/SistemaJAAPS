/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.dao.DetalleFacturaDAO;
import com.ec.entidad.Parametrizar;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioParametrizar;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;

/**
 *
 * @author gato
 */
public class MenuOpciones extends SelectorComposer<Component> {

//    @Wire("#btnAdministrar")
//    Button btnAdministrar;
//    @Wire("#btnCotizar")
//    Button btnCotizar;
//    @Wire("#btnCompaginado")
//    Button btnCompaginado;
//    @Wire("#btnGiganto")
//    Button btnGiganto;
//    @Wire("#btnDigital")
//    Button btnDigital;
//    @Wire("#btnUsuarios")
//    Button btnUsuarios;
//    @Wire("#btnIngresoCompras")
//    Button btnIngresoCompras;
    @Wire("#btnFacturar")
    Menuitem btnFacturar;
    @Wire("#menuVentas")
    Menu menuVentas;
    @Wire("#menuCompras")
    Menu menuCompras;
    @Wire("#menuKardex")
    Menu menuKardex;
    @Wire("#menuReportes")
    Menu menuReportes;
    @Wire("#menuContabilidad")
    Menu menuContabilidad;
    @Wire("#btnAdministarVenta")
    Menuitem btnAdministarVenta;
    UserCredential credential = new UserCredential();
    private String acceso = "";

 

    public MenuOpciones() {
        Session sess = Sessions.getCurrent();
        UserCredential cre = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        credential = cre;
        
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

    }

    @Listen("onClick = #buttonConsultar")
    public void buttonConsultar() {
        Executions.sendRedirect("/consultas.zul");
    }

    @Listen("onClick = #btnFacturar")
    public void doFacturar() {
        Executions.sendRedirect("/venta/facturar.zul");
    }

    @Listen("onClick = #btnKardex")
    public void btnKardex() {
        Executions.sendRedirect("/administrar/kardex.zul");
    }

    @Listen("onClick = #btnSinFact")
    public void btnSinFactura() {
        Executions.sendRedirect("/venta/ventadiaria.zul");
    }

    @Listen("onClick = #btnAdministarVenta")
    public void doAdministrarVenta() {
        Executions.sendRedirect("/venta/administrar.zul");
    }

    @Listen("onClick = #btnConsulta")
    public void doConsultas() {
        Executions.sendRedirect("/compra/consultas.zul");
    }

    @Listen("onClick = #btnReporte")
    public void doReportes() {
        Executions.sendRedirect("/venta/listanotaventa.zul");
    }

    @Listen("onClick = #btnNotaVentas")
    public void doAdministrarNotaVenta() {
        Executions.sendRedirect("/venta/listafacturas.zul");
    }

    @Listen("onClick = #btnNotaVenta")
    public void btnNotaVenta() {
        Executions.sendRedirect("/venta/listanotaventa.zul");
    }

    @Listen("onClick = #btnGuiaRemision")
    public void btnGuiaRemision() {
        Executions.sendRedirect("/venta/listaguia.zul");
    }

    @Listen("onClick = #btnCierreCaja")
    public void doCierreCaja() {
        Executions.sendRedirect("/venta/cierrecaja.zul");
    }

    @Listen("onClick = #btnProformasEmitidas")
    public void doListaProformas() {
        Executions.sendRedirect("/venta/listacotizaciones.zul");
    }

    @Listen("onClick = #btnIngresoCompras")
    public void doIngresoCompras() {
        Executions.sendRedirect("/compra/compras.zul");
    }

    @Listen("onClick = #btnListaComprasSRI")
    public void btnListaComprasSRI() {
        Executions.sendRedirect("/compra/listacomprassri.zul");
    }

    @Listen("onClick = #btnListaCompras")
    public void doInventario() {
        Executions.sendRedirect("/compra/listacompras.zul");
    }

    @Listen("onClick = #btnListaVentaDiaria")
    public void doListaVentaDiaria() {
        Executions.sendRedirect("/administrar/listaventadiaria.zul");
    }

    @Listen("onClick = #btnSistema")
    public void doAdministrarSistema() {
        Executions.sendRedirect("/administrar/administrarusuarios.zul");
    }

    @Listen("onClick = #btnPedidos")
    public void doRealizarPedidos() {
        Executions.sendRedirect("/administrar/pedidos.zul");
    }

    @Listen("onClick = #btnMailMasivo")
    public void mailMasivo() {
        Executions.sendRedirect("/administrar/publicidad.zul");
    }

    @Listen("onClick = #btnContabilidad")
    public void btnContabilidad() {
        Executions.sendRedirect("/contabilidad/ventasats.zul");
    }

    @Listen("onClick = #btnContabilidadATS")
    public void btnContabilidadATS() {
        Executions.sendRedirect("/contabilidad/ats.zul");
    }

    @Listen("onClick = #btnRetenciones")
    public void btnRetenciones() {
        Executions.sendRedirect("/compra/listaretencion.zul");
    }

    @Listen("onClick = #btnBalanceUtilidad")
    public void btnBalanceUtilidad() {
        Executions.sendRedirect("/contabilidad/compraventa.zul");
    }

    @Listen("onClick = #btnCrearRet")
    public void btnCrearRet() {
        Executions.sendRedirect("/contabilidad/retencion.zul");
    }

    @Listen("onClick = #btnRetencionesCasillero")
    public void btnRetencionesCasillero() {
        Executions.sendRedirect("/compra/listaretencioncasillero.zul");
    }

//    @Listen("onClick = #btnCuClase")
//    public void btnCuClase() {
//        Executions.sendRedirect("/contabilidad/clase.zul");
//    }
//
//    @Listen("onClick = #btnCuCuenta")
//    public void btnCuCuenta() {
//        Executions.sendRedirect("/contabilidad/cuenta.zul");
//    }
//    @Listen("onClick = #btnCuGrupo")
//    public void btnCuGrupo() {
//        Executions.sendRedirect("/contabilidad/grupo.zul");
//    }
    @Listen("onClick = #btnPlanCuenta")
    public void btnCuSubClase() {
        Executions.sendRedirect("/contabilidad/plancuentas.zul");
    }

    @Listen("onClick = #btnListaSubContable")
    public void btnListaSubContable() {
        Executions.sendRedirect("/contabilidad/visualizarsubcuenta.zul");
    }

    @Listen("onClick = #btnListaTipoRetencion")
    public void btnListaTipoRetencion() {
        Executions.sendRedirect("/contabilidad/codigoretenciones.zul");
    }

    @Listen("onClick = #btnAsientoConta")
    public void btnAsientoConta() {
        Executions.sendRedirect("/contabilidad/ingresoasientomanual.zul");
    }

    @Listen("onClick = #btnReportesConta")
    public void btnReportesConta() {
        Executions.sendRedirect("/contabilidad/reportesConta.zul");
    }

    @Listen("onClick = #btnFactAutori")
    public void btnFactAutori() {
        Executions.sendRedirect("/venta/facturasautorizadas.zul");
    }

    @Listen("onClick = #btnNotaCredito")
    public void btnNotaCredito() {
        Executions.sendRedirect("/venta/listanc.zul");
    }

    @Listen("onClick = #btnEmitirGuiaRemision")
    public void btnEmitirGuiaRemision() {
        Executions.sendRedirect("/venta/guia.zul");
    }

    @Listen("onClick = #btnVentaRuta")
    public void btnVentaRuta() {
        Executions.sendRedirect("/venta/listaventaruta.zul");
    }

    @Listen("onClick = #btnRepVentCant")
    public void btnRepVentCant() {
        Executions.sendRedirect("/contabilidad/cantproducto.zul");
    }

    @Listen("onClick = #btnAjuste")
    public void btnAjuste() {
        Executions.sendRedirect("/administrar/ajuste.zul");
    }

    @Listen("onClick = #btnAcumuladoventas")
    public void btnAcumuladoventas() {
        Executions.sendRedirect("/reportevistas/ventaaniomes.zul");
    }

    @Listen("onClick = #btnRotacionProducto")
    public void btnRotacionProducto() {
        Executions.sendRedirect("/reportevistas/rotacion.zul");
    }

    @Listen("onClick = #btnKardexPorProd")
    public void btnKardexPorProd() {
        Executions.sendRedirect("/reportevistas/kardexproductos.zul");
    }

    @Listen("onClick = #btnListarCierre")
    public void btnListarCierre() {
        Executions.sendRedirect("/reportevistas/listacierrecaja.zul");
    }

    @Listen("onClick = #btnLecturas")
    public void btnLecturas() {
        Executions.sendRedirect("/venta/admlecturas.zul");
    }

    @Listen("onClick = #btnEstadisticoMensual")
    public void btnEstadisticoMensual() {
        Executions.sendRedirect("/contabilidad/estadisticomensual.zul");
    }

    @Listen("onClick = #btnReporteDetCob")
    public void btnReporteDetCob() {
        Executions.sendRedirect("/contabilidad/listadodetalladoord.zul");
    }

    @Command
    public void facturar(@BindingParam("valor") DetalleFacturaDAO valor) {
        Executions.sendRedirect("/venta/facturar.zul");
    }

    
    @Listen("onClick = #btnHistoricoLecturas")
    public void btnHistoricoLecturas() {
        Executions.sendRedirect("/venta/historicolecturas.zul");
    }
    @Listen("onClick = #btnMedACtivo")
    public void btnMedACtivo() {
        Executions.sendRedirect("/venta/lectura_activo_inactivo.zul");
    }
    
    @Listen("onClick = #btnListaNeAll")
    public void btnListaNeAll() {
        Executions.sendRedirect("/venta/listanotaentregaall.zul");
    }
    
    @Listen("onClick = #btnCierreCajaUsu")
    public void btnCierreCaja() {
//        if (credential.getUsuarioSistema().getUsuNivel() != 1) {
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/nuevo/cierrecaja.zul", null, null);
        window.doModal();
//        } else {
//            Clients.showNotification("El usuario administrador no puede cerrar una caja",
//                    Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 5000, true);
//        }

    }

    @Command
    public void nuevoProducto() {

        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/nuevo/producto.zul", null, null);
        window.doModal();

    }

    @Command
    public void nuevoCliente() {

        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/nuevo/cliente.zul", null, null);
        window.doModal();

    }

    

    public UserCredential getCredential() {
        return credential;
    }

    public void setCredential(UserCredential credential) {
        this.credential = credential;
    }

    public String getAcceso() {
        return acceso;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }

 

}
