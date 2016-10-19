package com.avichay.BPALM;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by avichay.mulyan on 27/09/2016.
 */
public class MyEvent {


    private String timestamp;

    @JsonProperty("event_type")

    private String eventType;

    private String data;

    public String getTimestamp ()
    {
        return timestamp;
    }

    public void setTimestamp (String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getEventType ()
    {
        return eventType;
    }

    public void setEventType (String eventType)
    {
        this.eventType = eventType;
    }

    public String getData ()
    {
        return data;
    }

    public void setData (String data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [timestamp = "+timestamp+", event_type = "+eventType+", data = "+data+"]";
    }

}
