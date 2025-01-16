DECLARE
-- 성 리스트
v_hname SYS.ODCIVARCHAR2LIST := SYS.ODCIVARCHAR2LIST(
        '김', '이', '박', '최', '정', '강', '조', '윤', '장', '임',
        '오', '한', '신', '서', '권', '황', '안', '송', '유', '홍'
    );
    -- 이름 리스트
    v_name SYS.ODCIVARCHAR2LIST := SYS.ODCIVARCHAR2LIST(
        '민', '준', '서', '현', '우', '원', '호', '아', '진', '영',
        '경', '연', '슬', '빈', '혁', '찬', '솔', '나', '예', '은'
    );
    -- 주소 리스트
    v_address SYS.ODCIVARCHAR2LIST := SYS.ODCIVARCHAR2LIST(
        '서울특별시', '경기도', '충청남도', '충청북도', '제주도',
        '경상남도', '경상북도', '전라북도', '전라남도'
    );
    -- 성별 리스트
    v_gender SYS.ODCIVARCHAR2LIST := SYS.ODCIVARCHAR2LIST(
        '남', '여'
    );

    v_id VARCHAR2(50);
    v_email VARCHAR2(100);
    v_phone VARCHAR2(20);
    v_dob DATE;
    v_gender_selected VARCHAR2(1);
    v_joinDate DATE;
BEGIN
    -- 관리자 계정 삽입
INSERT INTO member_table (
    id, password, name, address, email, phone, dob, gender, admin, delete, joinDate
)
VALUES (
           'system1', -- id
           '1234', -- password
           v_hname(TRUNC(DBMS_RANDOM.VALUE(1, v_hname.COUNT + 1))) ||
           v_name(TRUNC(DBMS_RANDOM.VALUE(1, v_name.COUNT + 1))) ||
           v_name(TRUNC(DBMS_RANDOM.VALUE(1, v_name.COUNT + 1))), -- 이름
           v_address(TRUNC(DBMS_RANDOM.VALUE(1, v_address.COUNT + 1))) || ' ' ||
           '상세주소 ' || TRUNC(DBMS_RANDOM.VALUE(1, 100)), -- 주소
           'admin1@example.com', -- 이메일 (관리자용)
           '010-' ||
           LPAD(TRUNC(DBMS_RANDOM.VALUE(1000, 9999)), 4, '0') || '-' ||
           LPAD(TRUNC(DBMS_RANDOM.VALUE(1000, 9999)), 4, '0'), -- 전화번호
           TRUNC(DBMS_RANDOM.VALUE(1985, 2010)) || '-' ||
           LPAD(TRUNC(DBMS_RANDOM.VALUE(1, 13)), 2, '0') || '-' ||
           LPAD(TRUNC(DBMS_RANDOM.VALUE(1, 28)), 2, '0'), -- 생년월일 (15~40세)
           v_gender(TRUNC(DBMS_RANDOM.VALUE(1, v_gender.COUNT + 1))), -- 성별
           'Y', -- admin
           'False', -- delete
           TRUNC(SYSDATE) -- 가입일
       );

-- 일반 사용자 계정 999개 삽입
FOR i IN 1..999 LOOP
        -- 랜덤 아이디 생성 (예: user_1001, user_1002, ...)
        v_id := 'user_' || (i + 1000);

        -- 랜덤 이메일 생성
        v_email := 'user' || (i + 1000) || '@example.com';

        -- 랜덤 전화번호 생성
        v_phone := '010-' ||
                   LPAD(TRUNC(DBMS_RANDOM.VALUE(1000, 9999)), 4, '0') || '-' ||
                   LPAD(TRUNC(DBMS_RANDOM.VALUE(1000, 9999)), 4, '0');

        -- 랜덤 생년월일 생성 (15~40세)
        v_dob := TO_DATE(
                    TRUNC(DBMS_RANDOM.VALUE(1985, 2010)) || '-' ||
                    LPAD(TRUNC(DBMS_RANDOM.VALUE(1, 13)), 2, '0') || '-' ||
                    LPAD(TRUNC(DBMS_RANDOM.VALUE(1, 28)), 2, '0'),
                    'YYYY-MM-DD'
                 );

        -- 랜덤 성별 선택
        v_gender_selected := v_gender(TRUNC(DBMS_RANDOM.VALUE(1, v_gender.COUNT + 1)));

        -- 랜덤 가입일 생성 (예: 최근 5년 이내)
        v_joinDate := TRUNC(SYSDATE) - TRUNC(DBMS_RANDOM.VALUE(0, 1825)); -- 약 5년

INSERT INTO member_table (
    id, password, name, address, email, phone, dob, gender, admin, delete, joinDate
)
VALUES (
           v_id, -- id
           '1234', -- password
           v_hname(TRUNC(DBMS_RANDOM.VALUE(1, v_hname.COUNT + 1))) ||
           v_name(TRUNC(DBMS_RANDOM.VALUE(1, v_name.COUNT + 1))) ||
           v_name(TRUNC(DBMS_RANDOM.VALUE(1, v_name.COUNT + 1))), -- 이름
           v_address(TRUNC(DBMS_RANDOM.VALUE(1, v_address.COUNT + 1))) || ' ' ||
           '상세주소 ' || TRUNC(DBMS_RANDOM.VALUE(1, 100)), -- 주소
           v_email, -- 이메일
           v_phone, -- 전화번호
           v_dob, -- 생년월일
           v_gender_selected, -- 성별
           'N', -- admin
           'False', -- delete
           v_joinDate -- 가입일
       );
END LOOP;

COMMIT;
END;
/
