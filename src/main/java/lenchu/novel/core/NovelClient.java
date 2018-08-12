package lenchu.novel.core;

import java.util.stream.Stream;

import lenchu.novel.dto.Chapter;

/**
 * @author lenchu
 *
 */
public interface NovelClient {
	
	/**
	 * 获取章节集合
	 * 包含索引，标题及网址，内容可以单独获取
	 * @param novelUrl 以http开头的完整网址
	 * @return
	 */
	Stream<Chapter> getChapters(String novelUrl);
	
	/**
	 * 获取整个novel的内容，可以直接写入文件
	 * @param novelUrl
	 * @return
	 */
	Novel getNovel(String novelUrl);
	
	/**
	 * 获取章节内容
	 * @param url 章节地址 以http开头的完整网址
	 * @return
	 */
	String getChapterContent(String url);

	/**
	 * 是否能获取此url的novel
	 * @param novelUrl 以http开头的完整网址
	 * @return
	 */
	boolean canGet(String novelUrl);
}
