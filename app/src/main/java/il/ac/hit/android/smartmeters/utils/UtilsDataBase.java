package il.ac.hit.android.smartmeters.utils;

import android.util.Log;

//TODO: probably delete all of this for a class that connect to the database.
public class UtilsDataBase
{
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    public static final String[] DUMMY_CREDENTIALS = new String[]{"tom:123", "mendz:456", "inga:789"};

    public static boolean checkIfUserNameExists(String userName)
    {
        return getPasswordIfUserNameExits(userName) != null;
    }

    public static String getPasswordIfUserNameExits(String userName)
    {
        //TODO: implements here checking with the real DataBase.
        for (String credential : DUMMY_CREDENTIALS)
        {
            String[] pieces = credential.split(":");
            if (pieces[0].equalsIgnoreCase(userName))
            {
                Log.i("forgot_password", "Check the user name from db: " + pieces[0] + "with user input" + userName);
                // Account exists, return true and get the password.
                return pieces[1];
            }
        }

        return null;
    }

    public static boolean isUserNameAndPasswordAreCorrect(String userName, String password)
    {
        for (String credential : UtilsDataBase.DUMMY_CREDENTIALS)
        {
            String[] pieces = credential.split(":");
            if (pieces[0].equalsIgnoreCase(userName))
            {
                // Account exists, return true if the password matches.
                return pieces[1].equals(password);
            }
        }

        return false;
    }
}
