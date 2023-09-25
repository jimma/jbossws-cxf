package org.jboss.test.ws.jaxws.samples.wsse.policy.basic;
import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.security.SimpleAuthorizingInterceptor;
import org.apache.cxf.message.Message;

public class RolesInteceptor extends SimpleAuthorizingInterceptor {

    @Override
    public void handleMessage(Message message) throws Fault {
        super.handleMessage(message);
    }

    public RolesInteceptor() {
        super();
        readRoles();
    }

    private void readRoles() {
        Map<String, String> roles = new HashMap<String, String>();
        String ruoli = "friend";
        roles.put("sayHello", ruoli);
        setMethodRolesMap(roles);
    }
}
