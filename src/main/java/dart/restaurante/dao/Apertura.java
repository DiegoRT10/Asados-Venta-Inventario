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
@Table(name = "Apertura")
@NamedQueries({
    @NamedQuery(name = "Apertura.findAll", query = "SELECT a FROM Apertura a"),
    @NamedQuery(name = "Apertura.findById", query = "SELECT a FROM Apertura a WHERE a.id = :id"),
    @NamedQuery(name = "Apertura.findByFechaApertura", query = "SELECT a FROM Apertura a WHERE a.fechaApertura = :fechaApertura"),
    @NamedQuery(name = "Apertura.findByMonto", query = "SELECT a FROM Apertura a WHERE a.monto = :monto")})
public class Apertura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Column(name = "fechaApertura")
    @Temporal(TemporalType.DATE)
    private Date fechaApertura;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Monto")
    private BigDecimal monto;
//    @OneToMany(mappedBy = "idApertura")
//    private Collection<Caja> cajaCollection;

    public Apertura() {
    }

    public Apertura(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
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
        if (!(object instanceof Apertura)) {
            return false;
        }
        Apertura other = (Apertura) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dart.restaurante.dao.Apertura[ id=" + id + " ]";
    }
    
}
