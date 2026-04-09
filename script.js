// Highlight the active nav link on scroll
(function () {
  "use strict";

  var sections = document.querySelectorAll("main section[id]");
  var navLinks = document.querySelectorAll("nav a");

  function updateActiveLink() {
    var scrollY = window.scrollY || window.pageYOffset;

    sections.forEach(function (section) {
      var top = section.offsetTop - 120;
      var bottom = top + section.offsetHeight;
      var id = section.getAttribute("id");

      navLinks.forEach(function (link) {
        if (link.getAttribute("href") === "#" + id) {
          if (scrollY >= top && scrollY < bottom) {
            link.classList.add("active");
          } else {
            link.classList.remove("active");
          }
        }
      });
    });
  }

  window.addEventListener("scroll", updateActiveLink);
  updateActiveLink();
})();
