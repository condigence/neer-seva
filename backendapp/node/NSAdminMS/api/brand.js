var connection = require("../db");
var app = require("../app");

//// All your Brand API here e.i GET, POST, PUT, DELETE

// API FOR Brand
/////////////////////////////////////////////////////////////////

/**
 * @swagger
 * /neerseva/api/v1/brands:
 *  get:
 *    description: Use to request all brands
 *    responses:
 *      '200':
 *        description: A successful response
 */
/////////////////////////////////////////////////////////////////
// Get All Brand
app.get("/neerseva/api/v1/brands", (req, res) => {
  let sql = "SELECT * FROM brand";
  connection.query(sql, (err, results) => {
    if (err) throw err;
    res.json(results);
  });
});
/////////////////////////////////////////////////////////////////
// Get brand by brandId
app.get("/neerseva/api/v1/brands/:brandId", (req, res) => {
  let id = (brandId = req.params.brandId);
  let sql = "SELECT * FROM brand WHERE id = " + id;
  connection.query(sql, (err, result) => {
    if (err) throw err;
    if (result.length == 0) {
      res.status(404).json({
        message: "brand id not found"
      });
    } else {
      res.json(result);
    }
  });
});
//////////////////////////////////////////////////////////////////////
// Get brand by brand name
app.get("/neerseva/api/v1/brands/by/:brandName", (req, res) => {
  let name = (brandName = req.params.brandName);
  let sql = "SELECT * FROM brand WHERE name = '" + name + "'";
  connection.query(sql, (err, result) => {
    if (err) throw err;
    if (result.length == 0) {
      res.status(404).json({
        message: "brand Name not found"
      });
    } else {
      res.json(result);
    }
  });
});
/////////////////////////////////////////////////////////////////
// Get brand by category name
app.get(
  "/neerseva/api/v1/brands/by/category/:brandCategoryName",
  (req, res) => {
    let category_name = (brandCategoryName = req.params.brandCategoryName);
    let sql =
      "SELECT * FROM brand WHERE category_name = '" + category_name + "'";
    connection.query(sql, (err, result) => {
      if (err) throw err;
      if (result.length == 0) {
        res.status(404).json({
          message: "brand Category not found"
        });
      } else {
        res.json(result);
      }
    });
  }
);
//////////////////////////////////////////////////////////////////
// Get brand by subCategory name
app.get(
  "/neerseva/api/v1/brands/subCategories/:subCategoryName",
  (req, res) => {
    let subcategory_name = (subCategoryName = req.params.subCategoryName);
    let sql =
      "SELECT * FROM brand WHERE subcategory_name = '" + subcategory_name + "'";
    connection.query(sql, (err, result) => {
      if (err) throw err;
      if (result.length == 0) {
        res.status(404).json({
          message: "brand subCategory not found"
        });
      } else {
        res.json(result);
      }
    });
  }
);
///////////////////////////////////////////////////////////////////////////////

// Get Brand item by brand id and itemId
app.get("/neerseva/api/v1/brands/:brandId/items/:itemId", (req, res) => {
  let id = (brandId = req.params.brandId);
  let item_id = (itemId = req.params.itemId);
  let sql =
    "SELECT * FROM item WHERE item_id = '" +
    item_id +
    "' and id = '" +
    id +
    "' ";
  connection.query(sql, (err, result) => {
    if (err) throw err;
    if (result.length == 0) {
      res.status(404).json({
        message: "item not found"
      });
    } else {
      res.json(result);
    }
  });
});

///////////////////////////////////////////////////////////////////

//Get All Brand items by brand Id, CategoryName and SubCategoryName
app.get(
  "/neerseva/api/v1/brands/:brandId/categories/:brandCategoryName/subCategories/:brandSubCategoryName",
  (req, res) => {
    let id = (brandId = req.params.brandId);
    let category_name = (brandCategoryName = req.params.brandCategoryName);
    let subcategory_name = (brandSubCategoryName =
      req.params.brandSubCategoryName);
    let sql =
      "SELECT * FROM brand WHERE id = '" +
      id +
      "' and category_name = '" +
      category_name +
      "' and subcategory_name = '" +
      subcategory_name +
      "' ";
    connection.query(sql, (err, result) => {
      if (err) throw err;
      if (result.length == 0) {
        res.status(404).json({
          message: "item not found"
        });
      } else {
        res.json(result);
      }
    });
  }
);

/////////////////////////////////////////////////////////////////////////
/**
 * @swagger
 * /Brands:
 *    post:
 *      description: Use to create new brand
 *    parameters:
 *      - name: brand
 *        in: query
 *        description: Name of our brand
 *        required: false
 *        schema:
 *          type: string
 *          format: string
 *    responses:
 *      '201':
 *        description: Successfully created brand
 */

// /**
//  * @swagger
//  *  /neerseva/api/v1/brands:
//  *   post:
//  *  description: Add a new brand to the collection
//  *  operationId: addBrand
//  *  parameters:
//  * - name: Brand
//  *  in: body
//  * description: The Brand JSON you want to post
//  * schema:
//  *   $ref: '#/neerseva/api/v1/brands'
//  * responses:
//  * '201':
//  *   description: Brand created successfully
//  *   schema:
//  *     $ref: '#/neerseva/api/v1/brands'
//  */
// create a brand
app.post("/neerseva/api/v1/brands", (req, res) => {

  console.log("Inside create brand!");
  let brand = req.body;
  
  let sql = "INSERT INTO brand SET ?";
  connection.query(sql, brand, (err, result) => {
    if (err) throw err;
    res.send(result);
  });
});
////////////////////////////////////////////////////////////////////////

// update Brand
app.put("/neerseva/api/v1/brands/:brandId", (req, res) => {
  connection.query(
    "UPDATE `brand` SET  `name` = ?,  `image_id` =?,  `date_created` = ?  where `id` = ?",
    [
      req.body.name,
      req.body.imageId,
      req.body.date_created,
      req.body.id
    ],
    function(error, results, fields) {
      if (error) throw error;
      res.send(JSON.stringify(results));
    }
  );
});
//////////////////////////////////////////////////////////////////////
// Delete Brand
app.delete("/neerseva/api/v1/brands/:brandId", (req, res) => {
  let id = (brandId = req.params.brandId);
  connection.query("DELETE FROM `brand` WHERE `id`=?", [id], function(
    error,
    results,
    fields
  ) {
    if (error) throw error;
    res.send(results);
  });
});
