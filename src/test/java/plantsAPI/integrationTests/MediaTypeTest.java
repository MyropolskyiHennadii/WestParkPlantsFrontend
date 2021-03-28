package plantsAPI.integrationTests;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.junit.MatcherAssert.assertThat;

//testing media type
public class MediaTypeTest {

    private static final Logger logger = LoggerFactory.getLogger(MediaTypeTest.class);

    @Test
    @Order(1)
    //geoposition controller
    void testMediaTypeApi_GeopositionController() throws IOException {
        String pathToApp = "http://94.130.181.51:8095/" + "apiWestpark/";
        logger.info("Testing media type: "+pathToApp);
        // Given
        String jsonMimeType = "application/json";
        HttpUriRequest request = new HttpGet( pathToApp );

        // When
        HttpResponse response = HttpClientBuilder.create().build().execute( request );

        // Then
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        logger.info("Media type: "+mimeType);
        assertThat(jsonMimeType, equalTo(mimeType));
    }

    @Test
    @Order(1)
    //exchange controller
    void testMediaTypeApi_ExchangeController() throws IOException {
        String pathToApp = "http://94.130.181.51:8095/" + "exchangeWestpark/";
        logger.info("Testing media type: "+pathToApp);
        // Given
        String jsonMimeType = "application/json";
        HttpUriRequest request = new HttpGet( pathToApp );

        // When
        HttpResponse response = HttpClientBuilder.create().build().execute( request );

        // Then
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        logger.info("Media type: "+mimeType);
        assertThat(jsonMimeType, equalTo(mimeType));
    }

}
