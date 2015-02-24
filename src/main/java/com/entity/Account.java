/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "account")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
    @NamedQuery(name = "Account.findById", query = "SELECT a FROM Account a WHERE a.id = :id"),
    @NamedQuery(name = "Account.findByCurrentBalance", query = "SELECT a FROM Account a WHERE a.currentBalance = :currentBalance"),
    @NamedQuery(name = "Account.findByAvilableBalance", query = "SELECT a FROM Account a WHERE a.avilableBalance = :avilableBalance"),
    @NamedQuery(name = "Account.findByPendingBalance", query = "SELECT a FROM Account a WHERE a.pendingBalance = :pendingBalance"),
    @NamedQuery(name = "Account.findByCreated", query = "SELECT a FROM Account a WHERE a.created = :created")})
public class Account implements Serializable {
    @Column(name = "current_balance")
    private BigDecimal currentBalance;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private Collection<TransParty> transPartyCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "avilable_balance")
    private BigDecimal avilableBalance;
    @Column(name = "pending_balance")
    private BigDecimal pendingBalance;
    @Basic(optional = false)
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public Account(BigDecimal avilableBalance,BigDecimal pendingBalance,BigDecimal currentBalance,Date created) {
        this.avilableBalance=avilableBalance;
        this.pendingBalance=pendingBalance;
        this.currentBalance=currentBalance;
        this.created=created;
    }

    public Account(){

    }
    public Account(Long id) {
        this.id = id;
    }

    public Account(Long id, Date created) {
        this.id = id;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public BigDecimal getAvilableBalance() {
        return avilableBalance;
    }

    public void setAvilableBalance(BigDecimal avilableBalance) {
        this.avilableBalance = avilableBalance;
    }

    public BigDecimal getPendingBalance() {
        return pendingBalance;
    }

    public void setPendingBalance(BigDecimal pendingBalance) {
        this.pendingBalance = pendingBalance;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Account[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<TransParty> getTransPartyCollection() {
        return transPartyCollection;
    }

    public void setTransPartyCollection(Collection<TransParty> transPartyCollection) {
        this.transPartyCollection = transPartyCollection;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }
    
}
