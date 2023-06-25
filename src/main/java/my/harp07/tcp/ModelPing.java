package my.harp07.tcp;


public class ModelPing {
    
    private double port;
    private double time;
    private String info;
    private String ip;
    private String tip; 
    private String cloud; 
    private String state; 
    private String snmpState;

    public ModelPing() {
    }

    public double getPort() {
        return port;
    }

    public void setPort(double port) {
        this.port = port;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSnmpState() {
        return snmpState;
    }

    public void setSnmpState(String snmpState) {
        this.snmpState = snmpState;
    }
   
}
