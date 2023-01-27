/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dart.restaurante.controller;

import dart.restaurante.controller.exceptions.IllegalOrphanException;
import dart.restaurante.controller.exceptions.NonexistentEntityException;
import dart.restaurante.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dart.restaurante.dao.Caja;
import dart.restaurante.dao.Gasto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diego Ramos
 */
public class GastoJpaController implements Serializable {

    public GastoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Gasto gasto) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Caja idCajaOrphanCheck = gasto.getIdCaja();
        if (idCajaOrphanCheck != null) {
            Gasto oldIdGastoOfIdCaja = idCajaOrphanCheck.getIdGasto();
            if (oldIdGastoOfIdCaja != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Caja " + idCajaOrphanCheck + " already has an item of type Gasto whose idCaja column cannot be null. Please make another selection for the idCaja field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Caja idCaja = gasto.getIdCaja();
            if (idCaja != null) {
                idCaja = em.getReference(idCaja.getClass(), idCaja.getId());
                gasto.setIdCaja(idCaja);
            }
            em.persist(gasto);
            if (idCaja != null) {
                idCaja.setIdGasto(gasto);
                idCaja = em.merge(idCaja);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGasto(gasto.getId()) != null) {
                throw new PreexistingEntityException("Gasto " + gasto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Gasto gasto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Gasto persistentGasto = em.find(Gasto.class, gasto.getId());
            Caja idCajaOld = persistentGasto.getIdCaja();
            Caja idCajaNew = gasto.getIdCaja();
            List<String> illegalOrphanMessages = null;
            if (idCajaNew != null && !idCajaNew.equals(idCajaOld)) {
                Gasto oldIdGastoOfIdCaja = idCajaNew.getIdGasto();
                if (oldIdGastoOfIdCaja != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Caja " + idCajaNew + " already has an item of type Gasto whose idCaja column cannot be null. Please make another selection for the idCaja field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCajaNew != null) {
                idCajaNew = em.getReference(idCajaNew.getClass(), idCajaNew.getId());
                gasto.setIdCaja(idCajaNew);
            }
            gasto = em.merge(gasto);
            if (idCajaOld != null && !idCajaOld.equals(idCajaNew)) {
                idCajaOld.setIdGasto(null);
                idCajaOld = em.merge(idCajaOld);
            }
            if (idCajaNew != null && !idCajaNew.equals(idCajaOld)) {
                idCajaNew.setIdGasto(gasto);
                idCajaNew = em.merge(idCajaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = gasto.getId();
                if (findGasto(id) == null) {
                    throw new NonexistentEntityException("The gasto with id " + id + " no longer exists.");
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
            Gasto gasto;
            try {
                gasto = em.getReference(Gasto.class, id);
                gasto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gasto with id " + id + " no longer exists.", enfe);
            }
            Caja idCaja = gasto.getIdCaja();
            if (idCaja != null) {
                idCaja.setIdGasto(null);
                idCaja = em.merge(idCaja);
            }
            em.remove(gasto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Gasto> findGastoEntities() {
        return findGastoEntities(true, -1, -1);
    }

    public List<Gasto> findGastoEntities(int maxResults, int firstResult) {
        return findGastoEntities(false, maxResults, firstResult);
    }

    private List<Gasto> findGastoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Gasto.class));
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

    public Gasto findGasto(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Gasto.class, id);
        } finally {
            em.close();
        }
    }

    public int getGastoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Gasto> rt = cq.from(Gasto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
