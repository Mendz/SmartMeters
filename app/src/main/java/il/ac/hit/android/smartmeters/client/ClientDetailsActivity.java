package il.ac.hit.android.smartmeters.client;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import il.ac.hit.android.smartmeters.R;
import il.ac.hit.android.smartmeters.database.DatabaseOperations;
import il.ac.hit.android.smartmeters.database.Tables;
import il.ac.hit.android.smartmeters.utils.UtilsSignOut;

public class ClientDetailsActivity extends ActionBarActivity
{
    private String _id;

    private TextView _textViewId;
    private TextView _textViewName;
    private TextView _textViewAddress;
    private TextView _textViewPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        _id = getIntent().getStringExtra(Tables.ClientTable.UserId);

        setContentView(R.layout.activity_client_details);
        setAllTheTextViews();

        fillAllTheClientTextViews();



    }

    private void fillAllTheClientTextViews()
    {
        DatabaseOperations databaseOperations = new DatabaseOperations(this);
        Client client = databaseOperations.getClientById(_id, databaseOperations);

        _textViewId.setText(_id);
        ;
        _textViewName.setText(client.getName());
        _textViewAddress.setText(client.getAddress());

        String phoneNumber = client.getPhoneNumber();
        if(!TextUtils.isEmpty(phoneNumber))
        {
            _textViewPhoneNumber.setText(phoneNumber);
        }
        else
        {
            _textViewPhoneNumber.setText("NO PHONE-NUMBER");
        }
    }

    private void setAllTheTextViews()
    {
        _textViewId = (TextView) findViewById(R.id.textViewClientIdShow);
        _textViewName = (TextView) findViewById(R.id.textViewClientNameShow);
        _textViewAddress = (TextView) findViewById(R.id.textViewClientAddressShow);
        _textViewPhoneNumber = (TextView) findViewById(R.id.textViewClientPhoneNumberShow);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client_details, menu);
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
}
