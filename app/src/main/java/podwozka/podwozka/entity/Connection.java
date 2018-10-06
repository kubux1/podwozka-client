package podwozka.podwozka.entity;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import settings.ConnectionSettings;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Connection {
    private int httpResponseCode;
    private String response;


    private void setHttpResponseCode(int httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
    }

    public int getHttpResponseCode() {
        return this.httpResponseCode;
    }

    private void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return this.response;
    }

    public Connection(){ }

    public void sendCommand(final String httpCommand, final String httpCommandType, final String dataToSend, final String idToken, final CountDownLatch latch){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final ConnectionSettings connectionSettings = new ConnectionSettings();
                String url = connectionSettings.getHostIP() + ":" + connectionSettings.getHostPort() + "/" + httpCommand; // URL to call

                try {
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // Setting basic request
                    con.setRequestMethod(httpCommandType);
                    con.setRequestProperty("Content-Type","application/json");
                    // Set token if passed
                    if(idToken != null) {
                        con.setRequestProperty("Authorization", "Bearer " + idToken);
                    }

                    // Send request
                    if(httpCommandType == "GET"){
                        con.connect();
                    }
                    if(dataToSend != null) {
                        con.setDoOutput(true);
                        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                        wr.writeBytes(dataToSend);
                        wr.flush();
                        wr.close();
                    }

                    setHttpResponseCode(con.getResponseCode());
                    System.out.println("nSending" + httpCommandType + "request to URL : " + url);
                    if(dataToSend != null) {
                        System.out.println("Data : " + dataToSend);
                    }
                    System.out.println("Response Code : " + getHttpResponseCode());

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String output;
                    StringBuffer response = new StringBuffer();

                    while ((output = in.readLine()) != null) {
                        response.append(output);
                    }
                    setResponse(response.toString());
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        });
        thread.start();
    }
}
