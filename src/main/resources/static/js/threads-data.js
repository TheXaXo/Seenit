$(document).ready(loadThreads);

function loadThreads() {
    let threadsDiv = $('#threadsDiv');
    let url = "/threads/all";

    $.get(url, function (data) {
        let posts = $(data).filter("#content");
        $(threadsDiv).append(posts.children());
    });
}