package il.ac.hit.android.smartmeters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import il.ac.hit.android.smartmeters.utils.UtilsDataBase;


public class ForgotPasswordActivity extends ActionBarActivity implements View.OnClickListener
{
    private AlertDialog _alertDialog;
    private String _userGetPassword = null;
    private EditText _editTextUserName;
    private AlertDialog.Builder _builder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Button buttonGetPassword = (Button) findViewById(R.id.buttonGetPassword);

        buttonGetPassword.setOnClickListener(this);

        _editTextUserName = (EditText) findViewById(R.id.editTextUserNameForGetPass);
        _editTextUserName.setError(null);

        _builder = new AlertDialog.Builder(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.buttonGetPassword:
            {
                if (!TextUtils.isEmpty(_editTextUserName.getText().toString()))
                {
                    String userName = _editTextUserName.getText().toString();
                    _userGetPassword = UtilsDataBase.getPasswordIfUserNameExits(userName);

                    if (_userGetPassword != null)
                    {
                        Log.i("forgot_password", "The user: " + _editTextUserName.getText().toString() + " exits with the password: " + _userGetPassword);
                        setDialogAlertPasswordAndShow();
                    }
                    else
                    {
                        Log.i("forgot_password", "The user: " + _editTextUserName.getText().toString() + " not exits");
                        setEditTextErrorAndFocus(_editTextUserName, getString(R.string.edit_text_error_user_name_not_match));
                    }
                }
                else
                {
                    setEditTextErrorAndFocus(_editTextUserName, getString(R.string.edit_text_error_user_name_required));
                }
            }
            break;
        }

    }

    private void setDialogAlertPasswordAndShow()
    {
        Log.i("forgot_password", "Setting the dialog with the password: " + _userGetPassword);
        String userName = _editTextUserName.getText().toString();
        _builder.setTitle("Hello " + userName).setMessage("Your Password is -  " + _userGetPassword).setCancelable(false).setNegativeButton("Close", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });
        _alertDialog = _builder.create();
        _alertDialog.show();
    }

    private void setEditTextErrorAndFocus(EditText editText, String error)
    {
        editText.setError(error);
        editText.requestFocus();
    }
}
