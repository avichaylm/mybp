package com.avichay.BPALM;

import javafx.util.Pair;

/**
 * Created by avichay.mulyan on 17/10/2016.
 */
public interface EventsStatisticData {


    public void add(String eventType, String data);
    public Pair<Long, Long> getEventsStats(String eventType);


}
