<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<meta name="msapplication-TileColor" content="#ffffff">
<meta name="msapplication-TileImage"
 content="${pageContext.request.contextPath}/favicon/ms-icon-144x144.png"
>
<meta name="theme-color" content="#ffffff">
<title>Lucene Search</title>

<!-- Bootstrap Core CSS -->
<link href="${pageContext.request.contextPath}\vendor/bootstrap/css/bootstrap.min.css"
 rel="stylesheet"
>

<!-- MetisMenu CSS -->
<link href="${pageContext.request.contextPath}\vendor/metisMenu/metisMenu.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}\dist/css/sb-admin-2.css" rel="stylesheet">

<!-- Morris Charts CSS -->
<link href="${pageContext.request.contextPath}\vendor/morrisjs/morris.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="${pageContext.request.contextPath}\vendor/font-awesome/css/font-awesome.min.css"
 rel="stylesheet" type="text/css"
>

<!-- jQuery -->
<script src="${pageContext.request.contextPath}\vendor/jquery/jquery.min.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="${pageContext.request.contextPath}\vendor/bootstrap/js/bootstrap.min.js"></script>

<!-- Metis Menu Plugin JavaScript -->
<script src="${pageContext.request.contextPath}\vendor/metisMenu/metisMenu.min.js"></script>

<!-- Custom Theme JavaScript -->
<script src="${pageContext.request.contextPath}\dist/js/sb-admin-2.js"></script>

<script type="text/javascript">
	function logout() {
<%request.setAttribute("MSG", "Logged out successfully.");
			response.addHeader("Cache-Control",
					"no-cache,no-store,private,must-revalidate,max-stale=0,post-check=0,pre-check=0");
			response.addHeader("Pragma", "no-cache");
			response.addDateHeader("Expires", 0);%>
	window.location.href = "${pageContext.request.contextPath}";
	}

	function setDefault() {
		<c:if test="${not empty _search}">
		document.getElementById('_search').value = "${_search}";
		</c:if>

		<c:if test="${not empty dataMap}">
		document.getElementById('DataGrid').style.display = 'inline';
		document.getElementById('DataGrid').style.display = 'block';
		document.getElementById('DataGrid').style.overflow = 'auto';
		</c:if>

		<c:if test="${empty dataMap && not empty headerMap}">
		document.getElementById('DataGrid_Fail').style.display = 'inline';
		document.getElementById('DataGrid_Fail').style.display = 'block';
		</c:if>

		$.ajax({
			type : 'get',
			cache : false,
			url : '${pageContext.request.contextPath}/TagCloud',
			success : function(response) {
				// We get the element having id of display_info and put the response inside it
				$('#display_info').html(response);
			}
		});

	}

	function clearForm() {
		document.getElementById("Search").reset();
		clearError();
	}

	function printPDF(button) {
		document.getElementById("button").value = button.id;
		document.getElementById("print_form").submit();
	}

	function clearError() {
		document.getElementById("DataGrid").style.display = 'none';
		document.getElementById("DataGrid_Fail").style.display = 'none';

	}

	function selectTag(search) {
		document.getElementById('_search').value = search;
		document.getElementById('Search').submit();

	}
</script>

</head>
<body onload="Javascript:setDefault();">

 <div id="wrapper">

  <!-- Navigation -->
  <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">

   <div class="navbar-header">
    <a class="navbar-brand" href="${pageContext.request.contextPath}\jsp\UI_Screen\main.jsp"
     style="color: #337ab7;"
    > <i class="fa fa-ravelry fa-fw"></i> Lucene Search
    </a>
   </div>
   <!-- /.navbar-header -->

   <ul class="nav navbar-top-links navbar-right">

    <!-- /.dropdown -->
    <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#"> <i
      class="fa fa-user fa-fw"
     ></i> Welcome Sourabh Parsekar
    </a> <!-- /.dropdown-user --></li>
    <!-- /.dropdown -->
   </ul>
   <!-- /.navbar-top-links -->
  </nav>

  <div id="page-wrapper">
   <div class="row">
    <div class="col-lg-12">
     <h3 class="page-header">
      <i class="fa fa-spinner fa-pulse fa-1x fa-fw"></i> Search
     </h3>
    </div>
    <!-- /.col-lg-12 -->
   </div>
   <!-- /.row -->

   <div class="row">
    <div class="col-lg-7">
     <div class="panel panel-primary">
      <div class="panel-heading panel-title">
       <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne"
        style="text-decoration: none;"
       ><i class="fa fa-search fa-fw" aria-hidden="true"></i> Search Records<small> [click
         here to hide/unhide]</small></a>
      </div>
      <div id="collapseOne" class="panel-collapse collapse in">
       <div class="panel-body">
        <div class="row">
         <div class="col-lg-9">
          <form id="Search" role="form" method="post"
           action="${pageContext.request.contextPath}/DataController"
          >
           <input type="hidden" name="_print_page" id="_print_page" value="/index.jsp">
           <div class="form-group">
            <label>Search Tag</label> <input name="_search" id="_search" class="form-control"
             placeholder="Enter 'Search Criteria'" oninput="Javascript:clearError();"
             onchange="Javascript:clearError();" required="required"
            >
           </div>


           <button type="submit" class="btn btn-primary">
            <i class="fa fa-search fa-fw" aria-hidden="true"></i> Search
           </button>
           <button type="button" class="btn btn-default" onclick="clearForm();">
            <i class="fa fa-refresh fa-fw" aria-hidden="true"></i> Clear
           </button>
          </form>
         </div>
        </div>
        <!-- /.row (nested) -->
       </div>
       <!-- /.panel-body -->
      </div>
      <!-- /.panel collapse-->
     </div>
     <!-- /.panel -->
    </div>
    <!-- /.col-lg-9 -->
    <div class="col-lg-5">
     <div class="panel panel-primary">
      <div class="panel-heading panel-title">
       <a data-toggle="collapse" data-parent="#accordion2" href="collapseTwo"
        style="text-decoration: none;"
       ><i class="fa fa-tags fa-fw" aria-hidden="true"></i> Tag Cloud<small></small></a>
      </div>
      <div id="collapseTwo" class="panel-collapse collapse in">
       <div class="panel-body">
        <div class="row">
         <div class="col-lg-9">
          <form id="Search" role="form" method="post"
           action="${pageContext.request.contextPath}/DataController"
          >
           <input type="hidden" name="_print_page" id="_print_page" value="/index.jsp">
           <div class="form-group">
            <p id="display_info">Loading Search Tags. Please wait...</p>
           </div>

          </form>
         </div>
        </div>
        <!-- /.row (nested) -->
       </div>
       <!-- /.panel-body -->
      </div>
      <!-- /.panel collapse-->
     </div>
     <!-- /.panel -->
    </div>
    <!-- /.col-lg-3 -->

   </div>
   <!-- /.row -->

   <div class="row" id="DataGrid" style="display: none; overflow: scroll;">
    <div class="col-lg-12">
     <div class="panel panel-info">
      <div class="panel-heading">
       <i class="fa fa-th-large fa-fw"></i> Retrieved Records
      </div>
      <!-- /.panel-heading -->
      <div class="panel-body">
       <div class="table-responsive">
        <form id="print_form" target="_blank"
         action="${pageContext.request.contextPath}/DataReceiver" method="post"
        >
         <input type="hidden" name="_print_PDFpage" id="_print_PDFpage"
          value="/jsp/card_print/single_card_print.jsp"
         >
         <div style="overflow: auto;">
          <table id="dataTables" class="table table-striped table-bordered table-hover">
           <thead>
            <tr align="center">
             <th></th>
             <c:forEach items="${headerMap}" var="element">
              <th style="text-align: center; font-size: 12px;">${element.value}<input
               type="hidden" name="${element.key}" id="${element.key}" value="${element.value}"
              >
              </th>
             </c:forEach>
            </tr>
           </thead>

           <tbody>
            <c:set var="count" value="0" scope="page" />
            <c:forEach items="${dataMap}" var="row">
             <tr>
              <td><c:set var="count" value="${count + 1}" scope="page" /> <input type="radio"
               name="row" id="row" value="${count}" checked="checked"
              ></td>
              <c:forEach items="${row}" var="dataElement">
               <td>${dataElement.value}</td>
              </c:forEach>
             </tr>
            </c:forEach>
           </tbody>

          </table>

          <input type="hidden" name="_numberOfColumns" id="_numberOfColumns"
           value="${fn:length(headerMap)}"
          > <input type="hidden" name="_numberOfRows" id="_numberOfRows" value="${count}">

         </div>
         <button id="print" type="button" class="btn btn-info" onclick="return printPDF(this);">
          <i class="fa fa-print fa-fw" aria-hidden="true"></i> Get File
         </button>

         <!--          <button id="preview" type="button" class="btn btn-warning" onclick="return printPDF(this);">
          <i class="fa fa-address-card fa-fw" aria-hidden="true"></i> Preview
         </button>
 -->
         <input type="hidden" name="button" id="button">

        </form>
       </div>
       <!-- /.table-responsive -->
      </div>
      <!-- /.panel-body -->
     </div>
     <!-- /.panel -->
    </div>
    <!-- /.col-lg-12 -->
   </div>
   <!-- /.row -->

   <div class="row col-lg-8" id="DataGrid_Fail" style="display: none; overflow: auto;" align="left">
    <div class="col-lg-12">
     <div class="panel panel-default">
      <div class="panel-heading" align="left">
       <i class="fa fa-exclamation-circle fa-fw" aria-hidden="true"></i>Alerts
      </div>
      <!-- /.panel-heading -->
      <div class="panel-body">
       <div class="alert alert-warning">
        No Results available for your Search Criteria. Please check again. <a onclick="clearForm();"
         class="alert-link"
        >Click here to reload.</a>
       </div>
      </div>
      <!-- .panel-body -->
     </div>
     <!-- /.panel -->
    </div>
    <!-- /.col-lg-12 -->
   </div>
   <!-- /.row -->


  </div>
  <!-- /#page-wrapper -->
 </div>
 <!-- /#wrapper -->


 <!-- DataTables JavaScript -->
 <script src="${pageContext.request.contextPath}\vendor/datatables/js/jquery.dataTables.min.js"></script>
 <script
  src="${pageContext.request.contextPath}\vendor/datatables-plugins/dataTables.bootstrap.min.js"
 ></script>
 <script
  src="${pageContext.request.contextPath}\vendor/datatables-responsive/dataTables.responsive.js"
 ></script>

 <script type="text/javascript">
		$(document).ready(function() {
			$('#dataTables').DataTable({
				"scrolly" : true
			});
		});
	</script>


</body>
</html>