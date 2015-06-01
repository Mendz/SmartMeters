package il.ac.hit.android.smartmeters.utils;


import android.widget.EditText;

public class UtilsEditTextError
{
    public static void setEditTextErrorAndFocus(EditText editText, String error)
    {
        editText.setError(error);
        editText.requestFocus();
    }
}
