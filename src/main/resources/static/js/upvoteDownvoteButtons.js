$(function () {
    $('div.upvote-button').click(function () {
        let upvoteButton = $(this);
        let downvoteButton = $(this).parent().find('.downvote-button');

        upvoteButton.removeClass();
        downvoteButton.removeClass();

        upvoteButton.addClass("arrow-up-clicked upvote-button");
        downvoteButton.addClass("arrow-down downvote-button");
    });

    $('div.downvote-button').click(function () {
        let downvoteButton = $(this);
        let upvoteButton = $(this).parent().find('.upvote-button');

        upvoteButton.removeClass();
        downvoteButton.removeClass();

        upvoteButton.addClass("arrow-up upvote-button");
        downvoteButton.addClass("arrow-down-clicked downvote-button");
    });
});