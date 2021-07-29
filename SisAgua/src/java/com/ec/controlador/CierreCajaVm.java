/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.CierreCaja;
import com.ec.entidad.Producto;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioCierreCaja;
import com.ec.untilitario.ArchivoUtils;
import com.ec.untilitario.DispararReporte;
import com.ec.untilitario.ModeloAcumuladoDiaUsuario;
import com.ec.vista.servicios.ServicioAcumuladoDiarioUsuario;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import net.sf.jasperreports.engine.JRException;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

/**
 *
 * @author gato
 */
public class CierreCajaVm {

    @Wire
    Window windowCierre;
    private CierreCaja cierreCaja = new CierreCaja();
    ServicioCierreCaja servicioCierreCaja = new ServicioCierreCaja();
    UserCredential credential = new UserCredential();
    private Date fecha = new Date();
    private BigDecimal totFactura = BigDecimal.ZERO;
    private BigDecimal totNotaVenta = BigDecimal.ZERO;

    ServicioAcumuladoDiarioUsuario servicioAcumuladoDiarioUsuario = new ServicioAcumuladoDiarioUsuario();

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") Producto producto, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);

        Session sess = Sessions.getCurrent();
        UserCredential cre = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        credential = cre;
        cierreCaja = servicioCierreCaja.findALlCierreCajaForFechaIdUsuario(new Date(), credential.getUsuarioSistema()).get(0);
        if (servicioAcumuladoDiarioUsuario.findCierrePorUsuario(fecha, credential.getUsuarioSistema()).size() > 0) {
            ModeloAcumuladoDiaUsuario acumuladoDiaUsuario = servicioAcumuladoDiarioUsuario.findCierrePorUsuario(fecha, credential.getUsuarioSistema()).get(0);
            cierreCaja.setCieValor(ArchivoUtils.redondearDecimales(acumuladoDiaUsuario.getValorTotal(), 2).add(ArchivoUtils.redondearDecimales(cierreCaja.getCieValorInicio(), 2)));
            totFactura = ArchivoUtils.redondearDecimales(acumuladoDiaUsuario.getValorFacturas(), 2);
            totNotaVenta = ArchivoUtils.redondearDecimales(acumuladoDiaUsuario.getValorNotaVenta(), 2);
        } else {
            cierreCaja.setCieValor(BigDecimal.ZERO);
            cierreCaja.setCieCuadre(BigDecimal.ZERO);
            cierreCaja.setCieDiferencia(BigDecimal.ZERO);
            cierreCaja.setCieValorInicio(BigDecimal.ZERO);
            totFactura = BigDecimal.ZERO;
            totNotaVenta = BigDecimal.ZERO;
        }

    }

    @Command
    @NotifyChange({"cierreCaja"})
    public void calcularDiferencia() {
        if (cierreCaja.getCieCuadre() != null) {
            if (cierreCaja.getCieCuadre().doubleValue() > 0) {
                cierreCaja.setCieDiferencia(cierreCaja.getCieValor().subtract(cierreCaja.getCieCuadre()));
            }
        }

    }

    @Command
    public void guardar() {
        if (cierreCaja.getCieValorInicio() != null) {
            servicioCierreCaja.modificar(cierreCaja);
            try {
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
        windowCierre.detach();

    }

    public CierreCaja getCierreCaja() {
        return cierreCaja;
    }

    public void setCierreCaja(CierreCaja cierreCaja) {
        this.cierreCaja = cierreCaja;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotFactura() {
        return totFactura;
    }

    public void setTotFactura(BigDecimal totFactura) {
        this.totFactura = totFactura;
    }

    public BigDecimal getTotNotaVenta() {
        return totNotaVenta;
    }

    public void setTotNotaVenta(BigDecimal totNotaVenta) {
        this.totNotaVenta = totNotaVenta;
    }

}
