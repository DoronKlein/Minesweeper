/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connectors;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author doron
 */
@Entity
@Table(name = "UsersVsGames")
@NamedQueries({
    @NamedQuery(name = "UsersVsGames.findAll", query = "SELECT u FROM UsersVsGames u"),
    @NamedQuery(name = "UsersVsGames.findById", query = "SELECT u FROM UsersVsGames u WHERE u.id = :id")})
public class UsersVsGames implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "GameID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Games gameID;
    @JoinColumn(name = "UserID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Users userID;

    public UsersVsGames() {
    }

    public UsersVsGames(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Games getGameID() {
        return gameID;
    }

    public void setGameID(Games gameID) {
        this.gameID = gameID;
    }

    public Users getUserID() {
        return userID;
    }

    public void setUserID(Users userID) {
        this.userID = userID;
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
        if (!(object instanceof UsersVsGames)) {
            return false;
        }
        UsersVsGames other = (UsersVsGames) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "connectors.UsersVsGames[ id=" + id + " ]";
    }
    
}
