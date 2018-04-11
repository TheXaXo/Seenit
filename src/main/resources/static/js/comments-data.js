let currentPage = 0;

$(document).ready(loadFirstPage);

$(window).scroll(function () {
    if ($(window).scrollTop() + $(window).height() > $(document).height() - 50) {
        loadCommentsOnScroll();
    }
});

function loadFirstPage() {
    loadPage(0);
}

function loadCommentsOnScroll() {
    let totalPages = parseInt($('#totalPages').val());
    let nextPage = currentPage + 1;

    if (nextPage >= totalPages) {
        return;
    }

    currentPage = nextPage;
    loadPage(nextPage);
}

function loadPage(pageNumber) {
    let commentsDiv = $('#commentsDiv');
    let url = "/" + (location.pathname).substr(1) + "/all" + location.search + "?page=" + pageNumber;

    $.get(url, function (data) {
        let comments = $(data).filter("#content");
        $(commentsDiv).append(comments.children());
    });
}