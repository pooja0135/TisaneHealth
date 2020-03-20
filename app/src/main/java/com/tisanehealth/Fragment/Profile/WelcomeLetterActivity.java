package com.tisanehealth.Fragment.Profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.tisanehealth.Helper.AppSettings;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.R;

public class WelcomeLetterActivity extends AppCompatActivity {

    WebView webview;
    ImageView ivBack;
    ProgressBar progressBar;
    Preferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_letter);

        ivBack=findViewById(R.id.ivBack);
        webview=findViewById(R.id.webview);
        progressBar =findViewById(R.id.progressbar);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pref=new Preferences(this);



        webview.setWebViewClient(new myWebClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //Required functionality here
                return super.onJsAlert(view, url, message, result);
            }
        });



        webview.loadUrl("https://docs.google.com/viewer?embedded=true&url="+"http://thcp.co.in/Wel.aspx?ID="+pref.get(AppSettings.UserId));
        //webview.loadUrl("http://thcp.co.in/Wel.aspx/Id="+pref.get(AppSettings.UserId));




        webview.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


    }


    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            // progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            progressBar.setVisibility(View.GONE);
            view.loadUrl(url);

            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {

            progressBar.setVisibility(View.GONE);
            view.loadUrl("javascript:var footer = document.getElementById(\"FooterContainer\"); footer.parentNode.removeChild(footer);");

            super.onPageFinished(view, url);
        }





    }




    // To handle &quot;Back&quot; key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webview.canGoBack()) {
            this.webview.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
