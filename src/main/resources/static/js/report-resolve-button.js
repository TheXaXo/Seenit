$(function () {
    $(document).on('click', 'a#resolveButton', function (e) {
        e.preventDefault();

        let button = $(this);
        let href = $(button).attr('href');

        $.get(href, function () {
            $(button).parent().parent().remove();
        });
    });
});