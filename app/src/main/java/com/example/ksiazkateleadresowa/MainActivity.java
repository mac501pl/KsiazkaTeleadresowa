package com.example.ksiazkateleadresowa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final byte READ_CONTACTS_REQUEST_CODE = 0;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // aplikacja nie działa w 100% poprawnie.
        // gdy odpytuję telefon o takie informacje jak firma, tytuł czy notatki, dostaję z reguły
        // dziwne i niepasujące informacje jak np. numer telefonu. nie wiem czym ejst to spowodowane
        // bo wydaje mi się że odpytuję o te informacje poprawnie. Nie mam żadnego pomysłu jak to naprawić
        // czasem też podawane są adresy email nie tego kontaktu

        webView = findViewById(R.id.webView);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_CONTACTS_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.READ_CONTACTS)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        loadLocalHTML();
                    } else {
                        Toast.makeText(this, "Aplikacja nie może działać\nbez dostępu do kontaktów!", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_REQUEST_CODE);
                    }
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void loadLocalHTML() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        webView.addJavascriptInterface(new CustomJavaScriptInterface(this), "HtmlAndroidBridge");

        webView.loadUrl("file:///android_asset/index.html");
    }
}
