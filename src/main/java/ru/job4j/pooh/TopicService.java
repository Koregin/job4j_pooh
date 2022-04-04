package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        return switch (req.httpRequestType()) {
            case "POST" -> {
                var getTopic = topics.get(req.getSourceName());
                if (getTopic != null) {
                    getTopic.forEach((id, queue) -> queue.add(req.getParam()));
                }
                yield new Resp("", "200");
            }
            case "GET" -> {
                topics.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
                /*Add subscriber with queue if absent*/
                topics.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
                var text = topics.get(req.getSourceName()).get(req.getParam()).poll();
                yield new Resp(text != null ? text : "", text != null ? "200" : "204");
            }
            default -> new Resp("", "501");
        };
    }
}
