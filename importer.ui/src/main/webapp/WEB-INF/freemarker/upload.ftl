<html>
    <head>
        <META http-equiv="Content-Type" content="text/html;charset=UTF-8">
        <title>Upload Dataset</title>
        <script type="text/javascript" src="/importer/jquery.js"></script>
    </head>
    <body>
        <form method="post" enctype="multipart/form-data" action="upload.html">
            <fieldset>
                <legend>Upload Data</legend>
                <p>
                    <label for="fileData" path="fileData">File</label><br/>
                    <input path="fileData" type="file" id="fileData" name="fileData"/>
                </p>
                <p>
                    <input id="uploadDataset" type="submit" />
                    <img id="uploadSpinner" src="/importer/images/1-1.gif" />
                </p>
            </fieldset>
        </form>

        <script>
            $(document).ready(function() {
                $("#uploadSpinner").hide();
            });

            $("#uploadDataset").click(function() {
                $("uploadSpinner").show();
            });
        </script>
    </body>
</html>