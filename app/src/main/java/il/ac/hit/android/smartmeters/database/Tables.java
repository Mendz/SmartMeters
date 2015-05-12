package il.ac.hit.android.smartmeters.database;

import android.provider.BaseColumns;

public class Tables {
    public  static  final String DataBaseName = "ElectricCompanyDB";

    public  Tables()
    {

    }

    public  static  abstract  class ClientTable implements BaseColumns
    {
        public  static  final String UserId = "UserId";
        public  static  final String Password = "Password";
        public  static  final String ClientName = "ClientName";
        public  static  final String Address = "Address";
        public  static  final String PhoneNumber = "PhoneNumber";
        public  static  final String TableName = "ClientTable";
    }

    public  static  abstract  class MeterTable implements BaseColumns
    {
        public  static  final String UserId = "UserId";
        public  static  final String MeterID = "MeterID";
        public  static  final String Address = "Address";
        public  static  final String TableName = "MeterTable";
    }

    public  static  abstract  class ReadingTable implements BaseColumns
    {
        public  static  final String ReadingID = "ReadingID";
        public  static  final String MeterID = "MeterID";
        public  static  final String Date = "Date";
        public  static  final String Time = "Time";
        public  static  final String TableName = "ReadingTable";
    }

}
