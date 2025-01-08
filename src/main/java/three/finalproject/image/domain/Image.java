package three.finalproject.image.domain;

public class Image {
    private Long image_no; //이미지번소호
    private Long product_no; //상품번호(FK)
    private String origin_path; //원본 이미지 경로
    private String save_path; //저장 이미지 경로
    private String save_date; //저장날짜(YYYY-MM-DD)
    private String update_date; //수정날짜(YYYY-MM-DD)
    private String delete; //삭제여부
}
