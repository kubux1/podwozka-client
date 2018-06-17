package settings;

public class ConnectionSettings {
    private final String hostIP = "http://192.168.0.41";
    private String hostPort = "8080";

    public ConnectionSettings() {
    }

    public String getHostPort() {
        return hostPort;
    }

    public String getHostIP() {
        return hostIP;
    }
}
