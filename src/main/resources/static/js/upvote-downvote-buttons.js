$(function () {
    $(document).on('click', 'div.upvote-button', function () {
        let upvoteButton = $(this);
        let downvoteButton;
        let id;
        let scoreText;
        let elementType;

        if (upvoteButton.hasClass("post")) {
            elementType = "post";
            downvoteButton = $(this).parent().find('.downvote-button');
            scoreText = $(this).parent().find('.score');
            id = $(this).parent().find('input.idInput').val();
        } else {
            elementType = "comment";
            downvoteButton = $(this).parent().parent().find('.downvote-button');
            scoreText = $(this).parent().parent().parent().find('.score');
            id = $(this).parent().parent().find('input.idInput').val();
        }

        if ($(upvoteButton).hasClass("arrow-up-clicked")) {
            upvoteButton.removeClass();
            upvoteButton.addClass("arrow-up upvote-button " + elementType);

            scoreText.text(parseInt(scoreText.text()) - 1);
        } else {
            upvoteButton.removeClass();

            if (downvoteButton.hasClass("arrow-down-clicked")) {
                downvoteButton.removeClass();
                downvoteButton.addClass("arrow-down downvote-button " + elementType);

                scoreText.text(parseInt(scoreText.text()) + 2);
            } else {
                scoreText.text(parseInt(scoreText.text()) + 1);
            }

            upvoteButton.addClass("arrow-up-clicked upvote-button " + elementType);
        }

        $.get("/upvote/" + elementType + "/" + id, function (data) {
        });
    });

    $(document).on('click', 'div.downvote-button', (function () {
        let downvoteButton = $(this);
        let upvoteButton;
        let id;
        let scoreText;
        let elementType;

        if (downvoteButton.hasClass("post")) {
            elementType = "post";
            upvoteButton = $(this).parent().find('.upvote-button');
            id = $(this).parent().find('input.idInput').val();
            scoreText = $(this).parent().find('.score');
        } else {
            elementType = "comment";
            upvoteButton = $(this).parent().parent().find('.upvote-button');
            id = $(this).parent().parent().find('input.idInput').val();
            scoreText = $(this).parent().parent().parent().find('.score');
        }

        if ($(downvoteButton).hasClass("arrow-down-clicked")) {
            downvoteButton.removeClass();
            downvoteButton.addClass("arrow-down downvote-button " + elementType);

            scoreText.text(parseInt(scoreText.text()) + 1);
        } else {
            downvoteButton.removeClass();

            if (upvoteButton.hasClass("arrow-up-clicked")) {
                upvoteButton.removeClass();
                upvoteButton.addClass("arrow-up upvote-button " + elementType);

                scoreText.text(parseInt(scoreText.text()) - 2);
            } else {
                scoreText.text(parseInt(scoreText.text()) - 1);
            }

            downvoteButton.addClass("arrow-down-clicked downvote-button " + elementType);
        }

        $.get("/downvote/" + elementType + "/" + id, function (data) {
        });
    }));
});