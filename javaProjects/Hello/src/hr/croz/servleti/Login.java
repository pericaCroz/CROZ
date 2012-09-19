package hr.croz.servleti;

import hr.croz.podaci.UserData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Login extends HttpServlet {

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
		
		String username = (String)request.getParameter("username");
		String password = (String)request.getParameter("password");
		
		if (username==null || username.isEmpty() || password == null || password.isEmpty()) {
			request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
			return;
		}
		
		UserData user = findUser(username, password);
		
		if (user == null) {
			request.setAttribute("error", "Authentication unsuccessful.");
			request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
			return;
		}
		
		request.getSession().setAttribute("user", user);
		response.sendRedirect(request.getContextPath() + "/hello");
		return;
		
	}

	private UserData findUser(String username, String password) {
		if (username.equals("Robert") && password.equals("Perica")) {
			List<String> uloge = new ArrayList<String>();
			uloge.add("admin");
			uloge.add("user");
			UserData u = new UserData("Robert", "Perica", uloge);
			return u;
		} else if (username.equals("Marko") && password.equals("Markovic")) {
			List<String> uloge = new ArrayList<String>();
			uloge.add("admin");
			UserData u = new UserData("Marko", "Markovic", uloge);
			return u;
		} else if (username.equals("Pero") && password.equals("Peric")) {
			List<String> uloge = new ArrayList<String>();
			uloge.add("user");
			UserData u = new UserData("Pero", "Peric", uloge);
			return u;
		}
		return null;
		
	}
}
