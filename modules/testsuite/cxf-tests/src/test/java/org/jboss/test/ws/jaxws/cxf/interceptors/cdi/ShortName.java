package org.jboss.test.ws.jaxws.cxf.interceptors.cdi;

import javax.enterprise.inject.Default;

@ShortDefault
public class ShortName implements Name
{

   @Override
   public String getValue()
   {
      return "CDI";
   }

}
