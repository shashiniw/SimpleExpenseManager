package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class PersistentExpenseManager extends ExpenseManager {
    private DatabaseHelper dbHelper;
    public PersistentExpenseManager( DatabaseHelper dbHelper) {
        this.dbHelper=dbHelper;
        setup();
    }

    @Override
    public void setup(){
        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(dbHelper);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(dbHelper);
        setAccountsDAO(persistentAccountDAO);

        // dummy data
        //Account dummyAcct1 = new Account("1234567A", "BOC", "Anakin Skywalker", 100000.0);
        //Account dummyAcct2 = new Account("7894567Z", "NDB", "Obi-Wan Kenobi", 800000.0);
        //getAccountsDAO().addAccount(dummyAcct1);
        //getAccountsDAO().addAccount(dummyAcct2);
    }
}
