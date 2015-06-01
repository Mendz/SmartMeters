package il.ac.hit.android.smartmeters.client;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import com.google.android.gms.internal.in;
import il.ac.hit.android.smartmeters.R;
import il.ac.hit.android.smartmeters.database.DatabaseOperations;
import il.ac.hit.android.smartmeters.database.Tables;
import il.ac.hit.android.smartmeters.login.LoginActivity;
import il.ac.hit.android.smartmeters.utils.UtilsViewText;
import il.ac.hit.android.smartmeters.utils.UtilsSignOut;

import java.util.Random;


public class ClientActivity extends ActionBarActivity implements View.OnClickListener
{
    private String _id;
    private EditText _editTextAddMeter;
    private TextView _textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        setViews();

        Intent intent = this.getIntent();

        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase(LoginActivity.LOGIN_ACTION))
        {
            _id = intent.getStringExtra(Tables.ClientTable.UserId);
        }

        UtilsViewText.setTextViewTitleHello(_textViewTitle, _id, this);

    }

    private void setViews()
    {
        Button buttonMetersMap = (Button) findViewById(R.id.buttonClientContersMap);
        buttonMetersMap.setOnClickListener(this);

        Button buttonDetails = (Button) findViewById(R.id.buttonClientDetails);
        buttonDetails.setOnClickListener(this);

        Button buttonAddMeter = (Button) findViewById(R.id.buttonAddMeter);
        buttonAddMeter.setOnClickListener(this);

        _editTextAddMeter = (EditText) findViewById(R.id.editTextAddMeterAddress);

        _textViewTitle = (TextView) findViewById(R.id.textViewClientScreen);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client, menu);
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
            case R.id.buttonClientContersMap:
            {
                intent = new Intent(this, ClientMap.class);
                intent.putExtra(Tables.ClientTable.UserId, _id);
            }
            break;
            case R.id.buttonClientDetails:
            {
                intent = new Intent(this, ClientDetailsActivity.class);
                intent.putExtra(Tables.ClientTable.UserId, _id);
            }
            break;
            case R.id.buttonAddMeter:
            {
                if (!TextUtils.isEmpty(_editTextAddMeter.getText().toString()))
                {
                    String address = _editTextAddMeter.getText().toString();

                    DatabaseOperations databaseOperations = new DatabaseOperations(this);
                    Random randomGenerator = new Random();
                    int kwh = randomGenerator.nextInt(1500);

                    int meterId = address.hashCode();
                    if (meterId < 0)
                    {
                        meterId *= -1;
                    }

                    databaseOperations.setMeter(databaseOperations, String.valueOf(meterId)
                            .substring(0, 7), _id, address, String.valueOf(kwh));

                    Toast.makeText(this, "Added meter in the address: " + address, Toast.LENGTH_SHORT).show();

                    _editTextAddMeter.setText("");

                }
                else
                {
                    UtilsViewText.setEditTextErrorAndFocus(_editTextAddMeter, "You have to enter the address!");
                }
            }
            break;
        }

        if (intent != null)
        {
            startActivity(intent);
        }
    }
}
