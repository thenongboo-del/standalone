function removeQuestion(questionId) {
    if (confirm("게시물을 정말 삭제하시겠습니까?")) {
        const form = document.getElementById("deleteForm");
        form.action = "/CRMSystem/support/qna/delete/" + questionId;
        form.submit();  // 폼 제출 → 삭제 처리됨
    }
}