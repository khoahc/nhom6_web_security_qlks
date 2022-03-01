package com.nhom6.qlks.utils;

import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.annotation.HttpMethodConstraint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.util.Util;
import com.nhom6.qlks.hibernate.pojo.Booking;
import com.nhom6.qlks.hibernate.pojo.HoaDon;
import com.nhom6.qlks.hibernate.pojo.KhachHang;
import com.nhom6.qlks.hibernate.pojo.User;

import org.apache.commons.codec.binary.Hex;

public class Utils {
	public String strToMD5(String str) {
		String hashtext = null;
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(str.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			hashtext = bigInt.toString(16);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return hashtext;
	}
	
	public String checkRegister(User user) {
		String err_msg = null;
		
		if (user.getHoTen().length() == 0) {
			return err_msg = "Thiếu họ tên";
		}
		
		if (user.getGioiTinh() == null) {
			return err_msg = "Thiếu giới tính";
		}
		
		if (user.getCmnd().length() == 0) {
			return err_msg = "Thiếu số chứng mình nhân dân";
		}
		
		if (user.getEmail().length() == 0) {
			return err_msg = "Thiếu địa chỉ email";
		}
		
		if (user.getSdt().length() == 0) {
			return err_msg = "Thiếu số điện thoại";
		}
		
		if (user.getTenDangNhap().length() == 0) {
			return err_msg = "Thiếu tên username";
		}
		
		if (user.getMatKhau().length() == 0) {
			return err_msg = "Thiếu password";
		}
		
		return err_msg;
	}

	public static int getRentalDays(Date checkIn, Date checkOut) {
		int sub = 0;
		long difference_In_Time = checkOut.getTime() - checkIn.getTime(); 
		sub = (int) (difference_In_Time / (1000 * 60 * 60 * 24));
		return sub;
	}
	
	public static float calcTotalPriceBooking(Booking booking) {
		float donGia = booking.getPhong().getLoaiPhong().getDonGia();
		int soNgayThue = getRentalDays(booking.getCheckIn(), booking.getCheckOut());
		
		float totalPrice = donGia * soNgayThue;
		
		return totalPrice;
	}
	
	public static float calcTotalPriceBill(HoaDon hoaDon) {
		float totalPrice = 0;
		
		for (Booking bk : hoaDon.getBookings()) {
			totalPrice += calcTotalPriceBooking(bk);
		}
		
		return totalPrice;
	}
	
	public static String encode(String key, String data) throws Exception {
		  Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		  SecretKey secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
		  sha256_HMAC.init(secret_key);

		  return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
	}
	
	public static JSONObject parse(String responseBody) {
		return new JSONObject(responseBody);
	}
	
	public static JSONObject payByMomo(String totalPrice, String domain) {
		// domain = 'http://14.160.134.135:65001/'
		String endpoint = "https://test-payment.momo.vn/gw_payment/transactionProcessor";
		String partnerCode = "MOMOIQA420180417";
		String accessKey = "Q8gbQHeDesB2Xs0t";
		String serectkey = "PPuDXq1KowPT1ftR8DvlQTHhC03aul17";
		String orderInfo = "Thanh toán tiềm thuê phòng";
		String returnUrl = domain + "/momo/return";
		String notifyurl = domain + "/api/momo/notify";
		String amount = (totalPrice);
		String orderId = UUID.randomUUID().toString();
		String requestId = UUID.randomUUID().toString();
		System.out.println(orderId + " ..." + requestId);
		String requestType = "captureMoMoWallet";
		String extraData = "merchantName=;merchantId=";
		
		String rawSignature = "partnerCode=" + partnerCode + "&accessKey=" + accessKey + 
				"&requestId=" + requestId + "&amount=" + amount + 
				"&orderId=" + orderId + "&orderInfo=" + orderInfo + 
				"&returnUrl=" + returnUrl + "&notifyUrl=" + notifyurl + 
				"&extraData=" + extraData;
		
		System.out.println(rawSignature);
		
		try {
			String signature = encode(serectkey, rawSignature);
			System.out.println("signature: " + signature);
			var values = new HashMap<String, String>() {{
	            put("partnerCode", partnerCode);
	            put ("accessKey", accessKey);
	            put("requestId", requestId);
	            put("amount", amount);
	            put("orderId", orderId);
	            put("orderInfo", orderInfo);
	            put("returnUrl", returnUrl);
	            put("notifyUrl", notifyurl);
	            put("extraData", extraData);
	            put("requestType", requestType);
	            put("signature", signature);
	        }};

	        var objectMapper = new ObjectMapper();
	        try {
				String requestBody = objectMapper.writeValueAsString(values);
				
				HttpClient client = HttpClient.newHttpClient();
				HttpRequest request = (HttpRequest) HttpRequest.newBuilder()
						.uri(URI.create(endpoint))
						.POST(HttpRequest.BodyPublishers.ofString(requestBody))
						.build();
				
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				System.out.println(response.body());
				return parse(response.body());
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
	}
}
