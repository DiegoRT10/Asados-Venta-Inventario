/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dart.restaurante.controller;

import dart.restaurante.controller.exceptions.NonexistentEntityException;
import dart.restaurante.controller.exceptions.PreexistingEntityException;
import dart.restaurante.dao.Apertura;
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
public class AperturaJpaController implements Serializable {

    public AperturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Apertura apertura) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(apertura);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findApertura(apertura.getId()) != null) {
                throw new PreexistingEntityException("Apertura " + apertura + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Apertura apertura) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            apertura = em.merge(apertura);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = apertura.getId();
                if (findApertura(id) == null) {
                    throw new NonexistentEntityException("The apertura with id " + id + " no longer exists.");
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
            Apertura apertura;
            try {
                apertura = em.getReference(Apertura.class, id);
                apertura.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The apertura with id " + id + " no longer exists.", enfe);
            }
            em.remove(apertura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Apertura> findAperturaEntities() {
        return findAperturaEntities(true, -1, -1);
    }

    public List<Apertura> findAperturaEntities(int maxResults, int firstResult) {
        return findAperturaEntities(false, maxResults, firstResult);
    }

    private List<Apertura> findAperturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Apertura.class));
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

    public Apertura findApertura(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Apertura.class, id);
        } finally {
            em.close();
        }
    }

    public int getAperturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Apertura> rt = cq.from(Apertura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
