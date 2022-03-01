package com.nhom6.qlks.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.nhom6.qlks.hibernate.daos.HoaDonDao;
import com.nhom6.qlks.hibernate.pojo.Booking;
import com.nhom6.qlks.hibernate.pojo.HoaDon;
import com.nhom6.qlks.hibernate.pojo.User;
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
		
		HttpSession session = request.getSession(true);
		List<Booking> billDetail = (List<Booking>) session.getAttribute("billDetail");
		
		User user = (User) session.getAttribute("user");
		
		HoaDonDao hoaDonDao = new HoaDonDao();
		HoaDon hoaDon = new HoaDon();
		hoaDon.setUser(user);
		
		Date currentDate = new Date();
		hoaDon.setNgayTao(currentDate);

		Hashtable<String, Object> rs = new Hashtable<String, Object>();
		JSONObject json = hoaDonDao.insertHoaDonOnline(hoaDon, billDetail, getBaseUrl(request));
		if ((int)json.get("errorCode") == 0) {
			rs.put("statusCode", 200);
			rs.put("payUrl", json.get("payUrl"));
		} else {
			rs.put("statusCode", 404);
			rs.put("msg", "Có lỗi xảy ra, vui lòng thử lại");
		};
		
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
