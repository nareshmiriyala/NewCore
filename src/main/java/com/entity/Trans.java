/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author nareshm
 */
@Entity
@Table(name = "trans")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Trans.findAll", query = "SELECT t FROM Trans t"),
    @NamedQuery(name = "Trans.findById", query = "SELECT t FROM Trans t WHERE t.id = :id"),
    @NamedQuery(name = "Trans.findByType", query = "SELECT t FROM Trans t WHERE t.type = :type"),
    @NamedQuery(name = "Trans.findByResult", query = "SELECT t FROM Trans t WHERE t.result = :result"),
    @NamedQuery(name = "Trans.findByState", query = "SELECT t FROM Trans t WHERE t.state = :state"),
    @NamedQuery(name = "Trans.findByCreated", query = "SELECT t FROM Trans t WHERE t.created = :created"),
    @NamedQuery(name = "Trans.findByLastModified", query = "SELECT t FROM Trans t WHERE t.lastModified = :lastModified")})
public class Trans implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trans")
    private Collection<TransParty> transPartyCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "result")
    private short result;
    @Basic(optional = false)
    @Column(name = "state")
    private short state;
    @Basic(optional = false)
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Basic(optional = false)
    @Column(name = "last_modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;

    public Trans() {
    }

    public Trans(Long id) {
        this.id = id;
    }

    public Trans(Long id, String type, short result, short state, Date created, Date lastModified) {
        this.id = id;
        this.type = type;
        this.result = result;
        this.state = state;
        this.created = created;
        this.lastModified = lastModified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public short getResult() {
        return result;
    }

    public void setResult(short result) {
        this.result = result;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
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
        if (!(object instanceof Trans)) {
            return false;
        }
        Trans other = (Trans) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Trans[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<TransParty> getTransPartyCollection() {
        return transPartyCollection;
    }

    public void setTransPartyCollection(Collection<TransParty> transPartyCollection) {
        this.transPartyCollection = transPartyCollection;
    }
    
}
