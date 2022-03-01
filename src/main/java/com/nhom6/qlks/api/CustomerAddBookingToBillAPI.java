package com.nhom6.qlks.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.List;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.nhom6.qlks.hibernate.daos.BookingDao;
import com.nhom6.qlks.hibernate.pojo.Booking;
import com.nhom6.qlks.utils.Utils;

/**
 * Servlet implementation class CustomerAddBookingToBillAPI
 */
@WebServlet("/aip/customer-add-booking-to-bill")
public class CustomerAddBookingToBillAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Gson gson;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomerAddBookingToBillAPI() {
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
		
		String idBookingStr = request.getParameter("idBooking");
		int idBooking = Integer.parseInt(idBookingStr);
		
		HttpSession session = request.getSession(true);
		
		List<Booking> billDetail = (List<Booking>) session.getAttribute("billDetail");
		
		Hashtable<String, Object> rs = new Hashtable<String, Object>();
		
		if (checkBookingInBillDetail(billDetail, idBooking)) {
			System.out.println(billDetail);
			 
	        // Creating iterator object
	        Iterator itr = billDetail.iterator();
	 
	        // Holds true till there is single element
	        // remaining in the object
	        while (itr.hasNext()) {
	 
	            // Remove elements smaller than 10 using
	            // Iterator.remove()
	        	Booking item = (Booking) itr.next();
	            if (item.getIdBooking() == idBooking)
	                itr.remove();
	        }
	 
	        // Printing the updated ArrayList
	        System.out.println(billDetail);
		} else {
			Booking booking = new BookingDao().getBookingById(idBooking);
			billDetail.add(booking);
		}
		
		rs.put("status", 200);
		
		float totalPriceBill = 0;
		
		for (Booking bk : billDetail) {
			totalPriceBill += Utils.calcTotalPriceBooking(bk);
		}
		
		rs.put("totalPrice", totalPriceBill);
		
		PrintWriter out = response.getWriter();
		String rsStr = this.gson.toJson(rs);
		out.write(rsStr);
		out.flush();
	}
	
	private boolean checkBookingInBillDetail(List<Booking> billDetail, int idBooking) {
//		if (billDetail == null) {
//			return false;
//		}
		for (Booking bk : billDetail) {
			if (bk.getIdBooking() == idBooking) {
				return true;
			}
		}
		
		return false;
	}

}
