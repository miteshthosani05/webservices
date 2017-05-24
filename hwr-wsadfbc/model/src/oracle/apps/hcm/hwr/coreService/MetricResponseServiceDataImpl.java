package oracle.apps.hcm.hwr.coreService;

import org.eclipse.persistence.sdo.SDODataObject;

public class MetricResponseServiceDataImpl extends SDODataObject implements MetricResponseServiceData {

   public static final int START_PROPERTY_INDEX = 0;

   public static final int END_PROPERTY_INDEX = START_PROPERTY_INDEX + 1;

   public MetricResponseServiceDataImpl() {}

   public java.lang.Long getCompletedItems() {
      return getLong(START_PROPERTY_INDEX + 0);
   }

   public void setCompletedItems(java.lang.Long value) {
      set(START_PROPERTY_INDEX + 0 , value);
   }

   public java.lang.String getTimeStampofCompletion() {
      return getString(START_PROPERTY_INDEX + 1);
   }

   public void setTimeStampofCompletion(java.lang.String value) {
      set(START_PROPERTY_INDEX + 1 , value);
   }


}

