package three.project.domain.product;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Product {
    private String category_code;
    private String product_code;
    private String product_name;
    private String company;
    private String image;
    private Integer in_price;
    private Integer out_price;
    private Integer sell_count;
    private Integer quantity;
    private Integer max_quantity;
    private Integer min_quantity;
    private Integer visit_count;
    private Boolean seal_service;

    public Product() {

    }

    public Product(
            String category_code,
            String product_code,
            String product_name,
            String company,
            String image,
            Integer in_price,
            Integer out_price,
            Integer sell_count,
            Integer max_quantity,
            Integer min_quantity,
            Integer visit_count,
            Boolean seal_service
    ) {
        this.category_code = category_code;
        this.product_code = product_code;
        this.product_name = product_name;
        this.company = company;
        this.image = image;
        this.in_price = in_price;
        this.out_price = out_price;
        this.sell_count = sell_count;
        this.quantity = max_quantity;
        this.min_quantity = min_quantity;
        this.visit_count = visit_count;
        this.seal_service = seal_service;
    }

}
