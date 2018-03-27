$(function () {
    $('button#searchButton').click(function () {
        window.location.href = '/s/' + $('input#searchInput').val();
    });
});
