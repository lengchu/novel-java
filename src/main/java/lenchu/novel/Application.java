package lenchu.novel;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lenchu.novel.client.ComSbkk88Client;
import lenchu.novel.core.Novel;
import lenchu.novel.core.NovelClient;
import lenchu.novel.dto.Chapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lenchu
 *
 */
@Slf4j
public class Application {

	public static void main(String[] args) throws Exception {
		log.info("start...");
//		String novelUrl = "https://www.x23us.com/html/68/68045/";
//		String novelUrl = "http://www.readers365.com/luxun/luxun01/";//http://www.readers365.com/jinyong/11/
//		String novelUrl = "http://www.shuwulou.com/shu/3875.html";
		String novelUrl = "https://www.sbkk88.com/mingzhu/gudaicn/zhouyi/";
		NovelClient client = new ComSbkk88Client();
		Novel novel = client.getNovel(novelUrl);
		FileUtils.writeStringToFile(new File("novels/novel.txt"), novel.getAsString(), "UTF-8");
		FileUtils.writeStringToFile(new File("novels/novel.json"), novel.getAsJsonString(), "UTF-8");
		log.info("finish...");
	}

	
	
	
	
	

	public static void testFluent() throws ClientProtocolException, IOException {
		String s = Request.Get("https://www.x23us.com/html/68/68045/").execute()
			   .returnContent().asString(Charset.forName("GBK"));
		System.out.println(s);
		
		Document $ = Jsoup.parse(s);
		Elements as = $.select("dl dd table tbody tr td a");
		
		as.stream().map((ele) -> {
			return Chapter.builder().title(ele.text()).url(ele.attr("href")).index(as.indexOf(ele)).build();
		})
		.forEach(novel -> System.out.println(novel));
	}

	public static void testHttpClient() throws IOException, ClientProtocolException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://www.baidu.com");
		
		CloseableHttpResponse resp = client.execute(httpGet);

		System.out.println(resp.getStatusLine());
		Stream.of(resp.getAllHeaders()).forEach(header -> System.out.println(header));
		System.out.println();
		Optional<String> s = IOUtils.readLines(resp.getEntity().getContent(), "UTF-8").
				stream().reduce((line, next) -> line + next);
		System.out.println(s.get());
		
		resp.close();
		client.close();
	}
}
