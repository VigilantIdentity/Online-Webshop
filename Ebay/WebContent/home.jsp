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
<title>Ebay | Buying </title>
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
<%! int login_id; %>
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
			
	<ul>
		<li> <a href="loginServlet?id=<%=login_id%>">Sign in</a> 
		<li> <a href="registrationServlet">register</a>
	</ul>
	
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
		
		<hr>
		<a href="buyOrCartServlet"> View Cart</a>
	
	
	<%
		} else{
	%>
	
		<b>Hi</b> <%=userName %>
		<ul>
			<li> <a href="logoutServlet">Sign out</a>
			<li> <a href="productServlet">Sell</a>
			<li> <a href="productcategoryServlet">Product Categories</a>
		</ul>
		
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
			
		<hr>
		<a href="buyOrCartServlet"> View Cart</a>
		
	<%
	
		}
	%>

</body>
</html>