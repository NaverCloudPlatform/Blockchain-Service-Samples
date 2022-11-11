/*
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ncloud.blockchain.example.contract;

import static java.nio.charset.StandardCharsets.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ncloud.blockchain.example.data.Facility;

@Contract(name = "FacilityContract",
	info = @Info(title = "Facility contract",
		description = "Facility Contract",
		version = "0.0.1",
		license =
		@License(name = "Apache-2.0",
			url = ""),
		contact = @Contact(email = "tracking-sample@example.com",
			name = "tracking-sample",
			url = "http://tracking-sample-example.com")))
public class FacilityContract implements ContractInterface {
	private static Log logger = LogFactory.getLog(ChaincodeBase.class);

	private final static Gson gson = new Gson();

	public FacilityContract() {

	}

	@Transaction()
	public boolean checkExists(Context ctx, String productId) {
		byte[] buffer = ctx.getStub().getState(productId);
		return (buffer != null && buffer.length > 0);
	}

	@Transaction()
	public void registerFacility(Context ctx, String facilityId, String name, String location, String category, String telNo, String emailAddress) {
		boolean exists = checkExists(ctx, facilityId);
		if (exists) {
			logger.warn("facility already exists");
			throw new ChaincodeException("The facility " + facilityId + " already exists");
		}

		if (!facilityId.startsWith("facility:" + name)) {
			logger.warn("facility id validation failed.");
			throw new ChaincodeException("The facility id must start with 'facility:{name}'. please check.");
		}

		Facility facility = new Facility();
		facility.setName(name.toLowerCase());
		facility.setLocation(location);
		facility.setCategory(category);
		facility.setTelNo(telNo);
		facility.setEmailAddress(emailAddress);

		ctx.getStub().putState(facilityId.toLowerCase(), facility.toJSONString().getBytes(UTF_8));
	}

	@Transaction()
	public Facility getFacility(Context ctx, String facilityId) {
		boolean exists = checkExists(ctx, facilityId);
		if (!exists) {
			logger.warn("facility does not exist");
			throw new ChaincodeException("The facility " + facilityId + " does not exist");
		}

		Facility newAsset = Facility.fromJSONString(new String(ctx.getStub().getState(facilityId), UTF_8));
		return newAsset;
	}

	@Transaction()
	public String getAllFacility(Context ctx) {
		List<Facility> result = new ArrayList<>();

		try {
			QueryResultsIterator<KeyValue> state = ctx.getStub().getStateByRange("a", "z");
			Iterator<KeyValue> iterator = state.iterator();

			while (iterator.hasNext()) {
				KeyValue keyValue = iterator.next();

				if (keyValue.getKey().startsWith("facility:")) {
					Facility facility = new Facility();

					JsonElement element = JsonParser.parseString(keyValue.getStringValue());
					JsonObject object = element.getAsJsonObject();

					facility.setId(keyValue.getKey());
					facility.setName(object.get("name").getAsString());
					facility.setCategory(object.get("category").getAsString());

					result.add(facility);
				}
			}

			return gson.toJson(result);
		} catch (Exception e) {
			throw new ChaincodeException("Error during get all facility list. exception=", e);
		}
	}


	@Transaction()
	public int getFacilityCount(Context ctx) {
		int count = 0;

		try {
			QueryResultsIterator<KeyValue> state = ctx.getStub().getStateByRange("a", "z");
			Iterator<KeyValue> iterator = state.iterator();

			while (iterator.hasNext()) {
				KeyValue keyValue = iterator.next();

				if (keyValue.getKey().startsWith("facility:")) {
					count++;
				}
			}
			
			return count;
		} catch (Exception e) {
			throw new ChaincodeException("Error during get facility count. exception=", e);
		}
	}

	@Transaction()
	public void updateFacility(Context ctx, String facilityId, String name, String location, String category, String telNo, String emailAddress) {
		boolean exists = checkExists(ctx, facilityId);
		if (!exists) {
			logger.warn("facility does not exist");
			throw new ChaincodeException("The facility " + facilityId + " does not exist");
		}
		Facility facility = new Facility();
		facility.setName(name);
		facility.setLocation(location);
		facility.setCategory(category);
		facility.setTelNo(telNo);
		facility.setEmailAddress(emailAddress);

		ctx.getStub().putState(facilityId, facility.toJSONString().getBytes(UTF_8));
	}

	@Transaction()
	public void deleteFacility(Context ctx, String facilityId) {
		boolean exists = checkExists(ctx, facilityId);
		if (!exists) {
			logger.warn("facility does not exist");
			throw new ChaincodeException("The facility " + facilityId + " does not exist");
		}
		ctx.getStub().delState(facilityId);
	}
}
