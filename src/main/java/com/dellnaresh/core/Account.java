package com.dellnaresh.core;

import java.math.BigDecimal;

import java.util.Date;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author  barney
 */
public class Account 
{
    /* copied from email from Ken 2003-08-04 */
    public static final int TYPE_INITIAL_STATE =    0;
    public static final int TYPE_MASTER =           1;
    public static final int TYPE_DISTRIBUTOR =      2;
    public static final int TYPE_RETAILER =         4;
    public static final int TYPE_USER =             8;
    
    public static final int TYPE_CORPORATE_WEB_PORTAL = 16;
    public static final int TYPE_CORPORATE_WEB_SERVICE = 32;

	public static final int TYPE_FAMILY_SIM =    1024;
    
    public static final int STATUS_INIT_STATE =      0;
    public static final int STATUS_PROVISIONED =     1;
    public static final int STATUS_ACTIVE =          2;
    public static final int STATUS_RESERVED =        4;
    public static final int STATUS_SUSPENDED =       8;
    public static final int STATUS_DELETED =         16;
    
    public static final int ACCOUNT_TYPE_INITIAL_STATE =      0;
    public static final int ACCOUNT_TYPE_DELIRIUM =           1;
    
    
    private long mID;
    private int mType;
    private long mAgent;
    private int mAgentType;
    private Date mCreated;
    private Date mOpenDate;
    
    private WalletBalance mBalance;
    private WalletBalance mDirtyBalance;
    
    private BigDecimal mThreshold;
    private BigDecimal mLimit;
    private BigDecimal mRate;
    private int mStatus;
    private BigDecimal mUpperLimit;
    private long mCapSetID;
    private long mParent;

    public long getParent() {
        return mParent;
    }

    public void setParent(long mParent) {
        this.mParent = mParent;
    }
	
    /**
     * Cache of cap track detail records
     */

	// Add extra attributes here for supporting AccountProduct(interests). Henry
	private long accountProductId;					//ACCOUNT_PRODUCT_ID
	private Date accountProductEffective;          	//ACCOUNT_PRODUCT_EFFECTIVE
	private BigDecimal minBalance;					//MIN_BALANCE
	private BigDecimal minCloseBalance;				//MIN_CLOSE_BALANCE
	private BigDecimal sumCloseBalance;            	//SUM_CLOSE_BALANCE
	private Date closeBalanceEffective;				//CLOSE_BALANCE_EFFECTIVE
	private BigDecimal interestAccrued; 			//INTEREST_ACCRUED
	private Date interestAccrualEffective; 			//INTEREST_ACCRUAL_EFFECTIVE
	private Date interestPostEffective;				//INTEREST_POST_EFFECTIVE
	// End of adding

	// Create a new constructor to support AccountProduct. Henry
	// This one maps the constructor
	// public Account( long aID,int aType,long anAgent,int anAgentType,
    //        Date aCreated,Date anOpenDate,
    //        BigDecimal anOpenBalance,BigDecimal aCurrBalance,BigDecimal aAvailBalance,
    //        BigDecimal aThreshold,BigDecimal aLimit,BigDecimal aRate,int aStatus,
	//		  BigDecimal aUpperLimit,long aCapSetID)
	// Keep the preview one for backwards compatible
	public Account(long mID, int mType, long mAgent, int mAgentType,
				Date mCreated, Date mOpenDate, BigDecimal anOpenBalance,
				BigDecimal aCurrBalance, BigDecimal aAvailBalance, BigDecimal mThreshold,
				BigDecimal mLimit, BigDecimal mRate, int mStatus,
				BigDecimal mUpperLimit, long mCapSetID, long accountProductId,
				Date accountProductEffective, BigDecimal minBalance,
				BigDecimal minCloseBalance, BigDecimal sumCloseBalance,
				Date closeBalanceEffective, BigDecimal interestAccrued,
				Date interestAccrualEffective, Date interestPostEffective ) {
		this(mID, mType, mAgent, mAgentType,mCreated, mOpenDate, anOpenBalance,
					aCurrBalance, aAvailBalance, mThreshold, mLimit, mRate,
					mStatus, mUpperLimit, mCapSetID);
		this.accountProductId = accountProductId;
		this.accountProductEffective = accountProductEffective;
		this.minBalance = minBalance;
		this.minCloseBalance = minCloseBalance;
		this.sumCloseBalance = sumCloseBalance;
		this.closeBalanceEffective = closeBalanceEffective;
		this.interestAccrued = interestAccrued;
		this.interestAccrualEffective = interestAccrualEffective;
		this.interestPostEffective = interestPostEffective;
	}
	
 	/**
 	 * New constructor to permit replacement/decoration of accounts by cloning internals of the object
 	 * Used initiallyby UI:Market unified wallet project. See U:Market codebase class:  UnifiedAccount
 	 * 
 	 * @param base The base Account object to clone
 	 */
     public Account(Account base) {
     	copyFrom(base);
     }
     
     public void copyFrom(Account base) {
 		this.mID = base.mID;
 		this.mType = base.mType;
 		this.mAgent = base.mAgent;
 		this.mAgentType = base.mAgentType;
 		this.mCreated = base.mCreated;
 		this.mOpenDate = base.mOpenDate;
 		this.mBalance = base.mBalance;
 		this.mDirtyBalance = base.mDirtyBalance;
 		this.mThreshold = base.mThreshold;
 		this.mLimit = base.mLimit;
 		this.mRate = base.mRate;
 		this.mStatus = base.mStatus;
 		this.mUpperLimit = base.mUpperLimit;
 		this.mCapSetID = base.mCapSetID;
 		this.accountProductId = base.accountProductId;
 		this.accountProductEffective = base.accountProductEffective;
 		this.minBalance = base.minBalance;
 		this.minCloseBalance = base.minCloseBalance;
 		this.sumCloseBalance = base.sumCloseBalance;
 		this.closeBalanceEffective = base.closeBalanceEffective;
 		this.interestAccrued = base.interestAccrued;
 		this.interestAccrualEffective = base.interestAccrualEffective;
 		this.interestPostEffective = base.interestPostEffective;
 		this.mData = base.mData;
                this.mParent = base.mParent;
 	}
 
	
	
    // Create another constructor to support AccountProduct. Henry
	// This one maps the constructor
	// public Account( long aID,int aType,long anAgent,int anAgentType,
    // 				Date aCreated,Date anOpenDate,
    //				BigDecimal anOpenBalance,WalletBalance aBalance,
    //				BigDecimal aThreshold,BigDecimal aLimit,BigDecimal aRate,int aStatus,
	//				BigDecimal aUpperLimit,long aCapSetID)
	// Keep the preview one for backwards compatible
	public Account(long mID, int mType, long mAgent, int mAgentType,
			Date mCreated, Date mOpenDate, BigDecimal anOpenBalance,
			WalletBalance aBalance, BigDecimal mThreshold,
			BigDecimal mLimit, BigDecimal mRate, int mStatus,
			BigDecimal mUpperLimit, long mCapSetID, long accountProductId,
			Date accountProductEffective, BigDecimal minBalance,
			BigDecimal minCloseBalance, BigDecimal sumCloseBalance,
			Date closeBalanceEffective, BigDecimal interestAccrued,
			Date interestAccrualEffective, Date interestPostEffective ) {
		this(mID, mType, mAgent, mAgentType, mCreated, mOpenDate, anOpenBalance,
				aBalance, mThreshold, mLimit, mRate, mStatus, mUpperLimit, mCapSetID );
		this.accountProductId = accountProductId;
		this.accountProductEffective = accountProductEffective;
		this.minBalance = minBalance;
		this.minCloseBalance = minCloseBalance;
		this.sumCloseBalance = sumCloseBalance;
		this.closeBalanceEffective = closeBalanceEffective;
		this.interestAccrued = interestAccrued;
		this.interestAccrualEffective = interestAccrualEffective;
		this.interestPostEffective = interestPostEffective;
	}
	
    
    /** Creates a new instance of Account */
    public Account( long aID,int aType,long anAgent,int anAgentType,
                    Date aCreated,Date anOpenDate,
                    BigDecimal anOpenBalance,BigDecimal aCurrBalance,BigDecimal aAvailBalance,
                    BigDecimal aThreshold,BigDecimal aLimit,BigDecimal aRate,int aStatus,
					BigDecimal aUpperLimit,long aCapSetID) 
    {
        mID=aID;
        mType=aType;
        mAgent=anAgent;
        mAgentType=anAgentType;
        mCreated=aCreated;
        mOpenDate=anOpenDate;
        setBalance(new WalletBalance(aCurrBalance,aAvailBalance));
        mThreshold=aThreshold;
        mLimit=aLimit;
        mRate=aRate;
        mStatus=aStatus;
		mUpperLimit=aUpperLimit;
		mCapSetID=aCapSetID;
    }
    
    

    
   
	public Account( long aID,int aType,long anAgent,int anAgentType,
                    Date aCreated,Date anOpenDate,
                    BigDecimal anOpenBalance,WalletBalance aBalance,
                    BigDecimal aThreshold,BigDecimal aLimit,BigDecimal aRate,int aStatus,
					BigDecimal aUpperLimit,long aCapSetID) 
    {
        mID=aID;
        mType=aType;
        mAgent=anAgent;
        mAgentType=anAgentType;
        mCreated=aCreated;
        mOpenDate=anOpenDate;
        setBalance(aBalance);
        mThreshold=aThreshold;
        mLimit=aLimit;
        mRate=aRate;
        mStatus=aStatus;
		mUpperLimit=aUpperLimit;
		mCapSetID=aCapSetID;
    }
    
    public Account()
    {
    }

    public long getID()
    {
        return mID;
    }
    
	/** returns value of DB column account.type (known values Account.ACCOUNT_TYPE_INITIAL_STATE (0) or Account.ACCOUNT_TYPE_DELIRIUM (1) ) */
    public int getType()
    {
        return mType;
    }
    
    /**
     * @deprecated use getOwnerID
     */
    public long getAgent()
    {
        return getOwnerID();
    }
    
    /**
     * Get the owner id 
     */
    public long getOwnerID()
    {
        return mAgent;
    }
    
	/** returns value of DB column account.agent_type (known values Account.TYPE_* - things like distributor, retailer etc)
	 * @see TYPE_INITIAL_STATE 
     * @see TYPE_MASTER 
     * @see TYPE_DISTRIBUTOR 
     * @see TYPE_RETAILER 
     * @see TYPE_USER 
	 */
    public int getAgentType()
    {
        return mAgentType;
    }
    
    /**
     * Test to see if AgentType is a Particular type (flag)
     * @param flag
     * @return true if it does
     */
    public boolean getAgentType(int flag)
    {
        return (getAgentType()&flag)==flag;
    }
    
    /**
     * Add this new flag to agenttype
     * @param flag - added flag
     * @return true if it does
     */
    public void addAgentType(int flag)
    {
        setAgentType(getAgentType()|flag);
    }
    
    /**
     * set agenttype to this new agent type
     * @param flag - new agent type
     */
    public void setAgentType(int flag)
    {
        mAgentType = flag;
    }
    
    public Date getCreated()
    {
        return mCreated;
    }
    
    public Date getOpenDate()
    {
        return mOpenDate;
    }
    
    /** 
     * Deprecated. Despite its name - tHis 
     * method return getDirtyBalance().getCurrentBalance()
     * not getBalance().getCurrentBalance()
     * because of dodgy horse shit code in U:Choose. Replaces these calls
     * with getBalance()/getDirtyBalance() from now on
     *
     * @deprecated use getDirtyBalance() or getBalance() 
     */
    public BigDecimal getCurrBalance()
    {
        return getDirtyBalance().getCurrentBalance();
    }
    
    /** 
     * Deprecated. Despite its name - tHis 
     * method return getDirtyBalance().getAvailableBalance()
     * not getBalance().getAvailableBalance()
     * because of dodgy horse shit code in U:Choose. Replaces these calls
     * with getBalance()/getDirtyBalance() from now on
     *
     * @deprecated use getDirtyBalance() or getBalance() 
     */
    public BigDecimal getAvailBalance()
    {
        return getDirtyBalance().getAvailableBalance();
    }
    
    /**
     * Get the read-commited balance for this wallet
     *
     * @return WalletBalance the wallet balance. Note that this value may not necessarily reflect
     *  what is in the database. They are only guaranteed to be equivalent if the account is 
     *  locked by the TransactionManager. The WalletBalance here is a cache of what was on the database
     *  at the time the Account record was created.
     */
    public WalletBalance getBalance()
    {
        return mBalance;
    }
    
    /**
     * Get the direct balance for this wallet. THe dirty balance is the commited
     * balance + or - changes that have been applied by the TransactionManagr but not 
     * yet commited to the database.
     *
     * e.g. if you are in preCommit() call getBalance() to get the balance at the start
     * of the transaction, but call this method to get the balance right now in the
     * context of the current transaction frame.
     *
     * @return WalletBalance the wallet balance
     */
    public WalletBalance getDirtyBalance()
    {
        if (mDirtyBalance!=null) return mDirtyBalance;
        return getBalance();
    }
    
    
    /**
     * Set dirty balance to passed value
     *
     * @param aBalance new dirty balance
     */
    public void commitBalanceChanges()
    {
	if (mDirtyBalance!=null) {
	        mBalance=mDirtyBalance;       
	}
        mDirtyBalance=null;
    }
    
    /**
     * Set dirty balance to passed value
     *
     * @param aBalance new dirty balance
     */
    public void rollbackBalanceChanges()
    {
        mDirtyBalance=null;
    }
    
    
    /**
     * Set committed balance to passed value
     *
     * @param aBalance new committed balance.
     */
    public void setBalance(WalletBalance aBalance)
    {
        mBalance=aBalance;
        mDirtyBalance=null;
    }
    

    /**
     * adjust dirty balance by supplied amount
     *
     * @param aBalance adjustment amount to apply to the balance
     */
    public void adjustDirtyBalance(WalletBalance aAdjust)
    {
        mDirtyBalance = getDirtyBalance().add(aAdjust);
    }
    
    /**
     * set dirty balance to supplied amount
     *
     * @param aBalance the new dirty balance
     */
    public void setDirtyBalance(WalletBalance aAdjust)
    {
        mDirtyBalance = aAdjust;
    }
    
        
    public BigDecimal getThreshold()
    {
        return mThreshold;
    }
    /**
     * Sets the threshold
     * @param newThreshold
     */
    public void setThreshold(BigDecimal newThreshold)
    {
        mThreshold=newThreshold;
    }
    
    public BigDecimal getLimit()
    {
        return mLimit;
    }
    
    public BigDecimal getRate()
    {
        return mRate;
    }

    /**
     * Sets the Rate
     * @param newRate
     */
    public void setRate(BigDecimal newRate)
    {
        mRate=newRate;
    }
    /**
     * Sets the rate
     * @param newRate
     */
    public void getRate(BigDecimal newRate)
    {
        mRate=newRate;
    }
    
    public int getStatus()
    {
        return mStatus;
    }
    /**
     * Sets the status
     * @param newRate
     */
    public void setStatus(int newStatus)
    {
        mStatus=newStatus;
    }

    public BigDecimal getUpperLimit()
    {
        return mUpperLimit;
    }

	public long getCapSetID()
	{
		return mCapSetID;
	}
    
    private Map mData;
    

    public void setID(long id)
    {
        mID=id;
    }
    
    public void setType(int type) 
    {
        mType=type;
    }
    
    public void setOwnerID(long id)
    {
        mAgent=id;
    }

    public void setCreated(Date created)
    {
        mCreated=created;
    }
    
    public void setOpenDate(Date open)
    {
        mOpenDate=open;
    }

    public void setLimit(BigDecimal limit)
    {
        mLimit=limit;
    }
    
    public void setUpperLimit(BigDecimal upperLimit)
    {
        mUpperLimit=upperLimit;
    }

    public void setCapSetID(long id)
    {
        mCapSetID=id;
    }

	
    // AccountProduct supporting attributes' setter & getter. Henry
    public long getAccountProductId() {
		return accountProductId;
	}
	public void setAccountProductId(long accountProductId) {
		this.accountProductId = accountProductId;
	}

	public Date getAccountProductEffective() {
		return accountProductEffective;
	}
	public void setAccountProductEffective(Date accountProductEffective) {
		this.accountProductEffective = accountProductEffective;
	}

	public BigDecimal getMinBalance() {
		return minBalance;
	}
	public void setMinBalance(BigDecimal minBalance) {
		this.minBalance = minBalance;
	}

	public BigDecimal getMinCloseBalance() {
		return minCloseBalance;
	}
	public void setMinCloseBalance(BigDecimal minCloseBalance) {
		this.minCloseBalance = minCloseBalance;
	}

	public BigDecimal getSumCloseBalance() {
		return sumCloseBalance;
	}
	public void setSumCloseBalance(BigDecimal sumCloseBalance) {
		this.sumCloseBalance = sumCloseBalance;
	}

	public Date getCloseBalanceEffective() {
		return closeBalanceEffective;
	}

	public void setCloseBalanceEffective(Date closeBalanceEffective) {
		this.closeBalanceEffective = closeBalanceEffective;
	}

	public BigDecimal getInterestAccrued() {
		return interestAccrued;
	}
	public void setInterestAccrued(BigDecimal interestAccrued) {
		this.interestAccrued = interestAccrued;
	}

	public Date getInterestAccrualEffective() {
		return interestAccrualEffective;
	}
	public void setInterestAccrualEffective(Date interestAccrualEffective) {
		this.interestAccrualEffective = interestAccrualEffective;
	}

	public Date getInterestPostEffective() {
		return interestPostEffective;
	}
	public void setInterestPostEffective(Date interestPostEffective) {
		this.interestPostEffective = interestPostEffective;
	}


	// End of setter & getter
}
