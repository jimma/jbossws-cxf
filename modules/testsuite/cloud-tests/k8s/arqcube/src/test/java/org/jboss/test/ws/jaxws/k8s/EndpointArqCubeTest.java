/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jboss.test.ws.jaxws.k8s;

import io.fabric8.kubernetes.api.model.Service;
import java.io.IOException;
import java.net.URL;
import javax.xml.namespace.QName;
import org.arquillian.cube.kubernetes.annotations.KubernetesResource;
import org.arquillian.cube.kubernetes.annotations.Named;
import org.arquillian.cube.kubernetes.annotations.PortForward;
import org.arquillian.cube.kubernetes.impl.requirement.RequiresKubernetes;
import org.arquillian.cube.requirement.ArquillianConditionalRunner;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.test.ws.jaxws.container.Endpoint;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;


/**
 * @author <a href="mailto:ema@redhat.com">Jim Ma</a>
 *
 */
@Category(RequiresKubernetes.class)
@RequiresKubernetes
@RunWith(ArquillianConditionalRunner.class)
@KubernetesResource("classpath:kubernetes.yml")
public class EndpointArqCubeTest {

    @Named("jbossws-cxf-k8s-basic")
    @ArquillianResource
    private Service endpointSevice;

    @Named("jbossws-cxf-k8s-basic")
    @PortForward
    @ArquillianResource
    private URL url;

    @Test
    public void shouldFindServiceInstance() throws IOException {
        assertNotNull(endpointSevice);
        assertNotNull(endpointSevice.getSpec());
        assertNotNull(endpointSevice.getSpec().getPorts());
    }

    @Test
    public void checkWSEndpoint() throws Exception {
        assertNotNull(url);
        URL baseURL = new URL(url + "/EndpointImpl");
        Endpoint endpoint = initPort(baseURL);
        String  echoed = endpoint.echo("from k8s pod");
        Assert.assertEquals("Echo:from k8s pod", echoed);
    }

    /*@Test
    public void  checkWSEndpoint(@InjectKubeClient KubernetesClient kubeClient) throws Exception {
        List<Pod> lst = kubeClient.pods().withLabel("app.kubernetes.io/name", APP_NAME).list().getItems();
        Assertions.assertEquals(1, lst.size(), "More than one pod found with expected label " + lst);
        Pod first = lst.get(0);
        Assertions.assertNotNull(first, "pod isn't created");
        Assertions.assertEquals("Running", first.getStatus().getPhase(), "Pod isn't running");
        LocalPortForward p = kubeClient.services().withName(APP_NAME).portForward(8080);
        Assertions.assertTrue(p.isAlive());
        URL baseURL = new URL("http://localhost:" + p.getLocalPort() + "/" + APP_NAME + "/EndpointImpl");
        Endpoint endpoint = initPort(baseURL);
        String  echoed = endpoint.echo("from k8s pod");
        Assertions.assertEquals("Echo:from k8s pod", echoed);
    }*/

    private Endpoint initPort(URL baseUrl) throws Exception {
        QName serviceName = new QName("http://org.jboss.ws/cxf/container", "EndpointImplService");
        URL wsdlURL = new URL(baseUrl + "?wsdl");
        jakarta.xml.ws.Service service = jakarta.xml.ws.Service.create(wsdlURL, serviceName);
        Endpoint proxy = service.getPort(Endpoint.class);
        return proxy;
    }
}