var connection = require("../db");
var app = require("../app");

/////////////////////// API for User /////////////////////
/////////////////////////////////////////////////////////

/**
 * @swagger
 * /neerseva/api/v1/Users:
 *  get:
 *    description: Use to request all users
 *    responses:
 *      '200':
 *        description: A successful response
 */

//Get All users
app.get("/neerseva/api/v1/users", (req, res) => {
  let sql = "SELECT * FROM user";
  connection.query(sql, (err, results) => {
    if (err) throw err;
    res.json(results);
  });
});

/////////////////////////////////////////////////////////

// Get USER by Id
app.get("/neerseva/api/v1/users/:id", (req, res) => {
  let id = req.params.id;
  let sql = "SELECT * FROM user WHERE id = " + id;
  connection.query(sql, (err, result) => {
    if (err) throw err;
    if (result.length == 0) {
      res.status(404).json({
        message: "user id not found",
      });
    } else {
      res.json(result);
    }
  });
});

//////////////////////////////////////////////////////////////////////
//Save user
app.post("/neerseva/api/v1/users", (req, res) => {
  let user = req.body;
  let sql = "INSERT INTO user SET ?";
  connection.query(sql, user, (err, result) => {
    if (err) throw err;
    res.send(result);
  });
});
///////////////////////////////////////////////////////////////////
// update User
app.put("/neerseva/api/v1/users/:id", (req, res) => {
  connection.query(
    "UPDATE `user` SET  `name` = ?, `contact` =?, `email` =?, `type` =?, `is_deleted` =?, `is_active` =?,  `date_created` =?, `password` =? where `id` = ?",
    [
      req.body.name,
      req.body.contact,
      req.body.email,
      req.body.type,
      req.body.is_deleted,
      req.body.is_active,
      req.body.date_created,
      req.body.password,
      req.body.id,
    ],
    function (error, results, fields) {
      if (error) throw error;
      res.send(JSON.stringify(results));
    }
  );
});
///////////////////////////////////////////////////////////////////
// Delete User
app.delete("/neerseva/api/v1/users/:id", (req, res) => {
  let id = req.params.id;
  connection.query("DELETE FROM `user` WHERE `id`=?", [id], function (
    error,
    results,
    fields
  ) {
    if (error) throw error;
    res.send(results);
  });
});

/////////////////////////////////////////////////////////
// Get Total number of users
app.get("/neerseva/api/v1/usercounts", (req, res) => {
  let sql = "SELECT COUNT (*) AS NumberOfUsers  FROM user";
  connection.query(sql, (err, results) => {
    if (err) throw err;
    res.json(results);
  });
});

//////////////////////////////////////////////////////////////////////////////////
// Get Customer count
app.get("/neerseva/api/v1/customerscount", (req, res) => {
  let sql =
    'select count(*) AS numberOfCustomer FROM user where user.type = "CUSTOMER"';
  connection.query(sql, (err, results) => {
    if (err) throw err;
    res.json(results);
  });
});
///////////////////////////////////////////////////////////////////////////////
// Get vendor Count
app.get("/neerseva/api/v1/vendorscount", (req, res) => {
  let sql =
    'select count(*) AS numberOfVendors FROM user where user.type = "VENDOR"';
  connection.query(sql, (err, results) => {
    if (err) throw err;
    res.json(results);
  });
});
