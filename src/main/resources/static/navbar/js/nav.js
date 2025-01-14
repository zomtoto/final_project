document.addEventListener('DOMContentLoaded', () => {
    const toggleButton = document.querySelector('.navbar-toggle');
    const navbarMenu = document.querySelector('.navbar-menu');
    const dropdowns = document.querySelectorAll('.dropdown');

    // 모바일 메뉴 토글
    toggleButton.addEventListener('click', () => {
        navbarMenu.classList.toggle('active');
        const isExpanded = navbarMenu.classList.contains('active');
        toggleButton.setAttribute('aria-expanded', isExpanded);
    });

    // 드롭다운 메뉴 클릭 시 토글
    dropdowns.forEach(dropdown => {
        const link = dropdown.querySelector('.menu-link');
        link.addEventListener('click', (e) => {
            e.preventDefault();
            dropdown.querySelector('.dropdown-menu').classList.toggle('active');
        });
    });
});
