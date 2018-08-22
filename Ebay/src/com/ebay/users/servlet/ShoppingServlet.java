package com.ebay.users.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ebay.products.AddToCart;
import com.ebay.products.Product;
import com.ebay.products.Product_Category;
import com.ebay.users.User;

@WebServlet(name="/ShoppingServlet", urlPatterns= {"/searchServlet","/searchProductServlet","/productDetailServlet","/buyOrCartServlet",
		"/removeFromCartServlet","/moveTemporaryToCartServlet","/checkoutServlet"})

public class ShoppingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CART_ATTRIBUTE="cart";
	private NumberFormat currencyFormat= NumberFormat.getCurrencyInstance(Locale.US);
	private List<AddToCart> cart;
	private String viewCart="View Cart";
	private List<Product> prodList;
	
	private Connection con;
	private Statement st;
       
    
    public ShoppingServlet() {
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
		
		String uri= request.getRequestURI();
	
		if(uri.endsWith("/searchServlet")) {
			search(request,response);
		}else if(uri.endsWith("/productDetailServlet")){
			productDetailForm(request,response);
		}else if(uri.endsWith("/buyOrCartServlet")){
			buyOrCart(request,response);
		}else if(uri.endsWith("/removeFromCartServlet")) {
			removeFromCart(request,response);
		}else if(uri.endsWith("/moveTemporaryToCartServlet")) {
			moveTemporaryToCart(request,response);
		}else if(uri.endsWith("/checkoutServlet")) {
			checkoutForm(request,response);
		} else if(uri.endsWith("/searchProductServlet")) {
			searchProductForm(request,response);
		}
	
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String uri= request.getRequestURI();
		
		if(uri.endsWith("/searchServlet")) {
			search(request,response);
		}else if(uri.endsWith("/productDetailServlet")){
			productDetailForm(request,response);
		}else if(uri.endsWith("/buyOrCartServlet")){
			buyOrCart(request,response);
		}else if(uri.endsWith("/removeFromCartServlet")) {
			removeFromCart(request,response);
		}else if(uri.endsWith("/moveTemporaryToCartServlet")) {
			moveTemporaryToCart(request,response);
		}else if(uri.endsWith("/checkoutServlet")) {
			checkoutForm(request,response);
		}else if(uri.endsWith("/searchProductServlet")) {
			searchProductForm(request,response);
		}
	}
	
	private void search(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
			PrintWriter writer= response.getWriter();
			
			String searchItem= request.getParameter("pSearch").trim();
			String selectedItem= request.getParameter("select_cat").trim();
			
			ResultSet rs=null;
			String invalidSearch=null;
			
		if(selectedItem.equals("All Categories")) {
				
				//writer.println(selectedItem);
				
				prodList= new ArrayList<Product>();
				
			
				try {
					String searchSql="SELECT COUNT(*) as rowsi FROM product WHERE product_name='"+searchItem+"'";
					
					rs= st.executeQuery(searchSql);
					rs.next();
					int rows=rs.getInt("rowsi");
					
				 		if(rows==0)	{
								invalidSearch="Such item doesn't exist!! Please search for another Item";
								request.setAttribute("invalidSearch",invalidSearch);
								request.getRequestDispatcher("home1.jsp").forward(request, response);
				 		}else {
					
								invalidSearch="";
								String searchSql1="SELECT * FROM product WHERE product_name='"+searchItem+"'";
								rs= st.executeQuery(searchSql1);
									while(rs.next()) {
									prodList.add( new Product(rs.getInt("product_id"), rs.getInt("user_id"),
								rs.getInt("category_id"), rs.getString("product_name"),rs.getString("description"), rs.getFloat("price")));
									}
				
								HttpSession session=request.getSession(false);
								session.setAttribute("searchProd", prodList);
								response.sendRedirect("searchProductServlet");
								//response.sendRedirect("session.jsp");
								//request.setAttribute("searchProd", prodList);
								//request.getRequestDispatcher("searchedProduct.jsp").forward(request, response);
				 		
				 			}
				 		
				}catch(Exception e) {
					e.printStackTrace();
				}
			
		} // end of if(selectedItem.equals("All Categories"))	
		else {
				
					prodList= new ArrayList<Product>();
				
			try {
					
					String searchcategoryId="SELECT * FROM product_category WHERE name='"+selectedItem+"'";
					int cat_id=0;
				
					rs=st.executeQuery(searchcategoryId);
				
					while(rs.next()) {
						
						cat_id=rs.getInt("cat_id");
						}
				
					String searchSql="SELECT COUNT(*) as rows FROM product WHERE product_name='"+searchItem+"' and category_id="+cat_id;
				
					rs= st.executeQuery(searchSql);
					rs.next();
					int rows=rs.getInt("rows");
				 
				if(rows==0)	{
					
					invalidSearch="Such item doesn't exist!! Please search for another Item";
					request.setAttribute("invalidSearch",invalidSearch);
					request.getRequestDispatcher("home1.jsp").forward(request, response);
					
				}else {
				
					invalidSearch="";
					String searchSql1="SELECT * FROM product WHERE product_name='"+searchItem+"' and category_id="+cat_id;
					rs= st.executeQuery(searchSql1);
					
					while(rs.next()) {
					prodList.add( new Product(rs.getInt("product_id"), rs.getInt("user_id"), rs.getInt("category_id"), rs.getString("product_name"),rs.getString("description"), rs.getFloat("price")));
					}
				
					HttpSession session=request.getSession(false);
					session.setAttribute("searchProd", prodList);
					response.sendRedirect("searchProductServlet");
					//response.sendRedirect("session.jsp");
					//request.setAttribute("searchProd", prodList);
					//request.getRequestDispatcher("searchedProduct.jsp").forward(request, response);
				 }
			}catch(Exception e) {
					e.printStackTrace();
				}
	   } // end of else if(selectedItem.equals("All Categories"))
			
	}
		
			
	private void searchProductForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html");
		PrintWriter writer =response.getWriter();
	
		NumberFormat currencyFormat=null;	
	    List<Product> searchProdList=null;
		
		writer.println("<!DOCTYPE html>" + 
				"<html>" + 
				"<head>" + 
				"<meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>" + 
				"<link rel='stylesheet' href='css/style.css'>");

		writer.println("<title>Searched Products</title></head>");
		
		
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
		writer.println("<h3>List of search products </h3> <br>");
		
			HttpSession session= request.getSession(false);
			searchProdList=(List<Product>) session.getAttribute("searchProd");
			if(searchProdList!=null){
				
				writer.println("<table><tr><th>Product Name</th><th>Quantity</th><th>Detail</th></tr>");
				
				for(Product availProd:searchProdList){ 
				
				writer.println("<tr><td>"+availProd.getName()+"</td><td>"+availProd.getPrice()+"</td><td>"
				+"<a href='productDetailServlet?pId="+availProd.getPid()+"'> Detail</a></td></tr>");
				
					
					} // end of for(Product availProd:searchProdList)
							
				writer.println("</table>");
				
	       } // end of if(searchProdList!=null){
			else{
				
				 writer.println(" <h3> No Product is searched </h3>");			
							
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
private void productDetailForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			response.setContentType("text/html");
			PrintWriter writer= response.getWriter();
			
			int productId=0;
			Product product=null;
			String userName=null;
			String catName=null;
			String insFailure1="";
			
			
	try {
			productId= Integer.parseInt( request.getParameter("pId"));
				
			product =getProduct(productId);
			String userSelectSql="SELECT * FROM user WHERE id="+product.getUser_id();
			ResultSet rs= st.executeQuery(userSelectSql);
				
				while(rs.next()) {
					userName= rs.getString("Fname");
					
     				}
				
			String catSelectSql="SELECT * FROM product_category WHERE cat_id="+product.getCat_id();
			rs=st.executeQuery(catSelectSql);
				
				while(rs.next()) {
					catName=rs.getString("name");
			    	}
			
		}catch(NumberFormatException | SQLException e) {
				e.printStackTrace();
			}
			
		if(product!=null) {
				
				writer.println("<!DOCTYPE html>" + 
						"<html>" + 
						"<head>" + 
						"<meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>" + 
						"<link rel='stylesheet' href='css/style.css'>");
				writer.println("<title>Product Detail view</title></head>");
				
				
				writer.println("<body>");
			
				HttpSession session = request.getSession(false);
				User loginUser = (User)  session.getAttribute("user");
				
				if(loginUser!=null) {
								
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
				}else if(loginUser==null) {
					
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
					
				  } // end of else if(loginUser==null) {
				
				writer.println("<section id='boxes'>"  
						+"<div class='box'>");
					
				writer.println("<h3>Products listed with description</h3> <br>");
				
					writer.println("<form action='buyOrCartServlet' method='post' onsubmit='return validQuantity()'>");
				
				writer.println("<input type='hidden' name='hid' value='"+productId+"'>");
		
				writer.println("<table>");

				writer.println("<tr><th>Product Name</th><th>Description</th><th>Price</th><th>Seller</th><th>Category</th></tr>");
				writer.println("<tr><td>"+product.getName()+"</td><td>"+product.getDescription()+"</td><td>"+currencyFormat.format(product.getPrice())+"</td><td>"+userName+"</td><td>"+catName+"</td></tr>");
				
						insFailure1= (String) request.getAttribute("insFailure");
			  			if(insFailure1!=null) {
			  			writer.println (insFailure1+"<br>");
			  			}
	  					  		
				writer.println("<tr><td><input type='text' id='quantity' name='quantity' value=1><br>"
						+ "&nbsp<span id='valid'></span></td>"
						+ "<td><input type='submit' id='toBuy' name='buyCartView' value='Buy It Now'></td></tr>");
				writer.println("<tr><td></td><td><input type='submit' id='toCart' name='buyCartView' value='Add To Cart'></td></tr>");
			      

				writer.println("</table>");
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

				writer.println("<script src='js/validquantity.js'></script>");
			writer.println("</body></html>");

			
				
			} // end of if(product!=null) {
			else {
				
				//writer.println("No Such product present");
				String insFailure="Product List is empty can't added to cart! please try again.";
				request.setAttribute("insFailure", insFailure);
				request.getRequestDispatcher("productDetailServlet").forward(request, response);
			
			  }
	}
	
private void buyOrCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
			response.setContentType("text/html");
			PrintWriter writer= response.getWriter();
			List<AddToCart> cart= new ArrayList<AddToCart>();
			//String cartBtn= request.getParameter("toCart");
			ResultSet rs1=null;
			Statement st1=null;
			
			String buyCartViewOption;
			
			buyCartViewOption=request.getParameter("buyCartView");
			
			if(buyCartViewOption==null) {
				
				buyCartViewOption="View Cart";
			}
			
			writer.println(buyCartViewOption);
			
			if(buyCartViewOption!=null) {
				
				if(buyCartViewOption.equals("Buy It Now")) {
					//writer.println("Buying is clicked");
				}else if(buyCartViewOption.equals("Add To Cart")) {
					//writer.println("Add To cart is clicked");
				}else if(buyCartViewOption.equals("View Cart")) {
					//writer.println("view cart is clicked");
				}
			}
			
			int pId=0;
			int qty=0;
   try {
					
			if(buyCartViewOption.equals("Buy It Now")|| buyCartViewOption.equals("Add To Cart")) {
				
			pId= Integer.parseInt(request.getParameter("hid"));
			qty = Integer.parseInt(request.getParameter("quantity"));
			
	      } // end of if(buyCartViewOption.equals("Buy It Now")|| buyCartViewOption.equals("Add To Cart")) {
	 
					
			
			writer.println("<!DOCTYPE html>" + 
					"<html>" + 
					"<head>" + 
					"<meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>" + 
					"<link rel='stylesheet' href='css/style.css'>");
			writer.println("<title>Successful Insertion</title></head>");
			
			
			HttpSession session = request.getSession(false);
			User loginUser = (User)  session.getAttribute("user");
			
			if(loginUser!=null) {
				
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

				
				if(pId==0) { // user is signin but only view his cart not entering new products
					
					// cart= new ArrayList<AddToCart>();
					 
					 String selectSql="SELECT * FROM cart WHERE buyer_id="+loginUser.getId();
						 ResultSet rs= st.executeQuery(selectSql);
						while(rs.next()) {
							int cart_id=rs.getInt(1);
							int seller_id=rs.getInt(2);
							int product_id=rs.getInt(3);
							int quantity= rs.getInt(5);
							float price= rs.getFloat(6);
							
							String seller_name="";
							String product_name="";
							
							String seller_nameSql="select * from user where id="+seller_id;
							 st1=con.createStatement();
							 rs1=st1.executeQuery(seller_nameSql);
							if(rs1.next()) {
												
											seller_name=rs1.getString("fname");
									
										}
							
							String product_nameSql="select * from product where product_id="+product_id;
							 st1=con.createStatement();
							 rs1=st1.executeQuery(product_nameSql);
							if(rs1.next()) {
								
												product_name=rs1.getString("product_name");
											}
							
							
							cart.add(new AddToCart(cart_id,seller_id,seller_name,product_id,product_name,
									loginUser.getId(),loginUser.getFname(),quantity,price));
							
						} // end of while(rs.next())
					 
					
				} // end of if(pId==0)
				else if( buyCartViewOption.equals("Add To Cart")) {
					
					//cart= new ArrayList<AddToCart>();
					Product product=getProduct(pId);
					
					if(product!=null && qty>0) {
					
					String insertSql="INSERT INTO cart(seller_id,product_id,buyer_id,quantity,price)"+
					"VALUES("+product.getUser_id()+","+product.getPid()+","+loginUser.getId()+","+qty+","+product.getPrice()+")";
					    int insertedRow=0;
					
			
							insertedRow= st.executeUpdate(insertSql);
							
							if(insertedRow>0) {
								//writer.print("row inserted");
								String selectSql="SELECT * FROM cart WHERE buyer_id="+loginUser.getId();
								 ResultSet rs= st.executeQuery(selectSql);
								while(rs.next()) {
									int cart_id=rs.getInt(1);
									int seller_id=rs.getInt(2);
									int product_id=rs.getInt(3);
									int quantity= rs.getInt(5);
									float price= rs.getFloat(6);
									
									String seller_name="";
									String product_name="";
									
									String seller_nameSql="select * from user where id="+seller_id;
									 st1=con.createStatement();
									 rs1=st1.executeQuery(seller_nameSql);
									if(rs1.next()) {
											seller_name=rs1.getString("fname");
											}
									
									
									String product_nameSql="select * from product where product_id="+product_id;
									 st1=con.createStatement();
									 rs1=st1.executeQuery(product_nameSql);
									if(rs1.next()) {
										product_name=rs1.getString("product_name");
										}
									
									
									cart.add(new AddToCart(cart_id,seller_id,seller_name,product_id,product_name,
											loginUser.getId(), loginUser.getFname(),quantity,price));
									
									} // end of while(rs.next())
														
							} // end of if(insertedRow>0)
							else if(insertedRow==0) {
								
								String selectSql="SELECT * FROM cart WHERE buyer_id="+loginUser.getId();
								 ResultSet rs= st.executeQuery(selectSql);
								while(rs.next()) {
									int cart_id=rs.getInt(1);
									int seller_id=rs.getInt(2);
									int product_id=rs.getInt(3);
									int quantity= rs.getInt(5);
									float price= rs.getFloat(6);
									
									String seller_name="";
									String product_name="";
									
									String seller_nameSql="select * from user where id="+seller_id;
									 st1=con.createStatement();
									 rs1=st1.executeQuery(seller_nameSql);
									if(rs1.next()) {
											seller_name=rs1.getString("fname");
											}
									
									
									String product_nameSql="select * from product where product_id="+product_id;
									 st1=con.createStatement();
									 rs1=st1.executeQuery(product_nameSql);
									if(rs1.next()) {
										product_name=rs1.getString("product_name");
										}
									
									
									cart.add(new AddToCart(cart_id,seller_id,seller_name,product_id,product_name,
											loginUser.getId(), loginUser.getFname(),quantity,price));
									
									} // end of while(rs.next())
								
								} // end of else if(insertedRow==0) condition
							
					} // end of if(product!=null && qty>=0) {
							
					} // end of else if(buyCartViewOption.equals("Add To Cart"))
				else if(buyCartViewOption.equals("Buy It Now")) {
					writer.println("You are signin and trying to buying");
				}
																					
			} // end of if(loginUser!=null)
			else if(loginUser==null) {
				
				int guest_id=0;
				String guest_name="guest";
				
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
									+"<li><a href='home1.jsp?guest_name="+guest_name+"'>Signin to save Cart</a>"
								+"</ul>"
							+"</nav>"
						+"</div>"
					+"</header>");
								
					if(pId==0) {
											
					//	cart=new ArrayList<AddToCart>();
						
						String selectSql="SELECT * FROM cart WHERE buyer_id="+guest_id;
						 ResultSet rs= st.executeQuery(selectSql);
						while(rs.next()) {
							int cart_id=rs.getInt(1);
							int seller_id=rs.getInt(2);
							int product_id=rs.getInt(3);
							int quantity= rs.getInt(5);
							float price= rs.getFloat(6);
							
							String seller_name="";
							String product_name="";
							
							String seller_nameSql="select * from user where id="+seller_id;
							 st1=con.createStatement();
							 rs1=st1.executeQuery(seller_nameSql);
							if(rs1.next()) {
									seller_name=rs1.getString("fname");
									}
							
							
							String product_nameSql="select * from product where product_id="+product_id;
							 st1=con.createStatement();
							 rs1=st1.executeQuery(product_nameSql);
							if(rs1.next()) {
								product_name=rs1.getString("product_name");
								}
							cart.add(new AddToCart(cart_id,seller_id,seller_name,product_id,product_name,
									
									guest_id, guest_name,quantity,price));
							
							} // end of while(rs.next())
						
					} // end of if(pId==0)
					else if(buyCartViewOption.equals("Add To Cart")) {
						
						//cart= new ArrayList<AddToCart>();
						
						Product product=getProduct(pId);
								
						
						if(product!=null && qty>0) {
							
							/*
							 session= request.getSession();
							
							 cart = (List<AddToCart>) session.getAttribute(CART_ATTRIBUTE);
							
							if(cart==null) {
								
								cart=new ArrayList<AddToCart>();
								session.setAttribute(CART_ATTRIBUTE, cart);
							}
								*/						
						
						
						String insertSql="INSERT INTO cart(seller_id,product_id,buyer_id,quantity,price)"+
								"VALUES("+product.getUser_id()+","+product.getPid()+","+guest_id+","+qty+","+product.getPrice()+")";
						
						int insertedRow=0;
						
						insertedRow= st.executeUpdate(insertSql);
						
						if(insertedRow>0) {
							//writer.print("row inserted");
							
							 String selectSql="SELECT * FROM cart WHERE buyer_id="+guest_id;
							 ResultSet rs= st.executeQuery(selectSql);
							while(rs.next()) {
								int cart_id=rs.getInt(1);
								int seller_id=rs.getInt(2);
								int product_id=rs.getInt(3);
								int quantity= rs.getInt(5);
								float price= rs.getFloat(6);
								
								String seller_name="";
								String product_name="";
								
								String seller_nameSql="select * from user where id="+seller_id;
								 st1=con.createStatement();
								 rs1=st1.executeQuery(seller_nameSql);
								if(rs1.next()) {
										seller_name=rs1.getString("fname");
										}
								
								
								String product_nameSql="select * from product where product_id="+product_id;
								 st1=con.createStatement();
								 rs1=st1.executeQuery(product_nameSql);
								if(rs1.next()) {
									product_name=rs1.getString("product_name");
									}
								
								cart.add(new AddToCart(cart_id,seller_id,seller_name,product_id,product_name,
										
										guest_id, guest_name,quantity,price));
								
								
								} // end of while(rs.next())
							
							} // end of else if(insertedRow==0) condition
		
						} // end of if(product!=null && qty>=0) {
						
				} // end of else if(buyCartViewOption.equals("Add To Cart"))
					else if(buyCartViewOption.equals("Buy It Now")) {
						
						writer.println("You are signout and cant buy");
						
					}
																					
		}//end of else if(loginUser==null) {
			
			writer.println("<section id='boxes'><div class='box'>");
			 	     
		     double total = 0.0;	
		     int total_qty=0;
		    if (cart != null) {
		    	
		         writer.println("<table style='align:left'>");
		         writer.println("<tr><td style='width:150px'>Quantity"+ "</td>"
		                 + "<td style='width:150px'>Product</td>"
		                 + "<td style='width:150px'>Price</td>"
		                // + "<td style='width:150px'>Seller</td>"
		                 + "<td>Amount</td></tr>");
		         
		         for (AddToCart crt : cart) {
		            
		             if (crt.getQuantity() != 0) {
		                
		                 writer.println("<tr>");
		                 //writer.println("<td>" + crt.getQuantity() + "</td>");
		                 writer.println("<td><form action='' post=''>");
		                 writer.println("<input type='text' id='qty' name='qty' min='1' max='100'"
		                 		+ " value="+crt.getQuantity()+">");
		                 
		                 writer.println("</form></td>");
		                 writer.println("<td>" + crt.getProduct_name() + "</td>");
		                 writer.println("<td>" + currencyFormat.format(crt.getPrice()) + "</td>");
		              //   writer.println("<td>" + crt.getSeller_name() + "</td>");
		                 
		                 double subtotal = crt.getPrice() * crt.getQuantity();

		                 writer.println("<td>"
		                         + currencyFormat.format(subtotal)
		                         + "</td>");
		                 total += subtotal;
		                 total_qty+=crt.getQuantity();
		                 writer.println("</tr>");
		                 
		                 writer.println("<tr><td>");
		                 
		                String delFailure= (String) request.getAttribute("delFailure");
		     			if(delFailure!=null) {
		     			writer.println (delFailure);
		     			}
		                 
		                 writer.println("</td><td colspan='2' " 
				                 + "style='text-align:right'>"
				        		 + "<a href='removeFromCartServlet?pId="+crt.getProduct_id()+"&sId="+crt.getSeller_id()
				        		 +"&bId="+crt.getBuyer_id()+"'> Remove </a>"
				                 + "</td></tr>");
		               } // end of if (crt.getQuantity() != 0)
		             
		         } // end of for (AddToCart crt : cart)
		         
		        // writer.println("<hr>");
		         writer.println("</table>");
		         
		         
		        
		         writer.println("<form action='checkoutServlet' method='post'>");
		         writer.println("<table style='align:right'>");
		         writer.println("<input type='submit' name='checkoutt' value='Checkout to payement'>");
		         writer.println("<tr><td  style='width:80px;text-align:left'>"
		                 + "Items("+total_qty
		                 + ")</td>");
		         writer.println("<td style='text-align:right'>"
		                 + "US "+currencyFormat.format(total)
		                 + "</td></tr>");
		         
		         writer.println("<tr><td style='width:80px;text-align:left'>"
		                 + "Total: </td>");
		         writer.println("<td style='text-align:right'>"+ currencyFormat.format(total)
		                 + "</td></tr>");
		         
		         writer.println("</table>");
		         writer.println("</form>");
		         
		         writer.println("</div></section>");
			   
				      	         
		     } // end of if (cart != null)
		    
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
		    
		
}catch(Exception e) {
	e.printStackTrace();
 }finally {
	 try {
		 if(st1!=null) {
		 st1.close();
		 }
		 if(rs1!=null) {
		 rs1.close();
		 }
	 
	 }catch(SQLException e) {
		 e.printStackTrace();
	 }
	 
 }

} // end of buyOrCart() method
			
private void removeFromCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	
	int pId=Integer.parseInt(request.getParameter("pId"));
	int bId=Integer.parseInt(request.getParameter("bId"));
	int sId=Integer.parseInt(request.getParameter("sId"));
	//int guest_id=0;
	PrintWriter writer=response.getWriter();
	
try {
	HttpSession session = request.getSession(false);
	User loginUser = (User)  session.getAttribute("user");
	String delFailure="";
	
	if(loginUser!=null) {
		if(loginUser.getId()==bId) {
			String deleteSql="DELETE FROM cart WHERE seller_id="+sId+" and product_id="+pId+" and buyer_id="+bId; 
			
			int deletedRow=0;
					
			deletedRow= st.executeUpdate(deleteSql);
			if(deletedRow>0) {
				response.sendRedirect("buyOrCartServlet");
			}else {
				delFailure="Item Not Found! please try again.";
				request.setAttribute("delFailure", delFailure);
				request.getRequestDispatcher("buyOrCartServlet").forward(request, response);
			}
			
		} //end of if(loginUser.getId()==bId)
	} // end of if(loginUser!=null) 
	else if(loginUser==null) {
		
			String deleteSql="DELETE FROM cart WHERE seller_id="+sId+" and product_id="+pId+" and buyer_id="+bId; 
			
			int deletedRow=0;
			
			
			deletedRow= st.executeUpdate(deleteSql);
			if(deletedRow>0) {
				response.sendRedirect("buyOrCartServlet");
			}else {
				delFailure="Item Not Found! please try again.";
				request.setAttribute("delFailure", delFailure);
				request.getRequestDispatcher("buyOrCartServlet").forward(request, response);
			}
			
		
	} // end of if(loginUser==null) 
	
	
}// end of try{
catch(Exception e) {
	e.printStackTrace();
}
	
	
}// end of removeFromCart() method


private void moveTemporaryToCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	
	int guest_userId=Integer.parseInt(request.getParameter("guest_userId"));
	
	String updateSql="UPDATE cart SET buyer_id="+guest_userId;
	
	try {
		int updatedRow= st.executeUpdate(updateSql);
		
		if(updatedRow>0) {
			
			buyOrCart( request, response);
			response.sendRedirect("buyOrCartServlet");
		}
	} catch (SQLException e) {
		
		e.printStackTrace();
	}
}

private void checkoutForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	response.setContentType("text/html");
	PrintWriter writer= response.getWriter();
	
	
	HttpSession session = request.getSession(false);
	User loginUser = (User)  session.getAttribute("user");
	
	writer.println("<!DOCTYPE html>" + 
			"<html>" + 
			"<head>" + 
			"<meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>" + 
			"<link rel='stylesheet' href='css/style.css'>");
	writer.println("<title>Checkout Form</title></head>");
	
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
						
						+"<li><a href='registrationServlet'>Register</a>"
						+"<li><a href='buyOrCartServlet'> View Cart</a>"
					+"</ul>"
				+"</nav>"
			+"</div>"
		+"</header>");

	writer.println("<section id='boxes'>"  
			+"<div class='box'>");
	
	if(loginUser!=null) {
		//writer.println(loginUser.getFname() );
		
			
		writer.println("<table style='align:left'>");
	    writer.println("<form action='' method=''>");
	    writer.println("<tr><td style='width:150px'>"
	    + "<input type='radio' name='payment' value='paypal' style='pointer-events:none;'"
	    + " onclick=\"window.location='https://www.google.com/'\"> PayPal </td>");
	    writer.println("<tr><td style='width:200px'><input type='radio' name='payment' value='creditdebit'> Credit or Debit Card </td>");
	    writer.println("</form>");
	    writer.println("</table>");
	    
	    
	    

		
	} // end of if(loginUser!=null)
	else if(loginUser==null) {
		
		writer.println("Signin before proceeding to buy");
		
	} // end of else if(loginUser==null)
	
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
	
}// end of checkoutForm() method

	private Product getProduct(int pId) {
		 
		 for(Product product: prodList) {
			 
			 if(product.getPid() ==pId) {
				 
				 return product;
			 }
			 
		 }
		 return null;
	 }

}











