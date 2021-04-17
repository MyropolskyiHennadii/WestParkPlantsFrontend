package plantsAPI.integrationTests;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.junit.MatcherAssert.assertThat;

//check connection to real working app
public class HttpConnectionTest {

    private static final Logger logger = LoggerFactory.getLogger(HttpConnectionTest.class);

    @Test
    @Order(0)
    void testHttpRequestToWebApplication() throws IOException {
        String pathToApp = "http://94.130.181.51:8095/WestPark";
        logger.info("Testing HTTP-connection to: "+pathToApp);
        // Given
        HttpUriRequest request = new HttpGet( pathToApp);

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

        int codeResponse = httpResponse.getStatusLine().getStatusCode();
        logger.info("Code response: "+codeResponse);
        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(200));
    }


}
