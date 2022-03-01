package com.nhom6.qlks.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.nhom6.qlks.utils.Utils;

import com.google.gson.Gson;

/**
 * Servlet implementation class CustomerPayBookingsOnline
 */
@WebServlet("/aip/customer-pay-bookings-online")
public class CustomerPayBookingsOnline extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Gson gson;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomerPayBookingsOnline() {
        super();
        // TODO Auto-generated constructor stub
        this.gson = new Gson();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		String idBookingStr = request.getParameter("idBookings");
		System.out.println("idBookings: " + idBookingStr);
		
		Hashtable<String, Object> rs = new Hashtable<String, Object>();
		
		String totalPrice = "500000";
		
		JSONObject json = Utils.payByMomo(totalPrice, getBaseUrl(request));
		if ((int)json.get("errorCode") == 0) {
			rs.put("statusCode", 200);
			rs.put("payUrl", json.get("payUrl"));
		} else {
			rs.put("statusCode", 404);
			rs.put("msg", "Có lỗi xảy ra, vui lòng thử lại");
		};
		System.out.println("domain: " + getBaseUrl(request));
		
		PrintWriter out = response.getWriter();
		String rsStr = this.gson.toJson(rs);
		out.write(rsStr);
		out.flush();
	}
	
	public static String getBaseUrl(HttpServletRequest request) {
	    String scheme = request.getScheme() + "://";
	    String serverName = request.getServerName();
	    String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
	    String contextPath = request.getContextPath();
	    return scheme + serverName + serverPort + contextPath;
	  }

}
