$(function() {
    "use strict";


// chart 1

  $('#dashboard2-chart-1').sparkline([5,8,7,10,5,8,7,10,5,8,7,10,5,8,7,10,5,8,7,10], {
            type: 'bar',
            height: '40',
            barWidth: '2',
            resize: true,
            barSpacing: '5',
            barColor: '#fff'
        });


  // chart 2

  $('#dashboard2-chart-2').sparkline([5,8,7,10,5,8,7,10,5,8,7,10,5,8,7,10,5,8,7,10], {
            type: 'bar',
            height: '40',
            barWidth: '2',
            resize: true,
            barSpacing: '5',
            barColor: '#fff'
        });


  // chart 3

  $('#dashboard2-chart-3').sparkline([5,8,7,10,5,8,7,10,5,8,7,10,5,8,7,10,5,8,7,10], {
            type: 'bar',
            height: '40',
            barWidth: '2',
            resize: true,
            barSpacing: '5',
            barColor: '#fff'
        });


// chart 4

     var ctx = document.getElementById('dashboard2-chart-4').getContext('2d');
              
      // Chart initialization removed - handled by Angular components/ng2-charts


	  
// chart 5
    var ctx = document.getElementById("dashboard2-chart-5").getContext('2d');
   
      var gradientStroke3 = ctx.createLinearGradient(0, 0, 0, 300);
      gradientStroke3.addColorStop(0, '#ff6a00');
      gradientStroke3.addColorStop(1, '#ee0979');

      var gradientStroke4 = ctx.createLinearGradient(0, 0, 0, 300);
      gradientStroke4.addColorStop(0, ' #00b09b');
      gradientStroke4.addColorStop(0.5, '#96c93d');

      // Chart initialization removed - handled by Angular components/ng2-charts
	  
	  
	  
	  
  // chart 6
 var ctx = document.getElementById('dashboard2-chart-6').getContext('2d');

  var gradientStroke1 = ctx.createLinearGradient(0, 0, 0, 300);
      gradientStroke1.addColorStop(0, 'rgba(255, 255, 255, 0.5');
      gradientStroke1.addColorStop(1, 'rgba(255, 255, 255, 0.1)');

      // Chart initialization removed - handled by Angular components/ng2-charts
	  	  
		  
// world map

jQuery('#dashboard-map').vectorMap(
{
    map: 'world_mill_en',
    backgroundColor: 'transparent',
    borderColor: '#818181',
    borderOpacity: 0.25,
    borderWidth: 1,
    zoomOnScroll: false,
    color: '#009efb',
    regionStyle : {
        initial : {
          fill : '#11cdef'
        }
      },
    markerStyle: {
      initial: {
                    r: 9,
                    'fill': '#fff',
                    'fill-opacity':1,
                    'stroke': '#000',
                    'stroke-width' : 5,
                    'stroke-opacity': 0.4
                },
                },
    enableZoom: true,
    hoverColor: '#009efb',
    markers : [{
        latLng : [21.00, 78.00],
        name : 'I Love My India'
      
      }],
    hoverOpacity: null,
    normalizeFunction: 'linear',
    scaleColors: ['#b6d6ff', '#005ace'],
    selectedColor: '#c9dfaf',
    selectedRegions: [],
    showTooltip: true
	
   });
  


   });	 
   