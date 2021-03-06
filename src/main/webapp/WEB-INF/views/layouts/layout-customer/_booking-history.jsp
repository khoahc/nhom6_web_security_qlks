<%@page import="com.nhom6.qlks.hibernate.pojo.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<div class="container" style="min-height:500px">
<h1 class="text-center mt-5 text-info display-4 font-weight-bold">Lịch sử đặt phòng</h1>

<ul class="nav nav-tabs">
	<li class="nav-item">							
		<a class="nav-link active" href="javascript:void(0);" onclick="getUnpaidBookingHistory()">Chưa thanh toán</a>
	</li>								
	
	<li class="nav-item">
		<a class="nav-link" href="javascript:void(0);" onclick="getPaidBookingHistory()">Đã thanh toán</a>
	</li>
													   
</ul>

<div class="table-responsive">
	<table class="table mt-5 h3 ">
	<thead>
		<tr>
	    	<th>
	    		<!-- <input type="checkbox"> -->
	    	</th>
	        <th>Mã booking</th>
	        <th>Phòng</th>
	        <th>Số người</th>
	        <th>Check-in</th>
	        <th>Check-out</th>
	        <th>Đơn giá</th>
	        <th></th>
	    </tr>
	</thead>
	<tbody id="tb-booking-history">
	    <%-- <%User user = (User) session.getAttribute("user");%>
		<% if (user != null) { %>
			<c:choose>
				<c:when test="${numBooking == 0}">
			        <tr>
			            <td colspan="7" class="text-center">Không có booking nào</td>
			        </tr>
		   		 </c:when>
		   		 <c:otherwise>
			   		<c:forEach items="${bookings}" var="booking">				       
				        <tr id="booking-id">
				        	<td>
				        		<input type="checkbox" class="form-check-input" value="">
				        	</td>
				            <td> <c:out value = "${booking.getIdBooking()}"/></td>				         
				            <td> <c:out value = "${booking.getPhong().getTenPhong()}"/></td>				         
				            <td> <c:out value = "${booking.getSoNguoi()}"/></td>				         
				            <td><fmt:formatDate pattern = "yyyy-MM-dd" value="${booking.getCheckIn()}"/></td>			         
				            <td><fmt:formatDate pattern = "yyyy-MM-dd" value="${booking.getCheckOut()}"/></td>			        
				            
				        	<td>
				        		<fmt:setLocale value = "vi_VN"/>
				        		<fmt:formatNumber value="${booking.getPhong().getLoaiPhong().getDonGia()}" type="currency"  maxFractionDigits = "0"/>
				        	</td>
				        </tr>
					</c:forEach> 
		   		 </c:otherwise>			
			</c:choose>
			
	    <% } %> --%>
	</tbody>
	
	<script>
		function formatMoney(number) {
			let numStr = number.toString();
			let len = numStr.length;
			let n = len/3 - 1;
			let numStrRev = numStr.split("").reverse().join("");
			for (var i = 0; i < n; i++) {
				let j = 3 + i*4;
			    numStrRev = numStrRev.slice(0, j) + "," + numStrRev.slice(j);
			}
			
			return numStrRev = numStrRev.split("").reverse().join("");
		}
	
		function addBookingToBill(idBooking) {
			let url = "<%=request.getContextPath()%>/aip/customer-add-booking-to-bill";
		    
		    $.post(
		    	url,
		    	{
		    		idBooking: idBooking
		    	},
		    	function(data) {
		    		console.log(data);
		    		document.getElementById("total-price").innerText = formatMoney(data.totalPrice);
		    	}
		    )
		}
	
		function getBookingHistory(statusPay) {
			let table = document.getElementById("tb-booking-history");
		    table.innerHTML = '';
		    
		    let url = "<%=request.getContextPath()%>/aip/customer-booking-history?pay=" + statusPay;
		    
			$.get(
					url,
					function(data){
						if (data.length == 0) {
							table.innerHTML = '<tr><td colspan="7" class="text-center">Không có booking nào</td></tr>';
						}
					    for (let i = 0; i < data.length; i++) {
					        let r = table.insertRow(i);
					        
					        let checkBox = document.createElement('input');
					        checkBox.type = 'checkbox';
					        checkBox.addEventListener("click", () => {
					        	addBookingToBill(data[i].idBooking);
					        });
							if (statusPay == 'paid') {
								checkBox.hidden = true;
							}
					        
							let count = 0;
							
					        r.insertCell(count++).appendChild(checkBox);
					        r.insertCell(count++).innerText = data[i].idBooking;
					        r.insertCell(count++).innerText = data[i].tenPhong;
					        r.insertCell(count++).innerText = data[i].soNguoi;
					        r.insertCell(count++).innerText = data[i].checkIn;
					        r.insertCell(count++).innerText = data[i].checkOut;
					        r.insertCell(count++).innerText = formatMoney(data[i].donGia) + " VNĐ";
					    }
					});
		}
		
		function getPaidBookingHistory() {
			document.querySelector("body > div > div > ul > li:nth-child(1) > a").classList.remove("active");
			document.querySelector("body > div > div > ul > li:nth-child(2) > a").classList.add("active");
			getBookingHistory('paid');
		}
		
		function getUnpaidBookingHistory() {
			document.querySelector("body > div > div > ul > li:nth-child(1) > a").classList.add("active");
			document.querySelector("body > div > div > ul > li:nth-child(2) > a").classList.remove("active");
			getBookingHistory('unpaid');
		}
		
		function payBookingsOnline() {
			let table = document.getElementById("tb-booking-history");
		    
			let idBookings = [];
			
		    table.childNodes.forEach((item, index, arr) => {
		    	if (item.childNodes[0].childNodes[0].checked == true) {
		    		idBookings.push(item.childNodes[1].innerText)
		    	}
		    });
		    
		    let url = "<%=request.getContextPath()%>/aip/customer-pay-bookings-online";
		    
		    $.post(
		    	url,
		    	{
		    		data: idBookings
		    	},
		    	function(data) {
		    		console.log(data);
		    		if (data.statusCode == 200) {
		    			window.open(data.payUrl);
		    		} else {
		    			alert(data.msg);
		    		}
		    	}
		    )
		}
		
		getUnpaidBookingHistory();
		
	</script>
	    
	</table>
</div>
<p>Tổng tiền: <strong id="total-price">0</strong> <strong>VNĐ</strong></p>
<button onclick="payBookingsOnline()" class="btn">Thanh toán</button>
<br><br>
<p id="notify">${msg}</p>

</div>
