/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dart.restaurante.controller;

import dart.restaurante.controller.exceptions.NonexistentEntityException;
import dart.restaurante.controller.exceptions.PreexistingEntityException;
import dart.restaurante.dao.Usuario;
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
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getVentaDiaCollection() == null) {
            usuario.setVentaDiaCollection(new ArrayList<VentaDia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<VentaDia> attachedVentaDiaCollection = new ArrayList<VentaDia>();
            for (VentaDia ventaDiaCollectionVentaDiaToAttach : usuario.getVentaDiaCollection()) {
                ventaDiaCollectionVentaDiaToAttach = em.getReference(ventaDiaCollectionVentaDiaToAttach.getClass(), ventaDiaCollectionVentaDiaToAttach.getId());
                attachedVentaDiaCollection.add(ventaDiaCollectionVentaDiaToAttach);
            }
            usuario.setVentaDiaCollection(attachedVentaDiaCollection);
            em.persist(usuario);
            for (VentaDia ventaDiaCollectionVentaDia : usuario.getVentaDiaCollection()) {
                Usuario oldIdUsuarioOfVentaDiaCollectionVentaDia = ventaDiaCollectionVentaDia.getIdUsuario();
                ventaDiaCollectionVentaDia.setIdUsuario(usuario);
                ventaDiaCollectionVentaDia = em.merge(ventaDiaCollectionVentaDia);
                if (oldIdUsuarioOfVentaDiaCollectionVentaDia != null) {
                    oldIdUsuarioOfVentaDiaCollectionVentaDia.getVentaDiaCollection().remove(ventaDiaCollectionVentaDia);
                    oldIdUsuarioOfVentaDiaCollectionVentaDia = em.merge(oldIdUsuarioOfVentaDiaCollectionVentaDia);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getId()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            Collection<VentaDia> ventaDiaCollectionOld = persistentUsuario.getVentaDiaCollection();
            Collection<VentaDia> ventaDiaCollectionNew = usuario.getVentaDiaCollection();
            Collection<VentaDia> attachedVentaDiaCollectionNew = new ArrayList<VentaDia>();
            for (VentaDia ventaDiaCollectionNewVentaDiaToAttach : ventaDiaCollectionNew) {
                ventaDiaCollectionNewVentaDiaToAttach = em.getReference(ventaDiaCollectionNewVentaDiaToAttach.getClass(), ventaDiaCollectionNewVentaDiaToAttach.getId());
                attachedVentaDiaCollectionNew.add(ventaDiaCollectionNewVentaDiaToAttach);
            }
            ventaDiaCollectionNew = attachedVentaDiaCollectionNew;
            usuario.setVentaDiaCollection(ventaDiaCollectionNew);
            usuario = em.merge(usuario);
            for (VentaDia ventaDiaCollectionOldVentaDia : ventaDiaCollectionOld) {
                if (!ventaDiaCollectionNew.contains(ventaDiaCollectionOldVentaDia)) {
                    ventaDiaCollectionOldVentaDia.setIdUsuario(null);
                    ventaDiaCollectionOldVentaDia = em.merge(ventaDiaCollectionOldVentaDia);
                }
            }
            for (VentaDia ventaDiaCollectionNewVentaDia : ventaDiaCollectionNew) {
                if (!ventaDiaCollectionOld.contains(ventaDiaCollectionNewVentaDia)) {
                    Usuario oldIdUsuarioOfVentaDiaCollectionNewVentaDia = ventaDiaCollectionNewVentaDia.getIdUsuario();
                    ventaDiaCollectionNewVentaDia.setIdUsuario(usuario);
                    ventaDiaCollectionNewVentaDia = em.merge(ventaDiaCollectionNewVentaDia);
                    if (oldIdUsuarioOfVentaDiaCollectionNewVentaDia != null && !oldIdUsuarioOfVentaDiaCollectionNewVentaDia.equals(usuario)) {
                        oldIdUsuarioOfVentaDiaCollectionNewVentaDia.getVentaDiaCollection().remove(ventaDiaCollectionNewVentaDia);
                        oldIdUsuarioOfVentaDiaCollectionNewVentaDia = em.merge(oldIdUsuarioOfVentaDiaCollectionNewVentaDia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            Collection<VentaDia> ventaDiaCollection = usuario.getVentaDiaCollection();
            for (VentaDia ventaDiaCollectionVentaDia : ventaDiaCollection) {
                ventaDiaCollectionVentaDia.setIdUsuario(null);
                ventaDiaCollectionVentaDia = em.merge(ventaDiaCollectionVentaDia);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
