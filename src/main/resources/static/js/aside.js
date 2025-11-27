document.addEventListener("DOMContentLoaded", () => {
  const headers = document.querySelectorAll('.menu-header');

  headers.forEach(header => {
    header.addEventListener('click', () => {
      const currentMenuItem = header.parentElement;  // 내가 클릭한 li
      const isAlreadyActive = currentMenuItem.classList.contains('active');

      // 1. 먼저 모든 메뉴 닫기 (화살표도 원래대로)
      document.querySelectorAll('.menu-item').forEach(item => {
        item.classList.remove('active');
      });

      // 2. 내가 클릭한 게 이미 열려 있던 거면 그냥 닫기만 하고 끝
      //    아니면 다시 열기
      if (!isAlreadyActive) {
        currentMenuItem.classList.add('active');
      }
      // → 같은 메뉴를 다시 누르면 닫히고, 다른 메뉴 누르면 이전 건 닫히고 새로 열림!
    });
  });
});