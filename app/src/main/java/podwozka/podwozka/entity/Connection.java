package podwozka.podwozka.entity;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Connection {
    private URL url;
    private int HttpResponseCode;
    private InputStream inputStream;

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

    public Connection(){ }

    public int sendGetCommand(final String httpCommand, final CountDownLatch latch) {
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

    public String sendPostCommand(String httpCommand, List<NameValuePair> object) {
        String response = null;
        HttpClient httpclient = new DefaultHttpClient();
        ConnectionSettings connectionSettings = new ConnectionSettings();
        HttpPost httppost = new HttpPost(connectionSettings.getHostIP() + ":" + connectionSettings.getHostPort() + "/" + httpCommand);

        try {
            httppost.setEntity(new UrlEncodedFormEntity(object));
            // Execute HTTP Post Request
            HttpResponse httpResponse = httpclient.execute(httppost);
            setHttpResponseCode(httpResponse.getStatusLine().getStatusCode());
            if(getHttpResponseCode() == 200) {
                HttpEntity resEntityGet = httpResponse.getEntity();
                if(resEntityGet !=null ){
                    // Response is in JSON
                    response = EntityUtils.toString(resEntityGet);
                }
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
