/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.Clases;
import javax.persistence.EntityManager;

/**
 *
 * @author HC
 */
public class ServicioPlanCuentas {
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(Clases clases) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(clases);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar clase " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(Clases clases) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(clases));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  clase" + e);
        } finally {
            em.close();
        }

    }

    public void modificar(Clases clases) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(clases);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar clase");
        } finally {
            em.close();
        }

    }   
}
