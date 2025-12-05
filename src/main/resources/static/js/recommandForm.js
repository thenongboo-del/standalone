    let sessionId = null;

    function startConsultation(){
        const budget = document.getElementById("budget").value || "미정";
        const age = document.getElementById("age").value || "미입력";
        const gender = document.getElementById("gender").value || "미입력";
        const purpose = document.getElementById("purpose").value || "미입력";
        const family = document.getElementById("family").value || "없음";
        const etc = document.getElementById("etc").value || "없음";

        const firstMessage = `안녕하세요! 고객님 정보를 바탕으로 추천해드릴게요.\n예산: ${budget}만원 | 나이: ${age} | 성별: ${gender} | 용도: ${purpose} | 가족: ${family} | 기타: ${etc}`;

        // 세션 라벨
        document.getElementById('sessionLabel').innerText = '진행중';

        // 첫 메시지 전송 표시
        addMessage('user', '안녕하세요! 상담 부탁드려요~', true);
        sendMessage(firstMessage);
    }

    function formatTime(date){
        const h = String(date.getHours()).padStart(2,'0');
        const m = String(date.getMinutes()).padStart(2,'0');
        return `${h}:${m}`;
    }

    function addMessage(sender, text, withMeta=false){
        const area = document.getElementById('responseArea');
        const row = document.createElement('div');
        row.className = `message-row ${sender}`;

        const avatar = document.createElement('div');
        avatar.className = `avatar ${sender==='bot'? 'bot' : ''}`;
        avatar.textContent = sender==='bot'? 'Bot' : 'ME';

        const bubble = document.createElement('div');
        bubble.className = `bubble ${sender}`;
        bubble.innerHTML = text.replace(/\n/g,'<br>');

        const meta = document.createElement('div');
        meta.className = 'meta';
        meta.textContent = formatTime(new Date());
        if(sender==='user') meta.classList.add('right');

        if(sender==='bot'){
            row.appendChild(avatar);
            row.appendChild(bubble);
            bubble.appendChild(meta);
        } else {
            row.appendChild(bubble);
            row.appendChild(avatar);
            bubble.appendChild(meta);
        }

        area.appendChild(row);
        // 부드러운 스크롤: 항상 최신 메시지 보이기
        area.scrollTop = area.scrollHeight;
    }

    function sendMessage(message){
        fetch('http://localhost:8003/chat', {
            method: 'POST', headers: {'Content-Type':'application/json'},
            body: JSON.stringify({ message: message, session_id: sessionId })
        })
        .then(r=>r.json())
        .then(data=>{
            sessionId = data.session_id || sessionId;
            document.getElementById('sessionLabel').innerText = sessionId ? '연결됨' : '진행중';
            addMessage('bot', data.reply || '죄송합니다. 답변을 준비하지 못했습니다.');
        })
        .catch(err=>{
            console.error(err);
            addMessage('bot', '죄송합니다, 응답을 받지 못했습니다. 네트워크를 확인해주세요.');
        });
    }

    // 채팅 폼 제출
    document.getElementById('chatForm').addEventListener('submit', function(e){
        e.preventDefault();
        const msgEl = document.getElementById('message');
        const txt = msgEl.value.trim();
        if(!txt) return;
        addMessage('user', txt, true);
        sendMessage(txt);
        msgEl.value = '';
    });

    // 엔터 = 전송 (Shift+Enter = 줄바꿈)
    document.getElementById('message').addEventListener('keydown', function(e){
        if(e.key === 'Enter' && !e.shiftKey){ e.preventDefault(); this.form.requestSubmit(); }
    });

