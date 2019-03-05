$(function () {
    let currentSubscribedSubseenitsPage = 0;
    let loadMoreSubscribedSubseenitsBtn = $('#loadMoreSubscribedSubseenitsBtn');

    loadFirstSubscribedSubseenitsPage();

    function loadFirstSubscribedSubseenitsPage() {
        loadSubscribedSubseenitsPage(0);
    }

    $(loadMoreSubscribedSubseenitsBtn).click(function () {
        loadSubscribedSubseenitsPage(currentSubscribedSubseenitsPage);
    });

    function loadSubscribedSubseenitsPage(pageNumber) {
        let subscribedSubseenitsDiv = $('#subscribedSubseenitsDiv');
        let url = "/subseenits/subscribed" + "?page=" + pageNumber;

        $.get(url, function (data) {
            let subseenits = $(data).filter('#content');
            let totalSubscribedPagesCount = Number($(data).filter('#totalSubscribedPagesCount').val());

            if (totalSubscribedPagesCount > pageNumber + 1) {
                $('#loadMoreSubscribedSubseenits').show();
            } else {
                $('#loadMoreSubscribedSubseenits').hide();
            }

            if (subseenits.children().length === 0) {
                return;
            }

            if (pageNumber === 0) {
                $(subscribedSubseenitsDiv).empty();
            }

            $(subscribedSubseenitsDiv).append(subseenits.children());
        });

        currentSubscribedSubseenitsPage++;
    }
});