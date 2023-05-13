/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2023, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.test.ws.jaxws.k8s;
import static org.wildfly.test.cloud.common.WildflyTags.KUBERNETES;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.wildfly.test.cloud.common.WildFlyCloudTestCase;
import org.wildfly.test.cloud.common.WildFlyKubernetesIntegrationTest;

import io.dekorate.testing.annotation.Inject;
import io.fabric8.kubernetes.api.model.KubernetesList;
import io.fabric8.kubernetes.client.KubernetesClient;

import io.fabric8.kubernetes.api.model.Pod;
import java.util.List;
import org.wildfly.test.cloud.common.KubernetesResource;


/**
 * @author <a href="mailto:ema@redhat.com">Jim Ma</a>
 *
 */
@Tag(KUBERNETES)
@WildFlyKubernetesIntegrationTest(
        buildEnabled=false,
        deployEnabled=false,
        kubernetesResources = {
                @KubernetesResource(
                        definitionLocation = "src/test/resources/kubernetes.yml"
                ),}
)
public class EndpointTestCaseIT extends WildFlyCloudTestCase {
    @Inject
    private KubernetesClient client;
    @Inject
    private KubernetesList kubernetesList;

    @Test
    public void  checkWSEndpoint() throws Exception {
        List<Pod> lst = client.pods().withLabel("app.kubernetes.io/name","jbossws-cxf-k8s-tests").list().getItems();
        Assertions.assertEquals(1, lst.size(), "More than one pod found with expected label " + lst);
        Pod first = lst.get(0);
        Assertions.assertNotNull(first);
    }

}