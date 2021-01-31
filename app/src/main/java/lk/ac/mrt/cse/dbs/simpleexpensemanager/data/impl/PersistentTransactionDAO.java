package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    DatabaseHelper dbHelper;
    public PersistentTransactionDAO(DatabaseHelper dbHelper) {
        this.dbHelper=dbHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String dateFormated=new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).format(date);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(dbHelper.DATE_COLUMN_NAME,dateFormated);
        values.put(dbHelper.ACCOUNT_NO_COLUMN_NAME, accountNo);
        values.put(dbHelper.EXPENSE_TYPE_COLUMN_NAME,expenseType.name());
        values.put(dbHelper.AMOUNT_COLUMN_NAME, amount);

        db.insert(dbHelper.TRANSACTIONS_TABLE_NAME,null,values);
        db.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionsList=new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String queryString="SELECT * FROM "+dbHelper.TRANSACTIONS_TABLE_NAME;
        Cursor cursor=db.rawQuery(queryString,null);
        if (cursor.moveToFirst()) {
            Date formattedDate;
            do {
                try {
                    String dbDate=cursor.getString(2);
                    String accountNum=cursor.getString(1);
                    ExpenseType expenseType=ExpenseType.valueOf(cursor.getString(3));
                    Double amount=Double.parseDouble(cursor.getString(4));
                    //Converted to Date type
                    formattedDate = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).parse(dbDate);

                    Transaction transaction = new Transaction(formattedDate,accountNum,expenseType,amount);

                    transactionsList.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return transactionsList;

    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionsList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ dbHelper.TRANSACTIONS_TABLE_NAME+" ORDER BY "+dbHelper.DATE_COLUMN_NAME+" DESC LIMIT ?", new String[]{String.valueOf(limit)});

        if (cursor.moveToFirst()) {
            do {
                try {
                    String dbDate=cursor.getString(2);
                    String accountNum=cursor.getString(1);
                    ExpenseType expenseType=ExpenseType.valueOf(cursor.getString(3));
                    Double amount=Double.parseDouble(cursor.getString(4));
                    Date formatteddate = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).parse(dbDate);
                    Transaction transaction = new Transaction(formatteddate, accountNum, expenseType, amount);
                    transactionsList.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return list
        return transactionsList;
    }
}
