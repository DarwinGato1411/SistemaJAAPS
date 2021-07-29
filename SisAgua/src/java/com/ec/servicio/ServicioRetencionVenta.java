/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.RetencionVenta;
import com.ec.entidad.RetencionVenta;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioRetencionVenta {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(RetencionVenta retencionVenta) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(retencionVenta);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar retencionVenta " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(RetencionVenta retencionVenta) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(retencionVenta));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  retencionVenta" + e);
        } finally {
            em.close();
        }

    }

    public void modificar(RetencionVenta retencionVenta) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(retencionVenta);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar retencionVenta");
        } finally {
            em.close();
        }

    }

    public List<RetencionVenta> findAll() {

        List<RetencionVenta> listaRetencionVentas = new ArrayList<RetencionVenta>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT a FROM RetencionVenta a");
            listaRetencionVentas = (List<RetencionVenta>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta retencionVenta " + e.getMessage());
        } finally {
            em.close();
        }

        return listaRetencionVentas;
    }

}
