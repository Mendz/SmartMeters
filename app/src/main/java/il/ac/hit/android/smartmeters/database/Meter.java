package il.ac.hit.android.smartmeters.database;


public class Meter
{
    private String meterId;
    private String userId;
    private String address;
    private String kWh;

    public String getMeterId()
    {
        return meterId;
    }

    public void setMeterId(String meterId)
    {
        this.meterId = meterId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getkWh()
    {
        return kWh;
    }

    public void setkWh(String kWh)
    {
        this.kWh = kWh;
    }

    @Override
    public String toString()
    {
        return "Meter{" +
                "meterId='" + meterId + '\'' +
                ", userId='" + userId + '\'' +
                ", address='" + address + '\'' +
                ", kWh='" + kWh + '\'' +
                '}';
    }
}
