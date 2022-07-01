var connection = require('./db');
var app = require('./app');

//////////////////////////////////////////

//// Register All your API here

///////////////////////////////////////////

//// COMMON/////
var home = require('./api/home');
var login = require('./api/login');
var placeorder = require('./api/placeorder');
var profiles = require('./api/profile');
var addresses = require('./api/address');
var orders = require('./api/order');
var orderdetails = require('./api/order-detail');

//// ADMIN/////
var users = require('./api/user');
var brands = require('./api/brand');
var items = require('./api/item');

//// VENDOR/////
var stocks = require('./api/stock');
var shops = require('./api/shop');

//// CUSTOMER/////
