package lenchu.novel.client;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.fluent.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import lenchu.novel.core.Novel;
import lenchu.novel.core.NovelClient;
import lenchu.novel.dto.Chapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ComReaders365Client implements NovelClient {

	NovelClient client;
	
	@Before
	public void before() {
		this.client = new ComReaders365Client();
	}
	
	@Test
	public void testGetChapters() {
		client.getChapters("http://www.readers365.com/luxun/luxun01/").forEach(c -> {
			System.out.println(c.toString());
		});;
	}
	
	@Test
	public void testGetContent() {
		String content = client.getChapterContent("http://www.readers365.com/luxun/luxun01/a000.htm");
		System.out.println(content);
	}
	
	@Override
	public Stream<Chapter> getChapters(String novelUrl) {
		log.info("getting chapters...");
		try {
			String data = Request.Get(novelUrl).execute().returnContent().asString(Charset.forName("GBK"));
			
			Document $ = Jsoup.parse(data);
			Elements as = $.select("td div.content a");
			
			return as.stream().map(ele -> {
				int index = as.indexOf(ele);
				return Chapter.builder()
						.title("第" + index + "章: " + ele.text())
						.url(ele.attr("href").startsWith("/") ? 
								StringUtils.substringBefore(novelUrl, "//") + "//" + host + ele.attr("href") :
								novelUrl + ele.attr("href"))
						.index(index)
						.build();
			});
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public Novel getNovel(String novelUrl) {
		if (this.canGet(novelUrl)) {				
			List<Chapter> list = new ArrayList<>();
			getChapters(novelUrl).map(c -> {
				log.info("getting: " + c.getTitle());
				c.setContent(getChapterContent(c.getUrl()));
				return c;
			})
			.sorted(Comparator.comparing(Chapter::getIndex))
			.forEach(c -> {
				list.add(c);
			});
			return new Novel(list);
		} else 
			throw new RuntimeException("novelUrl does not match this client");
	}

	@Override
	public String getChapterContent(String url) {
		try {
			String data = Request.Get(url).execute().returnContent().asString(Charset.forName("GBK"));
			Document $ = Jsoup.parse(data);
			StringBuilder content = new StringBuilder();
			$.select("td[width=99%]").stream().forEach(t -> {
				content.append(t.ownText());
			});
			return content.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean canGet(String novelUrl) {
		String str2 = StringUtils.substringBetween(novelUrl, "//", "/");
		log.info(str2);
		return StringUtils.equalsIgnoreCase(host, str2);
	}

	public static final String host = "www.readers365.com";
}
