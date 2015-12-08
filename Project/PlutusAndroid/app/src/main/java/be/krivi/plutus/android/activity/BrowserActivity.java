package be.krivi.plutus.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import be.krivi.plutus.android.R;

public class BrowserActivity extends Activity{

    private WebView browser;
    private ProgressBar bar;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_browser );

        browser = (WebView)findViewById( R.id.webView );
        bar = (ProgressBar)findViewById( R.id.progressBar );

        browser.setWebViewClient( new WebViewClient() );
        browser.setWebChromeClient( new WebChromeClient(){
            public void onProgressChanged( WebView view, int progress ){
                if( progress < 100 && bar.getVisibility() == ProgressBar.GONE )
                    bar.setVisibility( ProgressBar.VISIBLE );
                bar.setProgress( progress );
                if( progress == 100 )
                    bar.setVisibility( ProgressBar.GONE );
            }
        } );
        browser.loadUrl( "http://hln.be" );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ){
        getMenuInflater().inflate( R.menu.menu_browser, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ){

        if( item.getItemId() == R.id.action_open_in_browser ){
            Uri uri = Uri.parse( browser.getUrl() );
            Intent intent = new Intent( Intent.ACTION_VIEW, uri );
            startActivity( intent );
            return true;
        }

        return super.onOptionsItemSelected( item );
    }
}
