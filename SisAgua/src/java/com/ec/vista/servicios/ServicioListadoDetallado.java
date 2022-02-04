/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.vista.servicios;

import com.ec.servicio.HelperPersistencia;
import com.ec.vistas.CantVentProductos;
import com.ec.vistas.ListadoDetallado;
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

public class ServicioListadoDetallado  {

 private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public List<ListadoDetallado> findByMes(Date inicio, Date fin) {

        List<ListadoDetallado> listaLisDetallado = new ArrayList<ListadoDetallado>();
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
          //  Query query = em.createQuery("SELECT new com.ec.vistas.RotacionProducto(max(a.prodNombre),SUM(a.cantidadVenta),SUM(a.valorVentaProducto)) FROM RotacionProducto a WHERE a.facFecha BETWEEN :inicio and :fin  GROUP BY a.idProducto" );
            Query query = em.createQuery("SELECT new com.ec.vistas.ListadoDetallado( a.idFactura, a.facNumero, a.facFecha, a.medNumero, a.propNombre, a.propApellido, a.facMetrosCubicos, a.detDescripcion, a.detTotal )FROM ListadoDetallado a  WHERE a.facFecha  BETWEEN :inicio and :fin " );
            query.setParameter("inicio", inicio);
            query.setParameter("fin", fin);
            listaLisDetallado = (List<ListadoDetallado>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta Listado Detallado " + e.getMessage());
        } finally {
            em.close();
        }

        return listaLisDetallado;
    }

    
}
 /*List<RotacionProducto> listaRotacionProductos = new ArrayList<RotacionProducto>();
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT new com.ec.vistas.RotacionProducto(max(a.prodNombre),SUM(a.cantidadVenta),SUM(a.valorVentaProducto)) FROM RotacionProducto a WHERE a.facFecha BETWEEN :inicio and :fin  GROUP BY a.idProducto" );
            query.setParameter("inicio", inicio);
            query.setParameter("fin", fin);
            listaRotacionProductos = (List<RotacionProducto>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta RotacionProducto " + e.getMessage());
        } finally {
            em.close();
        }

        return listaRotacionProductos;*/