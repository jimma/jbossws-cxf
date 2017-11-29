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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

public class EndpointInterceptor extends AbstractPhaseCdiInterceptor<Message>
{
   @Inject
   private CDIBean cdiBean;
   private Name name;
   private String constructMessage;
   @Inject
   public EndpointInterceptor(@ShortDefault Name name)
   {
      super(Phase.RECEIVE);
      this.name = name;
   }
   
   
   
   public void handleMessage(Message message) throws Fault
   {
      StringBuilder sb = message.get(StringBuilder.class);
      if (sb == null) {
         sb = new StringBuilder();
         message.put(StringBuilder.class, sb);
      }
      sb.append(cdiBean.getValue()+"|"+ name.getValue()+"|"+constructMessage);
   }
   
   @PostConstruct
   public void reset () {
      constructMessage = "EndpointInterceptorPostConstructed";
   }
}
