package three.finalproject.analyze.domain.dto;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AnalyzeDTO {

    private Long image_no; //이미지 번호
    private Long member_no; //멤버 번호
    private Integer analyze_year; //분석 연도
    private String graph_type; //그래프 종류
    private String image_name; //이미지 이름
    private String image_description; //이미지 설명


}
