document.addEventListener("DOMContentLoaded", function () {

	window.completeConsultation = function (button, resultType) {
		
	    const card = button.closest('.consult-card');
	    if (!card || !card.dataset.id) {
	        alert("상담 정보를 찾을 수 없습니다.");
	        return;
	    }

	    const consultId = card.dataset.id;

	    if (!confirm("이 상담을 정말 완료 처리하시겠습니까?")) {
	        return;
	    }

	    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
	    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

	    const headers = {
	        'Content-Type': 'application/x-www-form-urlencoded',
	        'X-Requested-With': 'XMLHttpRequest'
	    };
	    if (csrfToken && csrfHeader) {
	        headers[csrfHeader] = csrfToken;
	    }

	    // resultType 포함
	    fetch('/CRMSystem/consultation/dealer/complete', {
	        method: 'POST',
	        headers: headers,
	        body: new URLSearchParams({ 
	            'id': consultId,
	            'resultType': resultType  // 여기에 BUY/NORMAL 전달
	        })
	    })
	    .then(response => {
	        if (!response.ok) throw new Error('서버 응답 오류');
	        return response.text();
	    })
	    .then(() => {
	        card.style.transition = 'all 0.6s ease-out';
	        card.style.opacity = '0';
	        card.style.transform = 'translateX(-100px)';
	        setTimeout(() => {
	            card.remove();
	            const container = document.querySelector('#tab-inprogress .consult-list') ||
	                              document.querySelector('#tab-scheduling .consult-list');
	            if (container && container.children.length === 0) {
	                container.innerHTML = '<div class="empty-state"><p>해당 상태의 상담이 없습니다.</p></div>';
	            }
	        }, 600);

	        alert("상담이 완료 처리되었습니다.");
	    })
	    .catch(err => {
	        console.error("완료 처리 실패:", err);
	        alert("완료 처리 중 오류가 발생했습니다.");
	    });
	};

    // ===================================================================
    // 사이드바 탭 전환
    // ===================================================================
    const tabs = document.querySelectorAll(".sidebar ul a");
    const contents = document.querySelectorAll(".tab-content");

    tabs.forEach(tab => {
        tab.addEventListener("click", function (e) {
            e.preventDefault();
            tabs.forEach(t => t.classList.remove("active"));
            this.classList.add("active");

            const target = this.dataset.tab;
            contents.forEach(c => c.classList.remove("active"));
            document.getElementById(target)?.classList.add("active");
			
			
			// 탭 클릭 처리 후(이미 당신 코드에서 탭 활성화하는 부분 끝에) 다음을 호출:
			function renderPendingCanvases() {
			    document.querySelectorAll('canvas#stockChart').forEach(canvas => {
			        if (canvas._needsRender) {
			            // 보일 때만 렌더
			            if (canvas.offsetParent) {
			                const { labels, values } = canvas._needsRender;
			                if (canvas.chartInstance) canvas.chartInstance.destroy();
			                canvas.chartInstance = new Chart(canvas, {
			                    type: 'bar',
			                    data: { labels, datasets: [{ label: '재고수', data: values, backgroundColor: '#FF5500', borderRadius: 6, barThickness: 24 }] },
			                    options: { responsive: true, plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true } } }
			                });
			                delete canvas._needsRender;
			            }
			        }
			    });
			}

			// 기존 탭 클릭 이벤트 안 (contents.forEach... 에서 active 추가한 직후) 호출
			// 예시: document.getElementById(target)?.classList.add("active"); 바로 다음 줄에:
			renderPendingCanvases();

        });
    });

    // ===================================================================
    // 상담 서브 탭 (대기중 / 일정조정중 / 진행중 / 종료)
    // ===================================================================
    const chatTabs = document.querySelectorAll('.chat-tab');
    const chatPanels = document.querySelectorAll('.chat-panel');

    chatTabs.forEach(tab => {
        tab.addEventListener('click', function () {
            const targetId = this.getAttribute('data-target');

            chatTabs.forEach(t => t.classList.remove('active'));
            chatPanels.forEach(p => {
                p.classList.remove('active');
                p.style.display = 'none';
                p.setAttribute('aria-hidden', 'true');
            });

            this.classList.add('active');
            const targetPanel = document.getElementById(targetId);
            if (targetPanel) {
                targetPanel.classList.add('active');
                targetPanel.style.display = 'block';
                targetPanel.setAttribute('aria-hidden', 'false');
                targetPanel.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
            }
        });
    });

    // ===================================================================
    // 다음상담 일정입력 모달 (모든 .btn-start 버튼에 적용 - 위치 상관없이!)
    // ===================================================================
    document.addEventListener('click', function (e) {
        if (e.target.matches('.btn-start') || e.target.closest('.btn-start')) {
            e.preventDefault();
            const btn = e.target.closest('.btn-start');
            const card = btn.closest('.consult-card');
            if (!card) return;

            const customerId = card.querySelector('.customer-id')?.textContent.trim() || '';
            const message = card.querySelector('.consult-message p')?.textContent.trim() || '';
            const consultId = card.dataset.id;

            document.getElementById('modal-customerId').textContent = '고객ID: ' + customerId;
            document.getElementById('modal-message').textContent = '상담 내용: ' + message;
            document.getElementById('form-consultId').value = consultId;

            document.getElementById('consultModal').style.display = 'block';
        }
    });

    // 모달 닫기
    document.querySelector('#consultModal .close')?.addEventListener('click', () => {
        document.getElementById('consultModal').style.display = 'none';
    });

    window.addEventListener('click', e => {
        const modal = document.getElementById('consultModal');
        if (e.target === modal) modal.style.display = 'none';
    });

    // ===================================================================
    // 재고 검색 + 차트 (기존 코드 그대로 유지)
    // ===================================================================
    const stockSearch = document.getElementById('stockSearch');
    const clearBtn = document.getElementById('clearStockSearch');
    const cards = document.querySelectorAll('.carList .car-item, .car-item');

    function filterCars(q) {
        const term = (q || '').trim().toLowerCase();
        cards.forEach(card => {
            const name = (card.dataset.name || card.querySelector('.car-name')?.textContent || '').toLowerCase();
            const company = (card.dataset.company || card.querySelector('.car-company')?.textContent || '').toLowerCase();
            const category = (card.dataset.category || card.querySelector('.car-category')?.textContent || '').toLowerCase();
            const matched = term === '' || name.includes(term) || company.includes(term) || category.includes(term);
            card.style.display = matched ? '' : 'none';
        });
    }

    stockSearch?.addEventListener('input', e => filterCars(e.target.value));
    clearBtn?.addEventListener('click', () => {
        stockSearch.value = '';
        filterCars('');
        stockSearch.focus();
    });

    function collectStockData() {
        const dataMap = new Map();
        document.querySelectorAll('.carList .car-item, .car-item').forEach(it => {
            const nameEl = it.querySelector('.car-name');
            const stockEl = it.querySelector('.stock-number');
            const name = (nameEl ? nameEl.textContent.trim() : (it.dataset.name || 'Unknown')).trim();
            const stock = parseInt((stockEl ? stockEl.textContent : it.dataset.stock) || 0, 10) || 0;
            dataMap.set(name, (dataMap.get(name) || 0) + stock);
        });
        return dataMap;
    }

    function renderChart() {
        const dataMap = collectStockData();
        const labels = Array.from(dataMap.keys()).slice(0, 10);
        const values = labels.map(l => dataMap.get(l));

        /*document.getElementById('totalModels') && (document.getElementById('totalModels').textContent = dataMap.size);
        document.getElementById('totalStock') && (document.getElementById('totalStock').textContent = Array.from(dataMap.values()).reduce((a, b) => a + b, 0));
        document.getElementById('outOfStockCount') && (document.getElementById('outOfStockCount').textContent = Array.from(dataMap.values()).filter(v => v <= 0).length);*/
		
		document.querySelectorAll('#totalModels').forEach(el => el.textContent = dataMap.size);
		document.querySelectorAll('#totalStock').forEach(el => el.textContent = Array.from(dataMap.values()).reduce((a,b)=>a+b,0));
		document.querySelectorAll('#outOfStockCount').forEach(el => el.textContent = Array.from(dataMap.values()).filter(v => v <= 0).length);

        /*const ctx = document.getElementById('stockChart');
        if (!ctx) return;
        if (ctx.chartInstance) ctx.chartInstance.destroy();

        ctx.chartInstance = new Chart(ctx, {
            type: 'bar',
            data: { labels, datasets: [{ label: '재고수', data: values, backgroundColor: '#FF5500', borderRadius: 6, barThickness: 24 }] },
            options: { responsive: true, plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true } } }
        });*/
		
		// --- 모든 캔버스(#stockChart)에 대해 각각 차트 생성/갱신 ---
		const canvases = document.querySelectorAll('canvas#stockChart');
		canvases.forEach((canvas) => {
		    // 화면에 숨겨져 있거나 display:none이면 차트 렌더 시 크기 문제가 생길 수 있음.
		    // visible check: offsetParent 존재하면 렌더 가능
		    if (!canvas.offsetParent) {
		        // 숨김 상태인 캔버스는 나중에 탭이 활성화될 때 렌더하도록 처리
		        // 마커를 붙여두고 리턴
		        canvas._needsRender = { labels, values };
		        return;
		    }

		    if (canvas.chartInstance) canvas.chartInstance.destroy();

		    canvas.chartInstance = new Chart(canvas, {
		        type: 'bar',
		        data: { labels, datasets: [{ label: '재고수', data: values, backgroundColor: '#FF5500', borderRadius: 6, barThickness: 24 }] },
		        options: { responsive: true, plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true } } }
		    });
		});
		
    }

    renderChart();

    // 고객 리스트 검색 (기존 코드)
    const searchInput = document.getElementById('customerSearch');
    const clearBTN = document.getElementById('clearSearch');
    const list = document.querySelectorAll('.customer-card');

    function filterList(term) {
        const q = term.trim().toLowerCase();
        list.forEach(card => {
            const name = (card.dataset.name || '').toLowerCase();
            const phone = (card.dataset.phone || '').toLowerCase();
            const email = (card.dataset.email || '').toLowerCase();
            const matched = q === '' || name.includes(q) || phone.includes(q) || email.includes(q);
            card.style.display = matched ? '' : 'none';
        });
    }

    searchInput?.addEventListener('input', e => filterList(e.target.value));
    clearBTN?.addEventListener('click', () => {
        searchInput.value = '';
        filterList('');
        searchInput.focus();
    });

    // 아코디언 토글
    document.querySelectorAll('.customer-summary').forEach(summary => {
        summary.addEventListener('click', function () {
            const card = this.closest('.customer-card');
            const details = card.querySelector('.customer-details');
            const isOpen = card.classList.toggle('open');
            details.style.display = isOpen ? 'block' : 'none';
        });
    });
	
	
	
	
	
	
	// ===================================================================
	// 종료된 상담 아코디언 토글
	// ===================================================================
	document.querySelectorAll('#tab-finished .consult-card').forEach(card => {
	  card.addEventListener('click', function () {
	    const details = this.querySelector('.consultDetailArea');
	    const isOpen = this.classList.toggle('open');
	    details.style.display = isOpen ? 'block' : 'none';
	    details.setAttribute('aria-hidden', !isOpen);
	  });
	});

	// ===================================================================
	
	// 종료된 상담 검색 
	const finishedSearch = document.getElementById('finishedSearch');
	const clearFinishedBtn = document.getElementById('clearFinishedSearch');
	const finishedCards = document.querySelectorAll('#tab-finished .consult-card');

	function filterFinished(term) {
	  const q = term.trim().toLowerCase();
	  finishedCards.forEach(card => {
	    const customerId = (card.dataset.customerId || '').toLowerCase();
	    const requestMessage = (card.dataset.requestMessage || '').toLowerCase();
	    // detail.content들 모두 검색 (여러 detail이 있을 수 있음)
	    let detailsContent = '';
	    card.querySelectorAll('.consult-details').forEach(detail => {
	      detailsContent += (detail.dataset.content || '').toLowerCase() + ' ';
	    });

	    const matched = q === '' || 
	                    customerId.includes(q) || 
	                    requestMessage.includes(q) || 
	                    detailsContent.includes(q);
	    card.style.display = matched ? '' : 'none';
	  });
	}

	finishedSearch?.addEventListener('input', e => filterFinished(e.target.value));
	clearFinishedBtn?.addEventListener('click', () => {
	  finishedSearch.value = '';
	  filterFinished('');
	  finishedSearch.focus();
	});
	
	
	
	
	
	
	

});