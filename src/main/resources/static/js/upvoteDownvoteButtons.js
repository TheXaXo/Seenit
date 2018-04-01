$(function () {
    $('div.upvote-button').click(function () {
        let upvoteButton = $(this);
        let downvoteButton = $(this).parent().find('.downvote-button');
        let postScoreText = $(this).parent().find('div.text-center').find('span.text-muted');
        let postId = $(this).parent().find('input.postIdInput').val();

        if ($(upvoteButton).hasClass("arrow-up-clicked")) {
            upvoteButton.removeClass();
            upvoteButton.addClass("arrow-up upvote-button");

            postScoreText.text(parseInt(postScoreText.text()) - 1);
        } else {
            upvoteButton.removeClass();

            if (downvoteButton.hasClass("arrow-down-clicked")) {
                downvoteButton.removeClass();
                downvoteButton.addClass("arrow-down downvote-button");

                postScoreText.text(parseInt(postScoreText.text()) + 2);
            } else {
                postScoreText.text(parseInt(postScoreText.text()) + 1);
            }

            upvoteButton.addClass("arrow-up-clicked upvote-button");
        }

        $.get("/upvote/" + postId, function (data) {
            console.log('asd');
        });
    });

    $('div.downvote-button').click(function () {
        let downvoteButton = $(this);
        let upvoteButton = $(this).parent().find('.upvote-button');
        let postScoreText = $(this).parent().find('div.text-center').find('span.text-muted');
        let postId = $(this).parent().find('input.postIdInput').val();

        if ($(downvoteButton).hasClass("arrow-down-clicked")) {
            downvoteButton.removeClass();
            downvoteButton.addClass("arrow-down downvote-button");

            postScoreText.text(parseInt(postScoreText.text()) + 1);
        } else {
            downvoteButton.removeClass();

            if (upvoteButton.hasClass("arrow-up-clicked")) {
                upvoteButton.removeClass();
                upvoteButton.addClass("arrow-up upvote-button");

                postScoreText.text(parseInt(postScoreText.text()) - 2);
            } else {
                postScoreText.text(parseInt(postScoreText.text()) - 1);
            }

            downvoteButton.addClass("arrow-down-clicked downvote-button");
        }

        $.get("/downvote/" + postId, function (data) {
            console.log('asd');
        });
    });
});