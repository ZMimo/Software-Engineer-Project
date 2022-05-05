package com.example.ajz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DBHelper extends SQLiteOpenHelper {

    // These string values make referencing tables and columns easier
    private static final String LOGIN_TABLE_NAME = "LoginData";
    private static final String COLUMN_USERNAME = "Username";
    private static final String COLUMN_PASSWORD = "Password";

    private static final String ITEM_TABLE_NAME = "ItemData";
    private static final String COLUMN_BARCODE = "BarecodeID";
    private static final String COLUMN_ITEM_NAME = "ItemName";
    private static final String COLUMN_ITEM_COST = "ItemCost";
    private static final String COLUMN_QUANTITY = "Quantity";

    private static final String TRANSACTION_TABLE_NAME = "TransactionData";
    private static final String COLUMN_DATE_TIME = "DateTime";
    private static final String COLUMN_TRANSACTION_TYPE = "TransactionType";
    private static final String COLUMN_PROFIT = "Profit";


    private static final String CREATE_LOGIN_TABLE = "create Table " + LOGIN_TABLE_NAME +
            "( " + COLUMN_USERNAME + " TEXT primary key, " +
            COLUMN_PASSWORD + " TEXT)";
    private static final String CREATE_ITEM_TABLE = "create Table " + ITEM_TABLE_NAME +
            "( " + COLUMN_BARCODE + " TEXT primary key, " +
            COLUMN_ITEM_NAME + " TEXT, " +
            COLUMN_ITEM_COST + " FLOAT, " +
            COLUMN_QUANTITY + " INT)";
    private static final String CREATE_TRANSACTION_TABLE = "create Table " + TRANSACTION_TABLE_NAME +
            "( " + COLUMN_DATE_TIME + " TEXT primary key, " +
            COLUMN_TRANSACTION_TYPE + " TEXT, " +
            COLUMN_ITEM_NAME + " TEXT, " +
            COLUMN_QUANTITY + " INT, " +
            COLUMN_PROFIT + " FLOAT)";

    public DBHelper(Context context) {
        super(context, "AJZ1.db", null, 5);
    }

    //onCreate, onUpgrade, and onDowngrade are used to implement any changes to the database
    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL(CREATE_LOGIN_TABLE);
        DB.execSQL(CREATE_ITEM_TABLE);
        DB.execSQL(CREATE_TRANSACTION_TABLE);

        setData(DB);
    }
    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists " + LOGIN_TABLE_NAME);
        DB.execSQL("drop Table if exists " + ITEM_TABLE_NAME);
        DB.execSQL("drop Table if exists " + TRANSACTION_TABLE_NAME);

        onCreate(DB);
    }
    @Override
    public void onDowngrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists " + LOGIN_TABLE_NAME);
        DB.execSQL("drop Table if exists " + ITEM_TABLE_NAME);
        DB.execSQL("drop Table if exists " + TRANSACTION_TABLE_NAME);

        onCreate(DB);
    }

    //Hard-codes values to the master table and item table
    public void setData(SQLiteDatabase DB) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_BARCODE, "0000");
        contentValues.put(COLUMN_ITEM_NAME, "Apple");
        contentValues.put(COLUMN_ITEM_COST, 3);
        contentValues.put(COLUMN_QUANTITY, 0);

        DB.insert(ITEM_TABLE_NAME, null, contentValues);
    }

    public boolean insertLoginData(String Username, String Password)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_USERNAME, Username);
        contentValues.put(COLUMN_PASSWORD, Password);

        long result=DB.insert(LOGIN_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // These two functions add and remove from the quantity of an item
    public boolean addItem(String BarcodeID, int Amount)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor cursor = DB.rawQuery("Select * from " + ITEM_TABLE_NAME + " where " + COLUMN_BARCODE + " = ?", new String[]{BarcodeID});
        cursor.moveToFirst();

        if (cursor.getCount() == 0)
            return false;

        int Quantity = cursor.getInt(3); // i:3 is the quantity column
        Quantity += Amount;

        contentValues.put(COLUMN_BARCODE, BarcodeID);
        contentValues.put(COLUMN_QUANTITY, Quantity);

        long result=DB.update(ITEM_TABLE_NAME, contentValues, COLUMN_BARCODE + " = ?", new String[]{BarcodeID});
        cursor.close();
        return result != -1;
    }
    public boolean removeItem(String BarcodeID, int Amount) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor cursor = DB.rawQuery("Select * from " + ITEM_TABLE_NAME + " where " + COLUMN_BARCODE + " = ?", new String[]{BarcodeID});
        cursor.moveToFirst();

        if (cursor.getCount() == 0 || cursor.getInt(3)-Amount < 0)
            return false;

        int Quantity = cursor.getInt(3); // i:3 is the quantity column
        Quantity = Quantity - Amount;
        contentValues.put(COLUMN_BARCODE, BarcodeID);
        contentValues.put(COLUMN_QUANTITY, Quantity);

        long result=DB.update(ITEM_TABLE_NAME, contentValues, COLUMN_BARCODE + " = ?", new String[]{BarcodeID});
        cursor.close();
        return result != -1;
    }

    // These two functions insert and delete instances in the item table
    public boolean insertItemData(String BarcodeID, String ItemName, float ItemCost) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor cursor = DB.rawQuery("Select * from " + ITEM_TABLE_NAME + " where " + COLUMN_BARCODE + " = ?", new String[]{BarcodeID});
        if (cursor.getCount() > 0) {
            return false;
        }

        contentValues.put(COLUMN_BARCODE, BarcodeID);
        contentValues.put(COLUMN_ITEM_NAME, ItemName);
        contentValues.put(COLUMN_ITEM_COST, ItemCost);
        contentValues.put(COLUMN_QUANTITY, 0);

        long result=DB.insert(ITEM_TABLE_NAME, null, contentValues);
        return result != -1;
    }
    public boolean deleteItemData(String BarcodeID) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from " + ITEM_TABLE_NAME + " where " + COLUMN_BARCODE + " = ?", new String[]{BarcodeID});
        if (cursor.getCount() > 0) {
            long result = DB.delete(ITEM_TABLE_NAME,  COLUMN_BARCODE + "=?", new String[]{BarcodeID});
            cursor.close();
            return result != -1;
        } else {
            cursor.close();
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean insertTransactionData(String TransactionType, String BarcodeID, int Amount) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
        String DateTime = LocalDateTime.now().format(format);

        Cursor cursor = DB.rawQuery("Select * from " + ITEM_TABLE_NAME + " where " + COLUMN_BARCODE + " = ?", new String[]{BarcodeID});
        cursor.moveToFirst();
        if (cursor.getCount() == 0)
            return false;

        String ItemName =  cursor.getString(1); // i:1 is the item name column
        float Profit = cursor.getFloat(2)*Amount; // i:2 is the cost column

        contentValues.put(COLUMN_DATE_TIME, DateTime);
        contentValues.put(COLUMN_TRANSACTION_TYPE, TransactionType);
        contentValues.put(COLUMN_ITEM_NAME, ItemName);
        contentValues.put(COLUMN_QUANTITY, Amount);
        contentValues.put(COLUMN_PROFIT, Profit);

        long result=DB.insert(TRANSACTION_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean isValidLogin(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from " + LOGIN_TABLE_NAME + " where " + COLUMN_USERNAME + " = ? and " + COLUMN_PASSWORD + " = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    // These three return cursors to their respective tables
    public Cursor getLoginData() {
        SQLiteDatabase DB = this.getReadableDatabase();
        return DB.rawQuery("Select * from " + LOGIN_TABLE_NAME, null);
    }
    public Cursor getItemData() {
        SQLiteDatabase DB = this.getReadableDatabase();
        return DB.rawQuery("Select * from " + ITEM_TABLE_NAME, null);
    }
    public Cursor getProfitData() {
        SQLiteDatabase DB = this.getReadableDatabase();
        return DB.rawQuery("Select * from " + TRANSACTION_TABLE_NAME + " where " + COLUMN_TRANSACTION_TYPE + " = ?", new String[]{"Checkout"});
    }

    public boolean isBarcodeMatch(String BarcodeID) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("Select * from " + ITEM_TABLE_NAME + " where " + COLUMN_BARCODE + " = ?", new String[]{BarcodeID});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
}