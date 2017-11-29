package org.jboss.test.ws.jaxws.cxf.interceptors.cdi;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebServiceContext;

import org.apache.cxf.message.Message;

@WebService(name = "EJB3Endpoint", targetNamespace = "http://org.jboss.ws.jaxws.cxf/interceptors", serviceName = "EJB3Service", portName = "EJB3EndpointPort")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@Stateless
public class EJB3Endpoint{

   @Resource
   WebServiceContext ctx;

   
   @WebMethod
   public String echo(String input)
   {
      Message cxfMessage = (Message)ctx.getMessageContext().get(Message.class.getName());
      return input + " " + cxfMessage.get(StringBuilder.class).toString();
   }
}