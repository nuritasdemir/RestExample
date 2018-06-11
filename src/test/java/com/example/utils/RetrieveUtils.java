package com.example.utils;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

public class RetrieveUtils {

    public static String retrieveResourceFromResponse(HttpResponse response)
            throws IOException {

        String jsonFromResponse = EntityUtils.toString(response.getEntity());

        return jsonFromResponse;
    }
}
