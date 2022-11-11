/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ncloud.blockchain.example.data;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

import com.google.gson.Gson;

@DataType()
public class Facility {

    private final static Gson gson = new Gson();

    private String id;

    @Property()
    private String name;

    @Property()
    private String location;

    @Property()
    private String category;

    @Property()
    private String telNo;

    @Property()
    private String emailAddress;

    public Facility(){
    }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String toJSONString() {
        return gson.toJson(this);
    }

    public static Facility fromJSONString(String json) {
        Facility facility = new Facility();
        facility.setName(new JSONObject(json).getString("name"));
        facility.setLocation(new JSONObject(json).getString("location"));
        facility.setCategory(new JSONObject(json).getString("category"));
        facility.setTelNo(new JSONObject(json).getString("telNo"));
        facility.setEmailAddress(new JSONObject(json).getString("emailAddress"));

        return facility;
    }
}
