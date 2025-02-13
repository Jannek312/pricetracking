package de.jannek.price.tracking.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 21/12/2019 21:29
 */
public class WebRequest {


    public String getContent(final String inputUrl) throws MalformedURLException {
        final URL url = new URL(inputUrl);

        try {
            final URLConnection connection = url.openConnection();
            connection.setReadTimeout(1000 * 6);
            connection.setRequestProperty("User-Agent", UserAgentUtil.getInstance().getUserAgent());

            final BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

            final String content = reader.lines().collect(Collectors.joining());
            reader.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
