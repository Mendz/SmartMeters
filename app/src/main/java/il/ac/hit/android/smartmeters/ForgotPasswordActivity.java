package il.ac.hit.android.smartmeters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;



public class ForgotPasswordActivity extends ActionBarActivity implements View.OnClickListener
{
    private AlertDialog.Builder builder;
    private AlertDialog alert;
    private String userGetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //TODO: change this to the password from the database
        userGetPassword = "TEMP_PASSWORD";

        Button buttonGetPassword = (Button) findViewById(R.id.buttonGetPassword);

        buttonGetPassword.setOnClickListener(this);
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
                setDialogAlertPassword();

                alert.show();
            }
            break;
        }

    }

    private void setDialogAlertPassword()
    {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Your Password")
                .setMessage("Your Password is -  " + userGetPassword)
                .setCancelable(false)
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
    }


}
