/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dart.restaurante.controller;

import dart.restaurante.controller.exceptions.NonexistentEntityException;
import dart.restaurante.controller.exceptions.PreexistingEntityException;
import dart.restaurante.dao.Cliente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dart.restaurante.dao.VentaDia;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diego Ramos
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) throws PreexistingEntityException, Exception {
        if (cliente.getVentaDiaCollection() == null) {
            cliente.setVentaDiaCollection(new ArrayList<VentaDia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<VentaDia> attachedVentaDiaCollection = new ArrayList<VentaDia>();
            for (VentaDia ventaDiaCollectionVentaDiaToAttach : cliente.getVentaDiaCollection()) {
                ventaDiaCollectionVentaDiaToAttach = em.getReference(ventaDiaCollectionVentaDiaToAttach.getClass(), ventaDiaCollectionVentaDiaToAttach.getId());
                attachedVentaDiaCollection.add(ventaDiaCollectionVentaDiaToAttach);
            }
            cliente.setVentaDiaCollection(attachedVentaDiaCollection);
            em.persist(cliente);
            for (VentaDia ventaDiaCollectionVentaDia : cliente.getVentaDiaCollection()) {
                Cliente oldIdClienteOfVentaDiaCollectionVentaDia = ventaDiaCollectionVentaDia.getIdCliente();
                ventaDiaCollectionVentaDia.setIdCliente(cliente);
                ventaDiaCollectionVentaDia = em.merge(ventaDiaCollectionVentaDia);
                if (oldIdClienteOfVentaDiaCollectionVentaDia != null) {
                    oldIdClienteOfVentaDiaCollectionVentaDia.getVentaDiaCollection().remove(ventaDiaCollectionVentaDia);
                    oldIdClienteOfVentaDiaCollectionVentaDia = em.merge(oldIdClienteOfVentaDiaCollectionVentaDia);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCliente(cliente.getId()) != null) {
                throw new PreexistingEntityException("Cliente " + cliente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getId());
            Collection<VentaDia> ventaDiaCollectionOld = persistentCliente.getVentaDiaCollection();
            Collection<VentaDia> ventaDiaCollectionNew = cliente.getVentaDiaCollection();
            Collection<VentaDia> attachedVentaDiaCollectionNew = new ArrayList<VentaDia>();
            for (VentaDia ventaDiaCollectionNewVentaDiaToAttach : ventaDiaCollectionNew) {
                ventaDiaCollectionNewVentaDiaToAttach = em.getReference(ventaDiaCollectionNewVentaDiaToAttach.getClass(), ventaDiaCollectionNewVentaDiaToAttach.getId());
                attachedVentaDiaCollectionNew.add(ventaDiaCollectionNewVentaDiaToAttach);
            }
            ventaDiaCollectionNew = attachedVentaDiaCollectionNew;
            cliente.setVentaDiaCollection(ventaDiaCollectionNew);
            cliente = em.merge(cliente);
            for (VentaDia ventaDiaCollectionOldVentaDia : ventaDiaCollectionOld) {
                if (!ventaDiaCollectionNew.contains(ventaDiaCollectionOldVentaDia)) {
                    ventaDiaCollectionOldVentaDia.setIdCliente(null);
                    ventaDiaCollectionOldVentaDia = em.merge(ventaDiaCollectionOldVentaDia);
                }
            }
            for (VentaDia ventaDiaCollectionNewVentaDia : ventaDiaCollectionNew) {
                if (!ventaDiaCollectionOld.contains(ventaDiaCollectionNewVentaDia)) {
                    Cliente oldIdClienteOfVentaDiaCollectionNewVentaDia = ventaDiaCollectionNewVentaDia.getIdCliente();
                    ventaDiaCollectionNewVentaDia.setIdCliente(cliente);
                    ventaDiaCollectionNewVentaDia = em.merge(ventaDiaCollectionNewVentaDia);
                    if (oldIdClienteOfVentaDiaCollectionNewVentaDia != null && !oldIdClienteOfVentaDiaCollectionNewVentaDia.equals(cliente)) {
                        oldIdClienteOfVentaDiaCollectionNewVentaDia.getVentaDiaCollection().remove(ventaDiaCollectionNewVentaDia);
                        oldIdClienteOfVentaDiaCollectionNewVentaDia = em.merge(oldIdClienteOfVentaDiaCollectionNewVentaDia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = cliente.getId();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            Collection<VentaDia> ventaDiaCollection = cliente.getVentaDiaCollection();
            for (VentaDia ventaDiaCollectionVentaDia : ventaDiaCollection) {
                ventaDiaCollectionVentaDia.setIdCliente(null);
                ventaDiaCollectionVentaDia = em.merge(ventaDiaCollectionVentaDia);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
