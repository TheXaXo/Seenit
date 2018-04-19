$(function () {
    let sendMessageForm = $('form#sendMessageForm');
    let recipientUsernameInput = $("#threadRecipientUsernameInput");

    $(sendMessageForm).submit(sendMessage);

    function sendMessage(e) {
        e.preventDefault();

        let messageTextarea = $('#messageTextarea');
        let messagesDiv = $(document).find("#messagesDiv");
        let messagesScrollDiv = $("#messagesScrollDiv");
        let messageError = $("#messageError");
        let content = $(messageTextarea).val();
        let recipientUsername = $(recipientUsernameInput).val();

        $.post("/u/" + recipientUsername + "/message", {content: content})
            .done(function (data) {
                $(messageError).hide();

                let message = $(data);
                $(messagesDiv).append(message);
                $(messageTextarea).val("");

                $(messagesScrollDiv).scrollTop($(messagesScrollDiv)[0].scrollHeight);
            })
            .fail(function () {
                $(messageError).show();
                $(messageTextarea).text(content);
            })
    }
});