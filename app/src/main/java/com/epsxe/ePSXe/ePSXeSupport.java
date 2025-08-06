package com.epsxe.ePSXe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/* loaded from: classes.dex */
public class ePSXeSupport extends Activity {
    private static final int BACK_ID = 1;
    private static final int PREFERENCES_GROUP_ID = 0;
    WebView browser;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        this.browser = (WebView) findViewById(R.id.webkit);
        this.browser.loadUrl("file:///android_asset/html/localsupport.html");
        this.browser.setWebViewClient(new WebViewClient() { // from class: com.epsxe.ePSXe.ePSXeSupport.1
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("WebView", "Attempting to load URL: " + url);
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && event.getRepeatCount() == 0) {
            Intent myIntent = new Intent(this, (Class<?>) ePSXe.class);
            startActivity(myIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, R.string.file_unsuportedback).setIcon(android.R.drawable.ic_menu_revert);
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, 1, 0, R.string.file_unsuportedback).setIcon(android.R.drawable.ic_menu_revert);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Intent myIntent = new Intent(this, (Class<?>) ePSXe.class);
                startActivity(myIntent);
                finish();
                break;
        }
        return true;
    }
}
