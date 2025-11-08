       
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
                    new Chart(canvas.getContext('2d'), { type: 'pie', data: { labels: vals.map(()=>''), datasets: [{ data: vals, backgroundColor: ['#5e72e4','#ff2fa0','#2dce89','#f5365c','#fb6340'] }] }, options: { legend:{display:false}, tooltips:{displayColors:false}}});
                } catch(e){console.debug('pie->chart error', e)}
            });

            // donut -> Chart.js doughnut
            document.querySelectorAll('span.donut').forEach(el => {
                try {
                    const vals = _toNumberArray(el.textContent.trim());
                    const canvas = _replaceWithCanvas(el, 120, 120);
                    new Chart(canvas.getContext('2d'), { type: 'doughnut', data: { labels: vals.map(()=>''), datasets: [{ data: vals, backgroundColor: ['#5e72e4','#ff2fa0','#2dce89','#f5365c','#fb6340'] }] }, options: { cutoutPercentage: 25, legend:{display:false}, tooltips:{displayColors:false}}});
                } catch(e){console.debug('donut->chart error', e)}
            });

            // line -> Chart.js line
            document.querySelectorAll('.peity-line').forEach(el => {
                try {
                    const vals = _toNumberArray(el.textContent.trim());
                    const canvas = _replaceWithCanvas(el, 120, 40);
                    new Chart(canvas.getContext('2d'), { type: 'line', data: { labels: vals.map((v,i)=>i+1), datasets:[{ data: vals, borderColor: '#11cdef', backgroundColor: 'rgba(17,205,239,0.1)', pointRadius:0, fill:true }] }, options: { legend:{display:false}, tooltips:{displayColors:false}, scales:{xAxes:[{display:false}], yAxes:[{display:false}] } } });
                } catch(e){console.debug('line->chart error', e)}
            });

            // bar -> Chart.js bar
            document.querySelectorAll('.peity-bar').forEach(el => {
                try {
                    const vals = _toNumberArray(el.textContent.trim());
                    const canvas = _replaceWithCanvas(el, 120, 40);
                    new Chart(canvas.getContext('2d'), { type: 'bar', data: { labels: vals.map((v,i)=>i+1), datasets:[{ data: vals, backgroundColor: '#11cdef', borderWidth:0 }] }, options:{ legend:{display:false}, tooltips:{displayColors:false}, scales:{xAxes:[{display:false}], yAxes:[{display:false}]} } });
                } catch(e){console.debug('bar->chart error', e)}
            });
         
   });