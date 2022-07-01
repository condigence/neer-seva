


/*********************************/
/*         INDEX                 */
/*================================
 *     01.  Loader               *
 *     02.  Menus                *
 *     03.  Toggle Menu          *
 *     04.  Back to top          *
 *     05.  Feather icon         *
 *     06.  ToolTip              *
 ================================*/


 window.addEventListener('load',   fn , false )

 //  window.onload = function loader() {
 function fn() {
     // Preloader
     if(document.getElementById('preloader')){
         setTimeout(() => {
             document.getElementById('preloader').style.visibility = 'hidden';
             document.getElementById('preloader').style.opacity = '0';
         }, 350);
     }
 }
 //Menu
 function windowScroll() {
   const navbar = document.getElementById("navbar");
   if (
     document.body.scrollTop >= 50 ||
     document.documentElement.scrollTop >= 50
   ) {
     navbar.classList.add("nav-sticky");
   } else {
     navbar.classList.remove("nav-sticky");
   }
 }
 window.addEventListener("scroll", (ev) => {
   ev.preventDefault();
   windowScroll();
 });
   // Toggle menu
 function toggleMenu() {
   document.getElementById('isToggle').classList.toggle('open');
   var isOpen = document.getElementById('navbar')
   if (isOpen.style.display === "block") {
       isOpen.style.display = "none";
   } else {
       isOpen.style.display = "block";
   }
 };
 
 // back-to-top
 var mybutton = document.getElementById("back-to-top");
 window.onscroll = function () {
     scrollFunction();
 };
 
 function scrollFunction() {
     if(mybutton!=null){
         if (document.body.scrollTop > 500 || document.documentElement.scrollTop > 500) {
             mybutton.style.display = "block";
         } else {
             mybutton.style.display = "none";
         }
     }
 }
 
 function topFunction() {
     document.body.scrollTop = 0;
     document.documentElement.scrollTop = 0;
 }
 
 //Feather icon
 feather.replace();
 
 //Tooltip
 var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
 var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
   return new bootstrap.Tooltip(tooltipTriggerEl)
 });
   