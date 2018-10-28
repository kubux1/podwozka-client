package settings;

public class ConnectionSettings {
   //private final String hostIP = "http://80.211.235.140";
    private final String hostIP = "http://192.168.1.107";
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
