package org.jboss.test.ws.jaxws.cxf.interceptors.cdi;

import javax.annotation.PostConstruct;

public class CDIBean
{
   private String cdiValue;

   public CDIBean()
   {

   }

   public String getValue()
   {
      return cdiValue;
   }

   @PostConstruct
   public void init()
   {
      cdiValue = "CDIBeanValue";

   }

}
