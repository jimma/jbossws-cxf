package org.jboss.test.ws.jaxws.cxf.interceptors.cdi;


@ShortDefault
public class ShortName implements Name
{

   @Override
   public String getValue()
   {
      return "ShortName";
   }

}
