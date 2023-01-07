/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dart.restaurante.controller;

import dart.restaurante.controller.exceptions.NonexistentEntityException;
import dart.restaurante.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dart.restaurante.dao.Caja;
import dart.restaurante.dao.Cierre;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
        if (cierre.getCajaCollection() == null) {
            cierre.setCajaCollection(new ArrayList<Caja>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Caja> attachedCajaCollection = new ArrayList<Caja>();
            for (Caja cajaCollectionCajaToAttach : cierre.getCajaCollection()) {
                cajaCollectionCajaToAttach = em.getReference(cajaCollectionCajaToAttach.getClass(), cajaCollectionCajaToAttach.getId());
                attachedCajaCollection.add(cajaCollectionCajaToAttach);
            }
            cierre.setCajaCollection(attachedCajaCollection);
            em.persist(cierre);
            for (Caja cajaCollectionCaja : cierre.getCajaCollection()) {
                Cierre oldIdCierreOfCajaCollectionCaja = cajaCollectionCaja.getIdCierre();
                cajaCollectionCaja.setIdCierre(cierre);
                cajaCollectionCaja = em.merge(cajaCollectionCaja);
                if (oldIdCierreOfCajaCollectionCaja != null) {
                    oldIdCierreOfCajaCollectionCaja.getCajaCollection().remove(cajaCollectionCaja);
                    oldIdCierreOfCajaCollectionCaja = em.merge(oldIdCierreOfCajaCollectionCaja);
                }
            }
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
            Cierre persistentCierre = em.find(Cierre.class, cierre.getId());
            Collection<Caja> cajaCollectionOld = persistentCierre.getCajaCollection();
            Collection<Caja> cajaCollectionNew = cierre.getCajaCollection();
            Collection<Caja> attachedCajaCollectionNew = new ArrayList<Caja>();
            for (Caja cajaCollectionNewCajaToAttach : cajaCollectionNew) {
                cajaCollectionNewCajaToAttach = em.getReference(cajaCollectionNewCajaToAttach.getClass(), cajaCollectionNewCajaToAttach.getId());
                attachedCajaCollectionNew.add(cajaCollectionNewCajaToAttach);
            }
            cajaCollectionNew = attachedCajaCollectionNew;
            cierre.setCajaCollection(cajaCollectionNew);
            cierre = em.merge(cierre);
            for (Caja cajaCollectionOldCaja : cajaCollectionOld) {
                if (!cajaCollectionNew.contains(cajaCollectionOldCaja)) {
                    cajaCollectionOldCaja.setIdCierre(null);
                    cajaCollectionOldCaja = em.merge(cajaCollectionOldCaja);
                }
            }
            for (Caja cajaCollectionNewCaja : cajaCollectionNew) {
                if (!cajaCollectionOld.contains(cajaCollectionNewCaja)) {
                    Cierre oldIdCierreOfCajaCollectionNewCaja = cajaCollectionNewCaja.getIdCierre();
                    cajaCollectionNewCaja.setIdCierre(cierre);
                    cajaCollectionNewCaja = em.merge(cajaCollectionNewCaja);
                    if (oldIdCierreOfCajaCollectionNewCaja != null && !oldIdCierreOfCajaCollectionNewCaja.equals(cierre)) {
                        oldIdCierreOfCajaCollectionNewCaja.getCajaCollection().remove(cajaCollectionNewCaja);
                        oldIdCierreOfCajaCollectionNewCaja = em.merge(oldIdCierreOfCajaCollectionNewCaja);
                    }
                }
            }
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
            Collection<Caja> cajaCollection = cierre.getCajaCollection();
            for (Caja cajaCollectionCaja : cajaCollection) {
                cajaCollectionCaja.setIdCierre(null);
                cajaCollectionCaja = em.merge(cajaCollectionCaja);
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
