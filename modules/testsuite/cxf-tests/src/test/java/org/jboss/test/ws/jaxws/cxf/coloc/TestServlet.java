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

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.ws.Endpoint;
import jakarta.xml.ws.Service;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.xml.namespace.QName;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.binding.coloc.feature.ColocFeature;
import org.jboss.test.ws.jaxws.cxf.coloc.HelloWorldImpl;
import org.jboss.ws.common.utils.AddressUtils;

@WebServlet(name = "TestServlet", urlPatterns = "/invoke")
public class TestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Pattern VALID_IPV6_PATTERN;
    private static final String ipv6Pattern = "^([\\dA-F]{1,4}:|((?=.*(::))(?!.*\\3.+\\3))\\3?)([\\dA-F]{1,4}(\\3|:\\b)|\\2){5}(([\\dA-F]{1,4}(\\3|:\\b|$)|\\2){2}|(((2[0-4]|1\\d|[1-9])?\\d|25[0-5])\\.?\\b){4})\\z";
    static
    {
        try
        {
            VALID_IPV6_PATTERN = Pattern.compile(ipv6Pattern, Pattern.CASE_INSENSITIVE);
        }
        catch (PatternSyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Endpoint _endpoint;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        String hostName = toIPv6URLFormat(System.getProperty("jboss.bind.address", "localhost"));
        String serviceURL = "http://" + hostName + ":48084/HelloWorldService";
        _endpoint = Endpoint.publish(serviceURL, new HelloWorldImpl());
    }

    @Override
    public void destroy()
    {
        _endpoint.stop();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        URL wsdlURL = new URL("http://" + toIPv6URLFormat(req.getLocalAddr()) + ":" + 48084 + "/HelloWorldService?wsdl");
        QName qname = new QName("http://org.jboss.ws/jaxws/cxf/coloc", "HelloWorldService");
        BusFactory.getDefaultBus().getOutInterceptors().add(new ColocCheckInterceptor());
        Service service = Service.create(wsdlURL, qname, new ColocFeature());
        //Service service = Service.create(wsdlURL, qname);
        HelloWorld hello =  (HelloWorld) service.getPort(HelloWorld.class);
        res.getWriter().write(hello.sayHello());
    }

    private String toIPv6URLFormat(final String host)
    {
        boolean isIPv6URLFormatted = false;
        //strip out IPv6 URL formatting if already provided...
        if (host.startsWith("[") && host.endsWith("]")) {
            isIPv6URLFormatted = true;
        }
        //return IPv6 URL formatted address
        if (isIPv6URLFormatted) {
            return host;
        } else {
            Matcher m = VALID_IPV6_PATTERN.matcher(host);
            return m.matches() ? "[" + host + "]" : host;
        }
    }
}
