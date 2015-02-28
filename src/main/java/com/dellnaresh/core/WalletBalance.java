package com.dellnaresh.core;

import java.math.BigDecimal;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Track wallet balances.
 *
 * This object implements 3 balances:
 *      current balance - current balance of commited transactions
 *      avail balance - avail balance less money reserved for debit txns in progress
 *      pend balance - avail balance + money reserved for credit txns in progress
 *
 * Note that pend balance is a relatively new addition. It is only
 * actively used for trans core version is set to 6.
 *
 * @author Andrew Barnhama
 */
public class WalletBalance
{
    private BigDecimal mAvailBalance;
    private BigDecimal mCurrentBalance;
    private BigDecimal mPendBalance;

    public WalletBalance()
    {
        this(null,null,null);
    }


    /**
     * Creates a new instance of WalletBalance
     *
     * @param aCurrentBalance - the current balance
     * @param aAvailBalance - the available balance
     * @param aPendBalance - the pending balance
     */
    public WalletBalance(BigDecimal aCurrentBalance,BigDecimal aAvailBalance,BigDecimal aPendBalance)
    {
        mCurrentBalance=aCurrentBalance;
        mAvailBalance=aAvailBalance;
        mPendBalance=aPendBalance;
    }

    /**
     * Creates a new instance of WalletBalance
     *
     * @param aCurrentBalance - the current balance
     * @param aAvailBalance - the available balance
     */
    public WalletBalance(BigDecimal aCurrentBalance,BigDecimal aAvailBalance)
    {
        this(aCurrentBalance,aAvailBalance,null);
    }

    public BigDecimal getCurrentBalance()
    {
        return mCurrentBalance;
    }

    public BigDecimal getAvailableBalance()
    {
        return mAvailBalance;
    }

    public BigDecimal getPendingBalance()
    {
        return mPendBalance;
    }

    /**
     * Add passed balance to this balane and return the resultant object
     *
     * @param aBalance the balance to add
     *
     * @return the sum of this wallet and aBalance
     */
    public WalletBalance add(WalletBalance aBalance)
    {
        if (getClass()!=WalletBalance.class || aBalance.getClass()==WalletBalance.class) {
            return new WalletBalance(
                    getCurrentBalance().add(aBalance.getCurrentBalance()),
                    getAvailableBalance().add(aBalance.getAvailableBalance()),
                    getPendingBalance().add(aBalance.getPendingBalance())
            );
        }
        return aBalance.add(this);
    }

    /**
     * subtract passed balance to this balane and return the resultant object
     *
     * @param aBalance the balance to subtract
     *
     * @return the difference between this wallet and aBalance
     */
    public WalletBalance subtract(WalletBalance aBalance)
    {
        if (getClass()!=WalletBalance.class || aBalance.getClass()==WalletBalance.class) {
            return new WalletBalance(
                    getCurrentBalance().subtract(aBalance.getCurrentBalance()),
                    getAvailableBalance().subtract(aBalance.getAvailableBalance()),
                    getPendingBalance().subtract(aBalance.getPendingBalance())
            );
        }
        return aBalance.subtract(this).negate();
    }

    /**
     * return the negative of this balance.
     *
     * @return the negative of this balance
     */
    public WalletBalance negate()
    {
        return new WalletBalance(
                getCurrentBalance().negate(),
                getAvailableBalance().negate(),
                getPendingBalance().negate()
        );
    }

    /**
     * Get a balance based on requested name parameter
     *
     * @param name name of balance to query. valid values are 'available', 'current' and 'pending'
     */
    public BigDecimal getBalance(String name)
    {
        if (name.startsWith("av")) return getAvailableBalance();
        if (name.startsWith("cu")) return getCurrentBalance();
        if (name.startsWith("pe")) return getPendingBalance();
        return null;
    }

    /**
     * return zero is balances are all zero
     */
    public boolean isZero()
    {
        return
                (getAvailableBalance().signum()==0 &&
                        getCurrentBalance().signum()==0 &&
                        getPendingBalance().signum()==0);
    }

    public boolean equals(Object o)
    {
        if (o==null) return false;
        if (o instanceof WalletBalance) {
            if (getClass()==WalletBalance.class && o.getClass()!=WalletBalance.class) {
                return o.equals(this);
            }
            WalletBalance target = (WalletBalance)o;
            return
                    target.getCurrentBalance().equals(getCurrentBalance())
                            &&
                            target.getAvailableBalance().equals(getAvailableBalance())
                            &&
                            target.getPendingBalance().equals(getPendingBalance());
        }
        return false;
    }

    /** returns String of form c: current_balance a: available_balance p: pending_balance */
    public String toString()
    {
        return "(c:"+getCurrentBalance()+" a:"+getAvailableBalance()+" p:"+getPendingBalance()+")";
    }
}
