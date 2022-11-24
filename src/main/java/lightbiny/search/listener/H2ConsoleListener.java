package lightbiny.search.listener;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("local")
public class H2ConsoleListener {
	private Server webServer;

	@EventListener(ContextRefreshedEvent.class)
	public void start() throws SQLException {
		this.webServer = Server.createWebServer("-webPort", "8090", "-tcpAllowOthers").start();
		log.info("h2 console started at port 8090");
	}
	
	@EventListener(ContextClosedEvent.class)
	public void stop() {
		this.webServer.stop();
		log.info("h2 console stopped.");
	}
}
