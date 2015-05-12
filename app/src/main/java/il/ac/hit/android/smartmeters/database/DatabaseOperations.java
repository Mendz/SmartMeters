package il.ac.hit.android.smartmeters.database;

import android.content.ContentValues;
import android.content.Context;
import  android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOperations extends SQLiteOpenHelper {
    public  static  final int database_version = 1;
    public String CreateQueryClientTable = "CREATE TABLE " + Tables.ClientTable.TableName + "(" + Tables.ClientTable.UserId + " TEXT," + Tables.ClientTable.ClientName + " TEXT," + Tables.ClientTable.Password + " TEXT," + Tables.ClientTable.Address + " TEXT," + Tables.ClientTable.PhoneNumber + " TEXT);";
    public String CreateQueryMeterTable = "CREATE TABLE " + Tables.MeterTable.TableName + "(" + Tables.ClientTable.UserId + " TEXT," + Tables.ClientTable.ClientName + " TEXT," + Tables.ClientTable.Password + " TEXT," + Tables.ClientTable.Address + " TEXT," + Tables.ClientTable.PhoneNumber + " TEXT);";
    public String CreateQueryReadingTable = "CREATE TABLE " + Tables.ReadingTable.TableName + "(" + Tables.ClientTable.UserId + " TEXT," + Tables.ClientTable.ClientName + " TEXT," + Tables.ClientTable.Password + " TEXT," + Tables.ClientTable.Address + " TEXT," + Tables.ClientTable.PhoneNumber + " TEXT);";

    public DatabaseOperations (Context context)
    {
        super(context, Tables.DataBaseName,null,database_version);
        Log.d("Dtabase operations", "Database created");
    }
    public void onCreate(SQLiteDatabase sdb)
    {
        sdb.execSQL(CreateQueryClientTable);
        Log.d("Dtabase operations", "Client table created");
        sdb.execSQL(CreateQueryMeterTable);
        Log.d("Dtabase operations", "Meter table created");
        sdb.execSQL(CreateQueryReadingTable);
        Log.d("Dtabase operations", "Reaging table created");
    }

    public  void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
    {

    }

    public void setClient(DatabaseOperations dop, String userId, String password, String clientName, String address, String phoneNumber)
    {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tables.ClientTable.UserId, userId);
        cv.put(Tables.ClientTable.Password, password);
        cv.put(Tables.ClientTable.ClientName, clientName);
        cv.put(Tables.ClientTable.Address, address);
        cv.put(Tables.ClientTable.PhoneNumber, phoneNumber);
        long k = SQ.insert(Tables.ClientTable.TableName, null, cv);
        Log.d("Dtabase operations", "One raw inserted to Client table");
    }
}
