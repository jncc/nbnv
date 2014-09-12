<@template.master title="NBN Gateway - Online Record Validator" 
    csss=["//ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"] 
    javascripts=[]>
    <form method="POST" action="${api}/validator" enctype="multipart/form-data">
        File to upload: <input type="file" name="file"><br />
        <input type="submit" value="Upload">
    </form>
</@template.master>