package org.jboss.test.ws.jaxws.cxf.coloc;

import jakarta.xml.ws.ProtocolException;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

public class ColocCheckInterceptor extends AbstractPhaseInterceptor<Message> {
    public ColocCheckInterceptor() {
        super(Phase.PREPARE_SEND);
    }

    public void handleMessage(Message message) throws Fault {
        Boolean isColoc = (Boolean)message.get("org.apache.cxf.message.Message.COLOCATED");
        if (isColoc == null || isColoc == Boolean.FALSE) {
            String str = new String("Collocated Invocation should have been detected.");
            throw new ProtocolException(str);
        }
    }
}