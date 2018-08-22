package podwozka.podwozka.entity;

import settings.ConnectionSettings;

import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;

public class Connection {
    private URL url;
    private int HttpResponseCode;
    private InputStream inputStream;

    private void setHttpResponseCode(int HttpResponseCode) {
        this.HttpResponseCode = HttpResponseCode;
    }

    private int getHttpResponseCode() {
        return this.HttpResponseCode;
    }

    private void setInputStream(InputStream InputStream) {
        this.inputStream = InputStream;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public Connection(){ }

    public int sendCommand(String httpCommand, String httpMethod) {
        try {
            ConnectionSettings connectionSettings = new ConnectionSettings();
            this.url = new URL(connectionSettings.getHostIP() + ":" + connectionSettings.getHostPort() + "/" + httpCommand);
            HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
            connection.setRequestMethod(httpMethod);
            setHttpResponseCode(connection.getResponseCode());
            if (getHttpResponseCode() == 200) {
                setInputStream(connection.getInputStream());
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getHttpResponseCode();
    }
}
