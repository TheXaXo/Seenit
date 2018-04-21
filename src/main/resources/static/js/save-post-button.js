$(function () {
    $(document).on('click', 'a#savePostButton', function (e) {
        e.preventDefault();

        let savePostButton = $(this);
        let id = $(this).parent().find('input.idInput').val();

        $.get("/save/" + id)
            .done(function () {
                if ($(savePostButton).text() === "save") {
                    $(savePostButton).text("unsave");
                } else {
                    $(savePostButton).text("save");
                }
            });
    });
});