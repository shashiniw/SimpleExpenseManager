package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    //database parameters
    public static final String DB_NAME = "180718H.db";
    public static final int DB_VERSION = 1;


    //ACCOUNT table column name defining

    public static final String ACCOUNT_TABLE_NAME = "Account";
    public static final String ACCOUNT_NO_COLUMN_NAME = "accountNo";
    public static final String BANK_NAME_COLUMN_NAME = "bankName";
    public static final String ACCOUNT_HOLDER_NAME_COLUMN_NAME = "accountHolderName";
    public static final String BALANCE_COLUMN_NAME = "balance";

    // TRANSACTIONS table column name defining
    public static final String TRANSACTIONS_TABLE_NAME = "Transactions";
    public static final String TRANSACTION_ID_COLUMN_NAME = "transactionID";
    public static final String DATE_COLUMN_NAME = "date";
    public static final String EXPENSE_TYPE_COLUMN_NAME = "expenseType";
    public static final String AMOUNT_COLUMN_NAME = "amount";
    //ACCOUNT_NO_COLUMN_NAME has been defined above


    //create ACCOUNT table sql query
    private static final String createAccountTableQuery= "create table if not exists " + ACCOUNT_TABLE_NAME + "("
            + ACCOUNT_NO_COLUMN_NAME + " TEXT PRIMARY KEY,"
            + BANK_NAME_COLUMN_NAME + " TEXT,"
            + ACCOUNT_HOLDER_NAME_COLUMN_NAME + " TEXT,"
            + BALANCE_COLUMN_NAME + " REAL NOT NULL"
            +"  )";


    private static final String createTransactionTableQuery= "create table if not exists " + TRANSACTIONS_TABLE_NAME + "("
            + TRANSACTION_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ACCOUNT_NO_COLUMN_NAME+ " TEXT NOT NULL,"
            + DATE_COLUMN_NAME + " TEXT,"
            + EXPENSE_TYPE_COLUMN_NAME + " TEXT NOT NULL,"
            + AMOUNT_COLUMN_NAME + " REAL NOT NULL,"
            +"  FOREIGN KEY(" + ACCOUNT_NO_COLUMN_NAME + ") REFERENCES Account(" + ACCOUNT_NO_COLUMN_NAME + ")"
            +" )";


    /*
    create table if not exists Account(
            accountNo TEXT PRIMARY KEY,
            bankName TEXT,
            accountHolderName TEXT,
            balance REAL NOT NULL
    );


    create table if not exists Transactions(
            transactionID INTEGER PRIMARY KEY AUTOINCREMENT,
            accountNo TEXT,
            date TEXT,
            expenseType TEXT NOT NULL,
            amount REAL NOT NULL,
            FOREIGN KEY(accountNo) REFERENCES Account(accountNo)
            )
    */

    //constructor
    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //overriding methods inherited by super class
    @Override
    public void onCreate(SQLiteDatabase db) {
        //executing create table queries
        db.execSQL(createAccountTableQuery);
        db.execSQL(createTransactionTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTIONS_TABLE_NAME);
        onCreate(db);
    }
}

