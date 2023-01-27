/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dart.restaurante.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Diego Ramos
 */
@Entity
@Table(name = "Cierre")
@NamedQueries({
    @NamedQuery(name = "Cierre.findAll", query = "SELECT c FROM Cierre c"),
    @NamedQuery(name = "Cierre.findById", query = "SELECT c FROM Cierre c WHERE c.id = :id"),
    @NamedQuery(name = "Cierre.findByFechaCierre", query = "SELECT c FROM Cierre c WHERE c.fechaCierre = :fechaCierre"),
    @NamedQuery(name = "Cierre.findByMonto", query = "SELECT c FROM Cierre c WHERE c.monto = :monto")})
public class Cierre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Column(name = "fechaCierre")
    @Temporal(TemporalType.DATE)
    private Date fechaCierre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Monto")
    private BigDecimal monto;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCierre")
//    private Collection<Caja> cajaCollection;

    public Cierre() {
    }

    public Cierre(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

//    public Collection<Caja> getCajaCollection() {
//        return cajaCollection;
//    }
//
//    public void setCajaCollection(Collection<Caja> cajaCollection) {
//        this.cajaCollection = cajaCollection;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cierre)) {
            return false;
        }
        Cierre other = (Cierre) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dart.restaurante.dao.Cierre[ id=" + id + " ]";
    }
    
}
