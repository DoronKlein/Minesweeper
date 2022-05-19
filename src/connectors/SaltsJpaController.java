/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connectors;

import connectors.exceptions.IllegalOrphanException;
import connectors.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author doron
 */
public class SaltsJpaController implements Serializable {

    public SaltsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Salts salts) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users users = salts.getUsers();
            if (users != null) {
                users = em.getReference(users.getClass(), users.getId());
                salts.setUsers(users);
            }
            em.persist(salts);
            if (users != null) {
                Salts oldSaltIDOfUsers = users.getSaltID();
                if (oldSaltIDOfUsers != null) {
                    oldSaltIDOfUsers.setUsers(null);
                    oldSaltIDOfUsers = em.merge(oldSaltIDOfUsers);
                }
                users.setSaltID(salts);
                users = em.merge(users);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Salts salts) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Salts persistentSalts = em.find(Salts.class, salts.getId());
            Users usersOld = persistentSalts.getUsers();
            Users usersNew = salts.getUsers();
            List<String> illegalOrphanMessages = null;
            if (usersOld != null && !usersOld.equals(usersNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Users " + usersOld + " since its saltID field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usersNew != null) {
                usersNew = em.getReference(usersNew.getClass(), usersNew.getId());
                salts.setUsers(usersNew);
            }
            salts = em.merge(salts);
            if (usersNew != null && !usersNew.equals(usersOld)) {
                Salts oldSaltIDOfUsers = usersNew.getSaltID();
                if (oldSaltIDOfUsers != null) {
                    oldSaltIDOfUsers.setUsers(null);
                    oldSaltIDOfUsers = em.merge(oldSaltIDOfUsers);
                }
                usersNew.setSaltID(salts);
                usersNew = em.merge(usersNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = salts.getId();
                if (findSalts(id) == null) {
                    throw new NonexistentEntityException("The salts with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Salts salts;
            try {
                salts = em.getReference(Salts.class, id);
                salts.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The salts with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Users usersOrphanCheck = salts.getUsers();
            if (usersOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Salts (" + salts + ") cannot be destroyed since the Users " + usersOrphanCheck + " in its users field has a non-nullable saltID field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(salts);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Salts> findSaltsEntities() {
        return findSaltsEntities(true, -1, -1);
    }

    public List<Salts> findSaltsEntities(int maxResults, int firstResult) {
        return findSaltsEntities(false, maxResults, firstResult);
    }

    private List<Salts> findSaltsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Salts.class));
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

    public Salts findSalts(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Salts.class, id);
        } finally {
            em.close();
        }
    }

    public int getSaltsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Salts> rt = cq.from(Salts.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
