var connection = require("../db");
var app = require("../app");

////////////////////////////////////////////////////////////////////
/**
 * @swagger
 * /neerseva/api/v1/stocks:
 *  get:
 *    description: Use to request all stocks
 *    responses:
 *      '200':
 *        description: A successful response
 */

// Get all stocks
app.get("/neerseva/api/v1/stocks", (req, res) => {
  let sql = "SELECT * FROM stock";
  connection.query(sql, (err, results) => {
    if (err) throw err;
    res.json(results);
  });
});

///////////////////////////////////////////////////////////////////
// get stock by stockid
app.get("/neerseva/api/v1/stocks/:id", (req, res) => {
  let id = req.params.id;
  let sql = "SELECT * FROM stock WHERE id = " + id;
  connection.query(sql, (err, result) => {
    if (err) throw err;
    if (result.length == 0) {
      res.status(404).json({
        message: "stock id not found"
      });
    } else {
      res.json(result);
    }
  });
});

////////////////////////////////////////////////////////////////////////
//Create stocks
app.post("/neerseva/api/v1/stocks", (req, res) => {
  let stock = req.body;
  connection.query(
    "INSERT INTO `stock` SET  `quantity` = ?,  `item_id` =?",
    [req.body.quantity, req.body.id],
    (err, result) => {
      if (err) throw err;
      res.send(result);
    }
  );
});

/////////////////////////////////////////////////////////////////////
// update stocks
app.put("/neerseva/api/v1/stocks/:id", (req, res) => {
  let id = req.params.id;
  connection.query(
    "UPDATE `stock` SET  `quantity` = ?,  `item_id` =?, `date_created` =?, `is_available` = ?, `is_deleted` = ?, `created_by_user` =? where `id` = ?",
    [
      req.body.quantity,
      req.body.item_id,
      req.body.date_created,
      req.body.is_available,
      req.body.is_deleted,
      req.body.created_by_user,
      id
    ],
    function(error, results, fields) {
      if (error) throw error;
      res.send(JSON.stringify(results));
    }
  );
});
///////////////////////////////////////////////////////////////////////
// delete stocks
app.delete("/neerseva/api/v1/stocks/:id", (req, res) => {
  let id = req.params.id;
  connection.query("DELETE FROM `stock` WHERE `id`=?", [id], function(
    error,
    results,
    fields
  ) {
    if (error) throw error;
    res.send(results);
  });
});

app.get("/neerseva/api/v1/stockview", (req, res) => {
  let sql =
    "SELECT item.id, item.name, stock.id, stock.quantity FROM item INNER JOIN stock ON item.id = stock.item_id;";
  connection.query(sql, (err, results) => {
    if (err) throw err;
    res.json(results);
  });
});
