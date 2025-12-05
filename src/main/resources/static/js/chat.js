document.getElementById("chatForm").addEventListener("submit", function (e) {
    e.preventDefault(); // 기본 submit 막기

    const msg = document.getElementById("message").value;

    fetch("http://localhost:8000/chat", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            message: msg,
            session_id: null
        }),
    })
        .then((resp) => resp.json())
        .then((data) => {
            document.getElementById("responseArea").innerHTML =
                `<p><strong>나:</strong> ${msg}</p>
                 <p><strong>StandaloneBot:</strong> ${data.reply}</p>`;
        })
        .catch((err) => {
            document.getElementById("responseArea").innerHTML =
                `<p style="color:red">오류 발생: ${err}</p>`;
        });
});
