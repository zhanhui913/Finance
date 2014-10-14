package com.zhan.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zhan.models.Category;
import com.zhan.models.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Zhan on 2014-10-12.
 */
public class Database extends SQLiteOpenHelper{
    //Logcat ag
    private static final String LOG = "DATABASE";

    //Database name
    private static final String DATABASE_NAME = "FinanceDatabase";

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Column names for CATEGORY Table
    private static final String TABLE_CATEGORY = "category";
    private static final String CATEGORY_ID = "id";
    private static final String CATEGORY_TITLE = "title";

    //Column names for ITEM Table
    private static final String TABLE_ITEM = "item";
    private static final String ITEM_ID = "id";
    private static final String ITEM_CATEGORY_ID = "category_id";
    private static final String ITEM_TITLE = "title";
    private static final String ITEM_TYPE = "type";
    private static final String ITEM_DATE = "date";
    private static final String ITEM_PRICE = "price";

    //Database creation sql statement for CATEGORY Table
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + " (" +
            CATEGORY_ID + " INTEGER PRIMARY KEY," +
            CATEGORY_TITLE + " TEXT);";

    //Database creation sql statement for ITEM Table
    private static final String CREATE_TABLE_ITEM = "CREATE TABLE " + TABLE_ITEM + " (" +
            ITEM_ID + " INTEGER PRIMARY KEY," +
            ITEM_CATEGORY_ID + " INTEGER," +
            ITEM_TITLE + " TEXT," +
            ITEM_TYPE + " TEXT," +
            ITEM_DATE + " TEXT," +
            ITEM_PRICE + " REAL,"+
            "FOREIGN KEY (" + ITEM_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + CATEGORY_ID + "));";

    public Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //Create required tables
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w(LOG,
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        //On upgrade drop older tables (drop table with most dependencies first)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);

        //Create new tables
        onCreate(db);
    }

    //----------------------------------------------------------------------------------------------
    //
    // CRUD Operations for CATEGORY Table
    //
    //----------------------------------------------------------------------------------------------

    /**
     * Insert a Category into the database
     * @param category The new Category to be inserted
     */
    public void createCategory(Category category){
        //Get references to writable db
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(CATEGORY_ID, category.getId()); //Testing if auto increment works
        values.put(CATEGORY_TITLE, category.getTitle());

        db.insert(TABLE_CATEGORY,null,values);

        db.close();
        Log.d(TABLE_CATEGORY, "create Category " + category.toString());
    }

    /**
     * Get the Category with the corresponding id from the database
     * @param id The id of the Category
     * @return Category
     */
    public Category getCategory(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] TOUR_COLUMNS = {CATEGORY_ID,CATEGORY_TITLE};

        Cursor cursor = db.query(TABLE_CATEGORY,     //Table
                TOUR_COLUMNS,                        //Column names
                " id = ?",                           // Selections
                new String[]{String.valueOf(id)},    // selections argument
                null,                                // group by
                null,                                // having
                null,                                // order by
                null);                               // limit

        Category category = new Category();
        if(!(cursor.moveToFirst()) || (cursor.getCount()==0)){
            Log.d(TABLE_CATEGORY,"GetCategory returns empty for id = "+id);
        }else{
            Log.d(TABLE_CATEGORY,"GetCategory returns something for id = "+id);

            category.setId(cursor.getInt(0));
            category.setTitle(cursor.getString(1));
        }

        cursor.close();
        db.close();
        Log.d(TABLE_CATEGORY,"get Category "+category.toString());
        return category;
    }

    /**
     * Get all Categories from the database
     * @return ArrayList<Category>
     */
    public ArrayList<Category> getAllCategory(){
        ArrayList<Category> categories = new ArrayList<Category>();

        String query = "SELECT * FROM " + TABLE_CATEGORY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        Category category = null;
        if(cursor.moveToFirst()){
            do {
                category = new Category();
                category.setId(cursor.getInt(0));
                category.setTitle(cursor.getString(1));

                //Add category to arraylist
                categories.add(category);
                Log.d(TABLE_CATEGORY, "adding 1 category");
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Log.d(TABLE_CATEGORY,"get all categories "+category.toString());
        return categories;
    }


    /**
     * Update the Category with the corresponding new Category into the database
     * @param category The new Category
     * @return The number of rows affected
     */
    public int updateCategory(Category category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORY_ID,category.getId());
        values.put(CATEGORY_TITLE,category.getTitle());


        int i = db.update(TABLE_CATEGORY,   // Table
                values,                 // Column/value
                CATEGORY_ID + " = ?",         // Selections
                new String[] {String.valueOf(category.getId())}); //Select i

        db.close();
        Log.d(TABLE_CATEGORY,"updating category "+category.toString());
        return i;
    }

    /**
     * Delete the corresponding Category from the database
     * @param category The Category to be deleted
     */
    public void deleteCategory(Category category){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CATEGORY,
                CATEGORY_ID + " = ?",
                new String[]{String.valueOf(category.getId())});

        db.close();

        Log.d(TABLE_CATEGORY, "deleting category " + category.toString());
    }


    //----------------------------------------------------------------------------------------------
    //
    // CRUD Operations for ITEM Table
    //
    //----------------------------------------------------------------------------------------------

    /**
     * Insert an Item into the database
     * @param item The new Item to be inserted
     */
    public void createItem(Item item){
        //Get references to writable db
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(ITEM_ID, item.getId()); //Testing if auto increment works
        values.put(ITEM_CATEGORY_ID, item.getCategory().getId());
        values.put(ITEM_TITLE, item.getTitle());
        values.put(ITEM_TYPE, item.getType());
        values.put(ITEM_DATE, item.getDate().toString());
        values.put(ITEM_PRICE, item.getPrice());

        db.insert(TABLE_ITEM,null,values);

        db.close();
        Log.d(TABLE_ITEM, "create Item " + item.toString());
    }

    /**
     * Get the Item with the corresponding id from the database
     * @param id The id of the Item
     * @return Item
     */
    public Item getItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] TOUR_COLUMNS = {ITEM_ID,ITEM_CATEGORY_ID,ITEM_TITLE,ITEM_TYPE,ITEM_DATE,ITEM_PRICE};

        Cursor cursor = db.query(TABLE_ITEM,     //Table
                TOUR_COLUMNS,                        //Column names
                " id = ?",                           // Selections
                new String[]{String.valueOf(id)},    // selections argument
                null,                                // group by
                null,                                // having
                null,                                // order by
                null);                               // limit

        Item item = new Item();
        if(!(cursor.moveToFirst()) || (cursor.getCount()==0)){
            Log.d(TABLE_ITEM,"GetItem returns empty for id = "+id);
        }else{
            Log.d(TABLE_ITEM,"GetItem returns something for id = "+id);

            item.setId(cursor.getInt(0));
            item.setCategory(new Category(
                    cursor.getInt(1),
                    getCategory(cursor.getInt(1)).getTitle()
            ));
            item.setTitle(cursor.getString(2));
            item.setType(cursor.getString(3));
            item.setDate(convertStringToDate(cursor.getString(4)));
            item.setPrice(cursor.getDouble(5));
        }

        cursor.close();
        db.close();
        Log.d(TABLE_ITEM,"get Item "+item.toString());
        return item;
    }

    /**
     * Get all Items from the database
     * @return ArrayList<Item>
     */
    public ArrayList<Item> getAllItem(){
        ArrayList<Item> items = new ArrayList<Item>();

        String query = "SELECT * FROM " + TABLE_ITEM;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        Item item = null;
        if(cursor.moveToFirst()){
            do {
                item = new Item();
                item.setId(cursor.getInt(0));
                item.setCategory(new Category(
                        cursor.getInt(1),
                        getCategory(cursor.getInt(1)).getTitle()
                ));
                item.setTitle(cursor.getString(2));
                item.setType(cursor.getString(3));
                item.setDate(convertStringToDate(cursor.getString(4)));
                item.setPrice(cursor.getDouble(5));

                //Add Item to arraylist
                items.add(item);
                Log.d(TABLE_ITEM, "adding 1 item");
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Log.d(TABLE_ITEM,"get all items "+items.toString());
        return items;
    }


    /**
     * Update the Item with the corresponding new Item into the database
     * @param item The new Item
     * @return The number of rows affected
     */
    public int updateItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM_ID,item.getId());
        values.put(ITEM_CATEGORY_ID, item.getCategory().getId());
        values.put(ITEM_TITLE, item.getTitle());
        values.put(ITEM_TYPE, item.getType());
        values.put(ITEM_DATE, convertDateToString(item.getDate()));
        values.put(ITEM_PRICE, item.getPrice());

        int i = db.update(TABLE_ITEM,   // Table
                values,                 // Column/value
                ITEM_ID + " = ?",         // Selections
                new String[] {String.valueOf(item.getId())}); //Select i

        db.close();
        Log.d(TABLE_ITEM, "updating item " + item.toString());
        return i;
    }

    /**
     * Delete the corresponding Item from the database
     * @param item The Item to be deleted
     */
    public void deleteItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ITEM,
                ITEM_ID + " = ?",
                new String[]{String.valueOf(item.getId())});

        db.close();

        Log.d(TABLE_ITEM,"deleting item "+item.toString());
    }


    public Date convertStringToDate(String stringDate){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyy");
        Date date = null;

        try{
            date = formatter.parse(stringDate);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return date;
    }

    public String convertDateToString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String stringDate = formatter.format(date);

        return stringDate;
    }

}
