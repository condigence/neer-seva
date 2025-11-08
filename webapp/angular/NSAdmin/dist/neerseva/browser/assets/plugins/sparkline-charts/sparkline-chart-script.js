/* Chart.js replacements for sparkline charts
   Replace jQuery Sparkline initializers with Chart.js canvas charts.
   If Chart.js is not present, this file is a no-op.
*/
(function () {
  'use strict';

  function makeCanvasReplace(el) {
    const canvas = document.createElement('canvas');
    canvas.width = el.clientWidth || 150;
    canvas.height = el.clientHeight || 45;
    el.innerHTML = '';
    el.appendChild(canvas);
    return canvas;
  }

  function init() {
    if (!window.Chart) return;

    // chart 1: small bar
    const el1 = document.getElementById('sparklinechart1');
    if (el1) {
      const data = [1,4,5,9,8,10,5,8,4,1,0,7,5,7,9,8,10,5];
      const canvas = makeCanvasReplace(el1);
      new Chart(canvas.getContext('2d'), {
        type: 'bar',
        data: { labels: data.map((_,i)=>i+1), datasets:[{ data, backgroundColor: '#ffffff' }] },
        options: { legend:{display:false}, tooltips:{enabled:false}, scales:{ xAxes:[{display:false}], yAxes:[{display:false}] }, responsive:false, maintainAspectRatio:false }
      });
    }

    // chart 2: small pie
    const el2 = document.getElementById('sparklinechart2');
    if (el2) {
      const data = [20,20,20];
      const canvas = makeCanvasReplace(el2);
      new Chart(canvas.getContext('2d'), { type: 'pie', data: { labels:['','',''], datasets:[{ data, backgroundColor:['#ff2fa0','#5e72e4','#2dce89'] }] }, options:{ legend:{display:false}, tooltips:{enabled:false}, responsive:false, maintainAspectRatio:false } });
    }

    // chart 3: small sparkline line
    const el3 = document.getElementById('sparklinechart3');
    if (el3) {
      const data = [2,4,4,6,8,5,6,4,8,6,6,2];
      const canvas = makeCanvasReplace(el3);
      new Chart(canvas.getContext('2d'), {
        type: 'line',
        data: { labels: data.map((_,i)=>i+1), datasets:[{ data, borderColor:'#2dce89', backgroundColor:'#2dce89', fill:true, pointRadius:0 }] },
        options:{ legend:{display:false}, tooltips:{enabled:false}, scales:{ xAxes:[{display:false}], yAxes:[{display:false}] }, responsive:false, maintainAspectRatio:false }
      });
    }

    // chart 4: white line on dark background
    const el4 = document.getElementById('sparklinechart4');
    if (el4) {
      const data = [0,5,10,5,15,10,20,10,5,10,5,15,10];
      const canvas = makeCanvasReplace(el4);
      new Chart(canvas.getContext('2d'), { type: 'line', data: { labels: data.map((_,i)=>i+1), datasets:[{ data, borderColor:'#ffffff', backgroundColor:'transparent', pointRadius:0, borderWidth:2 }] }, options:{ legend:{display:false}, tooltips:{enabled:false}, scales:{ xAxes:[{display:false}], yAxes:[{display:false}] }, responsive:false, maintainAspectRatio:false } });
    }

    // chart 5: larger pie
    const el5 = document.getElementById('sparklinechart5');
    if (el5) {
      const data = [40,40,40];
      const canvas = makeCanvasReplace(el5);
      new Chart(canvas.getContext('2d'), { type: 'pie', data:{ labels:['','',''], datasets:[{ data, backgroundColor:['#5e72e4','#2dce89','#f5365c'] }] }, options:{ legend:{display:false}, tooltips:{enabled:false}, responsive:false, maintainAspectRatio:false } });
    }

    // chart 6: full-width bar
    const el6 = document.getElementById('sparklinechart6');
    if (el6) {
      const data = [15,16,20,18,19,14,17,12,11,12,10,14,17,14,10,15];
      const canvas = makeCanvasReplace(el6);
      new Chart(canvas.getContext('2d'), { type: 'bar', data:{ labels:data.map((_,i)=>i+1), datasets:[{ data, backgroundColor:'#ff2fa0' }] }, options:{ legend:{display:false}, tooltips:{enabled:false}, scales:{ xAxes:[{display:false}], yAxes:[{display:false}] }, responsive:false, maintainAspectRatio:false } });
    }

    // chart 7: larger line
    const el7 = document.getElementById('sparklinechart7');
    if (el7) {
      const data = [2,4,4,6,8,5,6,4,8,6,6,2];
      const canvas = makeCanvasReplace(el7);
      new Chart(canvas.getContext('2d'), { type: 'line', data:{ labels:data.map((_,i)=>i+1), datasets:[{ data, borderColor:'#11cdef', backgroundColor:'rgba(17,205,239,0.4)', fill:true, pointRadius:0, borderWidth:3 }] }, options:{ legend:{display:false}, tooltips:{enabled:false}, scales:{ xAxes:[{display:false}], yAxes:[{display:false}] }, responsive:false, maintainAspectRatio:false } });
    }
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }

})();