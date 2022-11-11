/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ncloud.blockchain.example.data;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

import com.google.gson.Gson;

@DataType()
public class Product {

    private final Gson gson = new Gson();

    private String id;

    @Property()
    private String name;

    @Property()
    private String category;

    @Property()
    private String facilityId;

    public Product(){
    }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String toJSONString() {
        return gson.toJson(this);
    }

    public static Product fromJSONString(String json) {
        Product facility = new Product();
        facility.setName(new JSONObject(json).getString("name"));
        facility.setCategory(new JSONObject(json).getString("category"));
        facility.setFacilityId(new JSONObject(json).getString("facilityId"));

        return facility;
    }
}
