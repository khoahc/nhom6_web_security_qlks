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
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.nhom6.qlks.hibernate.daos.BookingDao;
import com.nhom6.qlks.hibernate.pojo.Booking;
import com.nhom6.qlks.hibernate.pojo.User;

/**
 * Servlet implementation class CustomerBookingHistoryAPI
 */
@WebServlet("/aip/customer-booking-history")
public class CustomerBookingHistoryAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Gson gson;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomerBookingHistoryAPI() {
        super();
        // TODO Auto-generated constructor stub
        this.gson = new Gson();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		HttpSession session = request.getSession();
		User user =(User) session.getAttribute("user");
		
		String payStatus = request.getParameter("pay");
		
		BookingDao bookingDao = new BookingDao();
		List<Booking> bookings = bookingDao.getBookingsByIdUser(user.getId());
		
		List<Hashtable<String, Object>> rs = new ArrayList<Hashtable<String, Object>>();
		
		if (bookings != null) {
			bookings.forEach(x -> {
				Hashtable<String, Object> bk = new Hashtable<String, Object>();
				bk.put("idBooking", x.getIdBooking());
				bk.put("tenPhong", x.getPhong().getTenPhong());
				bk.put("soNguoi", x.getSoNguoi());
				bk.put("checkIn", x.getCheckIn());
				bk.put("checkOut", x.getCheckOut());
				bk.put("donGia", x.getPhong().getLoaiPhong().getDonGia());
				
				rs.add(bk);
			});
		}
		
		PrintWriter out = response.getWriter();
		String rsStr = this.gson.toJson(rs);
		out.write(rsStr);
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
