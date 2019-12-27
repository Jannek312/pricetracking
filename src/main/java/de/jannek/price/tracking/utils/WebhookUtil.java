package de.jannek.price.tracking.utils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 27/12/2019 17:56
 */
public class WebhookUtil {

    private static final WebhookUtil instance;
    static {
        instance = new WebhookUtil();
    }

    public static WebhookUtil getInstance() {
        return instance;
    }

    public void call(final String url, final String content){
        try {
            final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11");
            connection.connect();

            final OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(content);
            writer.close();

            connection.getInputStream();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
