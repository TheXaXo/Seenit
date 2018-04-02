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
    let currentPageInput = $('#currentPage');
    let currentPage = parseInt($(currentPageInput).val());
    let nextPage = currentPage + 1;

    if (nextPage >= totalPages) {
        return;
    }

    $(currentPageInput).val(nextPage);
    loadPage(nextPage);
}

function loadPage(pageNumber) {
    let postsDiv = $('#postsDiv');
    let url = "/" + (location.pathname).substr(1) + "/posts" + location.search + "?page=" + pageNumber;

    $.get(url, function (data) {
        let posts = $(data).filter("#allPostsFromData");
        $(postsDiv).append(posts.children());
    });
}