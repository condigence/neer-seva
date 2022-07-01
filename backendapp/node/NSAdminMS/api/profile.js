var connection = require("../db");
var app = require("../app");

////////////////////////////////////////////////////////////////////////
/**
 * @swagger
 * /neerseva/api/v1/profile:
 *  get:
 *    description: Use to request all stocks
 *    responses:
 *      '200':
 *        description: A successful response
 */

// Get all profiles
app.get("/neerseva/api/v1/profile", (req, res) => {
  let sql = "SELECT * FROM profile";
  connection.query(sql, (err, results) => {
    if (err) throw err;
    res.json(results);
  });
});
//////////////////////////////////////////////////////////////////////
// Get profile by profileId
app.get("/neerseva/api/v1/profile/:id", (req, res) => {
  let id = req.params.id;
  let sql = "SELECT * FROM profile WHERE id = " + id;
  connection.query(sql, (err, result) => {
    if (err) throw err;
    if (result.length == 0) {
      res.status(404).json({
        message: "profile id not found"
      });
    } else {
      res.json(result);
    }
  });
});
///////////////////////////////////////////////////////////////////////////////
//Save user profile
app.post("/neerseva/api/v1/profile", (req, res) => {
  let profile = req.body;
  let sql = "INSERT INTO profile SET ?";
  connection.query(sql, profile, (err, result) => {
    if (err) throw err;
    res.send(result);
  });
});
/////////////////////////////////////////////////////////////////////
// update user_profile
app.put("/neerseva/api/v1/profile/:id", (req, res) => {


console.log("Inside update profile"+" : "+ req.body.user_id +" : "+ req.body.image_id +" : "+  req.body.id)

  connection.query(
    "UPDATE `profile` SET  `user_id` = ?,  `image_id` = ?  where `id` = ?",
    [
      req.body.user_id,
     // req.body.full_name,
      req.body.image_id,
      req.body.id
    ],
    function(error, results, fields) {
      if (error) throw error;
      res.send(JSON.stringify(results));
    }
  );
});
/////////////////////////////////////////////////////////////////////
// delete user profile
app.delete("/neerseva/api/v1/profile/:id", (req, res) => {
  let id = req.params.id;
  connection.query("DELETE FROM `profile` WHERE `id`=?", [id], function(
    error,
    results,
    fields
  ) {
    if (error) throw error;
    res.send(results);
  });
});
