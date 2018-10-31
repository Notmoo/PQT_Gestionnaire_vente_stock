var MessageType = {
    ERROR_QUERY: "ERROR_QUERY",
    REFUSED_QUERY: "REFUSED_QUERY",

    QUERY_SIMPLIFIED_PRODUCT_LIST: "QUERY_SIMPLIFIED_PRODUCT_LIST",
    ACK_SIMPLIFIED_PRODUCT_LIST: "ACK_SIMPLIFIED_PRODUCT_LIST",

    QUERY_ACCOUNT_LIST: "QUERY_ACCOUNT_LIST",
    MSG_ACCOUNT_LIST: "MSG_ACCOUNT_LIST",

    QUERY_CONNECT_ACCOUNT: "QUERY_CONNECT_ACCOUNT",
    ACK_CONNECT_ACCOUNT: "ACK_CONNECT_ACCOUNT",

    QUERY_SERVING_LIST: "QUERY_SERVING_LIST",
    ACK_SERVING_LIST: "ACK_SERVING_LIST",

    QUERY_SERVING_DONE: "QUERY_SERVING_DONE",
    ACK_SERVING_DONE: "ACK_SERVING_DONE",

    QUERY_SERVING_VERSION: "QUERY_SERVING_VERSION",
    ACK_SERVING_VERSION: "ACK_SERVING_VERSION"
};

var permissionLevel = null;
var products = {};
var version = 0;
var autoUpdate = false;
var sales = [];

function getUser(){
    return $('#server-login').val();
}

function getPassword() {
    return $('#server-password').val();
}

function login() {
    var user = getUser();
    var password = getPassword();
    console.log("connecting user " + user);
    $.get(messageFactory(MessageType.QUERY_ACCOUNT_LIST, {}), function(data, status) {
        // First get the permission level of the user
        if (status === "success" && data['type'] === MessageType.MSG_ACCOUNT_LIST) {
            var userList = JSON.parse(data['fields']['accounts']);
            userList.forEach(function (value) {
                if (value['username'] === user)
                    permissionLevel = value['permissionLevel'];
            });
            // Then finally login
            if (permissionLevel !== "LOWEST") {
                logout(true);
                $.get(messageFactory(MessageType.QUERY_CONNECT_ACCOUNT, {
                    "desired_state": "true",
                    "account": JSON.stringify({
                        "username": user,
                        "password": password,
                        "permissionLevel": permissionLevel
                    })
                }), function (data, status) {
                    if (status === "success") {
                        if (data['type'] === MessageType.REFUSED_QUERY) {
                            console.log("Wrong login or password");
                            sendError(decodeURIComponent(data['fields']['Detail_refus']));
                        } else {
                            updateProducts();
                            // Auto update
                            if (!autoUpdate) {
                                autoUpdate = true;
                                setInterval(function () {
                                    updateSales();
                                }, 500);
                            }
                            $('#server-login').hide();
                            $('#server-password').hide();
                            $('#login-button').hide();
                            $('#logout-button').show();
                            $('#logged-user').show().empty().append(user);
                            $('#note-board').show();
                            Cookies.remove('pqtServerUsername');
                            Cookies.set('pqtServerUsername', user, {"expires": 30});
                        }
                    }

                }, "json").fail(function () {
                    console.log("Failed to login");
                });
            } else {
                console.log("Permission too low");
                sendError("Votre compte n'est pas autorisé à accéder à cette application");
            }
        }
    }, "json").fail(function () {
        console.log("Could not connect to the server");
    });
}

function writeNote(container, sale) {
    var noteContent = '';
    Object.keys(sale['products']).forEach(function (key) {
        noteContent += '<li><input id="order-' + sale['id'] + '-product-' + key +'" type="checkbox" onclick="completePartSale('+ sale['id'] + ', \'' + key.toString() + '\')"';
        if (sale['serving'][key]) {
            noteContent += ' checked ><del>';
        } else noteContent += '>';
        noteContent += products[key] + ' x ' + sale['products'][key];
        if (sale['serving'][key])
            noteContent += '</del>';
        noteContent += '</input></li>';
});
container.html(container.html() +
    '<li class="note">' +
    '<div class="note-content">' +

    '<button onclick="completeWholeSale(' + sale['id'] + ')" class="delete"><i class="material-icons">done</i></button>' +
    '<h2>Commande #' +  sale['id'] + '</h2>' +
    '<form id="order-' + sale['id'] + '">' +
    '<ul>' +
    noteContent +
    '</ul>' +
    '</form>' +
    '</div>' +
    '</li>');
}

function findSaleById(id) {
    var sale = null;
    sales.forEach(function (s) {
        if (s['id'] === id)
            sale = s;
    });
    return sale;
}

function completeWholeSale(id) {
    var sale = findSaleById(id);
    if (sale != null) {
        Object.keys(sale['serving']).forEach(function (key) {
            sale['serving'][key] = true;
        });
        completeSale(id);
    }
}

function completePartSale(id, productId) {
    var sale = findSaleById(id);
    console.log(id);
    console.log(productId);
    if (sale != null){
        sale['serving'][productId] = $('#order-' + id + '-product-' + productId).is(':checked');
        completeSale(id);
    }
}

function completeSale(id) {
    var sale = findSaleById(id);
    if (sale != null){
        $.get(messageFactory(MessageType.QUERY_SERVING_DONE, {"sale": JSON.stringify({
                "id": sale['id'], "products": sale['serving']
            })}), function (data, status) {
            /* we do nothing */
        }, "json").fail(function () {
            sendError("Problème de communication avec le serveur");
        });
    }
}

function updateProducts() {
    $.get(messageFactory(MessageType.QUERY_SIMPLIFIED_PRODUCT_LIST, {}), function (data, status) {
        if (status === "success" && data['type'] === MessageType.ACK_SIMPLIFIED_PRODUCT_LIST){
            var stock = JSON.parse(data['fields']['list']);
            stock.forEach(function (value) {
                products[value['id']] = value['name'];
            });
        } else {
            console.log("Error updating product list");
        }
    }, "json").fail(function () {
        sendError("Problème de communication avec le serveur");
    });
}

function updateSales() {
    $.get(messageFactory(MessageType.QUERY_SERVING_VERSION, {}), function (data, status) {
        if (status === "success" && data['type'] === MessageType.ACK_SERVING_VERSION && Number(data['fields']['version']) > version){
            version = Number(data['fields']['version']);
            $.get(messageFactory(MessageType.QUERY_SERVING_LIST, {}), function (data, status) {
                if (status === "success" && data['type'] === MessageType.ACK_SERVING_LIST){
                    var saleList = JSON.parse(data['fields']['sales']);
                    var displayBoard = $('#note-list');

                    // Keep up to date the list of sales
                    saleList.forEach(function (value) { sales[sales.length] = value;});

                    displayBoard.empty();
                    saleList.forEach(function (value) {
                        writeNote(displayBoard, value);
                    });
                }

            }, "json").fail(function () {
                sendError("Problème de communication avec le serveur");
            });
        }
    }, "json").fail(function () {
        sendError("Problème de communication avec le serveur");
    });
}

function logout(silent) {
    var user = getUser();
    var password = getPassword();
    $.get(messageFactory(MessageType.QUERY_CONNECT_ACCOUNT, {
        "desired_state": "false",
        "account": JSON.stringify({"username": user, "password": password, "permissionLevel": permissionLevel})
    }), function (data, status) {

        if (status === "success") {
            if (data['type'] === MessageType.REFUSED_QUERY) {
                console.log(data['fields']['Detail_refus']);
                if (!silent)
                    sendError(data['fields']['Detail_refus']);
            }
            else {
                permissionLevel = null;
                $('#server-login').show();
                $('#server-password').show();
                $('#login-button').show();
                $('#logout-button').hide();
                $('#logged-user').hide();
                $('#note-board').hide();
            }
        }

    }, "json").fail(function () {
        sendError("Problème de communication avec le serveur");
    });
}

function sendError(message) {
    $('#error-box').show().find('span:first-child').empty().append(message);
}

function messageFactory(messageType, fieldMap) {
    function encodeToUrl(message) {
        return "/pqt-server/?format=UTF-8&message=" + encodeURIComponent(message);
    }
    var request = {
        "fields": fieldMap,
        "type": messageType,
        "user": {"username": getUser(), "permissionLevel": permissionLevel}
    };
    console.log(JSON.stringify(request));
    return encodeToUrl(JSON.stringify(request));
}
