package com.example.SparkPhone.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: noah_moore
 * Date: 1/3/14
 * Time: 9:55 AM
 */
public abstract class BaseNetworkTask extends AsyncTask<BasicNameValuePair, Void, HttpResponse>
{
    private final Activity _activity;
    private final String _url;

    private ProgressDialog _dialog;

    BaseNetworkTask( Activity activity, String url )
    {
        _activity = activity;
        _url = url;
    }

    @Override
    protected HttpResponse doInBackground( BasicNameValuePair... basicNameValuePairs )
    {
        HttpResponse httpResponse;
        try
        {
            HttpRequestBase requestBase = getHttpResponse( _url, basicNameValuePairs );
            HttpClient httpClient = createHttpClient();
            httpResponse = httpClient.execute( requestBase );
        }
        catch ( Exception e )
        {
            Log.e( "Error", e.getMessage(), e );
            httpResponse = new BasicHttpResponse( new BasicStatusLine( new ProtocolVersion( "UNKNOWN ERROR", 1, 1 ), -1, e.getMessage() ) );
        }
        return httpResponse;
    }

    abstract HttpRequestBase getHttpResponse( String url, BasicNameValuePair... valuePairs );

    @Override
    protected void onPreExecute()
    {
        _dialog = ProgressDialog.show( _activity, "Contacting", "Executing " + _url, true );
    }

    @Override
    protected void onPostExecute( HttpResponse httpResponse )
    {
        HttpEntity responseEntity = httpResponse.getEntity();
        String status = "Status: " + httpResponse.getStatusLine();
        String response = getHttpResponse( responseEntity );

        String reply = "Reply: " + response;
        String text = status + ' ' + reply;
        Log.i( "Network Communication", text );

        AlertDialog alertDialog = new AlertDialog.Builder( _activity ).setTitle( status ).setMessage( reply ).setPositiveButton( "OK",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialogInterface, int i )
                    {
                        dialogInterface.dismiss();
                    }
                } ).create();
        if ( _dialog != null && _dialog.isShowing() )
        {
            _dialog.dismiss();
        }
        alertDialog.show();
    }

    private static String getHttpResponse( HttpEntity responseEntity )
    {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader( new InputStreamReader( responseEntity.getContent() ) );
            String line;
            while ( ( line = reader.readLine() ) != null )
            {
                content.append( line ).append( "\n" );
            }
        }
        catch ( Exception e )
        {
            Log.e( "Reader", "Error reading input", e );
        }
        finally
        {
            if ( reader != null )
            {
                try
                {
                    reader.close();
                }
                catch ( IOException e )
                {
                    Log.e( "Reader", "Error closing reader", e );
                }
            }
        }
        return content.substring( 0, content.length() - 1 );
    }

    private static HttpClient createHttpClient()
    {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion( params, HttpVersion.HTTP_1_1 );
        HttpProtocolParams.setContentCharset( params, HTTP.DEFAULT_CONTENT_CHARSET );
        HttpProtocolParams.setUseExpectContinue( params, true );

        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register( new Scheme( "http", PlainSocketFactory.getSocketFactory(), 80 ) );
        schReg.register( new Scheme( "https", SSLSocketFactory.getSocketFactory(), 443 ) );
        ClientConnectionManager conMgr = new ThreadSafeClientConnManager( params, schReg );

        return new DefaultHttpClient( conMgr, params );
    }
}
