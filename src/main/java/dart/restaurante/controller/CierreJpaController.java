/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dart.restaurante.controller;

import dart.restaurante.controller.exceptions.NonexistentEntityException;
import dart.restaurante.controller.exceptions.PreexistingEntityException;
import dart.restaurante.dao.Cierre;
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
public class CierreJpaController implements Serializable {

    public CierreJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cierre cierre) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(cierre);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCierre(cierre.getId()) != null) {
                throw new PreexistingEntityException("Cierre " + cierre + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cierre cierre) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            cierre = em.merge(cierre);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = cierre.getId();
                if (findCierre(id) == null) {
                    throw new NonexistentEntityException("The cierre with id " + id + " no longer exists.");
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
            Cierre cierre;
            try {
                cierre = em.getReference(Cierre.class, id);
                cierre.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cierre with id " + id + " no longer exists.", enfe);
            }
            em.remove(cierre);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cierre> findCierreEntities() {
        return findCierreEntities(true, -1, -1);
    }

    public List<Cierre> findCierreEntities(int maxResults, int firstResult) {
        return findCierreEntities(false, maxResults, firstResult);
    }

    private List<Cierre> findCierreEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cierre.class));
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

    public Cierre findCierre(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cierre.class, id);
        } finally {
            em.close();
        }
    }

    public int getCierreCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cierre> rt = cq.from(Cierre.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
