/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.ws.jaxws.samples.wsse.policy.trust;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.ws.WebServiceProvider;

import org.apache.cxf.annotations.EndpointProperties;
import org.apache.cxf.annotations.EndpointProperty;
import org.apache.cxf.interceptor.InInterceptors;
import org.apache.cxf.sts.StaticSTSProperties;
import org.apache.cxf.sts.operation.TokenIssueOperation;
import org.apache.cxf.sts.operation.TokenValidateOperation;
import org.apache.cxf.sts.service.ServiceMBean;
import org.apache.cxf.sts.service.StaticService;
import org.apache.cxf.sts.token.provider.SAMLTokenProvider;
import org.apache.cxf.sts.token.validator.SAMLTokenValidator;
import org.apache.cxf.ws.security.sts.provider.SecurityTokenServiceProvider;

@WebServiceProvider(serviceName = "SecurityTokenService",
      portName = "UT_Port",
      targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/",
      wsdlLocation = "WEB-INF/wsdl/ws-trust-1.4-service.wsdl")
@EndpointProperties(value = {
      @EndpointProperty(key = "ws-security.signature.username", value = "mystskey"),
      @EndpointProperty(key = "ws-security.signature.properties", value = "stsKeystore.properties"),
      @EndpointProperty(key = "ws-security.callback-handler", value = "org.jboss.test.ws.jaxws.samples.wsse.policy.trust.STSCallbackHandler")      
})
@InInterceptors(interceptors = {"org.jboss.wsf.stack.cxf.security.authentication.SubjectCreatingPolicyInterceptor"})
public class SampleSTS extends SecurityTokenServiceProvider
{
   public SampleSTS() throws Exception
   {
      super();
      
      StaticSTSProperties props = new StaticSTSProperties();
      props.setSignaturePropertiesFile("stsKeystore.properties");
      props.setSignatureUsername("mystskey");
      props.setCallbackHandlerClass(STSCallbackHandler.class.getName());
      props.setIssuer("DoubleItSTSIssuer");
      
      List<ServiceMBean> services = new LinkedList<ServiceMBean>();
      StaticService service = new StaticService();
      service.setEndpoints(Arrays.asList("http://localhost:(\\d)*/jaxws-samples-wsse-policy-trust/SecurityService", "http://\\[::1\\]:(\\d)*/jaxws-samples-wsse-policy-trust/SecurityService"));
      services.add(service);
      
      TokenIssueOperation issueOperation = new TokenIssueOperation();
      issueOperation.setServices(services);
      issueOperation.getTokenProviders().add(new SAMLTokenProvider());
      issueOperation.setStsProperties(props);
      
      TokenValidateOperation validateOperation = new TokenValidateOperation();
      validateOperation.getTokenValidators().add(new SAMLTokenValidator());
      validateOperation.setStsProperties(props);
      
      this.setIssueOperation(issueOperation);
      this.setValidateOperation(validateOperation);
   }
}
