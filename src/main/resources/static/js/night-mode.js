$(document).ready(function () {
    let nightModeBtn = $("#nightModeBtn");
    $(nightModeBtn).on("click", toggleNightMode);

    if (localStorage.getItem("isNightMode") === null) {
        localStorage.setItem("isNightMode", "false");
    } else if (localStorage.getItem("isNightMode") === "true") {
        $(nightModeBtn).removeClass("fa-moon").addClass("fa-sun");
        $("body").addClass("night-mode");
    }

    function toggleNightMode() {
        if (localStorage.getItem("isNightMode") === "false") {
            localStorage.setItem("isNightMode", "true");

            $(nightModeBtn).removeClass("fa-moon").addClass("fa-sun");
            $("body").addClass("night-mode");
        } else {
            localStorage.setItem("isNightMode", "false");

            $(nightModeBtn).removeClass("fa-sun").addClass("fa-moon");
            $("body").removeClass("night-mode");
        }
    }
});