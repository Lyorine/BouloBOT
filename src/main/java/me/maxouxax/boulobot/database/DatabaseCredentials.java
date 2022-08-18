package me.maxouxax.boulobot.database;

public class DatabaseCredentials {

    private String host;
    private String user;
    private String password;
    private String databaseName;
    private int port;

    public String toURI() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("jdbc:mariadb://")
                .append(host)
                .append(":")
                .append(port)
                .append("/")
                .append(databaseName);
        return stringBuilder.toString();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
