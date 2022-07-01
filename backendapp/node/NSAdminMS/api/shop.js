var connection = require("../db");
var app = require("../app");

////////////////////////API for Shop//////////////////////////////
/////////////////////////////////////////////////////////////////
/**
 * @swagger
 * /neerseva/api/v1/shops:
 *  get:
 *    description: Use to request all stocks
 *    responses:
 *      '200':
 *        description: A successful response
 */

// Get All Shop
app.get("/neerseva/api/v1/shops", (req, res) => {
  let sql = "SELECT * FROM shop";
  connection.query(sql, (err, results) => {
    if (err) throw err;
    res.json(results);
  });
});
///////////////////////////////////////////////////////////////////
// Get shop By shopId
app.get("/neerseva/api/v1/shops/:id", (req, res) => {
  let id = req.params.id;
  let sql = "SELECT * FROM shop WHERE id = " + id;
  connection.query(sql, (err, result) => {
    if (err) throw err;
    if (result.length == 0) {
      res.status(404).json({
        message: "shops not found"
      });
    } else {
      res.json(result);
    }
  });
});
///////////////////////////////////////////////////////////////
//Save Shop
app.post("/neerseva/api/v1/shops", (req, res) => {
  let shops = req.body;
  let sql = "INSERT INTO shop SET ?";
  connection.query(sql, shops, (err, result) => {
    if (err) throw err;
    res.send(result);
  });
});
//////////////////////////////////////////////////////////////////
// update A Shop
app.put("/neerseva/api/v1/shops/:id", (req, res) => {
  connection.query(
    "UPDATE `shop` SET  `name` = ?,  `image_id` =?, `type` =?, `address_id` = ?, `code` = ?, `branch` = ?  where `id` = ?",
    [
      req.body.name,
      req.body.image_id,
      req.body.type,
      req.body.address_id,
      req.body.code,
      req.body.branch,
      req.body.id
    ],
    function(error, results, fields) {
      if (error) throw error;
      res.send(JSON.stringify(results));
    }
  );
});
///////////////////////////////////////////////////////////////////
// Delete a Shop
app.delete("/neerseva/api/v1/shops/:id", (req, res) => {
  let id = req.params.id;
  connection.query("DELETE FROM `shop` WHERE `id`=?", [id], function(
    error,
    results,
    fields
  ) {
    if (error) throw error;
    res.send(results);
  });
});
