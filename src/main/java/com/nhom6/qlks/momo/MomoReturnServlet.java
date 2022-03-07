package com.nhom6.qlks.momo;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nhom6.qlks.hibernate.daos.HoaDonDao;

/**
 * Servlet implementation class MomoReturnServlet
 */
@WebServlet("/momo/return")
public class MomoReturnServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MomoReturnServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String errorCode = request.getParameter("errorCode");
		System.out.println(errorCode);
		String msg = request.getParameter("localMessage");
		System.out.println(msg);
		if (errorCode == "0") {
			
		} else {
			String orderId = request.getParameter("orderId");
			System.out.println("Delete: " + orderId);
			new HoaDonDao().deleteHoaDon(orderId);
		}
		request.setAttribute("msg", msg);
		response.sendRedirect(request.getContextPath().concat("/booking-history"));
//		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/customer/booking-history.jsp");
//		dispatcher.forward(request, response);
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String errorCode = request.getParameter("errorCode");
		System.out.println(errorCode);
	}

}
