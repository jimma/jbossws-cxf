/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.ws.jaxws.cxf.jbws3879;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceFeature;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.common.gzip.GZIPFeature;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.jboss.ws.api.configuration.ClientConfigUtil;
import org.jboss.ws.api.configuration.ClientConfigurer;
import org.jboss.wsf.stack.cxf.client.UseThreadBusFeature;
import org.jboss.wsf.test.ClientHelper;

public class Helper implements ClientHelper
{
   private String gzipFeatureEndpointURL;
   
   public Helper()
   {
      
   }

   public Helper(String endpointURL)
   {
      setTargetEndpoint(endpointURL);
   }

   public boolean testGZIPUsingFeatureOnClient() throws Exception
   {
      Bus bus = BusFactory.newInstance().createBus();
      try
      {
         BusFactory.setThreadDefaultBus(bus);
         
         HelloWorld port = getPort();
         
         ClientConfigurer configurer = ClientConfigUtil.resolveClientConfigurer();
         configurer.setConfigProperties(port, "jaxws-client-config.xml", "Feature Client Config");
         
         return "foo".equals(port.echo("foo"));
      }
      finally
      {
         bus.shutdown(true);
      }
   }
   
   public boolean testGZIPServerSideOnlyInterceptorOnClient() throws Exception
   {
      Bus bus = BusFactory.newInstance().createBus();
      try
      {
         BusFactory.setThreadDefaultBus(bus);
         
         HelloWorld port = getPort();
         Client client = ClientProxy.getClient(port);
         HTTPConduit conduit = (HTTPConduit)client.getConduit();
         HTTPClientPolicy policy = conduit.getClient();
         //enable Accept gzip, otherwise the server will not try to reply using gzip
         policy.setAcceptEncoding("gzip;q=1.0, identity; q=0.5, *;q=0");
         //add interceptor for decoding gzip message
         
         ClientConfigurer configurer = ClientConfigUtil.resolveClientConfigurer();
         configurer.setConfigProperties(port, "jaxws-client-config.xml", "Interceptor Client Config");
         
         return ("foo".equals(port.echo("foo")));
      }
      finally
      {
         bus.shutdown(true);
      }
   }
   
   public boolean testFailureGZIPServerSideOnlyInterceptorOnClient() throws Exception
   {
      HelloWorld port = getPort();
      Client client = ClientProxy.getClient(port);
      HTTPConduit conduit = (HTTPConduit)client.getConduit();
      HTTPClientPolicy policy = conduit.getClient();
      //enable Accept gzip, otherwise the server will not try to reply using gzip
      policy.setAcceptEncoding("gzip;q=1.0, identity; q=0.5, *;q=0");
      try
      {
         port.echo("foo");
         return false;
      }
      catch (Exception e)
      {
         //expected exception, as the client is not able to decode gzip message
         return true;
      }
   }
   
   private HelloWorld getPort(WebServiceFeature... features) throws MalformedURLException
   {
      URL wsdlURL = new URL(gzipFeatureEndpointURL + "?wsdl");
      QName serviceName = new QName("http://org.jboss.ws/jaxws/cxf/jbws3879", "HelloWorldService");
      Service service = Service.create(wsdlURL, serviceName, new UseThreadBusFeature());
      QName portQName = new QName("http://org.jboss.ws/jaxws/cxf/jbws3879", "HelloWorldImplPort");
      return (HelloWorld) service.getPort(portQName, HelloWorld.class, features);
   }

   @Override
   public void setTargetEndpoint(String address)
   {
      this.gzipFeatureEndpointURL = address;
   }
}