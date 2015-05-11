package il.ac.hit.android.smartmeters.database;

import android.provider.BaseColumns;

public class ClientTable {

    public  ClientTable()
    {

    }

    public  static  abstract  class Client_Table implements BaseColumns
    {
        public  static  final String UserId = "UserId";
        public  static  final String Password = "Password";
        public  static  final String ClientName = "ClientName";
        public  static  final String Address = "Address";
        public  static  final String PhoneNumber = "PhoneNumber";
        public  static  final String TableName = "ClientTable";
        public  static  final String DataBaseName = "ElectricCompanyDB";
    }

}
