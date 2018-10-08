package lenchu.novel.client;

import java.util.stream.Stream;

import lenchu.novel.core.AbstractNovelClient;
import lenchu.novel.dto.Chapter;

/**
 * @author lenchu
 *
 */
public class ComX23usClient extends AbstractNovelClient {
	
	public static final String host = "www.x23us.com";

	public ComX23usClient() {
		super(host);
	}

	@Override
	public Stream<Chapter> getChapters(String novelUrl) {
		return getChapters(novelUrl, "GBK", "dl dd table tbody tr td a");
	}
	
	@Override
	public String getChapterContent(String url) {
		return getChapterContent(url, "dd#contents");
	}
}
