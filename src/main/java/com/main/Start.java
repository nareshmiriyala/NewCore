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
import com.util.Constants;

/**
 * @author nareshm
 */
public class Start {
    public static void main(String args[]) {
        EntityManagerFactory emf = getEntityManagerFactory();
        try {
            TransJpaController transJpaController = new TransJpaController(emf);
            Trans trans=new Trans("transfer",(short)1001,(short)0,new Date(),new Date());
            Account debtoraccount = new Account(BigDecimal.TEN, BigDecimal.ONE, BigDecimal.TEN, new Date());
            Account creditoraccount = new Account(BigDecimal.valueOf(100), BigDecimal.ONE, BigDecimal.TEN, new Date());
            TransParty debtortransParty = new TransParty(Constants.DEBTOR, 10l, 20l, trans, debtoraccount);
            TransParty creditortransParty = new TransParty(Constants.CREDITOR, 10l, 50l, trans, creditoraccount);
            transJpaController.create(trans);
            AccountJpaController accountJpaController = new AccountJpaController(emf);
            accountJpaController.create(debtoraccount);
            accountJpaController.create(creditoraccount);
            TransPartyJpaController transPartyJpaController = new TransPartyJpaController(emf);
            transPartyJpaController.create(creditortransParty);
            transPartyJpaController.create(debtortransParty);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            emf.close();
        }
    }

    private static EntityManagerFactory getEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("com.mycompany_Core_jar_1.0-SNAPSHOTPU");
    }


}
