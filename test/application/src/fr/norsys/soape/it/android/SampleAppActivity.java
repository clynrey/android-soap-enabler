package fr.norsys.soape.it.android;

import org.tempuri.CelsiusToFahrenheit;
import org.tempuri.CelsiusToFahrenheitResponse;
import org.tempuri.FahrenheitToCelsius;
import org.tempuri.FahrenheitToCelsiusResponse;
import org.tempuri.TempConvertSoap;
import org.tempuri.TempConvertSoapImpl;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import fr.norsys.asoape.ws.WSCallBack;

public class SampleAppActivity
    extends Activity
{
    /** Remote Web Service. */
    private TempConvertSoap service;

    /** User input component. */
    private EditText input;

    /** User output component. */
    private EditText output;

    /** Handler to multithread the call. */
    private Handler handler;

    /** Called when the activity is first created. */
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );
        handler = new Handler();
        input = (EditText) findViewById( R.id.input );
        output = (EditText) findViewById( R.id.output );
        try
        {
            service = new TempConvertSoapImpl();
        }
        catch ( Exception exception )
        {
        }
    }

    public void convertToCelsius( View view )
    {
        FahrenheitToCelsius request = new FahrenheitToCelsius();
        request.setFahrenheit( input.getText().toString() );
        service.FahrenheitToCelsius( request, new WSCallBack<FahrenheitToCelsiusResponse>()
        {
            public void onSuccess( FahrenheitToCelsiusResponse result )
            {
                updateOutput( result.getFahrenheitToCelsiusResult() );
            }

            public void onFailure( Throwable throwable )
            {
                updateOutput( throwable.getMessage() );
            }
        } );
    }

    public void convertToFahrenheit( View view )
    {
        CelsiusToFahrenheit request = new CelsiusToFahrenheit();
        request.setCelsius( input.getText().toString() );
        service.CelsiusToFahrenheit( request, new WSCallBack<CelsiusToFahrenheitResponse>()
        {
            public void onSuccess( CelsiusToFahrenheitResponse result )
            {
                updateOutput( result.getCelsiusToFahrenheitResult() );
            };

            public void onFailure( Throwable throwable )
            {
                updateOutput( throwable.getMessage() );
            }
        } );
    }

    private void updateOutput( final String string )
    {
        handler.post( new Runnable()
        {
            public void run()
            {
                output.setText( string );
            }
        } );
    }
}