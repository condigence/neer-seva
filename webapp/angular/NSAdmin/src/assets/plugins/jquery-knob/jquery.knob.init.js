/* Lightweight Chart.js replacement for jQuery Knob visuals
   Finds inputs or elements with class 'knob' or 'dial' and renders a small
   Chart.js doughnut instead. This avoids loading the large jQuery-knob library
   on pages that just display static values.
*/
(function(){
  'use strict';

  function initKnobs() {
    if (!window.Chart) return;

    // target inputs or elements that commonly held a knob
    const nodes = Array.from(document.querySelectorAll('input.knob, .knob, .dial'));
    nodes.forEach(node => {
      // try to read value from value attribute or data-value, else fallback
      const raw = node.getAttribute('value') || node.getAttribute('data-value') || node.textContent;
      const val = Math.max(0, Math.min(100, Number(raw) || 0));

      // create container & canvas
      const container = document.createElement('div');
      container.style.display = 'inline-block';
      container.style.position = 'relative';
      const canvas = document.createElement('canvas');
      canvas.width = 80; canvas.height = 80;
      container.appendChild(canvas);

      // replace node with container (preserve id/class if present)
      if (node.parentNode) node.parentNode.replaceChild(container, node);

      // Chart initialization removed - handled by Angular/ng2-charts or component-level code.

      const label = document.createElement('div');
      label.style.position = 'absolute';
      label.style.left = '50%';
      label.style.top = '50%';
      label.style.transform = 'translate(-50%, -50%)';
      label.style.fontWeight = '600';
      label.style.fontSize = '12px';
      label.textContent = val + '%';
      container.appendChild(label);
    });
  }

  if (document.readyState === 'loading') document.addEventListener('DOMContentLoaded', initKnobs);
  else initKnobs();

})();
