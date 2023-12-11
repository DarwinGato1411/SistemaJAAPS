/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.Medidor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioMedidor {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(Medidor medidor) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(medidor);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar medidor " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(Medidor medidor) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(medidor));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  medidor " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(Medidor medidor) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(medidor);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar medidor " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<Medidor> findLikeNombreApellidoCedula(String valor) {

        List<Medidor> listaMedidors = new ArrayList<Medidor>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Medidor a WHERE a.idPredio.idPropietario.porpCedula LIKE :porpCedula OR a.idPredio.idPropietario.propNombre LIKE :propNombre OR a.idPredio.idPropietario.propApellido LIKE :propApellido ORDER BY CAST(a.medNumero as NUMERIC) ASC");
            query.setParameter("porpCedula", "%" + valor + "%");
            query.setParameter("propNombre", "%" + valor + "%");
            query.setParameter("propApellido", "%" + valor + "%");
            //query.setMaxResults(200);
            listaMedidors = (List<Medidor>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta medidor findLikeMedNumero " + e.getMessage());
        } finally {
            em.close();
        }

        return listaMedidors;
    }

    public List<Medidor> findLikeNombreApellidoCedulaActivo(String valor, boolean activo) {

        List<Medidor> listaMedidors = new ArrayList<Medidor>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            String queryString = "SELECT a FROM Medidor a WHERE a.medActivo = :medActivo "
                    + "AND (a.idPredio.idPropietario.porpCedula LIKE :porpCedula "
                    + "OR a.idPredio.idPropietario.propNombre LIKE :propNombre "
                    + "OR a.idPredio.idPropietario.propApellido LIKE :propApellido) "
                    + "ORDER BY CAST(a.medNumero as NUMERIC) ASC";
            Query query = em.createQuery(queryString);
            query.setParameter("porpCedula", "%" + valor + "%");
            query.setParameter("propNombre", "%" + valor + "%");
            query.setParameter("propApellido", "%" + valor + "%");
            query.setParameter("medActivo", activo);
            System.out.println("asdasd");
            //query.setMaxResults(200);
            listaMedidors = (List<Medidor>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta medidor findLikeMedNumero " + e.getMessage());
        } finally {
            em.close();
        }

        return listaMedidors;
    }

    public List<Medidor> findMedidorNumero(String valor) {

        List<Medidor> listaMedidors = new ArrayList<Medidor>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Medidor a WHERE a.medNumero=:medNumero ORDER BY CAST(a.medNumero as NUMERIC) ASC");
            query.setParameter("medNumero", valor);
            //query.setMaxResults(200);
            listaMedidors = (List<Medidor>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta medidor findLikeMedNumero " + e.getMessage());
        } finally {
            em.close();
        }

        return listaMedidors;
    }

    public Medidor findMedNumero(String valor) {

        List<Medidor> listaMedidors = new ArrayList<Medidor>();
        Medidor entidad = null;
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Medidor a WHERE a.medNumero =:medNumero");
            query.setParameter("medNumero", valor);
            listaMedidors = (List<Medidor>) query.getResultList();
            entidad = listaMedidors.size() > 0 ? listaMedidors.get(0) : null;

            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta findMedNumero" + e.getMessage());
        } finally {
            em.close();
        }

        return entidad;
    }

    public Medidor findUltimoMedidorRegistrado() {

        List<Medidor> listaMedidors = new ArrayList<Medidor>();
        Medidor entidad = null;
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Medidor a ORDER BY a.medNumero desc");
            query.setMaxResults(1);
            listaMedidors = (List<Medidor>) query.getResultList();
            entidad = listaMedidors.size() > 0 ? listaMedidors.get(0) : null;

            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta findMedNumero" + e.getMessage());
        } finally {
            em.close();
        }

        return entidad;
    }

    public List<Medidor> findMedidorForNumero(String valor) {

        List<Medidor> listaMedidors = new ArrayList<Medidor>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Medidor a WHERE a.medNumero =:medNumero  AND a.medActivo=:medActivo ORDER BY a.idPredio.idPropietario.propNombre ASC");
            query.setParameter("medNumero", valor);
            query.setParameter("medActivo", Boolean.TRUE);
            //query.setMaxResults(200);
            listaMedidors = (List<Medidor>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta medidor findLikeMedNumero " + e.getMessage());
        } finally {
            em.close();
        }

        return listaMedidors;
    }

    public List<Medidor> findLikeMedNumero(String valor) {

        List<Medidor> listaMedidors = new ArrayList<Medidor>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Medidor a WHERE a.medNumero LIKE :medNumero ORDER BY a.idPredio.idPropietario.propNombre ASC");
            query.setParameter("medNumero", "%" + valor + "%");
            //query.setMaxResults(200);
            listaMedidors = (List<Medidor>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta medidor findLikeMedNumero " + e.getMessage());
        } finally {
            em.close();
        }

        return listaMedidors;
    }

    public List<Medidor> findByNombreApellido(String valor) {

        List<Medidor> listaMedidors = new ArrayList<Medidor>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Medidor a WHERE (a.idPredio.idPropietario.propNombre LIKE :propNombre OR  a.idPredio.idPropietario.propApellido LIKE :propApellido) AND a.medActivo=:medActivo ORDER BY a.idPredio.idPropietario.propNombre ASC");
            query.setParameter("propNombre", "%" + valor + "%");
            query.setParameter("propApellido", "%" + valor + "%");
            query.setParameter("medActivo", Boolean.TRUE);
            //query.setMaxResults(200);
            listaMedidors = (List<Medidor>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta medidor findLikeMedNumero " + e.getMessage());
        } finally {
            em.close();
        }

        return listaMedidors;
    }
}
