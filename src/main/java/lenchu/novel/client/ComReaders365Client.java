package lenchu.novel.client;

import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import lenchu.novel.core.AbstractNovelClient;
import lenchu.novel.core.NovelClient;
import lenchu.novel.dto.Chapter;

public class ComReaders365Client extends AbstractNovelClient {

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
		return super.getChapters(novelUrl, "GBK", "td div.content a");
//		try {
//			String data = Request.Get(novelUrl).execute().returnContent().asString(Charset.forName("GBK"));
//			
//			Document $ = Jsoup.parse(data);
//			Elements as = $.select("td div.content a");
//			
//			return as.stream().map(ele -> {
//				int index = as.indexOf(ele);
//				return Chapter.builder()
//						.title("第" + index + "章: " + ele.text())
//						.url(ele.attr("href").startsWith("/") ? 
//								StringUtils.substringBefore(novelUrl, "//") + "//" + host + ele.attr("href") :
//								novelUrl + ele.attr("href"))
//						.index(index)
//						.build();
//			});
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
	}

	@Override
	public String getChapterContent(String url) {
		return getChapterContent(url, "GBK", "td[width=100%] p");
	}

	public ComReaders365Client() {
		super(host);
	}

	public static final String host = "www.readers365.com";
}
