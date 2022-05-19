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
public class BoardsJpaController implements Serializable {

    public BoardsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Boards boards) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Games gameID = boards.getGameID();
            if (gameID != null) {
                gameID = em.getReference(gameID.getClass(), gameID.getId());
                boards.setGameID(gameID);
            }
            em.persist(boards);
            if (gameID != null) {
                gameID.getBoardsCollection().add(boards);
                gameID = em.merge(gameID);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Boards boards) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Boards persistentBoards = em.find(Boards.class, boards.getId());
            Games gameIDOld = persistentBoards.getGameID();
            Games gameIDNew = boards.getGameID();
            if (gameIDNew != null) {
                gameIDNew = em.getReference(gameIDNew.getClass(), gameIDNew.getId());
                boards.setGameID(gameIDNew);
            }
            boards = em.merge(boards);
            if (gameIDOld != null && !gameIDOld.equals(gameIDNew)) {
                gameIDOld.getBoardsCollection().remove(boards);
                gameIDOld = em.merge(gameIDOld);
            }
            if (gameIDNew != null && !gameIDNew.equals(gameIDOld)) {
                gameIDNew.getBoardsCollection().add(boards);
                gameIDNew = em.merge(gameIDNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = boards.getId();
                if (findBoards(id) == null) {
                    throw new NonexistentEntityException("The boards with id " + id + " no longer exists.");
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
            Boards boards;
            try {
                boards = em.getReference(Boards.class, id);
                boards.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The boards with id " + id + " no longer exists.", enfe);
            }
            Games gameID = boards.getGameID();
            if (gameID != null) {
                gameID.getBoardsCollection().remove(boards);
                gameID = em.merge(gameID);
            }
            em.remove(boards);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Boards> findBoardsEntities() {
        return findBoardsEntities(true, -1, -1);
    }

    public List<Boards> findBoardsEntities(int maxResults, int firstResult) {
        return findBoardsEntities(false, maxResults, firstResult);
    }

    private List<Boards> findBoardsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Boards.class));
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

    public Boards findBoards(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Boards.class, id);
        } finally {
            em.close();
        }
    }

    public int getBoardsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Boards> rt = cq.from(Boards.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
