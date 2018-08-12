package lenchu.novel.client;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.fluent.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lenchu.novel.core.Novel;
import lenchu.novel.core.NovelClient;
import lenchu.novel.dto.Chapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lenchu
 *
 */
@Slf4j
public class ComX23usClient implements NovelClient {
	
	public static final String host = "www.x23us.com";

	@Override
	public boolean canGet(String novelUrl) {
		String str2 = StringUtils.substringBetween(novelUrl, "//", "/");
		return StringUtils.equalsIgnoreCase(host, str2);
	}

	@Override
	public Stream<Chapter> getChapters(String novelUrl) {
		log.info("getting chapters...");
		try {
			String data = Request.Get(novelUrl).execute()
					   .returnContent().asString(Charset.forName("GBK"));
			Document $ = Jsoup.parse(data);
			Elements as = $.select("dl dd table tbody tr td a");
			
			return as.stream().map(ele -> {
				return Chapter.builder()
						.title(ele.text())
						.url(ele.attr("href").startsWith("/") ? 
								StringUtils.substringBefore(novelUrl, "//") + "//" + host + ele.attr("href") :
								novelUrl + ele.attr("href"))
						.index(as.indexOf(ele))
//						.content(getChapterContent(ele.attr("href")))
						.build();
			});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String getChapterContent(String url) {
		try {
			String data = Request.Get(url).execute()
					.returnContent().asString();
			Element $ = Jsoup.parse(data);
			Optional<String> content = $.select("dd#contents").eachText().stream()
					.reduce((line, nextLine) -> line + nextLine );
			return content.get();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Novel getNovel(String novelUrl) {
		if (canGet(novelUrl)) {
			List<Chapter> list = new ArrayList<>();
			Stream<Chapter> chapterCollection = getChapters(novelUrl);
			chapterCollection.sorted(Comparator.comparing(Chapter::getIndex))
				.map(ele -> {
					try {
						log.info("getting: " + ele.getTitle());
//						String chapterContent = getChapterContent(ele.getUrl());
//						ele.setContent(chapterContent);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return ele;
				})
				.forEach(ele -> {
					list.add(ele);
				});
			return new Novel(list);
		} else 
			throw new RuntimeException("novelUrl does not match this client");
	}
}
