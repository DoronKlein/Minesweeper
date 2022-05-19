/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connectors;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author doron
 */
@Entity
@Table(name = "Salts")
@NamedQueries({
    @NamedQuery(name = "Salts.findAll", query = "SELECT s FROM Salts s"),
    @NamedQuery(name = "Salts.findById", query = "SELECT s FROM Salts s WHERE s.id = :id"),
    @NamedQuery(name = "Salts.findBySalt", query = "SELECT s FROM Salts s WHERE s.salt = :salt")})
public class Salts implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Salt")
    private String salt;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "saltID")
    private Users users;

    public Salts() {
    }

    public Salts(Integer id) {
        this.id = id;
    }

    public Salts(Integer id, String salt) {
        this.id = id;
        this.salt = salt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
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
        if (!(object instanceof Salts)) {
            return false;
        }
        Salts other = (Salts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "connectors.Salts[ id=" + id + " ]";
    }
    
}
