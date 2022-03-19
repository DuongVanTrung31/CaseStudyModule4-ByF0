package cg.casestudy4f0.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;

@WebListener
public class HttpSessionListenerConfig implements HttpSessionListener {

    private static final Logger LOG= LoggerFactory.getLogger(HttpSessionListenerConfig.class);

    private final AtomicInteger activeSessions;

    public HttpSessionListenerConfig() {
        super();
        activeSessions = new AtomicInteger();
    }


    /**
     * This method will be called when session created
     * @param sessionEvent
     */
    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        LOG.info("-------Incrementing Session Counter--------");
        activeSessions.incrementAndGet();
        LOG.info("-------Session Created--------");
        sessionEvent.getSession().setAttribute("activeSessions",activeSessions.get());
        LOG.info("Total Active Session : {} ", activeSessions.get());
    }

    /**
     * This method will be automatically called when session destroyed
     * @param sessionEvent
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        LOG.info("-------Decrementing Session Counter--------");
        activeSessions.decrementAndGet();
        sessionEvent.getSession().setAttribute("activeSessions",activeSessions.get());
        LOG.info("-------Session Destroyed--------");
    }

}
