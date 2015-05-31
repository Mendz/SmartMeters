package il.ac.hit.android.smartmeters;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import il.ac.hit.android.smartmeters.client.ClientActivity;
import il.ac.hit.android.smartmeters.database.DatabaseOperations;
import il.ac.hit.android.smartmeters.database.Tables;
import il.ac.hit.android.smartmeters.login.LoginActivity;
import il.ac.hit.android.smartmeters.support.SupportActivity;
import il.ac.hit.android.smartmeters.utils.UtilsSignOut;


public class AdminActivity extends ActionBarActivity implements View.OnClickListener
{
    private EditText _editTextClientName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        setTheViews();

    }

    private void setTheViews()
    {
        Button _accessClient;
        Button _accessSupport;

        _editTextClientName = (EditText) findViewById(R.id.editTextAccessClient);
        _editTextClientName.setOnClickListener(this);

        _accessClient = (Button) findViewById(R.id.buttonAccessClient);
        _accessClient.setOnClickListener(this);

        _accessSupport = (Button) findViewById(R.id.buttonAccessSupport);
        _accessSupport.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_log_off)
        {
            UtilsSignOut.logOff(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = null;

        switch (v.getId())
        {
            case R.id.buttonAccessClient:
            {
                if (!TextUtils.isEmpty(_editTextClientName.getText().toString()))
                {
                    String clientName = _editTextClientName.getText().toString();

                    DatabaseOperations databaseOperations = new DatabaseOperations(this);

                    if (databaseOperations.checkIfUserNameExists(clientName))
                    {
                        String id = databaseOperations.getIdByName(clientName);
                        String clientType = databaseOperations.getUserTypeById(id);

                        if (clientType.equalsIgnoreCase(Tables.UserTypeTable.UserTypes.CLIENT))
                        {
                            intent = new Intent(this, ClientActivity.class);
                            intent.putExtra(Tables.ClientTable.UserId, id);
                            intent.setAction(LoginActivity.LOGIN_ACTION);

                            Log.d("admin", "Put extra id: " + id);
                            startActivity(intent);

                            Toast.makeText(this, "You connect as " + clientName, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            setEditTextErrorAndFocus(_editTextClientName, "The user: " + clientName + " is not a client!");
                        }
                    }
                    else
                    {
                        setEditTextErrorAndFocus(_editTextClientName, getString(R.string.edit_text_error_user_name_not_match));
                    }
                }
                else
                {
                    setEditTextErrorAndFocus(_editTextClientName, getString(R.string.edit_text_error_user_name_required));
                }
            }
            break;
            case R.id.buttonAccessSupport:
            {
                intent = new Intent(this, SupportActivity.class);

                startActivity(intent);
            }
            break;
        }
    }

    private void setEditTextErrorAndFocus(EditText editText, String error)
    {
        editText.setError(error);
        editText.requestFocus();
    }
}
