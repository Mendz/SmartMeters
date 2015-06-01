package il.ac.hit.android.smartmeters.support;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import il.ac.hit.android.smartmeters.R;
import il.ac.hit.android.smartmeters.database.Tables;
import il.ac.hit.android.smartmeters.login.LoginActivity;
import il.ac.hit.android.smartmeters.utils.UtilsSignOut;
import il.ac.hit.android.smartmeters.utils.UtilsViewText;


public class SupportActivity extends ActionBarActivity implements View.OnClickListener
{
    private String _id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        Button button = (Button) findViewById(R.id.buttonMapAllMeters);
        button.setOnClickListener(this);

        TextView textViewSupportTitle = (TextView) findViewById(R.id.textViewSupportTitle);

        Intent intent = getIntent();
        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase(LoginActivity.LOGIN_ACTION))
        {
            _id = intent.getStringExtra(Tables.ClientTable.UserId);
            UtilsViewText.setTextViewTitleHello(textViewSupportTitle, _id, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_support, menu);
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
        switch (v.getId())
        {
            case R.id.buttonMapAllMeters:
            {
                Intent intent = new Intent(this, AllMetersMap.class);
                intent.putExtra(Tables.ClientTable.UserId,_id);
                startActivity(intent);
            }
            break;
        }
    }
}
