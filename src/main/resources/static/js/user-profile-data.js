let activeTabTotalPages = 0;
let currentPage = 0;
let activeTabContentDivSelector = "";
let query = "";

$(document).ready(() => {
    currentPage = 0;
    activeTabTotalPages = parseInt($("#submittedPostsPages").val());
    activeTabContentDivSelector = "#nav-posts";
    query = "/posts";

    loadFirstPage(activeTabContentDivSelector, query);
});

$(window).scroll(function () {
    if ($(window).scrollTop() + $(window).height() > $(document).height() - 50) {
        loadOnScroll();
    }
});

$(document).on('shown.bs.tab', 'a[data-toggle="tab"]', function (e) {
    let tab = $(e.target);
    let contentId = tab.attr("href");

    switch (contentId) {
        case "#nav-posts":
            activeTabTotalPages = parseInt($("#submittedPostsPages").val());
            activeTabContentDivSelector = "#nav-posts";
            query = "/posts";

            break;
        case "#nav-comments":
            activeTabTotalPages = parseInt($("#submittedCommentsPages").val());
            activeTabContentDivSelector = "#nav-comments";
            query = "/comments";

            break;
        case "#nav-posts-saved":
            activeTabTotalPages = parseInt($("#savedPostsPages").val());
            activeTabContentDivSelector = "#nav-posts-saved";
            query = "/posts/saved";

            break;
        case "#nav-posts-upvoted":
            activeTabTotalPages = parseInt($("#upvotedPostsPages").val());
            activeTabContentDivSelector = "#nav-posts-upvoted";
            query = "/posts/upvoted";

            break;
        case "#nav-posts-downvoted":
            activeTabTotalPages = parseInt($("#downvotedPostsPages").val());
            activeTabContentDivSelector = "#nav-posts-downvoted";
            query = "/posts/downvoted";

            break;
        case "#nav-comments-upvoted":
            activeTabTotalPages = parseInt($("#upvotedCommentsPages").val());
            activeTabContentDivSelector = "#nav-comments-upvoted";
            query = "/comments/upvoted";

            break;
        case "#nav-comments-downvoted":
            activeTabTotalPages = parseInt($("#downvotedCommentsPages").val());
            activeTabContentDivSelector = "#nav-comments-downvoted";
            query = "/comments/downvoted";

            break;
    }

    currentPage = 0;
    loadFirstPage(activeTabContentDivSelector, query);
});

function loadFirstPage(contentDivSelector, query) {
    loadPage(0, contentDivSelector, query);
}

function loadOnScroll() {
    let nextPage = currentPage + 1;

    if (nextPage >= activeTabTotalPages) {
        return;
    }

    currentPage = nextPage;
    loadPage(nextPage, activeTabContentDivSelector, query);
}

function loadPage(pageNumber, contentDivSelector, query) {
    let contentDiv = $(contentDivSelector);
    let url = "/" + (location.pathname).substr(1) + query + location.search + "?page=" + pageNumber;

    if (pageNumber === 0) {
        $(contentDivSelector).empty();
        $(contentDivSelector).append($('<h1>No Data</h1>'));
    }

    $.get(url, function (data) {
        let content = $(data).filter("#content");

        if (content.children().length === 0) {
            return;
        }

        if (pageNumber === 0) {
            $(contentDivSelector).empty();
        }

        $(contentDiv).append(content.children());
    });
}