document.addEventListener("DOMContentLoaded", () => {
    const modifyButtons = document.querySelectorAll(".answerModify");
    const deleteButtons = document.querySelectorAll(".answerDelete");

    // 삭제 버튼: 링크로 이동 전 confirm
    deleteButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            const answerArea = btn.closest(".answerArea");
            const answerId = answerArea.dataset.answerId;

            // confirm 창 띄우기
            const confirmed = window.confirm("정말 이 댓글을 삭제하시겠습니까?");
            if (!confirmed) return; // 취소 시 종료

            // 삭제 URL로 이동
            window.location.href = `/CRMSystem/support/qna/answer/delete?answerId=${answerId}`;
        });
    });

    // 수정 버튼: textarea로 변경
    modifyButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            const answerArea = btn.closest(".answerArea");
            const answerId = answerArea.dataset.answerId;
            const contentSpan = answerArea.querySelector(".answerContent");

            if (answerArea.querySelector("textarea")) return;

            const originalText = contentSpan.innerText.trim();

            const textarea = document.createElement("textarea");
            textarea.classList.add("answerEditTextarea");
            textarea.value = originalText;

            contentSpan.replaceWith(textarea);

            const btnArea = answerArea.querySelector(".answerBtnArea");
            btnArea.style.display = "none";

            const editBtnArea = document.createElement("span");
            editBtnArea.classList.add("answerEditBtnArea");
            editBtnArea.innerHTML = `
                <button class="answerSave btn">저장</button>
                <button class="answerCancel btn">취소</button>
            `;
            answerArea.appendChild(editBtnArea);

            // 저장 버튼 클릭 시: 링크로 이동
            editBtnArea.querySelector(".answerSave").addEventListener("click", () => {
                const newText = textarea.value.trim();
                window.location.href =
                    `/CRMSystem/support/qna/answer/update?answerId=${answerId}&content=${encodeURIComponent(newText)}`;
            });

            // 취소 버튼 클릭 시
            editBtnArea.querySelector(".answerCancel").addEventListener("click", () => {
                const originalSpan = document.createElement("span");
                originalSpan.classList.add("answerContent");
                originalSpan.innerText = originalText;

                textarea.replaceWith(originalSpan);
                editBtnArea.remove();
                btnArea.style.display = "inline-block";
            });
        });
    });
});
