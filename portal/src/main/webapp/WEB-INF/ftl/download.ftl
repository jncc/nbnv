<script>
    nbn.nbnv.api = '${api}';
    nbn.nbnv.userID = ${user.id?c};

    $(function() {
        var json = ${data};
        nbn.nbnv.ui.download(json, $('#filter'));
    });
</script>

<div id="filter"></div>