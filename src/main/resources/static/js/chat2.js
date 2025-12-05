    // 채팅 계속 쌓이도록 수정한 JS
    document.getElementById("chatForm").addEventListener("submit", function (e) {
        e.preventDefault();

        const msg = document.getElementById("message").value.trim();
        if (!msg) return;

        // 사용자 메시지 먼저 추가
        appendMessage(msg, "user");

        // 입력창 비우기
        document.getElementById("message").value = "";

        fetch("http://localhost:8002/chat", {   // 포트 8000 → 8080으로 수정 (Spring Boot 기본)
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ message: msg })
        })
        .then(resp => resp.json())
        .then(data => {
            appendMessage(data.answer || data.reply || "답변이 없어요", "bot");
        })
        .catch(err => {
            appendMessage("서버 연결 오류: " + err.message, "bot");
        });
    });

    // 메시지 추가 함수
    function appendMessage(text, sender) {
        const area = document.getElementById("responseArea");
        const div = document.createElement("div");
        div.className = `message ${sender}`;
        div.innerHTML = `<strong>${sender === 'user' ? '나' : 'StandaloneBot'}</strong><br>${text}`;
        area.appendChild(div);

        // 자동 스크롤 아래로
        area.scrollTop = area.scrollHeight;
    }
