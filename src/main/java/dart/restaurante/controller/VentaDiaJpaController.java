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
import dart.restaurante.dao.Cliente;
import dart.restaurante.dao.Usuario;
import dart.restaurante.dao.VentaProducto;
import java.util.ArrayList;
import java.util.Collection;
import dart.restaurante.dao.VentaComida;
import dart.restaurante.dao.VentaDia;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diego Ramos
 */
public class VentaDiaJpaController implements Serializable {

    public VentaDiaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VentaDia ventaDia) throws PreexistingEntityException, Exception {
        if (ventaDia.getVentaProductoCollection() == null) {
            ventaDia.setVentaProductoCollection(new ArrayList<VentaProducto>());
        }
        if (ventaDia.getVentaComidaCollection() == null) {
            ventaDia.setVentaComidaCollection(new ArrayList<VentaComida>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente idCliente = ventaDia.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getId());
                ventaDia.setIdCliente(idCliente);
            }
            Usuario idUsuario = ventaDia.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getId());
                ventaDia.setIdUsuario(idUsuario);
            }
            Collection<VentaProducto> attachedVentaProductoCollection = new ArrayList<VentaProducto>();
            for (VentaProducto ventaProductoCollectionVentaProductoToAttach : ventaDia.getVentaProductoCollection()) {
                ventaProductoCollectionVentaProductoToAttach = em.getReference(ventaProductoCollectionVentaProductoToAttach.getClass(), ventaProductoCollectionVentaProductoToAttach.getId());
                attachedVentaProductoCollection.add(ventaProductoCollectionVentaProductoToAttach);
            }
            ventaDia.setVentaProductoCollection(attachedVentaProductoCollection);
            Collection<VentaComida> attachedVentaComidaCollection = new ArrayList<VentaComida>();
            for (VentaComida ventaComidaCollectionVentaComidaToAttach : ventaDia.getVentaComidaCollection()) {
                ventaComidaCollectionVentaComidaToAttach = em.getReference(ventaComidaCollectionVentaComidaToAttach.getClass(), ventaComidaCollectionVentaComidaToAttach.getId());
                attachedVentaComidaCollection.add(ventaComidaCollectionVentaComidaToAttach);
            }
            ventaDia.setVentaComidaCollection(attachedVentaComidaCollection);
            em.persist(ventaDia);
            if (idCliente != null) {
                idCliente.getVentaDiaCollection().add(ventaDia);
                idCliente = em.merge(idCliente);
            }
            if (idUsuario != null) {
                idUsuario.getVentaDiaCollection().add(ventaDia);
                idUsuario = em.merge(idUsuario);
            }
            for (VentaProducto ventaProductoCollectionVentaProducto : ventaDia.getVentaProductoCollection()) {
                VentaDia oldIdVentaDiaOfVentaProductoCollectionVentaProducto = ventaProductoCollectionVentaProducto.getIdVentaDia();
                ventaProductoCollectionVentaProducto.setIdVentaDia(ventaDia);
                ventaProductoCollectionVentaProducto = em.merge(ventaProductoCollectionVentaProducto);
                if (oldIdVentaDiaOfVentaProductoCollectionVentaProducto != null) {
                    oldIdVentaDiaOfVentaProductoCollectionVentaProducto.getVentaProductoCollection().remove(ventaProductoCollectionVentaProducto);
                    oldIdVentaDiaOfVentaProductoCollectionVentaProducto = em.merge(oldIdVentaDiaOfVentaProductoCollectionVentaProducto);
                }
            }
            for (VentaComida ventaComidaCollectionVentaComida : ventaDia.getVentaComidaCollection()) {
                VentaDia oldIdVentaDiaOfVentaComidaCollectionVentaComida = ventaComidaCollectionVentaComida.getIdVentaDia();
                ventaComidaCollectionVentaComida.setIdVentaDia(ventaDia);
                ventaComidaCollectionVentaComida = em.merge(ventaComidaCollectionVentaComida);
                if (oldIdVentaDiaOfVentaComidaCollectionVentaComida != null) {
                    oldIdVentaDiaOfVentaComidaCollectionVentaComida.getVentaComidaCollection().remove(ventaComidaCollectionVentaComida);
                    oldIdVentaDiaOfVentaComidaCollectionVentaComida = em.merge(oldIdVentaDiaOfVentaComidaCollectionVentaComida);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findVentaDia(ventaDia.getId()) != null) {
                throw new PreexistingEntityException("VentaDia " + ventaDia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VentaDia ventaDia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VentaDia persistentVentaDia = em.find(VentaDia.class, ventaDia.getId());
            Cliente idClienteOld = persistentVentaDia.getIdCliente();
            Cliente idClienteNew = ventaDia.getIdCliente();
            Usuario idUsuarioOld = persistentVentaDia.getIdUsuario();
            Usuario idUsuarioNew = ventaDia.getIdUsuario();
            Collection<VentaProducto> ventaProductoCollectionOld = persistentVentaDia.getVentaProductoCollection();
            Collection<VentaProducto> ventaProductoCollectionNew = ventaDia.getVentaProductoCollection();
            Collection<VentaComida> ventaComidaCollectionOld = persistentVentaDia.getVentaComidaCollection();
            Collection<VentaComida> ventaComidaCollectionNew = ventaDia.getVentaComidaCollection();
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getId());
                ventaDia.setIdCliente(idClienteNew);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getId());
                ventaDia.setIdUsuario(idUsuarioNew);
            }
            Collection<VentaProducto> attachedVentaProductoCollectionNew = new ArrayList<VentaProducto>();
            for (VentaProducto ventaProductoCollectionNewVentaProductoToAttach : ventaProductoCollectionNew) {
                ventaProductoCollectionNewVentaProductoToAttach = em.getReference(ventaProductoCollectionNewVentaProductoToAttach.getClass(), ventaProductoCollectionNewVentaProductoToAttach.getId());
                attachedVentaProductoCollectionNew.add(ventaProductoCollectionNewVentaProductoToAttach);
            }
            ventaProductoCollectionNew = attachedVentaProductoCollectionNew;
            ventaDia.setVentaProductoCollection(ventaProductoCollectionNew);
            Collection<VentaComida> attachedVentaComidaCollectionNew = new ArrayList<VentaComida>();
            for (VentaComida ventaComidaCollectionNewVentaComidaToAttach : ventaComidaCollectionNew) {
                ventaComidaCollectionNewVentaComidaToAttach = em.getReference(ventaComidaCollectionNewVentaComidaToAttach.getClass(), ventaComidaCollectionNewVentaComidaToAttach.getId());
                attachedVentaComidaCollectionNew.add(ventaComidaCollectionNewVentaComidaToAttach);
            }
            ventaComidaCollectionNew = attachedVentaComidaCollectionNew;
            ventaDia.setVentaComidaCollection(ventaComidaCollectionNew);
            ventaDia = em.merge(ventaDia);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getVentaDiaCollection().remove(ventaDia);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getVentaDiaCollection().add(ventaDia);
                idClienteNew = em.merge(idClienteNew);
            }
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getVentaDiaCollection().remove(ventaDia);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getVentaDiaCollection().add(ventaDia);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            for (VentaProducto ventaProductoCollectionOldVentaProducto : ventaProductoCollectionOld) {
                if (!ventaProductoCollectionNew.contains(ventaProductoCollectionOldVentaProducto)) {
                    ventaProductoCollectionOldVentaProducto.setIdVentaDia(null);
                    ventaProductoCollectionOldVentaProducto = em.merge(ventaProductoCollectionOldVentaProducto);
                }
            }
            for (VentaProducto ventaProductoCollectionNewVentaProducto : ventaProductoCollectionNew) {
                if (!ventaProductoCollectionOld.contains(ventaProductoCollectionNewVentaProducto)) {
                    VentaDia oldIdVentaDiaOfVentaProductoCollectionNewVentaProducto = ventaProductoCollectionNewVentaProducto.getIdVentaDia();
                    ventaProductoCollectionNewVentaProducto.setIdVentaDia(ventaDia);
                    ventaProductoCollectionNewVentaProducto = em.merge(ventaProductoCollectionNewVentaProducto);
                    if (oldIdVentaDiaOfVentaProductoCollectionNewVentaProducto != null && !oldIdVentaDiaOfVentaProductoCollectionNewVentaProducto.equals(ventaDia)) {
                        oldIdVentaDiaOfVentaProductoCollectionNewVentaProducto.getVentaProductoCollection().remove(ventaProductoCollectionNewVentaProducto);
                        oldIdVentaDiaOfVentaProductoCollectionNewVentaProducto = em.merge(oldIdVentaDiaOfVentaProductoCollectionNewVentaProducto);
                    }
                }
            }
            for (VentaComida ventaComidaCollectionOldVentaComida : ventaComidaCollectionOld) {
                if (!ventaComidaCollectionNew.contains(ventaComidaCollectionOldVentaComida)) {
                    ventaComidaCollectionOldVentaComida.setIdVentaDia(null);
                    ventaComidaCollectionOldVentaComida = em.merge(ventaComidaCollectionOldVentaComida);
                }
            }
            for (VentaComida ventaComidaCollectionNewVentaComida : ventaComidaCollectionNew) {
                if (!ventaComidaCollectionOld.contains(ventaComidaCollectionNewVentaComida)) {
                    VentaDia oldIdVentaDiaOfVentaComidaCollectionNewVentaComida = ventaComidaCollectionNewVentaComida.getIdVentaDia();
                    ventaComidaCollectionNewVentaComida.setIdVentaDia(ventaDia);
                    ventaComidaCollectionNewVentaComida = em.merge(ventaComidaCollectionNewVentaComida);
                    if (oldIdVentaDiaOfVentaComidaCollectionNewVentaComida != null && !oldIdVentaDiaOfVentaComidaCollectionNewVentaComida.equals(ventaDia)) {
                        oldIdVentaDiaOfVentaComidaCollectionNewVentaComida.getVentaComidaCollection().remove(ventaComidaCollectionNewVentaComida);
                        oldIdVentaDiaOfVentaComidaCollectionNewVentaComida = em.merge(oldIdVentaDiaOfVentaComidaCollectionNewVentaComida);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = ventaDia.getId();
                if (findVentaDia(id) == null) {
                    throw new NonexistentEntityException("The ventaDia with id " + id + " no longer exists.");
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
            VentaDia ventaDia;
            try {
                ventaDia = em.getReference(VentaDia.class, id);
                ventaDia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ventaDia with id " + id + " no longer exists.", enfe);
            }
            Cliente idCliente = ventaDia.getIdCliente();
            if (idCliente != null) {
                idCliente.getVentaDiaCollection().remove(ventaDia);
                idCliente = em.merge(idCliente);
            }
            Usuario idUsuario = ventaDia.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getVentaDiaCollection().remove(ventaDia);
                idUsuario = em.merge(idUsuario);
            }
            Collection<VentaProducto> ventaProductoCollection = ventaDia.getVentaProductoCollection();
            for (VentaProducto ventaProductoCollectionVentaProducto : ventaProductoCollection) {
                ventaProductoCollectionVentaProducto.setIdVentaDia(null);
                ventaProductoCollectionVentaProducto = em.merge(ventaProductoCollectionVentaProducto);
            }
            Collection<VentaComida> ventaComidaCollection = ventaDia.getVentaComidaCollection();
            for (VentaComida ventaComidaCollectionVentaComida : ventaComidaCollection) {
                ventaComidaCollectionVentaComida.setIdVentaDia(null);
                ventaComidaCollectionVentaComida = em.merge(ventaComidaCollectionVentaComida);
            }
            em.remove(ventaDia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VentaDia> findVentaDiaEntities() {
        return findVentaDiaEntities(true, -1, -1);
    }

    public List<VentaDia> findVentaDiaEntities(int maxResults, int firstResult) {
        return findVentaDiaEntities(false, maxResults, firstResult);
    }

    private List<VentaDia> findVentaDiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VentaDia.class));
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

    public VentaDia findVentaDia(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VentaDia.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentaDiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VentaDia> rt = cq.from(VentaDia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
