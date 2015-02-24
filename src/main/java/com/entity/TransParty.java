/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nareshm
 */
@Entity
@Table(name = "trans_party")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransParty.findAll", query = "SELECT t FROM TransParty t"),
    @NamedQuery(name = "TransParty.findByTransid", query = "SELECT t FROM TransParty t WHERE t.transPartyPK.transid = :transid"),
    @NamedQuery(name = "TransParty.findByCurrBalance", query = "SELECT t FROM TransParty t WHERE t.currBalance = :currBalance"),
    @NamedQuery(name = "TransParty.findByAmount", query = "SELECT t FROM TransParty t WHERE t.amount = :amount"),
    @NamedQuery(name = "TransParty.findByAccountid", query = "SELECT t FROM TransParty t WHERE t.transPartyPK.accountid = :accountid")})
public class TransParty implements Serializable {
    @Basic(optional = false)
    @Column(name = "type")
    private int type;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TransPartyPK transPartyPK;
    @Column(name = "curr_balance")
    private Long currBalance;
    @Column(name = "amount")
    private Long amount;
    @JoinColumn(name = "transid", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Trans trans;
    @JoinColumn(name = "accountid", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Account account;

    public TransParty() {
    }

    public TransParty(int type,Long amount,Long currBalance,Trans trans,Account account) {
        this.type=type;
        this.amount=amount;
        this.trans=trans;
        this.account=account;
        this.currBalance=currBalance;
    }

    public TransParty(TransPartyPK transPartyPK) {
        this.transPartyPK = transPartyPK;
    }

    public TransParty(long transid, long accountid) {
        this.transPartyPK = new TransPartyPK(transid, accountid);
    }

    public TransPartyPK getTransPartyPK() {
        return transPartyPK;
    }

    public void setTransPartyPK(TransPartyPK transPartyPK) {
        this.transPartyPK = transPartyPK;
    }

    public Long getCurrBalance() {
        return currBalance;
    }

    public void setCurrBalance(Long currBalance) {
        this.currBalance = currBalance;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Trans getTrans() {
        return trans;
    }

    public void setTrans(Trans trans) {
        this.trans = trans;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transPartyPK != null ? transPartyPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransParty)) {
            return false;
        }
        TransParty other = (TransParty) object;
        if ((this.transPartyPK == null && other.transPartyPK != null) || (this.transPartyPK != null && !this.transPartyPK.equals(other.transPartyPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.TransParty[ transPartyPK=" + transPartyPK + " ]";
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
}
