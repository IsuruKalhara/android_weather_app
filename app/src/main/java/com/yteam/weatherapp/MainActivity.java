package com.yteam.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    EditText cityText;
    TextView descriptionValue;
    TextView descriptionView;
    TextView temperatureValue;
    TextView temperatureView;
    TextView pressureValue;
    TextView pressureView;
    TextView windValue;
    TextView windView;
    TextView humidValue;
    TextView humidView;
    TextView cityView;

    private String apiKey = "9da7943fd8d7aadf922bbefaf0b925ca";

    public class DownloadJson extends AsyncTask<String,Void,String >{

        @Override
        protected String doInBackground(String... strings) {
            try {
                String data = "";
                URL url = new URL(strings[0]);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int value = reader.read();
                while(value != -1){
                    data += (char) value;
                    value = reader.read();
                }
                return data;
            } catch (Exception e) {
                clearAll();
                e.printStackTrace();
                return "0";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                cityView.setText(""+jsonObject.getString("name")+" Now");

                JSONObject main = jsonObject.getJSONObject("main");
                temperatureValue.setText(""+main.getDouble("temp")+"°C");
                pressureValue.setText(""+main.getInt("pressure")+ " hpa");
                humidValue.setText(""+main.getInt("humidity")+" %");

                JSONObject wind = jsonObject.getJSONObject("wind");
                windValue.setText(""+wind.getDouble("speed")+" m/s");

                JSONArray arr = new JSONArray(jsonObject.getString("weather"));
                JSONObject obj = arr.getJSONObject(0);
                descriptionValue.setText(obj.getString("description"));



            }catch (Exception e){
                clearAll();
                e.printStackTrace();
            }
        }
    }

    public void checkWeather (View view){
        String cityString = cityText.getText().toString();
        String urlPrfix = "https://api.openweathermap.org/data/2.5/weather?q=";
        String urlMiddle = "&units=metric&appid=";
        DownloadJson json = new DownloadJson();
        json.execute(urlPrfix+cityString+urlMiddle+apiKey);

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityText.getWindowToken(),0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityText = findViewById(R.id.cityEditText);
        descriptionValue = findViewById(R.id.descriptionValue);
        descriptionView = findViewById(R.id.descriptionView);
        temperatureValue = findViewById(R.id.temperatureValue);
        temperatureView = findViewById(R.id.temperatureView);
        pressureValue = findViewById(R.id.pressureValue);
        pressureView = findViewById(R.id.pressureView);
        windValue = findViewById(R.id.windValue);
        windView = findViewById(R.id.windView);
        humidValue = findViewById(R.id.humidValue);
        humidView = findViewById(R.id.humidView);
        cityView = findViewById(R.id.cityView);
    }

    public void clearAll (){
        descriptionValue.setText("");
        temperatureValue.setText("");
        pressureValue.setText("");
        windValue.setText("");
        humidValue.setText("");
        cityView.setText("Weather not available");
    }
}
