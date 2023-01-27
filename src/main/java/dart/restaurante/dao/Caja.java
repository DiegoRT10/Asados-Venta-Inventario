/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dart.restaurante.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Diego Ramos
 */
@Entity
@Table(name = "Caja")
@NamedQueries({
    @NamedQuery(name = "Caja.findAll", query = "SELECT c FROM Caja c"),
    @NamedQuery(name = "Caja.findById", query = "SELECT c FROM Caja c WHERE c.id = :id"),
    @NamedQuery(name = "Caja.findByTotal", query = "SELECT c FROM Caja c WHERE c.total = :total")})
public class Caja implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total")
    private BigDecimal total;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCaja")
//    private Collection<Gasto> gastoCollection;
    @JoinColumn(name = "idApertura", referencedColumnName = "id")
    @ManyToOne
    private Apertura idApertura;
    @JoinColumn(name = "idCierre", referencedColumnName = "id")
    @ManyToOne
    private Cierre idCierre;
    @JoinColumn(name = "idGasto", referencedColumnName = "id")
    @ManyToOne
    private Gasto idGasto;

    public Caja() {
    }

    public Caja(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

//    public Collection<Gasto> getGastoCollection() {
//        return gastoCollection;
//    }
//
//    public void setGastoCollection(Collection<Gasto> gastoCollection) {
//        this.gastoCollection = gastoCollection;
//    }

    public Apertura getIdApertura() {
        return idApertura;
    }

    public void setIdApertura(Apertura idApertura) {
        this.idApertura = idApertura;
    }

    public Cierre getIdCierre() {
        return idCierre;
    }

    public void setIdCierre(Cierre idCierre) {
        this.idCierre = idCierre;
    }

    public Gasto getIdGasto() {
        return idGasto;
    }

    public void setIdGasto(Gasto idGasto) {
        this.idGasto = idGasto;
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
        if (!(object instanceof Caja)) {
            return false;
        }
        Caja other = (Caja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dart.restaurante.dao.Caja[ id=" + id + " ]";
    }
    
}
