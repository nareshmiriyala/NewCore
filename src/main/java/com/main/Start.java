/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.main;

import com.controller.AccountJpaController;
import com.controller.TransJpaController;
import com.controller.TransPartyJpaController;
import com.entity.Account;
import com.entity.Trans;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.entity.TransParty;
import com.entity.TransPartyPK;

/**
 *
 * @author nareshm
 */
public class Start {
    public static void main(String args[]){
        EntityManagerFactory emf = getEntityManagerFactory();
        TransJpaController transJpaController=new TransJpaController(emf);

       Trans trans=new Trans();
       trans.setCreated(new Date());
       trans.setLastModified(new Date());
       trans.setResult((short)1001);
       trans.setState((short)0);
       trans.setType("transfer");
        Account account=new Account();
        account.setAvilableBalance(BigDecimal.ONE);
        account.setCreated(new Date());
        account.setCurrentBalance(BigDecimal.ONE);
        account.setPendingBalance(BigDecimal.ONE);
        TransParty transParty=new TransParty();
         transParty.setAccount(account);
        transParty.setTrans(trans);
        transParty.setAmount(100l);
        transParty.setCurrBalance(10l);
        try {
            transJpaController.create(trans);
            AccountJpaController accountJpaController=new AccountJpaController(emf);
            accountJpaController.create(account);
            TransPartyJpaController transPartyJpaController=new TransPartyJpaController(emf);
            transPartyJpaController.create(transParty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            emf.close();
        }
    }

    private static EntityManagerFactory getEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("com.mycompany_Core_jar_1.0-SNAPSHOTPU");
    }


}
