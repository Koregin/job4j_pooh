package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String name = "weather";
        queue.putIfAbsent(name, new ConcurrentLinkedQueue<>());
        String text = "";
        if ("POST".equals(req.httpRequestType())) {
            queue.get(req.getSourceName()).add(req.getParam());
        } else {
            text = queue.get(name).poll();
        }
        return new Resp(text, text != null ? "200" : "204");
    }
}

