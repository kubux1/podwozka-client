package podwozka.podwozka.entity;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import settings.ConnectionSettings;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Connection extends AsyncTask<String, String, String> {
    private URL url;
    private int HttpResponseCode;
    private InputStream inputStream;
    private String response;


    private void setHttpResponseCode(int HttpResponseCode) {
        this.HttpResponseCode = HttpResponseCode;
    }
    public int getHttpResponseCode() {
        return this.HttpResponseCode;
    }

    private void setInputStream(InputStream InputStream) {
        this.inputStream = InputStream;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    private void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return this.response;
    }

    public Connection(){ }

    public int sendGetCommand(final String httpCommand, List<NameValuePair> object, final CountDownLatch latch) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ConnectionSettings connectionSettings = new ConnectionSettings();
                    URL url = new URL(connectionSettings.getHostIP() + ":" + connectionSettings.getHostPort() + "/" + httpCommand);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    setHttpResponseCode(connection.getResponseCode());
                    if (getHttpResponseCode() == 200) {
                        setInputStream(connection.getInputStream());
                    }
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

    /*
    public String sendPostCommand(final String httpCommand, final List<NameValuePair> object, final CountDownLatch latch) {
        final HttpClient httpclient = new DefaultHttpClient();
        final ConnectionSettings connectionSettings = new ConnectionSettings();
        final HttpPost httppost = new HttpPost(connectionSettings.getHostIP() + ":" + connectionSettings.getHostPort() + "/" + httpCommand);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

        try {
            httppost.setEntity(new UrlEncodedFormEntity(object));
            // Execute HTTP Post Request
            HttpResponse httpResponse = httpclient.execute(httppost);
            setHttpResponseCode(httpResponse.getStatusLine().getStatusCode());
            if(getHttpResponseCode() == 200) {
                HttpEntity resEntityGet = httpResponse.getEntity();
                if(resEntityGet !=null ){
                    // Response is in JSON
                    setResponse(EntityUtils.toString(resEntityGet));
                }
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        });
        thread.start();
        return getResponse();
    }
*/

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        final ConnectionSettings connectionSettings = new ConnectionSettings();
        String urlString = connectionSettings.getHostIP() + ":" + connectionSettings.getHostPort() + "/" + params[0]; // URL to call
        String data = params[1]; //data to post
        OutputStream out = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            out = new BufferedOutputStream(urlConnection.getOutputStream());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            out.close();

            urlConnection.connect();
            setHttpResponseCode(urlConnection.getResponseCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public int sendDeleteCommand(final String httpCommand, final CountDownLatch latch) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ConnectionSettings connectionSettings = new ConnectionSettings();
                    URL url = new URL(connectionSettings.getHostIP() + ":" + connectionSettings.getHostPort() + "/" + httpCommand);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    setHttpResponseCode(connection.getResponseCode());
                    if (getHttpResponseCode() == 200) {
                        setInputStream(connection.getInputStream());
                    }
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
}
