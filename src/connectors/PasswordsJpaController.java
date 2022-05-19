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
public class PasswordsJpaController implements Serializable {

    public PasswordsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Passwords passwords) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users users = passwords.getUsers();
            if (users != null) {
                users = em.getReference(users.getClass(), users.getId());
                passwords.setUsers(users);
            }
            em.persist(passwords);
            if (users != null) {
                Passwords oldPasswordIDOfUsers = users.getPasswordID();
                if (oldPasswordIDOfUsers != null) {
                    oldPasswordIDOfUsers.setUsers(null);
                    oldPasswordIDOfUsers = em.merge(oldPasswordIDOfUsers);
                }
                users.setPasswordID(passwords);
                users = em.merge(users);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Passwords passwords) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Passwords persistentPasswords = em.find(Passwords.class, passwords.getId());
            Users usersOld = persistentPasswords.getUsers();
            Users usersNew = passwords.getUsers();
            List<String> illegalOrphanMessages = null;
            if (usersOld != null && !usersOld.equals(usersNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Users " + usersOld + " since its passwordID field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usersNew != null) {
                usersNew = em.getReference(usersNew.getClass(), usersNew.getId());
                passwords.setUsers(usersNew);
            }
            passwords = em.merge(passwords);
            if (usersNew != null && !usersNew.equals(usersOld)) {
                Passwords oldPasswordIDOfUsers = usersNew.getPasswordID();
                if (oldPasswordIDOfUsers != null) {
                    oldPasswordIDOfUsers.setUsers(null);
                    oldPasswordIDOfUsers = em.merge(oldPasswordIDOfUsers);
                }
                usersNew.setPasswordID(passwords);
                usersNew = em.merge(usersNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = passwords.getId();
                if (findPasswords(id) == null) {
                    throw new NonexistentEntityException("The passwords with id " + id + " no longer exists.");
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
            Passwords passwords;
            try {
                passwords = em.getReference(Passwords.class, id);
                passwords.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The passwords with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Users usersOrphanCheck = passwords.getUsers();
            if (usersOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Passwords (" + passwords + ") cannot be destroyed since the Users " + usersOrphanCheck + " in its users field has a non-nullable passwordID field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(passwords);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Passwords> findPasswordsEntities() {
        return findPasswordsEntities(true, -1, -1);
    }

    public List<Passwords> findPasswordsEntities(int maxResults, int firstResult) {
        return findPasswordsEntities(false, maxResults, firstResult);
    }

    private List<Passwords> findPasswordsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Passwords.class));
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

    public Passwords findPasswords(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Passwords.class, id);
        } finally {
            em.close();
        }
    }

    public int getPasswordsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Passwords> rt = cq.from(Passwords.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
