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
package org.jboss.test.ws.jaxws.samples.asynch;

import java.util.concurrent.Future;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.AsyncHandler;
import jakarta.xml.ws.Response;

@WebService(name = "EndpointService", targetNamespace = "http://org.jboss.ws/cxf/samples/asynch")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface Endpoint
{
   @WebMethod
   public String echo(String user);
   
   //The following methods are added (or generated by wsconsume) for the sake of using asynch jaxws client only
   
   @WebMethod(operationName = "echo")
   public Response<String> echoAsync(String user);
   
   @WebMethod(operationName = "echo")
   public Future<String> echoAsync(String user, AsyncHandler<String> handler);
}
