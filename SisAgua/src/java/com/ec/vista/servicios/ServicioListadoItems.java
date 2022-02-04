/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.vista.servicios;
import com.ec.servicio.HelperPersistencia;
import com.ec.vistas.ListadoItems;
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
public class ServicioListadoItems {
    
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public List<ListadoItems> listadoTotal() {

        List<ListadoItems> listaLisItems = new ArrayList<ListadoItems>();
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
          //  Query query = em.createQuery("SELECT new com.ec.vistas.RotacionProducto(max(a.prodNombre),SUM(a.cantidadVenta),SUM(a.valorVentaProducto)) FROM RotacionProducto a WHERE a.facFecha BETWEEN :inicio and :fin  GROUP BY a.idProducto" );
            Query query = em.createQuery("SELECT new com.ec.vistas.ListadoItems( a.idItems, a.detDescripcion, a.sumaTotal )FROM ListadoItems a  " );

            listaLisItems = (List<ListadoItems>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta Listado Items " + e.getMessage());
        } finally {
            em.close();
        }

        return listaLisItems;
    }
    
}
