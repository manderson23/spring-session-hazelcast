/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.ExpiringSession;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.session.web.http.HttpSessionStrategy;

import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

// tag::class[]
@Configuration
public class Config {

 	private HttpSessionStrategy httpSessionStrategy;
	private HazelcastInstance instance;
	private String sessionMapName = "spring:session:sessions";
 	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
 	public Config()
 	{
 		// TODO handle creation and shutdown
 		com.hazelcast.config.Config cfg = new com.hazelcast.config.Config();
		NetworkConfig netConfig = new NetworkConfig();
		netConfig.setPort(24730);
		cfg.setNetworkConfig(netConfig);
		// TODO what is this all about?
		SerializerConfig serializer = new SerializerConfig()
			.setTypeClass(Object.class)
			.setImplementation(new ObjectStreamSerializer());
		cfg.getSerializationConfig().addSerializerConfig(serializer);
		MapConfig mc = new MapConfig();
		mc.setName(sessionMapName);

		mc.setMaxIdleSeconds(60);
		cfg.addMapConfig(mc);

		instance = Hazelcast.newHazelcastInstance(cfg);
 	}
 	
 	@Bean
 	public HazelcastSessionListener sessionListener() {
 		return new HazelcastSessionListener(eventPublisher);
 	}
 	
 	@Bean
	public MapSessionRepository sessionRepository(HazelcastSessionListener sessionListener) {
 		IMap<String,ExpiringSession> sessions = instance.getMap(sessionMapName);
 		String listenerId = sessions.addEntryListener(sessionListener, false);
 		// TODO remove listener on shutdown
		return new MapSessionRepository(sessions);
	}
 	
	@Bean
	public <S extends ExpiringSession> SessionRepositoryFilter<? extends ExpiringSession> springSessionRepositoryFilter(SessionRepository<S> sessionRepository, ServletContext servletContext) {
		SessionRepositoryFilter<S> sessionRepositoryFilter = new SessionRepositoryFilter<S>(sessionRepository);
		sessionRepositoryFilter.setServletContext(servletContext);
		if(httpSessionStrategy != null) {
			sessionRepositoryFilter.setHttpSessionStrategy(httpSessionStrategy);
		}
		return sessionRepositoryFilter;
	}
	
	@Autowired(required = false)
	public void setHttpSessionStrategy(HttpSessionStrategy httpSessionStrategy) {
		this.httpSessionStrategy = httpSessionStrategy;
	}
}
// end::class[]