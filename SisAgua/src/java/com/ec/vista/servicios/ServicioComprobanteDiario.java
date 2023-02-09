/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.vista.servicios;

import com.ec.entidad.contabilidad.ComprobanteDiario;
import com.ec.servicio.HelperPersistencia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author HC
 */
public class ServicioComprobanteDiario {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public List<ComprobanteDiario> findByFecha(Date inicio) {

        List<ComprobanteDiario> listaLisDetallado = new ArrayList<ComprobanteDiario>();
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            //  Query query = em.createQuery("SELECT new com.ec.vistas.RotacionProducto(max(a.prodNombre),SUM(a.cantidadVenta),SUM(a.valorVentaProducto)) FROM RotacionProducto a WHERE a.facFecha BETWEEN :inicio and :fin  GROUP BY a.idProducto" );
            Query query = em.createQuery("SELECT a FROM ComprobanteDiario a  WHERE a.fechaAcSubcuenta=:inicio ");
            query.setParameter("inicio", inicio);
//            query.setParameter("iniciofin", fin);
            listaLisDetallado = (List<ComprobanteDiario>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta Listado comprobante diarioç " + e.getMessage());
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
