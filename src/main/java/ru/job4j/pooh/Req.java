package ru.job4j.pooh;

public class Req {
    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String param = "";
        String[] request = content.split(System.lineSeparator());
        String[] methodAndPath = request[0].split("\\s+");
        String httpRequestType = methodAndPath[0];
        String[] pathArr = methodAndPath[1].split("/");
        String poohMode = pathArr[1];
        String sourceName = pathArr[2];
        if ("GET".equals(httpRequestType)) {
            if (pathArr.length >= 4) {
                param = pathArr[3];
            }
        } else {
            param = request[request.length - 1];
        }

        return new Req(httpRequestType, poohMode, sourceName, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}
