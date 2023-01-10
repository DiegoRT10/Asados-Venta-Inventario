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
@Table(name = "VentaProducto")
@NamedQueries({
    @NamedQuery(name = "VentaProducto.findAll", query = "SELECT v FROM VentaProducto v"),
    @NamedQuery(name = "VentaProducto.findById", query = "SELECT v FROM VentaProducto v WHERE v.id = :id"),
    @NamedQuery(name = "VentaProducto.findByCantidadProducto", query = "SELECT v FROM VentaProducto v WHERE v.cantidadProducto = :cantidadProducto"),
    @NamedQuery(name = "VentaProducto.findByTotal", query = "SELECT v FROM VentaProducto v WHERE v.total = :total")})
public class VentaProducto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Column(name = "cantidadProducto")
    private Integer cantidadProducto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total")
    private BigDecimal total;
    @JoinColumn(name = "idProducto", referencedColumnName = "id")
    @ManyToOne
    private Producto idProducto;
    @JoinColumn(name = "idVenta", referencedColumnName = "id")
    @ManyToOne
    private Venta idVenta;

    public VentaProducto() {
    }

    public VentaProducto(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(Integer cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }

    public Venta getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Venta idVenta) {
        this.idVenta = idVenta;
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
        if (!(object instanceof VentaProducto)) {
            return false;
        }
        VentaProducto other = (VentaProducto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dart.restaurante.dao.VentaProducto[ id=" + id + " ]";
    }
    
}
