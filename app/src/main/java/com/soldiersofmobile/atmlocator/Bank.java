package com.soldiersofmobile.atmlocator;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "banks")
public class Bank {


    @DatabaseField(id = true)
    private String name;

    @DatabaseField(canBeNull = false)
    private String phone;

    public Bank() {

    }

    public Bank(String bankName, String bankPhone) {
        name = bankName;
        phone = bankPhone;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
