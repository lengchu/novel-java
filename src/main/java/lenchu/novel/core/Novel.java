/**
 * @author lenchu
 */
package lenchu.novel.core;

import java.util.List;
import java.util.stream.Stream;

import com.google.gson.Gson;

import lenchu.novel.dto.Chapter;

/**
 * @author lenchu
 *
 */
public class Novel {
	
	private final List<Chapter> chapters;
	private final Gson gson = new Gson();
	
	public Novel(List<Chapter> chapters) {
		this.chapters = chapters;
	}

	public List<Chapter> getAsList() {
		return chapters;
	}

	public String getAsString() {
		StringBuilder builder = new StringBuilder();
		chapters.stream().forEach(ele -> {
			builder.append(ele.getTitle()).append("\n\n").append(ele.getContent()).append("\n\n\n\n\n");
		});
		return builder.toString();
	}
	
	public String getAsJsonString() {
		return gson.toJson(chapters);
	}
	
	public Stream<Chapter> getAsStream() {
		return chapters.stream();
	}

	@Override
	public String toString() {
		return getAsString();
	}
}
