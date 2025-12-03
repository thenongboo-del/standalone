function openConsult(type) {
    const titles = { buy: '구매상담', testdrive: '시승신청' };
    if (confirm(`[${titles[type]}]\n\n신청하시겠습니까?`)) {
        location.href = type === 'buy' ? '/CRMSystem/consultation/buy/form' : '/CRMSystem/consultation/drive/form';
    }
}

// 호버 시 더 강한 선택 효과 (선택사항)
document.querySelectorAll('.consultation-box').forEach(box => {
    box.addEventListener('mouseenter', () => box.style.border = '2px solid #FF5500');
    box.addEventListener('mouseleave', () => box.style.border = '');
});