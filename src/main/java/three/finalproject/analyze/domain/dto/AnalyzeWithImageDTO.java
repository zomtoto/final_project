package three.finalproject.analyze.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AnalyzeWithImageDTO {
    private Long image_no;
    private Long member_no;
    private String image_name;
    private Integer analyze_year;
    private String graph_type;
    private String image_description;

    // 이미지 관련 정보
    private String origin_path;
    private String save_path;
}
