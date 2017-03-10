package com.aos.clientewatson.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.aos.clientewatson.modelos.Response;
import com.google.gson.Gson;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

/**
 * Servlet implementation class ControladorServlet
 */
@WebServlet("/ControladorServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
				maxFileSize=1024*1024*50,      	// 50 MB
				maxRequestSize=1024*1024*100)
public class ControladorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static CountDownLatch lock = new CountDownLatch(1);   
	private static String resultado;
	
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
		System.out.println("antes de colocar el content type");
		response.setContentType( "application/json");
		System.out.println("gson");
		Gson gson = new Gson();
		System.out.println("Response");
		Response resp = new Response();
		System.out.println("resultado.equals()");
		if(resultado.equals(""))
			resp.setMensaje("No ha termnado la traduccion");
		else
			resp.setMensaje(resultado);
		
		System.out.println("finalizando para escribir la respuesta del get");
		response.getWriter( ).println( gson.toJson( resp));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Part partArchivo = request.getPart("userfile");
		
		InputStream is = null;
		
		for (Part part : request.getParts()) 
		{
			//if(!part.getName().equals("") && part.getName().equals("userfile"))
			//{
				is = part.getInputStream();
				/*
				byte[] buffer = new byte[is.available()];
				is.read(buffer);
			 
			    File targetFile = new File("/Users/sergioalexandermendietaarias/Documents/ServidoresAplicacion/jboss-eap-6.1_original/bin/audio.wav");
			    OutputStream outStream = new FileOutputStream(targetFile);
			    outStream.write(buffer);
			    
			    if(outStream != null)
			    	outStream.close();
			    */
			//}
        }
		
		System.out.println("antes de llamar a la api sppect to text");
		SpeechToText service = new SpeechToText();
		System.out.println("colocando el usuario y el password");
	    service.setUsernameAndPassword("290ee3c4-358a-42b2-91d5-c330431cda85", "dZjzQlJCMWrQ");
	    /*
	    RecognizeOptions options = new RecognizeOptions.Builder().continuous(true).interimResults(true)
	            .contentType(HttpMediaType.AUDIO_WAV).model("es-ES_BroadbandModel").build();
	    */
	    RecognizeOptions options = new RecognizeOptions.Builder().continuous(true).interimResults(true)
	            .contentType(HttpMediaType.AUDIO_WAV).model("es-ES_NarrowbandModel").build();
	    
	    System.out.println("despues de crear las opciones");
	    
	    service.recognizeUsingWebSocket(is, options, new BaseRecognizeCallback() {
	      @Override
	      public void onTranscription(SpeechResults speechResults) 
	      {
	    	  System.out.println("transcripcio termino");
	    	  System.out.println(speechResults);
	    	  resultado = speechResults.toString();
	      }
	
	      @Override
	      public void onDisconnected() {
	        lock.countDown();
	      }
	    });
	    
	    try 
	    {
			lock.await(1, TimeUnit.MINUTES);
		} 
	    catch (InterruptedException e) 
	    {
	    	System.out.println("hubo una interrupcion del servicio " + e.getMessage());
		}
	    System.out.println("esperando a que el servicio termine");
	}

}
