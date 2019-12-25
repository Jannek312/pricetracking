package de.jannek.price.tracking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 21/12/2019 21:29
 */
public class WebRequest {

    private static final Random RANDOM = new Random();
    private static final String[] USER_AGENTS = new String[]{
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.89 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.103 Safari/537.36 Edge/18.18362",
            "Mozilla/5.0 (Linux; Android 10; Pixel 3 XL) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.93 Mobile Safari/537.36"
    };

    public String getContent(final String inputUrl) throws MalformedURLException { //TODO
        final URL url = new URL(inputUrl);

        try {
            final URLConnection connection = url.openConnection();
            int index = RANDOM.nextInt(USER_AGENTS.length);
            System.out.println(String.format("USER_AGENTS[%d]", index));
            connection.setRequestProperty("User-Agent", USER_AGENTS[index]);

            final BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8"));

            final String content = reader.lines().collect(Collectors.joining());
            reader.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
