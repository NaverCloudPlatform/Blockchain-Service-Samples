/*
 * SPDX-License-Identifier: Apache License 2.0
 */

package com.ncloud.blockchain.example;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.ncloud.blockchain.example.contract.FacilityContract;
import com.ncloud.blockchain.example.data.Facility;

public final class FacilityContractTest {

	@Nested
	class AssetExists {
		@Test
		public void noProperAsset() {

			FacilityContract contract = new FacilityContract();
			Context ctx = mock(Context.class);
			ChaincodeStub stub = mock(ChaincodeStub.class);
			when(ctx.getStub()).thenReturn(stub);

			when(stub.getState("blockchain-store")).thenReturn(new byte[] {});
			boolean result = contract.checkExists(ctx, "blockchain-store");

			assertFalse(result);
		}

		@Test
		public void assetExists() {

			FacilityContract contract = new FacilityContract();
			Context ctx = mock(Context.class);
			ChaincodeStub stub = mock(ChaincodeStub.class);
			when(ctx.getStub()).thenReturn(stub);

			when(stub.getState("blockchain-store")).thenReturn(new byte[] {42});
			boolean result = contract.checkExists(ctx, "blockchain-store");

			assertTrue(result);

		}

		@Test
		public void noKey() {
			FacilityContract contract = new FacilityContract();
			Context ctx = mock(Context.class);
			ChaincodeStub stub = mock(ChaincodeStub.class);
			when(ctx.getStub()).thenReturn(stub);

			when(stub.getState("blockchain-store")).thenReturn(null);
			boolean result = contract.checkExists(ctx, "blockchain-store");

			assertFalse(result);

		}

	}

	@Nested
	class AssetCreates {

		@Test
		public void newAssetCreate() {
			FacilityContract contract = new FacilityContract();
			Context ctx = mock(Context.class);
			ChaincodeStub stub = mock(ChaincodeStub.class);
			when(ctx.getStub()).thenReturn(stub);

			contract.registerFacility(ctx, "facility:blockchain-store", "blockchain-store", "경기도 성남시 분당구", "잡화점", "010-0000-0000", "test@test.com");
			String json = "{\"name\":\"blockchain-store\",\"location\":\"경기도 성남시 분당구\",\"category\":\"잡화점\",\"telNo\":\"010-0000-0000\",\"emailAddress\":\"test@test.com\"}";

			verify(stub).putState("facility:blockchain-store", json.getBytes(UTF_8));
		}

		@Test
		public void newAssetCreateValidation() {
			FacilityContract contract = new FacilityContract();
			Context ctx = mock(Context.class);
			ChaincodeStub stub = mock(ChaincodeStub.class);
			when(ctx.getStub()).thenReturn(stub);

			Exception thrown = assertThrows(RuntimeException.class, () -> {
				contract.registerFacility(ctx, "blockchain-store:test", "blockchain-store", "경기도 성남시 분당구", "잡화점", "010-0000-0000", "test@test.com");
			});

			assertEquals(thrown.getMessage(), "The facility id must start with 'facility:{name}'. please check.");
		}

		@Test
		public void alreadyExists() {
			FacilityContract contract = new FacilityContract();
			Context ctx = mock(Context.class);
			ChaincodeStub stub = mock(ChaincodeStub.class);
			when(ctx.getStub()).thenReturn(stub);

			when(stub.getState("facility:blockchain-store:test")).thenReturn(new byte[] {42});

			Exception thrown = assertThrows(RuntimeException.class, () -> {
				contract.registerFacility(ctx, "facility:blockchain-store:test", "blockchain-store", "경기도 성남시 분당구", "잡화점", "010-0000-0000", "test@test.com");
			});

			assertEquals(thrown.getMessage(), "The facility facility:blockchain-store:test already exists");
		}

	}

	@Test
	public void assetRead() {
		FacilityContract contract = new FacilityContract();
		Context ctx = mock(Context.class);
		ChaincodeStub stub = mock(ChaincodeStub.class);
		when(ctx.getStub()).thenReturn(stub);

		Facility facility = new Facility();
		facility.setName("blockchain-store");
		facility.setLocation("경기도 성남시 분당구");
		facility.setCategory("잡화점");
		facility.setTelNo("010-0000-0000");
		facility.setEmailAddress("test@test.com");

		String json = facility.toJSONString();
		when(stub.getState("blockchain-store")).thenReturn(json.getBytes(StandardCharsets.UTF_8));

		Facility returnedAsset = contract.getFacility(ctx, "blockchain-store");
		assertEquals(returnedAsset.getName(), facility.getName());
	}

	@Nested
	class AssetUpdates {
		@Test
		public void updateExisting() {
			FacilityContract contract = new FacilityContract();
			Context ctx = mock(Context.class);
			ChaincodeStub stub = mock(ChaincodeStub.class);
			when(ctx.getStub()).thenReturn(stub);
			when(stub.getState("blockchain-store")).thenReturn(new byte[] {42});

			String json = "{\"name\":\"blockchain-store\",\"location\":\"경기도 성남시 분당구\",\"category\":\"잡화점\",\"telNo\":\"010-0000-0000\",\"emailAddress\":\"test@test.com\"}";

			contract.updateFacility(ctx, "blockchain-store", "blockchain-store", "경기도 성남시 분당구", "잡화점", "010-0000-0000", "test@test.com");

			verify(stub).putState("blockchain-store", json.getBytes(UTF_8));
		}

		@Test
		public void updateMissing() {
			FacilityContract contract = new FacilityContract();
			Context ctx = mock(Context.class);
			ChaincodeStub stub = mock(ChaincodeStub.class);
			when(ctx.getStub()).thenReturn(stub);

			when(stub.getState("blockchain-store")).thenReturn(null);

			Exception thrown = assertThrows(RuntimeException.class, () -> {
				contract.updateFacility(ctx, "blockchain-store", "blockchain-store", "경기도 성남시 분당구", "잡화점", "010-0000-0000", "test@test.com");
			});

			assertEquals(thrown.getMessage(), "The facility blockchain-store does not exist");
		}

	}

	@Test
	public void assetDelete() {
		FacilityContract contract = new FacilityContract();
		Context ctx = mock(Context.class);
		ChaincodeStub stub = mock(ChaincodeStub.class);
		when(ctx.getStub()).thenReturn(stub);
		when(stub.getState("blockchain-store")).thenReturn(null);

		Exception thrown = assertThrows(RuntimeException.class, () -> {
			contract.deleteFacility(ctx, "blockchain-store");
		});

		assertEquals(thrown.getMessage(), "The facility blockchain-store does not exist");
	}

}
