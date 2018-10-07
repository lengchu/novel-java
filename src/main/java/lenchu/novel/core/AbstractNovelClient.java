package lenchu.novel.core;

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

import lenchu.novel.dto.Chapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractNovelClient implements NovelClient {
	
	protected String host;

	public AbstractNovelClient(String host) {
		this.host = host;
	}
	
	public Stream<Chapter> getChapters(String novelUrl, String selector) {
		return getChapters(novelUrl, "UTF-8", selector);
	}
	
	public Stream<Chapter> getChapters(String novelUrl, String charSet, String selector) {
		try {
			String data = Request.Get(novelUrl).execute()
					.returnContent().asString(Charset.forName(charSet));
			Document $ = Jsoup.parse(data);
			Elements as = $.select(selector);
			
			return as.stream().map(ele -> {
				return Chapter.builder()
						.title(ele.text())
						.url(ele.attr("href").startsWith("/") ? 
								StringUtils.substringBefore(novelUrl, "//") + "//" + host + ele.attr("href") :
								novelUrl + ele.attr("href"))
						.index(as.indexOf(ele))
						.build();
			});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getChapterContent(String url, String selector) {
		return getChapterContent(url, "UTF-8", selector);
	}

	public String getChapterContent(String url, String charSet, String selector) {
		try {
			String data = Request.Get(url).execute()
					.returnContent().asString(Charset.forName(charSet));
			Element $ = Jsoup.parse(data);
			Optional<String> content = $.select(selector).eachText().stream()
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
			log.info("getting chapters...");
			Stream<Chapter> chapterCollection = getChapters(novelUrl);
			chapterCollection.sorted(Comparator.comparing(Chapter::getIndex))
				.map(ele -> {
					try {
						log.info("getting: " + ele.getTitle());
						String chapterContent = getChapterContent(ele.getUrl());
						ele.setContent(chapterContent);
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

	@Override
	public boolean canGet(String novelUrl) {
		String str2 = StringUtils.substringBetween(novelUrl, "//", "/");
		return StringUtils.equalsIgnoreCase(this.host, str2);
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}
