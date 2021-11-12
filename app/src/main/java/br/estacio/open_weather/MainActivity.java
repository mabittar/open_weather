package br.estacio.open_weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements Runnable {
    private TextView txtCidade;
    private TextView txtEstado;
    private TextView txtPais;
    private TextView txtTemp;
    private TextView txtFeel;
    private TextView txtHumd;
    private JSONObject json;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtCidade = (TextView) findViewById(R.id.txtCidade);
        txtEstado = (TextView) findViewById(R.id.txtEstado);
        txtPais = (TextView) findViewById(R.id.txtPais);
        txtTemp = (TextView) findViewById(R.id.txtTemp);
        txtFeel = (TextView) findViewById(R.id.txtFeel);
        txtHumd = (TextView) findViewById(R.id.txtHumd);

    }

    @Override
    public void run() {
        URL url;
        HttpsURLConnection urlConnection = null;
        String uri = "https://fastwheater.herokuapp.com/api/weather/";

        try {
            url = new URL("uri" + txtCidade + "?state=" + txtEstado + "&country=" + txtPais);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStreamReader entrada = new InputStreamReader(urlConnection.getInputStream());
            json = new JSONObject(lerDados(entrada));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

    }

    private String lerDados(InputStreamReader entrada){
        char[] dados = new char[200];

        try {
            entrada.read(dados);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(dados);
    }

    public void btnPrevisao(View view) {
        new Thread(MainActivity.this).start();
        try {
            txtTemp.setText((CharSequence) json.getJSONObject("temp"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            txtFeel.setText((CharSequence) json.getJSONObject("feels_like"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            txtHumd.setText((CharSequence) json.getJSONObject("humidity"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}