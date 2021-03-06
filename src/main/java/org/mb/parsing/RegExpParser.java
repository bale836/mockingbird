package org.mb.parsing;

import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Dmitriy Dzhevaga on 15.09.2015.
 */
public final class RegExpParser extends AbstractParser {
    private static final Logger log = Logger.getLogger(RegExpParser.class);

    public RegExpParser(String text) {
        super(text);
    }

    @Override
    public String parse(String path) throws ParsingException {
        Pattern p;
        try {
            p = Pattern.compile(path);
        } catch (PatternSyntaxException e) {
            log.error(e);
            throw new ParsingException(e);
        }
        Matcher m = p.matcher(text);
        if (m.find()) {
            if (m.groupCount() > 0) {
                return m.group(1);
            } else {
                return m.group(0);
            }
        } else {
            return "";
        }
    }
}
