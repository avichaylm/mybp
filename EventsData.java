package com.avichay.BPALM;

import javafx.util.Pair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by avichay.mulyan on 17/10/2016.
 */
public class EventsData implements EventsStatisticData {

    private Map<String, StatsEntry> eventsMap = new ConcurrentHashMap<String, StatsEntry>();

    public EventsData() {

    }

    @Override
    public void add(String eventType, String eventData) {

        if (eventType !=null) {
            StatsEntry value = eventsMap.get(eventType);
            if (value == null) {
                value = eventsMap.computeIfAbsent(eventType, k -> new StatsEntry());
            }
            synchronized (value) {
                value.getCount().incrementAndGet();
                value.addWord(eventData);
            }
        }
    }

    @Override
    public Pair<Long, Long> getEventsStats(String eventType) {

        long countEvents = 0;
        long countWords = 0;
        if (eventType != null) {
            //   AtomicLong res =
            StatsEntry entry = eventsMap.get(eventType);
            if (entry != null) {

                synchronized (entry) {
                    countEvents = entry.getCount().get();
                    countWords = entry.getWords();
                }
            }
        }
        return new Pair<Long, Long>(countEvents, countWords);
    }








    private class StatsEntry {

        private AtomicLong count;
        private ConcurrentHashMap<String, String> wordsSet;

        public StatsEntry() {
            this.count = new AtomicLong();
            this.wordsSet = new ConcurrentHashMap<String, String>();
        }


        public AtomicLong getCount() {
            return count;
        }

        public void addWord(String word) {
            wordsSet.put(word, "");
        }

        public long getWords() {
            return wordsSet.size();
        }
    }
}
