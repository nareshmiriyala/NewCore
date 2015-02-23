/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.controller.exceptions.IllegalOrphanException;
import com.controller.exceptions.NonexistentEntityException;
import com.entity.Account;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.entity.TransParty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author nareshm
 */
public class AccountJpaController implements Serializable {

    public AccountJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Account account) {
        if (account.getTransPartyCollection() == null) {
            account.setTransPartyCollection(new ArrayList<TransParty>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<TransParty> attachedTransPartyCollection = new ArrayList<TransParty>();
            for (TransParty transPartyCollectionTransPartyToAttach : account.getTransPartyCollection()) {
                transPartyCollectionTransPartyToAttach = em.getReference(transPartyCollectionTransPartyToAttach.getClass(), transPartyCollectionTransPartyToAttach.getTransPartyPK());
                attachedTransPartyCollection.add(transPartyCollectionTransPartyToAttach);
            }
            account.setTransPartyCollection(attachedTransPartyCollection);
            em.persist(account);
            for (TransParty transPartyCollectionTransParty : account.getTransPartyCollection()) {
                Account oldAccountOfTransPartyCollectionTransParty = transPartyCollectionTransParty.getAccount();
                transPartyCollectionTransParty.setAccount(account);
                transPartyCollectionTransParty = em.merge(transPartyCollectionTransParty);
                if (oldAccountOfTransPartyCollectionTransParty != null) {
                    oldAccountOfTransPartyCollectionTransParty.getTransPartyCollection().remove(transPartyCollectionTransParty);
                    oldAccountOfTransPartyCollectionTransParty = em.merge(oldAccountOfTransPartyCollectionTransParty);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Account account) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Account persistentAccount = em.find(Account.class, account.getId());
            Collection<TransParty> transPartyCollectionOld = persistentAccount.getTransPartyCollection();
            Collection<TransParty> transPartyCollectionNew = account.getTransPartyCollection();
            List<String> illegalOrphanMessages = null;
            for (TransParty transPartyCollectionOldTransParty : transPartyCollectionOld) {
                if (!transPartyCollectionNew.contains(transPartyCollectionOldTransParty)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TransParty " + transPartyCollectionOldTransParty + " since its account field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<TransParty> attachedTransPartyCollectionNew = new ArrayList<TransParty>();
            for (TransParty transPartyCollectionNewTransPartyToAttach : transPartyCollectionNew) {
                transPartyCollectionNewTransPartyToAttach = em.getReference(transPartyCollectionNewTransPartyToAttach.getClass(), transPartyCollectionNewTransPartyToAttach.getTransPartyPK());
                attachedTransPartyCollectionNew.add(transPartyCollectionNewTransPartyToAttach);
            }
            transPartyCollectionNew = attachedTransPartyCollectionNew;
            account.setTransPartyCollection(transPartyCollectionNew);
            account = em.merge(account);
            for (TransParty transPartyCollectionNewTransParty : transPartyCollectionNew) {
                if (!transPartyCollectionOld.contains(transPartyCollectionNewTransParty)) {
                    Account oldAccountOfTransPartyCollectionNewTransParty = transPartyCollectionNewTransParty.getAccount();
                    transPartyCollectionNewTransParty.setAccount(account);
                    transPartyCollectionNewTransParty = em.merge(transPartyCollectionNewTransParty);
                    if (oldAccountOfTransPartyCollectionNewTransParty != null && !oldAccountOfTransPartyCollectionNewTransParty.equals(account)) {
                        oldAccountOfTransPartyCollectionNewTransParty.getTransPartyCollection().remove(transPartyCollectionNewTransParty);
                        oldAccountOfTransPartyCollectionNewTransParty = em.merge(oldAccountOfTransPartyCollectionNewTransParty);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = account.getId();
                if (findAccount(id) == null) {
                    throw new NonexistentEntityException("The account with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Account account;
            try {
                account = em.getReference(Account.class, id);
                account.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The account with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TransParty> transPartyCollectionOrphanCheck = account.getTransPartyCollection();
            for (TransParty transPartyCollectionOrphanCheckTransParty : transPartyCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Account (" + account + ") cannot be destroyed since the TransParty " + transPartyCollectionOrphanCheckTransParty + " in its transPartyCollection field has a non-nullable account field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(account);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Account> findAccountEntities() {
        return findAccountEntities(true, -1, -1);
    }

    public List<Account> findAccountEntities(int maxResults, int firstResult) {
        return findAccountEntities(false, maxResults, firstResult);
    }

    private List<Account> findAccountEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Account.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Account findAccount(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Account.class, id);
        } finally {
            em.close();
        }
    }

    public int getAccountCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Account> rt = cq.from(Account.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
