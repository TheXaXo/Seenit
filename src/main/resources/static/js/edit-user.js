$(function () {
    let userDetailsDiv = $("#userDetailsDiv");
    let rolesSelect = $("#roles");

    $('button#searchButton').click(function () {
        let username = $('input#searchInput').val();
        $(userDetailsDiv).empty();

        $.get("/user/edit/" + username)
            .done(function (data) {
                let form = $(data).filter("#form");

                $(userDetailsDiv).append(form);
                $(form).submit(onFormSubmit);
            })
            .fail(function () {
                $(userDetailsDiv).append("<h1>User not found</h1>");
            });
    });

    function onFormSubmit(e) {
        e.preventDefault();
        $(rolesSelect).prop('disabled', false);

        $.ajax({
            data: $(this).serialize(),
            type: $(this).attr('method'),
            url: $(this).attr('action'),
            success: function (response) {
                let form = $(response).filter("#form");

                $(form).submit(onFormSubmit);
                $(userDetailsDiv).empty();
                $(userDetailsDiv).append(form);
            }
        });
    }
});