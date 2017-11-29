/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.ws.jaxws.cxf.interceptors.cdi;

import java.io.File;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.wsf.test.JBossWSTest;
import org.jboss.wsf.test.JBossWSTestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 */
@RunWith(Arquillian.class)
public class CDIInterceptorsTestCase extends JBossWSTest
{
   @ArquillianResource
   private URL baseURL;
   
   @Deployment(testable = false)
   public static WebArchive createDeployment() {
      WebArchive archive = ShrinkWrap.create(WebArchive.class, "jaxws-cxf-cdi-interceptors.war");
      archive.setManifest(new StringAsset("Manifest-Version: 1.0\n"
                  + "Dependencies: org.apache.cxf\n"))
            .addClass(Endpoint.class)
            .addClass(EndpointImpl.class)
            .addClass(CDIBean.class)
            .addClass(Name.class)
            .addClass(LongName.class)
            .addClass(ShortName.class)
            .addClass(ShortDefault.class)
            .addClass(EndpointInterceptor.class)
            .addClass(EjbEndpointInterceptor.class)
            .addClass(BeanIface.class)
            .addClass(BeanImpl.class)
            .addClass(EJB3Endpoint.class)
            .addClass(AbstractPhaseCdiInterceptor.class)
            .addAsWebInfResource(new File(JBossWSTestHelper.getTestResourcesDir() + "/jaxws/cxf/interceptors/cdi/WEB-INF/beans.xml"), "beans.xml")
            .addAsResource(new File(JBossWSTestHelper.getTestResourcesDir() + "/jaxws/cxf/interceptors/cdi/WEB-INF/jaxws-endpoint-config.xml"));
      return archive;
   }

   @Test
   @RunAsClient
   public void testEndpointWithCDIInterceptors() throws Exception {
      URL wsdlURL = new URL(baseURL + "MyService?wsdl");
      Service service = Service.create(wsdlURL, new QName("http://org.jboss.ws.jaxws.cxf/interceptors", "MyService"));
      Endpoint port = service.getPort(new QName("http://org.jboss.ws.jaxws.cxf/interceptors", "MyEndpointPort"), Endpoint.class);
      assertEquals("Hi CDIBeanValue|ShortName|EndpointInterceptorPostConstructed|EndpointImplPostConstructeds|EjbBeanCalled", port.echo("Hi"));

   }
   
   @Test
   @RunAsClient
   public void testEJBEndpointWithCDIInterceptors() throws Exception {
      URL wsdlURL = new URL(baseURL + "/EJB3Service/EJB3Endpoint?wsdl");
      Service service = Service.create(wsdlURL, new QName("http://org.jboss.ws.jaxws.cxf/interceptors", "EJB3Service"));
      Endpoint port = service.getPort(new QName("http://org.jboss.ws.jaxws.cxf/interceptors", "EJB3EndpointPort"), Endpoint.class);
      assertEquals("Hi CDIBeanValue|ShortName|EjbEndpointInterceptorPostConstructed", port.echo("Hi"));

   }
}
