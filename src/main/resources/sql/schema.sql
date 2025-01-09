CREATE TABLE member_table (
                              member_no INTEGER PRIMARY KEY AUTO_INCREMENT, -- 회원번호
                              id VARCHAR(50) NOT NULL UNIQUE,               -- 아이디
                              password VARCHAR(100) NOT NULL,              -- 비밀번호
                              name VARCHAR(100) NOT NULL,                  -- 이름
                              dob VARCHAR(10),                             -- 생년월일 (stored as string in YYYY-MM-DD format)
                              gender VARCHAR(10) CHECK(gender IN ('남', '여')), -- 성별
                              address VARCHAR(255),                        -- 주소
                              email VARCHAR(100),                          -- 이메일
                              phone VARCHAR(20),                           -- 전화번호
                              admin VARCHAR(10) CHECK (admin IN ('Y', 'N')), -- 관리자 여부
                              joinDate VARCHAR(10),                        -- 회원가입일 (stored as string in YYYY-MM-DD format)
                              delete VARCHAR(10) CHECK (delete IN ('True', 'False')) -- 삭제 여부
);

CREATE TABLE category_table (
                                category_no INTEGER PRIMARY KEY AUTO_INCREMENT, -- 카테고리번호
                                name VARCHAR(100) NOT NULL,                     -- 이름
                                delete VARCHAR(10) CHECK (delete IN ('True', 'False'))  -- 삭제 여부
);

CREATE TABLE product_table (
                               product_no INTEGER PRIMARY KEY AUTO_INCREMENT, -- 상품번호
                               category_no INTEGER,                           -- 카테고리번호 (FK)
                               name VARCHAR(100) NOT NULL,                   -- 상품이름
                               company VARCHAR(100),                         -- 회사명
                               in_price INTEGER NOT NULL,                    -- 입고가
                               out_price INTEGER NOT NULL,                   -- 판매가
                               sell_count INTEGER DEFAULT 0,                 -- 판매량
                               quantity INTEGER,                              -- 재고
                               visit INTEGER DEFAULT 0,                       -- 조회수
                               seal_service VARCHAR(10) CHECK (seal_service IN ('True', 'False')), -- 각인서비스
                               delete VARCHAR(10) CHECK (delete IN ('True', 'False')), -- 삭제 여부
                               FOREIGN KEY (category_no) REFERENCES category_table(category_no)
);

CREATE TABLE buy_table (
                           buy_no INTEGER PRIMARY KEY AUTO_INCREMENT,    -- 구매번호
                           member_no INTEGER,                            -- 회원번호 (FK)
                           product_no INTEGER,                           -- 상품번호 (FK)
                           date VARCHAR(10),                              -- 구매날짜 (stored as string in YYYY-MM-DD format)
                           quantity INTEGER,                              -- 구매수량
                           seal_service VARCHAR(10),                       -- 각인여부
                           total_price INTEGER,                           -- 총구매액
                           method VARCHAR(50),                            -- 구매방식
                           FOREIGN KEY (member_no) REFERENCES member_table(member_no),
                           FOREIGN KEY (product_no) REFERENCES product_table(product_no)
);

CREATE TABLE image_table (
                             image_no INTEGER PRIMARY KEY AUTO_INCREMENT,   -- 이미지번호
                             product_no INTEGER NULL,                       -- 상품번호 (FK, nullable)
                             origin_path VARCHAR(255),                      -- 원본이미지 경로
                             save_path VARCHAR(255),                        -- 저장 이미지 경로
                             save_date VARCHAR(10),                         -- 저장날짜 (stored as string in YYYY-MM-DD format)
                             update_date VARCHAR(10),                       -- 수정날짜 (stored as string in YYYY-MM-DD format)
                             delete VARCHAR(10) CHECK (delete IN ('True', 'False')), -- 삭제 여부
                             FOREIGN KEY (product_no) REFERENCES product_table(product_no)
);

CREATE TABLE analyze_table (
                               analyze_no INTEGER PRIMARY KEY AUTO_INCREMENT,       -- 분석번호
                               image_no INTEGER NOT NULL,                           -- 이미지번호 (FK, image_table)
                               member_no INTEGER NOT NULL,                          -- 회원번호 (FK, member_table)
                               image_name VARCHAR(255) NOT NULL,                    -- 이미지 이름
                               image_description TEXT,                              -- 이미지(분석) 설명
                               FOREIGN KEY (image_no) REFERENCES image_table(image_no), -- FK to image_table
                               FOREIGN KEY (member_no) REFERENCES member_table(member_no) -- FK to member_table
);
