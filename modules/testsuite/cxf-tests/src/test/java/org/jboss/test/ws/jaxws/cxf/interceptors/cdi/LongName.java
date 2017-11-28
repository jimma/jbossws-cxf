package org.jboss.test.ws.jaxws.cxf.interceptors.cdi;

public class LongName implements Name
{

   @Override
   public String getValue()
   {
     return "CDI Injection Support";
   }

}
