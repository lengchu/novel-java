package lenchu.novel.test;

import java.io.IOException;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.junit.Test;

/**
 * @author lenchu
 *
 */
public class HttpAsyncClientTest {

	@Test
	public void test() {
		try (CloseableHttpAsyncClient client = HttpAsyncClients.createDefault()) {
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
