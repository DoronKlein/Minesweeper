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
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author doron
 */
public class UsersJpaController implements Serializable {

    public UsersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) throws IllegalOrphanException {
        if (users.getUsersVsGamesCollection() == null) {
            users.setUsersVsGamesCollection(new ArrayList<UsersVsGames>());
        }
        List<String> illegalOrphanMessages = null;
        Emails emailIDOrphanCheck = users.getEmailID();
        if (emailIDOrphanCheck != null) {
            Users oldUsersOfEmailID = emailIDOrphanCheck.getUsers();
            if (oldUsersOfEmailID != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Emails " + emailIDOrphanCheck + " already has an item of type Users whose emailID column cannot be null. Please make another selection for the emailID field.");
            }
        }
        Names nameIDOrphanCheck = users.getNameID();
        if (nameIDOrphanCheck != null) {
            Users oldUsersOfNameID = nameIDOrphanCheck.getUsers();
            if (oldUsersOfNameID != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Names " + nameIDOrphanCheck + " already has an item of type Users whose nameID column cannot be null. Please make another selection for the nameID field.");
            }
        }
        Passwords passwordIDOrphanCheck = users.getPasswordID();
        if (passwordIDOrphanCheck != null) {
            Users oldUsersOfPasswordID = passwordIDOrphanCheck.getUsers();
            if (oldUsersOfPasswordID != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Passwords " + passwordIDOrphanCheck + " already has an item of type Users whose passwordID column cannot be null. Please make another selection for the passwordID field.");
            }
        }
        Salts saltIDOrphanCheck = users.getSaltID();
        if (saltIDOrphanCheck != null) {
            Users oldUsersOfSaltID = saltIDOrphanCheck.getUsers();
            if (oldUsersOfSaltID != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Salts " + saltIDOrphanCheck + " already has an item of type Users whose saltID column cannot be null. Please make another selection for the saltID field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Emails emailID = users.getEmailID();
            if (emailID != null) {
                emailID = em.getReference(emailID.getClass(), emailID.getId());
                users.setEmailID(emailID);
            }
            Names nameID = users.getNameID();
            if (nameID != null) {
                nameID = em.getReference(nameID.getClass(), nameID.getId());
                users.setNameID(nameID);
            }
            Passwords passwordID = users.getPasswordID();
            if (passwordID != null) {
                passwordID = em.getReference(passwordID.getClass(), passwordID.getId());
                users.setPasswordID(passwordID);
            }
            Salts saltID = users.getSaltID();
            if (saltID != null) {
                saltID = em.getReference(saltID.getClass(), saltID.getId());
                users.setSaltID(saltID);
            }
            Collection<UsersVsGames> attachedUsersVsGamesCollection = new ArrayList<UsersVsGames>();
            for (UsersVsGames usersVsGamesCollectionUsersVsGamesToAttach : users.getUsersVsGamesCollection()) {
                usersVsGamesCollectionUsersVsGamesToAttach = em.getReference(usersVsGamesCollectionUsersVsGamesToAttach.getClass(), usersVsGamesCollectionUsersVsGamesToAttach.getId());
                attachedUsersVsGamesCollection.add(usersVsGamesCollectionUsersVsGamesToAttach);
            }
            users.setUsersVsGamesCollection(attachedUsersVsGamesCollection);
            em.persist(users);
            if (emailID != null) {
                emailID.setUsers(users);
                emailID = em.merge(emailID);
            }
            if (nameID != null) {
                nameID.setUsers(users);
                nameID = em.merge(nameID);
            }
            if (passwordID != null) {
                passwordID.setUsers(users);
                passwordID = em.merge(passwordID);
            }
            if (saltID != null) {
                saltID.setUsers(users);
                saltID = em.merge(saltID);
            }
            for (UsersVsGames usersVsGamesCollectionUsersVsGames : users.getUsersVsGamesCollection()) {
                Users oldUserIDOfUsersVsGamesCollectionUsersVsGames = usersVsGamesCollectionUsersVsGames.getUserID();
                usersVsGamesCollectionUsersVsGames.setUserID(users);
                usersVsGamesCollectionUsersVsGames = em.merge(usersVsGamesCollectionUsersVsGames);
                if (oldUserIDOfUsersVsGamesCollectionUsersVsGames != null) {
                    oldUserIDOfUsersVsGamesCollectionUsersVsGames.getUsersVsGamesCollection().remove(usersVsGamesCollectionUsersVsGames);
                    oldUserIDOfUsersVsGamesCollectionUsersVsGames = em.merge(oldUserIDOfUsersVsGamesCollectionUsersVsGames);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getId());
            Emails emailIDOld = persistentUsers.getEmailID();
            Emails emailIDNew = users.getEmailID();
            Names nameIDOld = persistentUsers.getNameID();
            Names nameIDNew = users.getNameID();
            Passwords passwordIDOld = persistentUsers.getPasswordID();
            Passwords passwordIDNew = users.getPasswordID();
            Salts saltIDOld = persistentUsers.getSaltID();
            Salts saltIDNew = users.getSaltID();
            Collection<UsersVsGames> usersVsGamesCollectionOld = persistentUsers.getUsersVsGamesCollection();
            Collection<UsersVsGames> usersVsGamesCollectionNew = users.getUsersVsGamesCollection();
            List<String> illegalOrphanMessages = null;
            if (emailIDNew != null && !emailIDNew.equals(emailIDOld)) {
                Users oldUsersOfEmailID = emailIDNew.getUsers();
                if (oldUsersOfEmailID != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Emails " + emailIDNew + " already has an item of type Users whose emailID column cannot be null. Please make another selection for the emailID field.");
                }
            }
            if (nameIDNew != null && !nameIDNew.equals(nameIDOld)) {
                Users oldUsersOfNameID = nameIDNew.getUsers();
                if (oldUsersOfNameID != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Names " + nameIDNew + " already has an item of type Users whose nameID column cannot be null. Please make another selection for the nameID field.");
                }
            }
            if (passwordIDNew != null && !passwordIDNew.equals(passwordIDOld)) {
                Users oldUsersOfPasswordID = passwordIDNew.getUsers();
                if (oldUsersOfPasswordID != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Passwords " + passwordIDNew + " already has an item of type Users whose passwordID column cannot be null. Please make another selection for the passwordID field.");
                }
            }
            if (saltIDNew != null && !saltIDNew.equals(saltIDOld)) {
                Users oldUsersOfSaltID = saltIDNew.getUsers();
                if (oldUsersOfSaltID != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Salts " + saltIDNew + " already has an item of type Users whose saltID column cannot be null. Please make another selection for the saltID field.");
                }
            }
            for (UsersVsGames usersVsGamesCollectionOldUsersVsGames : usersVsGamesCollectionOld) {
                if (!usersVsGamesCollectionNew.contains(usersVsGamesCollectionOldUsersVsGames)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsersVsGames " + usersVsGamesCollectionOldUsersVsGames + " since its userID field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (emailIDNew != null) {
                emailIDNew = em.getReference(emailIDNew.getClass(), emailIDNew.getId());
                users.setEmailID(emailIDNew);
            }
            if (nameIDNew != null) {
                nameIDNew = em.getReference(nameIDNew.getClass(), nameIDNew.getId());
                users.setNameID(nameIDNew);
            }
            if (passwordIDNew != null) {
                passwordIDNew = em.getReference(passwordIDNew.getClass(), passwordIDNew.getId());
                users.setPasswordID(passwordIDNew);
            }
            if (saltIDNew != null) {
                saltIDNew = em.getReference(saltIDNew.getClass(), saltIDNew.getId());
                users.setSaltID(saltIDNew);
            }
            Collection<UsersVsGames> attachedUsersVsGamesCollectionNew = new ArrayList<UsersVsGames>();
            for (UsersVsGames usersVsGamesCollectionNewUsersVsGamesToAttach : usersVsGamesCollectionNew) {
                usersVsGamesCollectionNewUsersVsGamesToAttach = em.getReference(usersVsGamesCollectionNewUsersVsGamesToAttach.getClass(), usersVsGamesCollectionNewUsersVsGamesToAttach.getId());
                attachedUsersVsGamesCollectionNew.add(usersVsGamesCollectionNewUsersVsGamesToAttach);
            }
            usersVsGamesCollectionNew = attachedUsersVsGamesCollectionNew;
            users.setUsersVsGamesCollection(usersVsGamesCollectionNew);
            users = em.merge(users);
            if (emailIDOld != null && !emailIDOld.equals(emailIDNew)) {
                emailIDOld.setUsers(null);
                emailIDOld = em.merge(emailIDOld);
            }
            if (emailIDNew != null && !emailIDNew.equals(emailIDOld)) {
                emailIDNew.setUsers(users);
                emailIDNew = em.merge(emailIDNew);
            }
            if (nameIDOld != null && !nameIDOld.equals(nameIDNew)) {
                nameIDOld.setUsers(null);
                nameIDOld = em.merge(nameIDOld);
            }
            if (nameIDNew != null && !nameIDNew.equals(nameIDOld)) {
                nameIDNew.setUsers(users);
                nameIDNew = em.merge(nameIDNew);
            }
            if (passwordIDOld != null && !passwordIDOld.equals(passwordIDNew)) {
                passwordIDOld.setUsers(null);
                passwordIDOld = em.merge(passwordIDOld);
            }
            if (passwordIDNew != null && !passwordIDNew.equals(passwordIDOld)) {
                passwordIDNew.setUsers(users);
                passwordIDNew = em.merge(passwordIDNew);
            }
            if (saltIDOld != null && !saltIDOld.equals(saltIDNew)) {
                saltIDOld.setUsers(null);
                saltIDOld = em.merge(saltIDOld);
            }
            if (saltIDNew != null && !saltIDNew.equals(saltIDOld)) {
                saltIDNew.setUsers(users);
                saltIDNew = em.merge(saltIDNew);
            }
            for (UsersVsGames usersVsGamesCollectionNewUsersVsGames : usersVsGamesCollectionNew) {
                if (!usersVsGamesCollectionOld.contains(usersVsGamesCollectionNewUsersVsGames)) {
                    Users oldUserIDOfUsersVsGamesCollectionNewUsersVsGames = usersVsGamesCollectionNewUsersVsGames.getUserID();
                    usersVsGamesCollectionNewUsersVsGames.setUserID(users);
                    usersVsGamesCollectionNewUsersVsGames = em.merge(usersVsGamesCollectionNewUsersVsGames);
                    if (oldUserIDOfUsersVsGamesCollectionNewUsersVsGames != null && !oldUserIDOfUsersVsGamesCollectionNewUsersVsGames.equals(users)) {
                        oldUserIDOfUsersVsGamesCollectionNewUsersVsGames.getUsersVsGamesCollection().remove(usersVsGamesCollectionNewUsersVsGames);
                        oldUserIDOfUsersVsGamesCollectionNewUsersVsGames = em.merge(oldUserIDOfUsersVsGamesCollectionNewUsersVsGames);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = users.getId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
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
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<UsersVsGames> usersVsGamesCollectionOrphanCheck = users.getUsersVsGamesCollection();
            for (UsersVsGames usersVsGamesCollectionOrphanCheckUsersVsGames : usersVsGamesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the UsersVsGames " + usersVsGamesCollectionOrphanCheckUsersVsGames + " in its usersVsGamesCollection field has a non-nullable userID field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Emails emailID = users.getEmailID();
            if (emailID != null) {
                emailID.setUsers(null);
                emailID = em.merge(emailID);
            }
            Names nameID = users.getNameID();
            if (nameID != null) {
                nameID.setUsers(null);
                nameID = em.merge(nameID);
            }
            Passwords passwordID = users.getPasswordID();
            if (passwordID != null) {
                passwordID.setUsers(null);
                passwordID = em.merge(passwordID);
            }
            Salts saltID = users.getSaltID();
            if (saltID != null) {
                saltID.setUsers(null);
                saltID = em.merge(saltID);
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
