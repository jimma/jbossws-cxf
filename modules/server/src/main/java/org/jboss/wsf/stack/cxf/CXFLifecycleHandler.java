/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2017, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.wsf.stack.cxf;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.ws.common.Loggers;
import org.jboss.ws.common.Messages;
import org.jboss.wsf.spi.deployment.Endpoint;
import org.jboss.wsf.spi.deployment.EndpointState;
import org.jboss.wsf.spi.deployment.LifecycleHandler;
import org.jboss.wsf.spi.invocation.InvocationHandler;
import org.jboss.wsf.stack.cxf.client.configuration.InterceptorUtils;

public class CXFLifecycleHandler implements LifecycleHandler
{
   private static CXFLifecycleHandler me;
   
   public static CXFLifecycleHandler getInstance()
   {
      if (me == null)
      {
         me = new CXFLifecycleHandler();
      }
      return me;
   }
   
   public void start(Endpoint ep)
   {
      EndpointState state = ep.getState();
      if (state != EndpointState.UNDEFINED)
      {
         Loggers.DEPLOYMENT_LOGGER.cannotStartEndpoint(state, ep.getName());
      }
      else
      {
         ClassLoader originLoader = SecurityActions.getContextClassLoader();
         ClassLoader newLoader = SecurityActions.createDelegateClassLoader(SecurityActions.getContextClassLoader(), ep.getService().getDeployment().getClassLoader());
         try
         {
            SecurityActions.setContextClassLoader(newLoader);
            if (ep.getEndpointMetrics() != null)
               ep.getEndpointMetrics().start();

            InvocationHandler invHandler = ep.getInvocationHandler();
            if (invHandler == null)
               throw Messages.MESSAGES.invocationHandlerNotAvailable(ep.getName());
            invHandler.init(ep);

            org.apache.cxf.endpoint.Endpoint cxfEp = ep.getAttachment(org.apache.cxf.endpoint.Endpoint.class);
            Object manager = ep.getAttachment(BeanManager.class);
            if (manager != null)
            {
               BeanManager beanManager = (BeanManager)manager;
               InterceptorUtils.addInterceptors(cxfEp, ep.getEndpointConfig().getProperties(), beanManager);
            }
            else
            {
               InterceptorUtils.addInterceptors(cxfEp, ep.getEndpointConfig().getProperties());

            }
         }
         finally
         {
            SecurityActions.setContextClassLoader(originLoader);
         }
         ep.setState(EndpointState.STARTED);
      }
   }

   public void stop(Endpoint ep)
   {
      EndpointState state = ep.getState();
      if (state == EndpointState.STOPPED) {
        //if the endpoint is stopped in EndpointServiceDA
        return;
      }
      if (state != EndpointState.STARTED)
      {
         Loggers.DEPLOYMENT_LOGGER.cannotStopEndpoint(state, ep.getName());
      }
      else
      {
         if (ep.getEndpointMetrics() != null)
            ep.getEndpointMetrics().stop();

         ep.setState(EndpointState.STOPPED);
      }
   }

}