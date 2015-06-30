package sample;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.session.ExpiringSession;
import org.springframework.session.events.SessionDestroyedEvent;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.MapEvent;

public class HazelcastSessionListener implements EntryListener<String, ExpiringSession> {

	private ApplicationEventPublisher eventPublisher;
	
	public HazelcastSessionListener(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	public void entryAdded(EntryEvent<String, ExpiringSession> event) {
		// Not required
	}

	public void entryRemoved(EntryEvent<String, ExpiringSession> event) {
		System.out.println("Session removed: " + event);
		publishSessionDestroyedEvent(event.getKey());
	}
	
	private void publishSessionDestroyedEvent(String sessionId) {
		System.out.println("Session destroyed " + sessionId);
	    eventPublisher.publishEvent(new SessionDestroyedEvent(this, sessionId));
	}

	public void entryUpdated(EntryEvent<String, ExpiringSession> event) {
		// Not required
	}

	public void entryEvicted(EntryEvent<String, ExpiringSession> event) {
		System.out.println("Session evicted: " + event);
		publishSessionDestroyedEvent(event.getKey());
	}

	public void mapEvicted(MapEvent event) {
		// Not required.
	}

	public void mapCleared(MapEvent event) {
		// Not required.
	}
}
