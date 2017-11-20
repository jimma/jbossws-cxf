package org.jboss.test.ws.jaxws.cxf.interceptors.cdi;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * This Extension handles default scopes for discovered JAX-RS components. It
 * also observes ProcessInjectionTarget event and wraps InjectionTargets
 * representing JAX-RS components within JaxrsInjectionTarget. Furthermore, it
 * builds the sessionBeanInterface map which maps Session Bean classes to a
 * local interface. This map is used in CdiInjectorFactory during lookup of
 * Sesion Bean JAX-RS components.
 *
 * @author Jozef Hartinger
 *
 */
public class JBossWSCdiExtension implements Extension
{
   private static boolean active;
   
   private static BeanManager beanManager;
   /*private static final String JAVAX_EJB_STATELESS = "javax.ejb.Stateless";
   private static final String JAVAX_EJB_SINGLETON = "javax.ejb.Singleton";

   private final List<Class> providers = new ArrayList<Class>();
   private final List<Class> resources = new ArrayList<Class>();*/

   // Scope literals
 /*  public static final Annotation requestScopedLiteral = new AnnotationLiteral<RequestScoped>()
   {
      private static final long serialVersionUID = 3381824686081435817L;
   };
   public static final Annotation applicationScopedLiteral = new AnnotationLiteral<ApplicationScoped>()
   {
      private static final long serialVersionUID = -8211157243671012820L;
   };*/

   public static boolean isCDIActive()
   {
      return active;
   }
   
   /*private Map<Class<?>, Type> sessionBeanInterface = new HashMap<Class<?>, Type>();*/

   /**
    * Obtain BeanManager reference for future use.
    */
   public void observeBeforeBeanDiscovery(@Observes BeforeBeanDiscovery event, BeanManager beanManager)
   {
      this.beanManager = beanManager;
      active = true;
   }
   public static BeanManager getBeanManager() {
      if (beanManager != null) {
          return beanManager;
      }

      // Do a lookup for BeanManager in JNDI (this is the only *portable* way)
      beanManager = LookupBeanManager("java:comp/BeanManager");
      if (beanManager != null)
      {
         
         return beanManager;
      }

      // Do a lookup for BeanManager at an alternative JNDI location (workaround for WELDINT-19)
      beanManager = LookupBeanManager("java:app/BeanManager");
      if (beanManager != null)
      {
         return beanManager;
      }
      return null;
   }
   
   private static BeanManager LookupBeanManager(String name) {
      
         try
         {
            InitialContext ctx = new InitialContext();
            return (BeanManager) ctx.lookup(name);
         }
         catch (NamingException e)
         {

            return null;
         }
         catch (NoClassDefFoundError ncdfe)
         {
          
            return null;
         }
   }

   private void setBeanManager(BeanManager beanManager)
   {
       if (this.beanManager == null) {
           // this may happen if Solder Config receives BBD first
           this.beanManager = beanManager;
       }
   }

}