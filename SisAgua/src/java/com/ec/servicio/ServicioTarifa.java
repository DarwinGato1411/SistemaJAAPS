/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.Tarifa;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioTarifa {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(Tarifa tarifa) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(tarifa);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar tarifa " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(Tarifa tarifa) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(tarifa));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  tarifa " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(Tarifa tarifa) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(tarifa);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar tarifa " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<Tarifa> findLikeTariDescripcion(String valor) {

        List<Tarifa> listaTarifas = new ArrayList<Tarifa>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Tarifa a WHERE a.tariDescripcion LIKE :tariDescripcion ORDER BY a.tariDescripcion ASC");
            query.setParameter("tariDescripcion", "%" + valor + "%");

            listaTarifas = (List<Tarifa>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta tarifa findLikeEstlNombre " + e.getMessage());
        } finally {
            em.close();
        }

        return listaTarifas;
    }

    public Tarifa findEstlSigla(Integer valor) {

        List<Tarifa> listaTarifas = new ArrayList<Tarifa>();
        Tarifa entidad = null;
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Tarifa a WHERE a.idMedidor =:idMedidor");
            query.setParameter("idMedidor", valor);
            listaTarifas = (List<Tarifa>) query.getResultList();
            entidad = listaTarifas.size() > 0 ? listaTarifas.get(0) : null;

            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta findEstlSigla" + e.getMessage());
        } finally {
            em.close();
        }

        return entidad;
    }
}
