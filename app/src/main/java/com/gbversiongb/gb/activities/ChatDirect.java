package com.gbversiongb.gb.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.hbb20.CountryCodePicker;
import com.hbb20.CountryCodePicker.OnCountryChangeListener;

import com.gbversiongb.gb.R;
import com.gbversiongb.gb.modules.Utils;

public class ChatDirect extends AppCompatActivity {
    EditText message;
    private EditText number;
    RelativeLayout SendMessage;
    CountryCodePicker CcP;
    private SharedPreferences preference;
    ImageView image;


    private class btnSendMessageListner implements OnClickListener {
        public void onClick(View v) {
            String messege = ChatDirect.this.message.getText().toString();
            String number = ChatDirect.this.number.getText().toString();
            String mainNumber = ChatDirect.this.CcP.getSelectedCountryCode() + number;
            if (messege.length() == 0) {
                Toast.makeText(ChatDirect.this, "Please enter message", Toast.LENGTH_SHORT).show();
            } else if (number.length() == 0) {
                Toast.makeText(ChatDirect.this, R.string.message_number_empty, Toast.LENGTH_SHORT).show();
            } else if (number.length() < 7 || messege.length() <= 0) {
                Toast.makeText(ChatDirect.this, R.string.message_number_error, Toast.LENGTH_SHORT).show();
            } else {
                try {
                    PackageManager packageManager = ChatDirect.this.getPackageManager();
                    Intent intent = new Intent("android.intent.action.VIEW");
                    try {
                        String str3 = "https://api.whatsapp.com/send?phone=" + mainNumber + "&text=" + messege;
                        intent.setPackage("com.whatsapp");
                        intent.setData(Uri.parse(str3));
                        if (intent.resolveActivity(packageManager) != null) {
                            ChatDirect.this.startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e2) {
                    Toast.makeText(ChatDirect.this, "Error/n" + e2.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private class btnCcpListner implements OnCountryChangeListener {
        public void onCountrySelected() {
            ChatDirect.this.CcP.setCountryPreference(ChatDirect.this.CcP.getSelectedCountryNameCode());
            ChatDirect.this.preference.edit().putString("last_locale", ChatDirect.this.CcP.getSelectedCountryCode()).apply();
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        Utils.loadLocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat_direct);

        if (!(ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_NETWORK_STATE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.SET_WALLPAPER") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.INTERNET") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.SYSTEM_ALERT_WINDOW") == 0) && VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.ACCESS_NETWORK_STATE", "android.permission.SET_WALLPAPER", "android.permission.INTERNET", "android.permission.SYSTEM_ALERT_WINDOW"}, 0);
        }
        image = findViewById(R.id.imBack);
        image.setOnClickListener(view -> onBackPressed());
        this.message = findViewById(R.id.msg);
        this.number = findViewById(R.id.input_text);
        this.CcP = findViewById(R.id.ccp);
        this.SendMessage = findViewById(R.id.go);
        this.SendMessage.setOnClickListener(new btnSendMessageListner());
        this.preference = PreferenceManager.getDefaultSharedPreferences(this);
        this.CcP.setCountryForNameCode(Utils.getCurrentLocale(this));
        this.CcP.setOnCountryChangeListener(new btnCcpListner());
        if (getIntent().getStringExtra("number") != null) {
            this.number.setText(getIntent().getStringExtra("number"));
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }
}
