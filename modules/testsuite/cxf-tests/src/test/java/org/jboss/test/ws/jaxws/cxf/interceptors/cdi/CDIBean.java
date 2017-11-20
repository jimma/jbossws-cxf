package org.jboss.test.ws.jaxws.cxf.interceptors.cdi;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CDIBean
{
   public CDIBean()
   {

   }

   public String getValue()
   {
      return "CDIBeanValue";
   }

}
