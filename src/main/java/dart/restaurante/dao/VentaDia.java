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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "VentaDia")
@NamedQueries({
    @NamedQuery(name = "VentaDia.findAll", query = "SELECT v FROM VentaDia v"),
    @NamedQuery(name = "VentaDia.findById", query = "SELECT v FROM VentaDia v WHERE v.id = :id"),
    @NamedQuery(name = "VentaDia.findByFechaVentaDia", query = "SELECT v FROM VentaDia v WHERE v.fechaVentaDia = :fechaVentaDia"),
    @NamedQuery(name = "VentaDia.findByTotal", query = "SELECT v FROM VentaDia v WHERE v.total = :total")})
public class VentaDia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Column(name = "fechaVentaDia")
    @Temporal(TemporalType.DATE)
    private Date fechaVentaDia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total")
    private BigDecimal total;
    @OneToMany(mappedBy = "idVentaDia")
    private Collection<VentaProducto> ventaProductoCollection;
    @OneToMany(mappedBy = "idVentaDia")
    private Collection<VentaComida> ventaComidaCollection;
    @JoinColumn(name = "idCliente", referencedColumnName = "id")
    @ManyToOne
    private Cliente idCliente;
    @JoinColumn(name = "idUsuario", referencedColumnName = "id")
    @ManyToOne
    private Usuario idUsuario;

    public VentaDia() {
    }

    public VentaDia(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFechaVentaDia() {
        return fechaVentaDia;
    }

    public void setFechaVentaDia(Date fechaVentaDia) {
        this.fechaVentaDia = fechaVentaDia;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Collection<VentaProducto> getVentaProductoCollection() {
        return ventaProductoCollection;
    }

    public void setVentaProductoCollection(Collection<VentaProducto> ventaProductoCollection) {
        this.ventaProductoCollection = ventaProductoCollection;
    }

    public Collection<VentaComida> getVentaComidaCollection() {
        return ventaComidaCollection;
    }

    public void setVentaComidaCollection(Collection<VentaComida> ventaComidaCollection) {
        this.ventaComidaCollection = ventaComidaCollection;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
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
        if (!(object instanceof VentaDia)) {
            return false;
        }
        VentaDia other = (VentaDia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dart.restaurante.dao.VentaDia[ id=" + id + " ]";
    }
    
}
