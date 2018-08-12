/**
 * @author lenchu
 */
package lenchu.novel.test;

import java.io.IOException;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * @author lenchu
 *
 */
public class ComX23usClientTest {

	@Test
	public void testGetContent() throws ClientProtocolException, IOException {
		String data = Request.Get("https://www.x23us.com/html/68/68045/29187918.html").execute()
				.returnContent().asString();
		
		Element $ = Jsoup.parse(data);
		Optional<String> content = $.select("dd#contents").eachText().stream()
				.reduce((line, nextLine) -> line + nextLine );
		System.out.println(content);
	}
	
	@Test
	public void testUrl() {
		String host = "www.x23us.com";
		String url = "/html/68/68045/29187918.html";
		String novelUrl = "https://www.x23us.com/";
		String fullUrl = url.startsWith("/") ? 
				StringUtils.substringBefore(novelUrl, "//") + "//" + host + url : novelUrl + url;
				
		System.out.println(fullUrl);
	}
}
