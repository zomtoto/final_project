package three.finalproject.analyze.domain.dto;

import lombok.*;
import three.finalproject.image.domain.dto.ImageDTO;
import three.finalproject.member.domain.dto.MemberDTO;

@Getter
@Setter
@ToString
public class AnalyzeDTO {

    private Long analyze_no; // 분석 번호
    private Integer analyze_year; //분석 연도
    private String graph_type; //그래프 종류
    private GraphDTO graph;
    private ImageDTO image; //image_no
    private MemberDTO member; //member_no

    public AnalyzeDTO() {
        this.image = new ImageDTO();
        this.member = new MemberDTO();
        this.graph = new GraphDTO();
    }

}
