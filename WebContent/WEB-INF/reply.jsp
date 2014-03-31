<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
	import="aws.*"
	import="config.*"
	import="object.*"
	import="query.*"
	import="java.util.ArrayList"    
%>

<% 
final String rtmpAddr = Config.getConfigByName("rtmpAddr");
final String httpAddr = Config.getConfigByName("httpAddr");


VideoQuery vq = RDSManager.createVideoQuery();
//execute query and get all origin videos
ArrayList<Video> videos = vq.getVideos();

//String rid = request.getParameter("id");
String rid = "2";
ArrayList<Video> main = vq.getVideoByID(rid);

//get reply videos
ArrayList<Video> replies = vq.getRepliesByID(rid);

//out.println(videos.size());

String email = null;
boolean onlyShowMy = false;

Object o = session.getAttribute("email");
if(o!=null) {
	email = o.toString();
	//id = session.getAttribute("id").toString();
}
if( onlyShowMy==true && email!=null) {
	//videos = videoDAO.getVideosByOwner(id);
}
else{
	videos = vq.getVideos();	
}

%>

<!DOCTYPE html >

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="description" content="" />
<meta name="keywords" content="" />

    <!-- Bootstrap core CSS -->
    <link href="./css/bootstrap.css" rel="stylesheet"> 
    <link href="./css/carousel.css" rel="stylesheet">
    <link href="./css/sticky-footer-navbar.css" rel="stylesheet">
    
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
    <script src="./js/bootstrap.min.js"></script>
    
    
	<link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,700,900,300italic" rel="stylesheet" />
		<script src="js/jquery.min.js"></script>
		<script src="js/jquery.dropotron.min.js"></script>
		<!--  <script src="js/config.js"></script> -->
		<script src="js/config.js">
		<script src="js/skel.min.js"></script>
		<script src="js/skel-panels.min.js"></script>
		
		<script type='text/javascript' src=<%=httpAddr %>></script>
		
	<noscript>
		<link rel="stylesheet" href="css/skel-noscript.css" />
		<link rel="stylesheet" href="css/style.css" />
		<link rel="stylesheet" href="css/style-desktop.css" />
	</noscript>



	<!--[if lte IE 8]><script src="js/html5shiv.js"></script><link rel="stylesheet" href="css/ie8.css" /><![endif]-->
   
    

</head>

<title>Twitt-tube</title>

</head>

<body>
<!-- Replace RTMP-DISTRIBUTION-DOMAIN-NAME with the domain name of your 
RTMP distribution, for example, s5678.cloudfront.net (begins with "s").

Replace VIDEO-FILE-NAME with the name of your .mp4 or .flv video file, 
including the .mp4 or .flv filename extension. For example, if you uploaded 
my-vacation.mp4, enter my-vacation.mp4.
-->

       <div class="navbar navbar-default navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container">
          
          <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
              <span class="sr-only">Toggle navigation</span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="./">Twitt <span class="glyphicon glyphicon-film"></span> tube</a>
          </div>
          
          <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
              <li class="active"><a href="#"><i>Slogan Slogan Slogan Slogan</i></a></li>
            </ul>
               
<% 
if(email==null) {
%>
	 		<form class="navbar-form navbar-right" action="./SignIn" method="Post">
            	<div class="form-group">
              		<input type="text" placeholder="Email" class="form-control" name="email">
            	</div>
            	<div class="form-group">
              		<input type="password" placeholder="Password" class="form-control" name="pwd">
            	</div>
            	<button type="submit" class="btn btn-success">Sign in</button>
     		</form>
<%
} 
else {
%>
		    <ul class="nav navbar-nav navbar-right">
         		<li><a href="./"><b>Hi <%=email %> </b></a></li>
         		<li> 
          			<a href="main.jsp?showFlg=true">My Posts<b class="caret"></b></a>
          		</li>
       	 		<li><a data-toggle="modal" href="#myModal">New Post</a><li>	 		
	 			<li>	 
           			<form class="navbar-form navbar-right" action="./SignOut" method="Post">
             			<button type="submit" class="btn btn-success">Sign Out</button>
	  				</form>
       	 		<li>
       		</ul>	 		
<% 
}
%>
        </div><!--/.nav-collapse -->
        
        </div><!-- container -->
      </div>

<!-- Carousel
    ================================================== -->
    <div id="myCarousel" class="container" data-ride="carousel">
     <header style="padding-top:70px">
     	<h1></h1>
		<h4><span class="glyphicon glyphicon-play"></span> <%=main.get(0).getName()%></h4>
      	<h5><span class="glyphicon glyphicon-time"></span> <%=main.get(0).getTimestamp() %></h5>
	</header>
	<section id="banner">
		<div id='<%=rid %>'></div>
		<script type="text/javascript">
		jwplayer('<%=rid %>').setup({
		file: "<%=rtmpAddr + main.get(0).getUrl() %>",
		width: 1024,
					      			
		});
		</script>
	</section>
	<footer>
		<a href="#myModal" data-toggle="modal" class="btn btn-success">Reply</a>
	</footer>
	
   <!--   <a class="left carousel-control" href="#myCarousel" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a>
      <a class="right carousel-control" href="#myCarousel" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a> -->
    </div><!-- /.carousel -->

<div class="container">
  	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  		<div class="modal-dialog">
    		<div class="modal-content">
      			<div class="modal-header">
        			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        			<h4 class="modal-title" id="myModalLabel">Upload File</h4>
      			</div>
      			<div class="modal-body">
        			<form class="form-horizontal" ACTION="replyUpload.jsp" ENCTYPE="multipart/form-data" method="Post">
						<div class="form-group">
    						<label for="title" class="col-sm-2 control-label">Title</label>
    						<div class="col-sm-10">
      							<input type="text" class="form-control" id="title" name="video_title" placeholder="Title">
    						</div>
  						</div>
  					    <div class="form-group">
					    	<label for="info" class="col-sm-2 control-label">Comment</label>
					    	<div class="col-sm-10">
					      		<textarea rows="3" class="form-control" id="info" name="video_info" placeholder="Brief intro"></textarea>
					    	</div>
					  	</div>
					  	<div class="form-group">
					    	<label for="file" class="col-sm-2 control-label">File</label>
					    	<div class="col-sm-10">
					      		<input type="file" id="file" name="file1">
					    	</div>
					  	</div>
					    <button type="submit" class="btn btn-success">Upload</button>					    
        			</form>
      			</div>
      			<div class="modal-footer">
        			<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      			</div>
    		</div><!-- /.modal-content -->
  		</div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	<hr>
	</div>
	
	
	
<div class="container">
	<div class="row"> <!-- for the first element -->
 <%
 int i = 0;
 if(videos!=null) {
	 for(Video v : videos) {
	 	if((++i)%3==0) {
	 		out.println("<div class=\"row\">");
	 	}
 %>
 	<div class="col-md-4">
      	<h4><span class="glyphicon glyphicon-play"></span> <%=v.getName()%></h4>
      	<h5><span class="glyphicon glyphicon-time"></span> <%=v.getTimestamp() %></h5>
      	<div id="<%=v.getID() %>"></div>
      	<p><%=v.getComment()%></p>

 <% 
 	if(onlyShowMy==true && email!=null) {
 %>
 			<table class="table table-condensed">
      			<tr>
      				<th>
      					<form action="./VideoDelServlet?id=<%=v.getID()%>" method="Post">
      						<button  type="sumbmit" class="btn btn-sm btn-warning"><span class="glyphicon glyphicon-trash"></span></button>
      					</form>
      				</th>
<%
	 }
%>
      			</tr>
      		</table>
      </div>
      
      <script type="text/javascript">
    	jwplayer("<%=v.getID() %>").setup({
        file: "<%=rtmpAddr+v.getUrl()%>",
        width: 350
    	});
	  </script>
 <% 	
 	if(i%3==0) {
		out.println("</div>");
	}
	 }
 }
 %>
   
   </div> <!--  for the last element -->

</div>



</body>
</html>