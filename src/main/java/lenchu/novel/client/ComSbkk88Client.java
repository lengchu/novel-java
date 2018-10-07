package lenchu.novel.client;

import java.util.stream.Stream;

import org.junit.Test;

import lenchu.novel.core.AbstractNovelClient;
import lenchu.novel.dto.Chapter;

public class ComSbkk88Client extends AbstractNovelClient {
	
	private static final String host = "www.sbkk88.com";
	
	public ComSbkk88Client() {
		super(host);
	}

	@Override
	public Stream<Chapter> getChapters(String novelUrl) {
		return getChapters(novelUrl, "GBK", "ul.leftList li a");
	}

	@Override
	public String getChapterContent(String url) {
		return getChapterContent(url, "GBK", "div#f_article");
	}

	@Test
	public void test() {
		String content = new ComSbkk88Client().getChapterContent("https://www.sbkk88.com/html/sudongpochuan/71386.html");
		System.out.println(content);
	}

}
