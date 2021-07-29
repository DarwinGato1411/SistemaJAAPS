/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.Predio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioPredio {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(Predio predio) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(predio);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar predio " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(Predio predio) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(predio));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  predio " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(Predio predio) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(predio);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar predio " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<Predio> findLikePredNumero(String valor) {

        List<Predio> listaPredios = new ArrayList<Predio>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Predio a WHERE a.predNumero LIKE :predNumero ");
            query.setParameter("predNumero", "%" + valor + "%");
            query.setMaxResults(200);
            listaPredios = (List<Predio>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta predio findLikePredNumero " + e.getMessage());
        } finally {
            em.close();
        }

        return listaPredios;
    }

    public Predio findPredNumero(String valor) {

        List<Predio> listaPredios = new ArrayList<Predio>();
        Predio entidad = null;
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Predio a WHERE a.predNumero =:predNumero");
            query.setParameter("predNumero", valor);
            listaPredios = (List<Predio>) query.getResultList();
            entidad = listaPredios.size() > 0 ? listaPredios.get(0) : null;

            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta predio findPredNumero" + e.getMessage());
        } finally {
            em.close();
        }

        return entidad;
    }

    public List<Predio> findLikePreDireccionCedulaNombre(String valor) {

        List<Predio> listaPredios = new ArrayList<Predio>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Predio a WHERE a.predDireccion LIKE :predDireccion OR a.idPropietario.porpCedula LIKE :porpCedula OR a.idPropietario.propNombre LIKE :propNombre OR a.idPropietario.propApellido LIKE :propApellido");
            query.setParameter("predDireccion", "%" + valor + "%");
            query.setParameter("porpCedula", "%" + valor + "%");
            query.setParameter("propNombre", "%" + valor + "%");
            query.setParameter("propApellido", "%" + valor + "%");
            query.setMaxResults(200);
            listaPredios = (List<Predio>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta predio findLikePredNumero " + e.getMessage());
        } finally {
            em.close();
        }

        return listaPredios;
    }
}
