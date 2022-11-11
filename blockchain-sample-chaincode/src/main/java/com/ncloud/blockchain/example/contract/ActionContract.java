/*
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ncloud.blockchain.example.contract;

import static java.nio.charset.StandardCharsets.*;

import java.util.Iterator;
import java.util.StringJoiner;
import java.util.TimeZone;

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
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.ncloud.blockchain.example.data.Action;

@Contract(name = "ActionContract",
	info = @Info(title = "Action contract",
		description = "Action Contract",
		version = "1.0.0",
		license =
		@License(name = "Apache-2.0",
			url = ""),
		contact = @Contact(email = "tracking-sample@example.com",
			name = "tracking-sample",
			url = "http://tracking-sample.me")))
public class ActionContract implements ContractInterface {
	private static Log logger = LogFactory.getLog(ChaincodeBase.class);

	public ActionContract() {

	}

	@Transaction()
	public boolean checkExists(Context ctx, String productId) {
		byte[] buffer = ctx.getStub().getState(productId);
		return (buffer != null && buffer.length > 0);
	}

	@Transaction()
	public void initialize(Context ctx, String id, String productId, String facilityId, String action) {
		if (checkExists(ctx, id)) {
			logger.warn("product id already registered");
			throw new ChaincodeException("Product already produced. you can add action");
		}

		Action result = new Action();
		result.setProductId(productId);
		result.setFacilityId(facilityId);
		result.setAction(action);
		result.setProcessTime(DateTime.now(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Asia/Seoul"))).toString("yyyy-MM-dd HH:mm:ss"));


		ctx.getStub().putState(id, result.toJSONString().getBytes(UTF_8));
	}

	@Transaction()
	public Action getCurrentAction(Context ctx, String id) {
		boolean exists = checkExists(ctx, id);
		if (!exists) {
			logger.warn("product id not found");
			throw new ChaincodeException("The Product history does not exist");
		}

		Action newAsset = Action.fromJSONString(new String(ctx.getStub().getState(id), UTF_8));
		return newAsset;
	}

	@Transaction()
	public void registerAction(Context ctx, String id, String productId, String facilityId, String action) {
		boolean exists = checkExists(ctx, id);
		if (!exists) {
			logger.warn("product history not found");
			throw new ChaincodeException("The Product history does not exist");
		}
		Action result = new Action();
		result.setProductId(productId.toLowerCase());
		result.setFacilityId(facilityId.toLowerCase());
		result.setAction(action);
		result.setProcessTime(DateTime.now(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Asia/Seoul"))).toString("yyyy-MM-dd HH:mm:ss"));

		ctx.getStub().putState(id.toLowerCase(), result.toJSONString().getBytes(UTF_8));
	}

	@Transaction()
	public String getActionHistory(Context ctx, String id) {
		boolean exists = checkExists(ctx, id);
		if (!exists) {
			logger.warn("product history not found");
			throw new ChaincodeException("The product history does not exist");
		}

		StringJoiner sb = new StringJoiner(",", "[", "]");
		try {
			QueryResultsIterator<KeyModification> history = ctx.getStub().getHistoryForKey(id);

			if (history == null) {
				logger.warn("product history not found");
				throw new ChaincodeException("There is no history for this product.");
			}

			Iterator<KeyModification> iterator = history.iterator();
			while (iterator.hasNext()) {
				String iteratorValue = iterator.next().getStringValue();
				sb.add(iteratorValue);
			}
			history.close();
		} catch (Exception e) {
			logger.error("error occurred while get action history e=", e);
			throw new ChaincodeException("An error occurred while get history. exception=", e);
		}
		return sb.toString();
	}
}
