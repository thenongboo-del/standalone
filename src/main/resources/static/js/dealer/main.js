document.addEventListener("DOMContentLoaded", function() {
	
	
	
	
    const tabs = document.querySelectorAll(".sidebar ul a");
    const contents = document.querySelectorAll(".tab-content");

    tabs.forEach(tab => {
        tab.addEventListener("click", function(e) {
            e.preventDefault();
            
            // 탭 활성화
            tabs.forEach(t => t.classList.remove("active"));
            this.classList.add("active");

            // 컨텐츠 전환
            const target = this.dataset.tab;
            contents.forEach(c => c.classList.remove("active"));
            document.getElementById(target).classList.add("active");
        });
    });
	
	
	
	const searchInput = document.getElementById('customerSearch');
	const clearBTN = document.getElementById('clearSearch');
	const list = document.querySelectorAll('.customer-card');

	// 아코디언 토글
	document.querySelectorAll('.customer-summary').forEach(summary => {
	  summary.addEventListener('click', function () {
	    const card = this.closest('.customer-card');
	    const details = card.querySelector('.customer-details');

	    const isOpen = card.classList.toggle('open');
	    if (isOpen) {
	      details.style.display = 'block';
	      details.setAttribute('aria-hidden', 'false');
	    } else {
	      details.style.display = 'none';
	      details.setAttribute('aria-hidden', 'true');
	    }
	  });

	  // 키보드 접근성: Enter / Space 로 토글
	  summary.addEventListener('keydown', function (e) {
	    if (e.key === 'Enter' || e.key === ' ') {
	      e.preventDefault();
	      this.click();
	    }
	  });
	});

	// 검색 필터 (이름, 전화, 이메일)
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

	searchInput && searchInput.addEventListener('input', function () {
	  filterList(this.value);
	});

	clearBTN && clearBTN.addEventListener('click', function () {
	  if (searchInput) {
	    searchInput.value = '';
	    filterList('');
	    searchInput.focus();
	  }
	});
	
	
	
	
	
	// ================================스톡 재고 ==============================
	
	// 검색 요소
	  const stockSearch = document.getElementById('stockSearch');
	  const clearBtn = document.getElementById('clearStockSearch');
	  const carCards = document.querySelectorAll('.carList .carList > .car-item, .carList .carList .car-item');

	  // NOTE: thymeleaf 구조에 따라 car-item 셀렉터가 달라질 수 있으니 위 두 패턴으로 둘다 캡쳐

	  // 실제로 렌더된 카드 노드들을 가져오기 위한 보편적 셀렉터:
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

	  if (stockSearch) {
	    stockSearch.addEventListener('input', function() {
	      filterCars(this.value);
	    });
	  }
	  if (clearBtn) {
	    clearBtn.addEventListener('click', function() {
	      if (stockSearch) {
	        stockSearch.value = '';
	        filterCars('');
	        stockSearch.focus();
	      }
	    });
	  }

	  // ----- 차트 데이터 생성 -----
	  // Thymeleaf로 렌더된 DOM에서 데이터 수집 (name, stock)
	  function collectStockData() {
	    const dataMap = new Map(); // name -> stock sum
	    const cardEls = document.querySelectorAll('.carList [th\\:each], .carList .car-item, .carList .carList');
	    // 보다 안전하게, carList의 각 노드에서 .car-name, .stock-number 사용
	    const items = document.querySelectorAll('.carList .car-item, .car-item');
	    items.forEach(it => {
	      const nameEl = it.querySelector('.car-name');
	      const stockEl = it.querySelector('.stock-number');
	      const name = (nameEl ? nameEl.textContent.trim() : (it.dataset.name || 'Unknown')).trim();
	      const stock = parseInt((stockEl ? stockEl.textContent : it.dataset.stock) || 0, 10) || 0;
	      dataMap.set(name, (dataMap.get(name) || 0) + stock);
	    });
	    return dataMap;
	  }

	  // 차트 렌더
	  function renderChart() {
	    const dataMap = collectStockData();
	    const labels = Array.from(dataMap.keys()).slice(0, 10); // 상위 10개 모델만 표시
	    const values = labels.map(l => dataMap.get(l));

	    // 총계 업데이트
	    const totalModels = document.getElementById('totalModels');
	    const totalStock = document.getElementById('totalStock');
	    const outOfStockCount = document.getElementById('outOfStockCount');
	    if (totalModels) totalModels.textContent = dataMap.size;
	    if (totalStock) totalStock.textContent = Array.from(dataMap.values()).reduce((s, v) => s + v, 0);
	    if (outOfStockCount) outOfStockCount.textContent = Array.from(dataMap.values()).filter(v => v <= 0).length;

	    const ctx = document.getElementById('stockChart');
	    if (!ctx) return;

	    // destroy 기존 차트가 있다면 제거 (SPA 재렌더 안전)
	    if (ctx.chartInstance) {
	      ctx.chartInstance.destroy();
	    }

	    ctx.chartInstance = new Chart(ctx, {
	      type: 'bar',
	      data: {
	        labels: labels,
	        datasets: [{
	          label: '재고수',
	          data: values,
	          borderRadius: 6,
	          borderWidth: 0,
	          barThickness: 24,
	          backgroundColor: labels.map(() => '#FF5500')
	        }]
	      },
	      options: {
	        responsive: true,
	        plugins: {
	          legend: { display: false },
	          tooltip: { mode: 'index', intersect: false }
	        },
	        scales: {
	          x: {
	            ticks: { color: '#333' }
	          },
	          y: {
	            beginAtZero: true,
	            ticks: { color: '#333' }
	          }
	        }
	      }
	    });
	  }

	  // 문서 준비 완전히 된 후 차트 렌더
	  renderChart();

	  // 만약 동적으로 목록이 바뀌면(예: 검색결과가 필터링된 후 재계산 원하면)
	  // filter 이벤트에서 renderChart()를 다시 호출하면 됩니다.
	
	
	
});
