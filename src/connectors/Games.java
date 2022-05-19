/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connectors;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author doron
 */
@Entity
@Table(name = "Games")
@NamedQueries({
    @NamedQuery(name = "Games.findAll", query = "SELECT g FROM Games g"),
    @NamedQuery(name = "Games.findById", query = "SELECT g FROM Games g WHERE g.id = :id"),
    @NamedQuery(name = "Games.findByUserID", query = "SELECT g FROM Games g WHERE g.userID = :userID"),
    @NamedQuery(name = "Games.findByRows", query = "SELECT g FROM Games g WHERE g.rows = :rows"),
    @NamedQuery(name = "Games.findByColumns", query = "SELECT g FROM Games g WHERE g.columns = :columns"),
    @NamedQuery(name = "Games.findByMines", query = "SELECT g FROM Games g WHERE g.mines = :mines"),
    @NamedQuery(name = "Games.findByTime", query = "SELECT g FROM Games g WHERE g.time = :time"),
    @NamedQuery(name = "Games.findByIsWin", query = "SELECT g FROM Games g WHERE g.isWin = :isWin")})
public class Games implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "UserID")
    private int userID;
    @Basic(optional = false)
    @Column(name = "Rows")
    private int rows;
    @Basic(optional = false)
    @Column(name = "Columns")
    private int columns;
    @Basic(optional = false)
    @Column(name = "Mines")
    private int mines;
    @Basic(optional = false)
    @Column(name = "Time")
    private int time;
    @Column(name = "IsWin")
    private Boolean isWin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gameID")
    private Collection<UsersVsGames> usersVsGamesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gameID")
    private Collection<Moves> movesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gameID")
    private Collection<Boards> boardsCollection;

    public Games() {
    }

    public Games(Integer id) {
        this.id = id;
    }

    public Games(Integer id, int userID, int rows, int columns, int mines, int time) {
        this.id = id;
        this.userID = userID;
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Boolean getIsWin() {
        return isWin;
    }

    public void setIsWin(Boolean isWin) {
        this.isWin = isWin;
    }

    public Collection<UsersVsGames> getUsersVsGamesCollection() {
        return usersVsGamesCollection;
    }

    public void setUsersVsGamesCollection(Collection<UsersVsGames> usersVsGamesCollection) {
        this.usersVsGamesCollection = usersVsGamesCollection;
    }

    public Collection<Moves> getMovesCollection() {
        return movesCollection;
    }

    public void setMovesCollection(Collection<Moves> movesCollection) {
        this.movesCollection = movesCollection;
    }

    public Collection<Boards> getBoardsCollection() {
        return boardsCollection;
    }

    public void setBoardsCollection(Collection<Boards> boardsCollection) {
        this.boardsCollection = boardsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Games)) {
            return false;
        }
        Games other = (Games) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "connectors.Games[ id=" + id + " ]";
    }
    
}
