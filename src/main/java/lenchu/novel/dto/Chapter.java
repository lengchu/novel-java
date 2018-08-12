package lenchu.novel.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author lenchu
 *
 */
@Data
@Builder
public class Chapter {

	private Integer index;
	
	private String title;
	
	private String url;
	
	private String content;
}
