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
import dart.restaurante.dao.VentaProducto;
import java.util.ArrayList;
import java.util.Collection;
import dart.restaurante.dao.Compra;
import dart.restaurante.dao.Producto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Diego Ramos
 */
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) throws PreexistingEntityException, Exception {
        if (producto.getVentaProductoCollection() == null) {
            producto.setVentaProductoCollection(new ArrayList<VentaProducto>());
        }
        if (producto.getCompraCollection() == null) {
            producto.setCompraCollection(new ArrayList<Compra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<VentaProducto> attachedVentaProductoCollection = new ArrayList<VentaProducto>();
            for (VentaProducto ventaProductoCollectionVentaProductoToAttach : producto.getVentaProductoCollection()) {
                ventaProductoCollectionVentaProductoToAttach = em.getReference(ventaProductoCollectionVentaProductoToAttach.getClass(), ventaProductoCollectionVentaProductoToAttach.getId());
                attachedVentaProductoCollection.add(ventaProductoCollectionVentaProductoToAttach);
            }
            producto.setVentaProductoCollection(attachedVentaProductoCollection);
            Collection<Compra> attachedCompraCollection = new ArrayList<Compra>();
            for (Compra compraCollectionCompraToAttach : producto.getCompraCollection()) {
                compraCollectionCompraToAttach = em.getReference(compraCollectionCompraToAttach.getClass(), compraCollectionCompraToAttach.getId());
                attachedCompraCollection.add(compraCollectionCompraToAttach);
            }
            producto.setCompraCollection(attachedCompraCollection);
            em.persist(producto);
            for (VentaProducto ventaProductoCollectionVentaProducto : producto.getVentaProductoCollection()) {
                Producto oldIdProductoOfVentaProductoCollectionVentaProducto = ventaProductoCollectionVentaProducto.getIdProducto();
                ventaProductoCollectionVentaProducto.setIdProducto(producto);
                ventaProductoCollectionVentaProducto = em.merge(ventaProductoCollectionVentaProducto);
                if (oldIdProductoOfVentaProductoCollectionVentaProducto != null) {
                    oldIdProductoOfVentaProductoCollectionVentaProducto.getVentaProductoCollection().remove(ventaProductoCollectionVentaProducto);
                    oldIdProductoOfVentaProductoCollectionVentaProducto = em.merge(oldIdProductoOfVentaProductoCollectionVentaProducto);
                }
            }
            for (Compra compraCollectionCompra : producto.getCompraCollection()) {
                Producto oldIdProductoOfCompraCollectionCompra = compraCollectionCompra.getIdProducto();
                compraCollectionCompra.setIdProducto(producto);
                compraCollectionCompra = em.merge(compraCollectionCompra);
                if (oldIdProductoOfCompraCollectionCompra != null) {
                    oldIdProductoOfCompraCollectionCompra.getCompraCollection().remove(compraCollectionCompra);
                    oldIdProductoOfCompraCollectionCompra = em.merge(oldIdProductoOfCompraCollectionCompra);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProducto(producto.getId()) != null) {
                throw new PreexistingEntityException("Producto " + producto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getId());
            Collection<VentaProducto> ventaProductoCollectionOld = persistentProducto.getVentaProductoCollection();
            Collection<VentaProducto> ventaProductoCollectionNew = producto.getVentaProductoCollection();
            Collection<Compra> compraCollectionOld = persistentProducto.getCompraCollection();
            Collection<Compra> compraCollectionNew = producto.getCompraCollection();
            Collection<VentaProducto> attachedVentaProductoCollectionNew = new ArrayList<VentaProducto>();
            for (VentaProducto ventaProductoCollectionNewVentaProductoToAttach : ventaProductoCollectionNew) {
                ventaProductoCollectionNewVentaProductoToAttach = em.getReference(ventaProductoCollectionNewVentaProductoToAttach.getClass(), ventaProductoCollectionNewVentaProductoToAttach.getId());
                attachedVentaProductoCollectionNew.add(ventaProductoCollectionNewVentaProductoToAttach);
            }
            ventaProductoCollectionNew = attachedVentaProductoCollectionNew;
            producto.setVentaProductoCollection(ventaProductoCollectionNew);
            Collection<Compra> attachedCompraCollectionNew = new ArrayList<Compra>();
            for (Compra compraCollectionNewCompraToAttach : compraCollectionNew) {
                compraCollectionNewCompraToAttach = em.getReference(compraCollectionNewCompraToAttach.getClass(), compraCollectionNewCompraToAttach.getId());
                attachedCompraCollectionNew.add(compraCollectionNewCompraToAttach);
            }
            compraCollectionNew = attachedCompraCollectionNew;
            producto.setCompraCollection(compraCollectionNew);
            producto = em.merge(producto);
            for (VentaProducto ventaProductoCollectionOldVentaProducto : ventaProductoCollectionOld) {
                if (!ventaProductoCollectionNew.contains(ventaProductoCollectionOldVentaProducto)) {
                    ventaProductoCollectionOldVentaProducto.setIdProducto(null);
                    ventaProductoCollectionOldVentaProducto = em.merge(ventaProductoCollectionOldVentaProducto);
                }
            }
            for (VentaProducto ventaProductoCollectionNewVentaProducto : ventaProductoCollectionNew) {
                if (!ventaProductoCollectionOld.contains(ventaProductoCollectionNewVentaProducto)) {
                    Producto oldIdProductoOfVentaProductoCollectionNewVentaProducto = ventaProductoCollectionNewVentaProducto.getIdProducto();
                    ventaProductoCollectionNewVentaProducto.setIdProducto(producto);
                    ventaProductoCollectionNewVentaProducto = em.merge(ventaProductoCollectionNewVentaProducto);
                    if (oldIdProductoOfVentaProductoCollectionNewVentaProducto != null && !oldIdProductoOfVentaProductoCollectionNewVentaProducto.equals(producto)) {
                        oldIdProductoOfVentaProductoCollectionNewVentaProducto.getVentaProductoCollection().remove(ventaProductoCollectionNewVentaProducto);
                        oldIdProductoOfVentaProductoCollectionNewVentaProducto = em.merge(oldIdProductoOfVentaProductoCollectionNewVentaProducto);
                    }
                }
            }
            for (Compra compraCollectionOldCompra : compraCollectionOld) {
                if (!compraCollectionNew.contains(compraCollectionOldCompra)) {
                    compraCollectionOldCompra.setIdProducto(null);
                    compraCollectionOldCompra = em.merge(compraCollectionOldCompra);
                }
            }
            for (Compra compraCollectionNewCompra : compraCollectionNew) {
                if (!compraCollectionOld.contains(compraCollectionNewCompra)) {
                    Producto oldIdProductoOfCompraCollectionNewCompra = compraCollectionNewCompra.getIdProducto();
                    compraCollectionNewCompra.setIdProducto(producto);
                    compraCollectionNewCompra = em.merge(compraCollectionNewCompra);
                    if (oldIdProductoOfCompraCollectionNewCompra != null && !oldIdProductoOfCompraCollectionNewCompra.equals(producto)) {
                        oldIdProductoOfCompraCollectionNewCompra.getCompraCollection().remove(compraCollectionNewCompra);
                        oldIdProductoOfCompraCollectionNewCompra = em.merge(oldIdProductoOfCompraCollectionNewCompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = producto.getId();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
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
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            Collection<VentaProducto> ventaProductoCollection = producto.getVentaProductoCollection();
            for (VentaProducto ventaProductoCollectionVentaProducto : ventaProductoCollection) {
                ventaProductoCollectionVentaProducto.setIdProducto(null);
                ventaProductoCollectionVentaProducto = em.merge(ventaProductoCollectionVentaProducto);
            }
            Collection<Compra> compraCollection = producto.getCompraCollection();
            for (Compra compraCollectionCompra : compraCollection) {
                compraCollectionCompra.setIdProducto(null);
                compraCollectionCompra = em.merge(compraCollectionCompra);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
