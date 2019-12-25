package de.jannek.price.tracking;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 21/12/2019 21:29
 */
public class PriceParser {

    private static final Pattern[] patterns = new Pattern[]{
            //<input type="hidden" id="attach-base-product-price" value="379.0" />
            Pattern.compile("<input\\stype=\"hidden\"\\sid=\"attach-base-product-price\"\\svalue=\"([0-9]+(?:\\.[0-9]{0,2}))?\"\\s\\/>"),


            //<span class="a-color-price a-text-bold">EUR 12,99</span>
            Pattern.compile("<span\\sclass=\"a-color-price\\sa-text-bold\">EUR\\s([0-9]+(?:\\,[0-9]{0,2}))?<\\/span>")
    };

    public String getPrice(final String content) {
        /*
        final Matcher matcher = Pattern
                //.compile("(?:<input\\stype=\"hidden\"\\sid=\"attach-base-product-price\"\\svalue=\"([0-9]+(?:\\.[0-9]{0,2}))?\"\\s\\/>|<span\\sclass=\"a-color-price\\sa-text-bold\">EUR\\s([0-9]+(?:,[0-9]{0,2}))?<\\/span>)")
                .compile("(?:<input\\stype=\"hidden\"\\sid=\"attach-base-product-price\"\\svalue=\"([0-9]+(?:\\.[0-9]{0,2}))?\"\\s\\/>|<span\\sclass=\"a-color-price\\sa-text-bold\">EUR\\s([0-9]+(?:\\,[0-9]{0,2}))?<\\/span>)")
                .compile("<span\\sclass=\"a-color-price\\sa-text-bold\">EUR\\s([0-9]+(?:\\,[0-9]{0,2}))?<\\/span>")
                .compile("<input\\stype=\"hidden\"\\sid=\"attach-base-product-price\"\\svalue=\"([0-9]+(?:\\.[0-9]{0,2}))?\"\\s\\/>")
                .matcher(content);*/



        for (Pattern pattern : patterns) {
            final Matcher matcher = pattern.matcher(content);
            if (matcher.find() || matcher.matches()) {
                return matcher.group(1);
            }
        }

        return null;
    }

}
