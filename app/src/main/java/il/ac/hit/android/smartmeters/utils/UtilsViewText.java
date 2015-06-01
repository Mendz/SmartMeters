package il.ac.hit.android.smartmeters.utils;


import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import il.ac.hit.android.smartmeters.database.DatabaseOperations;

public class UtilsViewText
{
    public static void setEditTextErrorAndFocus(EditText editText, String error)
    {
        editText.setError(error);
        editText.requestFocus();
    }

    public static void setTextViewTitleHello(TextView textViewTitleHello, String id,Context context)
    {
        DatabaseOperations databaseOperations = new DatabaseOperations(context);

        String title = textViewTitleHello.getText().toString();
        String name = databaseOperations.getNameById(id);

        textViewTitleHello.setText(title + " " + name + ".");
    }
}
