package com.ebay.users.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

import com.ebay.users.*;
import com.ebay.products.*;

@WebServlet(name="LoginServlet", urlPatterns= { "/loginServlet", "/registrationServlet","/logingServlet","/registeringServlet",
		"/logoutServlet"})
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int login_id=1;

	String viewCart="View Cart";
	
	private  Connection con=null;
	private Statement st=null;

     
    public LoginServlet() {
        super();
       
    }
    
    public void init(ServletConfig config) throws ServletException {
    	
    	super.init();
    	
    	
    	try {
    		
    		
    	
    		Class.forName(config.getServletContext().getInitParameter("driverpath"));
    	
    	}catch(ClassNotFoundException e) {
    		e.printStackTrace();
    	}
    	
    	String SSL="? verifyServerCertificate=false&useSSL=false";
    	String url= config.getServletContext().getInitParameter("url");
    	String user=config.getServletContext().getInitParameter("user");
    	String pass= config.getServletContext().getInitParameter("pass");

    try{
    	 con=DriverManager.getConnection(url+SSL, user, pass);

    		 st =con.createStatement();
    		// ps=con.prepareStatement();
    		
    		
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String uri = request.getRequestURI();
		
		try {
			
			if(uri.endsWith("/loginServlet")) {
				
				loginForm(request,response);
				
			}else if(uri.endsWith("/registrationServlet")) {
				
				   registrationForm( response);
				
			}else if(uri.endsWith("/logoutServlet")) {
				
					logout(request,response);
					
			}else if(uri.endsWith("/logingServlet")) {
					
					loging(request, response);
				
			}else if(uri.endsWith("/registeringServlet")){
					registering(request,response);
				} 
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String uri = request.getRequestURI();
		
try {
			
	if(uri.endsWith("/loginServlet")) {
		
		loginForm(request,response);
		
	}else if(uri.endsWith("/registrationServlet")) {
		
		   registrationForm( response);
		
	}else if(uri.endsWith("/logoutServlet")) {
		
			logout(request,response);
			
	}else if(uri.endsWith("/logingServlet")) {
			
			loging(request, response);
		
	}else if(uri.endsWith("/registeringServlet")){
			registering(request,response);
		} 
		
		}catch(SQLException e) {
			e.printStackTrace();
		}					
	}
	
private void loginForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html");
		
		PrintWriter writer =response.getWriter();
		
		
		
		try {
			
			 login_id=Integer.parseInt(request.getParameter("id"));
			}catch(NumberFormatException e) {
				e.printStackTrace();
			}
			
		
		String loginFailure="";
		
		
		
		writer.print(login_id);
		
		writer.println("<!DOCTYPE html>" + 
				"<html>" + 
				"<head>" + 
				"<meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>" + 
				"<link rel='stylesheet' href='css/style.css'>");
		writer.println("<title>User login</title></head>");
		
		
		writer.println("<body>"
				+ "<header>"
				+"<div class = 'container'>"
					+"<div id = 'branding'>"
					+"<img src='images/logo.jpg' width=50px height=50px>"
					+"<h1> <span class='highlight'> elibrary</span></h1>"
					+"</div>"
					+"<nav>"
						+"<ul>"
							+"<li class= 'current'> <a href='home1.jsp'>Home</a>"
							+"<li><a href='buyOrCartServlet?buyCartView="+viewCart+"'>View Cart</a>"
						+"</ul>"
					+"</nav>"
				+"</div>"
			+"</header>");

		writer.println("<section id='boxes'>"  
				+"<div class='box'>");
			
		writer.println("<h3>User Login</h3> <br>");
		writer.println("<form action='logingServlet' method='post'>");
		
			loginFailure= (String) request.getAttribute("failure");
			if(loginFailure!=null) {
			writer.println (loginFailure+"<br>");
			}
							
			writer.println("<input type='hidden' id='hid' name='hid' value="+login_id+"><br>"
			
			+"<div>"
			+"Email Address <br>"
			+"<input type='email' id='email' name='email' placeholder='Enter Email...'><br>"
			+"</div>"
			+"<div>"
			+"Password <br>"
			+"<input type='password' id='password' name='password' placeholder='Enter Passowrd...' maxlength='20' placeholder='Password' onkeyup='return passwordStrength();'></br>"
			+"<span id='strength'></span><br>"
			+"</div>"
			+"<div>"
			+"<input type='submit' value='Signin'>"
			+"&nbsp &nbsp <h6>New user?<a href='registrationServlet'> Sign up</a></h6>"
			+"</div>"
		+"</form>");
			
			writer.println("</div></section>");
	
		
		
		
		writer.println("<footer>"
		+"<div class='container'>"
                +"<div id='branding'>"
                        +"<img src='images/logo.jpg' alt='' width=50px height=50px>"
                        +"<h1> <span class='highlight'>elibrary</span></h1>" 
                         +"<p><span class='copy'>Copyright &copy; 2018</span></p>"
                +"</div>"
        +"</div>"
	+"</footer>");
		
		writer.println("<script src='js/passwordstrength.js'></script>");
		
		
		writer.println("</body></html>");
	
	}
	
	private void loging(HttpServletRequest request ,HttpServletResponse response) throws SQLException,IOException, ServletException {
		
			
		RequestDispatcher rd=null;
		String	email= request.getParameter("email").trim();
		String	password= request.getParameter("password").trim();
		int login_hid=Integer.parseInt(request.getParameter("hid"));
		String loginFail="";
		
	
	
				String loginSql= "SELECT * FROM user WHERE email='"+email+"'";
				
				ResultSet rs=st.executeQuery(loginSql);
				
				if(rs.next()) {
					
					if(password.equals(rs.getString(5))) {
						loginFail="";
					int autoId=rs.getInt(1);
					String fName= rs.getString(2);
					String lName= rs.getString(3);
					String contact= rs.getString(6);
					
					
					// get Session for user
					HttpSession session= request.getSession();
										
					User userLogin= new User(autoId, fName,  lName, email, password, contact);
					
					session.setAttribute("user", userLogin);
				
					
					if(login_hid==1) {
						
						response.sendRedirect("home1.jsp");
						
					}else if(login_hid==0) {
					
						
						response.sendRedirect("moveTemporaryToCartServlet?guest_userId="+userLogin.getId());
					}
					
				}else {
					
					loginFail="Wrong password !! Please Enter your Correct Pasword";
					request.setAttribute("failure",loginFail);
					rd=request.getRequestDispatcher("/loginServlet");
					rd.forward(request, response);
												
				}
				
			}else {
				
				loginFail="Wrong user ID !! Please Enter your Correct User ID";
				request.setAttribute("failure",loginFail);
				rd=request.getRequestDispatcher("/loginServlet");
				rd.forward(request, response);
				
			}
			
	}
	
	private void registrationForm( HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html");
		
		PrintWriter writer=response.getWriter();
		
		writer.println("<!DOCTYPE html>" + 
				"<html>" + 
				"<head>" + 
				"<meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>" + 
				"<link rel='stylesheet' href='css/style.css'>");
		writer.println("<title>User Registration</title></head>");
		
		
		writer.println("<body>"
				+ "<header>"
				+"<div class = 'container'>"
					+"<div id = 'branding'>"
					+"<img src='images/logo.jpg' width=50px height=50px>"
					+"<h1> <span class='highlight'> elibrary</span></h1>"
					+"</div>"
					+"<nav>"
						+"<ul>"
							+"<li class= 'current'> <a href='home1.jsp'>Home</a>"
							+"<li><a href='buyOrCartServlet'> View Cart</a>"
						+"</ul>"
					+"</nav>"
				+"</div>"
			+"</header>");

		writer.println("<section id='boxes'>"  
				+"<div class='box'>");
		
		writer.println("<h1>Registration Form</h1>");
		writer.println("<p>Fields marked with * are mandatory leaving any field empty will not proceed this form</p>");
		
		writer.println("<form action='registeringServlet' method='post'>");
		
			writer.println("First name*<br>");
			writer.println("<input type='text' id='fName' name='fName'><br>");
			writer.println("Last name*<br>");
			writer.println("<input type='text' id='lName' name='lName'><br>");
			writer.println("Email*<br>");
			writer.println("<input type='email' id='email' name='email'><br>");
			writer.println("Confirm email*<br>");
			writer.println("<input type='email' id='con_email' name='con_email'><br>");
			writer.println("Password*<br>");
			writer.println("<input type='password' id='password' name='password'><br>");
			writer.println("Confirm password*<br>");
			writer.println("<input type='password' id='con_password' name='con_password'><br>");
			writer.println("Mobile<br>");
			writer.println("<input type='text' id='contact' name='contact'><br>");
			writer.println("<input type='submit' id='register' name='register' value='Register'>");
			writer.println("<input type='reset' id='reset' name='reset' value='Reset'>");
			
		writer.println("</form>");
		
		writer.println("</div></section>");
		
		writer.println("<footer>"
				+"<div class='container'>"
		                +"<div id='branding'>"
		                        +"<img src='images/logo.jpg' alt='' width=50px height=50px>"
		                        +"<h1> <span class='highlight'>elibrary</span></h1>" 
		                         +"<p><span class='copy'>Copyright &copy; 2018</span></p>"
		                +"</div>"
		        +"</div>"
			+"</footer>");
		
		writer.println("</body></html>");
		
		
	}
	
	private void registering(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		
				
		String fName= request.getParameter("fName").trim();
		String lName= request.getParameter("lName").trim();
		String email=request.getParameter("email").trim();
		String password=request.getParameter("password").trim();
		String contact= request.getParameter("contact").trim();
		
		String insertSql= "INSERT INTO user(fname, lname, email, password, contact)"+
							"VALUES('"+fName+"','"+lName+"','"+email+"','"+password+"','"+contact+"')";
		
		int inserted= st.executeUpdate(insertSql);
		
			if(inserted>0) {
				
				String selectSql= "select * from user where email='"+email+"'";
				ResultSet rs=st.executeQuery(selectSql);
				while(rs.next()) {
					HttpSession session= request.getSession();
					User userLogin= new User(rs.getInt("Id"), rs.getString("Fname"), rs.getString("Lname"), rs.getString("Email"), rs.getString("Password"), rs.getString("Contact"));
					session.setAttribute("user", userLogin);
					response.sendRedirect("home.jsp");
				    }
					
				}
		
		}
	
	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		/*
		
		HttpSession session= request.getSession();
		request.removeAttribute("user");
		session.invalidate();
		response.sendRedirect("home.jsp");
		
		*/
		
		
		
		HttpSession session= request.getSession();
		Enumeration allSignout= session.getAttributeNames();
		
		while(allSignout.hasMoreElements()) {
			
			String key = allSignout.nextElement().toString();
			session.removeAttribute(key);
			
			}
		session.invalidate();
			
		//response.sendRedirect("home.jsp");
		response.sendRedirect("home1.jsp");
		
	}

}
