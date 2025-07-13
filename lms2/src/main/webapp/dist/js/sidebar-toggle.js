/**
 * sidebar-toggle
 */

document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll('.accordion-button').forEach(btn => {
    const icon = btn.querySelector('.toggle-icon');
    const target = document.querySelector(btn.dataset.bsTarget);

    if (icon && target) {
      target.addEventListener('show.bs.collapse', () => icon.textContent = '−');
      target.addEventListener('hide.bs.collapse', () => icon.textContent = '+');
    }
  });
});