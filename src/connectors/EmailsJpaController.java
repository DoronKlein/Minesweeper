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
public class EmailsJpaController implements Serializable {

    public EmailsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Emails emails) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users users = emails.getUsers();
            if (users != null) {
                users = em.getReference(users.getClass(), users.getId());
                emails.setUsers(users);
            }
            em.persist(emails);
            if (users != null) {
                Emails oldEmailIDOfUsers = users.getEmailID();
                if (oldEmailIDOfUsers != null) {
                    oldEmailIDOfUsers.setUsers(null);
                    oldEmailIDOfUsers = em.merge(oldEmailIDOfUsers);
                }
                users.setEmailID(emails);
                users = em.merge(users);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Emails emails) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Emails persistentEmails = em.find(Emails.class, emails.getId());
            Users usersOld = persistentEmails.getUsers();
            Users usersNew = emails.getUsers();
            List<String> illegalOrphanMessages = null;
            if (usersOld != null && !usersOld.equals(usersNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Users " + usersOld + " since its emailID field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usersNew != null) {
                usersNew = em.getReference(usersNew.getClass(), usersNew.getId());
                emails.setUsers(usersNew);
            }
            emails = em.merge(emails);
            if (usersNew != null && !usersNew.equals(usersOld)) {
                Emails oldEmailIDOfUsers = usersNew.getEmailID();
                if (oldEmailIDOfUsers != null) {
                    oldEmailIDOfUsers.setUsers(null);
                    oldEmailIDOfUsers = em.merge(oldEmailIDOfUsers);
                }
                usersNew.setEmailID(emails);
                usersNew = em.merge(usersNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = emails.getId();
                if (findEmails(id) == null) {
                    throw new NonexistentEntityException("The emails with id " + id + " no longer exists.");
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
            Emails emails;
            try {
                emails = em.getReference(Emails.class, id);
                emails.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The emails with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Users usersOrphanCheck = emails.getUsers();
            if (usersOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Emails (" + emails + ") cannot be destroyed since the Users " + usersOrphanCheck + " in its users field has a non-nullable emailID field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(emails);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Emails> findEmailsEntities() {
        return findEmailsEntities(true, -1, -1);
    }

    public List<Emails> findEmailsEntities(int maxResults, int firstResult) {
        return findEmailsEntities(false, maxResults, firstResult);
    }

    private List<Emails> findEmailsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Emails.class));
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

    public Emails findEmails(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Emails.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmailsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Emails> rt = cq.from(Emails.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
