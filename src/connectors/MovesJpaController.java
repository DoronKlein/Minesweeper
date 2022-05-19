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
public class MovesJpaController implements Serializable {

    public MovesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Moves moves) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Games gameID = moves.getGameID();
            if (gameID != null) {
                gameID = em.getReference(gameID.getClass(), gameID.getId());
                moves.setGameID(gameID);
            }
            em.persist(moves);
            if (gameID != null) {
                gameID.getMovesCollection().add(moves);
                gameID = em.merge(gameID);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Moves moves) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Moves persistentMoves = em.find(Moves.class, moves.getId());
            Games gameIDOld = persistentMoves.getGameID();
            Games gameIDNew = moves.getGameID();
            if (gameIDNew != null) {
                gameIDNew = em.getReference(gameIDNew.getClass(), gameIDNew.getId());
                moves.setGameID(gameIDNew);
            }
            moves = em.merge(moves);
            if (gameIDOld != null && !gameIDOld.equals(gameIDNew)) {
                gameIDOld.getMovesCollection().remove(moves);
                gameIDOld = em.merge(gameIDOld);
            }
            if (gameIDNew != null && !gameIDNew.equals(gameIDOld)) {
                gameIDNew.getMovesCollection().add(moves);
                gameIDNew = em.merge(gameIDNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = moves.getId();
                if (findMoves(id) == null) {
                    throw new NonexistentEntityException("The moves with id " + id + " no longer exists.");
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
            Moves moves;
            try {
                moves = em.getReference(Moves.class, id);
                moves.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The moves with id " + id + " no longer exists.", enfe);
            }
            Games gameID = moves.getGameID();
            if (gameID != null) {
                gameID.getMovesCollection().remove(moves);
                gameID = em.merge(gameID);
            }
            em.remove(moves);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Moves> findMovesEntities() {
        return findMovesEntities(true, -1, -1);
    }

    public List<Moves> findMovesEntities(int maxResults, int firstResult) {
        return findMovesEntities(false, maxResults, firstResult);
    }

    private List<Moves> findMovesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Moves.class));
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

    public Moves findMoves(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Moves.class, id);
        } finally {
            em.close();
        }
    }

    public int getMovesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Moves> rt = cq.from(Moves.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
