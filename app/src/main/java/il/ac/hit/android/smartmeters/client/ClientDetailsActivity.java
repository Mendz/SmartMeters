package il.ac.hit.android.smartmeters.client;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import il.ac.hit.android.smartmeters.R;
import il.ac.hit.android.smartmeters.database.DatabaseOperations;
import il.ac.hit.android.smartmeters.database.Meter;
import il.ac.hit.android.smartmeters.database.Tables;
import il.ac.hit.android.smartmeters.utils.UtilsSignOut;

import java.util.List;

public class ClientDetailsActivity extends ActionBarActivity
{
    private String _id;

    private TextView _textViewId;
    private TextView _textViewName;
    private TextView _textViewAddress;
    private TextView _textViewPhoneNumber;
    private LinearLayout _linearLayoutMeterId;
    private LinearLayout _linearLayoutMeterAddress;
    private LinearLayout _linearLayoutMeterKwh;

    private DatabaseOperations _databaseOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        _databaseOperations = new DatabaseOperations(this);

        _id = getIntent().getStringExtra(Tables.ClientTable.UserId);

        setContentView(R.layout.activity_client_details);
        setAllTheViews();

        fillAllTheClientTextViews();

        setMeterDetails();
    }

    private void setMeterDetails()
    {
        List<Meter> meterList = _databaseOperations.getAllMetersById(_id, _databaseOperations);

        TextView textViewMeterDetails;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 10;

        for (Meter meter : meterList)
        {
            textViewMeterDetails = new TextView(this);
            textViewMeterDetails.setText(meter.getMeterId());
            _linearLayoutMeterId.addView(textViewMeterDetails, params);

            textViewMeterDetails = new TextView(this);
            textViewMeterDetails.setText(meter.getAddress());
            _linearLayoutMeterAddress.addView(textViewMeterDetails, params);

            textViewMeterDetails = new TextView(this);
            textViewMeterDetails.setText(meter.getkWh());
            _linearLayoutMeterKwh.addView(textViewMeterDetails,params);
        }
    }

    private void fillAllTheClientTextViews()
    {
        Client client = _databaseOperations.getClientById(_id, _databaseOperations);

        _textViewId.setText(_id);
        ;
        _textViewName.setText(client.getName());
        _textViewAddress.setText(client.getAddress());

        String phoneNumber = client.getPhoneNumber();
        if (!TextUtils.isEmpty(phoneNumber))
        {
            _textViewPhoneNumber.setText(phoneNumber);
        }
        else
        {
            _textViewPhoneNumber.setText("NO PHONE-NUMBER");
        }
    }

    private void setAllTheViews()
    {
        _textViewId = (TextView) findViewById(R.id.textViewClientIdShow);
        _textViewName = (TextView) findViewById(R.id.textViewClientNameShow);
        _textViewAddress = (TextView) findViewById(R.id.textViewClientAddressShow);
        _textViewPhoneNumber = (TextView) findViewById(R.id.textViewClientPhoneNumberShow);

        _linearLayoutMeterId = (LinearLayout) findViewById(R.id.linearLayoutMeterId);
        _linearLayoutMeterAddress = (LinearLayout) findViewById(R.id.linearLayoutMeterAddress);
        _linearLayoutMeterKwh = (LinearLayout) findViewById(R.id.linearLayoutMeterKwh);
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
