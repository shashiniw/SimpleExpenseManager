package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private DatabaseHelper dbHelper;
    public PersistentAccountDAO(DatabaseHelper dbHelper) {
        this.dbHelper=dbHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumList=new ArrayList<>();
        String queryString="SELECT "+dbHelper.ACCOUNT_NO_COLUMN_NAME+" FROM "+dbHelper.ACCOUNT_TABLE_NAME;
        // Gets the data repository in read mode
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {
            do {
                accountNumList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        else{
            //think and add what to happen
        }
        cursor.close();
        db.close();
        // return list
        return accountNumList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList=new ArrayList<>();
        String queryString="SELECT * FROM "+dbHelper.ACCOUNT_TABLE_NAME;
        // Gets the data repository in read mode
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {
            do {
                String accountNum=cursor.getString(0);
                String bankName=cursor.getString(1);
                String AccountHolderName=cursor.getString(2);
                Double balance=Double.parseDouble(cursor.getString(3));
                Account account=new Account(accountNum,bankName,AccountHolderName,balance);
                accountList.add(account);
            } while (cursor.moveToNext());
        }
        else{
            //think and add what to happen
        }
        cursor.close();
        db.close();
        // return list
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        // Gets the data repository in read mode
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + dbHelper.ACCOUNT_TABLE_NAME + " WHERE " + dbHelper.ACCOUNT_NO_COLUMN_NAME +" = " + accountNo, null);

        if (cursor != null) {
            cursor.moveToFirst();
            String accountNum=cursor.getString(0);
            String bankName=cursor.getString(1);
            String AccountHolderName=cursor.getString(2);
            Double balance=Double.parseDouble(cursor.getString(3));
            Account account=new Account(accountNum,bankName,AccountHolderName,balance);
            cursor.close();
            return account;
        }
        db.close();
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);

    }

    @Override
    public void addAccount(Account account) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(dbHelper.ACCOUNT_NO_COLUMN_NAME, account.getAccountNo());
        values.put(dbHelper.BANK_NAME_COLUMN_NAME, account.getBankName());
        values.put(dbHelper.ACCOUNT_HOLDER_NAME_COLUMN_NAME, account.getAccountHolderName());
        values.put(dbHelper.BALANCE_COLUMN_NAME, account.getBalance());
        // Insert the new row, returning the primary key value of the new row
        long insert = db.insert(dbHelper.ACCOUNT_TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(dbHelper.ACCOUNT_TABLE_NAME, dbHelper.ACCOUNT_NO_COLUMN_NAME + "=?", new String[]{accountNo});
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //get the account by calling the method we implemented in this class
        Account account = this.getAccount(accountNo);

        ContentValues values = new ContentValues();
        switch (expenseType) {
            case EXPENSE:
                values.put(dbHelper.BALANCE_COLUMN_NAME, account.getBalance() - amount);
                break;
            case INCOME:
                values.put(dbHelper.BALANCE_COLUMN_NAME, account.getBalance() + amount);
                break;
        }
        // Update row in account table in database
        db.update(dbHelper.ACCOUNT_TABLE_NAME, values, dbHelper.ACCOUNT_NO_COLUMN_NAME + " = ?",
                new String[] { accountNo });

    }
}
