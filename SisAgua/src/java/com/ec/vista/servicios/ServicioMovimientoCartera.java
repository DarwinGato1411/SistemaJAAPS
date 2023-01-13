/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.vista.servicios;

import com.ec.servicio.HelperPersistencia;
import com.ec.vistas.ListadoDetalladoOrdenado;
import com.ec.vistas.MovimientoCartera;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author HC
 */
public class ServicioMovimientoCartera {
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public List<MovimientoCartera> findByMes(Date inicio, Date fin) {

        List<MovimientoCartera> listamovcartera = new ArrayList<MovimientoCartera>();
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
           // Query query = em.createQuery("SELECT new com.ec.vistas.RotacionProducto(max(a.prodNombre),SUM(a.cantidadVenta),SUM(a.valorVentaProducto)) FROM RotacionProducto a WHERE a.facFecha BETWEEN :inicio and :fin  GROUP BY a.idProducto" );
          // Query query = em.createQuery("SELECT new com.ec.vistas.ListadoDetalladoOrdenado( a.idFactura, a.facNumero, a.facFecha, a.medNumero, a.propNombre, a.propApellido, a.facMetrosCubicos )FROM ListadoDetallado a  WHERE a.facFecha  BETWEEN :inicio and :fin " );
           
          Query query = em.createQuery("SELECT new com.ec.vistas.MovimientoCartera(a.id, a.detDescripcion, a.facTotal, a.facFecha)FROM MovimientoCartera a  WHERE a.facFecha  BETWEEN :inicio and :fin " );
            query.setParameter("inicio", inicio);
            query.setParameter("fin", fin);
            listamovcartera = (List<MovimientoCartera>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta Movimiento Cartera " + e.getMessage());
        } finally {
            em.close();
        }

        return listamovcartera;
    }
    
}
