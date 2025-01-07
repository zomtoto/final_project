package three.finalproject.domain.image;

import lombok.Data;

@Data
public class Image {
    private Long image_no; //이미지번소
    private Long product_no; //상품번호(FK)
    private String origin_path; //원본 이미지 경로
    private String save_path; //저장 이미지 경로
    private String save_date; //저장날짜(YYYY-MM-DD)
    private String update_date; //수정날짜(YYYY-MM-DD)
    private String delete; //삭제여부

    public Image() {
    }

    public Image(Long image_no, Long product_no, String origin_path, String save_path,
                 String save_date, String update_date, String delete) {
        this.image_no = image_no;
        this.product_no = product_no;
        this.origin_path = origin_path;
        this.save_path = save_path;
        this.save_date = save_date;
        this.update_date = update_date;
        this.delete = delete;
    }
}
