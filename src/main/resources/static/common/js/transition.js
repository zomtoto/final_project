// DOMContentLoaded 이벤트 리스너 등록
// fade-in 효과 추가 및 페이지 전환 로직 처리

document.addEventListener('DOMContentLoaded', () => {
    const body = document.querySelector('body');

    // 페이지 로드 시 페이드인 효과 적용
    body.classList.add('fade-in');

    // 일반 폼과 링크에 대한 페이지 전환 처리 (data-add-form 제외)
    document.querySelectorAll('a[href], form:not([data-add-form])').forEach(element => {
        element.addEventListener('click', (e) => {
            const isForm = element.tagName === 'FORM';
            const isAnchor = element.tagName === 'A' && element.href;

            if (isForm || isAnchor) {
                e.preventDefault(); // 기본 동작 방지

                // 폼 또는 앵커의 href/action 가져오기
                const href = isForm ? element.action : element.href;

                // fade-out 효과 추가
                body.classList.add('fade-out');

                // 0.5초 후 리다이렉트 또는 폼 제출
                setTimeout(() => {
                    if (isForm) {
                        element.submit(); // 폼 제출
                    } else {
                        window.location.href = href; // 링크 이동
                    }
                }, 500);
            }
        });
    });

    // data-add-form을 가진 폼에 대한 별도 처리
    document.querySelectorAll('form[data-add-form]').forEach(form => {
        form.addEventListener('submit', (e) => {
            // 제출 시 fade-out 효과 적용
            body.classList.add('fade-out');
        });
    });
});

// 뒤로가기 및 페이지 캐시 복원 처리
window.addEventListener('pageshow', (event) => {
    const body = document.querySelector('body');

    // 캐시 복원 시 fade-out 초기화
    if (body.classList.contains('fade-out')) {
        body.classList.remove('fade-out');
    }
});
