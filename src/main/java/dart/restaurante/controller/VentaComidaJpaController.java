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
import dart.restaurante.dao.Comida;
import dart.restaurante.dao.VentaComida;
import dart.restaurante.dao.VentaDia;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
            Comida idComida = ventaComida.getIdComida();
            if (idComida != null) {
                idComida = em.getReference(idComida.getClass(), idComida.getId());
                ventaComida.setIdComida(idComida);
            }
            VentaDia idVentaDia = ventaComida.getIdVentaDia();
            if (idVentaDia != null) {
                idVentaDia = em.getReference(idVentaDia.getClass(), idVentaDia.getId());
                ventaComida.setIdVentaDia(idVentaDia);
            }
            em.persist(ventaComida);
            if (idComida != null) {
                idComida.getVentaComidaCollection().add(ventaComida);
                idComida = em.merge(idComida);
            }
            if (idVentaDia != null) {
                idVentaDia.getVentaComidaCollection().add(ventaComida);
                idVentaDia = em.merge(idVentaDia);
            }
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
            VentaComida persistentVentaComida = em.find(VentaComida.class, ventaComida.getId());
            Comida idComidaOld = persistentVentaComida.getIdComida();
            Comida idComidaNew = ventaComida.getIdComida();
            VentaDia idVentaDiaOld = persistentVentaComida.getIdVentaDia();
            VentaDia idVentaDiaNew = ventaComida.getIdVentaDia();
            if (idComidaNew != null) {
                idComidaNew = em.getReference(idComidaNew.getClass(), idComidaNew.getId());
                ventaComida.setIdComida(idComidaNew);
            }
            if (idVentaDiaNew != null) {
                idVentaDiaNew = em.getReference(idVentaDiaNew.getClass(), idVentaDiaNew.getId());
                ventaComida.setIdVentaDia(idVentaDiaNew);
            }
            ventaComida = em.merge(ventaComida);
            if (idComidaOld != null && !idComidaOld.equals(idComidaNew)) {
                idComidaOld.getVentaComidaCollection().remove(ventaComida);
                idComidaOld = em.merge(idComidaOld);
            }
            if (idComidaNew != null && !idComidaNew.equals(idComidaOld)) {
                idComidaNew.getVentaComidaCollection().add(ventaComida);
                idComidaNew = em.merge(idComidaNew);
            }
            if (idVentaDiaOld != null && !idVentaDiaOld.equals(idVentaDiaNew)) {
                idVentaDiaOld.getVentaComidaCollection().remove(ventaComida);
                idVentaDiaOld = em.merge(idVentaDiaOld);
            }
            if (idVentaDiaNew != null && !idVentaDiaNew.equals(idVentaDiaOld)) {
                idVentaDiaNew.getVentaComidaCollection().add(ventaComida);
                idVentaDiaNew = em.merge(idVentaDiaNew);
            }
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
            Comida idComida = ventaComida.getIdComida();
            if (idComida != null) {
                idComida.getVentaComidaCollection().remove(ventaComida);
                idComida = em.merge(idComida);
            }
            VentaDia idVentaDia = ventaComida.getIdVentaDia();
            if (idVentaDia != null) {
                idVentaDia.getVentaComidaCollection().remove(ventaComida);
                idVentaDia = em.merge(idVentaDia);
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
