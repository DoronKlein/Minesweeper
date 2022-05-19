/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connectors;

import connectors.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author doron
 */
public class UsersVsGamesJpaController implements Serializable {

    public UsersVsGamesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsersVsGames usersVsGames) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Games gameID = usersVsGames.getGameID();
            if (gameID != null) {
                gameID = em.getReference(gameID.getClass(), gameID.getId());
                usersVsGames.setGameID(gameID);
            }
            Users userID = usersVsGames.getUserID();
            if (userID != null) {
                userID = em.getReference(userID.getClass(), userID.getId());
                usersVsGames.setUserID(userID);
            }
            em.persist(usersVsGames);
            if (gameID != null) {
                gameID.getUsersVsGamesCollection().add(usersVsGames);
                gameID = em.merge(gameID);
            }
            if (userID != null) {
                userID.getUsersVsGamesCollection().add(usersVsGames);
                userID = em.merge(userID);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsersVsGames usersVsGames) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsersVsGames persistentUsersVsGames = em.find(UsersVsGames.class, usersVsGames.getId());
            Games gameIDOld = persistentUsersVsGames.getGameID();
            Games gameIDNew = usersVsGames.getGameID();
            Users userIDOld = persistentUsersVsGames.getUserID();
            Users userIDNew = usersVsGames.getUserID();
            if (gameIDNew != null) {
                gameIDNew = em.getReference(gameIDNew.getClass(), gameIDNew.getId());
                usersVsGames.setGameID(gameIDNew);
            }
            if (userIDNew != null) {
                userIDNew = em.getReference(userIDNew.getClass(), userIDNew.getId());
                usersVsGames.setUserID(userIDNew);
            }
            usersVsGames = em.merge(usersVsGames);
            if (gameIDOld != null && !gameIDOld.equals(gameIDNew)) {
                gameIDOld.getUsersVsGamesCollection().remove(usersVsGames);
                gameIDOld = em.merge(gameIDOld);
            }
            if (gameIDNew != null && !gameIDNew.equals(gameIDOld)) {
                gameIDNew.getUsersVsGamesCollection().add(usersVsGames);
                gameIDNew = em.merge(gameIDNew);
            }
            if (userIDOld != null && !userIDOld.equals(userIDNew)) {
                userIDOld.getUsersVsGamesCollection().remove(usersVsGames);
                userIDOld = em.merge(userIDOld);
            }
            if (userIDNew != null && !userIDNew.equals(userIDOld)) {
                userIDNew.getUsersVsGamesCollection().add(usersVsGames);
                userIDNew = em.merge(userIDNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usersVsGames.getId();
                if (findUsersVsGames(id) == null) {
                    throw new NonexistentEntityException("The usersVsGames with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsersVsGames usersVsGames;
            try {
                usersVsGames = em.getReference(UsersVsGames.class, id);
                usersVsGames.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usersVsGames with id " + id + " no longer exists.", enfe);
            }
            Games gameID = usersVsGames.getGameID();
            if (gameID != null) {
                gameID.getUsersVsGamesCollection().remove(usersVsGames);
                gameID = em.merge(gameID);
            }
            Users userID = usersVsGames.getUserID();
            if (userID != null) {
                userID.getUsersVsGamesCollection().remove(usersVsGames);
                userID = em.merge(userID);
            }
            em.remove(usersVsGames);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsersVsGames> findUsersVsGamesEntities() {
        return findUsersVsGamesEntities(true, -1, -1);
    }

    public List<UsersVsGames> findUsersVsGamesEntities(int maxResults, int firstResult) {
        return findUsersVsGamesEntities(false, maxResults, firstResult);
    }

    private List<UsersVsGames> findUsersVsGamesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsersVsGames.class));
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

    public UsersVsGames findUsersVsGames(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsersVsGames.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersVsGamesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsersVsGames> rt = cq.from(UsersVsGames.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
