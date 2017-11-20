package org.jboss.test.ws.jaxws.cxf.interceptors.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CDITarget
{
   @Inject
   private CDIBean bean;
   public CDITarget()
   {
     
   }

   public String getValue()
   {
      return bean.getValue();
   }

}