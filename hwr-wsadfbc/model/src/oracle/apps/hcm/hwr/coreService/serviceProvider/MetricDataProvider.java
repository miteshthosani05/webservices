package oracle.apps.hcm.hwr.coreService.serviceProvider;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class MetricDataProvider {
    public MetricDataProvider() {
        super();
    }
    private Iterator<MetricData> iterator;

    public MetricDataProvider(final List<MetricData> MetricDataList) {
        if (MetricDataList == null) {
            iterator = (new ArrayList<MetricData>()).iterator();
        } else {
            iterator = MetricDataList.iterator();
        }
    }

    public Iterator<MetricData> getMetricDataIterator() {
        return iterator;
    }
}
