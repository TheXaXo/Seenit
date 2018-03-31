$(function () {
    $('form#submitLinkForm').submit(loadImage);
});

loadImage = function (e) {
    e.preventDefault();
    let self = this;

    function proxify(request) {
        request.url = "http://www.inertie.org/ba-simple-proxy.php?mode=native&url=" + encodeURIComponent(request.url);
        return request;
    }

    let resolver = new ImageResolver({requestPlugin: proxify});

    resolver.register(new ImageResolver.FileExtension());
    resolver.register(new ImageResolver.NineGag());
    resolver.register(new ImageResolver.Instagram());
    resolver.register(new ImageResolver.ImgurPage());
    resolver.register(new ImageResolver.MimeType());
    resolver.register(new ImageResolver.Flickr('6a4f9b6d16c0eaced089c91a2e7e87ad'));
    resolver.register(new ImageResolver.Opengraph());
    resolver.register(new ImageResolver.Webpage());

    let link = $('#linkInput').val();
    let thumbnailUrlInput = $('#thumbnailUrl');

    resolver.resolve(link, function (result) {
        if (result) {
            thumbnailUrlInput.val(result.image);
        }

        self.submit();
    });
};