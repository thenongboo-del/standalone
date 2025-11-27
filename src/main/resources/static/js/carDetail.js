// carDetail.js (간결, detail toggle 전용)
document.addEventListener("DOMContentLoaded", () => {
	
	
	document.addEventListener('click', function (e) {
	const t = e.target;
	if (t && t.matches('.thumbs img')) {
	const src = t.getAttribute('data-src') || t.src;
	const main = document.getElementById('mainImage');
	if (main) main.src = src;
	}
	});


	// basic order/cart click handlers (progressive enhancement)
	document.getElementById('btnOrderAdd')?.addEventListener('click', function (e) {
	// If server handles a link, prefer href. For client-only demo we open a dialog.
	if (this.getAttribute('href') === null) {
	const carId = document.getElementById('carId')?.value || '[[${car.carId}]]';
	// example behavior: redirect to order page
	location.href = '/order/' + encodeURIComponent(carId);
	}
	});


	document.getElementById('btnCartAdd')?.addEventListener('click', function (e) {
	const carId = document.getElementById('carId')?.value || '[[${car.carId}]]';
	// minimal form submission example: use fetch to call put endpoint
	fetch('/cars/cart', {
	method: 'PUT',
	headers: {'Content-Type':'application/json'},
	body: JSON.stringify({carId: carId})
	}).then(r => {
	if (r.ok) alert('장바구니에 추가되었습니다.');
	else alert('요청 실패: ' + r.status);
	}).catch(()=> alert('네트워크 오류'));
	});


	// 썸네일 클릭 -> 반영
	document.querySelectorAll('.thumbs img').forEach(img => img.setAttribute('tabindex', '0'));
	
	
	
	
	
	
	
	
	
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
