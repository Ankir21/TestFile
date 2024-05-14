package com.example.Test;

public class ClientData {
    private String name;
    private int balance;
    private long internetTraffic;
    private String internetTrafficFormatted;
    private String timeOfDay;

    public ClientData(String name, int balance, long internetTraffic) {
        this.name = name;
        this.balance = balance;
        this.internetTraffic = internetTraffic;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public long getInternetTraffic() {
        return internetTraffic;
    }

    public String getInternetTrafficFormatted() {
        return internetTrafficFormatted;
    }

    public void setInternetTrafficFormatted(String internetTrafficFormatted) {
        this.internetTrafficFormatted = internetTrafficFormatted;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }
}


