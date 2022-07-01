var connection = require("../db");
var app = require("../app");

// Get All order Detail
app.get("/neerseva/api/v1/orderdetails", (req, res) => {
  let sql = "SELECT * FROM order_detail";
  connection.query(sql, (err, results) => {
    if (err) throw err;
    res.json(results);
    //   res.end(JSON.stringify(results));
  });
});

// Get order detail By Id
app.get("/neerseva/api/v1/orderdetails/:id", (req, res) => {
  let id = req.params.id;
  let sql = "SELECT * FROM order_detail WHERE id = " + id;
  connection.query(sql, (err, result) => {
    if (err) throw err;
    if (result.length == 0) {
      res.json("order detail id not found");
    } else {
      res.json(result);
    }
  });
});
///////////////////////////////////////////////////////////////
//Save order
app.post("/neerseva/api/v1/orderdetails", (req, res) => {
  let orderdetails = req.body;
  let sql = "INSERT INTO order_detail SET ?";
  connection.query(sql, orderdetails, (err, result) => {
    if (err) throw err;
    res.send(result);
  });
});
//////////////////////////////////////////////////////////////////
// update A Order
app.put("/neerseva/api/v1/orderdetails/:id", (req, res) => {
  connection.query(
    "UPDATE `order_detail` SET  `discount` = ?,  `gst` =?, `is_free` =?, `item_id` = ?, `item_quantity` = ?, `service_charge` = ?, `sub_total` =?, `total_amount` =?  where `id` = ?",
    [
      req.body.discount,
      req.body.gst,
      req.body.is_free,
      req.body.item_id,
      req.body.item_quantity,
      req.body.service_charge,
      req.body.sub_total,
      req.body.total_amount,
      req.body.id
    ],
    function(error, results, fields) {
      if (error) throw error;
      res.send(JSON.stringify(results));
    }
  );
});
///////////////////////////////////////////////////////////////////
// Delete a Order
app.delete("/neerseva/api/v1/orderdetails/:id", (req, res) => {
  let id = req.params.id;
  connection.query("DELETE FROM `order_detail` WHERE `id`=?", [id], function(
    error,
    results,
    fields
  ) {
    if (error) throw error;
    res.send(results);
  });
});
