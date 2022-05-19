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
@Table(name = "Boards")
@NamedQueries({
    @NamedQuery(name = "Boards.findAll", query = "SELECT b FROM Boards b"),
    @NamedQuery(name = "Boards.findById", query = "SELECT b FROM Boards b WHERE b.id = :id"),
    @NamedQuery(name = "Boards.findByRowValue", query = "SELECT b FROM Boards b WHERE b.rowValue = :rowValue")})
public class Boards implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "RowValue")
    private String rowValue;
    @JoinColumn(name = "GameID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Games gameID;

    public Boards() {
    }

    public Boards(Integer id) {
        this.id = id;
    }

    public Boards(Integer id, String rowValue) {
        this.id = id;
        this.rowValue = rowValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRowValue() {
        return rowValue;
    }

    public void setRowValue(String rowValue) {
        this.rowValue = rowValue;
    }

    public Games getGameID() {
        return gameID;
    }

    public void setGameID(Games gameID) {
        this.gameID = gameID;
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
        if (!(object instanceof Boards)) {
            return false;
        }
        Boards other = (Boards) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "connectors.Boards[ id=" + id + " ]";
    }
    
}
