package oracle.apps.hcm.hwr.coreService.adapters;

import java.util.logging.Level;

import oracle.apps.fnd.applcore.log.AppsLogger;
import oracle.apps.hcm.hwr.webservices.server.HWREndorsementService;
import oracle.apps.hcm.hwr.webservices.server.HWRMetricService;
import oracle.apps.hcm.hwr.webservices.server.HWRPublicService;
import oracle.apps.hcm.hwr.webservices.server.IHWREndorsementService;
import oracle.apps.hcm.hwr.webservices.server.IHWRMetricService;
import oracle.apps.hcm.hwr.webservices.server.IHWRPublicService;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;


/**
 * The Class holds the Instance of HWRPublicService.
 */
public class HWRServiceHolder {

    private IHWRPublicService mHWRPublicService;
    
    private IHWRMetricService mHWRMetricService;
    
    private IHWREndorsementService mHWREndorsementService;

    private static final HWRServiceHolder sInstance = new HWRServiceHolder();

    public static HWRServiceHolder getInstance() {
        return sInstance;
    }

    /**
     * Private Constructor which loads all the Spring COnfig XML files.
     *      */
    private HWRServiceHolder() {
            if(AppsLogger.isEnabled(Level.FINE)){
                AppsLogger.write(this, "Start HWRServiceHolder Constructor", Level.FINE);
            }

            BeanFactoryLocator beanFactoryLocator = ContextSingletonBeanFactoryLocator.getInstance("classpath:hwr-core-config.xml");
            BeanFactoryReference beanFactoryReference = beanFactoryLocator.useBeanFactory("hwr.core");
            final BeanFactory beanFactory = beanFactoryReference.getFactory();
            mHWRPublicService = (HWRPublicService)beanFactory.getBean("HWRPublicService");
            mHWRMetricService=(HWRMetricService)beanFactory.getBean("HWRMetricService");
            mHWREndorsementService=(HWREndorsementService)beanFactory.getBean("HWREndorsementService");
            if(AppsLogger.isEnabled(Level.FINE)){
                AppsLogger.write(HWRServiceHolder.class, "End HWRServiceHolder Constructor", Level.FINE);
            }

        }

    public IHWRPublicService getHWRPublicService() {
        return mHWRPublicService;
    }

    public IHWRMetricService getHWRMetricService() {
        return mHWRMetricService;
    }

    public IHWREndorsementService getHWREndorsementService() {
        return mHWREndorsementService;
    }
}
