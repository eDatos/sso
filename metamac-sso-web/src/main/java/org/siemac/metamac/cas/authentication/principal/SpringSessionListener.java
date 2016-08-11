package org.siemac.metamac.cas.authentication.principal;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringSessionListener implements HttpSessionListener {

    private boolean eventOccurring_ = false;

    @Override
    public synchronized void sessionCreated(HttpSessionEvent arg0) {
        if (!eventOccurring_) {
            eventOccurring_ = true;
            HttpSession session = arg0.getSession();
            WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
            String[] allBeans = ctx.getBeanNamesForType(ConfigurationService.class);

            for (String bean : allBeans) {
                Object obj = ctx.getBean(bean);
                session.setAttribute(bean, obj);
            }
            eventOccurring_ = false;
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent arg0) {
    }
}
