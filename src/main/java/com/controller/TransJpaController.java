/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.controller.exceptions.IllegalOrphanException;
import com.controller.exceptions.NonexistentEntityException;
import com.entity.Trans;
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
public class TransJpaController implements Serializable {

    public TransJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Trans trans) {
        if (trans.getTransPartyCollection() == null) {
            trans.setTransPartyCollection(new ArrayList<TransParty>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<TransParty> attachedTransPartyCollection = new ArrayList<TransParty>();
            for (TransParty transPartyCollectionTransPartyToAttach : trans.getTransPartyCollection()) {
                transPartyCollectionTransPartyToAttach = em.getReference(transPartyCollectionTransPartyToAttach.getClass(), transPartyCollectionTransPartyToAttach.getTransPartyPK());
                attachedTransPartyCollection.add(transPartyCollectionTransPartyToAttach);
            }
            trans.setTransPartyCollection(attachedTransPartyCollection);
            em.persist(trans);
            for (TransParty transPartyCollectionTransParty : trans.getTransPartyCollection()) {
                Trans oldTransOfTransPartyCollectionTransParty = transPartyCollectionTransParty.getTrans();
                transPartyCollectionTransParty.setTrans(trans);
                transPartyCollectionTransParty = em.merge(transPartyCollectionTransParty);
                if (oldTransOfTransPartyCollectionTransParty != null) {
                    oldTransOfTransPartyCollectionTransParty.getTransPartyCollection().remove(transPartyCollectionTransParty);
                    oldTransOfTransPartyCollectionTransParty = em.merge(oldTransOfTransPartyCollectionTransParty);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Trans trans) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Trans persistentTrans = em.find(Trans.class, trans.getId());
            Collection<TransParty> transPartyCollectionOld = persistentTrans.getTransPartyCollection();
            Collection<TransParty> transPartyCollectionNew = trans.getTransPartyCollection();
            List<String> illegalOrphanMessages = null;
            for (TransParty transPartyCollectionOldTransParty : transPartyCollectionOld) {
                if (!transPartyCollectionNew.contains(transPartyCollectionOldTransParty)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TransParty " + transPartyCollectionOldTransParty + " since its trans field is not nullable.");
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
            trans.setTransPartyCollection(transPartyCollectionNew);
            trans = em.merge(trans);
            for (TransParty transPartyCollectionNewTransParty : transPartyCollectionNew) {
                if (!transPartyCollectionOld.contains(transPartyCollectionNewTransParty)) {
                    Trans oldTransOfTransPartyCollectionNewTransParty = transPartyCollectionNewTransParty.getTrans();
                    transPartyCollectionNewTransParty.setTrans(trans);
                    transPartyCollectionNewTransParty = em.merge(transPartyCollectionNewTransParty);
                    if (oldTransOfTransPartyCollectionNewTransParty != null && !oldTransOfTransPartyCollectionNewTransParty.equals(trans)) {
                        oldTransOfTransPartyCollectionNewTransParty.getTransPartyCollection().remove(transPartyCollectionNewTransParty);
                        oldTransOfTransPartyCollectionNewTransParty = em.merge(oldTransOfTransPartyCollectionNewTransParty);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = trans.getId();
                if (findTrans(id) == null) {
                    throw new NonexistentEntityException("The trans with id " + id + " no longer exists.");
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
            Trans trans;
            try {
                trans = em.getReference(Trans.class, id);
                trans.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The trans with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TransParty> transPartyCollectionOrphanCheck = trans.getTransPartyCollection();
            for (TransParty transPartyCollectionOrphanCheckTransParty : transPartyCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Trans (" + trans + ") cannot be destroyed since the TransParty " + transPartyCollectionOrphanCheckTransParty + " in its transPartyCollection field has a non-nullable trans field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(trans);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Trans> findTransEntities() {
        return findTransEntities(true, -1, -1);
    }

    public List<Trans> findTransEntities(int maxResults, int firstResult) {
        return findTransEntities(false, maxResults, firstResult);
    }

    private List<Trans> findTransEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Trans.class));
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

    public Trans findTrans(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Trans.class, id);
        } finally {
            em.close();
        }
    }

    public int getTransCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Trans> rt = cq.from(Trans.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
