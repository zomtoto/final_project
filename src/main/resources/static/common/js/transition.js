// DOMContentLoaded 이벤트 리스너 등록
// fade-in 효과 추가 및 페이지 전환 로직 처리

document.addEventListener('DOMContentLoaded', () => {
    const body = document.querySelector('body');

    // 페이지 로드 시 페이드인 효과 적용
    body.classList.add('fade-in');

    // 링크에 대한 페이지 전환 처리 (form 태그는 제외)
    document.querySelectorAll('a[href]').forEach(anchor => {
        anchor.addEventListener('click', (e) => {
            e.preventDefault(); // 기본 동작 방지

            // fade-out 효과 추가
            body.classList.add('fade-out');

            // 0.5초 후 링크 이동
            setTimeout(() => {
                window.location.href = anchor.href;
            }, 500);
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
