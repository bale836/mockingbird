package org.mb.http.basic;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public interface HTTPServer {
    void start() throws Exception;
    void setHandler(Handler handler);
}