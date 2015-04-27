package il.ac.hit.android.smartmeters;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class RegisterNewUserActivity extends ActionBarActivity implements View.OnClickListener
{
    private EditText _editTextPassword;
    private EditText _editTextRePassword;
    private Button _buttonSave;
    String _password;
    String _rePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);

        _editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        _editTextRePassword = (EditText) findViewById(R.id.editTextRePassword);
        _editTextPassword.setError(null);
        _editTextRePassword.setError(null);


        _buttonSave = (Button) findViewById(R.id.buttonGetPassword);
        _buttonSave.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_new_user, menu);
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

    private boolean isCheckPasswordsSame()
    {
        setPasswordsMembers();

        boolean checkSame = true;

        if (!_password.equals(_rePassword))
        {
            checkSame = false;
        }

        return checkSame;
    }

    private void setPasswordsMembers()
    {
        _password = _editTextPassword.getText().toString();
        _rePassword = _editTextRePassword.getText().toString();
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.buttonGetPassword:
            {
                setPasswordsMembers();
                View focusView = _editTextPassword;

                if (!isCheckPasswordsSame())
                {
                    _editTextRePassword.setError(getString(R.string.error_password_not_identical));
                    focusView = _editTextRePassword;
                }

                if (LoginActivity.isStringNullOrWhiteSpace(_password))
                {
                    _editTextPassword.setError(getString(R.string.error_password_empty));
                    focusView = _editTextPassword;
                }

                focusView.requestFocus();
            }

            break;
        }
    }
}
