// /CRMSystem/js/carList.js
document.addEventListener("DOMContentLoaded", function () {

    // 1. 비활성화된 화살표 (처음, 이전, 다음, 마지막)
    document.querySelectorAll('.pagination li.disabled .page-btn').forEach(function (el) {
        el.style.color = '#aaa';           
        el.style.opacity = '0.85';
        el.style.pointerEvents = 'none';      // 클릭 완전 차단 (보험)
    });

    // 2. 현재 페이지 숫자 (active)
    document.querySelectorAll('.pagination li.active .page-btn').forEach(function (el) {
        el.style.color = 'ff5500';             // 흰색 글자
        el.style.fontWeight = 'bold';
        /*el.style.boxShadow = '0 2px 8px rgba(255,85,0,0.4)';*/
    });

    // 3. 일반 숫자 페이지 (클릭 가능한 상태)
    document.querySelectorAll('.pagination li:not(.active):not(.disabled) .page-btn').forEach(function (el) {
        el.style.color = '#333';              // 기본 검정
        el.style.backgroundColor = 'white';
        el.style.transition = 'all 0.3s';

    });
	
	
	
	document.querySelectorAll('.menu-header').forEach(header => {
	  header.addEventListener('click', () => {
	    const menuItem = header.parentElement;
	    menuItem.classList.toggle('active');
	  });
	});

});