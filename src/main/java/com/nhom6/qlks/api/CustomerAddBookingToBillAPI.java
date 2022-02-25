package com.nhom6.qlks.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

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
		System.out.println(idBookingStr);
		
		Hashtable<String, Object> rs = new Hashtable<String, Object>();
		
		PrintWriter out = response.getWriter();
		String rsStr = this.gson.toJson(rs);
		out.write(rsStr);
		out.flush();
	}

}
