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
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author doron
 */
@Entity
@Table(name = "Users")
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findById", query = "SELECT u FROM Users u WHERE u.id = :id")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userID")
    private Collection<UsersVsGames> usersVsGamesCollection;
    @JoinColumn(name = "EmailID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private Emails emailID;
    @JoinColumn(name = "NameID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private Names nameID;
    @JoinColumn(name = "PasswordID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private Passwords passwordID;
    @JoinColumn(name = "SaltID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private Salts saltID;

    public Users() {
    }

    public Users(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Collection<UsersVsGames> getUsersVsGamesCollection() {
        return usersVsGamesCollection;
    }

    public void setUsersVsGamesCollection(Collection<UsersVsGames> usersVsGamesCollection) {
        this.usersVsGamesCollection = usersVsGamesCollection;
    }

    public Emails getEmailID() {
        return emailID;
    }

    public void setEmailID(Emails emailID) {
        this.emailID = emailID;
    }

    public Names getNameID() {
        return nameID;
    }

    public void setNameID(Names nameID) {
        this.nameID = nameID;
    }

    public Passwords getPasswordID() {
        return passwordID;
    }

    public void setPasswordID(Passwords passwordID) {
        this.passwordID = passwordID;
    }

    public Salts getSaltID() {
        return saltID;
    }

    public void setSaltID(Salts saltID) {
        this.saltID = saltID;
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
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "connectors.Users[ id=" + id + " ]";
    }
    
}
