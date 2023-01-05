/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dart.restaurante.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Diego Ramos
 */
@Entity
@Table(name = "VentaComida")
@NamedQueries({
    @NamedQuery(name = "VentaComida.findAll", query = "SELECT v FROM VentaComida v"),
    @NamedQuery(name = "VentaComida.findById", query = "SELECT v FROM VentaComida v WHERE v.id = :id"),
    @NamedQuery(name = "VentaComida.findByCantidadComida", query = "SELECT v FROM VentaComida v WHERE v.cantidadComida = :cantidadComida"),
    @NamedQuery(name = "VentaComida.findByTotal", query = "SELECT v FROM VentaComida v WHERE v.total = :total")})
public class VentaComida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Column(name = "cantidadComida")
    private Integer cantidadComida;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total")
    private BigDecimal total;
    @JoinColumn(name = "idComida", referencedColumnName = "id")
    @ManyToOne
    private Comida idComida;
    @JoinColumn(name = "idVentaDia", referencedColumnName = "id")
    @ManyToOne
    private VentaDia idVentaDia;

    public VentaComida() {
    }

    public VentaComida(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCantidadComida() {
        return cantidadComida;
    }

    public void setCantidadComida(Integer cantidadComida) {
        this.cantidadComida = cantidadComida;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Comida getIdComida() {
        return idComida;
    }

    public void setIdComida(Comida idComida) {
        this.idComida = idComida;
    }

    public VentaDia getIdVentaDia() {
        return idVentaDia;
    }

    public void setIdVentaDia(VentaDia idVentaDia) {
        this.idVentaDia = idVentaDia;
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
        if (!(object instanceof VentaComida)) {
            return false;
        }
        VentaComida other = (VentaComida) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dart.restaurante.dao.VentaComida[ id=" + id + " ]";
    }
    
}
