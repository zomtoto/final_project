document.addEventListener('DOMContentLoaded', () => {
    const body = document.querySelector('body');

    // 페이지 로드 시 페이드인 효과
    body.classList.add('fade-in');

    // 리다이렉트 시 페이드아웃 효과
    document.querySelectorAll('a, form').forEach(element => {
        element.addEventListener('click', (e) => {
            if (element.tagName === 'FORM' || (element.tagName === 'A' && element.href)) {
                e.preventDefault();
                const href = element.tagName === 'FORM' ? element.action : element.href;

                body.classList.add('fade-out');

                setTimeout(() => {
                    window.location.href = href; // 0.5초 후 리다이렉트
                }, 500);
            }
        });
    });
});
