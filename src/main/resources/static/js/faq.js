document.addEventListener("DOMContentLoaded", () => {
    const faqItems = document.querySelectorAll(".faqItem");

    faqItems.forEach(item => {
        const btn = item.querySelector(".faqQuestion");
        const answer = item.querySelector(".faqAnswer");

        btn.addEventListener("click", () => {
            const isOpen = btn.getAttribute("aria-expanded") === "true";

            // 모든 FAQ 닫기 (원한다면 주석 처리하면 다중 열림 가능)
            faqItems.forEach(i => {
                const b = i.querySelector(".faqQuestion");
                const a = i.querySelector(".faqAnswer");
                b.setAttribute("aria-expanded", "false");
                a.classList.remove("open");
                a.hidden = true;
            });

            if (!isOpen) {
                btn.setAttribute("aria-expanded", "true");
                answer.classList.add("open");
                answer.hidden = false;
            }
        });
    });
});
