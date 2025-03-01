const modal = document.getElementById("modal");
const closeBtn = document.getElementById("close");
const modalTitle = document.getElementById("modal-title");

export function openModal(title = "Login") {
    modalTitle.textContent = title;
    modal.style.display = "flex";
}

export function closeModal() {
    modal.style.display = "none";
}

closeBtn.addEventListener("click", closeModal);
window.addEventListener("click", (e) => {
    if (e.target === modal) closeModal();
});
document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") closeModal();
});
