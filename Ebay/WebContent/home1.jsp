<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="com.ebay.users.*" %> 
<%@ page import="com.ebay.users.servlet.*" %> 
<%@ page import="javax.servlet.http.HttpSession" %>  
<%@ page import="java.sql.*" %> 

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="css/style.css">
<title>Login Form</title>
</head>
<body>

	<%!

    Connection con=null;
	Statement st=null;

%>
<%

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
%>
<%! 
		int login_id=1;
		String viewCart="View Cart";
%>
<%
String guest_name=request.getParameter("guest_name");
if(guest_name==null || guest_name==""){
	login_id=1;
}else{
	login_id=0;
}
        
%>
	<% 
		String userName="";
		
		User loginUser = (User)  session.getAttribute("user");
		
		if(loginUser==null){
			loginUser = new User();
		}
		
		userName= loginUser.getFname();
		
if(userName==null || userName==""){
			
  %>	
	

	<header>
		<div class = "container">
			<div id = "branding">
				<img src="images/logo.jpg" width=50px height=50px>
				<h1> <span class="highlight"> elibrary</span></h1>
			</div>
			<nav>
				<ul>
					<li class= "current"> <a href="home1.jsp">Home</a>
					<li> <a href="loginServlet?id=<%=login_id%>">Sign in</a>
					<li><a href="registrationServlet">Register</a>
					<li><a href="buyOrCartServlet?buyCartView=<%=viewCart%>"> View Cart</a>
				</ul>
			</nav>
		</div>
	</header>
	 
		<form action="searchServlet" method="post">
<%		
			String invalidSearch=null;
			invalidSearch= (String) request.getAttribute("invalidSearch");
			if(invalidSearch!=null) {
%>
			<%=invalidSearch%><br><br>
			
<%			
			}
%>		
		
			<input type="text" id="pSearch" name="pSearch" placeholder="Search for anything">
			
			<select id="select_cat" name="select_cat">
			<option> All Categories</option>
			
	<%  String selectSql="SELECT * FROM product_category ORDER BY cat_id";
			try {
					
					ResultSet rs=st.executeQuery(selectSql);
					
					while(rs.next()) {
	 %>		
						<option><%= rs.getString("name") %></option>
	<%					
					}
						
			}catch(SQLException e) {
				e.printStackTrace();
			}
	 %>	
			</select>
			<input type="submit" id="search" name="search" value="Search">
			
			</form>
		
		
	<%
		} else if(loginUser.getEmail().equals("inam@gmail.com")){
	%>
	
		<header>
		<div class = "container">
			<div id = "branding">
				<img src="images/logo.jpg" width=50px height=50px>
				<h1> <span class="highlight"> elibrary</span></h1>
			</div>
			<nav>
				<ul>
					<li class= "current"> <i>Hi <b><%=userName %></b></i></li>
					<li><a href="productcategoryServlet">Product Categories</a>
					<li><a href="logoutServlet">Sign out</a>
				</ul>
			</nav>
		</div>
	</header>
	<h3>Admin Page</h3>
	<p>As an administrator you have certain privileges to block or delete abusive users. Moreover unwanted products
	or products with issues can be blocked or move to disputed table.It is your responsibility to insert product categories, which can be used by customer in their sale
	to place their products in relevant product category</p>
	
	Following are pages where product categories, user and product details can be viewed or edited.
	
	<ul>
		<li> Insert or edit <a href="#"> product categories</a>
		<li> Block or delete <a href="#"> user</a>
		<li> Block or delete <a href="#"> product</a>
		
	</ul>
		
	<%
		}else{
	%>	
	<header>
		<div class = "container">
			<div id = "branding">
				<img src="images/logo.jpg" width=50px height=50px>
				<h1> <span class="highlight"> elibrary</span></h1>
			</div>
			<nav>
				<ul>
					<li class= "current"> <i>Hi <b><%=userName %></b></i></li>
					<li><a href="productServlet">Sell</a>
					<li><a href="buyOrCartServlet"> View Cart</a>
					<li><a href="logoutServlet">Sign out</a>
				</ul>
			</nav>
		</div>
	</header>
	
	<form action="searchServlet" method="post">
		
		<%		
			String invalidSearch=null;
			invalidSearch= (String) request.getAttribute("invalidSearch");
			if(invalidSearch!=null) {
%>
			<%=invalidSearch%><br><br>
			
<%			
			}
%>
		
			<input type="text" id="pSearch" name="pSearch" placeholder="Search for anything">
			
			<select id="select_cat" name="select_cat">
			<option> All Categories</option>
			
	<%  String selectSql="SELECT * FROM product_category ORDER BY cat_id";
			try {
					
					ResultSet rs=st.executeQuery(selectSql);
					
					while(rs.next()) {
	 %>		
						<option><%= rs.getString("name") %></option>
	<%					
					}
						
			}catch(SQLException e) {
				e.printStackTrace();
			}
	 %>	
			</select>
			<input type="submit" id="search" name="search" value="Search">
			
			</form>
	
	<%
	
		}
	%>
	
		
	
	<section id="boxes">
		<div class="box">
			
		</div>
		<div class="box">
		
		</div>
	
	</section>
<footer>
		<div class="container">
                <div id="branding">
                        <img src="images/logo.jpg" alt="" width=50px height=50px>
                        <h1> <span class="highlight">elibrary</span></h1> 
                         <p><span class="copy">Copyright &copy; 2018</span></p>
                </div>
        </div>
	</footer>


</body>
</html>