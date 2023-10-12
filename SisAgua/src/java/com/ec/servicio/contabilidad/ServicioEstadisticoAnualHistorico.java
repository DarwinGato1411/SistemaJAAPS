/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio.contabilidad;

import com.ec.entidad.EstadisticoAnualHistorico;

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
public class ServicioEstadisticoAnualHistorico {
    
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(EstadisticoAnualHistorico estadisticoAnualHistorico) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(estadisticoAnualHistorico);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar estadisticoAnualHistorico " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(EstadisticoAnualHistorico estadisticoAnualHistorico) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(estadisticoAnualHistorico));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  estadisticoAnualHistorico " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(EstadisticoAnualHistorico estadisticoAnualHistorico) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(estadisticoAnualHistorico);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar estadisticoAnualHistorico " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<EstadisticoAnualHistorico> findEstadisticoAnual(Date inicio , Date fin ) {

        List<EstadisticoAnualHistorico> listaEstadisticoMensual = new ArrayList<EstadisticoAnualHistorico>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM EstadisticoAnualHistorico a WHERE a.fechaInicio BETWEEN :inicio and :fin  ORDER BY a.rubro ASC");
            query.setParameter("inicio",inicio);
            query.setParameter("fin",fin);

            listaEstadisticoMensual = (List<EstadisticoAnualHistorico>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta estadisticoAnualHistorico findByNombre " + e.getMessage());
        } finally {
            em.close();
        }

        return listaEstadisticoMensual;
    }

   
}
