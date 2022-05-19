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
public class GamesJpaController implements Serializable {

    public GamesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Games games) {
        if (games.getUsersVsGamesCollection() == null) {
            games.setUsersVsGamesCollection(new ArrayList<UsersVsGames>());
        }
        if (games.getMovesCollection() == null) {
            games.setMovesCollection(new ArrayList<Moves>());
        }
        if (games.getBoardsCollection() == null) {
            games.setBoardsCollection(new ArrayList<Boards>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<UsersVsGames> attachedUsersVsGamesCollection = new ArrayList<UsersVsGames>();
            for (UsersVsGames usersVsGamesCollectionUsersVsGamesToAttach : games.getUsersVsGamesCollection()) {
                usersVsGamesCollectionUsersVsGamesToAttach = em.getReference(usersVsGamesCollectionUsersVsGamesToAttach.getClass(), usersVsGamesCollectionUsersVsGamesToAttach.getId());
                attachedUsersVsGamesCollection.add(usersVsGamesCollectionUsersVsGamesToAttach);
            }
            games.setUsersVsGamesCollection(attachedUsersVsGamesCollection);
            Collection<Moves> attachedMovesCollection = new ArrayList<Moves>();
            for (Moves movesCollectionMovesToAttach : games.getMovesCollection()) {
                movesCollectionMovesToAttach = em.getReference(movesCollectionMovesToAttach.getClass(), movesCollectionMovesToAttach.getId());
                attachedMovesCollection.add(movesCollectionMovesToAttach);
            }
            games.setMovesCollection(attachedMovesCollection);
            Collection<Boards> attachedBoardsCollection = new ArrayList<Boards>();
            for (Boards boardsCollectionBoardsToAttach : games.getBoardsCollection()) {
                boardsCollectionBoardsToAttach = em.getReference(boardsCollectionBoardsToAttach.getClass(), boardsCollectionBoardsToAttach.getId());
                attachedBoardsCollection.add(boardsCollectionBoardsToAttach);
            }
            games.setBoardsCollection(attachedBoardsCollection);
            em.persist(games);
            for (UsersVsGames usersVsGamesCollectionUsersVsGames : games.getUsersVsGamesCollection()) {
                Games oldGameIDOfUsersVsGamesCollectionUsersVsGames = usersVsGamesCollectionUsersVsGames.getGameID();
                usersVsGamesCollectionUsersVsGames.setGameID(games);
                usersVsGamesCollectionUsersVsGames = em.merge(usersVsGamesCollectionUsersVsGames);
                if (oldGameIDOfUsersVsGamesCollectionUsersVsGames != null) {
                    oldGameIDOfUsersVsGamesCollectionUsersVsGames.getUsersVsGamesCollection().remove(usersVsGamesCollectionUsersVsGames);
                    oldGameIDOfUsersVsGamesCollectionUsersVsGames = em.merge(oldGameIDOfUsersVsGamesCollectionUsersVsGames);
                }
            }
            for (Moves movesCollectionMoves : games.getMovesCollection()) {
                Games oldGameIDOfMovesCollectionMoves = movesCollectionMoves.getGameID();
                movesCollectionMoves.setGameID(games);
                movesCollectionMoves = em.merge(movesCollectionMoves);
                if (oldGameIDOfMovesCollectionMoves != null) {
                    oldGameIDOfMovesCollectionMoves.getMovesCollection().remove(movesCollectionMoves);
                    oldGameIDOfMovesCollectionMoves = em.merge(oldGameIDOfMovesCollectionMoves);
                }
            }
            for (Boards boardsCollectionBoards : games.getBoardsCollection()) {
                Games oldGameIDOfBoardsCollectionBoards = boardsCollectionBoards.getGameID();
                boardsCollectionBoards.setGameID(games);
                boardsCollectionBoards = em.merge(boardsCollectionBoards);
                if (oldGameIDOfBoardsCollectionBoards != null) {
                    oldGameIDOfBoardsCollectionBoards.getBoardsCollection().remove(boardsCollectionBoards);
                    oldGameIDOfBoardsCollectionBoards = em.merge(oldGameIDOfBoardsCollectionBoards);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Games games) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Games persistentGames = em.find(Games.class, games.getId());
            Collection<UsersVsGames> usersVsGamesCollectionOld = persistentGames.getUsersVsGamesCollection();
            Collection<UsersVsGames> usersVsGamesCollectionNew = games.getUsersVsGamesCollection();
            Collection<Moves> movesCollectionOld = persistentGames.getMovesCollection();
            Collection<Moves> movesCollectionNew = games.getMovesCollection();
            Collection<Boards> boardsCollectionOld = persistentGames.getBoardsCollection();
            Collection<Boards> boardsCollectionNew = games.getBoardsCollection();
            List<String> illegalOrphanMessages = null;
            for (UsersVsGames usersVsGamesCollectionOldUsersVsGames : usersVsGamesCollectionOld) {
                if (!usersVsGamesCollectionNew.contains(usersVsGamesCollectionOldUsersVsGames)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsersVsGames " + usersVsGamesCollectionOldUsersVsGames + " since its gameID field is not nullable.");
                }
            }
            for (Moves movesCollectionOldMoves : movesCollectionOld) {
                if (!movesCollectionNew.contains(movesCollectionOldMoves)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Moves " + movesCollectionOldMoves + " since its gameID field is not nullable.");
                }
            }
            for (Boards boardsCollectionOldBoards : boardsCollectionOld) {
                if (!boardsCollectionNew.contains(boardsCollectionOldBoards)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Boards " + boardsCollectionOldBoards + " since its gameID field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<UsersVsGames> attachedUsersVsGamesCollectionNew = new ArrayList<UsersVsGames>();
            for (UsersVsGames usersVsGamesCollectionNewUsersVsGamesToAttach : usersVsGamesCollectionNew) {
                usersVsGamesCollectionNewUsersVsGamesToAttach = em.getReference(usersVsGamesCollectionNewUsersVsGamesToAttach.getClass(), usersVsGamesCollectionNewUsersVsGamesToAttach.getId());
                attachedUsersVsGamesCollectionNew.add(usersVsGamesCollectionNewUsersVsGamesToAttach);
            }
            usersVsGamesCollectionNew = attachedUsersVsGamesCollectionNew;
            games.setUsersVsGamesCollection(usersVsGamesCollectionNew);
            Collection<Moves> attachedMovesCollectionNew = new ArrayList<Moves>();
            for (Moves movesCollectionNewMovesToAttach : movesCollectionNew) {
                movesCollectionNewMovesToAttach = em.getReference(movesCollectionNewMovesToAttach.getClass(), movesCollectionNewMovesToAttach.getId());
                attachedMovesCollectionNew.add(movesCollectionNewMovesToAttach);
            }
            movesCollectionNew = attachedMovesCollectionNew;
            games.setMovesCollection(movesCollectionNew);
            Collection<Boards> attachedBoardsCollectionNew = new ArrayList<Boards>();
            for (Boards boardsCollectionNewBoardsToAttach : boardsCollectionNew) {
                boardsCollectionNewBoardsToAttach = em.getReference(boardsCollectionNewBoardsToAttach.getClass(), boardsCollectionNewBoardsToAttach.getId());
                attachedBoardsCollectionNew.add(boardsCollectionNewBoardsToAttach);
            }
            boardsCollectionNew = attachedBoardsCollectionNew;
            games.setBoardsCollection(boardsCollectionNew);
            games = em.merge(games);
            for (UsersVsGames usersVsGamesCollectionNewUsersVsGames : usersVsGamesCollectionNew) {
                if (!usersVsGamesCollectionOld.contains(usersVsGamesCollectionNewUsersVsGames)) {
                    Games oldGameIDOfUsersVsGamesCollectionNewUsersVsGames = usersVsGamesCollectionNewUsersVsGames.getGameID();
                    usersVsGamesCollectionNewUsersVsGames.setGameID(games);
                    usersVsGamesCollectionNewUsersVsGames = em.merge(usersVsGamesCollectionNewUsersVsGames);
                    if (oldGameIDOfUsersVsGamesCollectionNewUsersVsGames != null && !oldGameIDOfUsersVsGamesCollectionNewUsersVsGames.equals(games)) {
                        oldGameIDOfUsersVsGamesCollectionNewUsersVsGames.getUsersVsGamesCollection().remove(usersVsGamesCollectionNewUsersVsGames);
                        oldGameIDOfUsersVsGamesCollectionNewUsersVsGames = em.merge(oldGameIDOfUsersVsGamesCollectionNewUsersVsGames);
                    }
                }
            }
            for (Moves movesCollectionNewMoves : movesCollectionNew) {
                if (!movesCollectionOld.contains(movesCollectionNewMoves)) {
                    Games oldGameIDOfMovesCollectionNewMoves = movesCollectionNewMoves.getGameID();
                    movesCollectionNewMoves.setGameID(games);
                    movesCollectionNewMoves = em.merge(movesCollectionNewMoves);
                    if (oldGameIDOfMovesCollectionNewMoves != null && !oldGameIDOfMovesCollectionNewMoves.equals(games)) {
                        oldGameIDOfMovesCollectionNewMoves.getMovesCollection().remove(movesCollectionNewMoves);
                        oldGameIDOfMovesCollectionNewMoves = em.merge(oldGameIDOfMovesCollectionNewMoves);
                    }
                }
            }
            for (Boards boardsCollectionNewBoards : boardsCollectionNew) {
                if (!boardsCollectionOld.contains(boardsCollectionNewBoards)) {
                    Games oldGameIDOfBoardsCollectionNewBoards = boardsCollectionNewBoards.getGameID();
                    boardsCollectionNewBoards.setGameID(games);
                    boardsCollectionNewBoards = em.merge(boardsCollectionNewBoards);
                    if (oldGameIDOfBoardsCollectionNewBoards != null && !oldGameIDOfBoardsCollectionNewBoards.equals(games)) {
                        oldGameIDOfBoardsCollectionNewBoards.getBoardsCollection().remove(boardsCollectionNewBoards);
                        oldGameIDOfBoardsCollectionNewBoards = em.merge(oldGameIDOfBoardsCollectionNewBoards);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = games.getId();
                if (findGames(id) == null) {
                    throw new NonexistentEntityException("The games with id " + id + " no longer exists.");
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
            Games games;
            try {
                games = em.getReference(Games.class, id);
                games.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The games with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<UsersVsGames> usersVsGamesCollectionOrphanCheck = games.getUsersVsGamesCollection();
            for (UsersVsGames usersVsGamesCollectionOrphanCheckUsersVsGames : usersVsGamesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Games (" + games + ") cannot be destroyed since the UsersVsGames " + usersVsGamesCollectionOrphanCheckUsersVsGames + " in its usersVsGamesCollection field has a non-nullable gameID field.");
            }
            Collection<Moves> movesCollectionOrphanCheck = games.getMovesCollection();
            for (Moves movesCollectionOrphanCheckMoves : movesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Games (" + games + ") cannot be destroyed since the Moves " + movesCollectionOrphanCheckMoves + " in its movesCollection field has a non-nullable gameID field.");
            }
            Collection<Boards> boardsCollectionOrphanCheck = games.getBoardsCollection();
            for (Boards boardsCollectionOrphanCheckBoards : boardsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Games (" + games + ") cannot be destroyed since the Boards " + boardsCollectionOrphanCheckBoards + " in its boardsCollection field has a non-nullable gameID field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(games);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Games> findGamesEntities() {
        return findGamesEntities(true, -1, -1);
    }

    public List<Games> findGamesEntities(int maxResults, int firstResult) {
        return findGamesEntities(false, maxResults, firstResult);
    }

    private List<Games> findGamesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Games.class));
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

    public Games findGames(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Games.class, id);
        } finally {
            em.close();
        }
    }

    public int getGamesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Games> rt = cq.from(Games.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
