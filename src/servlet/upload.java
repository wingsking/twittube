package servlet;

import java.io.IOException;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.soap.AddressingFeature.Responses;

import com.amazonaws.Response;

import object.Video;
import query.VideoQuery;
import aws.RDSManager;
import aws.S3BucketManager;

/**
 * Servlet implementation class upload
 */
public class upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public upload() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// need to get real filepath in the client file system
		String filepath = request.getParameter("filepath");
		String name = request.getParameter("name");
		String comment = request.getParameter("comment");
		
		S3BucketManager s3 = new S3BucketManager();
		String url = s3.upload(filepath);
		
		Video v = new Video();
		v.setName(name);
		v.setComment(comment);
		v.setUrl(url);
		
		//connect to db and insert new video info
		VideoQuery vq = RDSManager.createVideoQuery();
		vq.insertVideo(v);
		
		//refresh
		response.sendRedirect("./");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
