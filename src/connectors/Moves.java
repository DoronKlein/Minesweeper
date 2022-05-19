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
@Table(name = "Moves")
@NamedQueries({
    @NamedQuery(name = "Moves.findAll", query = "SELECT m FROM Moves m"),
    @NamedQuery(name = "Moves.findById", query = "SELECT m FROM Moves m WHERE m.id = :id"),
    @NamedQuery(name = "Moves.findBySelectedRow", query = "SELECT m FROM Moves m WHERE m.selectedRow = :selectedRow"),
    @NamedQuery(name = "Moves.findBySelectedColumn", query = "SELECT m FROM Moves m WHERE m.selectedColumn = :selectedColumn"),
    @NamedQuery(name = "Moves.findByAction", query = "SELECT m FROM Moves m WHERE m.action = :action")})
public class Moves implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "SelectedRow")
    private int selectedRow;
    @Basic(optional = false)
    @Column(name = "SelectedColumn")
    private int selectedColumn;
    @Basic(optional = false)
    @Column(name = "Action")
    private boolean action;
    @JoinColumn(name = "GameID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Games gameID;

    public Moves() {
    }

    public Moves(Integer id) {
        this.id = id;
    }

    public Moves(Integer id, int selectedRow, int selectedColumn, boolean action) {
        this.id = id;
        this.selectedRow = selectedRow;
        this.selectedColumn = selectedColumn;
        this.action = action;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    public int getSelectedColumn() {
        return selectedColumn;
    }

    public void setSelectedColumn(int selectedColumn) {
        this.selectedColumn = selectedColumn;
    }

    public boolean getAction() {
        return action;
    }

    public void setAction(boolean action) {
        this.action = action;
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
        if (!(object instanceof Moves)) {
            return false;
        }
        Moves other = (Moves) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "connectors.Moves[ id=" + id + " ]";
    }
    
}
