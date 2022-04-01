package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String text = "";
        if ("POST".equals(req.httpRequestType())) {
            /*put param for all subscribers*/
            topics.getOrDefault(req.getSourceName(), null)
                    .forEach((id, queue) -> queue.add(req.getParam()));
        }
        if ("GET".equals(req.httpRequestType())) {
            /*Add topic if absent*/
            topics.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
            /*Add subscriber with queue if absent*/
            topics.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            text = topics.get(req.getSourceName()).get(req.getParam()).poll();
        }
        return new Resp(text != null ? text : "", text != null ? "200" : "204");
    }
}
