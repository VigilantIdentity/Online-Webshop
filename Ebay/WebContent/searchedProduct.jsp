<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import= "java.util.*, com.ebay.products.*, java.text.NumberFormat" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
 
 	<a href="home.jsp"> Back to home page</a>
  
<%
	NumberFormat currencyFormat=null;	
    List<Product> searchProdList=null;
	
	session= request.getSession(false);
	if(session!=null){
		
		searchProdList=(List<Product>) session.getAttribute("searchProd");
		if(searchProdList!=null){
%>
		<ul>

<%
			for(Product availProd:searchProdList){ 
	%>			
				 	<li><%= availProd.getName() + "   Price  "+ availProd.getPrice() %>  
					<a href="productDetailServlet?pId=<%=availProd.getPid() %>"> Detail</a>  
			
 <%
	
	       } // end of for(Product availProd:searchProdList)
			
%>

	</ul>
<%	
        } // end of if(session.getAttribute("searchProd").equals("searchProd")){
		else{
%>
                 <h3> No Product is searched </h3>			
<%			
       }
	  
		
	} // end of if(session!=null)
	else{
		
	
%>
			<h3> Session is out please sign in again </h3>
<%
	}
%>
	

</body>
</html>