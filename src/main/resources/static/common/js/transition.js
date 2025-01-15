document.addEventListener('DOMContentLoaded', () => {
    const body = document.querySelector('body');

    // 페이지 로드 시 페이드인 효과
    body.classList.add('fade-in');
});

// 뒤로가기 및 페이지 캐시 복원 시 페이드아웃 상태 초기화
window.addEventListener('pageshow', (event) => {
    const body = document.querySelector('body');

    // 페이지가 캐시로부터 복원된 경우에도 fade-out 클래스 제거
    if (body.classList.contains('fade-out')) {
        body.classList.remove('fade-out');
    }
});

document.addEventListener('DOMContentLoaded', () => {
    const body = document.querySelector('body');

    // 리다이렉트 시 페이드아웃 효과
    document.querySelectorAll('a[href], form').forEach(element => {
        element.addEventListener('click', (e) => {
            const isForm = element.tagName === 'FORM';
            const isAnchor = element.tagName === 'A' && element.href;

            if (isForm || isAnchor) {
                e.preventDefault();
                const href = isForm ? element.action : element.href;

                body.classList.add('fade-out');

                // 0.5초 후 리다이렉트
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
});
