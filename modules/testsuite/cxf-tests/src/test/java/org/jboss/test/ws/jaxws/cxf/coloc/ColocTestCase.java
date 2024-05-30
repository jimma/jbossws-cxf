/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jboss.test.ws.jaxws.cxf.coloc;

import java.io.File;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.ws.common.IOUtils;
import org.jboss.wsf.test.JBossWSTest;
import org.jboss.wsf.test.JBossWSTestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ArquillianExtension.class)
public class ColocTestCase extends JBossWSTest
{
   private static final String DEP = "jaxws-cxf-coloc";
   @ArquillianResource
   private URL baseURL;

   @Deployment(testable = false)
   public static WebArchive createDeployment()
   {
      WebArchive archive = ShrinkWrap.create(WebArchive.class, DEP + ".war");
      archive.setManifest(new StringAsset("Manifest-Version: 1.0\n"
           + "Dependencies: org.apache.cxf.impl\n"))
            .addClass(HelloWorld.class)
            .addClass(HelloWorldImpl.class)
            .addClass(TestServlet.class)
            .addClass(ColocCheckInterceptor.class)
            .setWebXML(new File(JBossWSTestHelper.getTestResourcesDir() + "/jaxws/cxf/coloc/WEB-INF/web.xml"));
      return archive;
   }

   @Test
   @RunAsClient
   public void testServerSide() throws Exception
   {
      URL url = new URL("http://" + getServerHost() + ":" + getServerPort() + "/jaxws-cxf-coloc/invoke");
      assertEquals("HelloWorld", IOUtils.readAndCloseStream(url.openStream()));
   }

}
