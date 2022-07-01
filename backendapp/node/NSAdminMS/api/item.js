var connection = require("../db");
var app = require("../app");

// API ITEMS

// API ITEMS
///////////////////////////////////////////////////////////
/**
 * @swagger
 * /neerseva/api/v1/items:
 *  get:
 *    description: Use to request all stocks
 *    responses:
 *      '200':
 *        description: A successful response
 */

// get all items
app.get("/neerseva/api/v1/items", (req, res) => {
  let sql = "SELECT * FROM item";
  connection.query(sql, (err, results) => {
    if (err) throw err;
    res.json(results);
  });
});
////////////////////////////////////////////////////////////
// get item by id
app.get("/neerseva/api/v1/items/:id", (req, res) => {
  let id = req.params.id;
  let sql = "SELECT * FROM item WHERE id = " + id;
  connection.query(sql, (err, result) => {
    if (err) throw err;
    if (result.length == 0) {
      res.status(404).json("item id not found");
    } else {
      res.json(result);
    }
  });
});
///////////////////////////////////////////////////////////////
// Get Items By BrandId
app.get("/neerseva/api/v1/items/by/:brand_id", (req, res) => {
  let brand_id = req.params.brand_id;
  let sql = "SELECT * FROM item WHERE brand_id = '" + brand_id + "'";
  connection.query(sql, (err, result) => {
    if (err) throw err;
    if (result.length == 0) {
      res.status(404).json("Brand id not found");
    } else {
      res.json(result);
    }
  });
});
/////////////////////////////////////////////////////////////////
//Create item
app.post("/neerseva/api/v1/items", (req, res) => {

 // console.log("creating item");
  let items = req.body;
  let sql = "INSERT INTO item SET ?";
  connection.query(sql, items, (err, result) => {
    if (err) throw err;
    res.send(result);
  });
});
//////////////////////////////////////////////////////////////////
// update items
app.put("/neerseva/api/v1/items/:id", (req, res) => {
  connection.query(
    "UPDATE `item` SET  `name` = ?,  `code` =?, `price` =?, `mrp` = ?, `disp_price` = ?, `discount` = ?, `type` = ?, `description` = ?,  `capacity` = ?, `brand_id` = ?, `image_id` = ?,  `date_created` = ?, `created_by_user` = ?, `is_deleted` = ?, `tax` = ? where `id` = ?",
    [
      req.body.name,
      req.body.code,
      req.body.price,
      req.body.mrp,
      req.body.disp_price,
      req.body.discount,
      req.body.type,
      req.body.description,
      req.body.capacity,
      req.body.brand_id,
      req.body.image_id,
      req.body.date_created,
      req.body.created_by_user,
      req.body.is_deleted,
      req.body.tax,
      req.body.id
    ],
    function(error, results, fields) {
      if (error) throw error;
      res.send(JSON.stringify(results));
    }
  );
});
///////////////////////////////////////////////////////////////////
// delete items
app.delete("/neerseva/api/v1/items/:id", (req, res) => {
  let id = req.params.id;
  connection.query("DELETE FROM `item` WHERE `id`=?", [id], function(
    error,
    results,
    fields
  ) {
    if (error) throw error;
    res.send(results);
  });
});
