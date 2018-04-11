let currentPage = 0;

$(document).ready(loadFirstPage);

$(window).scroll(function () {
    if ($(window).scrollTop() + $(window).height() > $(document).height() - 50) {
        loadPostsOnScroll();
    }
});

function loadFirstPage() {
    loadPage(0);
}

function loadPostsOnScroll() {
    let totalPages = parseInt($('#totalPages').val());
    let nextPage = currentPage + 1;

    if (nextPage >= totalPages) {
        return;
    }

    currentPage = nextPage;
    loadPage(nextPage);
}

function loadPage(pageNumber) {
    let postsDiv = $('#postsDiv');
    let url = "/" + (location.pathname).substr(1) + "/posts" + location.search + "?page=" + pageNumber;

    $.get(url, function (data) {
        let posts = $(data).filter("#content");
        $(postsDiv).append(posts.children());
    });
}