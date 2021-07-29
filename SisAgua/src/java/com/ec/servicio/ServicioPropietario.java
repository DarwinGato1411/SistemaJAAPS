/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.Propietario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioPropietario {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(Propietario propietario) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(propietario);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar propietario " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(Propietario propietario) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(propietario));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  propietario " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(Propietario propietario) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(propietario);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar propietario " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<Propietario> findLikeCedulaNombre(String valor) {

        List<Propietario> listaPropietarios = new ArrayList<Propietario>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Propietario a WHERE a.porpCedula LIKE :porpCedula OR a.propNombre LIKE :propNombre OR a.propApellido LIKE :propApellido ORDER BY a.propApellido ASC");
            query.setParameter("porpCedula", "%" + valor + "%");
            query.setParameter("propNombre", "%" + valor + "%");
            query.setParameter("propApellido", "%" + valor + "%");
            //query.setMaxResults(200);
            listaPropietarios = (List<Propietario>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta propietario findLikeCedulaNombre " + e.getMessage());
        } finally {
            em.close();
        }

        return listaPropietarios;
    }

    public Propietario findCedula(String valor) {

        List<Propietario> listaPropietarios = new ArrayList<Propietario>();
        Propietario entidad = null;
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Propietario a WHERE a.porpCedula =:porpCedula");
            query.setParameter("porpCedula", valor);
            listaPropietarios = (List<Propietario>) query.getResultList();
            entidad = listaPropietarios.size() > 0 ? listaPropietarios.get(0) : null;

            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta propietario findCedula" + e.getMessage());
        } finally {
            em.close();
        }

        return entidad;
    }
}
