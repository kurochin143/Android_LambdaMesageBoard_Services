package com.example.israel.android_lambdamessages;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkAdapter {

    public static final int TIMEOUT = 3000;

    @WorkerThread
    @Nullable
    static String httpRequestGET(String urlStr) {
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(TIMEOUT);
            httpURLConnection.setConnectTimeout(TIMEOUT);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Connection error. Response code: " + Integer.toString(responseCode));
            }

            inputStream = httpURLConnection.getInputStream();
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String resStr;
                while ((resStr = reader.readLine()) != null) {
                    builder.append(resStr);
                }

                return builder.toString(); // success
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SecurityException e) { // for future reference. if ever i forgot to put internet permission again!!
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return null;
    }

    @WorkerThread
    @Nullable
    static String httpRequestPOST(String urlStr, String requestBody) {
        HttpURLConnection httpURLConnection = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(TIMEOUT);
            httpURLConnection.setConnectTimeout(TIMEOUT);
            httpURLConnection.setRequestMethod("POST");
            //httpURLConnection.connect();
            outputStream = httpURLConnection.getOutputStream();
            if (outputStream != null) {
                outputStream.write(requestBody.getBytes());
            }

            inputStream = httpURLConnection.getInputStream();
            StringBuilder builder = new StringBuilder();
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String resStr;
                while ((resStr = reader.readLine()) != null) {
                    builder.append(resStr);
                }
            }

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Connection error. Response code: " + Integer.toString(responseCode));
            }

            return builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SecurityException e) { // for future reference. if ever i forgot to put internet permission again!!
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return null;

    }

}
