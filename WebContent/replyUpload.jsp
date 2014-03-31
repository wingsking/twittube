<%@page import="query.VideoQuery"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="com.jspsmart.upload.*"
    import="aws.*"
	import="config.*"
	import="object.*"
	import="query.*"
	import="java.util.ArrayList"   
    %>
    
<%
	//create VideoQuery
	VideoQuery videoDAO = RDSManager.createVideoQuery();
	String rid = request.getParameter("rid");
	System.out.println(rid);
	
	SmartUpload s = new SmartUpload();
	s.initialize(pageContext); 
	s.setMaxFileSize(500*1024*1024);
	s.upload();
	
	int num = s.getFiles().getCount();
// 	System.out.println(num); 
	String url = null;
	for (int i = 0; i < num; i++) {
		com.jspsmart.upload.File file = s.getFiles().getFile(i);
	
		if (!file.isMissing()) {
			String fname = file.getFileName();
		    String suffix = fname.substring(0, fname.lastIndexOf('.'));
		    String ext = s.getFiles().getFile(0).getFileExt();  
		    int fileSize = file.getSize();
		    String directory = getServletContext().getRealPath("/");
		    
		   	String trace = directory + fname;
    
		   	System.out.println("INFO: " + fname + " is saved as " + trace);
		  	
		   	file.saveAs(trace,s.SAVE_PHYSICAL);
	
			if (true) {
//		    if(ext.equals("flv") || ext.equals("mp4")) {
				S3BucketManager s3o = new S3BucketManager();
				String root = request.getSession().getServletContext().getRealPath("/");
				url = s3o.upload(trace);
				
				Video v = new Video();
				v.setName(s.getRequest().getParameter("video_title"));
				v.setComment(s.getRequest().getParameter("video_info"));
				v.setUrl(url);
				v.setOwner(request.getParameter("email"));
				
				System.out.println("before");
				videoDAO.replyToVideo(v, rid);
				System.out.println("after");
				
		    }
		    else{
		    	out.println("<html><body><script>alert(\"Only .flv and .mp4 video is allowed!\");</script></body></html>");
		    }
			
			java.io.File f = new java.io.File(trace);
			f.deleteOnExit();
			
			System.out.println("INFO: Delete "  + fname + " from web server.");
		}
	}
		
	Transcoder tc = new Transcoder();
	tc.transcode(url, "iphone");
	tc.transcode(url, "mobile");
		
	
	response.sendRedirect("reply.jsp?rid="+rid);
%>