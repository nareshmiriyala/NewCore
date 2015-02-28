package com.dellnaresh.core;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by nareshm on 28/02/2015.
 */
public class AdjustWallet {
    public static void main(String[] args) {



    }
    public int adjustWallet(WalletBalance adjust) {

        Map<Long, Account> accountMap=getLockedAccounts();

        long transParty=1045l;//transParty.getID or transParty.getProxyId
        long proxyIdParty=1046l;
        //get all the same party accounts
        List<Account> samePartyAccounts=getSamePartyAccounts(accountMap,transParty,proxyIdParty);
        //get the total balance of all same party accounts
        BigDecimal totalBalance=getTotalBalance(samePartyAccounts);
        if(totalBalance.compareTo(adjust.getAvailableBalance())<0){
            return 1001;//insufficient funds
        }




        return 0;
    }

    private BigDecimal getTotalBalance(List<Account> samePartyAccounts) {
        BigDecimal totalBalance=BigDecimal.ZERO;
        for(Account account:samePartyAccounts){
            totalBalance=totalBalance.add( account.getDirtyBalance().getAvailableBalance());
        }
        return totalBalance;
    }

    private List<Account> getSamePartyAccounts(Map<Long, Account> accountMap, long transParty, long proxyIdParty) {
        List<Account> accountList=new ArrayList<>();
        Iterator accountMapIterator=accountMap.entrySet().iterator();
        while (accountMapIterator.hasNext()){
            if(transParty==0 ){
                break;
            }
            Account  account= (Account) accountMapIterator.next();
            //check if the party has parent
            if(account.getParent()==transParty) {
                //if same proxy as account, add it to 0 index
                if (proxyIdParty != 0 && (account.getID() == proxyIdParty)) {
                    accountList.add(0, account);
                }else if(account.getID()==transParty){//parent account
                    accountList.add(1,account);

                }else {
                    accountList.add(account);
                }

            }
        }
        return accountList;
    }

    public static Map<Long,Account> getLockedAccounts(){
        return null;
    }
}
