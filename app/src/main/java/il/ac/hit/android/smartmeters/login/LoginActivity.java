package il.ac.hit.android.smartmeters.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import il.ac.hit.android.smartmeters.AdminActivity;
import il.ac.hit.android.smartmeters.R;
import il.ac.hit.android.smartmeters.support.SupportActivity;
import il.ac.hit.android.smartmeters.client.ClientActivity;
import il.ac.hit.android.smartmeters.database.DatabaseOperations;
import il.ac.hit.android.smartmeters.database.Tables;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor>, OnClickListener
{
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUserNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mButtonForgotPassword;
    private Button mButtonRegister;
    private CheckBox mCheckBoxRememberPassword;

    private static final int BACKGROUND_COLOR_LOGIN_PROGRESS = 1;
    private static final int BACKGROUND_COLOR_DEFAULT = 2;
    private View mLayoutLoginActivity;

    private SharedPreferences mLoginPreferences;
    private SharedPreferences.Editor mLoginPrefsEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d("login_activity", "within onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        printToLogAllTables();

        mButtonForgotPassword = (Button) findViewById(R.id.buttonForgetPassword);
        mButtonForgotPassword.setOnClickListener(this);
        mButtonRegister = (Button) findViewById(R.id.buttonRegister);
        mButtonRegister.setOnClickListener(this);

        // Set up the login form.
        mUserNameView = (AutoCompleteTextView) findViewById(R.id.userNameTextView);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == R.id.login || id == EditorInfo.IME_NULL)
                {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mUserNameSignInButton = (Button) findViewById(R.id.userNameSignInButton);
        mUserNameSignInButton.setOnClickListener(this);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mLayoutLoginActivity = findViewById(R.id.layoutLogin);
        mLoginFormView.requestFocus();

        mCheckBoxRememberPassword = (CheckBox) findViewById(R.id.checkBoxRememberPassword);

        String clientId = getClientIdIfUserDataRemembered();

        if (clientId != null)
        {
            goToIntentByUser(clientId);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        printToLogAllTables();
    }

    private void printToLogAllTables()
    {
        DatabaseOperations databaseOperations = new DatabaseOperations(this);
        String allTables = databaseOperations.getAllTablesString(databaseOperations);

        Log.d("Dtabase operations", "All the database: \n\n" + allTables);
    }

    private void goToIntentByUser(String clientId)
    {
        showProgress(true);
        DatabaseOperations databaseOperations = new DatabaseOperations(this);

        String userType = databaseOperations.getUserTypeById(clientId);
        Intent intent = null;

        switch (userType)
        {
            case Tables.UserTypeTable.UserTypes.ADMIN:
            {
                intent = new Intent(this, AdminActivity.class);
            }
            break;
            case Tables.UserTypeTable.UserTypes.SUPPORT:
            {
                intent = new Intent(this, SupportActivity.class);
            }
            break;
            case Tables.UserTypeTable.UserTypes.CLIENT:
            {
                intent = new Intent(this, ClientActivity.class);
                intent.putExtra(Tables.ClientTable.UserId, clientId);
            }
            break;
        }

        showProgress(false);

        if (intent != null)
        {
            startActivity(intent);
        }
        else
        {
            Log.e("login_activity", "Wrong User Type: " + userType);
            Toast.makeText(this, "An error occurred with user type: " + userType, Toast.LENGTH_SHORT).show();
        }

        //finish();
    }

    private String getClientIdIfUserDataRemembered()
    {
        Log.d("login_activity", "within getClientIdIfUserDataRemembered");
        boolean saveLogin;

        mLoginPreferences = getSharedPreferences(LoginPreferences.NAME, MODE_PRIVATE);
        mLoginPrefsEditor = mLoginPreferences.edit();

        saveLogin = mLoginPreferences.getBoolean(LoginPreferences.SAVE_LOGIN, false);

        Log.d("login_activity", "save login: " + saveLogin);
        if (saveLogin)
        {
            mUserNameView.setText(mLoginPreferences.getString(LoginPreferences.CLIENT_NAME, ""));
            mPasswordView.setText(mLoginPreferences.getString(LoginPreferences.CLIENT_PASSWORD, ""));
            mCheckBoxRememberPassword.setChecked(true);

            return mLoginPreferences.getString(LoginPreferences.CLIENT_ID, "");
        }

        return null;
    }

    private void saveLoginPreferences(String clientId, String clientName, String password)
    {
        if (mCheckBoxRememberPassword.isChecked())
        {
            mLoginPrefsEditor.putBoolean(LoginPreferences.SAVE_LOGIN, true);
            mLoginPrefsEditor.putString(LoginPreferences.CLIENT_NAME, clientName);
            mLoginPrefsEditor.putString(LoginPreferences.CLIENT_PASSWORD, password);
            mLoginPrefsEditor.putString(LoginPreferences.CLIENT_ID, clientId);
            mLoginPrefsEditor.commit();
        }
        else
        {
            mLoginPrefsEditor.clear();
            mLoginPrefsEditor.commit();
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.userNameSignInButton:
            {
                attemptLogin();
            }
            break;
            case R.id.buttonRegister:
            {
                mButtonForgotPassword.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(this, RegisterNewUserActivity.class);

                startActivity(intent);
            }
            break;
            case R.id.buttonForgetPassword:
            {
                mButtonForgotPassword.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(this, ForgotPasswordActivity.class);

                startActivity(intent);
            }
            break;
        }
    }

    private void populateAutoComplete()
    {
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin()
    {
        if (mAuthTask != null)
        {
            return;
        }

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) && !isPasswordValid(password))
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid user name address.
        if (TextUtils.isEmpty(userName))
        {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }
        else if (!isUserNameValid(userName))
        {
            mUserNameView.setError(getString(R.string.error_invalid_user_name));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else
        {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(userName, password, this);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUserNameValid(String userName)
    {
        return !isStringNullOrWhiteSpace(userName);
    }

    private boolean isPasswordValid(String password)
    {
        return password.length() >= getResources().getInteger(R.integer.min_password_length);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show)
    {

        setBackgroundColorLoginProgress(show ? BACKGROUND_COLOR_LOGIN_PROGRESS : BACKGROUND_COLOR_DEFAULT);

        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter()
                    {
                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter()
                    {
                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });
        }
        else
        {
            setBackgroundColorLoginProgress(show ? BACKGROUND_COLOR_LOGIN_PROGRESS : BACKGROUND_COLOR_DEFAULT);
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?", new String[]{ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
    {
        List<String> userNames = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            userNames.add(cursor.getString(ProfileQuery.NAME));
            cursor.moveToNext();
        }

        addUserNamesToAutoComplete(userNames);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader)
    {

    }

    private interface ProfileQuery
    {
        String[] PROJECTION = {ContactsContract.CommonDataKinds.Nickname.NAME, ContactsContract.CommonDataKinds.Nickname.IS_PRIMARY,};

        int NAME = 0;
        int IS_PRIMARY = 1;
    }

    //TODO: change this and akk the other methods to take user names from our database!
    //TODO: it will show if there is a user name like this..
    private void addUserNamesToAutoComplete(List<String> userNamesCollection)
    {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(LoginActivity.this, android.R.layout.simple_dropdown_item_1line, userNamesCollection);

        mUserNameView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
    {

        private final String mUserName;
        private final String mPassword;
        private DatabaseOperations mDatabaseOperations;
        private String mClientId;

        UserLoginTask(String userName, String password, Context context)
        {
            mUserName = userName;
            mPassword = password;
            mDatabaseOperations = new DatabaseOperations(context);

        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            //            try
            //            {
            //                // Simulate network access.
            //                Thread.sleep(2000);
            //            } catch (InterruptedException e)
            //            {
            //                return false;
            //            }

            try
            {
                mClientId = mDatabaseOperations.isUserNameAndPasswordAreCorrect(mUserName, mPassword);
            } catch (Exception e)
            {
                Log.e("async_task_background", "There was a problem", e);
                e.printStackTrace();
            }

            return mClientId != null;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            mAuthTask = null;
            showProgress(false);

            if (success)
            {
                saveLoginPreferences(mClientId, mUserName, mPassword);
                goToIntentByUser(mClientId);
            }
            else
            {
                //TODO: for now it will show this error even when the user name is incorrect.
                mButtonForgotPassword.setVisibility(View.VISIBLE);
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled()
        {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public static boolean isStringNullOrWhiteSpace(String value)
    {
        if (value == null)
        {
            return true;
        }

        for (int i = 0; i < value.length(); i++)
        {
            if (!Character.isWhitespace(value.charAt(i)))
            {
                return false;
            }
        }

        return true;
    }

    private void setBackgroundColorLoginProgress(int backgroundColor)
    {
        switch (backgroundColor)
        {
            case BACKGROUND_COLOR_LOGIN_PROGRESS:
            {
                mLayoutLoginActivity.setBackground(getResources().getDrawable(R.color.color_background_layout_login));
            }
            break;
            case BACKGROUND_COLOR_DEFAULT:
            {
                mLayoutLoginActivity.setBackground(getResources().getDrawable(R.color.color_background_layout));
            }
            break;
        }
    }
}



