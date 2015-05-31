package il.ac.hit.android.smartmeters.client;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.internal.in;
import il.ac.hit.android.smartmeters.R;
import il.ac.hit.android.smartmeters.database.Tables;
import il.ac.hit.android.smartmeters.login.LoginActivity;
import il.ac.hit.android.smartmeters.utils.UtilsSignOut;


public class ClientActivity extends ActionBarActivity implements View.OnClickListener
{
    private String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        Button buttonMetersMap = (Button) findViewById(R.id.buttonClientContersMap);
        buttonMetersMap.setOnClickListener(this);

        Button buttonDetails = (Button) findViewById(R.id.buttonClientDetails);
        buttonDetails.setOnClickListener(this);

        Intent intent = this.getIntent();
        _id = intent.getStringExtra(Tables.ClientTable.UserId);
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
        }

        startActivity(intent);
    }
}
