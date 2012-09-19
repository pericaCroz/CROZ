package hr.croz.servleti;

import hr.croz.podaci.UserData;

import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Hello extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
			doStuff(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		doStuff(request, response);
	}
	
	protected void doStuff(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
			UserData trenutni = (UserData)request.getSession().getAttribute("user");
			
			if(trenutni==null) {
				response.sendRedirect(request.getContextPath() + "/login");
				return;
			}
			
			List<String> text = new ArrayList<String>();
			
			InetAddress thisComputer = InetAddress.getLocalHost();
			text.add("IP: " + thisComputer.getHostAddress());
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			text.add("Date and time: " + dateFormat.format(date));
			
			if(trenutni.getRoles().contains("admin")) {
				text.add("Ovaj tekst vide administratori.");
			}
			if(trenutni.getRoles().contains("user")) {
				text.add("Ovaj tekst vide korisnici.");
			}
			
			request.setAttribute("text", text);
			
			request.getRequestDispatcher("/WEB-INF/pages/hello.jsp").forward(request, response);
			return;
	}

}
