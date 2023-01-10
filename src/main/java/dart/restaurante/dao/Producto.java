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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Diego Ramos
 */
@Entity
@Table(name = "Producto")
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p"),
    @NamedQuery(name = "Producto.findById", query = "SELECT p FROM Producto p WHERE p.id = :id"),
    @NamedQuery(name = "Producto.findByNombre", query = "SELECT p FROM Producto p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Producto.findByCategoria", query = "SELECT p FROM Producto p WHERE p.categoria = :categoria"),
    @NamedQuery(name = "Producto.findByUnidad", query = "SELECT p FROM Producto p WHERE p.unidad = :unidad"),
    @NamedQuery(name = "Producto.findByPrecioCompra", query = "SELECT p FROM Producto p WHERE p.precioCompra = :precioCompra"),
    @NamedQuery(name = "Producto.findByPrecioVenta", query = "SELECT p FROM Producto p WHERE p.precioVenta = :precioVenta"),
    @NamedQuery(name = "Producto.findByStock", query = "SELECT p FROM Producto p WHERE p.stock = :stock")})
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "categoria")
    private String categoria;
    @Basic(optional = false)
    @Column(name = "unidad")
    private String unidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "precioCompra")
    private BigDecimal precioCompra;
    @Basic(optional = false)
    @Column(name = "precioVenta")
    private BigDecimal precioVenta;
    @Basic(optional = false)
    @Column(name = "stock")
    private int stock;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProducto")
//    private Collection<DetalleCompra> detalleCompraCollection;
//    @OneToMany(mappedBy = "idProducto")
//    private Collection<VentaProducto> ventaProductoCollection;

    public Producto() {
    }

    public Producto(String id) {
        this.id = id;
    }

    public Producto(String id, String nombre, String categoria, String unidad, BigDecimal precioCompra, BigDecimal precioVenta, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.unidad = unidad;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

//    public Collection<DetalleCompra> getDetalleCompraCollection() {
//        return detalleCompraCollection;
//    }
//
//    public void setDetalleCompraCollection(Collection<DetalleCompra> detalleCompraCollection) {
//        this.detalleCompraCollection = detalleCompraCollection;
//    }
//
//    public Collection<VentaProducto> getVentaProductoCollection() {
//        return ventaProductoCollection;
//    }
//
//    public void setVentaProductoCollection(Collection<VentaProducto> ventaProductoCollection) {
//        this.ventaProductoCollection = ventaProductoCollection;
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
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dart.restaurante.dao.Producto[ id=" + id + " ]";
    }
    
}
