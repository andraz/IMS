/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ims.entity;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *JPA entity Role, which represents a table role in database.
 * @author andrazhribernik
 */
@Entity
@Table(name = "Role")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r"),
    @NamedQuery(name = "Role.findByIdRole", query = "SELECT r FROM Role r WHERE r.idRole = :idRole"),
    @NamedQuery(name = "Role.findByName", query = "SELECT r FROM Role r WHERE r.name = :name")})
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idRole")
    private Integer idRole;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleidRole")
    private Set<User> userSet;
    
    /**
     * Constructor
     */
    public Role() {
    }
    /**
     * Constructor
     * @param idRole 
     */
    public Role(Integer idRole) {
        this.idRole = idRole;
    }
    /**
     * Constructor
     * @param idRole
     * @param name 
     */
    public Role(Integer idRole, String name) {
        this.idRole = idRole;
        this.name = name;
    }
    /**
     * Get id of that Role
     * @return id
     */
    public Integer getIdRole() {
        return idRole;
    }
    /**
     * Set id of that Role
     * @param idRole 
     */
    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }
    /**
     * Get name of that Role
     * @return name 
     */
    public String getName() {
        return name;
    }
    /**
     * Set name of that role
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Get Set of Users with that Role
     * @return Set of Users
     */
    @XmlTransient
    public Set<User> getUserSet() {
        return userSet;
    }
    /**
     * Set Set of Users with that Role
     * @param userSet 
     */
    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRole != null ? idRole.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Role)) {
            return false;
        }
        Role other = (Role) object;
        if ((this.idRole == null && other.idRole != null) || (this.idRole != null && !this.idRole.equals(other.idRole))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ims.entity.Role[ idRole=" + idRole + " ]";
    }
    
}
