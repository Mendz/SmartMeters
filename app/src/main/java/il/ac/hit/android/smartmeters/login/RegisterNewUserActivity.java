package il.ac.hit.android.smartmeters.login;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import il.ac.hit.android.smartmeters.R;
import il.ac.hit.android.smartmeters.database.DatabaseOperations;
import il.ac.hit.android.smartmeters.database.Tables;
import il.ac.hit.android.smartmeters.utils.UtilsDataBase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class RegisterNewUserActivity extends ActionBarActivity implements View.OnClickListener, TextWatcher
{
    private EditText _editTextUserName;
    private EditText _editTextAddress;
    private EditText _editTextPassword;
    private EditText _editTextRePassword;
    private EditText _editTextPhoneNumber;
    private Button _buttonSave;
    private String _password;
    private String _rePassword;
    List<EditText> editTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);

        settingAllEditText();

        _buttonSave = (Button) findViewById(R.id.buttonGetPassword);
        _buttonSave.setOnClickListener(this);
    }

    private void settingAllEditText()
    {
        _editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        _editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        _editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        _editTextRePassword = (EditText) findViewById(R.id.editTextRePassword);
        _editTextPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);

        _editTextUserName.setError(null);
        _editTextAddress.setError(null);
        _editTextPassword.setError(null);
        _editTextRePassword.setError(null);

        _editTextUserName.addTextChangedListener(this);
        _editTextAddress.addTextChangedListener(this);
        _editTextPassword.addTextChangedListener(this);
        _editTextRePassword.addTextChangedListener(this);


        setListOfEditsText();
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_register_new_user, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings)
//        {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
                DatabaseOperations databaseOperations = new DatabaseOperations(this);
                String userName = _editTextUserName.getText().toString();
                boolean cancel = false;

                setPasswordsMembers();
                View focusView = _editTextPassword;

                if (databaseOperations.checkIfUserNameExists(userName))
                {
                    _editTextUserName.setError(getString(R.string.error_user_name_already_exists));
                    focusView = _editTextUserName;
                    cancel = true;
                }

                if (!isCheckPasswordsSame())
                {
                    _editTextRePassword.setError(getString(R.string.error_password_not_identical));
                    focusView = _editTextRePassword;
                    cancel = true;
                }

                if (!isPasswordValid())
                {
                    _editTextPassword.setError(getString(R.string.error_password_short));
                    focusView = _editTextPassword;
                    cancel = true;
                }

                if (cancel)
                {
                    focusView.requestFocus();
                }
                else
                {
                    //TODO: changed the client id to hash code

                    String clientName = _editTextUserName.getText().toString();
                    String address = _editTextAddress.getText().toString();
                    String phoneNumber = _editTextPhoneNumber.getText().toString();
//                    String clientId = String.valueOf(UUID.randomUUID());
                    String clientId = String.valueOf(clientName.hashCode());
                    clientId = TextUtils.substring(clientId,0,7);


                    databaseOperations.setClient(databaseOperations, clientId, _password, clientName, address, phoneNumber);
                    databaseOperations.setUserType(databaseOperations, clientId, Tables.UserTypeTable.UserTypes.CLIENT);


                    Toast.makeText(getApplicationContext(), "The user : \"" + userName + "\" is added.", Toast.LENGTH_LONG)
                            .show();

                    finish();
                }
            }

            break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        //nothing...
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        _buttonSave.setEnabled(isAllTextBoxFull());
    }

    @Override
    public void afterTextChanged(Editable s)
    {
        //nothing...
    }

    private void setListOfEditsText()
    {
        editTexts = new ArrayList<>();
        editTexts.add(_editTextPassword);
        editTexts.add(_editTextRePassword);
        editTexts.add(_editTextUserName);
        editTexts.add(_editTextAddress);
    }

    private boolean isAllTextBoxFull()
    {
        for (EditText editText : editTexts)
        {
            if (TextUtils.isEmpty(editText.getText().toString()))
            {
                return false;
            }
        }

        return true;
    }

    private boolean isPasswordValid()
    {
        return _password.length() >= getResources().getInteger(R.integer.min_password_length);
    }
}
