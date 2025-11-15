$(function() {
    "use strict";


// chart 1 and chart 2 initializers removed to avoid double-initialization.
// Charts are initialized from Angular components (ng2-charts) or by the
// component code so global initializers are not required here.


// chart 3

var el3 = document.getElementById('dashboard-chart-3');
if (el3 && el3.getContext) {
     var ctx = el3.getContext('2d');

   	var gradientStroke1 = ctx.createLinearGradient(0, 0, 0, 300);
          gradientStroke1.addColorStop(0, 'rgba(37, 117, 252, 0.9)');
          gradientStroke1.addColorStop(1, 'rgba(106, 17, 203, 0.5)');

       // Chart initialization removed - handled by Angular components/ng2-charts
}


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
