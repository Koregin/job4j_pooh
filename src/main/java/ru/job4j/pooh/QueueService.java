package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String name = req.getSourceName();
        return switch (req.httpRequestType()) {
            case "POST" -> {
                queue.putIfAbsent(name, new ConcurrentLinkedQueue<>());
                var result = queue.get(req.getSourceName()).add(req.getParam());
                yield new Resp("", result ? "200" : "204");
            }
            case "GET" -> {
                var text = queue.getOrDefault(name, new ConcurrentLinkedQueue<>()).poll();
                yield new Resp(text != null ? text : "", text != null ? "200" : "204");
            }
            default -> new Resp("", "501");
        };
    }
}

