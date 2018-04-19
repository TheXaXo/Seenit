$(function () {
    let sendMessageForm = $("#sendMessageForm");
    let sendMessageDiv = $("#sendMessageDiv");
    let messagesDiv = $("#messagesDiv");
    let messagesScrollDiv = $("#messagesScrollDiv");
    let threadRecipientUsernameInput = $("#threadRecipientUsernameInput");
    let ignoreScrollEventListener;
    let threadId;

    let currentPage = 0;
    let totalPages;

    $(document).on('click', 'div.thread', loadFirstPageMessages);
    $(messagesScrollDiv).scroll(function () {
        if ($(messagesScrollDiv).scrollTop() === 0 && !ignoreScrollEventListener) {
            console.log($(messagesScrollDiv).scrollTop());
            loadMessagesOnScroll();
        }
    });

    function loadFirstPageMessages() {
        ignoreScrollEventListener = true;
        $(messagesDiv).empty();
        currentPage = 0;

        threadId = $(this).find(".idInput").val();
        loadMessagesPage(true, 0);

        let recipientUsername = $(this).find("#recipientUsernameInput").val();
        totalPages = $(this).find("#messagesPagesCountInput").val();

        $(sendMessageDiv).show();
        $(threadRecipientUsernameInput).val(recipientUsername);
        $(sendMessageForm).attr('action', "/u/" + recipientUsername + "/message");
    }

    function loadMessagesOnScroll() {
        let nextPage = currentPage + 1;

        if (nextPage >= totalPages) {
            return;
        }

        currentPage = nextPage;
        loadMessagesPage(false, nextPage);
    }

    function loadMessagesPage(scrollToBottom, pageNumber) {
        $.get("/threads/" + threadId + "/messages" + "?page=" + pageNumber + "&sort=creationDate,desc")
            .done(function (data) {
                let messages = $(data).eq(0).children().get().reverse();
                let lastScrollHeight = $(messagesScrollDiv)[0].scrollHeight;

                $(messagesDiv).prepend(messages);
                $(messagesScrollDiv)[0].scrollTop += $(messagesScrollDiv)[0].scrollHeight - lastScrollHeight;

                if (scrollToBottom) {
                    $(messagesScrollDiv).scrollTop($(messagesScrollDiv)[0].scrollHeight);
                    ignoreScrollEventListener = false;
                }
            })
    }
});