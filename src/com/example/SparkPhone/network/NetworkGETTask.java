package com.example.SparkPhone.network;

import android.app.Activity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;

/**
 * Created with IntelliJ IDEA.
 * User: noah_moore
 * Date: 1/3/14
 * Time: 8:50 AM
 */
public final class NetworkGETTask extends BaseNetworkTask
{
    public NetworkGETTask( Activity activity, String url )
    {
        super( activity, url );
    }

    @Override
    HttpRequestBase getHttpResponse( String url, BasicNameValuePair... valuePairs )
    {
        HttpGet httpGet = new HttpGet( url );
        if ( valuePairs.length != 0 )
        {
            BasicHttpParams params = new BasicHttpParams();
            for ( BasicNameValuePair pair : valuePairs )
            {
                String name = pair.getName();
                String value = pair.getValue();
                params.setParameter( name, value );
            }
            httpGet.setParams( params );
        }
        return httpGet;
    }
}
