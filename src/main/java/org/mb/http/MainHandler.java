package org.mb.http;

import com.google.common.collect.Maps;
import org.mb.http.basic.Request;
import org.mb.http.basic.Response;
import org.mb.http.basic.Handler;
import org.mb.http.mapping.HandlerDataMapping;
import org.mb.settings.Settings;
import org.mb.parsing.BulkParser;

import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public class MainHandler implements Handler {
    private final Settings settings;

    public MainHandler(Settings settings) {
        this.settings = settings;
    }

    @Override
    public Response handle(Request request) {
        Map<String, String> parsingResults = Maps.newHashMap();
        BulkParser bulkParser = settings.getBulkParser();
        parsingResults.putAll(bulkParser.parse(request.getContent()));
        HandlerDataMapping.HandlerData handlerData = settings.getMapping().find(request, parsingResults);
        return handlerData.getResponse();
    }
}