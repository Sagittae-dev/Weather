package com.example.weather;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView getWeatherTextView;

    public void getWeather(View view){

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute("https://openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=439d4b804bc8187953eb36d2a8c26a02");
    }


    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1)
                {
                    char current = (char)data;
                    result +=current;
                    data = reader.read();
                }
                return result;
            }
            catch (Exception e)
            {
                //Toast.makeText(getApplicationContext(), "I cant find the weather:(",Toast.LENGTH_LONG).show();
                e.printStackTrace();
                return  null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                String countryInfo= jsonObject.getJSONObject("sys").getString("country");
                Log.i("weather content", weatherInfo);
                Log.i ("country ", countryInfo);
                JSONArray array = new JSONArray(weatherInfo);
                String setText = "";

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject1 = array.getJSONObject(i);
                    Log.i("main", jsonObject1.getString("main"));
                    Log.i("description", jsonObject1.getString("description"));
                    getWeatherTextView = findViewById(R.id.getWeatherTextView);
                    if (!jsonObject1.getString("description").equals("") && !jsonObject1.getString("description").equals("")) {
                        setText = jsonObject1.getString("main") + ": " + jsonObject1.getString("description")  +"\r\n";
                    }
                }
                if (!setText.equals(""))
                    getWeatherTextView.setText(setText+ "\r\n"+"Country: "+ countryInfo);
            }
            catch(Exception e)
            {
                Toast.makeText(getApplicationContext(), "I cant find the weather:(",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);

    }
}