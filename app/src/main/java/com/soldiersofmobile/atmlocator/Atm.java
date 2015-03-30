package com.soldiersofmobile.atmlocator;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "atms")
public class Atm {

    public static final String ADDRESS = "address";

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(canBeNull = false)
    private double latitiude;

    @DatabaseField(canBeNull = false)
    private double longitude;

    @DatabaseField(columnName = ADDRESS, canBeNull = true)
    private String address;

    @DatabaseField(foreign = true)
    private Bank bank;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitiude() {
        return latitiude;
    }

    public void setLatitiude(double latitiude) {
        this.latitiude = latitiude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
}
