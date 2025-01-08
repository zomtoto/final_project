package three.finalproject.category.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CategoryDTO {
    private Long category_no; //카테고리번호
    private String name; //이름
    private String delete; //삭제여부
}
