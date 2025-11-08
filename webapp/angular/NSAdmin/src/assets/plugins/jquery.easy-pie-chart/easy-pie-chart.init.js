/* Chart.js replacement for easy-pie-chart initializers
	 This script finds elements that previously used easyPieChart classes
	 and renders a Chart.js doughnut showing the percentage. It is safe
	 if the elements are not present (no-op).
*/
(function () {
	'use strict';

	const classColorMap = {
		'easy-pie-chart-1': '#5e72e4',
		'easy-pie-chart-2': '#2dce89',
		'easy-pie-chart-3': '#11cdef',
		'easy-pie-chart-4': '#f5365c',
		'easy-pie-chart-5': '#ff2fa0',
		'easy-pie-chart-6': '#fb6340',
		'easy-pie-chart-7': '#172b4d',
		'easy-pie-chart-8': '#5e72e4'
	};

	function getPercentFromElement(el) {
		// try data-percent, then .percent text, then fallback to 0
		const dp = el.getAttribute('data-percent');
		if (dp !== null) return Number(dp) || 0;
		const pct = el.querySelector('.percent');
		if (pct) return Number(pct.textContent.trim()) || 0;
		return 0;
	}

	function createDoughnut(el, percent, color) {
		// create a canvas and render a small doughnut representing percent
		const canvas = document.createElement('canvas');
		canvas.width = 80;
		canvas.height = 80;
		// clear existing content and insert canvas
		el.innerHTML = '';
		el.appendChild(canvas);

		// Chart.js v2-style options used in this project
		if (!window.Chart) return;
		new Chart(canvas.getContext('2d'), {
			type: 'doughnut',
			data: {
				labels: ['', ''],
				datasets: [{
					data: [percent, Math.max(0, 100 - percent)],
					backgroundColor: [color, '#e9ecef'],
					borderWidth: 0
				}]
			},
			options: {
				cutoutPercentage: 70,
				responsive: false,
				maintainAspectRatio: false,
				legend: { display: false },
				tooltips: { enabled: false }
			}
		});

		// Add center percent text
		const label = document.createElement('div');
		label.className = 'easy-pie-percent-label';
		label.style.position = 'absolute';
		label.style.left = '50%';
		label.style.top = '50%';
		label.style.transform = 'translate(-50%, -50%)';
		label.style.fontSize = '12px';
		label.style.fontWeight = '600';
		label.textContent = Math.round(percent) + '%';
		el.style.position = 'relative';
		el.appendChild(label);
	}

	// Run on DOM ready
	if (document.readyState === 'loading') {
		document.addEventListener('DOMContentLoaded', init);
	} else {
		init();
	}

	function init() {
		Object.keys(classColorMap).forEach(cls => {
			document.querySelectorAll('.' + cls).forEach(el => {
				const percent = getPercentFromElement(el);
				createDoughnut(el, percent, classColorMap[cls]);
			});
		});
	}
})();