/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dart.restaurante.controller;

import dart.restaurante.controller.exceptions.IllegalOrphanException;
import dart.restaurante.controller.exceptions.NonexistentEntityException;
import dart.restaurante.controller.exceptions.PreexistingEntityException;
import dart.restaurante.dao.Caja;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dart.restaurante.dao.Gasto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diego Ramos
 */
public class CajaJpaController implements Serializable {

    public CajaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Caja caja) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Gasto idGasto = caja.getIdGasto();
            if (idGasto != null) {
                idGasto = em.getReference(idGasto.getClass(), idGasto.getId());
                caja.setIdGasto(idGasto);
            }
            em.persist(caja);
            if (idGasto != null) {
                Caja oldIdCajaOfIdGasto = idGasto.getIdCaja();
                if (oldIdCajaOfIdGasto != null) {
                    oldIdCajaOfIdGasto.setIdGasto(null);
                    oldIdCajaOfIdGasto = em.merge(oldIdCajaOfIdGasto);
                }
                idGasto.setIdCaja(caja);
                idGasto = em.merge(idGasto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCaja(caja.getId()) != null) {
                throw new PreexistingEntityException("Caja " + caja + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Caja caja) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Caja persistentCaja = em.find(Caja.class, caja.getId());
            Gasto idGastoOld = persistentCaja.getIdGasto();
            Gasto idGastoNew = caja.getIdGasto();
            List<String> illegalOrphanMessages = null;
            if (idGastoOld != null && !idGastoOld.equals(idGastoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Gasto " + idGastoOld + " since its idCaja field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idGastoNew != null) {
                idGastoNew = em.getReference(idGastoNew.getClass(), idGastoNew.getId());
                caja.setIdGasto(idGastoNew);
            }
            caja = em.merge(caja);
            if (idGastoNew != null && !idGastoNew.equals(idGastoOld)) {
                Caja oldIdCajaOfIdGasto = idGastoNew.getIdCaja();
                if (oldIdCajaOfIdGasto != null) {
                    oldIdCajaOfIdGasto.setIdGasto(null);
                    oldIdCajaOfIdGasto = em.merge(oldIdCajaOfIdGasto);
                }
                idGastoNew.setIdCaja(caja);
                idGastoNew = em.merge(idGastoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = caja.getId();
                if (findCaja(id) == null) {
                    throw new NonexistentEntityException("The caja with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Caja caja;
            try {
                caja = em.getReference(Caja.class, id);
                caja.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The caja with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Gasto idGastoOrphanCheck = caja.getIdGasto();
            if (idGastoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Caja (" + caja + ") cannot be destroyed since the Gasto " + idGastoOrphanCheck + " in its idGasto field has a non-nullable idCaja field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(caja);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Caja> findCajaEntities() {
        return findCajaEntities(true, -1, -1);
    }

    public List<Caja> findCajaEntities(int maxResults, int firstResult) {
        return findCajaEntities(false, maxResults, firstResult);
    }

    private List<Caja> findCajaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Caja.class));
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

    public Caja findCaja(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Caja.class, id);
        } finally {
            em.close();
        }
    }

    public int getCajaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Caja> rt = cq.from(Caja.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
