$(function() {
    "use strict";

// chart 1

// Chart initialization removed - handled by Angular components/ng2-charts



// chart 2

// Chart initialization removed - handled by Angular components/ng2-charts

	
// chart 3

// Chart initialization removed - handled by Angular components/ng2-charts

   
 // Replace peity donut with Chart.js doughnuts
  function _createDoughnutFromSpan(spanEl) {
    try {
      const txt = spanEl.textContent.trim();
      const parts = txt.indexOf(',')>-1 ? txt.split(',') : (txt.indexOf('/')>-1 ? txt.split('/') : txt.split(/\s+/));
      const values = parts.map(p => parseFloat(p) || 0);
      const canvas = document.createElement('canvas');
      canvas.width = 120; canvas.height = 120;
      spanEl.parentNode.replaceChild(canvas, spanEl);
      const ctx = canvas.getContext('2d');
// Chart initialization removed - handled by Angular components/ng2-charts
    } catch (e) { console.debug('createDoughnutFromSpan error', e); }
  }

  document.querySelectorAll('span.donut').forEach(el => _createDoughnutFromSpan(el));







   });	 
   