package org.jboss.wsf.stack.cxf.interceptor;

import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.StringMap;
import org.apache.cxf.phase.AbstractPhaseInterceptor;

public abstract class AbstractTCCLPhaseInterceptor<T extends Message> extends AbstractPhaseInterceptor<T> {
    public AbstractTCCLPhaseInterceptor(String phase) {
        super(null, phase, false);
    }

    public AbstractTCCLPhaseInterceptor(String i, String p) {
        super(i, p, false);
    }

    public AbstractTCCLPhaseInterceptor(String phase, boolean uniqueId) {
        super(null, phase, uniqueId);
    }

    public AbstractTCCLPhaseInterceptor(String i, String p, boolean uniqueId) {
        super(i,p, uniqueId);
    }

    @Override
    public void handleMessage(T message) throws Fault {
        ClassLoaderUtils.ClassLoaderHolder origLoader = null;
        try {
            origLoader = ClassLoaderUtils.setThreadContextClassloader(this.getClass().getClassLoader());
            handleMessageWithTCCL(message);
        } finally {
            origLoader.reset();
        }
    }
    public abstract void handleMessageWithTCCL(T message) throws Fault;
}
