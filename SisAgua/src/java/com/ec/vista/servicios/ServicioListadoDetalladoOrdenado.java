/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.vista.servicios;
import com.ec.servicio.HelperPersistencia;
import com.ec.vistas.CantVentProductos;
import com.ec.vistas.ListadoDetalladoOrdenado;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;
/**
 *
 * @author HC
 */
public class ServicioListadoDetalladoOrdenado {
     private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public List<ListadoDetalladoOrdenado> findByMes(Date inicio, Date fin) {

        List<ListadoDetalladoOrdenado> listaLisDetalladoOrdenado = new ArrayList<ListadoDetalladoOrdenado>();
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
           // Query query = em.createQuery("SELECT new com.ec.vistas.RotacionProducto(max(a.prodNombre),SUM(a.cantidadVenta),SUM(a.valorVentaProducto)) FROM RotacionProducto a WHERE a.facFecha BETWEEN :inicio and :fin  GROUP BY a.idProducto" );
          // Query query = em.createQuery("SELECT new com.ec.vistas.ListadoDetalladoOrdenado( a.idFactura, a.facNumero, a.facFecha, a.medNumero, a.propNombre, a.propApellido, a.facMetrosCubicos )FROM ListadoDetallado a  WHERE a.facFecha  BETWEEN :inicio and :fin " );
           
          Query query = em.createQuery("SELECT new com.ec.vistas.ListadoDetalladoOrdenado( a.idFactura, a.facNumero, a.facFecha, a.medNumero, a.propNombre, a.propApellido, a.facMetrosCubicos, a.agua, a.excedente, a.alcantarrillado, a.desechos, a.medioAmbiente, a.interes1, a.interes2, a.facTotal )FROM ListadoDetalladoOrdenado a  WHERE a.facFecha  BETWEEN :inicio and :fin " );
            query.setParameter("inicio", inicio);
            query.setParameter("fin", fin);
            listaLisDetalladoOrdenado = (List<ListadoDetalladoOrdenado>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta Listado Detallado Ordenado " + e.getMessage());
        } finally {
            em.close();
        }

        return listaLisDetalladoOrdenado;
    }
}