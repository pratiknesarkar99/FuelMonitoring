package com.example.fuelmonitoring.tabs;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.fuelmonitoring.user.UserHome;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class UserBackgroundWorker extends AsyncTask<String, Void, String> {

    //Counter for No of Attempts
    private static int COUNTER = 5;

    Context context;

    UserBackgroundWorker(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];

        if(type.equals("userlogin")){
            try {
                String uname = params[1];
                String pass = params[2];

                //To communicate with android using php use 10.0.2.2 in url
                String login_url = "http://192.168.43.189/UserLogin.php";
                URL url = new URL(login_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("uname", "UTF-8")+"="+URLEncoder.encode(uname, "UTF-8")+"&"
                        +URLEncoder.encode("pass", "UTF-8")+"="+URLEncoder.encode(pass, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String result="", line;

                while ((line=bufferedReader.readLine()) != null){
                        result += line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("Login Successful")){
            Intent intent = new Intent(context, UserHome.class);
            Toast toast = Toast.makeText(context, "*Login Successful*", Toast.LENGTH_SHORT);
            toast.show();
            context.startActivity(intent);
        }else {
            if(COUNTER == 0){
                Toast toast1 = Toast.makeText(context, "Login Blocked, Try again Later!!!", Toast.LENGTH_SHORT);
                toast1.show();
            }else {
                COUNTER--;
                Toast toast = Toast.makeText(context, "Wrong credentials!!! " + COUNTER + " attempts left!!!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
