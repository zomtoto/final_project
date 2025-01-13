document.addEventListener('DOMContentLoaded', () => {
    // 스피너 초기화: 페이지 로드 시 스피너 숨김
    const loadingOverlay = document.getElementById('loadingOverlay');
    loadingOverlay.classList.remove('active');

    // 버튼 클릭 시 간단한 효과 추가
    const buttons = document.querySelectorAll('button');
    buttons.forEach(button => {
        button.addEventListener('click', () => {
            button.classList.add('active');
            setTimeout(() => button.classList.remove('active'), 300);
        });
    });

    // 폼 제출 시 로딩 스피너 표시
    const form = document.getElementById('loginForm');

    form.addEventListener('submit', (e) => {
        // 필요 시 클라이언트 측 유효성 검증 추가
        // if (!form.checkValidity()) {
        //     e.preventDefault();
        //     form.classList.add('was-validated');
        //     return;
        // }

        // 로딩 스피너 표시
        loadingOverlay.classList.add('active');
    });
});