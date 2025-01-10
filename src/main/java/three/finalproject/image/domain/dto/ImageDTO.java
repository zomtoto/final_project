package three.finalproject.image.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import three.finalproject.product.domain.dto.ProductDTO;

@Getter
@Setter
@ToString
public class ImageDTO {
    private Long image_no; //이미지번소호
    private String origin_path; //원본 이미지 경로
    private String save_path; //저장 이미지 경로
    private String image_name; //이미지 이름
    private String image_description; //이미지 설명
    private String save_date; //저장날짜(YYYY-MM-DD)
    private String update_date; //수정날짜(YYYY-MM-DD)
    private String delete; //삭제여부


    private ProductDTO product; //member_no




}
