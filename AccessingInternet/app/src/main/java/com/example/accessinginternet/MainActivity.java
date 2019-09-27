package com.example.accessinginternet;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button checkInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkInternet = findViewById(R.id.checkInternetButton);
        checkInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){
                    Toast.makeText(MainActivity.this, "Hello. Internet is Connected", Toast.LENGTH_SHORT).show();
                    new GetDataAsyc().execute("http://api.theappsdr.com/simple.php");
                }
                else{
                    Toast.makeText(MainActivity.this, "Internet is not connected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected())
            return false;
        return true;
    }

    public class GetDataAsyc extends AsyncTask<String, Void, String>{
        @Override
        protected void onPostExecute(String s) {
            if(s != null)
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainActivity.this, "null result", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                connection =(HttpURLConnection) url.openConnection();
                inputStream = connection.getInputStream();
                BufferedReader reader =  new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while((line = reader.readLine())!=null)
                {
                    stringBuilder.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(connection!=null)
                    connection.disconnect();
                if(inputStream!=null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return stringBuilder.toString();
        }
    }
}
