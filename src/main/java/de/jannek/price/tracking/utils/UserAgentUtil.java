package de.jannek.price.tracking.utils;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 27/12/2019 17:52
 */
public class UserAgentUtil {

    private static final Random RANDOM = new Random();
    private static final String[] USER_AGENTS = new String[]{
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.89 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.103 Safari/537.36 Edge/18.18362",
            "Mozilla/5.0 (Linux; Android 10; Pixel 3 XL) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.93 Mobile Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:71.0) Gecko/20100101 Firefox/71.0",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 Edge/18.18362",
            "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
    };

    private static final UserAgentUtil instance;

    static {
        instance = new UserAgentUtil();
    }

    public static UserAgentUtil getInstance() {
        return instance;
    }

    public String getUserAgent() {
        return RandomUserAgent.getRandomUserAgent();
        //return USER_AGENTS[RANDOM.nextInt(USER_AGENTS.length)];
    }

}
