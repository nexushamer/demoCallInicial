package com.aos.clientewatson.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class ControladorServlet
 */
@WebServlet("/ControladorServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
				maxFileSize=1024*1024*50,      	// 50 MB
				maxRequestSize=1024*1024*100)
public class ControladorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControladorServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Part partArchivo = request.getPart("userfile");
		
		for (Part part : request.getParts()) 
		{
			//if(!part.getName().equals("") && part.getName().equals("userfile"))
			//{
				InputStream is = part.getInputStream();
				byte[] buffer = new byte[is.available()];
				is.read(buffer);
			 
			    File targetFile = new File("/Users/sergioalexandermendietaarias/Documents/ServidoresAplicacion/jboss-eap-6.1_original/bin/audio.wav");
			    OutputStream outStream = new FileOutputStream(targetFile);
			    outStream.write(buffer);
			    
			    if(outStream != null)
			    	outStream.close();
			//}
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}

}
