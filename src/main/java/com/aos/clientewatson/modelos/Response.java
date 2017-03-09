package com.aos.clientewatson.modelos;

import java.io.Serializable;

public class Response implements Serializable
{
	private static final long serialVersionUID = 8258676591314170367L;
	private String mensaje;
	
	public Response(){}
	
	public String getMensaje() 
	{
		return mensaje;
	}
	
	public void setMensaje(String mensaje) 
	{
		this.mensaje = mensaje;
	}
}
