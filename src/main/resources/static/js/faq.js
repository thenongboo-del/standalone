document.addEventListener('DOMContentLoaded', () => {
  // 설정
  const PAGE_SIZE = 6;         // 페이지당 아이템 수 (원하면 변경)
  const DEBOUNCE_MS = 200;     // 검색 debounce 시간

  // DOM 포인트
  const faqListContainer = document.querySelector('.faqList');
  if (!faqListContainer) return;

  // 삽입할 UI 영역 (검색 + 페이저)
  const controlsWrapper = document.createElement('div');
  controlsWrapper.className = 'faq-controls';
  controlsWrapper.style.display = 'flex';
  controlsWrapper.style.justifyContent = 'space-between';
  controlsWrapper.style.alignItems = 'center';
  controlsWrapper.style.margin = '18px 0';

  // 검색 입력
  const searchBox = document.createElement('input');
  searchBox.type = 'search';
  searchBox.placeholder = '질문/답변으로 검색...';
  searchBox.className = 'faq-search-input';
  searchBox.style.padding = '10px';
  searchBox.style.width = '60%';
  searchBox.setAttribute('aria-label', 'FAQ 검색');

  // 페이저 컨테이너
  const pagerContainer = document.createElement('div');
  pagerContainer.className = 'faq-pager';
  pagerContainer.style.display = 'flex';
  pagerContainer.style.gap = '6px';
  pagerContainer.style.alignItems = 'center';

  controlsWrapper.appendChild(searchBox);
  controlsWrapper.appendChild(pagerContainer);

  // controlsWrapper를 faqListContainer 바로 위에 삽입
  faqListContainer.parentNode.insertBefore(controlsWrapper, faqListContainer);

  // 원본 항목들 스냅샷
  const originalItems = Array.from(faqListContainer.querySelectorAll('.faqItem'));

  // 내부 상태
  let filteredItems = originalItems.slice();
  let currentPage = 1;
  let totalPages = Math.max(1, Math.ceil(filteredItems.length / PAGE_SIZE));

  // 유틸: 텍스트 추출 (질문 + 답변)
  const textOf = (item) => {
    const q = item.querySelector('.faqQuestion')?.innerText || '';
    const a = item.querySelector('.faqAnswer')?.innerText || '';
    return (q + '\n' + a).trim();
  };

  // 모든 open 닫기
  function closeAllAnswers() {
    const answers = faqListContainer.querySelectorAll('.faqAnswer.open');
    answers.forEach(a => {
      a.classList.remove('open');
      a.hidden = true;
      const btn = faqListContainer.querySelector(`#${a.getAttribute('aria-labelledby')}`);
      if (btn) btn.setAttribute('aria-expanded', 'false');
    });
  }

  // 페이지 렌더링: filteredItems -> DOM
  function renderPage(page) {
    closeAllAnswers();

    // bounds
    totalPages = Math.max(1, Math.ceil(filteredItems.length / PAGE_SIZE));
    if (page < 1) page = 1;
    if (page > totalPages) page = totalPages;
    currentPage = page;

    // clear container then append visible items
    faqListContainer.innerHTML = '';

    const start = (currentPage - 1) * PAGE_SIZE;
    const slice = filteredItems.slice(start, start + PAGE_SIZE);

    // append each element node (we clone original nodes to preserve event handlers in JS file)
    slice.forEach(node => {
      faqListContainer.appendChild(node);
    });

    renderPager();
  }

  // 페이저 UI 생성/업데이트
  function renderPager() {
    pagerContainer.innerHTML = '';

    const info = document.createElement('div');
    info.className = 'pager-info';
    info.style.fontSize = '0.95rem';
    info.style.color = '#fff';
    info.style.opacity = '0.9';
    info.textContent = `전체 ${filteredItems.length}개 · ${currentPage}/${totalPages} 페이지`;
    pagerContainer.appendChild(info);

    const btns = document.createElement('div');
    btns.style.display = 'flex';
    btns.style.gap = '6px';
    btns.style.marginLeft = '8px';

    const makeBtn = (label, disabled, onClick) => {
      const b = document.createElement('button');
      b.type = 'button';
      b.textContent = label;
      b.disabled = !!disabled;
      b.style.padding = '6px 10px';
      b.style.borderRadius = '6px';
      b.style.border = 'none';
      b.style.cursor = disabled ? 'not-allowed' : 'pointer';
      b.addEventListener('click', onClick);
      return b;
    };

    btns.appendChild(makeBtn('<<', currentPage === 1, () => renderPage(1)));
    btns.appendChild(makeBtn('<', currentPage === 1, () => renderPage(currentPage - 1)));

    // 숫자 페이지 (간단 구현: 현재 기준 +/- 2 페이지)
    const startPage = Math.max(1, currentPage - 2);
    const endPage = Math.min(totalPages, currentPage + 2);
    for (let p = startPage; p <= endPage; p++) {
      const pageBtn = makeBtn(String(p), false, () => renderPage(p));
      if (p === currentPage) {
        pageBtn.style.fontWeight = '700';
        pageBtn.style.textDecoration = 'underline';
      }
      btns.appendChild(pageBtn);
    }

    btns.appendChild(makeBtn('>', currentPage === totalPages, () => renderPage(currentPage + 1)));
    btns.appendChild(makeBtn('>>', currentPage === totalPages, () => renderPage(totalPages)));

    pagerContainer.appendChild(btns);
  }

  // 검색 처리 (debounced)
  function debounce(fn, ms) {
    let t = null;
    return (...args) => {
      clearTimeout(t);
      t = setTimeout(() => fn(...args), ms);
    };
  }

  function doSearch(rawQuery) {
    const q = (rawQuery || '').trim().toLowerCase();
    if (!q) {
      // 모든 아이템 복원 (원본 순서 유지)
      filteredItems = originalItems.slice();
    } else {
      filteredItems = originalItems.filter(item => {
        return textOf(item).toLowerCase().includes(q);
      });
    }
    // 페이지 1로 이동
    renderPage(1);
  }

  const debouncedSearch = debounce(doSearch, DEBOUNCE_MS);
  searchBox.addEventListener('input', (e) => {
    debouncedSearch(e.target.value);
  });

  // 초기: attach click 토글 (답변 열기/닫기) — 원래 스크립트 대신 이 토글을 사용
  function attachToggleHandlers() {
    // 각 faqItem에 대해 질문 버튼 클릭시 aria-expanded 토글 + open class 토글
    originalItems.forEach(item => {
      const btn = item.querySelector('.faqQuestion');
      const answer = item.querySelector('.faqAnswer');
      if (!btn || !answer) return;

      // ensure id/aria attributes exist (they already do in template)
      btn.addEventListener('click', () => {
        const isOpen = btn.getAttribute('aria-expanded') === 'true';
        // 닫기: 모두 닫는 동작을 유지 (single-open UX)
        originalItems.forEach(i => {
          const b = i.querySelector('.faqQuestion');
          const a = i.querySelector('.faqAnswer');
          if (!b || !a) return;
          b.setAttribute('aria-expanded', 'false');
          a.classList.remove('open');
          a.hidden = true;
        });

        if (!isOpen) {
          btn.setAttribute('aria-expanded', 'true');
          answer.classList.add('open');
          answer.hidden = false;
        }
      });
    });
  }

  // 초기화 순서
  attachToggleHandlers();
  renderPage(1);

  // 접근성: 키보드로 검색창에서 Enter치면 포커스를 첫 결과 질문으로
  searchBox.addEventListener('keydown', (e) => {
    if (e.key === 'Enter') {
      const first = faqListContainer.querySelector('.faqQuestion');
      if (first) first.focus();
    }
  });

  // Expose a tiny API on window (옵션 — 디버깅용)
  window.faqSearchPager = {
    search: (s) => { searchBox.value = s; doSearch(s); },
    goToPage: (p) => { renderPage(p); },
    getState: () => ({ total: filteredItems.length, page: currentPage, totalPages })
  };
});
