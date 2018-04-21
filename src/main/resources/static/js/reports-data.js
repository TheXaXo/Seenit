let currentPage = 0;

$(document).ready(loadFirstPage);

$(window).scroll(function () {
    if ($(window).scrollTop() + $(window).height() > $(document).height() - 50) {
        loadReportsOnScroll();
    }
});

function loadFirstPage() {
    loadPage(0);
}

function loadReportsOnScroll() {
    let totalPages = parseInt($('#totalPages').val());
    let nextPage = currentPage + 1;

    if (nextPage >= totalPages) {
        return;
    }

    currentPage = nextPage;
    loadPage(nextPage);
}

function loadPage(pageNumber) {
    let tableBody = $('#tableBody');
    let url = "/reports/all" + "?page=" + pageNumber;

    $.get(url, function (data) {
        let reports = $(data);
        $(tableBody).append($(reports));
    });
}