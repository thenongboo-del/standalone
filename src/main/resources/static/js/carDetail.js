// carDetail.js (간결, detail toggle 전용)
document.addEventListener("DOMContentLoaded", () => {
  const btn = document.querySelector(".detailBtnArea .detailBtn");
  const detail = document.querySelector(".detailInfo");

  if (!btn || !detail) return;

  // 초기 상태: 닫힘(안전용)
  detail.style.maxHeight = "0px";

  btn.addEventListener("click", () => {
    const isOpen = detail.classList.toggle("active");

    if (isOpen) {
      // 열기: 실제 내용 높이에 맞춰서 부드럽게 확장
      detail.style.maxHeight = detail.scrollHeight + "px";
      btn.setAttribute("aria-expanded", "true");
    } else {
      // 닫기
      detail.style.maxHeight = "0px";
      btn.setAttribute("aria-expanded", "false");
    }
  });
});
