package com.ebay.users.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ebay.products.Product;
import com.ebay.products.Product_Category;
import com.ebay.users.User;

@WebServlet(name="ProductServlet", urlPatterns= {"/productServlet","/productcategoryServlet",
		"/productinsertedServlet","/productcategoryinsertedServlet","/successServlet" })
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private  Connection con=null;
	private Statement st=null;
	
	String viewCart="View Cart";
     
    public ProductServlet() {
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
    		
    		
    		
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String uri=request.getRequestURI();
		
		if(uri.endsWith("/productServlet")) {
			productForm(request,response);
			
		}else if(uri.endsWith("/productcategoryServlet")) {
			productcategoryForm(request,response);
			
		}else if(uri.endsWith("/productinsertedServlet")) {
			productinserted(request,response);
			
		} else if(uri.endsWith("/productcategoryinsertedServlet")) {
			productcategoryinserted(request,response);
			
		} else if(uri.endsWith("/successServlet")) {
			successForm(request,response);
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
				String uri=request.getRequestURI();
			
				if(uri.endsWith("/productServlet")) {
					productForm(request,response);
					
				}else if(uri.endsWith("/productcategoryServlet")) {
					productcategoryForm(request,response);
					
				}else if(uri.endsWith("/productinsertedServlet")) {
					productinserted(request,response);
					
				} else if(uri.endsWith("/productcategoryinsertedServlet")) {
					productcategoryinserted(request,response);
					
				} else if(uri.endsWith("/successServlet")) {
					successForm(request,response);
				}
		
	}
	
	private void productForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter writer =response.getWriter();
		
		
		writer.println("<!DOCTYPE html>" + 
				"<html>" + 
				"<head>" + 
				"<meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>" + 
				"<link rel='stylesheet' href='css/style.css'>");
		
		writer.println("<title>Product Form</title></head>");
		writer.println("<body>");
		
		HttpSession session= request.getSession(false);
		if(session!=null){
		
		writer.println("<header>"
				+"<div class = 'container'>"
					+"<div id = 'branding'>"
					+"<img src='images/logo.jpg' width=50px height=50px>"
					+"<h1> <span class='highlight'> elibrary</span></h1>"
					+"</div>"
					+"<nav>"
						+"<ul>"
							+"<li class= 'current'> <a href='home1.jsp'>Home</a>"
							+"<li><a href='buyOrCartServlet?buyCartView="+viewCart+"'> View Cart</a>"
							+"</ul>"
					+"</nav>"
				+"</div>"
			+"</header>");
		
		writer.println("<section id='boxes'>"  
				+"<div class='box'>");
		
		writer.println("<h1>Enter Product Detail For Sale</h1>");
		
		writer.println("<form action='productinsertedServlet' method='post'>");
		
			String sessionOut= (String) request.getAttribute("sessionOut");
			if(sessionOut!=null) {
				if(sessionOut.endsWith("signout.")) {
					writer.println (sessionOut+" Please <a href='loginServlet'> Signin</a>");
					writer.println(" again.<br> ");
				}else {
					writer.println (sessionOut);
				}
			}
			
			writer.println("Product name<br>");
			writer.println("<input type='text' id='pName' name='pName' placeholder='Enter product name'><br>");
			
			writer.println("Product category<br>");
			writer.println("<select id='select_cat' name='select_cat'>");
			writer.println("<option> Select Category</option>");
			
			String selectSql="SELECT * FROM product_category ORDER BY cat_id";
			try {
					
					ResultSet rs=st.executeQuery(selectSql);
					
					while(rs.next()) {
						writer.println("<option>"+rs.getString("name")+"</option>");
						
					}
						
				}catch(SQLException e) {
					e.printStackTrace();
				}
			
				writer.println("</select></br>");
			
				writer.println("Description<br>");
				writer.println("<textarea id='description' name='description' placeholder='Enter product description'></textarea><br>");
				writer.println("Price<br>");
				writer.println("<input type='number' id='price' name='price' value='0.0' placeholder='Price'><br>");
				writer.println("<input type='submit' id='submit' name='submit' value='Submit'>");
			
				writer.println("</form>");
		
				writer.println("</div></section>");
		
		
		}else if(session==null) {
		
			writer.println("<header>"
					+"<div class = 'container'>"
						+"<div id = 'branding'>"
						+"<img src='images/logo.jpg' width=50px height=50px>"
						+"<h1> <span class='highlight'> elibrary</span></h1>"
						+"</div>"
						+"<nav>"
							+"<ul>"
								+"<li class= 'current'> <a href='home1.jsp'>Home</a>"
								+"<li><a href='buyOrCartServlet?buyCartView="+viewCart+"'> View Cart</a>"
							+"</ul>"
						+"</nav>"
					+"</div>"
				+"</header>");
			
			writer.println("<section id='boxes'>"  
					+"<div class='box'>");
			
			writer.println("<h1>Please <a href='loginServlet'>signin or register </a>before selling product</h1>");
			
			writer.println("</div></section>");
		}
		
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
	
	private void productcategoryForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter writer =response.getWriter();
		String duplicate="";
		
		writer.println("<!DOCTYPE html>" + 
				"<html>" + 
				"<head>" + 
				"<meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>" + 
				"<link rel='stylesheet' href='css/style.css'>");
		
		writer.println("<title>Product Category Form</title></head>");
		
		writer.println("<body>");
		
		HttpSession session= request.getSession(false);
		if(session!=null){
			
			User loginUser = (User)  session.getAttribute("user");
			
			if(loginUser.getEmail().equals("inam@gmail.com")) {		
		
		writer.println( "<header>"
				+"<div class = 'container'>"
					+"<div id = 'branding'>"
					+"<img src='images/logo.jpg' width=50px height=50px>"
					+"<h1> <span class='highlight'> elibrary</span></h1>"
					+"</div>"
					+"<nav>"
						+"<ul>"
							+"<li class= 'current'> <a href='home1.jsp'>Home</a>"
						+"</ul>"
					+"</nav>"
				+"</div>"
			+"</header>");

		writer.println("<section id='boxes'>"  
				+"<div class='box'>");
		
		writer.println("<h1>Product Category Detail</h1>");
		writer.println("<p>Here product categories can be inserted by Admin helping customer to search or sale"
				+" their Items categories wise.</p>");
		
		writer.println("<form action='productcategoryinsertedServlet' method='post'>");
			
				duplicate= (String) request.getAttribute("duplicate");
				if(duplicate!=null) {
				writer.println (duplicate+"<br>");
				}
					
			writer.println("Category name<br>");
			writer.println("<input type='text' id='cat_name' name='cat_name' placeholder='Enter category name'><br>");
			writer.println("<input type='submit' id='submit' name='submit' value='Submit'>");
		
		writer.println("</form>");
		
		writer.println("</div></section>");
			
			}else {
				
				
				writer.println( "<header>"
						+"<div class = 'container'>"
							+"<div id = 'branding'>"
							+"<img src='images/logo.jpg' width=50px height=50px>"
							+"<h1> <span class='highlight'> elibrary</span></h1>"
							+"</div>"
							+"<nav>"
								+"<ul>"
									+"<li class= 'current'> <a href='home1.jsp'>Home</a>"
									+"</ul>"
							+"</nav>"
						+"</div>"
					+"</header>");

				writer.println("<section id='boxes'>"  
						+"<div class='box'>");
				
				writer.println("<h1>You do not have right to insert product category detail.</h1>");
				writer.println("</div></section>");
				
			}
		}// end of if(session!=null)
		else if(session==null) {
			
			writer.println( "<header>"
					+"<div class = 'container'>"
						+"<div id = 'branding'>"
						+"<img src='images/logo.jpg' width=50px height=50px>"
						+"<h1> <span class='highlight'> elibrary</span></h1>"
						+"</div>"
						+"<nav>"
							+"<ul>"
								+"<li class= 'current'> <a href='home1.jsp'>Home</a>"
							+"</ul>"
						+"</nav>"
					+"</div>"
				+"</header>");

			writer.println("<section id='boxes'>"  
					+"<div class='box'>");
			
			writer.println("<h1>You are sign out. Please <a href='loginServlet'> signin </a> again.</h1>");
			writer.println("</div></section>");
		}
		
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
	
private void productinserted(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	 
	RequestDispatcher rd=null;
	 String pName = request.getParameter("pName").trim();
	 String select_cat = request.getParameter("select_cat").trim();
	 String description = request.getParameter("description").trim();
	 float price = Float.parseFloat(request.getParameter("price").trim());
	 String sessionOut="";
	 User userLogin=null;
	 Product_Category prod_cat=null;
	 int userId=0;
	 int catId=0;
	 
	 HttpSession session= request.getSession(false);
	 
	 if(session!=null) {
		 
		 
		 
		 Enumeration<String> name = session.getAttributeNames();
		 
		 while(name.hasMoreElements()) {
			 
			 String key=name.nextElement().toString();
			  				
				if(key.equals("user")) {
					
			    userLogin = (User) session.getAttribute("user");
				userId= userLogin.getId();
				 
			 	}else if(key.equals("prod_cat")) {
				 
				 prod_cat= (Product_Category) session.getAttribute("prod_cat");
				 
				 catId= prod_cat.getCat_id();
				 
				 }
			 				 
		 	}
		 
		 String selectSql= "select * from product_category where name='"+select_cat+"'";
			
		 	try {
		 		
		 		ResultSet rs=st.executeQuery(selectSql);
				while(rs.next()) {
					
				 catId=rs.getInt(1);
				
				}
			 
				String insertSql= "INSERT INTO product(user_id, category_id, product_name, description, price)"+
					"VALUES('"+userId+"','"+catId+"','"+pName+"','"+description+"','"+price+"')";
				
				int inserted= st.executeUpdate(insertSql);
				
				if(inserted>0) {
					
					 sessionOut="";
					 
					 selectSql= "select * from product where product_name='"+pName+"'";
					 rs=st.executeQuery(selectSql);
					while(rs.next()) {
						session= request.getSession();
						Product prod= new Product(rs.getInt("product_id"), rs.getInt("user_id"), rs.getInt("category_id"), rs.getString("product_name"), rs.getString("description"), rs.getFloat("price"));
						session.setAttribute("prod", prod);
						response.sendRedirect("successServlet");
						
					    }
						
					}else {
						sessionOut=" Can't insert product!! Please try again.";
						request.setAttribute("sessionOut",sessionOut);
						rd=request.getRequestDispatcher("/productServlet");
						rd.forward(request, response);
					}
				 		
		 	}catch(Exception e) {
		 		e.printStackTrace();
		 	}
	 
	 	}else {
	 		
			 
		    sessionOut=" Can't insert product!! User is signout.";
			request.setAttribute("sessionOut",sessionOut);
			rd=request.getRequestDispatcher("/productServlet");
			rd.forward(request, response);
	 }
	 
 }

private void successForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	response.setContentType("text/html");
	
	
	PrintWriter writer =response.getWriter();
	
	
	
	writer.println("<!DOCTYPE html>" + 
			"<html>" + 
			"<head>" + 
			"<meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>" + 
			"<link rel='stylesheet' href='css/style.css'>");
	writer.println("<title>Successful Insertion</title></head>");
	
	
	writer.println("<body>");
	writer.println("<header>"
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
	
	HttpSession session = request.getSession(false);
	User loginUser = (User)  session.getAttribute("user");
	
	if(loginUser!=null) {
		
	writer.println("<h3>Item Inserted Successfully</h3> <br>");
	writer.println("<p>Your item is inserted successfully on sale list now. We thank you for choosing our site "
	+ "for sale and will look farward in future. You can <a href='#'>update or delete</a> your item anytime</p>");
	
	writer.println("Return to <a href='home1.jsp'>Home page</a>");
	}
	else if(loginUser==null) { 
		writer.println("<h1>You are sign out. Please <a href='loginServlet'> signin </a> again.</h1>");
	}
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

private void productcategoryinserted(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	RequestDispatcher rd=null;
	String cat_name=request.getParameter("cat_name");
	String duplicate="";
	
	if(cat_name.isEmpty()) {
		
		duplicate=" Can't be empty!! Please Enter valid category";
		request.setAttribute("duplicate",duplicate);
		rd=request.getRequestDispatcher("/productcategoryServlet");
		rd.forward(request, response);
		
		
	}else {
		
		String filterSql = "SELECT name FROM product_category WHERE name='"+cat_name+"'";
	try {
		
		ResultSet rs=st.executeQuery(filterSql);
		int count=0;
		
		while(rs.next()) {
			count++;
		}
		
		if(count>0) {
			duplicate="Following Category exists!! Please Enter another category";
			request.setAttribute("duplicate",duplicate);
			rd=request.getRequestDispatcher("/productcategoryServlet");
			rd.forward(request, response);
		}else {
			
			String insertSql="INSERT INTO product_category(name) VALUES('"+cat_name+"')";
			
			int inserted= st.executeUpdate(insertSql);
			
			if(inserted>0) {
				
				String selectSql= "select * from product_category where name='"+cat_name+"'";
				rs=st.executeQuery(selectSql);
				while(rs.next()) {
					HttpSession session= request.getSession();
					Product_Category prod_cat= new Product_Category(rs.getInt("cat_id"), rs.getString("name"));
					session.setAttribute("prod_cat", prod_cat);
					response.sendRedirect("home.jsp");
				    }
				}
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	
	}// end of outer if/else
	
}

}
