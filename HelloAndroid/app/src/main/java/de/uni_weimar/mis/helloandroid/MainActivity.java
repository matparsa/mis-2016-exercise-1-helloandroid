package de.uni_weimar.mis.helloandroid;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

public class MainActivity extends AppCompatActivity {
    String urlStr="";
    //we found this solution  from stackOverFlow
    @Override
    protected void onCreate(Bundle savedInstanceState) {
          StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonShowText = (Button) findViewById(R.id.buttonShowText);
        buttonShowText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText et=(EditText)findViewById(R.id.etURL);
                //if user forget or not inserted protocol we consider it as http

                urlStr=et.getText().toString().toLowerCase();
                String siteContent=retrievedHTML(urlStr);
                TextView tvMessage=(TextView)  findViewById(R.id.tvMessage);
                tvMessage.setVisibility(View.VISIBLE);
                WebView wv=(WebView) findViewById(R.id.webView);
                wv.getSettings().setDomStorageEnabled(true);
                wv.setVisibility(View.INVISIBLE);
                tvMessage.setMovementMethod(new ScrollingMovementMethod());
                tvMessage.setText(siteContent);
                }

        });
        Button buttonShowHtml = (Button) findViewById(R.id.btnShowHtml);
        buttonShowHtml.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView tvMessage=(TextView)  findViewById(R.id.tvMessage);
                tvMessage.setVisibility(View.INVISIBLE);
                WebView wv=(WebView) findViewById(R.id.webView);
                wv.setVisibility(View.VISIBLE);
                wv.setWebViewClient(new WebViewClient());
                wv.loadUrl(urlStr);
            }});
    }

    protected String retrievedHTML(String URL)
    {
        HttpClient client = new DefaultHttpClient();
        HttpResponse response ;
        try {
            response = client.execute(new HttpGet(URL));
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                response.getEntity().writeTo(outStream);
                return outStream.toString();
            }
            else{
                response.getEntity().getContent().close();
                throw new IOException(status.getReasonPhrase());
            }
        }
        catch (ConnectTimeoutException e) {
            Toast.makeText(getApplicationContext(), "Timeout error", Toast.LENGTH_LONG).show();
        }
        catch (UnsupportedEncodingException e) {
            Toast.makeText(getApplicationContext(), "Unsupported Encoding error", Toast.LENGTH_LONG).show();
        }
        catch (ClientProtocolException e) {
            Toast.makeText(getApplicationContext(), "Client Protocol error", Toast.LENGTH_LONG).show();
        }
        catch (MalformedURLException e){
            Toast.makeText(getApplicationContext(), "URL is not right", Toast.LENGTH_LONG).show();
        }
        catch (SocketTimeoutException e) {
            Toast.makeText(getApplicationContext(), "Socket timeout error", Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            Toast.makeText(getApplicationContext(), "IO error", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "error:"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }
}
