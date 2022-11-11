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
import com.ncloud.blockchain.example.data.Product;

@Contract(name = "ProductContract",
	info = @Info(title = "Product contract",
		description = "Product Contract",
		version = "0.0.1",
		license =
		@License(name = "Apache-2.0",
			url = ""),
		contact = @Contact(email = "tracking-sample@example.com",
			name = "tracking-sample",
			url = "http://tracking-sample.me")))
public class ProductContract implements ContractInterface {
	private static Log logger = LogFactory.getLog(ChaincodeBase.class);

    private final static Gson gson = new Gson();

	private final FacilityContract facilityContract = new FacilityContract();
	private final ActionContract actionContract = new ActionContract();

	public ProductContract() {

	}

	@Transaction()
	public boolean checkExists(Context ctx, String productId) {
		byte[] buffer = ctx.getStub().getState(productId);
		return (buffer != null && buffer.length > 0);
	}

	@Transaction()
	public void registerProduct(Context ctx, String productId, String name, String category, String producedFacilityId) {
		boolean exists = checkExists(ctx, productId);

		if (exists) {
			logger.warn("product id already registered");
			throw new ChaincodeException("The product " + productId + " already exists");
		}

		if (!facilityContract.checkExists(ctx, producedFacilityId)) {
			logger.warn("facility id not found");
			throw new ChaincodeException("Produced facility id not found. register facility first.");
		}

		if (!productId.startsWith("product:" + name)) {
			logger.warn("product id validation failed.");
			throw new ChaincodeException("The product id must start with 'product:{name}'. please check.");
		}

		Product product = new Product();
		product.setName(name.toLowerCase());
		product.setCategory(category);
		product.setFacilityId(producedFacilityId);

		ctx.getStub().putState(productId.toLowerCase(), product.toJSONString().getBytes(UTF_8));
		actionContract.initialize(ctx, "history:" + productId, productId, producedFacilityId, "produce");
	}

	@Transaction()
	public Product getProduct(Context ctx, String productId) {
		boolean exists = checkExists(ctx, productId);
		if (!exists) {
			logger.warn("product id not found");
			throw new ChaincodeException("The product " + productId + " does not exist");
		}

		Product newAsset = Product.fromJSONString(new String(ctx.getStub().getState(productId), UTF_8));
		return newAsset;
	}

	@Transaction()
	public String getAllProduct(Context ctx) {
		List<Product> result = new ArrayList();

		try {
			QueryResultsIterator<KeyValue> state = ctx.getStub().getStateByRange("a", "z");
			Iterator<KeyValue> iterator = state.iterator();

			while (iterator.hasNext()) {
				KeyValue keyValue = iterator.next();

				if (keyValue.getKey().startsWith("product:")) {
					Product product = new Product();

					JsonElement element = JsonParser.parseString(keyValue.getStringValue());
					JsonObject object = element.getAsJsonObject();

					product.setId(keyValue.getKey());
					product.setName(object.get("name").getAsString());
					product.setCategory(object.get("category").getAsString());

					result.add(product);
				}
			}

			return gson.toJson(result);
		} catch (Exception e) {
			throw new ChaincodeException("Error during get all product list. exception=", e);
		}
	}

	@Transaction()
	public int getProductCount(Context ctx) {
		int count = 0;

		try {
			QueryResultsIterator<KeyValue> state = ctx.getStub().getStateByRange("product:0", "product:z");
			Iterator<KeyValue> iterator = state.iterator();

			while (iterator.hasNext()) {
				KeyValue keyValue = iterator.next();

				if (keyValue.getKey().startsWith("product:")) {
					count++;
				}
			}
			
			return count;
		} catch (Exception e) {
			throw new ChaincodeException("Error during get product count. exception=", e);
		}
	}
}
