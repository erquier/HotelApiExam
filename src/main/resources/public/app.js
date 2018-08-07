function crearCliente() {
    $.post("http://localhost:4567/crear",
            {
                nombre: $('#nombre').val(),
                email: $('#email').val(),
                password: $('#password').val(),
                telefono: $('#telefono').val(),
                direccion: $('#direccion').val(),
                estado: $('#estado').val()
            })
            .done(function (data) {
                alert("Dato insertado: " + data);
            }, "json");
}

function verCliente() {
    $.get("http://localhost:4567/ver/1", function (data) {

        $("#nombre").html(data.nombre);
    }, "json");

}

function reservar() {
    
    $.post("http://localhost:4567/reserva",
            {
                idcliente: $("#IDcliente").val(),
                fechallegada: $("#fechallegada").val(),
                fechasalida: $("#fechasalida").val(),
                costo: $("#costo").val()
            })
            .done(function (data) {
                alert("Dato insertado: " + data);
            }, "json");
}


//cargar foto
function load() {
    var elBrowse = document.getElementById("browse"),
            elPreview = document.getElementById("preview");



    function readImage(file) {

        // Create a new FileReader instance
        // https://developer.mozilla.org/en/docs/Web/API/FileReader
        var reader = new FileReader();

        // Once a file is successfully readed:
        reader.addEventListener("load", function () {


            var image = new Image();
            image.addEventListener("load", function () {


                var imageInfo = file.name + ' ' +
                        image.width + 'x' +
                        image.height + ' ' +
                        file.type + ' ' +
                        Math.round(file.size / 1024) + 'KB';

                // Finally append our created image and the HTML info string to our `#preview` 
                elPreview.appendChild(this);
                alert(imageInfo);



            });

            image.src = reader.result;

        });

        // https://developer.mozilla.org/en-US/docs/Web/API/FileReader/readAsDataURL
        reader.readAsDataURL(file);
    }


//Evento de elegir archivo
    elBrowse.addEventListener("change", function () {


        var files = this.files;
        // Let's create an empty `errors` String to collect eventual errors into:
        var errors = "";

        if (!files) {
            errors += "Carga de archivo no soportada.";
        }


        if (files && files[0]) {



            var file = files[0];


            if ((/\.(png|jpeg|jpg|gif)$/i).test(file.name)) {

                readImage(file);
            } else {
                errors += file.name + " No es una imagen \n";
            }
        }



        if (errors) {
            alert(errors);
        }

    });
}