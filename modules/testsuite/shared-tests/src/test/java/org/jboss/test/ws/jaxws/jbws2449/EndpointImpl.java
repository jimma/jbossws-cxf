/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.test.ws.jaxws.jbws2449;

import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

import org.jboss.ws.api.annotation.WebContext;

@Stateless
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
@WebContext(contextRoot="jaxws-jbws2449", urlPattern="/*")
@WebService
(
   name = "Endpoint",
   portName = "EndpointPort",
   serviceName = "EndpointService",
   targetNamespace = "http://org.jboss.ws/jbws2449",
   wsdlLocation = "META-INF/wsdl/test.wsdl"
)
public class EndpointImpl implements Endpoint
{
   @WebMethod
   public String echo(String input)
   {
      return input;
   }
}
