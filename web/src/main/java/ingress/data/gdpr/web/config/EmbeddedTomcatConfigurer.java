/*
 * Copyright (C) 2014-2018 SgrAlpha
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ingress.data.gdpr.web.config;

import org.apache.catalina.core.AprLifecycleListener;
import org.apache.coyote.http11.Http11AprProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Configuration
public class EmbeddedTomcatConfigurer {

    private final boolean aprEnabled;
    private final Map<String, Object> connector;

    public EmbeddedTomcatConfigurer(
            @Value("${server.tomcat.customized.apr-enabled:false}") final boolean aprEnabled,
            @Value("${server.tomcat.customized.connector:#{null}}") final Map<String, Object> connector) {
        this.aprEnabled = aprEnabled;
        this.connector = connector;
    }

    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory container = new TomcatServletWebServerFactory();
        if (this.isAprEnabled()) {
            AprLifecycleListener arpLifecycle = new AprLifecycleListener();
            arpLifecycle.setUseAprConnector(true);
            container.setProtocol(Http11AprProtocol.class.getName());
            container.addContextLifecycleListeners(arpLifecycle);
        }
        return container;
    }

    @Bean
    public TomcatConnectorCustomizer connectorCustomizer() {
        return connector -> {
            Map<String, Object> props = this.getConnector().orElse(Collections.emptyMap());
            props.forEach(connector::setAttribute);
        };
    }

    private boolean isAprEnabled() {
        return aprEnabled;
    }

    private Optional<Map<String, Object>> getConnector() {
        return Optional.ofNullable(connector);
    }

}
