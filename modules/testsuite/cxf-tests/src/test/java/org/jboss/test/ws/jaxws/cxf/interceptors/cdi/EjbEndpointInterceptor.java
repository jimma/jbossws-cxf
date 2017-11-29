package org.jboss.test.ws.jaxws.cxf.interceptors.cdi;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

public class EjbEndpointInterceptor  extends AbstractPhaseCdiInterceptor<Message>
{
   @Inject
   private CDIBean cdiBean;
   private Name name;
   private String constructMessage;
   @Inject
   public EjbEndpointInterceptor(@ShortDefault Name name)
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
      constructMessage = "EjbEndpointInterceptorPostConstructed";
   }
}
