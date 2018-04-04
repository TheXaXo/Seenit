$(function () {
    $('form#addCommentForm').submit(addComment);
});

addComment = function (e) {
    e.preventDefault();

    let commentTextarea = $('#contentTextarea');
    let commentsDiv = $('#commentsDiv');
    let content = $(commentTextarea).val();
    let postId = $('.idInput').val();
    let commentError = $('#addCommentError');

    $.post("/" + postId + "/comments/add", {content: content})
        .done(function (data) {
            let comment = $(data).filter("#commentData");
            $(commentsDiv).prepend(comment.children());
            $(commentTextarea).val("");
            $(commentError).hide();
        })
        .fail(function () {
            $(commentError).show();
        });
};