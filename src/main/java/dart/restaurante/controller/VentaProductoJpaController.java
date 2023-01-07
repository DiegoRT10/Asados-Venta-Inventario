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
import dart.restaurante.dao.Producto;
import dart.restaurante.dao.VentaDia;
import dart.restaurante.dao.VentaProducto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
            Producto idProducto = ventaProducto.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getId());
                ventaProducto.setIdProducto(idProducto);
            }
            VentaDia idVentaDia = ventaProducto.getIdVentaDia();
            if (idVentaDia != null) {
                idVentaDia = em.getReference(idVentaDia.getClass(), idVentaDia.getId());
                ventaProducto.setIdVentaDia(idVentaDia);
            }
            em.persist(ventaProducto);
            if (idProducto != null) {
                idProducto.getVentaProductoCollection().add(ventaProducto);
                idProducto = em.merge(idProducto);
            }
            if (idVentaDia != null) {
                idVentaDia.getVentaProductoCollection().add(ventaProducto);
                idVentaDia = em.merge(idVentaDia);
            }
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
            VentaProducto persistentVentaProducto = em.find(VentaProducto.class, ventaProducto.getId());
            Producto idProductoOld = persistentVentaProducto.getIdProducto();
            Producto idProductoNew = ventaProducto.getIdProducto();
            VentaDia idVentaDiaOld = persistentVentaProducto.getIdVentaDia();
            VentaDia idVentaDiaNew = ventaProducto.getIdVentaDia();
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getId());
                ventaProducto.setIdProducto(idProductoNew);
            }
            if (idVentaDiaNew != null) {
                idVentaDiaNew = em.getReference(idVentaDiaNew.getClass(), idVentaDiaNew.getId());
                ventaProducto.setIdVentaDia(idVentaDiaNew);
            }
            ventaProducto = em.merge(ventaProducto);
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getVentaProductoCollection().remove(ventaProducto);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getVentaProductoCollection().add(ventaProducto);
                idProductoNew = em.merge(idProductoNew);
            }
            if (idVentaDiaOld != null && !idVentaDiaOld.equals(idVentaDiaNew)) {
                idVentaDiaOld.getVentaProductoCollection().remove(ventaProducto);
                idVentaDiaOld = em.merge(idVentaDiaOld);
            }
            if (idVentaDiaNew != null && !idVentaDiaNew.equals(idVentaDiaOld)) {
                idVentaDiaNew.getVentaProductoCollection().add(ventaProducto);
                idVentaDiaNew = em.merge(idVentaDiaNew);
            }
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
            Producto idProducto = ventaProducto.getIdProducto();
            if (idProducto != null) {
                idProducto.getVentaProductoCollection().remove(ventaProducto);
                idProducto = em.merge(idProducto);
            }
            VentaDia idVentaDia = ventaProducto.getIdVentaDia();
            if (idVentaDia != null) {
                idVentaDia.getVentaProductoCollection().remove(ventaProducto);
                idVentaDia = em.merge(idVentaDia);
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
