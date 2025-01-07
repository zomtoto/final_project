package three.finalproject.domain.category;

import lombok.Data;

@Data
public class Category {
    private Long category_no; //카테고리번호
    private String name; //이름
    private String delete; //삭제여부

    public Category() {

    }

    public Category(Long category_no, String name, String delete) {
        this.category_no = category_no;
        this.name = name;
        this.delete = delete;
    }
}
