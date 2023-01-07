/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dart.restaurante.controller;

import dart.restaurante.controller.exceptions.NonexistentEntityException;
import dart.restaurante.controller.exceptions.PreexistingEntityException;
import dart.restaurante.dao.VentaProducto;
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
public class VentaProductoJpaController implements Serializable {

    public VentaProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VentaProducto ventaProducto) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(ventaProducto);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findVentaProducto(ventaProducto.getId()) != null) {
                throw new PreexistingEntityException("VentaProducto " + ventaProducto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VentaProducto ventaProducto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ventaProducto = em.merge(ventaProducto);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = ventaProducto.getId();
                if (findVentaProducto(id) == null) {
                    throw new NonexistentEntityException("The ventaProducto with id " + id + " no longer exists.");
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
            VentaProducto ventaProducto;
            try {
                ventaProducto = em.getReference(VentaProducto.class, id);
                ventaProducto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ventaProducto with id " + id + " no longer exists.", enfe);
            }
            em.remove(ventaProducto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VentaProducto> findVentaProductoEntities() {
        return findVentaProductoEntities(true, -1, -1);
    }

    public List<VentaProducto> findVentaProductoEntities(int maxResults, int firstResult) {
        return findVentaProductoEntities(false, maxResults, firstResult);
    }

    private List<VentaProducto> findVentaProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VentaProducto.class));
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

    public VentaProducto findVentaProducto(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VentaProducto.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentaProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VentaProducto> rt = cq.from(VentaProducto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
