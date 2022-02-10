/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio.contabilidad;

import com.ec.servicio.*;
import com.ec.entidad.contabilidad.CuClase;
import com.ec.entidad.Medidor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author gato
 */
public class ServicioClase {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(CuClase cuClase) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(cuClase);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar cuClase " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(CuClase cuClase) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(cuClase));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  cuClase " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(CuClase cuClase) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(cuClase);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar cuClase " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<CuClase> findByNombre(String valor) {

        List<CuClase> listaCuClases = new ArrayList<CuClase>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM CuClase a WHERE a.clasNombre  LIKE :clasNombre ORDER BY a.clasNumero ASC");
            query.setParameter("clasNombre", "%" + valor + "%");

            listaCuClases = (List<CuClase>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta cuClase findByNombre " + e.getMessage());
        } finally {
            em.close();
        }

        return listaCuClases;
    }

}
