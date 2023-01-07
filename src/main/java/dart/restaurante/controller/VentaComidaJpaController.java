/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dart.restaurante.controller;

import dart.restaurante.controller.exceptions.NonexistentEntityException;
import dart.restaurante.controller.exceptions.PreexistingEntityException;
import dart.restaurante.dao.VentaComida;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Diego Ramos
 */
public class VentaComidaJpaController implements Serializable {

    public VentaComidaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VentaComida ventaComida) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(ventaComida);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findVentaComida(ventaComida.getId()) != null) {
                throw new PreexistingEntityException("VentaComida " + ventaComida + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VentaComida ventaComida) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ventaComida = em.merge(ventaComida);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = ventaComida.getId();
                if (findVentaComida(id) == null) {
                    throw new NonexistentEntityException("The ventaComida with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VentaComida ventaComida;
            try {
                ventaComida = em.getReference(VentaComida.class, id);
                ventaComida.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ventaComida with id " + id + " no longer exists.", enfe);
            }
            em.remove(ventaComida);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VentaComida> findVentaComidaEntities() {
        return findVentaComidaEntities(true, -1, -1);
    }

    public List<VentaComida> findVentaComidaEntities(int maxResults, int firstResult) {
        return findVentaComidaEntities(false, maxResults, firstResult);
    }

    private List<VentaComida> findVentaComidaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VentaComida.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public VentaComida findVentaComida(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VentaComida.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentaComidaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VentaComida> rt = cq.from(VentaComida.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
