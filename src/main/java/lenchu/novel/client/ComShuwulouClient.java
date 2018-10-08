package lenchu.novel.client;

import java.util.stream.Stream;

import lenchu.novel.core.AbstractNovelClient;
import lenchu.novel.dto.Chapter;

public class ComShuwulouClient extends AbstractNovelClient {

	private static final String host = "www.shuwulou.com";

	public ComShuwulouClient() {
		super(host);
	}

	@Override
	public Stream<Chapter> getChapters(String novelUrl) {
		return getChapters(novelUrl, "GBK", "dl.zjlist dd a");
	}

	@Override
	public String getChapterContent(String url) {
		return super.getChapterContent(url, "div#content");
	}

}
