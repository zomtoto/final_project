package three.finalproject.analyze.domain.dto;

import lombok.*;
import three.finalproject.image.domain.dto.ImageDTO;
import three.finalproject.member.domain.dto.MemberDTO;

@Getter
@Setter
@ToString
public class AnalyzeDTO {

    private Integer analyze_year; //분석 연도
    private String graph_type; //그래프 종류
    private String image_description; //이미지 설명

    //DTO
    private ImageDTO image; //image_no
    private MemberDTO member; //member_no

    // 기본 생성자에서 초기화
    public AnalyzeDTO() {
        this.image = new ImageDTO();   // 미리 생성
        this.member = new MemberDTO(); // 필요하다면 같이 생성
    }

}
