package il.ac.hit.android.smartmeters.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOperations extends SQLiteOpenHelper
{
    public static final int database_version = 1;
    public String CreateQuery = "CREATE TABLE " + ClientTable.Client_Table.TableName + "(" + ClientTable.Client_Table.UserId + " TEXT," + ClientTable.Client_Table.ClientName + " TEXT," + ClientTable.Client_Table.Password + " TEXT," + ClientTable.Client_Table.Address + " TEXT," + ClientTable.Client_Table.PhoneNumber + " TEXT);";

    public DatabaseOperations(Context context)
    {
        super(context, ClientTable.Client_Table.DataBaseName, null, database_version);
        Log.d("Dtabase operations", "Database created");
    }

    public void onCreate(SQLiteDatabase sdb)
    {
        sdb.execSQL(CreateQuery);
        Log.d("Dtabase operations", "Table created");
    }

    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
    {

    }
}
