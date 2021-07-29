/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Cliente;
import com.ec.entidad.Parametrizar;
import com.ec.entidad.Predio;
import com.ec.entidad.Propietario;
import com.ec.servicio.ServicioCliente;
import com.ec.servicio.ServicioParametrizar;
import com.ec.servicio.ServicioPredio;
import com.ec.servicio.ServicioPropietario;
import com.ec.untilitario.AduanaJson;
import com.ec.untilitario.ArchivoUtils;
import java.math.BigDecimal;

import java.util.Date;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author gato
 */
public class NuevoPropietario {

    @Wire
    Window wPropietario;
    private Propietario entidad = new Propietario();
    ServicioPropietario servicioPropietario = new ServicioPropietario();
    ServicioParametrizar servicioParametrizar = new ServicioParametrizar();
    private Parametrizar parametrizar = null;
    private String accion = "create";

    ServicioCliente servicioCliente = new ServicioCliente();
    ServicioPredio servicioPredio = new ServicioPredio();

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") Propietario valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        parametrizar = servicioParametrizar.FindALlParametrizar();
        if (valor != null) {
            this.entidad = valor;
            accion = "update";
        } else {
            this.entidad = new Propietario();
            accion = "create";

        }
    }

    @Command
    @NotifyChange({"entidad"})
    public void buscarAduana() {
        if (entidad.getPorpCedula() != null) {
            if (!entidad.getPorpCedula().equals("")) {
                String cedulaBuscar = "";
                if (entidad.getPorpCedula().length() > 10) {
                    cedulaBuscar = entidad.getPorpCedula().substring(0, 10);
                } else {
                    cedulaBuscar = entidad.getPorpCedula();
                }
                AduanaJson aduana = ArchivoUtils.obtenerdatoAduana(cedulaBuscar);
                if (aduana != null) {

                    String nombreApellido[] = aduana.getNombre().split(" ");
                    String nombrePersona = "";
                    String apellidoPersona = "";
                    switch (nombreApellido.length) {
                        case 1:
                            apellidoPersona = nombreApellido[0];
                            nombrePersona = "A";
                            break;
                        case 2:
                            apellidoPersona = nombreApellido[0];
                            nombrePersona = nombreApellido[1];
                            break;
                        case 3:
                            apellidoPersona = nombreApellido[0] + " " + nombreApellido[1];
                            nombrePersona = nombreApellido[2];
                            break;
                        case 4:
                            apellidoPersona = nombreApellido[0] + " " + nombreApellido[1];
                            nombrePersona = nombreApellido[2] + " " + nombreApellido[3];
                            break;
                        default:
                            break;
                    }
                    entidad.setPropApellido(apellidoPersona);
                    entidad.setPropNombre(nombrePersona);

                }
            }
        }

    }

    @Command
    public void guardar() {
        if (entidad.getPorpCedula() != null
                && entidad.getPropNombre() != null) {
            Cliente cliente = servicioCliente.FindClienteForCedulaPropietario(entidad.getPorpCedula());

            Propietario prop = servicioPropietario.findCedula(entidad.getPorpCedula());
            if (prop != null && accion.equals("create")) {
                Clients.showNotification("El propietario ya se encuentra registrado..!",
                        Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
                return;
            }
            if (cliente == null) {
                cliente = new Cliente();
                cliente.setCliCedula(entidad.getPorpCedula());
                cliente.setCliNombres(entidad.getPropNombre());
                cliente.setCliApellidos(entidad.getPropApellido());
                cliente.setCliNombre(entidad.getPropNombre() + " " + entidad.getPropApellido());
                cliente.setCliRazonSocial(entidad.getPropNombre() + " " + entidad.getPropApellido());
                cliente.setCliDireccion(entidad.getPropDireccion());
                cliente.setCiudad(parametrizar.getParCiudad());
                cliente.setCliMovil("0999999999");
                cliente.setCliTelefono("099999999");
                cliente.setCliCorreo(parametrizar.getParCorreoDefecto());

                servicioCliente.crear(cliente);

            } else {
                cliente.setCliCedula(entidad.getPorpCedula());
                cliente.setCliNombres(entidad.getPropNombre());
                cliente.setCliApellidos(entidad.getPropApellido());
                cliente.setCliNombre(entidad.getPropNombre() + " " + entidad.getPropApellido());
                cliente.setCliRazonSocial(entidad.getPropNombre() + " " + entidad.getPropApellido());
                cliente.setCliDireccion(entidad.getPropDireccion());
                servicioCliente.modificar(cliente);
            }
            if (accion.equals("create")) {
                servicioPropietario.crear(entidad);
                Predio predio = new Predio();
                predio.setIdPropietario(entidad);
                predio.setPreArea(BigDecimal.ZERO);
                predio.setPreFechaRegistro(new Date());
                predio.setPredDireccion(entidad.getPropDireccion());
                predio.setPreUbicacion(entidad.getPropSector());
                predio.setPredNumero(Integer.SIZE);
                servicioPredio.crear(predio);

                wPropietario.detach();
            } else {
                servicioPropietario.modificar(entidad);
                // Messagebox.show("Guardado con exito");

                wPropietario.detach();
            }

        } else {

            Clients.showNotification("Verifique la informacion requerida",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
        }
    }

    @Command
    @NotifyChange("entidad")
    public void obtenerEdad() {
        if (entidad.getPorpFechaNac() != null) {
            BigDecimal edad = ArchivoUtils.obtenerEdad(entidad.getPorpFechaNac());
            entidad.setPropEdad(edad.intValue());
        }
    }

    public Propietario getEntidad() {
        return entidad;
    }

    public void setEntidad(Propietario entidad) {
        this.entidad = entidad;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

}
