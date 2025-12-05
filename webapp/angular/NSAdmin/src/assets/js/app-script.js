
$(function() {
    "use strict";
     
	 
//sidebar menu js
$.sidebarMenu($('.sidebar-menu'));

// === toggle-menu js
var _previousFocused = null;
var _focusableElements = [];
function getFocusable($container) {
    var selectors = 'a[href], area[href], input:not([disabled]), select:not([disabled]), textarea:not([disabled]), button:not([disabled]), iframe, object, embed, [tabindex]:not([tabindex="-1"]), [contenteditable]';
    return $container.find(selectors).filter(function() {
        return $(this).is(':visible');
    });
}

function openSidebar() {
    var $wrapper = $("#wrapper");
    var $sidebar = $("#sidebar-wrapper");
    $wrapper.removeClass('toggled');
    $sidebar.attr('aria-hidden', 'false');
    $(".toggle-menu").attr('aria-expanded','true');
    // show overlay
    $("#sidebar-overlay").attr('aria-hidden','false').addClass('visible');

    // focus management
    _previousFocused = document.activeElement;
    _focusableElements = getFocusable($sidebar);
    if (_focusableElements.length > 0) {
        _focusableElements.first().focus();
    } else {
        $sidebar.attr('tabindex', '-1').focus();
    }
}

function closeSidebar() {
    var $wrapper = $("#wrapper");
    var $sidebar = $("#sidebar-wrapper");
    $wrapper.addClass('toggled');
    $sidebar.attr('aria-hidden','true');
    $(".toggle-menu").attr('aria-expanded','false');
    // hide overlay
    $("#sidebar-overlay").attr('aria-hidden','true').removeClass('visible');

    // restore focus
    try {
        if (_previousFocused && typeof _previousFocused.focus === 'function') {
            _previousFocused.focus();
        }
    } catch (e) {
        // ignore
    }
}

$(".toggle-menu").on("click", function(e) {
    e.preventDefault();
    var $wrapper = $("#wrapper");
    var $sidebar = $("#sidebar-wrapper");
    var $toggle = $(this);

    // Toggle classes that control layout
    if ($wrapper.hasClass('toggled')) {
        // currently collapsed -> open
        openSidebar();
    } else {
        // currently open -> close
        closeSidebar();
    }
});

    // Click on overlay closes sidebar on small screens
    $(document).on('click', '#sidebar-overlay', function() {
        closeSidebar();
    });

    // Key handlers: ESC to close, Tab trapping when sidebar open
    $(document).on('keydown', function(e) {
        var $wrapper = $("#wrapper");
        var $sidebar = $("#sidebar-wrapper");
        var isOpen = !$wrapper.hasClass('toggled');
        if (!isOpen) return;

        if (e.key === 'Escape' || e.key === 'Esc') {
            closeSidebar();
            return;
        }

        if (e.key === 'Tab') {
            // focus trap
            var focusables = _focusableElements;
            if (!focusables || focusables.length === 0) return;
            var first = focusables.first()[0];
            var last = focusables.last()[0];
            var active = document.activeElement;
            if (e.shiftKey) {
                if (active === first || active === $sidebar[0]) {
                    e.preventDefault();
                    last.focus();
                }
            } else {
                if (active === last) {
                    e.preventDefault();
                    first.focus();
                }
            }
        }
    });

    // On load, default the sidebar to hidden for small screens so content is primary
    if (window.innerWidth < 768) {
        closeSidebar();
    } else {
        // Ensure overlay hidden on larger screens
        $("#sidebar-overlay").attr('aria-hidden','true').removeClass('visible');
    }

    // === sidebar menu activation js

$(function() {
        for (var i = window.location, o = $(".sidebar-menu a").filter(function() {
            return this.href == i;
        }).addClass("active").parent().addClass("active"); ;) {
            if (!o.is("li")) break;
            // Only add 'active' to parent if it contains a submenu with the active link
            var parent = o.parent();
            if (parent.hasClass("sidebar-submenu")) {
                o = parent.parent().addClass("active");
            } else {
                break;
            }
        }
    }),
	   
/* Back To Top */

$(document).ready(function(){ 
    $(window).on("scroll", function(){ 
        if ($(this).scrollTop() > 300) { 
            $('.back-to-top').fadeIn(); 
        } else { 
            $('.back-to-top').fadeOut(); 
        } 
    }); 
    $('.back-to-top').on("click", function(){ 
        $("html, body").animate({ scrollTop: 0 }, 600); 
        return false; 
    }); 
});	   
	   
$(function () {
  $('[data-toggle="popover"]').popover()
})


$(function () {
  $('[data-toggle="tooltip"]').tooltip()
})



});