package org.jboss.wsf.stack.cxf.interceptor;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Interceptor;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.jboss.wsf.spi.deployment.Endpoint;
import org.jboss.wsf.stack.cxf.client.configuration.InterceptorUtils;

public class CdiInjectionInterceptor extends AbstractPhaseInterceptor<Message>
{
   public CdiInjectionInterceptor()
   {
      super(Phase.RECEIVE);
      addAfter(EndpointAssociationInterceptor.class.getName());
   }

   @Override
   public void handleMessage(Message message) throws Fault
   {

      Exchange exchange = message.getExchange();
      Endpoint endpoint = exchange.get(Endpoint.class);
      org.apache.cxf.endpoint.Endpoint cxfEndpoint = exchange.getEndpoint();
      synchronized (cxfEndpoint)
      {
         if (cxfEndpoint.get("isAddInterceptors") != null)
         {
            return;
         }
         Object manager = endpoint.getAttachment(BeanManager.class);
         if (manager != null)
         {
            BeanManager beanManager = (BeanManager)manager;
            InterceptorUtils.addInterceptors(exchange.getEndpoint(), endpoint.getEndpointConfig().getProperties(), beanManager);
         }
         else
         {
            InterceptorUtils.addInterceptors(exchange.getEndpoint(), endpoint.getEndpointConfig().getProperties());
            
         }
         cxfEndpoint.put("isAddInterceptors", true);
      }
   }
}
