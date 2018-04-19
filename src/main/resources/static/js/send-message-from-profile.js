$(function () {
    let newMessageForm = $("form#newMessageForm");
    let formAction = $(newMessageForm).attr('action');
    let messageTextarea = $('#messageTextarea');
    let messageError = $('#messageError');

    $(newMessageForm).submit(newMessage);

    function newMessage(e) {
        e.preventDefault();

        let content = $(messageTextarea).val();

        $.post(formAction, {content: content})
            .done(function () {
                $(messageError).hide();
                window.location.href = "/messages";
            })
            .fail(function () {
                $(messageTextarea).text(content);
                $(messageError).show();
            });
    }
});