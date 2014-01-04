package com.example.SparkPhone;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.example.SparkPhone.network.BaseNetworkTask;
import com.example.SparkPhone.network.NetworkGETTask;
import com.example.SparkPhone.network.NetworkPOSTTask;
import org.apache.http.message.BasicNameValuePair;

public class Spark extends Activity
{
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );
        restoreViews();
    }

    private void restoreViews()
    {
        SharedPreferences sharedPreferences = getPreferences( MODE_PRIVATE );
        String coreId = sharedPreferences.getString( String.valueOf( R.id.coreId ), "" );
        String accessToken = sharedPreferences.getString( String.valueOf( R.id.accessToken ), "" );
        String methodName = sharedPreferences.getString( String.valueOf( R.id.methodName ), "" );
        String methodArgName = sharedPreferences.getString( String.valueOf( R.id.methodArgName ), "" );
        String methodArgValue = sharedPreferences.getString( String.valueOf( R.id.methodArgValue ), "" );

        setEditViewText( R.id.coreId, coreId );
        setEditViewText( R.id.accessToken, accessToken );
        setEditViewText( R.id.methodName, methodName );
        setEditViewText( R.id.methodArgName, methodArgName );
        setEditViewText( R.id.methodArgValue, methodArgValue );
    }

    public void executeMethod( View pressedView )
    {
        String coreId = getEditViewText( R.id.coreId );
        String accessToken = getEditViewText( R.id.accessToken );
        String methodName = getEditViewText( R.id.methodName );
        String methodArgName = getEditViewText( R.id.methodArgName );
        String methodArgValue = getEditViewText( R.id.methodArgValue );

        SharedPreferences sharedPreferences = getPreferences( MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString( String.valueOf( R.id.coreId ), coreId );
        editor.putString( String.valueOf( R.id.accessToken ), accessToken );
        editor.putString( String.valueOf( R.id.methodName ), methodName );
        editor.putString( String.valueOf( R.id.methodArgName ), methodArgName );
        editor.putString( String.valueOf( R.id.methodArgValue ), methodArgValue );
        editor.commit();

        executeFunction( coreId, methodName, pressedView.getId() == R.id.executePost, new BasicNameValuePair( "access_token", accessToken ), new BasicNameValuePair( methodArgName, methodArgValue ) );
    }

    private void executeFunction( String coreId, String methodName, boolean usePost, BasicNameValuePair... params )
    {
        String URL = "https://api.spark.io/v1/devices/" + coreId + "/" + methodName;
        BaseNetworkTask task;
        if ( usePost )
        {
            task = new NetworkPOSTTask( this, URL );
        }
        else
        {
            task = new NetworkGETTask( this, URL );
        }
        task.execute( params );
    }

    private String getEditViewText( int id )
    {
        return ( (EditText)findViewById( id ) ).getText().toString();
    }

    private void setEditViewText( int id, String text )
    {
        ( (EditText)findViewById( id ) ).setText( text );
    }
}
