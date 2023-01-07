/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dart.restaurante.controller;

import dart.restaurante.controller.exceptions.NonexistentEntityException;
import dart.restaurante.controller.exceptions.PreexistingEntityException;
import dart.restaurante.dao.Apertura;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dart.restaurante.dao.Caja;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
        if (apertura.getCajaCollection() == null) {
            apertura.setCajaCollection(new ArrayList<Caja>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Caja> attachedCajaCollection = new ArrayList<Caja>();
            for (Caja cajaCollectionCajaToAttach : apertura.getCajaCollection()) {
                cajaCollectionCajaToAttach = em.getReference(cajaCollectionCajaToAttach.getClass(), cajaCollectionCajaToAttach.getId());
                attachedCajaCollection.add(cajaCollectionCajaToAttach);
            }
            apertura.setCajaCollection(attachedCajaCollection);
            em.persist(apertura);
            for (Caja cajaCollectionCaja : apertura.getCajaCollection()) {
                Apertura oldIdAperturaOfCajaCollectionCaja = cajaCollectionCaja.getIdApertura();
                cajaCollectionCaja.setIdApertura(apertura);
                cajaCollectionCaja = em.merge(cajaCollectionCaja);
                if (oldIdAperturaOfCajaCollectionCaja != null) {
                    oldIdAperturaOfCajaCollectionCaja.getCajaCollection().remove(cajaCollectionCaja);
                    oldIdAperturaOfCajaCollectionCaja = em.merge(oldIdAperturaOfCajaCollectionCaja);
                }
            }
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
            Apertura persistentApertura = em.find(Apertura.class, apertura.getId());
            Collection<Caja> cajaCollectionOld = persistentApertura.getCajaCollection();
            Collection<Caja> cajaCollectionNew = apertura.getCajaCollection();
            Collection<Caja> attachedCajaCollectionNew = new ArrayList<Caja>();
            for (Caja cajaCollectionNewCajaToAttach : cajaCollectionNew) {
                cajaCollectionNewCajaToAttach = em.getReference(cajaCollectionNewCajaToAttach.getClass(), cajaCollectionNewCajaToAttach.getId());
                attachedCajaCollectionNew.add(cajaCollectionNewCajaToAttach);
            }
            cajaCollectionNew = attachedCajaCollectionNew;
            apertura.setCajaCollection(cajaCollectionNew);
            apertura = em.merge(apertura);
            for (Caja cajaCollectionOldCaja : cajaCollectionOld) {
                if (!cajaCollectionNew.contains(cajaCollectionOldCaja)) {
                    cajaCollectionOldCaja.setIdApertura(null);
                    cajaCollectionOldCaja = em.merge(cajaCollectionOldCaja);
                }
            }
            for (Caja cajaCollectionNewCaja : cajaCollectionNew) {
                if (!cajaCollectionOld.contains(cajaCollectionNewCaja)) {
                    Apertura oldIdAperturaOfCajaCollectionNewCaja = cajaCollectionNewCaja.getIdApertura();
                    cajaCollectionNewCaja.setIdApertura(apertura);
                    cajaCollectionNewCaja = em.merge(cajaCollectionNewCaja);
                    if (oldIdAperturaOfCajaCollectionNewCaja != null && !oldIdAperturaOfCajaCollectionNewCaja.equals(apertura)) {
                        oldIdAperturaOfCajaCollectionNewCaja.getCajaCollection().remove(cajaCollectionNewCaja);
                        oldIdAperturaOfCajaCollectionNewCaja = em.merge(oldIdAperturaOfCajaCollectionNewCaja);
                    }
                }
            }
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
            Collection<Caja> cajaCollection = apertura.getCajaCollection();
            for (Caja cajaCollectionCaja : cajaCollection) {
                cajaCollectionCaja.setIdApertura(null);
                cajaCollectionCaja = em.merge(cajaCollectionCaja);
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
