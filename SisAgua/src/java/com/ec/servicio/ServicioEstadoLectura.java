/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.EstadoLectura;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioEstadoLectura {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(EstadoLectura estadoLectura) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(estadoLectura);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar estadoLectura " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(EstadoLectura estadoLectura) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(estadoLectura));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  estadoLectura " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(EstadoLectura estadoLectura) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(estadoLectura);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar estadoLectura " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<EstadoLectura> findLikeEstlNombre(String valor) {

        List<EstadoLectura> listaEstadoLecturas = new ArrayList<EstadoLectura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM EstadoLectura a WHERE a.estlNombre LIKE :estlNombre");
            query.setParameter("estlNombre", valor);
            
            listaEstadoLecturas = (List<EstadoLectura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta estadoLectura findLikeEstlNombre " + e.getMessage());
        } finally {
            em.close();
        }

        return listaEstadoLecturas;
    }

    public EstadoLectura findEstlSigla(String valor) {

        List<EstadoLectura> listaEstadoLecturas = new ArrayList<EstadoLectura>();
        EstadoLectura entidad = null;
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM EstadoLectura a WHERE a.estlSigla =:estlSigla");
            query.setParameter("estlSigla", valor);
            listaEstadoLecturas = (List<EstadoLectura>) query.getResultList();
            entidad = listaEstadoLecturas.size() > 0 ? listaEstadoLecturas.get(0) : null;

            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta findEstlSigla" + e.getMessage());
        } finally {
            em.close();
        }

        return entidad;
    }
}
