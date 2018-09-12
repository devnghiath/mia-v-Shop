Dropzone.autoDiscover = false;
function loadPopup(url) {
    var $modal = $('#popup');
    $modal.modal('show');

    $.ajax({
        url: url,
        success: function (result) {
            $("#popup").html(result);
        }

    })

}

function selectItem(name, value, type) {
    console.log(name);
    if (type == 0) {
        $("#categoryName").val(name);
        $("#categoryId").val(value);
    } else if (type == 1) {
        $("#brandName").val(name);
        $("#brandId").val(value);

    } else {
        $("#manufacturersName").val(name);
        $("#manufacturersId").val(value);
    }
}

$(".dropzone").dropzone({
    url: "/admin/uploadImageProduct",
    success: function(file, responseText){

        var myJson =JSON.stringify(responseText); //jQuery.parseJSON(responseText)
        console.log(myJson);
        var dataJson = JSON.parse(myJson);
        // alert( dataJson.fileName);
        // $('<input>').attr({
        //     type: 'hidden',
        //     name: 'fileName',
        //     value: dataJson.fileName
        // }).appendTo("#imageList");
        $("#imageList").append(responseText)
        // $('.dz-hidden-input').remove();
        // $("#imageList").append('<input type="hidden" name="myfieldname" value= dataJson.fileName" />');
    }
});
