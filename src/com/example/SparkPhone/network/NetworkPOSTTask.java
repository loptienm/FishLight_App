package com.example.SparkPhone.network;

import android.app.Activity;
import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: noah_moore
 * Date: 1/3/14
 * Time: 8:50 AM
 */
public class NetworkPOSTTask extends BaseNetworkTask
{
    public NetworkPOSTTask( Activity activity, String url )
    {
        super( activity, url );
    }

    @Override
    HttpRequestBase getHttpResponse( String url, BasicNameValuePair... valuePairs )
    {
        HttpPost requestBase = new HttpPost( url );
        if ( valuePairs.length != 0 )
        {
            List<NameValuePair> nameValuePairs = new ArrayList<>( valuePairs.length );
            Collections.addAll( nameValuePairs, valuePairs );
            try
            {
                requestBase.setEntity( new UrlEncodedFormEntity( nameValuePairs ) );
            }
            catch ( UnsupportedEncodingException e )
            {
                Log.e( "HTTPPost", "Unable to set nameValuePairs", e );
            }
        }
        return requestBase;
    }
}