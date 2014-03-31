package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import object.User;
import query.*;
import aws.*;

/**
 * Servlet implementation class Register
 */
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String email = request.getParameter("email");
		//System.out.println(email);
		String pwd = request.getParameter("pwd");
		//System.out.println(pwd);
		//Writer w =  response.getWriter();

		UserQuery uq = RDSManager.createUserQuery();
		uq.insertUser(email, pwd);
		
	 	SNSManager tlsm = new SNSManager();
//	 	tlsm.createTopic();
		tlsm.subscribe(email);
		
		//System.out.println("error");
		HttpSession session = request.getSession(true);
		session.setAttribute("email", email);
		response.sendRedirect("main.jsp");
		
	}

}
