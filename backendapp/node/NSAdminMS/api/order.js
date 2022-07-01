var connection = require("../db");
var app = require("../app");

////////////////////////API for Order//////////////////////////////
/////////////////////////////////////////////////////////////////
/**
 * @swagger
 * /neerseva/api/v1/orders:
 *  get:
 *    description: Use to request all orders
 *    responses:
 *      '200':
 *        description: A successful response
 */

// Get All orders

app.get("/neerseva/api/v1/orders", (req, res) => {
  let sql = "SELECT * FROM cust_order";
  connection.query(sql, (err, results) => {
    if (err) throw err;
    res.json(results);
  });
});

///////////////////////////////////////////////////////////////////
// Get order By orderId
app.get("/neerseva/api/v1/orders/:id", (req, res) => {
  let id = req.params.id;
  let sql = "SELECT * FROM cust_order WHERE id = " + id;
  connection.query(sql, (err, result) => {
    if (err) throw err;
    if (result.length == 0) {
      res.json("order id not found");
    } else {
      res.json(result);
    }
  });
});
///////////////////////////////////////////////////////////////////
// Get order By customerId
app.get(
  "/neerseva/api/v1/orders/by/customer/:order_from_cust_id",
  (req, res) => {
    let order_from_cust_id = req.params.order_from_cust_id;
    let sql =
      "SELECT * FROM cust_order WHERE order_from_cust_id = " +
      order_from_cust_id;
    connection.query(sql, (err, result) => {
      if (err) throw err;
      if (result.length == 0) {
        res.json("order id not found");
      } else {
        res.json(result);
      }
    });
  }
);
///////////////////////////////////////////////////////////////
//Save order
app.post("/neerseva/api/v1/orders", (req, res) => {
  let orders = req.body;
  let sql = "INSERT INTO order SET ?";
  connection.query(sql, orders, (err, result) => {
    if (err) throw err;
    res.send(result);
  });
});
//////////////////////////////////////////////////////////////////
// update A Order
app.put("/neerseva/api/v1/orders/:id", (req, res) => {
  connection.query(
    "UPDATE `order` SET  `eta` = ?,  `by_cust_id` =?, `date` =?,  `delivery_status` = ?, `is_cancelled` = ?, `is_paid` =?, `location_code` =?, `number` = ?, `payment_cust_contact` = ?, `payment_date` =?, `payment_method` =?, `payment_time` = ?, `payment_type` = ?, `status` = ?, `time` = ?, `to_vendor_id` = ?, `type` = ?, `payment_txn_id` = ?  where `id` = ?",
    [
      req.body.eta,
      req.body.by_cust_id,
      req.body.date,
      req.body.delivery_status,
      req.body.is_cancelled,
      req.body.is_paid,
      req.body.location_code,
      req.body.number,
      req.body.payment_cust_contact,
      req.body.payment_date,
      req.body.payment_method,
      req.body.payment_time,
      req.body.payment_type,
      req.body.status,
      req.body.time,
      req.body.to_vendor_id,
      req.body.type,
      req.body.payment_txn_id,
      req.body.id,
    ],
    function (error, results, fields) {
      if (error) throw error;
      res.send(JSON.stringify(results));
    }
  );
});
///////////////////////////////////////////////////////////////////
// Delete a Order
app.delete("/neerseva/api/v1/orders/:id", (req, res) => {
  let id = req.params.id;
  connection.query("DELETE FROM `order` WHERE `id`=?", [id], function (
    error,
    results,
    fields
  ) {
    if (error) throw error;
    res.send(results);
  });
});

// Get total number of orders
app.get("/neerseva/api/v1/ordercount", (req, res) => {
  let sql = "SELECT COUNT (*) AS NumberOfOrders  FROM cust_order";
  connection.query(sql, (err, results) => {
    if (err) throw err;
    res.json(results);
  });
});

// Get Orders By Vendor Id
app.get(
  "/neerseva/api/v1/orders/by/vendor/:order_to_vendor_id",
  (req, res) => {
    let order_to_vendor_id = req.params.order_to_vendor_id;
    let sql =
      "SELECT * FROM cust_order WHERE order_to_vendor_id = " +
      order_to_vendor_id;
    connection.query(sql, (err, result) => {
      if (err) throw err;
      if (result.length == 0) {
        res.json("order id not found");
      } else {
        res.json(result);
      }
    });
  }
);

// Get Total orders count by vendor Id
app.get("/neerseva/api/v1/order/vendor/ordercount/:order_to_vendor_id", (req, res) => {
  //let sql = "SELECT COUNT (*) AS NumberOfOrders  FROM cust_order";
  let order_to_vendor_id = req.params.order_to_vendor_id;
    let sql =
      "SELECT COUNT (*) AS NumberOfOrders  FROM cust_order WHERE order_to_vendor_id = " + order_to_vendor_id
  connection.query(sql, (err, results) => {
    if (err) throw err;
    res.json(results);
  });
});




