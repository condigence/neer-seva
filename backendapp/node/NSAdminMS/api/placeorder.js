// var connection = require("../db");
// var app = require("../app");




// ///////////////////////////////////////////////////////////////
// //Save place order
// app.post("/placeorder", (req, res) => {
//     let orders = 
    
// {
// 	"order_id": 12345,
// 	"order_details": {
// 		"Date": "99999999",
// 		"Time": "09:00 AM",
// 		"del_status": "NEW",
// 		"order_status": "PENDING",
// 		"ETA": "NOTSET"
// 	},

// 	"vendor": {
// 		"vendor_id": 12122,
// 		"contact": 2345234432,
// 		"shop_address": "4th Cross, Raj Kumar Shop"
// 	},
// 	"customer": {
// 		"contact": 99999999,
// 		"address": {
// 			"id": 12,
// 			"locality": "HSR Layout"
// 		},
// 		"name": "",
// 		"email": ""
// 	},
// 	"items": [{
// 			"item_id": 123,
// 			"item_code": "BIS5L",
// 			"item_name": "Bisleri 5 Liter",
// 			"item_price": 45,
// 			"item_quantity": 2
// 		},
// 		{
// 			"item_id": 122,
// 			"item_code": "BIS2L",
// 			"item_name": "Bisleri 2 Liter",
// 			"item_price": 20,
// 			"item_quantity": 1
// 		}
// 	]
// }



//     let sql = "INSERT INTO orders SET ?";
//     let query = connection.query(sql, orders, (err, result) => {
//       if (err) throw err;
//       res.send(result);
//     });
//   });


  