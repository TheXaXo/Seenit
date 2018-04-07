$(document).ready(loadSubscribedSubseenits);

function loadSubscribedSubseenits() {
    let subscribedSubseenitsDiv = $('#subscribedSubseenitsDiv');
    let url = "/subseenits/subscribed";

    $.get(url, function (data) {
        let subseenits = $(data).filter("#subscribedSubseenits");
        $(subscribedSubseenitsDiv).replaceWith(subseenits.children());
    });
}