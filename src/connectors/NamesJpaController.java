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
public class NamesJpaController implements Serializable {

    public NamesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Names names) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users users = names.getUsers();
            if (users != null) {
                users = em.getReference(users.getClass(), users.getId());
                names.setUsers(users);
            }
            em.persist(names);
            if (users != null) {
                Names oldNameIDOfUsers = users.getNameID();
                if (oldNameIDOfUsers != null) {
                    oldNameIDOfUsers.setUsers(null);
                    oldNameIDOfUsers = em.merge(oldNameIDOfUsers);
                }
                users.setNameID(names);
                users = em.merge(users);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Names names) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Names persistentNames = em.find(Names.class, names.getId());
            Users usersOld = persistentNames.getUsers();
            Users usersNew = names.getUsers();
            List<String> illegalOrphanMessages = null;
            if (usersOld != null && !usersOld.equals(usersNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Users " + usersOld + " since its nameID field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usersNew != null) {
                usersNew = em.getReference(usersNew.getClass(), usersNew.getId());
                names.setUsers(usersNew);
            }
            names = em.merge(names);
            if (usersNew != null && !usersNew.equals(usersOld)) {
                Names oldNameIDOfUsers = usersNew.getNameID();
                if (oldNameIDOfUsers != null) {
                    oldNameIDOfUsers.setUsers(null);
                    oldNameIDOfUsers = em.merge(oldNameIDOfUsers);
                }
                usersNew.setNameID(names);
                usersNew = em.merge(usersNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = names.getId();
                if (findNames(id) == null) {
                    throw new NonexistentEntityException("The names with id " + id + " no longer exists.");
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
            Names names;
            try {
                names = em.getReference(Names.class, id);
                names.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The names with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Users usersOrphanCheck = names.getUsers();
            if (usersOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Names (" + names + ") cannot be destroyed since the Users " + usersOrphanCheck + " in its users field has a non-nullable nameID field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(names);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Names> findNamesEntities() {
        return findNamesEntities(true, -1, -1);
    }

    public List<Names> findNamesEntities(int maxResults, int firstResult) {
        return findNamesEntities(false, maxResults, firstResult);
    }

    private List<Names> findNamesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Names.class));
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

    public Names findNames(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Names.class, id);
        } finally {
            em.close();
        }
    }

    public int getNamesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Names> rt = cq.from(Names.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
