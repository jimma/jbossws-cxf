package org.jboss.test.ws.jaxws.cxf.interceptors.cdi;

import java.util.Collection;
import java.util.Set;

import org.apache.cxf.common.util.SortedArraySet;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.PhaseInterceptor;

public abstract class AbstractPhaseCdiInterceptor<T extends Message> implements PhaseInterceptor<T> {
   private  String id;
   private  String phase;
   private  Set<String> before = new SortedArraySet<String>();
   private  Set<String> after = new SortedArraySet<String>();

   /**
    * Instantiates the interceptor to live in a specified phase. The
    * interceptor's id will be set to the name of the implementing class.
    *
    * @param phase the interceptor's phase
    */
   public AbstractPhaseCdiInterceptor(String phase) {
       this(null, phase, false);
   }

   /**
    * Instantiates the interceptor with a specified id.
    *
    * @param i the interceptor's id
    * @param p the interceptor's phase
    */
   public AbstractPhaseCdiInterceptor(String i, String p) {
       this(i, p, false);
   }

   /**
    * Instantiates the interceptor and specifies if it gets a system
    * determined unique id. If <code>uniqueId</code> is set to true the
    * interceptor's id will be determined by the runtime. If
    * <code>uniqueId</code> is set to false, the implementing class' name
    * is used as the id.
    *
    * @param phase the interceptor's phase
    * @param uniqueId true to have a unique ID generated
    */
   public AbstractPhaseCdiInterceptor(String phase, boolean uniqueId) {
       this(null, phase, uniqueId);
   }

   /**
    * Instantiates the interceptor with a specified id or with a system
    * determined unique id. The specified id will be used unless
    * <code>uniqueId</code> is set to true.
    *
    * @param i the interceptor's id
    * @param p the interceptor's phase
    * @param uniqueId
    */
   public AbstractPhaseCdiInterceptor(String i, String p, boolean uniqueId) {
       if (i == null) {
           i = getClass().getName();
       }
       if (uniqueId) {
           i += System.identityHashCode(this);
       }
       id = i;
       phase = p;
   }

   /**
    * Specifies that the current interceptor needs to be added to the
    * interceptor chain before the specified collection of interceptors.
    * This method replaces any existing list with the provided list.
    *
    * @param i a collection of interceptor ids
    */
   public void setBefore(Collection<String> i) {
       before.clear();
       before.addAll(i);
   }

   /**
    * Specifies that the current interceptor needs to be added to the
    * interceptor chain after the specified collection of interceptors.
    * This method replaces any existing list with the provided list.
    *
    * @param i a collection of interceptor ids
    */
   public void setAfter(Collection<String> i) {
       after.clear();
       after.addAll(i);
   }

   /**
    * Specifies that the current interceptor needs to be added to the
    * interceptor chain before the specified collection of interceptors.
    *
    * @param i a collection of interceptor ids
    */
   public void addBefore(Collection<String> i) {
       before.addAll(i);
   }

   /**
    * Specifies that the current interceptor needs to be added to the
    * interceptor chain after the specified collection of interceptors.
    *
    * @param i a collection of interceptor ids
    */
   public void addAfter(Collection<String> i) {
       after.addAll(i);
   }

   /**
    * Specifies that the current interceptor needs to be added to the
    * interceptor chain before the specified interceptor.
    *
    * @param i an interceptor id
    */
   public void addBefore(String i) {
       before.add(i);
   }

   /**
    * Specifies that the current interceptor needs to be added to the
    * interceptor chain after the specified interceptor.
    *
    * @param i an interceptor id
    */
   public void addAfter(String i) {
       after.add(i);
   }


   public Set<String> getAfter() {
       return after;
   }

   public  Set<String> getBefore() {
       return before;
   }

   public Collection<PhaseInterceptor<? extends Message>> getAdditionalInterceptors() {
       return null;
   }

   public  String getId() {
       return id;
   }

   public  String getPhase() {
       return phase;
   }


   public void handleFault(T message) {
   }

   public boolean isGET(T message) {
       String method = (String)message.get(Message.HTTP_REQUEST_METHOD);
       return "GET".equals(method);
   }

   /**
    * Determine if current messaging role is that of requestor.
    *
    * @param message the current Message
    * @return true if the current messaging role is that of requestor
    */
   protected boolean isRequestor(T message) {
       return MessageUtils.isRequestor(message);
   }

}
