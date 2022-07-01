var connection = require("../db");
var app = require("../app");

//// API for Address GET, POST, PUT, DELETE
/**
 * @swagger
 * /neerseva/api/v1/addresses:
 *  get:
 *    description: Use to request all stocks
 *    responses:
 *      '200':
 *        description: A successful response
 */

app.get("/neerseva/api/v1/addresses", (req, res) => {
  let sql = "SELECT * FROM address";
  connection.query(sql, (err, results) => {
    if (err) throw err;
    res.json(results);
  });
});

/////////////////////////////////////////////////////////////////
//create a address
app.post("/neerseva/api/v1/addresses", (req, res) => {
  let address = req.body;
  let sql = "INSERT INTO address SET ?";
  connection.query(sql, address, (err, result) => {
    if (err) throw err;
    res.send(result);
  });
});
////////////////////////////////////////////////////////////////////
// Get Address by userId

app.get("/neerseva/api/v1/addresses/by/user/:user_id", (req, res) => {
  let userId =  req.params.user_id;

// let user_id = req.params.userId;
//console.log("userID : "+userId);

  let sql = "select * from address where user_id = " + userId;
  connection.query(sql, (err, result) => {
    if (err) throw err;
    if (result.length == 0) {
      res.status(404).json({
        message: "userId not found"
      });
    } else {
      res.json(result);
    }
  });
});

////////////////////////////////////////////////////////////
// get address by Address Id
app.get("/neerseva/api/v1/addresses/by/:id", (req, res) => {
  let id = req.params.id;
  let sql = "SELECT * FROM address WHERE id = " + id;
  connection.query(sql, (err, result) => {
    if (err) throw err;
    if (result.length == 0) {
      res.status(404).json({
        message: "address Id not found"
      });
    } else {
      res.json(result);
    }
  });
});

////////////////////////////////////////////////////////////////////////
// update user address
app.put("/neerseva/api/v1/addresses/by/:addressId", (req, res) => {
  connection.query(
    "UPDATE `address` SET  `type` = ?, `line1` = ?, `line2` = ?, `line3` = ?, `line4` = ?,  `pin` = ?, `city` = ?, `state` = ?, `country` = ?, `location_long` = ?, `location_latt` = ?, `location_name` = ?, `user_id` = ?, `is_default` =?  where `id` = ?",
    [
      req.body.type,
      req.body.line1,
      req.body.line2,
      req.body.line3,
      req.body.line4,
      req.body.pin,
      req.body.city,
      req.body.state,
      req.body.country,
      req.body.location_long,
      req.body.location_latt,
      req.body.location_name,
      req.body.user_id,
      req.body.is_default,
      req.body.id
    ],
    function(error, results, fields) {
      if (error) throw error;
      res.send(JSON.stringify(results));
    }
  );
});

////////////////////////////////////////////////////////////////////
// update user address by Address Id
app.put("/neerseva/api/v1/addresses/by/:userId", (req, res) => {
  connection.query(
    "UPDATE `address` SET  `type` = ?, `line1` = ?, `line2` = ?, `line3` = ?, `line4` = ?,  `pin` = ?, `city` = ?, `state` = ?, `country` = ?, `location_long` = ?, `location_latt` = ?, `location_name` = ?, `id` = ?, `is_default`= ?  where `user_id` = ?",
    [
      req.body.type,
      req.body.line1,
      req.body.line2,
      req.body.line3,
      req.body.line4,
      req.body.pin,
      req.body.city,
      req.body.state,
      req.body.country,
      req.body.location_long,
      req.body.location_latt,
      req.body.location_name,
      req.body.user_id,
      req.body.is_default
    ],
    function(error, results, fields) {
      if (error) throw error;
      res.send(JSON.stringify(results));
    }
  );
});

//////////////////////////////////////////////////////////////////////
// delete user address By userId
app.delete("/neerseva/api/v1/addresses/:userId", (req, res) => {
  let user_id = (userId = req.params.userId);
  connection.query(
    "DELETE FROM `address` WHERE `user_id`=?",
    [user_id],
    function(error, results, fields) {
      if (error) throw error;
      res.send(results);
    }
  );
});

//////////////////////////////////////////////////////////////////////
// delete user address By Address Id
app.delete("/neerseva/api/v1/addresses/by/:id", (req, res) => {
  let id = req.params.id;
  connection.query("DELETE FROM `address` WHERE `id`=?", [id], function(
    error,
    results,
    fields
  ) {
    if (error) throw error;
    res.send(results);
  });
});

// All address API for User
// GET, POST, PUT, DELETE

///////////////////////////////////////////////////////
// Get address by venodr Id

// app.get('/neerseva/api/v1/addresses/by/vendor/:venodrId', (req, res) => {
//   let vendor_id = venodrId = req.params.venodrId;
//   let sql = 'SELECT * FROM shop WHERE vendor_id = ' + vendor_id;
//   connection.query(sql, (err, result) => {
//     if (err) throw err;
//     if (result.length == 0) {
//       res.status(404).json({message: 'vendorId not found'});
//     } else {
//       res.json(result);
//     }
//   });
// });
///////////////////////////////////////////////////////////////////////

//create vendor shop address
app.post("/neerseva/api/v1/user/addresses", (req, res) => {
  let shop = req.body;
  let sql = "INSERT INTO shop SET ?";
  connection.query(sql, shop, (err, result) => {
    if (err) throw err;
    res.send(result);
  });
});

///////////////////////////////////////////////////////////////////
// Update address by venodr

app.put("/neerseva/api/v1/addresses/by/user/:userId", (req, res) => {
  connection.query(
    "UPDATE `shop` SET  `name` = ?,  `image_id` =?, `type` =?, `address_id` = ?, `code` = ?, `branch` = ?, `id` = ?  where `user_id` = ?",
    [
      req.body.name,
      req.body.image_id,
      req.body.type,
      req.body.address_id,
      req.body.code,
      req.body.branch,
      req.body.id,
      req.body.user_id
    ],
    function(error, results, fields) {
      if (error) throw error;
      res.send(JSON.stringify(results));
    }
  );
});

///////////////////////////////////////
//// delete venodr address
app.delete("//neerseva/api/v1/addresses/:userId", (req, res) => {
  let user_id = (userId = req.params.userId);
  connection.query("DELETE FROM `shop` WHERE `user_id`=?", [user_id], function(
    error,
    results,
    fields
  ) {
    if (error) throw error;
    res.send(results);
  });
});





//makeDefault address
app.put("/neerseva/api/v1/makeDefault", (req, res) => {
  let user_id = req.body.user_id;
  let address_id = req.body.address_id;
  let sql = "UPDATE `address` SET  `is_default` = ? where `user_id` = ? and `is_default` = ?";
  // let sql = "SELECT * FROM address where user_id = ? and is_default = ?"
  // let sql = "INSERT INTO shop SET ?";
  connection.query(sql, ["false",user_id,"true"], (err, result) => {
    if (err) throw err;
    // res.send(result);
    let sql = "UPDATE `address` SET  `is_default` = ? where `id` = ?";
    connection.query(sql, ["true",address_id], (err, result) => {
      if (err) throw err;
      res.send(result);
    })
  });
});




