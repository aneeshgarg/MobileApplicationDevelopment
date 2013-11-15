package com.thetechdork.rottentomatoes.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.thetechdork.rottentomatoes.utility.Config;

public class UsernameActivity extends BaseActivity {

    private EditText usernameTextBox;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
        
        usernameTextBox = (EditText) findViewById(R.id.username_textbox);

        pref = getSharedPreferences(SHARED_PREF_NAME, 0);
        String username = pref.getString(USERNAME_KEY, null);
        if (username != null)
            usernameTextBox.setText(username);
        else
            usernameTextBox.setText("");
        
        ((Button)findViewById(R.id.set_username_button)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                String username = usernameTextBox.getText().toString();
                if (username != null && username.length() > 0) {
                    usernameTextBox.setError(null);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(USERNAME_KEY, username);
                    String userId = Config.getUid(username);
                    editor.commit();
                    Log.d(LOG_KEY, "UserId: " + userId);
                    finish();
                }
                else
                    usernameTextBox.setError("Invalid username. Please re-enter username.");                
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

}
