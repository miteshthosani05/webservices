package oracle.apps.hcm.hwr.coreService.serviceProvider;



public class MetricData {
  public MetricData() {
    super();
  }


  private long CompletedItems;
  private String TimeStamp;


  public void setCompletedItems(long CompletedItems) {
    this.CompletedItems = CompletedItems;
  }

  public long getCompletedItems() {
    return CompletedItems;
  }


  public void setTimeStamp(String TimeStamp) {
    this.TimeStamp = TimeStamp;
  }

  public String getTimeStamp() {
    return TimeStamp;
  }


}
