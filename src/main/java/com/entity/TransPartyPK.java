/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author nareshm
 */
@Embeddable
public class TransPartyPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "transid")
    private long transid;
    @Basic(optional = false)
    @Column(name = "accountid")
    private long accountid;

    public TransPartyPK() {
    }

    public TransPartyPK(long transid, long accountid) {
        this.transid = transid;
        this.accountid = accountid;
    }

    public long getTransid() {
        return transid;
    }

    public void setTransid(long transid) {
        this.transid = transid;
    }

    public long getAccountid() {
        return accountid;
    }

    public void setAccountid(long accountid) {
        this.accountid = accountid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) transid;
        hash += (int) accountid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransPartyPK)) {
            return false;
        }
        TransPartyPK other = (TransPartyPK) object;
        if (this.transid != other.transid) {
            return false;
        }
        if (this.accountid != other.accountid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.TransPartyPK[ transid=" + transid + ", accountid=" + accountid + " ]";
    }
    
}
