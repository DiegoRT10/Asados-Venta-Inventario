/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dart.restaurante.controller;

import dart.restaurante.controller.exceptions.NonexistentEntityException;
import dart.restaurante.controller.exceptions.PreexistingEntityException;
import dart.restaurante.dao.Comida;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dart.restaurante.dao.VentaComida;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diego Ramos
 */
public class ComidaJpaController implements Serializable {

    public ComidaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Comida comida) throws PreexistingEntityException, Exception {
        if (comida.getVentaComidaCollection() == null) {
            comida.setVentaComidaCollection(new ArrayList<VentaComida>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<VentaComida> attachedVentaComidaCollection = new ArrayList<VentaComida>();
            for (VentaComida ventaComidaCollectionVentaComidaToAttach : comida.getVentaComidaCollection()) {
                ventaComidaCollectionVentaComidaToAttach = em.getReference(ventaComidaCollectionVentaComidaToAttach.getClass(), ventaComidaCollectionVentaComidaToAttach.getId());
                attachedVentaComidaCollection.add(ventaComidaCollectionVentaComidaToAttach);
            }
            comida.setVentaComidaCollection(attachedVentaComidaCollection);
            em.persist(comida);
            for (VentaComida ventaComidaCollectionVentaComida : comida.getVentaComidaCollection()) {
                Comida oldIdComidaOfVentaComidaCollectionVentaComida = ventaComidaCollectionVentaComida.getIdComida();
                ventaComidaCollectionVentaComida.setIdComida(comida);
                ventaComidaCollectionVentaComida = em.merge(ventaComidaCollectionVentaComida);
                if (oldIdComidaOfVentaComidaCollectionVentaComida != null) {
                    oldIdComidaOfVentaComidaCollectionVentaComida.getVentaComidaCollection().remove(ventaComidaCollectionVentaComida);
                    oldIdComidaOfVentaComidaCollectionVentaComida = em.merge(oldIdComidaOfVentaComidaCollectionVentaComida);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findComida(comida.getId()) != null) {
                throw new PreexistingEntityException("Comida " + comida + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Comida comida) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comida persistentComida = em.find(Comida.class, comida.getId());
            Collection<VentaComida> ventaComidaCollectionOld = persistentComida.getVentaComidaCollection();
            Collection<VentaComida> ventaComidaCollectionNew = comida.getVentaComidaCollection();
            Collection<VentaComida> attachedVentaComidaCollectionNew = new ArrayList<VentaComida>();
            for (VentaComida ventaComidaCollectionNewVentaComidaToAttach : ventaComidaCollectionNew) {
                ventaComidaCollectionNewVentaComidaToAttach = em.getReference(ventaComidaCollectionNewVentaComidaToAttach.getClass(), ventaComidaCollectionNewVentaComidaToAttach.getId());
                attachedVentaComidaCollectionNew.add(ventaComidaCollectionNewVentaComidaToAttach);
            }
            ventaComidaCollectionNew = attachedVentaComidaCollectionNew;
            comida.setVentaComidaCollection(ventaComidaCollectionNew);
            comida = em.merge(comida);
            for (VentaComida ventaComidaCollectionOldVentaComida : ventaComidaCollectionOld) {
                if (!ventaComidaCollectionNew.contains(ventaComidaCollectionOldVentaComida)) {
                    ventaComidaCollectionOldVentaComida.setIdComida(null);
                    ventaComidaCollectionOldVentaComida = em.merge(ventaComidaCollectionOldVentaComida);
                }
            }
            for (VentaComida ventaComidaCollectionNewVentaComida : ventaComidaCollectionNew) {
                if (!ventaComidaCollectionOld.contains(ventaComidaCollectionNewVentaComida)) {
                    Comida oldIdComidaOfVentaComidaCollectionNewVentaComida = ventaComidaCollectionNewVentaComida.getIdComida();
                    ventaComidaCollectionNewVentaComida.setIdComida(comida);
                    ventaComidaCollectionNewVentaComida = em.merge(ventaComidaCollectionNewVentaComida);
                    if (oldIdComidaOfVentaComidaCollectionNewVentaComida != null && !oldIdComidaOfVentaComidaCollectionNewVentaComida.equals(comida)) {
                        oldIdComidaOfVentaComidaCollectionNewVentaComida.getVentaComidaCollection().remove(ventaComidaCollectionNewVentaComida);
                        oldIdComidaOfVentaComidaCollectionNewVentaComida = em.merge(oldIdComidaOfVentaComidaCollectionNewVentaComida);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = comida.getId();
                if (findComida(id) == null) {
                    throw new NonexistentEntityException("The comida with id " + id + " no longer exists.");
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
            Comida comida;
            try {
                comida = em.getReference(Comida.class, id);
                comida.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comida with id " + id + " no longer exists.", enfe);
            }
            Collection<VentaComida> ventaComidaCollection = comida.getVentaComidaCollection();
            for (VentaComida ventaComidaCollectionVentaComida : ventaComidaCollection) {
                ventaComidaCollectionVentaComida.setIdComida(null);
                ventaComidaCollectionVentaComida = em.merge(ventaComidaCollectionVentaComida);
            }
            em.remove(comida);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Comida> findComidaEntities() {
        return findComidaEntities(true, -1, -1);
    }

    public List<Comida> findComidaEntities(int maxResults, int firstResult) {
        return findComidaEntities(false, maxResults, firstResult);
    }

    private List<Comida> findComidaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Comida.class));
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

    public Comida findComida(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Comida.class, id);
        } finally {
            em.close();
        }
    }

    public int getComidaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Comida> rt = cq.from(Comida.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
