/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ncloud.blockchain.example.data;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

import com.google.gson.Gson;

@DataType()
public class Action {

    private final static Gson gson = new Gson();

    @Property()
    private String productId;

    @Property()
    private String facilityId;

    @Property()
    private String action;

    @Property
    private String processTime;

    public Action(){
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String toJSONString() {
        return gson.toJson(this);
    }

    public void setProcessTime(String processTime) { this.processTime = processTime; }

    public String getProcessTime() { return processTime; }

    public static Action fromJSONString(String json) {
        Action action = new Action();
        action.setProductId(new JSONObject(json).getString("productId"));
        action.setFacilityId(new JSONObject(json).getString("facilityId"));
        action.setAction(new JSONObject(json).getString("action"));
        action.setProcessTime(new JSONObject(json).getString("processTime"));

        return action;
    }
}
