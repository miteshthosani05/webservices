package oracle.apps.hcm.hwr.coreService.adapters;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * This class provides an application-wide access to the
 * Spring ApplicationContext! The ApplicationContext is
 * injected in a static method of the class "AppContext".
 *
 * Use AppContext.getApplicationContext() to get access
 * to all Spring Beans.
 *
 * @author Siegfried Bolz
 */
public class HWRApplicationContextProvider implements ApplicationContextAware {

    public void setApplicationContext(final ApplicationContext pApplicationContext) throws BeansException {
        /**System.out.println("Start HWRApplicationContextProvider setApplicationContext");
        // Wiring the ApplicationContext into a static method
        //sApplicationContext = ctx;
        System.out.println("End HWRApplicationContextProvider setApplicationContext");

        String[] myBeans =
            pApplicationContext.getBeanNamesForType(IHWRPublicService.class);
        String myBean = null;
        for (String myTempBean : myBeans) {
            System.out.println("Bean Information = " + myTempBean);
            myBean = myTempBean;
        }
       IHWRPublicService myService = (IHWRPublicService)pApplicationContext.getBean(myBeans[0]);
       HWRServiceHolder myHWRServiceHolder = new HWRServiceHolder();
       myHWRServiceHolder.setHWRPublicService(myService);
    **/
    }
}
