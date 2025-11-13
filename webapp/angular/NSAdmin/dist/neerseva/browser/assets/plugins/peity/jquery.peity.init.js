       
$(function() {
    "use strict";

             // Convert peity visuals to Chart.js equivalents
            function _toNumberArray(txt) {
                if (!txt) return [];
                const parts = txt.indexOf(',')>-1 ? txt.split(',') : (txt.indexOf('/')>-1 ? txt.split('/') : txt.split(/\s+/));
                return parts.map(p => parseFloat((p||'').toString().replace(/[^0-9\.\-]/g,'')) || 0);
            }

            function _replaceWithCanvas(el, width=120, height=120) {
                const canvas = document.createElement('canvas');
                canvas.width = width; canvas.height = height;
                el.parentNode.replaceChild(canvas, el);
                return canvas;
            }

            // pie -> Chart.js pie
            document.querySelectorAll('span.pie').forEach(el => {
                try {
                    const vals = _toNumberArray(el.textContent.trim());
                    const canvas = _replaceWithCanvas(el, 120, 120);
                    // Chart initialization removed - handled by Angular/ng2-charts or component-level code.
                } catch(e){console.debug('pie->chart error', e)}
            });

            // donut -> Chart.js doughnut
            document.querySelectorAll('span.donut').forEach(el => {
                try {
                    const vals = _toNumberArray(el.textContent.trim());
                    const canvas = _replaceWithCanvas(el, 120, 120);
                    // Chart initialization removed - handled by Angular/ng2-charts or component-level code.
                } catch(e){console.debug('donut->chart error', e)}
            });

            // line -> Chart.js line
            document.querySelectorAll('.peity-line').forEach(el => {
                try {
                    const vals = _toNumberArray(el.textContent.trim());
                    const canvas = _replaceWithCanvas(el, 120, 40);
                    // Chart initialization removed - handled by Angular/ng2-charts or component-level code.
                } catch(e){console.debug('line->chart error', e)}
            });

            // bar -> Chart.js bar
            document.querySelectorAll('.peity-bar').forEach(el => {
                try {
                    const vals = _toNumberArray(el.textContent.trim());
                    const canvas = _replaceWithCanvas(el, 120, 40);
                    // Chart initialization removed - handled by Angular/ng2-charts or component-level code.
                } catch(e){console.debug('bar->chart error', e)}
            });
         
   });