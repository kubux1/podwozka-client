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
    private InputStream inputStream;
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

    public int sendGetCommand(final String httpCommand, List<NameValuePair> object, final String idToken, final CountDownLatch latch) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ConnectionSettings connectionSettings = new ConnectionSettings();
                    URL url = new URL(connectionSettings.getHostIP() + ":" + connectionSettings.getHostPort() + "/" + httpCommand);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Authorization","Bearer " + idToken);
                    connection.connect();

                    setHttpResponseCode(connection.getResponseCode());

                    // Get response
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String output;
                    StringBuffer response = new StringBuffer();

                    while ((output = in.readLine()) != null) {
                        response.append(output);
                    }
                    setResponse(response.toString());

                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        });
        thread.start();
        return getHttpResponseCode();
    }

    // Post command without authorization token
    public void sendPostCommand(final String httpCommand, final String dataToPost, final CountDownLatch latch){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final ConnectionSettings connectionSettings = new ConnectionSettings();
                String url = connectionSettings.getHostIP() + ":" + connectionSettings.getHostPort() + "/" + httpCommand; // URL to call
                OutputStream out = null;

                try {
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // Setting basic post request
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type","application/json");

                    // Send post request
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(dataToPost);
                    wr.flush();
                    wr.close();

                    int responseCode = con.getResponseCode();
                    setHttpResponseCode(responseCode);
                    System.out.println("nSending 'POST' request to URL : " + url);
                    System.out.println("Post Data : " + dataToPost);
                    System.out.println("Response Code : " + responseCode);

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

    // Post command with authorization token
    public void sendPostCommand(final String httpCommand, final String dataToPost, final String idToken, final CountDownLatch latch){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final ConnectionSettings connectionSettings = new ConnectionSettings();
                String url = connectionSettings.getHostIP() + ":" + connectionSettings.getHostPort() + "/" + httpCommand; // URL to call
                OutputStream out = null;

                try {
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // Setting basic post request
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type","application/json");
                    con.setRequestProperty("Authorization","Bearer " + idToken);

                    // Send post request
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(dataToPost);
                    wr.flush();
                    wr.close();

                    int responseCode = con.getResponseCode();
                    setHttpResponseCode(responseCode);
                    System.out.println("nSending 'POST' request to URL : " + url);
                    System.out.println("Post Data : " + dataToPost);
                    System.out.println("Response Code : " + responseCode);

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

    // Put command with authorization token
    public void sendPutCommand(final String httpCommand, final String dataToPost, final String idToken, final CountDownLatch latch) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final ConnectionSettings connectionSettings = new ConnectionSettings();
                String url = connectionSettings.getHostIP() + ":" + connectionSettings.getHostPort() + "/" + httpCommand; // URL to call
                OutputStream out = null;

                try {
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // Setting basic post request
                    con.setRequestMethod("PUT");
                    con.setRequestProperty("Content-Type","application/json");
                    con.setRequestProperty("Authorization","Bearer " + idToken);

                    // Send post request
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(dataToPost);
                    wr.flush();
                    wr.close();

                    int responseCode = con.getResponseCode();
                    setHttpResponseCode(responseCode);
                    System.out.println("nSending 'PUT' request to URL : " + url);
                    System.out.println("Put Data : " + dataToPost);
                    System.out.println("Response Code : " + responseCode);

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

    // Delete command with authorization token
    public void sendDeleteCommand(final String httpCommand, final String idToken, final CountDownLatch latch) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final ConnectionSettings connectionSettings = new ConnectionSettings();
                String url = connectionSettings.getHostIP() + ":" + connectionSettings.getHostPort() + "/" + httpCommand; // URL to call
                OutputStream out = null;

                try {
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // Setting basic post request
                    con.setRequestMethod("DELETE");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Authorization", "Bearer " + idToken);

                    int responseCode = con.getResponseCode();
                    setHttpResponseCode(responseCode);
                    System.out.println("nSending 'DELETE' request to URL : " + url);
                    System.out.println("Response Code : " + responseCode);

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
