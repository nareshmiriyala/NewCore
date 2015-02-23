/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.controller.exceptions.NonexistentEntityException;
import com.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.entity.Trans;
import com.entity.Account;
import com.entity.TransParty;
import com.entity.TransPartyPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author nareshm
 */
public class TransPartyJpaController implements Serializable {

    public TransPartyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TransParty transParty) throws PreexistingEntityException, Exception {
        if (transParty.getTransPartyPK() == null) {
            transParty.setTransPartyPK(new TransPartyPK());
        }
        transParty.getTransPartyPK().setAccountid(transParty.getAccount().getId());
        transParty.getTransPartyPK().setTransid(transParty.getTrans().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Trans trans = transParty.getTrans();
            if (trans != null) {
                trans = em.getReference(trans.getClass(), trans.getId());
                transParty.setTrans(trans);
            }
            Account account = transParty.getAccount();
            if (account != null) {
                account = em.getReference(account.getClass(), account.getId());
                transParty.setAccount(account);
            }
            em.persist(transParty);
            if (trans != null) {
                trans.getTransPartyCollection().add(transParty);
                trans = em.merge(trans);
            }
            if (account != null) {
                account.getTransPartyCollection().add(transParty);
                account = em.merge(account);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTransParty(transParty.getTransPartyPK()) != null) {
                throw new PreexistingEntityException("TransParty " + transParty + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TransParty transParty) throws NonexistentEntityException, Exception {
        transParty.getTransPartyPK().setAccountid(transParty.getAccount().getId());
        transParty.getTransPartyPK().setTransid(transParty.getTrans().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TransParty persistentTransParty = em.find(TransParty.class, transParty.getTransPartyPK());
            Trans transOld = persistentTransParty.getTrans();
            Trans transNew = transParty.getTrans();
            Account accountOld = persistentTransParty.getAccount();
            Account accountNew = transParty.getAccount();
            if (transNew != null) {
                transNew = em.getReference(transNew.getClass(), transNew.getId());
                transParty.setTrans(transNew);
            }
            if (accountNew != null) {
                accountNew = em.getReference(accountNew.getClass(), accountNew.getId());
                transParty.setAccount(accountNew);
            }
            transParty = em.merge(transParty);
            if (transOld != null && !transOld.equals(transNew)) {
                transOld.getTransPartyCollection().remove(transParty);
                transOld = em.merge(transOld);
            }
            if (transNew != null && !transNew.equals(transOld)) {
                transNew.getTransPartyCollection().add(transParty);
                transNew = em.merge(transNew);
            }
            if (accountOld != null && !accountOld.equals(accountNew)) {
                accountOld.getTransPartyCollection().remove(transParty);
                accountOld = em.merge(accountOld);
            }
            if (accountNew != null && !accountNew.equals(accountOld)) {
                accountNew.getTransPartyCollection().add(transParty);
                accountNew = em.merge(accountNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                TransPartyPK id = transParty.getTransPartyPK();
                if (findTransParty(id) == null) {
                    throw new NonexistentEntityException("The transParty with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(TransPartyPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TransParty transParty;
            try {
                transParty = em.getReference(TransParty.class, id);
                transParty.getTransPartyPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transParty with id " + id + " no longer exists.", enfe);
            }
            Trans trans = transParty.getTrans();
            if (trans != null) {
                trans.getTransPartyCollection().remove(transParty);
                trans = em.merge(trans);
            }
            Account account = transParty.getAccount();
            if (account != null) {
                account.getTransPartyCollection().remove(transParty);
                account = em.merge(account);
            }
            em.remove(transParty);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TransParty> findTransPartyEntities() {
        return findTransPartyEntities(true, -1, -1);
    }

    public List<TransParty> findTransPartyEntities(int maxResults, int firstResult) {
        return findTransPartyEntities(false, maxResults, firstResult);
    }

    private List<TransParty> findTransPartyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TransParty.class));
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

    public TransParty findTransParty(TransPartyPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TransParty.class, id);
        } finally {
            em.close();
        }
    }

    public int getTransPartyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TransParty> rt = cq.from(TransParty.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
