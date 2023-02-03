/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.DetalleTarifa;
import com.ec.entidad.Tarifa;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioDetalleTarifas {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(DetalleTarifa detalleTarifa) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(detalleTarifa);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar detalleTarifa " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(DetalleTarifa detalleTarifa) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(detalleTarifa));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  detalleTarifa " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(DetalleTarifa detalleTarifa) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(detalleTarifa);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar detalleTarifa " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public DetalleTarifa findIdTarifaAndMetros(Tarifa idTarifa, BigDecimal metros) {

        List<DetalleTarifa> listaDetalleTarifas = new ArrayList<DetalleTarifa>();
        DetalleTarifa entidad = null;
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM DetalleTarifa a WHERE a.idTarifa =:idTarifa AND (a.dettMetroInicial <= :dettMetroInicial AND a.dettMetroFinal >= :dettMetroFinal) ");
            query.setParameter("idTarifa", idTarifa);
            query.setParameter("dettMetroInicial", metros);
            query.setParameter("dettMetroFinal", metros);
            listaDetalleTarifas = (List<DetalleTarifa>) query.getResultList();
            entidad = listaDetalleTarifas.size() > 0 ? listaDetalleTarifas.get(0) : null;

            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta findIdTarifaAndMetros" + e.getMessage());
        } finally {
            em.close();
        }

        return entidad;
    }

    public List<DetalleTarifa> findIdTarifa(Tarifa idTarifa) {

        List<DetalleTarifa> listaDetalleTarifas = new ArrayList<DetalleTarifa>();
        //   DetalleTarifa entidad = null;
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM DetalleTarifa a WHERE a.idTarifa =:idTarifa ORDER BY a.idDetalleTar asc");
            query.setParameter("idTarifa", idTarifa);
            listaDetalleTarifas = (List<DetalleTarifa>) query.getResultList();

            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en la consulta findIdTarifa" + e.getMessage());
        } finally {
            em.close();
        }

        return listaDetalleTarifas;
    }
}
