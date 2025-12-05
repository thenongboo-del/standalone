




document.addEventListener("DOMContentLoaded", () => {
	
    const modal = document.getElementById("successModal");
    console.log("DEBUG: modal element:", modal);

    if (!modal) return; // 모달 없으면 종료

    const closeBtn = modal.querySelector(".modal-close-btn");
    console.log("DEBUG: closeBtn element:", closeBtn);

    // 닫는 함수 (부드러운 fade + DOM에서 제거)
    const closeModal = () => {
        try {
            modal.style.transition = "opacity 0.3s ease";
            modal.style.opacity = "0";
            // 포인터 차단해서 중복 클릭 방지
            modal.style.pointerEvents = "none";
            setTimeout(() => {
                // DOM에서 제거 (또는 display: none)
                if (modal.parentNode) modal.parentNode.removeChild(modal);
                console.log("DEBUG: modal removed from DOM");
            }, 320);
        } catch (err) {
            console.error("DEBUG: closeModal error:", err);
        }
    };

    // 버튼이 있으면 클릭 연결 (버블링 방지)
    if (closeBtn) {
        closeBtn.addEventListener("click", (e) => {
            e.stopPropagation();
            closeModal();
        });
    } else {
        console.warn("DEBUG: closeBtn not found - check .modal-close-btn selector or rendered HTML");
    }

    // 배경(오버레이) 클릭 시 닫기 — 클릭 타겟이 overlay인 경우만
    modal.addEventListener("click", (e) => {
        // modal 자체에 클릭이 들어왔을 때만 닫기 (자식 요소 클릭은 무시)
        if (e.target === modal) {
            closeModal();
        }
    });

    // ESC 키로도 닫기
    document.addEventListener("keydown", (e) => {
        if (e.key === "Escape") closeModal();
    });
	
	
	
	
	
	
});